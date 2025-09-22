package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamExplain;
import com.ruoyi.common.order.entity.UpstreamProduct;
import com.ruoyi.common.order.vo.UpstreamApiTypeVO;
import com.ruoyi.common.order.vo.UpstreamProductVO;
import com.ruoyi.console.service.UpstreamApiService;
import com.ruoyi.console.service.UpstreamProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/upstream")
@Slf4j
@Api(tags = "上游API相关接口")
public class UpstreamController {
    public static final String TAG = "UpstreamController";

    @Resource
    UpstreamApiService upstreamApiService;

    @Resource
    UpstreamProductService upstreamProductService;

    /**
     * 分页查询上游接口
     *
     * @return
     */
    @PostMapping("/api/selectUpstreamApiListPage")
    @ApiOperation("分页查询上游接口列表")
    public ResponseEntity<PageResult<UpstreamApi>> selectUpstreamApiListPage(@RequestBody UpstreamApiSelectBO upstreamApiSelectBO) {
        try {
            return ResponseEntity.success(upstreamApiService.selectUpstreamApiListPage(upstreamApiSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectUpstreamApiListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectUpstreamApiListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 新增上游接口
     *
     * @return
     */
    @PostMapping("/api/addUpstreamApi")
    @ApiOperation("新增上游接口")
    public ResponseEntity addUpstreamApi(@RequestBody UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) {
        try {
            upstreamApiService.addUpstreamApi(upstreamApiAddAndUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addUpstreamApi", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addUpstreamApi", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新上游接口
     *
     * @return
     */
    @PostMapping("/api/updateUpstreamApi")
    @ApiOperation("更新上游接口")
    public ResponseEntity updateUpstreamApi(@RequestBody UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) {
        try {
            upstreamApiService.updateUpstreamApi(upstreamApiAddAndUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateUpstreamApi", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateUpstreamApi", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 删除上游接口
     *
     * @return
     */
    @GetMapping("/api/deleteUpstreamApi")
    @ApiOperation("删除上游接口")
    public ResponseEntity deleteUpstreamApi(@RequestParam(required = false, value = "upstreamApiId") Integer upstreamApiId) {
        try {
            upstreamApiService.deleteUpstreamApi(upstreamApiId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteUpstreamApi", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteUpstreamApi", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 获取上游接口API类型
     *
     * @return
     */
    @GetMapping("/api/selectUpstreamApiTypeList")
    @ApiOperation("获取上游接口API类型")
    public ResponseEntity<List<UpstreamApiTypeVO>> selectUpstreamApiTypeList(@RequestParam(required = false,value = "upstreamApiType") String upstreamApiType) {
        try {
            return ResponseEntity.success(upstreamApiService.selectUpstreamApiTypeList(upstreamApiType));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectUpstreamApiTypeList", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectUpstreamApiTypeList", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 查询产品参数说明
     *
     * @return
     */
    @GetMapping("/api/selectUpstreamExplain")
    @ApiOperation("查询产品参数说明 upstreamApiType 上游API类型' explainType 说明类型 0 API说明 1产品说明")
    public ResponseEntity<UpstreamExplain> selectUpstreamExplain(@RequestParam(value = "upstreamApiType") String upstreamApiType,@RequestParam(value = "explainType") Integer explainType) {
        try {
            return ResponseEntity.success(upstreamApiService.selectUpstreamExplain(upstreamApiType,explainType));
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectUpstreamExplain", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 分页查询上游产品列表
     *
     * @return
     */
    @PostMapping("/product/selectUpstreamProductListPage")
    @ApiOperation("分页查询上游产品列表")
    public ResponseEntity<PageResult<UpstreamProductVO>> selectUpstreamProductListPage(@RequestBody UpstreamProductSelectBO upstreamProductSelectBO) {
        try {
            return ResponseEntity.success(upstreamProductService.selectUpstreamProductListPage(upstreamProductSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectUpstreamProductListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectUpstreamProductListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 新增上游产品
     *
     * @return
     */
    @PostMapping("/product/addUpstreamProduct")
    @ApiOperation("新增上游产品")
    public ResponseEntity addUpstreamProduct(@RequestBody UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) {
        try {
            upstreamProductService.addUpstreamProduct(upstreamProductAddAndUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addUpstreamProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addUpstreamProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新上游产品
     *
     * @return
     */
    @PostMapping("/product/updateUpstreamProduct")
    @ApiOperation("更新上游产品")
    public ResponseEntity updateUpstreamProduct(@RequestBody UpstreamProductAddAndUpdateBO upstreamProductAddAndUpdateBO) {
        try {
            upstreamProductService.updateUpstreamProduct(upstreamProductAddAndUpdateBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateUpstreamProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateUpstreamProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 删除上游产品
     *
     * @return
     */
    @GetMapping("/product/deleteUpstreamProduct")
    @ApiOperation("删除上游产品")
    public ResponseEntity deleteUpstreamProduct(@RequestParam(required = false, value = "upstreamProductId") Integer upstreamProductId) {
        try {
            upstreamProductService.deleteUpstreamProduct(upstreamProductId);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "deleteUpstreamProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deleteUpstreamProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }




}
