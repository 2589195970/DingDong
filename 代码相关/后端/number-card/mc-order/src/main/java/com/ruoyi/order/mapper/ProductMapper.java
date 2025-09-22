package com.ruoyi.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.bo.AgentProductBO;
import com.ruoyi.common.order.entity.Product;
import com.ruoyi.common.order.vo.AgentProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 查询admin代理商产品
     * @return
     */
    AgentProductBO selectAdminAgentProduct(@Param(value = "productCode") String productCode);

    /**
     * 查询代理商产品
     * @param agentCode
     * @return
     */
    AgentProductBO selectAgentProduct(@Param(value = "productCode") String productCode, @Param(value = "agentCode") String agentCode);


}
