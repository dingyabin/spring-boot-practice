package com.dingyabin.prometheusdemo.aop;

import com.dingyabin.prometheusdemo.aop.enums.MonitorReportType;
import com.dingyabin.prometheusdemo.aop.model.InvocationModel;
import com.dingyabin.prometheusdemo.service.InvocationMonitorService;
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
@Service
@Aspect
public class InvocationMonitorAspect {


    @Resource
    private InvocationMonitorService invocationMonitorService;


    @Around("@annotation(monitorReport)")
    public Object monitorReport(ProceedingJoinPoint joinPoint, MonitorReport monitorReport) throws Throwable {
        InvocationModel invocationModel = new InvocationModel(joinPoint, monitorReport);
        MonitorReportType reportType = monitorReport.value();
        long startTime = System.currentTimeMillis();
        boolean success = false;
        try {
            Object proceed = joinPoint.proceed();
            success = true;
            return proceed;
        } finally {
            if (reportType == MonitorReportType.COUNTER) {
                invocationMonitorService.counterIncr(invocationModel);
            }
            else if (reportType == MonitorReportType.COUNTER_WITH_RES) {
                invocationMonitorService.counterWithRetIncr(invocationModel, success);
            }
            else if (reportType == MonitorReportType.DURATION_SUMMARY) {
                invocationMonitorService.timerDuration(invocationModel, (System.currentTimeMillis() - startTime));
            }
        }
    }


}
