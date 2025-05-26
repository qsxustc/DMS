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
 * @since 2023-04-25
 */
@Getter
@Setter
  @ApiModel(value = "Abtype对象", description = "")
public class Abtype implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer id;

      @ApiModelProperty("异常行为的类型")
      private String abtype;

      @ApiModelProperty("扣除相应分数")
      private Integer mask;

      @ApiModelProperty("备注")
      private String remarks;


}
