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
 * @since 2023-03-22
 */
@Getter
@Setter
  @ApiModel(value = "Userinfo对象", description = "VIEW")
public class Userinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      private Integer id;

      @ApiModelProperty("用户名（用于登陆）")
      private String username;

      @ApiModelProperty("姓名")
      private String name;

      @ApiModelProperty("驾龄")
      private String driveyear;

      @ApiModelProperty("年龄")
      private Integer age;

      @ApiModelProperty("管理他的记录员id")
      private Integer leaderid;

      @ApiModelProperty("角色")
      private String role;

      @ApiModelProperty("电话")
      private String phone;

      @ApiModelProperty("地址")
      private String address;


}
