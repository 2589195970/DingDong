package com.ruoyi.web.controller.console.vip;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.vip.bo.VipUserQueryBO;
import com.ruoyi.common.vip.bo.VipUserSaveBO;
import com.ruoyi.common.vip.bo.VipUserSetLevelBO;
import com.ruoyi.common.vip.vo.VipUserVO;
import com.ruoyi.console.service.VipUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * VIP用户管理接口
 *
 * @author Codex
 */
@RestController
@RequestMapping("/vip/user")
@Api(tags = "VIP用户管理")
@Slf4j
public class VipUserController {

    private static final String TAG = "VipUserController";

    @Resource
    private VipUserService vipUserService;

    @PostMapping("/list")
    @ApiOperation("分页查询VIP用户")
    public ResponseEntity<PageResult<VipUserVO>> list(@RequestBody VipUserQueryBO queryBO) {
        try {
            PageResult<VipUserVO> pageResult = vipUserService.selectVipUserPage(queryBO);
            return ResponseEntity.success(pageResult);
        } catch (Exception e) {
            log.error("{}#list error", TAG, e);
            return ResponseEntity.error("查询失败，请稍后重试", null);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("查询VIP用户详情")
    public ResponseEntity<VipUserVO> detail(@PathVariable("id") Long id) {
        try {
            VipUserVO detail = vipUserService.selectVipUserDetail(id);
            return ResponseEntity.success(detail);
        } catch (Exception e) {
            log.error("{}#detail error", TAG, e);
            return ResponseEntity.error("查询失败，请稍后重试", null);
        }
    }

    @PostMapping
    @ApiOperation("新增VIP用户")
    public ResponseEntity<Void> add(@Validated @RequestBody VipUserSaveBO saveBO) {
        LoginUser loginUser = getLoginUser();
        try {
            vipUserService.addVipUser(saveBO, loginUser);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#add BizException: {}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#add error", TAG, e);
            return ResponseEntity.error("新增失败，请稍后重试");
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("修改VIP用户")
    public ResponseEntity<Void> edit(@PathVariable("id") Long id, @Validated @RequestBody VipUserSaveBO saveBO) {
        LoginUser loginUser = getLoginUser();
        saveBO.setId(id);
        try {
            vipUserService.updateVipUser(id, saveBO, loginUser);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#edit BizException: {}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#edit error", TAG, e);
            return ResponseEntity.error("修改失败，请稍后重试");
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除VIP用户")
    public ResponseEntity<Void> remove(@PathVariable("ids") String ids) {
        LoginUser loginUser = getLoginUser();
        try {
            List<Long> idList = Arrays.stream(StringUtils.split(ids, ','))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            vipUserService.deleteVipUserByIds(idList, loginUser);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#remove BizException: {}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#remove error", TAG, e);
            return ResponseEntity.error("删除失败，请稍后重试");
        }
    }

    @PutMapping("/setLevel")
    @ApiOperation("设置VIP等级")
    public ResponseEntity<Void> setLevel(@Validated @RequestBody VipUserSetLevelBO setLevelBO) {
        LoginUser loginUser = getLoginUser();
        try {
            vipUserService.setVipLevel(setLevelBO, loginUser);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.warn("{}#setLevel BizException: {}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage());
        } catch (Exception e) {
            log.error("{}#setLevel error", TAG, e);
            return ResponseEntity.error("设置失败，请稍后重试");
        }
    }

    @PostMapping("/supplement")
    @ApiOperation("补录VIP用户数据")
    public ResponseEntity<Integer> supplement() {
        LoginUser loginUser = getLoginUser();
        try {
            int count = vipUserService.supplementVipUsers(loginUser);
            return ResponseEntity.success(count);
        } catch (BizException e) {
            log.warn("{}#supplement BizException: {}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), 0);
        } catch (Exception e) {
            log.error("{}#supplement error", TAG, e);
            return ResponseEntity.error("补录失败，请稍后重试", 0);
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出VIP用户")
    public ResponseEntity<Integer> export(@RequestBody(required = false) List<Long> ids) {
        try {
            List<VipUserVO> dataList = vipUserService.exportVipUsers(ids == null ? Collections.emptyList() : ids);
            return ResponseEntity.success(dataList.size());
        } catch (Exception e) {
            log.error("{}#export error", TAG, e);
            return ResponseEntity.error("导出失败，请稍后重试", 0);
        }
    }
}
