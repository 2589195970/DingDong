package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.NameAudit;
import com.ruoyi.console.service.AgentNameAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商实名审核
 */
@RestController
@RequestMapping("/agentNameAudit")
@Slf4j
@Api(tags = "代理商实名审核")
public class AgentNameAuditController {
    public static final String TAG = "AgentNameAuditController";

    @Resource
    AgentNameAuditService agentNameAuditService;

    /**
     * 代理商实名认证列表查询
     *
     * @return
     */
    @PostMapping("/selectNameAuditListPage")
    @ApiOperation("代理商实名认证列表查询")
    public ResponseEntity<PageResult<NameAudit>> selectNameAuditListPage(@RequestBody NameAuditSelectBO nameAuditSelectBO) {
        try {
            return ResponseEntity.success(agentNameAuditService.selectNameAuditListPage(nameAuditSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectProductListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectProductListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 新增实名审核记录
     *
     * @return
     */
    @PostMapping("/addNameAudit")
    @ApiOperation("新增实名审核记录")
    public ResponseEntity addNameAudit(@RequestBody NameAudit nameAudit) {
        try {
            agentNameAuditService.addNameAudit(nameAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addNameAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addNameAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 查询登陆用户实名审核
     *
     * @return
     */
    @PostMapping("/selectNameAudit")
    @ApiOperation("查询登陆用户实名审核")
    public ResponseEntity<NameAudit> selectNameAudit() {
        try {
            return ResponseEntity.success(agentNameAuditService.selectNameAudit(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectNameAudit", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectNameAudit", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新实名审核记录
     *
     * @return
     */
    @PostMapping("/updateNameAudit")
    @ApiOperation("更新实名审核记录")
    public ResponseEntity updateNameAudit(@RequestBody NameAudit nameAudit) {
        try {
            agentNameAuditService.updateNameAudit(nameAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新实名审核状态
     *
     * @return
     */
    @PostMapping("/updateNameAuditStatus")
    @ApiOperation("更新实名审核状态")
    public ResponseEntity updateNameAuditStatus(@RequestBody NameAudit nameAudit) {
        try {
            agentNameAuditService.updateNameAuditStatus(nameAudit);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductSort", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductSort", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }



    /**
     * 删除实名审核记录
     *
     * @return
     */
    @GetMapping("/nameAuditId")
    @ApiOperation("删除实名审核记录")
    public ResponseEntity deleteProduct(@RequestParam(required = false, value = "nameAuditId") Integer nameAuditId) {
        try {
            agentNameAuditService.deleteNameAudit(nameAuditId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }



}
