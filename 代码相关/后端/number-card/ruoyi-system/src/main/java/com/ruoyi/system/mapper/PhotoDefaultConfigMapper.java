package com.ruoyi.system.mapper;

import com.ruoyi.common.order.entity.PhotoDefaultConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 照片默认配置 数据层
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
public interface PhotoDefaultConfigMapper
{
    /**
     * 查询照片默认配置
     *
     * @param configId 照片默认配置主键
     * @return 照片默认配置
     */
    public PhotoDefaultConfig selectPhotoDefaultConfigByConfigId(Long configId);

    /**
     * 查询照片默认配置列表
     *
     * @param photoDefaultConfig 照片默认配置
     * @return 照片默认配置集合
     */
    public List<PhotoDefaultConfig> selectPhotoDefaultConfigList(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 根据配置名称查询配置
     *
     * @param configName 配置名称
     * @return 照片默认配置
     */
    public PhotoDefaultConfig selectPhotoDefaultConfigByConfigName(String configName);

    /**
     * 检查配置名称是否唯一
     *
     * @param configName 配置名称
     * @param configId 配置ID（排除自身）
     * @return 结果
     */
    public int checkConfigNameUnique(@Param("configName") String configName, @Param("configId") Long configId);

    /**
     * 新增照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置
     * @return 结果
     */
    public int insertPhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 修改照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置
     * @return 结果
     */
    public int updatePhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 删除照片默认配置
     *
     * @param configId 照片默认配置主键
     * @return 结果
     */
    public int deletePhotoDefaultConfigByConfigId(Long configId);

    /**
     * 批量删除照片默认配置
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePhotoDefaultConfigByConfigIds(Long[] configIds);

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isActive 状态
     * @return 结果
     */
    public int updateConfigStatus(@Param("configId") Long configId, @Param("isActive") Integer isActive);

    /**
     * 查询启用的配置数量
     *
     * @param configType 配置类型
     * @return 数量
     */
    public int countActiveConfigs(Integer configType);

    /**
     * 查询默认模板配置列表
     *
     * @return 默认模板配置列表
     */
    public List<PhotoDefaultConfig> selectDefaultTemplateConfigs();

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @param isActive 启用状态
     * @return 配置列表
     */
    public List<PhotoDefaultConfig> selectConfigsByType(@Param("configType") Integer configType, @Param("isActive") Integer isActive);
}