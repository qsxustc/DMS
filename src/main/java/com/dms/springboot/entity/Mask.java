package com.dms.springboot.entity;

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
 * @since 2023-04-27
 */
@Getter
@Setter
  @ApiModel(value = "Mask对象", description = "")
public class Mask implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer id;

      @ApiModelProperty("时间周期 ")
      private String time;

      @ApiModelProperty("在某一个积分周期的分数")
      private String mask;

      @ApiModelProperty("驾驶员")
      private Integer userid;

}
