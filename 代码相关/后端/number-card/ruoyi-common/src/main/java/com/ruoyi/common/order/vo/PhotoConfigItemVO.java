package com.ruoyi.common.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 照片配置项视图对象
 * 用于表示photo_config JSON数组中的单个配置项
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/12 18:00
 */
@Data
@ApiModel("照片配置项视图对象")
public class PhotoConfigItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("照片类型 1-身份证正面 2-身份证反面 3-免冠照片 等")
    @NotNull(message = "照片类型不能为空")
    private Integer photoType;

    @ApiModelProperty("照片类型名称")
    @NotBlank(message = "照片类型名称不能为空")
    private String photoTypeName;

    @ApiModelProperty("是否必填 0-否 1-是")
    @NotNull(message = "必填标识不能为空")
    private Integer required;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("描述说明")
    @NotBlank(message = "描述说明不能为空")
    private String description;

    @ApiModelProperty("示例图片URL")
    private String exampleUrl;

    @ApiModelProperty("最大文件大小(字节)")
    @NotNull(message = "最大文件大小不能为空")
    @Min(value = 1, message = "最大文件大小必须大于0")
    private Long maxSize;

    @ApiModelProperty("支持的文件格式")
    @NotBlank(message = "支持的文件格式不能为空")
    private String supportedFormats;

    @ApiModelProperty("最小宽度")
    @Min(value = 1, message = "最小宽度必须大于0")
    private Integer minWidth;

    @ApiModelProperty("最小高度")
    @Min(value = 1, message = "最小高度必须大于0")
    private Integer minHeight;

    @ApiModelProperty("最大宽度")
    private Integer maxWidth;

    @ApiModelProperty("最大高度")
    private Integer maxHeight;

    @ApiModelProperty("排序顺序")
    private Integer sortOrder;
}