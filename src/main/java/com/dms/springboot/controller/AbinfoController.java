package com.dms.springboot.controller;


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
 * VIEW 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/abinfo")
public class AbinfoController {

    @Autowired
    private IAbinfoService abinfoService;

    @Autowired
    private IAbbService abbService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Abinfo abinfo) {
        abinfoService.saveOrUpdate(abinfo);
        return Result.success();
    }

    @PostMapping("/check")
    public Result updatecheck(@RequestBody Abinfo abinfo){
        Abb abb=new Abb();
        BeanUtil.copyProperties(abinfo, abb, true);
        abbService.saveOrUpdate(abb);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        abinfoService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        abinfoService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(abinfoService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(abinfoService.getById(id));
    }


    @GetMapping("/time")
    public Result findAll(@RequestParam(required = false) String start, @RequestParam(required = false) String end
    ,@RequestParam(required = true) Integer leaderid
                          ) {
        QueryWrapper<Abinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leaderid",leaderid);

        if (StrUtil.isNotBlank(start)) {
            // where time >= start
            queryWrapper.ge("time", start);
        }
        if (StrUtil.isNotBlank(end)) {
            // where time <= end
            queryWrapper.le("time", end);
        }
        return Result.success(abinfoService.list(queryWrapper));
    }

    @GetMapping("/time/driver")
    public Result findAlldriver(@RequestParam(required = false) String start, @RequestParam(required = false) String end
                                ,@RequestParam(required = true) Integer driverid
    ) {
        QueryWrapper<Abinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",driverid);

        if (StrUtil.isNotBlank(start)) {
            // where time >= start
            queryWrapper.ge("time", start);
        }
        if (StrUtil.isNotBlank(end)) {
            // where time <= end
            queryWrapper.le("time", end);
        }
        return Result.success(abinfoService.list(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String abtype,
                           @RequestParam(defaultValue = "") String licenseid,
                           @RequestParam(defaultValue = "") String cartype,
                           @RequestParam String jiluyuanid
    ) {
        QueryWrapper<Abinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name))
            queryWrapper.like("name", name);
        if (!"".equals(abtype))
            queryWrapper.like("abtype", abtype);
        if (!"".equals(licenseid))
            queryWrapper.like("licenseid", licenseid);
        if (!"".equals(cartype))
            queryWrapper.like("cartype", cartype);
        queryWrapper.eq("leaderid",jiluyuanid);
        return Result.success(abinfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    @GetMapping("/driver")
    public Result findPagedriver(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String abtype,
                           @RequestParam(defaultValue = "") String licenseid,
                           @RequestParam(defaultValue = "") String cartype,
                           @RequestParam String driverid
    ) {
        QueryWrapper<Abinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name))
            queryWrapper.like("name", name);
        if (!"".equals(abtype))
            queryWrapper.like("abtype", abtype);
        if (!"".equals(licenseid))
            queryWrapper.like("licenseid", licenseid);
        if (!"".equals(cartype))
            queryWrapper.like("cartype", cartype);
        queryWrapper.eq("userid",driverid);
        return Result.success(abinfoService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}