package com.ruoyi.common.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 照片默认配置视图对象
 * 用于前后端数据传输和展示
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/12 18:00
 */
@Data
@ApiModel("照片默认配置视图对象")
public class PhotoDefaultConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置ID")
    private Long configId;

    @ApiModelProperty("配置名称")
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    @ApiModelProperty("配置类型 1-默认模板 2-自定义模板")
    @NotNull(message = "配置类型不能为空")
    private Integer configType;

    @ApiModelProperty("配置类型名称")
    private String configTypeName;

    @ApiModelProperty("照片配置列表")
    private List<PhotoConfigItemVO> photoConfigList;

    @ApiModelProperty("配置描述")
    private String description;

    @ApiModelProperty("是否启用 0-禁用 1-启用")
    @NotNull(message = "启用状态不能为空")
    private Integer isActive;

    @ApiModelProperty("启用状态名称")
    private String isActiveName;

    @ApiModelProperty("创建者")
    private String createBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("照片配置数量")
    private Integer photoConfigCount;
}