package com.dingyabin.localmsg.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.dingyabin.localmsg.entity.LocalMessageRecord;
import com.dingyabin.localmsg.model.InvokeContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvokeService  {


    @Resource
    private LocalMessageRecordService localMessageRecordService;


    public void test(){

        try {
            LocalMessageRecord one = localMessageRecordService.getOne(null);
            String invokeCtx = one.getInvokeCtx();
            InvokeContext invokeContext = JSONObject.parseObject(invokeCtx, InvokeContext.class);

            Class<?> targetClass = Class.forName(invokeContext.getClassName());

            List<String> parameterTypes = invokeContext.getParameterTypes();

            Class<?>[] array = new Class[parameterTypes.size()];
            for (int i = 0; i < parameterTypes.size(); i++) {
                array[i] = Class.forName(parameterTypes.get(i));
            }

            List<String> methodArgs = invokeContext.getMethodArgs();

            Object[] args = new Object[methodArgs.size()];

            for (int i = 0; i < methodArgs.size(); i++) {
                args[i] = JSONObject.parseObject(methodArgs.get(i), array[i]);
            }

            Method method = ReflectionUtils.findMethod(targetClass, invokeContext.getMethodName(), array);

            Object bean = SpringUtil.getBean(targetClass);
            Object singletonTarget = AopProxyUtils.getSingletonTarget(bean);
            if (singletonTarget != null) {
                bean = singletonTarget;
            }
            method.invoke(bean, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
