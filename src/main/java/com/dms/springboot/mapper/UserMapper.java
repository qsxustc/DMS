package com.dms.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.controller.dto.UserPasswordDTO;
import com.dms.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 该表是存储用户的有关信息，所有用户的信息全在这个表中 Mapper 接口
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-21
 */

public interface UserMapper extends BaseMapper<User> {
    Page<User> findPage(Page<User> page, @Param("name") String name, @Param("phone") String phone, @Param("role") String role);

    @Update("update user set password = #{newPassword} where username = #{username}")
    int updatePasswordQiang(UserPasswordDTO userPasswordDTO);
}
