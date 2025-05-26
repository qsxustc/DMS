package com.dms.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.common.Constants;
import com.dms.springboot.common.Result;
import com.dms.springboot.controller.dto.UserDTO;
import com.dms.springboot.controller.dto.UserPasswordDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dms.springboot.service.IUserService;
import com.dms.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 该表是存储用户的有关信息，所有用户的信息全在这个表中 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        if(user.getPassword()==null)
            user.setPassword("21232f297a57a5a743894a0e4a801fc3");//admin 默认密码
        userService.saveOrUpdate(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String phone,
                           @RequestParam(defaultValue = "") String role
    ) {
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), name, phone, role));
    }

    @GetMapping("/page/jiluyuan")
    public Result findPagejiluyuan(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "") String phone,
                                   @RequestParam(defaultValue = "") String role,
                                   @RequestParam(defaultValue = "") String leaderid

    ) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("leaderid",leaderid);
        if(!"".equals(name))
            queryWrapper.like("name",name);
        if(!"".equals(phone))
            queryWrapper.like("phone",phone);
        if(!"".equals(role))
            queryWrapper.like("role",role);
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        userService.updatePasswordQiang(userPasswordDTO);
        return Result.success();
    }

    @PostMapping("/password/qiang")//强行通过管理员渠道恢复密码
    public Result passwordqiang(@RequestBody UserPasswordDTO userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        userService.updatePasswordQiang(userPasswordDTO);
        return Result.success();
    }


    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword("e10adc3949ba59abbe56e057f20f883e");
            user.setName(row.get(1).toString());
            user.setAge(Integer.valueOf(row.get(2).toString()));
            user.setDriveyear(row.get(3).toString());
            user.setLeaderid(Integer.valueOf(row.get(4).toString()));
            user.setPhone(row.get(5).toString());
            user.setRole(row.get(6).toString());
            user.setAddress(row.get(7).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return Result.success(true);
    }
}

