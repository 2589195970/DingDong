package com.ruoyi.system.service;

import com.ruoyi.common.order.entity.PhotoDefaultConfig;
import com.ruoyi.common.order.vo.PhotoDefaultConfigVO;
import java.util.List;

/**
 * 照片默认配置Service接口
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
public interface IPhotoDefaultConfigService
{
    /**
     * 查询照片默认配置
     *
     * @param configId 照片默认配置主键
     * @return 照片默认配置
     */
    public PhotoDefaultConfigVO selectPhotoDefaultConfigByConfigId(Long configId);

    /**
     * 查询照片默认配置列表
     *
     * @param photoDefaultConfig 照片默认配置
     * @return 照片默认配置集合
     */
    public List<PhotoDefaultConfigVO> selectPhotoDefaultConfigList(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 根据配置名称查询配置
     *
     * @param configName 配置名称
     * @return 照片默认配置
     */
    public PhotoDefaultConfigVO selectPhotoDefaultConfigByConfigName(String configName);

    /**
     * 新增照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    public int insertPhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 修改照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    public int updatePhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 批量删除照片默认配置
     *
     * @param configIds 需要删除的照片默认配置主键集合
     * @return 结果
     */
    public int deletePhotoDefaultConfigByConfigIds(Long[] configIds);

    /**
     * 删除照片默认配置信息
     *
     * @param configId 照片默认配置主键
     * @return 结果
     */
    public int deletePhotoDefaultConfigByConfigId(Long configId);

    /**
     * 校验配置名称是否唯一
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    public boolean checkConfigNameUnique(PhotoDefaultConfig photoDefaultConfig);

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isActive 状态
     * @return 结果
     */
    public int updateConfigStatus(Long configId, Integer isActive);

    /**
     * 查询启用的默认模板配置列表
     *
     * @return 默认模板配置列表
     */
    public List<PhotoDefaultConfigVO> selectDefaultTemplateConfigs();

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @param isActive 启用状态
     * @return 配置列表
     */
    public List<PhotoDefaultConfigVO> selectConfigsByType(Integer configType, Integer isActive);

    /**
     * 复制配置
     *
     * @param sourceConfigId 源配置ID
     * @param newConfigName 新配置名称
     * @param operator 操作人
     * @return 新配置
     */
    public PhotoDefaultConfigVO copyConfig(Long sourceConfigId, String newConfigName, String operator);

    /**
     * 导出照片默认配置数据
     *
     * @param photoDefaultConfigList 照片默认配置列表
     * @return Excel数据
     */
    public List<PhotoDefaultConfigVO> exportPhotoDefaultConfig(List<PhotoDefaultConfig> photoDefaultConfigList);

    /**
     * 转换实体为视图对象
     *
     * @param entity 实体对象
     * @return 视图对象
     */
    public PhotoDefaultConfigVO convertToVO(PhotoDefaultConfig entity);

    /**
     * 转换实体列表为视图对象列表
     *
     * @param entityList 实体列表
     * @return 视图对象列表
     */
    public List<PhotoDefaultConfigVO> convertToVOList(List<PhotoDefaultConfig> entityList);
}