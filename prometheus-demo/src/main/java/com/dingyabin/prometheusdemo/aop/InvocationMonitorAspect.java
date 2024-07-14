package com.dingyabin.prometheusdemo.aop;

import com.dingyabin.prometheusdemo.aop.enums.MonitorReportType;
import com.dingyabin.prometheusdemo.aop.model.InvocationModel;
import com.dingyabin.prometheusdemo.service.InvocationMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Resource
    private ReturnVerifyService verifyService;


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
            extracted(monitorReport, new InvocationModel(joinPoint, monitorReport), startTime, success, proceed);
        }
    }


    private void extracted(MonitorReport monitorReport, InvocationModel invocationModel, long startTime, boolean success, Object proceed) {
        try {
            for (MonitorReportType reportType : monitorReport.value()) {
                if (reportType == MonitorReportType.COUNTER) {
                    invocationMonitorService.counterIncr(invocationModel);
                    continue;
                }
                if (reportType == MonitorReportType.DURATION_SUMMARY) {
                    invocationMonitorService.timerDuration(invocationModel, (System.currentTimeMillis() - startTime));
                    continue;
                }
                if (reportType == MonitorReportType.COUNTER_WITH_RES) {
                    //获取校验key
                    String retCheckKey = monitorReport.resVerifyKey();
                    if (!StringUtils.hasLength(retCheckKey)) {
                        retCheckKey = monitorReport.name();
                    }
                    success = success && verifyService.retVerify(retCheckKey, proceed);
                    invocationMonitorService.counterWithRetIncr(invocationModel, success);
                }
            }
        } catch (Exception e) {
            log.error("上报数据异常....", e);
        }
    }


}
