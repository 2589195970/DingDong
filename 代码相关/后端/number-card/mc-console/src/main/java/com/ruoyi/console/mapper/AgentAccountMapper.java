package com.ruoyi.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.vo.AgentAccountListVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.common.order.bo.AgentTeamQueryBO;
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

    /**
     * 查询直接下级代理商总数
     * @param queryBO 查询参数
     * @return 总数
     */
    Integer selectDirectSubAgentsCount(@Param("queryBO") AgentTeamQueryBO queryBO);

    /**
     * 查询直接下级代理商列表
     * @param queryBO 查询参数
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 代理商列表
     */
    List<AgentAccountListVO> selectDirectSubAgentsList(@Param("queryBO") AgentTeamQueryBO queryBO,
                                                       @Param("offset") Integer offset,
                                                       @Param("pageSize") Integer pageSize);

    /**
     * 统计某个代理商的团队成员数（直接下级数量）
     * @param parentAgentCode 父代理商编码
     * @return 团队成员数
     */
    Integer countAgentTeamMembers(@Param("parentAgentCode") String parentAgentCode);

}
