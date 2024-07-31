package com.dingyabin.springsecuritydemo.mapper;

import com.dingyabin.springsecuritydemo.entity.SysRoleAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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




