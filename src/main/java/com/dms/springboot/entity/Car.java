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
 * 本表存储的是车的相关信息
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@Getter
@Setter
  @ApiModel(value = "Car对象", description = "本表存储的是车的相关信息")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("车牌号")
      private String licenseid;

      @ApiModelProperty("颜色")
      private String color;

      @ApiModelProperty("车型号")
      private String type;

      @ApiModelProperty("发动机号码")
      private String engineid;

      @ApiModelProperty("底盘号码")
      private String chassisid;

      @ApiModelProperty("责任人工号")
      private Integer dutyofficerid;

      @ApiModelProperty("清晰照片信息 ")
      private String photo;

      @ApiModelProperty("加入系统时间 ")
      private String createtime;

      @ApiModelProperty("备注")
      private String remarks;


}
