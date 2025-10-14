package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.order.entity.PhotoDefaultConfig;
import com.ruoyi.common.order.vo.PhotoConfigItemVO;
import com.ruoyi.common.order.vo.PhotoDefaultConfigVO;
import com.ruoyi.system.mapper.PhotoDefaultConfigMapper;
import com.ruoyi.system.service.IPhotoDefaultConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 照片默认配置Service业务层处理
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@Service
public class PhotoDefaultConfigServiceImpl implements IPhotoDefaultConfigService
{
    @Autowired
    private PhotoDefaultConfigMapper photoDefaultConfigMapper;

    /**
     * 查询照片默认配置
     *
     * @param configId 照片默认配置主键
     * @return 照片默认配置
     */
    @Override
    public PhotoDefaultConfigVO selectPhotoDefaultConfigByConfigId(Long configId)
    {
        PhotoDefaultConfig entity = photoDefaultConfigMapper.selectPhotoDefaultConfigByConfigId(configId);
        if (entity == null) {
            return null;
        }
        return convertToVO(entity);
    }

    /**
     * 查询照片默认配置列表
     *
     * @param photoDefaultConfig 照片默认配置
     * @return 照片默认配置
     */
    @Override
    public List<PhotoDefaultConfigVO> selectPhotoDefaultConfigList(PhotoDefaultConfig photoDefaultConfig)
    {
        List<PhotoDefaultConfig> entityList = photoDefaultConfigMapper.selectPhotoDefaultConfigList(photoDefaultConfig);
        return convertToVOList(entityList);
    }

    /**
     * 根据配置名称查询配置
     *
     * @param configName 配置名称
     * @return 照片默认配置
     */
    @Override
    public PhotoDefaultConfigVO selectPhotoDefaultConfigByConfigName(String configName)
    {
        if (StringUtils.isEmpty(configName)) {
            return null;
        }
        PhotoDefaultConfig entity = photoDefaultConfigMapper.selectPhotoDefaultConfigByConfigName(configName);
        return entity != null ? convertToVO(entity) : null;
    }

    /**
     * 新增照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    @Override
    public int insertPhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig)
    {
        // 参数校验
        if (photoDefaultConfig == null) {
            throw new ServiceException("参数不能为空");
        }
        if (StringUtils.isEmpty(photoDefaultConfig.getConfigName())) {
            throw new ServiceException("配置名称不能为空");
        }

        // 检查配置名称唯一性
        if (!checkConfigNameUnique(photoDefaultConfig)) {
            throw new ServiceException("配置名称已存在");
        }

        return photoDefaultConfigMapper.insertPhotoDefaultConfig(photoDefaultConfig);
    }

    /**
     * 修改照片默认配置
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    @Override
    public int updatePhotoDefaultConfig(PhotoDefaultConfig photoDefaultConfig)
    {
        // 参数校验
        if (photoDefaultConfig == null || photoDefaultConfig.getConfigId() == null) {
            throw new ServiceException("配置ID不能为空");
        }
        if (StringUtils.isEmpty(photoDefaultConfig.getConfigName())) {
            throw new ServiceException("配置名称不能为空");
        }

        // 检查配置是否存在
        PhotoDefaultConfig existingConfig = photoDefaultConfigMapper.selectPhotoDefaultConfigByConfigId(photoDefaultConfig.getConfigId());
        if (existingConfig == null) {
            throw new ServiceException("配置不存在");
        }

        // 检查配置名称唯一性
        if (!checkConfigNameUnique(photoDefaultConfig)) {
            throw new ServiceException("配置名称已存在");
        }

        return photoDefaultConfigMapper.updatePhotoDefaultConfig(photoDefaultConfig);
    }

    /**
     * 批量删除照片默认配置
     *
     * @param configIds 需要删除的照片默认配置主键
     * @return 结果
     */
    @Override
    public int deletePhotoDefaultConfigByConfigIds(Long[] configIds)
    {
        if (configIds == null || configIds.length == 0) {
            return 0;
        }
        return photoDefaultConfigMapper.deletePhotoDefaultConfigByConfigIds(configIds);
    }

    /**
     * 删除照片默认配置信息
     *
     * @param configId 照片默认配置主键
     * @return 结果
     */
    @Override
    public int deletePhotoDefaultConfigByConfigId(Long configId)
    {
        if (configId == null) {
            return 0;
        }
        return photoDefaultConfigMapper.deletePhotoDefaultConfigByConfigId(configId);
    }

