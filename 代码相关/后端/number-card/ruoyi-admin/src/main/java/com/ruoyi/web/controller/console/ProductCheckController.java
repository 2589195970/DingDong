package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.ProductCheckConfig;
import com.ruoyi.console.service.ProductCheckConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



@RestController
@RequestMapping("/productCheckConfig")
@Slf4j
@Api(tags = "产品库校验相关接口")
public class ProductCheckController {
    public static final String TAG = "ProductCheckController";

    @Resource
    ProductCheckConfigService productCheckConfigService;

    /**
     * 分页查询产品校验配置列表
     *
     * @return
     */
    @PostMapping("/selectProductCheckConfigListPage")
    @ApiOperation("分页查询产品校验配置列表")
    public ResponseEntity<PageResult<ProductCheckConfig>> selectProductCheckConfigListPage(@RequestBody ProductCheckSelectBO productCheckSelectBO) {
        try {
            return ResponseEntity.success(productCheckConfigService.selectProductCheckConfigListPage(productCheckSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectProductCheckConfigListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectProductCheckConfigListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 新增产品校验配置
     *
     * @return
     */
    @PostMapping("/addProductCheckConfig")
    @ApiOperation("新增产品校验配置")
    public ResponseEntity addProductCheckConfig(@RequestBody ProductCheckConfigAddBO productCheckConfigAddBO) {
        try {
            productCheckConfigService.addProductCheckConfig(productCheckConfigAddBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addProductCheckConfig", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addProductCheckConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 新增产品校验配置
     *
     * @return
     */
    @PostMapping("/updateProductCheckConfig")
    @ApiOperation("新增产品校验配置")
    public ResponseEntity updateProductCheckConfig(@RequestBody ProductCheckConfigUpdateBO productCheckConfigUpdateBO) {
        try {
            productCheckConfigService.updateProductCheckConfig(productCheckConfigUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductCheckConfig", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductCheckConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }



    /**
     * 删除产品校验配置
     *
     * @return
     */
    @GetMapping("/deleteProductCheckConfig")
    @ApiOperation("删除产品校验配置")
    public ResponseEntity deleteProductCheckConfig(@RequestParam(required = false, value = "productCheckConfigId") Integer productCheckConfigId) {
        try {
            productCheckConfigService.deleteProductCheckConfig(productCheckConfigId);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteProductCheckConfig", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }



}
