package com.dingyabin.prometheusdemo.controller;

import com.dingyabin.prometheusdemo.report.aop.MonitorReport;
import com.dingyabin.prometheusdemo.report.enums.MonitorReportType;
import com.dingyabin.prometheusdemo.service.InvocationMonitorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.dingyabin.prometheusdemo.report.ReturnVerifyHelper.STR_NOT_NULL;
import static com.dingyabin.prometheusdemo.report.enums.MonitorReportType.COUNTER_WITH_RES;
import static com.dingyabin.prometheusdemo.report.enums.MonitorReportType.DURATION_SUMMARY;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:0:30
 */
@RestController
public class BizController {


    @Resource
    private InvocationMonitorService invocationMonitorService;


    @MonitorReport(name = "biz_controller", value = {COUNTER_WITH_RES, DURATION_SUMMARY}, resVerifyKey = STR_NOT_NULL)
    @RequestMapping("biz")
    public String biz(@RequestParam(value = "param", required = false) String param) {
        if ("1".equals(param)) {
            throw new RuntimeException();
        }
        return "ok";
    }


    @RequestMapping("bad")
    public String bizBad() throws InterruptedException {
        while (true) {
            if (false) {
                break;
            }
        }
        return "ok";
    }


    @MonitorReport(name = "biz_test_duration", value = MonitorReportType.DURATION_SUMMARY)
    @RequestMapping("test1")
    public String test11() {
        return "ok";
    }


    @RequestMapping("test2")
    public String test2() {
        invocationMonitorService.timerDuration("biz_Test_with_ret", 100 );
        return "ok";
    }

}
