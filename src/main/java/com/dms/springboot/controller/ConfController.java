package com.dms.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.common.Result;
import com.dms.springboot.entity.Conf;
import com.dms.springboot.service.IConfService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 系统配置文件 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/conf")
public class ConfController {

    @Resource
    private IConfService confService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Conf conf) {
        confService.saveOrUpdate(conf);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        confService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        confService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(confService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(confService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Conf> queryWrapper = new QueryWrapper<>();
        return Result.success(confService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}