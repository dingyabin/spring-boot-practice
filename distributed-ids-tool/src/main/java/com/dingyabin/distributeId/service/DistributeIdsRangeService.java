package com.dingyabin.distributeId.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.distributeId.model.DistributeIdsRange;
import com.dingyabin.distributeId.mapper.DistributeIdsRangeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class DistributeIdsRangeService extends ServiceImpl<DistributeIdsRangeMapper, DistributeIdsRange> {


    public static final String DEFAULT_TYPE = "DEFAULT";


    public List<DistributeIdsRange> getTotalDistributeIdsRanges(){
        return list();
    }


    public DistributeIdsRange getRangeByBiz(String bizType) {
        LambdaQueryWrapper<DistributeIdsRange> wrapper = Wrappers.lambdaQuery(DistributeIdsRange.class)
                .eq(DistributeIdsRange::getBizType, bizType);
        return getOne(wrapper);
    }


    public int updateRangeStep(DistributeIdsRange idsRange) {
        LambdaUpdateWrapper<DistributeIdsRange> updateWrapper = Wrappers.lambdaUpdate(DistributeIdsRange.class)
                .set(DistributeIdsRange::getMaxId, idsRange.getMaxId() + idsRange.getStep())
                .setIncrBy(DistributeIdsRange::getVersion, 1)
                .eq(DistributeIdsRange::getId, idsRange.getId())
                .eq(DistributeIdsRange::getBizType, idsRange.getBizType())
                .eq(DistributeIdsRange::getVersion, idsRange.getVersion());
        return getBaseMapper().update(updateWrapper);
    }

}




