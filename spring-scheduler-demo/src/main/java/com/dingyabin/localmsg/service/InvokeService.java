package com.dingyabin.localmsg.service;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.dingyabin.localmsg.model.common.InvokeContext;
import com.dingyabin.localmsg.model.entity.LocalMessageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isSynchronizationActive;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Slf4j
@Service
public class InvokeService implements InitializingBean, DisposableBean {


    private ExecutorService executorService;


    @Resource
    private LocalMessageRecordService localMessageRecordService;


    @Override
    public void afterPropertiesSet() {
        int max = Runtime.getRuntime().availableProcessors() / 2;
        int core = max / 2;
        executorService = new ThreadPoolExecutor(
                core, max, 2L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNamePrefix("INVOKE_SERVICE_THREAD_POOL-").build());
    }


    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
        if (!executorService.awaitTermination(2, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
        }
    }


    public void invokeLocalMessage(LocalMessageRecord localMessage, boolean sync) {
        localMessageRecordService.saveLocalMessageRecord(localMessage);

        if (isSynchronizationActive()) {
            registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doInvoke(localMessage, sync);
                }
            });
        }
        doInvoke(localMessage, sync);
    }


    public void doInvoke(LocalMessageRecord localMessage, boolean sync) {
        if (sync) {
            execute(localMessage);
            return;
        }
        executorService.execute(() -> execute(localMessage));
    }


    public void execute(LocalMessageRecord localMessage) {
        try {
            InvokeContext invokeContext = JSONObject.parseObject(localMessage.getInvokeCtx(), InvokeContext.class);

            Class<?> targetClass = Class.forName(invokeContext.getClassName());

            Class<?>[] parameterType = parseParameterType(invokeContext.getParameterTypes());

            Method method = ReflectionUtils.findMethod(targetClass, invokeContext.getMethodName(), parameterType);

            if (method == null) {
                throw new NoSuchMethodException(invokeContext.getMethodName() + "#" + Arrays.toString(parameterType));
            }

            Object proxyBean = SpringUtil.getBean(targetClass);
            Object targetBean = AopProxyUtils.getSingletonTarget(proxyBean);
            if (targetBean != null) {
                proxyBean = targetBean;
            }
            method.invoke(proxyBean, parseArgObjects(invokeContext.getMethodArgs(), parameterType));
            localMessageRecordService.updateLocalMessageRecordSuccess(localMessage.getId());
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
            log.error("doInvoke fail, id={}", localMessage.getId(), e);
            updateFailed(localMessage, e);
        } catch (Throwable e) {
            log.error("doInvoke error, id={}", localMessage.getId(), e);
            updateRetry(localMessage, e);
        }
    }


    private void updateFailed(LocalMessageRecord localMessage, Exception e) {
        localMessageRecordService.updateLocalMessageRecordFail(localMessage.getId(), e.getMessage());
    }


    private void updateRetry(LocalMessageRecord localMessage, Throwable e) {
        Integer retryTime = localMessage.getRetryTime();
        if (retryTime + 1 > localMessage.getMaxRetryTime()) {
            localMessageRecordService.updateLocalMessageRecordFail(localMessage.getId(), e.getMessage());
            return;
        }
        localMessageRecordService.updateLocalMessageRecordRetry(localMessage.getId(), e.getMessage());
    }


    private static Object[] parseArgObjects(List<String> methodArgs, Class<?>[] parameterType) {
        Object[] args = new Object[methodArgs.size()];
        for (int i = 0; i < methodArgs.size(); i++) {
            args[i] = JSONObject.parseObject(methodArgs.get(i), parameterType[i]);
        }
        return args;
    }


    private static Class<?>[] parseParameterType(List<String> parameterTypes) throws ClassNotFoundException {
        Class<?>[] array = new Class[parameterTypes.size()];
        for (int i = 0; i < parameterTypes.size(); i++) {
            array[i] = Class.forName(parameterTypes.get(i));
        }
        return array;
    }


}
