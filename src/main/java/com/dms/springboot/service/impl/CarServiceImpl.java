package com.dms.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.entity.Car;
import com.dms.springboot.mapper.CarMapper;
import com.dms.springboot.service.ICarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 本表存储的是车的相关信息 服务实现类
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {
    @Resource
    CarMapper carMapper;

    public Page<Car> driverfindcarpage(Page<Car> objectPage, Integer driverid) {
        return carMapper.driverfindcarpage(objectPage, driverid);
    }
}
