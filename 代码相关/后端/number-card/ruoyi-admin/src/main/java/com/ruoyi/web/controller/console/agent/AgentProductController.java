package com.ruoyi.web.controller.console.agent;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentProductCardFeeChildrenQueryBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideCancelBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideCreateBO;
import com.ruoyi.common.order.bo.AgentProductCardFeeOverrideQueryBO;
import com.ruoyi.common.order.bo.AgentProductSelectBO;
import com.ruoyi.common.order.bo.AgentProductUpdateBO;
import com.ruoyi.common.order.bo.AgentProductUpdateCardFeeBO;
import com.ruoyi.common.order.bo.AgentProductUpdateCommissionBO;
import com.ruoyi.common.order.entity.AgentProduct;
import com.ruoyi.common.order.vo.AgentProductCardFeeChildVO;
import com.ruoyi.common.order.vo.AgentProductCardFeeOverrideVO;
import com.ruoyi.common.order.vo.AgentProductVO;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 代理商相关接口
 */
@RestController
@RequestMapping("/agentProduct")
@Slf4j
@Api(tags = "代理商产品相关接口")
public class AgentProductController {
    public static final String TAG = "AgentProductController";

    @Resource
    AgentProductService agentProductService;

    @Resource
    AgentAccountService agentAccountService;


    /**
     * 代理商分页查询产品
     *
     * @return
     */
    @PostMapping("/agentSelectProductListPage")
    @ApiOperation("代理商分页查询产品")
    public ResponseEntity<PageResult<AgentProductVO>> agentSelectProductListPage(@RequestBody AgentProductSelectBO agentProductSelectBO) {
        try {
            return ResponseEntity.success(agentProductService.agentSelectProductListPage(agentProductSelectBO, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 代理商修改佣金
     *
     * @return
     */
    @PostMapping("/updateProductCommission")
    @ApiOperation("代理商修改佣金")
    public ResponseEntity updateProductCommission(@RequestBody AgentProductUpdateCommissionBO productUpdateStatusBO) {
        try {

            agentProductService.updateAgentProductCommission(productUpdateStatusBO.getProductCode(),
                    agentAccountService.getAgentAccountByUserId(getLoginUser().getUserId(),true).getAgentCode(),productUpdateStatusBO.getProductCommission());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 代理商下游产品分配
     *
     * @return
     */
    @PostMapping("/updateAgentProduct")
    @ApiOperation("代理商下游产品分配")
    public ResponseEntity updateAgentProduct(@RequestBody AgentProductUpdateBO agentProductUpdateBO) {
        try {
            agentProductService.updateAgentProduct(agentProductUpdateBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    @PostMapping("/updateCardFee")
    @ApiOperation("代理商修改下级提卡费")
    public ResponseEntity updateCardFee(@RequestBody AgentProductUpdateCardFeeBO request) {
        try {
            AgentProduct agentProduct = agentProductService.updateAgentProductCardFee(request, getLoginUser());
            JSONObject data = new JSONObject();
            data.put("cardFeeProfit", agentProduct.getCardFeeProfit());
            data.put("downstreamCardFee", agentProduct.getDownstreamCardFee());
            data.put("incomingCardFee", agentProduct.getIncomingCardFee());
            return ResponseEntity.success(data);
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateCardFee", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateCardFee", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    @PostMapping("/listCardFeeChildren")
    @ApiOperation("查询直属下级提卡费信息")
    public ResponseEntity<List<AgentProductCardFeeChildVO>> listCardFeeChildren(@RequestBody AgentProductCardFeeChildrenQueryBO request) {
        try {
            return ResponseEntity.success(agentProductService.listAgentProductCardFeeChildren(request, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "listCardFeeChildren", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "listCardFeeChildren", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    @PostMapping("/listCardFeeOverrides")
    @ApiOperation("查询提卡费特例列表")
    public ResponseEntity<List<AgentProductCardFeeOverrideVO>> listCardFeeOverrides(@RequestBody AgentProductCardFeeOverrideQueryBO request) {
        try {
            return ResponseEntity.success(agentProductService.listAgentProductCardFeeOverrides(request, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "listCardFeeOverrides", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "listCardFeeOverrides", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    @PostMapping("/upsertCardFeeOverride")
    @ApiOperation("新增或更新提卡费特例")
    public ResponseEntity upsertCardFeeOverride(@RequestBody AgentProductCardFeeOverrideCreateBO request) {
        try {
            agentProductService.upsertAgentProductCardFeeOverride(request, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "upsertCardFeeOverride", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "upsertCardFeeOverride", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    @PostMapping("/cancelCardFeeOverride")
    @ApiOperation("取消提卡费特例")
    public ResponseEntity cancelCardFeeOverride(@RequestBody AgentProductCardFeeOverrideCancelBO request) {
        try {
            agentProductService.cancelAgentProductCardFeeOverride(request, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "cancelCardFeeOverride", e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "cancelCardFeeOverride", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

}
