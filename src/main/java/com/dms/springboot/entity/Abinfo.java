package com.dms.springboot.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-23
 */
@Getter
@Setter
@ApiModel(value = "Abinfo对象", description = "VIEW")
public class Abinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("驾驶员id ")
    private Integer userid;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("异常行为的时间")
    private String time;

    @ApiModelProperty("异常行为类型")
    private String abtype;

    @ApiModelProperty("所属视频id ")
    private Integer videoid;
    @ApiModelProperty("视频的存放地址")
    private String url;


    @ApiModelProperty("车辆id信息")
    private Integer carid;

    @ApiModelProperty("车牌号")
    private String licenseid;

    @ApiModelProperty("车辆类型")
    private String cartype;

    @ApiModelProperty("备注信息")
    private String remarks;

    @ApiModelProperty("当前异常行为是否已经审核，有三种状态“ACCEPT”“REJECT”“UNCHECKED”")
    private String checkstate;

    @ApiModelProperty("审核批示")
    private String checkremark;

    @ApiModelProperty("头像")
    private String avatarurl;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("记录员工号")
    private Integer leaderid;

    @ApiModelProperty("车辆责任人工号")
    private Integer dutyofficerid;
    @ApiModelProperty("车辆清晰照片信息 ")
    private String photo;
    @ApiModelProperty("扣除相应分数")
    private Integer mask;
    @ApiModelProperty("扣除相应分数")

    private String bothhandsleavingwheel;
    @ApiModelProperty("扣除相应分数")
    private String eyesclosed;
    @ApiModelProperty("扣除相应分数")
    private String nofacemask;
    @ApiModelProperty("扣除相应分数")
    private String notbucklingup;
    @ApiModelProperty("扣除相应分数")
    private String smoke;
    @ApiModelProperty("扣除相应分数")
    private String notfacingfront;
    @ApiModelProperty("扣除相应分数")
    private String cellphone;
    @ApiModelProperty("扣除相应分数")
    private String yawning;
    @ApiModelProperty("扣除相应分数")
    private String headlowered;

    public Abinfo() {
    }
}
