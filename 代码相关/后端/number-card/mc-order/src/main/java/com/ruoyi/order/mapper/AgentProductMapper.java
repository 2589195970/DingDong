package com.ruoyi.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.vo.ProductListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 代理商产品表
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
@Mapper
public interface AgentProductMapper extends BaseMapper<AgentProduct> {

    /**
     * 查询admin产品列表
     * @return
     */
    List<ProductListVO> selectAdminAgentProductList(@Param(value = "operatorType") Integer operatorType);


    /**
     * 查询代理商产品列表
     * @param agentCode
     * @return
     */
    List<ProductListVO> selectAgentProductList(@Param(value = "agentCode") String agentCode,@Param(value = "operatorType") Integer operatorType);
}
