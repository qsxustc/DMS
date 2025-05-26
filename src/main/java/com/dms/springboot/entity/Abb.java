package com.dms.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-25
 */
@Getter
@Setter
@ApiModel(value = "Abb对象", description = "")
public class Abb implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("异常行为的时间")
    private String time;

    @ApiModelProperty("异常行为类型")
    private String abtype;

    @ApiModelProperty("所属视频id ")
    private Integer videoid;

    @ApiModelProperty("驾驶员id ")
    private Integer userid;

    @ApiModelProperty("车辆id信息")
    private Integer carid;
    @ApiModelProperty("备注信息")
    private String remarks;

    @ApiModelProperty("当前异常行为是否已经审核，有三种状态“ACCEPT”“REJECT”“UNCHECKED”")
    private String checkstate;

    @ApiModelProperty("审核批示")
    private String checkremark;

    private String bothhandsleavingwheel;
    private String eyesclosed;
    private String nofacemask;
    private String notbucklingup;
    private String smoke;
    private String notfacingfront;
    private String cellphone;
    private String yawning;
    private String headlowered;
    @ApiModelProperty("扣除相应分数")
    private Integer mask;
}
