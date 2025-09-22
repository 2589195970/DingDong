package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.WorkOrderAddBO;
import com.ruoyi.common.order.bo.WorkOrderSelectBO;
import com.ruoyi.common.order.entity.WorkOrderRecover;
import com.ruoyi.common.order.vo.AgentApiVO;
import com.ruoyi.common.order.vo.AgentExtendUrlVO;
import com.ruoyi.common.order.vo.AgentInfoVO;
import com.ruoyi.common.order.vo.WorkOrderSelectVO;
import com.ruoyi.console.service.AgentExtendUrlService;
import com.ruoyi.console.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


@RestController
@RequestMapping("/agentExtendUrl")
@Slf4j
@Api(tags = "代理商信息相关接口")
public class AgentExtendUrlController {
    public static final String TAG = "AgentExtendUrlController";

    @Resource
    AgentExtendUrlService agentExtendUrlService;

    /**
     * 获取推广链接信息
     *
     * @return
     */
    @GetMapping("/getAgentExtendUrlVO")
    @ApiOperation("获取推广链接信息")
    public ResponseEntity<AgentExtendUrlVO> getAgentExtendUrlVO() {
        try {
            return ResponseEntity.success(agentExtendUrlService.getAgentExtendUrlVO(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getAgentExtendUrlVO", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getAgentExtendUrlVO", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 获取API 信息
     *
     * @return
     */
    @GetMapping("/getAgentApiVO")
    @ApiOperation("获取API")
    public ResponseEntity<AgentApiVO> getAgentApiVO() {
        try {
            return ResponseEntity.success(agentExtendUrlService.getAgentApiVO(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新回调url信息
     *
     * @return
     */
    @GetMapping("/updateCallbackUrl")
    @ApiOperation("更新回调url信息")
    public ResponseEntity updateCallbackUrl(@RequestParam(required = false, value = "agentCode") String agentCode,@RequestParam(required = false, value = "callbackUrl") String callbackUrl) {
        try {
            agentExtendUrlService.updateCallbackUrl(agentCode,callbackUrl);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 获取账号信息
     *
     * @return
     */
    @GetMapping("/getAgentInfoVO")
    @ApiOperation("获取账号信息")
    public ResponseEntity<AgentInfoVO> getAgentInfoVO() {
        try {
            return ResponseEntity.success(agentExtendUrlService.getAgentInfoVO(getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 更新用户手机号
     * @return
     */
    @GetMapping("/updateAgentPhone")
    @ApiOperation("更新用户手机号")
    public ResponseEntity updateAgentPhone(@RequestParam(required = false, value = "phone") String phone,@RequestParam(required = false, value = "smsCode") String smsCode) {
        try {
            agentExtendUrlService.updateAgentPhone(getLoginUser(),phone,smsCode);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getAgentApiVO", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

}
