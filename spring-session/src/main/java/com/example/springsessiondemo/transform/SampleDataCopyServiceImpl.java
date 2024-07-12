package com.example.springsessiondemo.transform;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsessiondemo.entity.SampleDataCopy;
import com.example.springsessiondemo.mapper.SampleDataCopyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sample_data_copy(测试表)】的数据库操作Service实现
* @createDate 2024-07-03 22:16:50
*/
@Service("sampleDataCopyService")
public class SampleDataCopyServiceImpl extends ServiceImpl<SampleDataCopyMapper, SampleDataCopy> {


    public void saveBatchByMappers(List<SampleDataCopy> list){
        baseMapper.insertBatch(list);
    }

}




