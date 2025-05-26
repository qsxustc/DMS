package com.dms.springboot.controller.dto;

import com.dms.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String name;//姓名
    private String avatarurl;
    private String token;
    private String role;
    private List<Menu> menus;
    private Integer mask;
}
