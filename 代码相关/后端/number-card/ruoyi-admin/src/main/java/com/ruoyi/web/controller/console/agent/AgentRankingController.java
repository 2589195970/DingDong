package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.order.bo.AgentRankingQueryBO;
import com.ruoyi.common.order.vo.AgentRankingVO;
import com.ruoyi.console.service.AgentRankingService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理商排行榜Controller
 *
 * @author ruoyi
 */
@Api(tags = "代理商排行榜管理")
@RestController
@RequestMapping("/console/agent/ranking")
public class AgentRankingController extends BaseController {

    @Autowired
    private AgentRankingService agentRankingService;

    /**
     * 获取代理商日榜
     */
    @ApiOperation(value = "获取代理商日榜", notes = "获取代理商各项指标的日排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rankingType", value = "排行类型：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "topCount", value = "查询TOP数量，默认10", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "parentAgentCode", value = "父代理商编码（查询团队内部排行）", dataType = "string", paramType = "query")
    })
    @GetMapping("/daily")
    public AjaxResult getDailyRanking(
            @RequestParam Integer rankingType,
            @RequestParam(defaultValue = "10") Integer topCount,
            @RequestParam(required = false) String parentAgentCode) {

        // 参数校验
        if (rankingType == null || rankingType < 1 || rankingType > 4) {
            return AjaxResult.error("排行类型参数错误，支持：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行");
        }

        if (topCount > 100) {
            return AjaxResult.error("查询数量不能超过100");
        }

        List<AgentRankingVO> rankings = agentRankingService.getDailyRanking(rankingType, topCount, parentAgentCode);
        return AjaxResult.success(rankings);
    }

    /**
     * 获取代理商月榜
     */
    @ApiOperation(value = "获取代理商月榜", notes = "获取代理商各项指标的月排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rankingType", value = "排行类型：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "topCount", value = "查询TOP数量，默认10", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "parentAgentCode", value = "父代理商编码（查询团队内部排行）", dataType = "string", paramType = "query")
    })
    @GetMapping("/monthly")
    public AjaxResult getMonthlyRanking(
            @RequestParam Integer rankingType,
            @RequestParam(defaultValue = "10") Integer topCount,
            @RequestParam(required = false) String parentAgentCode) {

        // 参数校验
        if (rankingType == null || rankingType < 1 || rankingType > 4) {
            return AjaxResult.error("排行类型参数错误，支持：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行");
        }

        if (topCount > 100) {
            return AjaxResult.error("查询数量不能超过100");
        }

        List<AgentRankingVO> rankings = agentRankingService.getMonthlyRanking(rankingType, topCount, parentAgentCode);
        return AjaxResult.success(rankings);
    }

    /**
     * 获取自定义时间范围排行榜
     */
    @ApiOperation(value = "获取自定义时间范围排行榜", notes = "根据自定义时间范围获取代理商排行榜")
    @PostMapping("/custom")
    @Log(title = "代理商排行榜", businessType = BusinessType.EXPORT)
    public AjaxResult getCustomRanking(@RequestBody AgentRankingQueryBO queryBO) {

        // 参数校验
        if (queryBO.getRankingType() == null || queryBO.getRankingType() < 1 || queryBO.getRankingType() > 4) {
            return AjaxResult.error("排行类型参数错误，支持：1-佣金排行 2-订单量排行 3-激活量排行 4-团队发展排行");
        }

        if (queryBO.getTopCount() == null) {
            queryBO.setTopCount(10);
        }

        if (queryBO.getTopCount() > 100) {
            return AjaxResult.error("查询数量不能超过100");
        }

        if (queryBO.getStartTime() == null || queryBO.getEndTime() == null) {
            return AjaxResult.error("开始时间和结束时间不能为空");
        }

        if (queryBO.getStartTime() >= queryBO.getEndTime()) {
            return AjaxResult.error("开始时间不能大于等于结束时间");
        }

        List<AgentRankingVO> rankings = agentRankingService.getAgentRanking(queryBO);
        return AjaxResult.success(rankings);
    }

    /**
     * 获取排行榜类型说明
     */
    @ApiOperation(value = "获取排行榜类型说明", notes = "获取所有支持的排行榜类型和说明")
    @GetMapping("/types")
    public AjaxResult getRankingTypes() {
        return AjaxResult.success()
                .put("1", "佣金排行 - 按代理商获得的佣金收入排序")
                .put("2", "订单量排行 - 按代理商完成的订单数量排序")
                .put("3", "激活量排行 - 按代理商激活的订单数量排序")
                .put("4", "团队发展排行 - 按代理商直接邀请的下级数量排序");
    }
}