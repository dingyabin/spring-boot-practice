package com.dingyabin.distributeId.service.api;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:16:24
 */
public interface IDistributeId {


    Long nextId();


    Long nextId(String bizType);

}
