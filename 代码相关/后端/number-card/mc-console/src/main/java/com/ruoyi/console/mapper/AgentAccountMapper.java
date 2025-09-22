package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
@Mapper
public interface AgentAccountMapper extends BaseMapper<AgentAccount> {

    /**
     * 查询代理商列表总数
     * @return
     */
    Integer selectAgentAccountListCount(@Param(value = "parentAgentName") String parentAgentName, @Param(value = "agentName") String agentName,
                                        @Param(value = "isRealName") Integer isRealName,@Param(value = "isEnabled") Integer isEnabled,
                                        @Param(value = "level") Integer level);



    /**
     * 查询代理商列表
     * @return
     */
    List<AgentAccountListVO> selectAgentAccountList(@Param(value = "parentAgentName") String parentAgentName, @Param(value = "agentName") String agentName,
                                                    @Param(value = "isRealName") Integer isRealName, @Param(value = "isEnabled") Integer isEnabled,
                                                    @Param(value = "level") Integer level,
                                                    @Param(value = "offset") Integer offset, @Param(value = "pageSize") Integer pageSize);

}
