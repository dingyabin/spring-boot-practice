package com.dingyabin.prometheusdemo.aop.model;

import com.dingyabin.prometheusdemo.aop.MonitorReport;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ClassUtils;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:23:14
 */
@Getter
@Setter
public class InvocationModel {

    private String name;

    private String shortClassName;

    private String methodName;


    public InvocationModel(ProceedingJoinPoint joinPoint, MonitorReport monitorReport){
        this.name = monitorReport.name();
        shortClassName = ClassUtils.getShortName(joinPoint.getTarget().getClass().getName());
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            methodName = ((MethodSignature) signature).getMethod().getName();
        }
    }


}
