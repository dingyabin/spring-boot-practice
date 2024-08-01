package com.dingyabin.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyabin.security.entity.SysRoleAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sys_role_authority】的数据库操作Mapper
* @createDate 2024-07-31 21:17:38
* @Entity com.dingyabin.springsecuritydemo.entity.SysRoleAuthority
*/
public interface SysRoleAuthorityMapper extends BaseMapper<SysRoleAuthority> {


    int insertBatch(@Param("list") List<SysRoleAuthority> list);

}




