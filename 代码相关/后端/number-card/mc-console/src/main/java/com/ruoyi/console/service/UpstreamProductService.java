package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UpstreamApiAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamApiSelectBO;
import com.ruoyi.common.order.bo.UpstreamProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamProductSelectBO;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamProduct;
import com.ruoyi.common.order.vo.UpstreamApiTypeVO;
import com.ruoyi.common.order.vo.UpstreamProductVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface UpstreamProductService extends IService<UpstreamProduct> {


    /**
     * 分页查询上游产品列表
     *
     * @return
     * @throws BizException
     */
    PageResult<UpstreamProductVO> selectUpstreamProductListPage(UpstreamProductSelectBO upstreamProductSelectBO) throws BizException;

    /**
     * 新增上游产品
     *
     * @return
     * @throws BizException
     */
    void addUpstreamProduct(UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) throws BizException;


    /**
     * 更新上游产品
     *
     * @return
     * @throws BizException
     */
    void updateUpstreamProduct(UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) throws BizException;


    /**
     * 删除上游产品
     *
     * @return
     * @throws BizException
     */
    void deleteUpstreamProduct(Integer upstreamProductId) throws BizException;


}
