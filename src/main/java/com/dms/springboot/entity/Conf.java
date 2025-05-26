package com.dms.springboot.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统配置文件
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-04-25
 */
@Getter
@Setter
@ApiModel(value = "Conf对象", description = "系统配置文件")
public class Conf implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("配置文件键")
    private String k;

    @ApiModelProperty("配置文件值")
    private String value;

    private String remarks;

}
