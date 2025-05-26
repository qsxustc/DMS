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
 * 该表是存储用户的有关信息，所有用户的信息全在这个表中
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-21
 */
@Getter
@Setter
  @ApiModel(value = "User对象", description = "该表是存储用户的有关信息，所有用户的信息全在这个表中")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名（用于登陆）")
      private String username;

      @ApiModelProperty("密码")
      private String password;

      @ApiModelProperty("姓名")
      private String name;

      @ApiModelProperty("年龄")
      private Integer age;

      @ApiModelProperty("地址")
      private String address;

      @ApiModelProperty("驾龄")
      private String driveyear;

      @ApiModelProperty("电话")
      private String phone;

      @ApiModelProperty("头像")
      private String avatarurl;

      @ApiModelProperty("创建时间")
      private String createtime;

      @ApiModelProperty("角色")
      private String role;

      @ApiModelProperty("管理他的记录员id")
      private Integer leaderid;

      private Integer mask;//分数

}
