package com.dingyabin.localmsg.annotation;

import com.alibaba.fastjson2.JSON;
import com.dingyabin.localmsg.model.common.InvokeContext;
import com.dingyabin.localmsg.model.entity.LocalMessageRecord;
import com.dingyabin.localmsg.service.InvokeService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LocalMessageAopProcessor {


    @Resource
    private InvokeService invokeService;


    @Around("@annotation(com.dingyabin.localmsg.annotation.LocalMessage)")
    public Object doAroundLocalMessageMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return joinPoint.proceed();
        }

        Method method = ((MethodSignature) signature).getMethod();
        LocalMessage localMessage = AnnotationUtils.findAnnotation(method, LocalMessage.class);
        if (localMessage == null) {
            return joinPoint.proceed();
        }

        List<String> parameterTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        List<String> methodArgs = Arrays.stream(joinPoint.getArgs()).map(JSON::toJSONString).collect(Collectors.toList());
        InvokeContext invokeContext = InvokeContext.builder()
                .methodName(method.getName())
                .className(method.getDeclaringClass().getName())
                .parameterTypes(parameterTypes)
                .methodArgs(methodArgs)
                .build();

        LocalMessageRecord localMessageRecord = new LocalMessageRecord(localMessage.bizType(), invokeContext, localMessage.maxRetryTime());

        invokeService.invokeLocalMessage(localMessageRecord, localMessage.sync());

        return null;
    }


}
