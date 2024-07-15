package com.dingyabin.prometheusdemo.report.model;

import com.dingyabin.prometheusdemo.report.aop.MonitorReport;
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

    private MonitorReport monitorReport;

    private String shortClassName;

    private String methodName;

    /**
     * 方法开始执行时间
     */
    private long startTime;

    /**
     * 方法是否成功
     */
    private boolean success;

    /**
     * 方法返回结果
     */
    private Object result;


    public InvocationModel(ProceedingJoinPoint joinPoint, MonitorReport monitorReport, long startTime, boolean success, Object result) {
        this.monitorReport = monitorReport;
        this.shortClassName = ClassUtils.getShortName(joinPoint.getTarget().getClass().getName());
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            this.methodName = ((MethodSignature) signature).getMethod().getName();
        }
        this.startTime = startTime;
        this.success = success;
        this.result = result;
    }


}
