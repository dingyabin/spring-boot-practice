package com.example.springsessiondemo.transform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsessiondemo.entity.SampleData;
import com.example.springsessiondemo.mapper.SampleDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 丁亚宾
 * @description 针对表【sample_data(测试表)】的数据库操作Service实现
 * @createDate 2024-07-03 22:16:50
 */
@Service("sampleDataService")
public class SampleDataServiceImpl extends ServiceImpl<SampleDataMapper, SampleData> {



    public List<SampleData> getSimDataByIdOrders(long startId, long size) {
        LambdaQueryWrapper<SampleData> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.gt(SampleData::getId, startId).orderByAsc(SampleData::getId).last("limit " + size);
        return baseMapper.selectList(lambdaQuery);
    }


}




