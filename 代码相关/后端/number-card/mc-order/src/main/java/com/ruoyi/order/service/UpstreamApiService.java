package com.ruoyi.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UpstreamApiAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamApiSelectBO;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamInfo;
import com.ruoyi.common.order.vo.UpstreamApiTypeVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface UpstreamApiService extends IService<UpstreamApi> {


    /**
     * 根据上游API Code查询
     *
     * @param
     * @return
     */
     UpstreamApi getUpstreamApi(String upstreamApiCode) throws BizException;


    /**
     * 根据上游API和条件获取上游产品信息
     * 分单在此方法中进行
     * @param
     * @return
     */
     UpstreamInfo getUpstreamInfo(String upstreamApiCode, String upstreamProductCode) throws BizException;

}
