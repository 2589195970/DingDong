package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.AgentProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 代理产品相关接口
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
@Mapper
public interface AgentProductMapper extends BaseMapper<AgentProduct> {

    /**
     * 批量插入代理商商品记录
     *
     * @param agentProducts 待插入列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<AgentProduct> agentProducts);

}
