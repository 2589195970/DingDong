package com.ruoyi.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.vip.bo.VipConfigQueryBO;
import com.ruoyi.common.vip.bo.VipConfigSaveBO;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.common.vip.vo.VipConfigVO;
import com.ruoyi.console.mapper.VipConfigMapper;
import com.ruoyi.console.service.VipConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * VIP等级配置服务实现
 *
 * @author Codex
 */
@Service
public class VipConfigServiceImpl extends ServiceImpl<VipConfigMapper, VipConfig> implements VipConfigService {

    @Override
    public PageResult<VipConfigVO> selectVipConfigPage(VipConfigQueryBO queryBO) throws BizException {
        if (queryBO == null) {
            queryBO = new VipConfigQueryBO();
        }
        int pageNo = queryBO.getPageNo() == null || queryBO.getPageNo() <= 0 ? 1 : queryBO.getPageNo();
        int pageSize = queryBO.getPageSize() == null || queryBO.getPageSize() <= 0 ? 10 : queryBO.getPageSize();

        LambdaQueryWrapper<VipConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryBO.getLevelName())) {
            queryWrapper.like(VipConfig::getLevelName, queryBO.getLevelName());
        }
        if (queryBO.getIsEnabled() != null) {
            queryWrapper.eq(VipConfig::getIsEnabled, queryBO.getIsEnabled());
        }
        queryWrapper.orderByAsc(VipConfig::getVipLevel);

        Page<VipConfig> page = page(new Page<>(pageNo, pageSize), queryWrapper);
        List<VipConfigVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<VipConfigVO> voPage = new Page<>(pageNo, pageSize);
        voPage.setTotal(page.getTotal());
        voPage.setCurrent(pageNo);
        voPage.setSize(pageSize);
        voPage.setRecords(voList);
        return PageResultFactory.createPageResult(voPage);
    }

    @Override
    public List<VipConfig> selectVipConfigList() {
        LambdaQueryWrapper<VipConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(VipConfig::getVipLevel);
        return list(queryWrapper);
    }

    @Override
    public List<VipConfig> selectEnabledVipConfigs() {
        LambdaQueryWrapper<VipConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VipConfig::getIsEnabled, BaseConstant.ONE_INT)
                .orderByAsc(VipConfig::getVipLevel);
        return list(queryWrapper);
    }

    @Override
    public VipConfig selectByVipLevel(Integer vipLevel) {
        if (vipLevel == null) {
            return null;
        }
        LambdaQueryWrapper<VipConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VipConfig::getVipLevel, vipLevel)
                .last("limit 1");
        return getOne(queryWrapper, false);
    }

    @Override
    public VipConfigVO selectVipConfigDetail(Long id) {
        if (id == null) {
            return null;
        }
        VipConfig vipConfig = getById(id);
        if (vipConfig == null) {
            return null;
        }
        return convertToVO(vipConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVipConfig(VipConfigSaveBO saveBO, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        validateVipConfig(saveBO, true);
        if (saveBO.getId() != null) {
            throw new BizException("新增VIP配置不允许携带ID");
        }
        if (selectByVipLevel(saveBO.getVipLevel()) != null) {
            throw new BizException("VIP等级{}已存在，请勿重复新增", saveBO.getVipLevel());
        }
        Date now = new Date();
        VipConfig vipConfig = new VipConfig();
        BeanUtils.copyProperties(saveBO, vipConfig);
        vipConfig.setCreateTime(now);
        vipConfig.setUpdateTime(now);
        if (vipConfig.getIsEnabled() == null) {
            vipConfig.setIsEnabled(BaseConstant.ONE_INT);
        }
        if (vipConfig.getFixedCommission() == null) {
            vipConfig.setFixedCommission(BaseConstant.ZERO_INT);
        }
        save(vipConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipConfig(VipConfigSaveBO saveBO, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (saveBO == null || saveBO.getId() == null) {
            throw new BizException("配置ID不能为空");
        }
        validateVipConfig(saveBO, false);
        VipConfig existConfig = getById(saveBO.getId());
        if (existConfig == null) {
            throw new BizException("待更新的VIP配置不存在");
        }
        if (saveBO.getVipLevel() != null && !Objects.equals(saveBO.getVipLevel(), existConfig.getVipLevel())) {
            LambdaQueryWrapper<VipConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(VipConfig::getVipLevel, saveBO.getVipLevel())
                    .ne(VipConfig::getId, saveBO.getId())
                    .last("limit 1");
            VipConfig conflictConfig = getOne(queryWrapper, false);
            if (conflictConfig != null) {
                throw new BizException("目标VIP等级{}已配置，请调整后重试", saveBO.getVipLevel());
            }
        }
        VipConfig updateEntity = new VipConfig();
        BeanUtils.copyProperties(saveBO, updateEntity);
        updateEntity.setId(saveBO.getId());
        updateEntity.setUpdateTime(new Date());
        if (updateEntity.getFixedCommission() == null) {
            updateEntity.setFixedCommission(BaseConstant.ZERO_INT);
        }
        updateById(updateEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVipConfigByIds(List<Long> ids, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (ids == null || ids.isEmpty()) {
            throw new BizException("请选择要删除的记录");
        }
        removeByIds(ids);
    }

    @Override
    public void updateVipConfigStatus(Long id, Integer isEnabled, LoginUser loginUser) throws BizException {
        validateOperator(loginUser);
        if (id == null) {
            throw new BizException("配置ID不能为空");
        }
        if (isEnabled == null || (!Objects.equals(isEnabled, BaseConstant.ZERO_INT) && !Objects.equals(isEnabled, BaseConstant.ONE_INT))) {
            throw new BizException("启用状态仅支持0或1");
        }
        VipConfig existConfig = getById(id);
        if (existConfig == null) {
            throw new BizException("待更新的VIP配置不存在");
        }
        VipConfig updateEntity = new VipConfig();
        updateEntity.setId(id);
        updateEntity.setIsEnabled(isEnabled);
        updateEntity.setUpdateTime(new Date());
        updateById(updateEntity);
    }

    @Override
    public void toggleVipConfig(Long id, boolean enable, LoginUser loginUser) throws BizException {
        updateVipConfigStatus(id, enable ? BaseConstant.ONE_INT : BaseConstant.ZERO_INT, loginUser);
    }

    private void validateVipConfig(VipConfigSaveBO vipConfig, boolean isCreate) throws BizException {
        if (vipConfig == null) {
            throw new BizException("VIP配置不能为空");
        }
        if (vipConfig.getVipLevel() == null) {
            throw new BizException("VIP等级不能为空");
        }
        if (isCreate) {
            if (vipConfig.getRequiredOrders() == null) {
                throw new BizException("升级所需订单数不能为空");
            }
            if (vipConfig.getRequiredOrders() < BaseConstant.ZERO_INT) {
                throw new BizException("升级所需订单数必须大于等于0");
            }
        } else if (vipConfig.getRequiredOrders() != null && vipConfig.getRequiredOrders() < BaseConstant.ZERO_INT) {
            throw new BizException("升级所需订单数必须大于等于0");
        }
        if (vipConfig.getIsEnabled() != null
                && !Objects.equals(vipConfig.getIsEnabled(), BaseConstant.ZERO_INT)
                && !Objects.equals(vipConfig.getIsEnabled(), BaseConstant.ONE_INT)) {
            throw new BizException("启用状态仅支持0或1");
        }
        if (vipConfig.getFixedCommission() != null && vipConfig.getFixedCommission() < BaseConstant.ZERO_INT) {
            throw new BizException("固定加成不能小于0");
        }
    }

    private void validateOperator(LoginUser loginUser) throws BizException {
        if (loginUser == null || loginUser.getUserId() == null) {
            throw new BizException("未获取到操作用户信息");
        }
    }

    private VipConfigVO convertToVO(VipConfig vipConfig) {
        if (vipConfig == null) {
            return null;
        }
        VipConfigVO vo = new VipConfigVO();
        BeanUtils.copyProperties(vipConfig, vo);
        return vo;
    }

}
