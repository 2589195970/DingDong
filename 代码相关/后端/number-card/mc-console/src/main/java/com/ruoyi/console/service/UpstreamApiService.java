package com.ruoyi.console.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UpstreamApiAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamApiSelectBO;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamExplain;
import com.ruoyi.common.order.vo.UpstreamApiTypeVO;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface UpstreamApiService extends IService<UpstreamApi> {


     /**
      * 分页查询上游接口列表
      * @return
      * @throws BizException
      */
     PageResult<UpstreamApi> selectUpstreamApiListPage(UpstreamApiSelectBO upstreamApiSelectBO) throws BizException;

     /**
      * 新增上游接口
      * @return
      * @throws BizException
      */
      void addUpstreamApi(UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) throws BizException;



     /**
      * 更新上游接口
      * @return
      * @throws BizException
      */
      void updateUpstreamApi(UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) throws BizException;


     /**
      * 删除上游接口
      * @return
      * @throws BizException
      */
      void deleteUpstreamApi(Integer upstreamApiId) throws BizException;


     /**
      * 获取上游接口API类型
      * @return
      * @throws BizException
      */
      List<UpstreamApiTypeVO> selectUpstreamApiTypeList(String upstreamApiType) throws BizException;


    /**
     * 查询产品参数说明
     * @param upstreamApiType
     * @param explainType
     * @return
     */
      UpstreamExplain selectUpstreamExplain(String upstreamApiType, Integer explainType);

}
