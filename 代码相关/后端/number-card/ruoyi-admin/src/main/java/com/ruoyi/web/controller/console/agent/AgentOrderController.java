package com.ruoyi.web.controller.console.agent;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AgentOrderSelectBO;
import com.ruoyi.common.order.vo.AgentOrderSelectVO;
import com.ruoyi.console.service.AgentAccountService;
import com.ruoyi.console.service.AgentOrderService;
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
 * 代理商订单相关接口
 */
@RestController
@RequestMapping("/agentOrder")
@Slf4j
@Api(tags = "代理商订单相关接口")
public class AgentOrderController {
    public static final String TAG = "AgentOrderController";

    @Resource
    AgentOrderService agentOrderService;


    /**
     * 代理商分页查询订单
     *
     * @return
     */
    @PostMapping("/agentSelectOrderListPage")
    @ApiOperation("代理商分页查询订单")
    public ResponseEntity<PageResult<AgentOrderSelectVO>> agentSelectOrderListPage(@RequestBody AgentOrderSelectBO agentOrderSelectBO) {
        try {
            return ResponseEntity.success(agentOrderService.agentSelectOrderListPage(agentOrderSelectBO, getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(), null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }







}
