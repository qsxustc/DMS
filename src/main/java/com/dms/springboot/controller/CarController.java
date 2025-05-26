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

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.dms.springboot.service.ICarService;
import com.dms.springboot.entity.Car;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 本表存储的是车的相关信息 前端控制器
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/car")
public class CarController {

    @Resource
    private ICarService carService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Car car) {
        carService.saveOrUpdate(car);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        carService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(carService.list());
    }


    @GetMapping("/driver")
    public Result driverfindcar(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam Integer driverid
                                )
    {

        return Result.success(carService.driverfindcarpage(new Page<>(pageNum, pageSize),driverid));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(carService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String dutyofficerid,
                           @RequestParam(defaultValue = "") String  color,
                           @RequestParam(defaultValue = "")String type,
                           @RequestParam(defaultValue = "")String licenseid
                           ) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(color))
            queryWrapper.like("color",color);
        if(!"".equals(dutyofficerid))
            queryWrapper.like("dutyofficerid",dutyofficerid);
        if(!"".equals(type))
            queryWrapper.like("type",type);
        if(!"".equals(licenseid))
            queryWrapper.like("licenseid",licenseid);
        return Result.success(carService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

