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
 * 本表是为了存储视频信息
 * </p>
 *
 * @author 屈松鑫
 * @since 2023-03-22
 */
@Getter
@Setter
  @ApiModel(value = "Video对象", description = "本表是为了存储视频信息")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("视频id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("视频名称")
      private String name;

      @ApiModelProperty("视频的产生时间")
      private String createtime;

      @ApiModelProperty("视频的存放地址")
      private String url;

      @ApiModelProperty("文件md5")
      private String md5;

      @ApiModelProperty("文件大小（kb）")
      private Long size;

      @ApiModelProperty("所属驾驶员id")
      private Integer ownerid;

      @ApiModelProperty("所属车辆id")
      private Integer carid;


}
