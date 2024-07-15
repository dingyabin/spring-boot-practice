package com.dingyabin.prometheusdemo.report.enums;

import com.dingyabin.prometheusdemo.report.ReturnVerifyHelper;
import com.dingyabin.prometheusdemo.report.model.InvocationModel;
import com.dingyabin.prometheusdemo.service.InvocationMonitorService;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:22:18
 */
public enum MonitorReportType {

    COUNTER {
        @Override
        public void submitReport(InvocationMonitorService monitorService, InvocationModel model) {
            monitorService.counterIncr(model);
        }
    },


    COUNTER_WITH_RES {
        @Override
        public void submitReport(InvocationMonitorService monitorService, InvocationModel model) {
            //获取校验key
            String resVerifyKey = model.getMonitorReport().resVerifyKey();
            if (!StringUtils.hasLength(resVerifyKey)) {
                resVerifyKey = model.getMonitorReport().name();
            }
            //校验结果是否符合预期
            boolean success = model.isSuccess() && ReturnVerifyHelper.retVerify(resVerifyKey, model.getResult());
            monitorService.counterWithRetIncr(model, success);
        }
    },

    DURATION_SUMMARY {
        @Override
        public void submitReport(InvocationMonitorService monitorService, InvocationModel model) {
            long cost = System.currentTimeMillis() - model.getStartTime();
            monitorService.timerDuration(model, cost);
        }
    };


    public abstract void submitReport(InvocationMonitorService monitorService, InvocationModel invocationModel);

}
