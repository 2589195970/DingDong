package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.NameAuditSelectBO;
import com.ruoyi.common.order.entity.NameAudit;

/**
 * 代理商姓名审核
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/03/07 17:11
 */
public interface AgentNameAuditService extends IService<NameAudit> {


    /**
     * 代理商实名认证列表查询
     *
     * @return
     * @throws BizException
     */
    PageResult<NameAudit> selectNameAuditListPage(NameAuditSelectBO nameAuditSelectBO) throws BizException;



    /**
     * 查询登陆用户实名审核
     *
     * @return
     * @throws BizException
     */
    NameAudit selectNameAudit(LoginUser loginUser) throws BizException;


    /**
     * 新增实名审核记录
     *
     * @return
     * @throws BizException
     */
    void addNameAudit(NameAudit nameAudit) throws BizException;


    /**
     * 更新实名审核记录
     *
     * @return
     * @throws BizException
     */
    void updateNameAudit(NameAudit nameAudit) throws BizException;


    /**
     * 更新实名审核状态
     *
     * @return
     * @throws BizException
     */
    void updateNameAuditStatus(NameAudit nameAudit) throws BizException;


    /**
     * 删除实名审核记录
     *
     * @throws BizException
     */
    void deleteNameAudit(Integer nameAuditId) throws BizException;

}
