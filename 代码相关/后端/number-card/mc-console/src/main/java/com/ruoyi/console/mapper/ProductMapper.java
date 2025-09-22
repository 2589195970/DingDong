package com.ruoyi.console.mapper;

        import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 查询代理商产品列表总数
     * @param agentCode
     * @return
     */
    Integer selectAgentProductListCount(@Param(value = "operatorType") Integer operatorType,@Param(value = "productStatus") Integer productStatus,
                                        @Param(value = "productType") Integer productType,
                                        @Param(value = "productName") String productName,@Param(value = "agentCode") String agentCode);

    /**
     * 查询代理商产品列表
     * @param agentCode
     * @return
     */
    List<AgentProductVO> selectAgentProductList(@Param(value = "operatorType") Integer operatorType,@Param(value = "productStatus") Integer productStatus,
                                                @Param(value = "productType") Integer productType,
                                                @Param(value = "productName") String productName,@Param(value = "agentCode") String agentCode,
                                                @Param(value = "offset") Integer offset,@Param(value = "pageSize") Integer pageSize);

}
