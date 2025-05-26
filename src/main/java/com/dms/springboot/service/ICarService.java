package com.dms.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.entity.Car;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 本表存储的是车的相关信息 服务类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
public interface ICarService extends IService<Car> {

    Page<Car> driverfindcarpage(Page<Car> objectPage, Integer driverid);
}
