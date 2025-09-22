package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.console.service.ToolConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



@RestController
@RequestMapping("/toolConfig")
@Slf4j
@Api(tags = "工具配置相关接口")
public class ToolConfigController {
    public static final String TAG = "ToolConfigController";

    @Resource
    ToolConfigService toolConfigService;


    /**
     * 查询工具配置
     *
     * @return
     */
    @GetMapping("/getToolConfig")
    @ApiOperation("查询工具配置")
    public ResponseEntity<ToolConfig> getToolConfig(@RequestParam(required = false, value = "toolConfigType") Integer toolConfigType) {
        try {
            return ResponseEntity.success(toolConfigService.getToolConfig(toolConfigType));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 更新工具配置
     *
     * @return
     */
    @PostMapping("/updateToolConfig")
    @ApiOperation("更新工具配置")
    public ResponseEntity updateToolConfig(@RequestBody ToolConfig toolConfig) {
        try {
            toolConfigService.updateToolConfig(toolConfig);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 更新推广海报图
     *
     * @return
     */
    @PostMapping("/updateRegisterQrcodeMap")
    @ApiOperation("更新推广海报图")
    public ResponseEntity updateRegisterQrcodeMap(@RequestBody ToolConfig toolConfig) {
        try {
            toolConfigService.updateRegisterQrcodeMap(toolConfig);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

}
