package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.order.bo.WithdrawalConfigUpdateBO;
import com.ruoyi.common.order.entity.WithdrawalConfig;
import com.ruoyi.console.service.WithdrawalConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


/**
 * 提现配置相关接口
 */
@RestController
@RequestMapping("/withdrawal/config")
@Slf4j
@Api(tags = "提现配置相关接口")
public class WithdrawalConfigController {
    public static final String TAG = "WithdrawalConfigController";

    @Resource
    WithdrawalConfigService withdrawalConfigService;

    /**
     * 查询提现配置
     *
     * @return
     */
    @PostMapping("/selectWithdrawalConfig")
    @ApiOperation("查询提现配置")
    public ResponseEntity<WithdrawalConfig> selectWithdrawalConfig() {
        try {
            return ResponseEntity.success(withdrawalConfigService.selectWithdrawalConfig());
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectWithdrawalConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 更新提现配置
     *
     * @return
     */
    @PostMapping("/updateWithdrawalConfig")
    @ApiOperation("更新提现配置")
    public ResponseEntity updateWithdrawalConfig(@RequestBody WithdrawalConfigUpdateBO withdrawalConfigUpdateBO) {
        try {
            withdrawalConfigService.updateWithdrawalConfig(withdrawalConfigUpdateBO,getLoginUser().getUserId());
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateWithdrawalConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }








}
