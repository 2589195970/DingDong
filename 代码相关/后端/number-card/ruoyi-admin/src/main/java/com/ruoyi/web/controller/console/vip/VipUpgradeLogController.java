package com.ruoyi.web.controller.console.vip;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.vip.bo.VipUpgradeLogQueryBO;
import com.ruoyi.common.vip.vo.VipUpgradeLogVO;
import com.ruoyi.console.service.VipUpgradeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * VIP升级日志接口
 *
 * @author Codex
 */
@RestController
@RequestMapping("/vip/upgradeLog")
@Api(tags = "VIP升级日志")
@Slf4j
public class VipUpgradeLogController {

    private static final String TAG = "VipUpgradeLogController";

    @Resource
    private VipUpgradeLogService vipUpgradeLogService;

    @PostMapping("/list")
    @ApiOperation("分页查询升级日志")
    public ResponseEntity<PageResult<VipUpgradeLogVO>> list(@RequestBody VipUpgradeLogQueryBO queryBO) {
        try {
            PageResult<VipUpgradeLogVO> pageResult = vipUpgradeLogService.selectVipUpgradeLogPage(queryBO);
            return ResponseEntity.success(pageResult);
        } catch (Exception e) {
            log.error("{}#list error", TAG, e);
            return ResponseEntity.error("查询失败，请稍后重试", null);
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出升级日志")
    public ResponseEntity<Integer> export(@RequestBody(required = false) List<Long> ids) {
        try {
            List<VipUpgradeLogVO> dataList = (ids == null || ids.isEmpty())
                    ? vipUpgradeLogService.selectVipUpgradeLogList(new VipUpgradeLogQueryBO())
                    : vipUpgradeLogService.selectVipUpgradeLogListByIds(ids);
            return ResponseEntity.success(dataList.size());
        } catch (Exception e) {
            log.error("{}#export error", TAG, e);
            return ResponseEntity.error("导出失败，请稍后重试", 0);
        }
    }
}
