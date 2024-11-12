package com.dingyabin.scheduler.api;

/**
 * @author 丁亚宾
 * Date: 2024/11/12.
 * Time:0:00
 */
public abstract class SimpleSchedulerTask implements Runnable {

    public abstract String getUniqueTaskName();

}
