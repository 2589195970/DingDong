package com.ruoyi.web.controller.console.vip;

import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.vip.bo.VipConfigQueryBO;
import com.ruoyi.common.vip.bo.VipConfigSaveBO;
import com.ruoyi.common.vip.entity.VipConfig;
import com.ruoyi.common.vip.entity.VipOperationLog;
import com.ruoyi.common.vip.vo.VipConfigVO;
import com.ruoyi.console.service.VipConfigService;
import com.ruoyi.console.service.VipOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * VIP配置管理接口
 *
 * @author Codex
 */
@RestController
@RequestMapping("/vip/config")
@Api(tags = "VIP配置管理")
@Slf4j
public class VipConfigController {

    private static final String TAG = "VipConfigController";

    @Resource
    private VipConfigService vipConfigService;

    @Resource
    private VipOperationLogService vipOperationLogService;

    @PostMapping("/list")
    @ApiOperation("分页查询VIP配置列表")
    public ResponseEntity<PageResult<VipConfigVO>> list(@RequestBody VipConfigQueryBO queryBO) {
        long start = System.currentTimeMillis();
        try {
            PageResult<VipConfigVO> pageResult = vipConfigService.selectVipConfigPage(queryBO);
            recordOperation("LIST", "list", queryBO, "success", start, 1);
            return ResponseEntity.success(pageResult);
        } catch (BizException e) {
            log.warn("{}#list BizException: {}", TAG, e.getMessage(), e);
            recordOperation("LIST", "list", queryBO, e.getMessage(), start, 0);
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.error("{}#list Exception", TAG, e);
            recordOperation("LIST", "list", queryBO, e.getMessage(), start, 0);
            return ResponseEntity.error("查询失败，请稍后重试", null);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("查询VIP配置详情")
    public ResponseEntity<VipConfigVO> detail(@PathVariable("id") Long id) {
        try {
            VipConfigVO vo = vipConfigService.selectVipConfigDetail(id);
            return ResponseEntity.success(vo);
        } catch (Exception e) {
            log.error("{}#detail Exception", TAG, e);
            return ResponseEntity.error("查询失败，请稍后重试", null);
        }
    }

    @PostMapping
    @ApiOperation("新增VIP配置")
    public ResponseEntity<Void> add(@Validated @RequestBody VipConfigSaveBO saveBO) {
        long start = System.currentTimeMillis();
        LoginUser loginUser = getLoginUser();
        try {
            vipConfigService.addVipConfig(saveBO, loginUser);
            recordOperation("ADD", "add", saveBO, "success", start, 1);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#add BizException: {}", TAG, e.getMessage(), e);
            recordOperation("ADD", "add", saveBO, e.getMessage(), start, 0);
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#add Exception", TAG, e);
            recordOperation("ADD", "add", saveBO, e.getMessage(), start, 0);
            return ResponseEntity.error("新增失败，请稍后重试");
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("修改VIP配置")
    public ResponseEntity<Void> edit(@PathVariable("id") Long id, @Validated @RequestBody VipConfigSaveBO saveBO) {
        long start = System.currentTimeMillis();
        LoginUser loginUser = getLoginUser();
        saveBO.setId(id);
        try {
            vipConfigService.updateVipConfig(saveBO, loginUser);
            recordOperation("EDIT", "edit", saveBO, "success", start, 1);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#edit BizException: {}", TAG, e.getMessage(), e);
            recordOperation("EDIT", "edit", saveBO, e.getMessage(), start, 0);
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#edit Exception", TAG, e);
            recordOperation("EDIT", "edit", saveBO, e.getMessage(), start, 0);
            return ResponseEntity.error("修改失败，请稍后重试");
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除VIP配置")
    public ResponseEntity<Void> remove(@PathVariable("ids") String ids) {
        long start = System.currentTimeMillis();
        LoginUser loginUser = getLoginUser();
        List<Long> idList = Arrays.stream(ids.split(","))
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        try {
            vipConfigService.deleteVipConfigByIds(idList, loginUser);
            recordOperation("DELETE", "remove", idList, "success", start, 1);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#remove BizException: {}", TAG, e.getMessage(), e);
            recordOperation("DELETE", "remove", idList, e.getMessage(), start, 0);
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#remove Exception", TAG, e);
            recordOperation("DELETE", "remove", idList, e.getMessage(), start, 0);
            return ResponseEntity.error("删除失败，请稍后重试");
        }
    }

    @PutMapping("/{id}/toggle")
    @ApiOperation("切换VIP配置启用状态")
    public ResponseEntity<Void> toggle(@PathVariable("id") Long id, @RequestParam("enable") boolean enable) {
        long start = System.currentTimeMillis();
        LoginUser loginUser = getLoginUser();
        try {
            vipConfigService.toggleVipConfig(id, enable, loginUser);
            recordOperation("TOGGLE", "toggle", id + ":" + enable, "success", start, 1);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#toggle BizException: {}", TAG, e.getMessage(), e);
            recordOperation("TOGGLE", "toggle", id + ":" + enable, e.getMessage(), start, 0);
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#toggle Exception", TAG, e);
            recordOperation("TOGGLE", "toggle", id + ":" + enable, e.getMessage(), start, 0);
            return ResponseEntity.error("操作失败，请稍后重试");
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出VIP配置")
    public ResponseEntity<Integer> export(@RequestBody(required = false) List<Long> ids) {
        long start = System.currentTimeMillis();
        try {
            List<VipConfig> exportList;
            if (ids == null || ids.isEmpty()) {
                exportList = vipConfigService.selectVipConfigList();
            } else {
                exportList = vipConfigService.listByIds(ids);
            }
            recordOperation("EXPORT", "export", ids, "success", start, 1);
            return ResponseEntity.success(exportList.size());
        } catch (Exception e) {
            log.error("{}#export Exception", TAG, e);
            recordOperation("EXPORT", "export", ids, e.getMessage(), start, 0);
            return ResponseEntity.error("导出失败，请稍后重试", 0);
        }
    }

    private void recordOperation(String operationType, String methodName, Object params, String result,
                                 long startTime, int status) {
        try {
            LoginUser loginUser = getLoginUser();
            VipOperationLog log = new VipOperationLog();
            log.setUserId(loginUser != null ? loginUser.getUserId() : null);
            log.setOperationType(operationType);
            log.setMethodName(TAG + "#" + methodName);
            log.setRequestParams(JSONUtil.toJsonStr(params));
            log.setResult(result);
            log.setStatus(status);
            log.setOperationTime(System.currentTimeMillis());
            log.setExecutionTime((int) (System.currentTimeMillis() - startTime));
            HttpServletRequest request = ServletUtils.getRequest();
            if (request != null) {
                log.setIpAddress(IpUtils.getIpAddr(request));
                log.setUserAgent(request.getHeader("User-Agent"));
            }
            vipOperationLogService.recordOperation(log);
        } catch (Exception e) {
            log.error("{}#recordOperation error", TAG, e);
        }
    }
}

