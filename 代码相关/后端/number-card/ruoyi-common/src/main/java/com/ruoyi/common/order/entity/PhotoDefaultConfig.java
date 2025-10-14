package com.ruoyi.common.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 照片默认配置实体类
 * 对应数据库表 t_photo_default_config
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@Data
@ApiModel("照片默认配置实体")
@TableName("t_photo_default_config")
public class PhotoDefaultConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置ID")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    @ApiModelProperty("配置名称")
    @TableField("config_name")
    private String configName;

    @ApiModelProperty("配置类型 1-默认模板 2-自定义模板")
    @TableField("config_type")
    private Integer configType;

    @ApiModelProperty("照片配置JSON")
    @TableField("photo_config")
    private String photoConfig;

    @ApiModelProperty("配置描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("是否启用 0-禁用 1-启用")
    @TableField("is_active")
    private Integer isActive;

    @ApiModelProperty("创建者")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新者")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}