package com.dingyabin.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyabin.security.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sys_role】的数据库操作Mapper
* @createDate 2024-07-31 21:17:38
* @Entity com.dingyabin.springsecuritydemo.entity.SysRole
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {


    int insertBatch(@Param("list") List<SysRole> list);

}