    /**
     * 校验配置名称是否唯一
     *
     * @param photoDefaultConfig 照片默认配置实体
     * @return 结果
     */
    @Override
    public boolean checkConfigNameUnique(PhotoDefaultConfig photoDefaultConfig)
    {
        if (photoDefaultConfig == null || StringUtils.isEmpty(photoDefaultConfig.getConfigName())) {
            return false;
        }
        Long configId = photoDefaultConfig.getConfigId() == null ? -1L : photoDefaultConfig.getConfigId();
        PhotoDefaultConfig info = photoDefaultConfigMapper.selectPhotoDefaultConfigByConfigName(photoDefaultConfig.getConfigName());
        if (info != null && info.getConfigId().longValue() != configId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isActive 状态
     * @return 结果
     */
    @Override
    public int updateConfigStatus(Long configId, Integer isActive)
    {
        if (configId == null || isActive == null || (isActive != 0 && isActive != 1)) {
            return 0;
        }
        return photoDefaultConfigMapper.updateConfigStatus(configId, isActive);
    }

    /**
     * 查询启用的默认模板配置列表
     *
     * @return 默认模板配置列表
     */
    @Override
    public List<PhotoDefaultConfigVO> selectDefaultTemplateConfigs()
    {
        List<PhotoDefaultConfig> entityList = photoDefaultConfigMapper.selectDefaultTemplateConfigs();
        return convertToVOList(entityList);
    }

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @param isActive 启用状态
     * @return 配置列表
     */
    @Override
    public List<PhotoDefaultConfigVO> selectConfigsByType(Integer configType, Integer isActive)
    {
        List<PhotoDefaultConfig> entityList = photoDefaultConfigMapper.selectConfigsByType(configType, isActive);
        return convertToVOList(entityList);
    }

    /**
     * 复制配置
     *
     * @param sourceConfigId 源配置ID
     * @param newConfigName 新配置名称
     * @param operator 操作人
     * @return 新配置
     */
    @Override
    public PhotoDefaultConfigVO copyConfig(Long sourceConfigId, String newConfigName, String operator)
    {
        if (sourceConfigId == null || StringUtils.isEmpty(newConfigName)) {
            throw new ServiceException("参数不能为空");
        }

        // 查询源配置
        PhotoDefaultConfig sourceEntity = photoDefaultConfigMapper.selectPhotoDefaultConfigByConfigId(sourceConfigId);
        if (sourceEntity == null) {
            throw new ServiceException("源配置不存在");
        }

        // 创建新配置
        PhotoDefaultConfig newEntity = new PhotoDefaultConfig();
        BeanUtils.copyBeanProp(newEntity, sourceEntity);
        newEntity.setConfigId(null); // 清空ID，让数据库自动生成
        newEntity.setConfigName(newConfigName);
        newEntity.setConfigType(2); // 复制的配置默认为自定义模板
        newEntity.setCreateBy(operator);
        newEntity.setCreateTime(new java.util.Date());
        newEntity.setUpdateBy(null);
        newEntity.setUpdateTime(null);

        // 检查新配置名称唯一性
        if (!checkConfigNameUnique(newEntity)) {
            throw new ServiceException("新配置名称已存在");
        }

        photoDefaultConfigMapper.insertPhotoDefaultConfig(newEntity);

        return convertToVO(newEntity);
    }

    /**
     * 导出照片默认配置数据
     *
     * @param photoDefaultConfigList 照片默认配置列表
     * @return Excel数据
     */
    @Override
    public List<PhotoDefaultConfigVO> exportPhotoDefaultConfig(List<PhotoDefaultConfig> photoDefaultConfigList)
    {
        return convertToVOList(photoDefaultConfigList);
    }

    /**
     * 转换实体为视图对象
     *
     * @param entity 实体对象
     * @return 视图对象
     */
    @Override
    public PhotoDefaultConfigVO convertToVO(PhotoDefaultConfig entity)
    {
        if (entity == null) {
            return null;
        }

        PhotoDefaultConfigVO vo = new PhotoDefaultConfigVO();
        BeanUtils.copyBeanProp(vo, entity);

        // 设置类型和状态名称
        if (entity.getConfigType() != null) {
            vo.setConfigTypeName(entity.getConfigType() == 1 ? "默认模板" : "自定义模板");
        }
        if (entity.getIsActive() != null) {
            vo.setIsActiveName(entity.getIsActive() == 1 ? "启用" : "禁用");
        }

        // 解析照片配置JSON
        if (StringUtils.isNotEmpty(entity.getPhotoConfig())) {
            List<PhotoConfigItemVO> photoConfigList = new ArrayList<>();

            try {
                JSONArray jsonArray = JSON.parseArray(entity.getPhotoConfig());
                for (int i = 0; i < jsonArray.size(); i++) {
                    PhotoConfigItemVO itemVO = new PhotoConfigItemVO();
                    itemVO.setPhotoType(jsonArray.getJSONObject(i).getInteger("photoType"));
                    itemVO.setPhotoTypeName(jsonArray.getJSONObject(i).getString("photoTypeName"));
                    itemVO.setRequired(jsonArray.getJSONObject(i).getInteger("required"));
                    itemVO.setTitle(jsonArray.getJSONObject(i).getString("title"));
                    itemVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
                    itemVO.setExampleUrl(jsonArray.getJSONObject(i).getString("exampleUrl"));
                    itemVO.setMaxSize(jsonArray.getJSONObject(i).getLong("maxSize"));
                    itemVO.setSupportedFormats(jsonArray.getJSONObject(i).getString("supportedFormats"));
                    itemVO.setMinWidth(jsonArray.getJSONObject(i).getInteger("minWidth"));
                    itemVO.setMinHeight(jsonArray.getJSONObject(i).getInteger("minHeight"));
                    itemVO.setMaxWidth(jsonArray.getJSONObject(i).getInteger("maxWidth"));
                    itemVO.setMaxHeight(jsonArray.getJSONObject(i).getInteger("maxHeight"));
                    itemVO.setSortOrder(jsonArray.getJSONObject(i).getInteger("sortOrder"));
                    photoConfigList.add(itemVO);
                }
            } catch (Exception e) {
                // JSON解析失败时忽略，保持空列表
            }

            vo.setPhotoConfigList(photoConfigList);
            vo.setPhotoConfigCount(photoConfigList.size());
        } else {
            vo.setPhotoConfigList(new ArrayList<>());
            vo.setPhotoConfigCount(0);
        }

        return vo;
    }

    /**
     * 转换实体列表为视图对象列表
     *
     * @param entityList 实体列表
     * @return 视图对象列表
     */
    @Override
    public List<PhotoDefaultConfigVO> convertToVOList(List<PhotoDefaultConfig> entityList)
    {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }

        List<PhotoDefaultConfigVO> voList = new ArrayList<>(entityList.size());
        for (PhotoDefaultConfig entity : entityList) {
            voList.add(convertToVO(entity));
        }
        return voList;
    }
}