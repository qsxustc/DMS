package com.dms.springboot.controller;


import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.dms.springboot.service.IAbbService;
import com.dms.springboot.entity.Abb;

import org.springframework.web.bind.annotation.RestController;

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
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/abb")
public class AbbController {

    @Resource
    private IAbbService abbService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Abb abb) {
        abbService.saveOrUpdate(abb);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        abbService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        abbService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(abbService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(abbService.getById(id));
    }



}

