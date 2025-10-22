package com.ruoyi.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.vip.bo.VipUserQueryBO;
import com.ruoyi.common.vip.bo.VipUserSaveBO;
import com.ruoyi.common.vip.bo.VipUserSetLevelBO;
import com.ruoyi.common.vip.entity.VipUser;
import com.ruoyi.common.vip.vo.VipUserSummaryVO;
import com.ruoyi.common.vip.vo.VipUserVO;

import java.util.List;

/**
 * VIP用户服务接口
 *
 * @author Codex
 */
public interface VipUserService extends IService<VipUser> {

    /**
     * 分页查询VIP用户
     *
     * @param queryBO 查询参数
     * @return 分页结果
     */
    PageResult<VipUserVO> selectVipUserPage(VipUserQueryBO queryBO);

    /**
     * 查询VIP用户详情
     *
     * @param id 主键
     * @return 详情
     */
    VipUserVO selectVipUserDetail(Long id);

    /**
     * 查询当前登录用户展示用的VIP摘要信息。
     *
     * @param userId sys_user 表主键
     * @return VIP摘要
     */
    VipUserSummaryVO selectVipSummaryByUserId(Long userId);

    /**
     * 新增VIP用户
     *
     * @param saveBO 参数
     * @param loginUser 当前登录人
     */
    void addVipUser(VipUserSaveBO saveBO, LoginUser loginUser) throws BizException;

    /**
     * 修改VIP用户
     *
     * @param id 主键
     * @param saveBO 参数
     * @param loginUser 当前登录人
     */
    void updateVipUser(Long id, VipUserSaveBO saveBO, LoginUser loginUser) throws BizException;

    /**
     * 批量删除VIP用户
     *
     * @param ids 主键集合
     * @param loginUser 当前登录人
     */
    void deleteVipUserByIds(List<Long> ids, LoginUser loginUser) throws BizException;

    /**
     * 设置VIP等级
     *
     * @param setLevelBO 参数
     * @param loginUser 当前登录人
     */
    void setVipLevel(VipUserSetLevelBO setLevelBO, LoginUser loginUser) throws BizException;

    /**
     * 导出VIP用户
     *
     * @param ids 主键集合，可为空表示导出全部
     * @return 数据列表
     */
    List<VipUserVO> exportVipUsers(List<Long> ids);

    /**
     * 补录缺失的VIP用户
     *
     * @param loginUser 当前登录人
     * @return 新增数量
     * @throws BizException 业务异常
     */
    int supplementVipUsers(LoginUser loginUser) throws BizException;
}
