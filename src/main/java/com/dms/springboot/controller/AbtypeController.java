package com.dms.springboot.controller;


import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.dms.springboot.service.IAbtypeService;
import com.dms.springboot.entity.Abtype;

import org.springframework.web.bind.annotation.RestController;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.dms.springboot.entity.Abb;
import com.dms.springboot.entity.Article;
import com.dms.springboot.service.IAbbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.dms.springboot.service.IAbinfoService;
import com.dms.springboot.entity.Abinfo;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.common.Result;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/abtype")
public class AbtypeController {

    @Resource
    private IAbtypeService abtypeService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Abtype abtype) {
        abtypeService.saveOrUpdate(abtype);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        abtypeService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        abtypeService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(abtypeService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(abtypeService.getById(id));
    }

//    @GetMapping("/page")
//    public Result findPage(@RequestParam Integer pageNum,
//                                @RequestParam Integer pageSize) {
//        QueryWrapper<Abtype> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return Result.success(abtypeService.page(new Page<>(pageNum, pageSize), queryWrapper));
//    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String abtype
    ) {
        QueryWrapper<Abtype> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(abtype))
            queryWrapper.like("abtype",abtype);
        return Result.success(abtypeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

