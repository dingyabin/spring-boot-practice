package com.example.springsessiondemo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsessiondemo.entity.SnowflakWorkerId;
import com.example.springsessiondemo.mapper.SnowflakWorkerIdMapper;
import org.springframework.stereotype.Service;

/**
* @author 丁亚宾
* @description 针对表【snowflak_worker_id】的数据库操作Service实现
* @createDate 2024-06-26 13:28:55
*/
@Service("snowflakWorkerIdService")
public class SnowflakWorkerIdServiceImpl extends ServiceImpl<SnowflakWorkerIdMapper, SnowflakWorkerId> {

}




