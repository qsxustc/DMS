package com.dms.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dms.springboot.entity.Car;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 本表存储的是车的相关信息 Mapper 接口
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
public interface CarMapper extends BaseMapper<Car> {
    @Select("select car.* from car,userandcar where userandcar.userid=#{driverid} and userandcar.carid=car.id")
    Page<Car> driverfindcarpage(Page<Car> objectPage, @Param("driverid") Integer driverid);
}
