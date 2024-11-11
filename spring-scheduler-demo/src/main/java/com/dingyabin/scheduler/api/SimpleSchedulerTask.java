package com.dingyabin.scheduler.api;

/**
 * @author 丁亚宾
 * Date: 2024/11/12.
 * Time:0:00
 */
public abstract class SimpleSchedulerTask {


    public abstract String getUniqueTaskName();


    public abstract void execute();


}
