package com.dingyabin.distributeId.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.distributeId.mapper.LeafAllocMapper;
import com.dingyabin.distributeId.model.LeafAlloc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * @description 针对表【leaf_alloc】的数据库操作Service实现
 * @createDate 2025-07-10 23:24:46
 */
@Service
public class LeafAllocService extends ServiceImpl<LeafAllocMapper, LeafAlloc> {


    public List<LeafAlloc> getAllLeafAlloc() {
        return list();
    }


    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        LambdaUpdateWrapper<LeafAlloc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("max_id = max_id + step").eq(LeafAlloc::getBizTag, tag);
        update(wrapper);
        LambdaQueryWrapper<LeafAlloc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeafAlloc::getBizTag, tag);
        return getOne(queryWrapper);
    }


    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        LambdaUpdateWrapper<LeafAlloc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("max_id = max_id +" + leafAlloc.getStep()).eq(LeafAlloc::getBizTag, leafAlloc.getBizTag());
        update(wrapper);
        LambdaQueryWrapper<LeafAlloc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LeafAlloc::getBizTag, leafAlloc.getBizTag());
        return getOne(queryWrapper);
    }


    public List<String> getAllTags() {
        List<LeafAlloc> leafAlloc = getAllLeafAlloc();
        return leafAlloc.stream().map(LeafAlloc::getBizTag).collect(Collectors.toList());
    }

}




