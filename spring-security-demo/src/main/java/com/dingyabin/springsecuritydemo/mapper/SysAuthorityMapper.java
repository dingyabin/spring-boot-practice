package com.dingyabin.springsecuritydemo.mapper;

import com.dingyabin.springsecuritydemo.entity.SysAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sys_authority】的数据库操作Mapper
* @createDate 2024-07-31 21:17:38
* @Entity com.dingyabin.springsecuritydemo.entity.SysAuthority
*/
public interface SysAuthorityMapper extends BaseMapper<SysAuthority> {


    int insertBatch(@Param("list") List<SysAuthority> list);


    List<SysAuthority> selectSysAuthorityByUserId(@Param("userId") Long userId);


}







