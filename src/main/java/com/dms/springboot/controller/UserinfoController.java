package com.dms.springboot.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dms.springboot.service.IUserinfoService;
import com.dms.springboot.entity.Userinfo;

import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * VIEW 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/userinfo")
public class UserinfoController {

    @Resource
    private IUserinfoService userinfoService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Userinfo userinfo) {
        userinfoService.saveOrUpdate(userinfo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userinfoService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userinfoService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userinfoService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userinfoService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userinfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Userinfo> list = userinfoService.list();

        // 通过工具类创建writer 写出到磁盘路径
        //  ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id", "编号");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("driveyear", "驾龄");
        writer.addHeaderAlias("leaderid", "记录员id");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("role", "角色");
        writer.addHeaderAlias("address", "地址");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


}

