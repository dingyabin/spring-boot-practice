package com.dingyabin.prometheusdemo.aop;

import com.dingyabin.prometheusdemo.aop.enums.MonitorReportType;
import com.dingyabin.prometheusdemo.aop.model.InvocationModel;
import com.dingyabin.prometheusdemo.service.InvocationMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * Date: 2024/7/14.
 * Time:11:06
 */
@Slf4j
@Service
@Aspect
public class InvocationMonitorAspect {

    @Resource
    private InvocationMonitorService invocationMonitorService;


    @Around("@annotation(monitorReport)")
    public Object monitorReport(ProceedingJoinPoint joinPoint, MonitorReport monitorReport) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            success = true;
            return proceed;
        } finally {
            submitReport(monitorReport, new InvocationModel(joinPoint, monitorReport, startTime, success, proceed));
        }
    }


    private void submitReport(MonitorReport monitorReport, InvocationModel invocationModel) {
        try {
            for (MonitorReportType monitorReportType : monitorReport.value()) {
                monitorReportType.submitReport(invocationMonitorService, invocationModel);
            }
        } catch (Exception e) {
            log.error("上报数据异常....", e);
        }
    }


}
