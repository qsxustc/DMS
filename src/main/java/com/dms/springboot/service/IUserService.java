package com.dms.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.controller.dto.UserDTO;
import com.dms.springboot.controller.dto.UserPasswordDTO;
import com.dms.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 该表是存储用户的有关信息，所有用户的信息全在这个表中 服务类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-21
 */
public interface IUserService extends IService<User> {
    Page<User> findPage(Page<User> page, String name, String phone, String role);

    UserDTO login(UserDTO userDTO);

    void updatePasswordQiang(UserPasswordDTO userPasswordDTO);

}
