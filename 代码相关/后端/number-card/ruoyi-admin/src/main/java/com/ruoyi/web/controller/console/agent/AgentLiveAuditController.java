package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.LiveAuditSelectBO;
import com.ruoyi.common.order.entity.LiveAudit;
import com.ruoyi.common.order.entity.LiveConfig;
import com.ruoyi.console.service.AgentLiveAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商直播审核
 *
 * @Description 代理商直播管理控制器
 * @Author Claude Code
 * @Date 2025/01/28
 */
@RestController
@RequestMapping("/agentLiveAudit")
@Slf4j
@Api(tags = "代理商直播审核")
public class AgentLiveAuditController {
    public static final String TAG = "AgentLiveAuditController";

    @Resource
    AgentLiveAuditService agentLiveAuditService;

    /**
     * 代理商直播审核列表查询
     *
     * @param liveAuditSelectBO 查询条件
     * @return 分页结果
     */
    @PostMapping("/selectLiveAuditListPage")
    @ApiOperation("代理商直播审核列表查询")
    public ResponseEntity<PageResult<LiveAudit>> selectLiveAuditListPage(@RequestBody LiveAuditSelectBO liveAuditSelectBO) {
        try {
            return ResponseEntity.success(agentLiveAuditService.selectLiveAuditListPage(liveAuditSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectLiveAuditListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectLiveAuditListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 新增直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @return 响应结果
     */
    @PostMapping("/addLiveAudit")
    @ApiOperation("新增直播审核记录")
    public ResponseEntity addLiveAudit(@RequestBody LiveAudit liveAudit) {
        try {
            agentLiveAuditService.addLiveAudit(liveAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addLiveAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addLiveAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 查询登录用户直播审核记录
     *
     * @return 直播审核记录
     */
    @PostMapping("/selectLiveAudit")
    @ApiOperation("查询登录用户直播审核记录")
    public ResponseEntity<LiveAudit> selectLiveAudit() {
        try {
            return ResponseEntity.success(agentLiveAuditService.selectLiveAudit(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectLiveAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectLiveAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 更新直播审核记录
     *
     * @param liveAudit 直播审核信息
     * @return 响应结果
     */
    @PostMapping("/updateLiveAudit")
    @ApiOperation("更新直播审核记录")
    public ResponseEntity updateLiveAudit(@RequestBody LiveAudit liveAudit) {
        try {
            agentLiveAuditService.updateLiveAudit(liveAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateLiveAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateLiveAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 更新直播审核状态
     *
     * @param liveAudit 直播审核信息
     * @return 响应结果
     */
    @PostMapping("/updateLiveAuditStatus")
    @ApiOperation("更新直播审核状态")
    public ResponseEntity updateLiveAuditStatus(@RequestBody LiveAudit liveAudit) {
        try {
            agentLiveAuditService.updateLiveAuditStatus(liveAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateLiveAuditStatus", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateLiveAuditStatus", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 删除直播审核记录
     *
     * @param liveAuditId 直播审核ID
     * @return 响应结果
     */
    @GetMapping("/deleteLiveAudit")
    @ApiOperation("删除直播审核记录")
    public ResponseEntity deleteLiveAudit(@RequestParam(value = "liveAuditId") Integer liveAuditId) {
        try {
            agentLiveAuditService.deleteLiveAudit(liveAuditId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteLiveAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteLiveAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 获取直播配置说明
     *
     * @return 直播配置
     */
    @PostMapping("/getLiveConfig")
    @ApiOperation("获取直播配置说明")
    public ResponseEntity<LiveConfig> getLiveConfig() {
        try {
            return ResponseEntity.success(agentLiveAuditService.getLiveConfig("live_instruction"));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getLiveConfig", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getLiveConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }

    /**
     * 更新直播配置说明
     *
     * @param liveConfig 直播配置
     * @return 响应结果
     */
    @PostMapping("/updateLiveConfig")
    @ApiOperation("更新直播配置说明")
    public ResponseEntity updateLiveConfig(@RequestBody LiveConfig liveConfig) {
        try {
            liveConfig.setConfigKey("live_instruction");
            agentLiveAuditService.updateLiveConfig(liveConfig);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateLiveConfig", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateLiveConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试", null);
        }
    }
}