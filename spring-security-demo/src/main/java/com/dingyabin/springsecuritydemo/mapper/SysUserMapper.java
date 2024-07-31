package com.dingyabin.springsecuritydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyabin.springsecuritydemo.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2024-07-31 15:02:06
* @Entity generator.domain.SysUser
*/
public interface SysUserMapper extends BaseMapper<SysUser> {


    int insertBatch(@Param("list") List<SysUser> list);

}




