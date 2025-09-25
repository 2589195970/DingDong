package com.ruoyi.web.controller.console;

import com.ruoyi.common.order.bo.ProductCopyBO;
import com.ruoyi.common.order.bo.ProductUpdateStatusBO;
import com.ruoyi.console.service.ProductService;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.ProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.ProductSelectBO;
import com.ruoyi.common.order.vo.ProductSelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;


@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = "产品库相关接口")
public class ProductController {
    public static final String TAG = "ProductController";

    @Resource
    ProductService productService;

    /**
     * 分页查询产品
     *
     * @return
     */
    @PostMapping("/selectProductListPage")
    @ApiOperation("分页查询产品")
    public ResponseEntity<PageResult<ProductSelectVO>> selectProductListPage(@RequestBody ProductSelectBO productSelectBO) {
        try {
            return ResponseEntity.success(productService.selectProductListPage(productSelectBO,getLoginUser()));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectProductListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectProductListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 新增产品
     *
     * @return
     */
    @PostMapping("/addProduct")
    @ApiOperation("新增产品")
    public ResponseEntity addProduct(@RequestBody ProductAddAndUpdateBO productAddAndUpdateBO) {
        try {
            productService.addProduct(productAddAndUpdateBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "addProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 复制产品
     *
     * @return
     */
    @PostMapping("/copyProduct")
    @ApiOperation("新增产品")
    public ResponseEntity copyProduct(@RequestBody ProductCopyBO productCopyBO) {
        try {
            productService.copyProduct(productCopyBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "copyProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "copyProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新产品
     *
     * @return
     */
    @PostMapping("/updateProduct")
    @ApiOperation("更新产品")
    public ResponseEntity updateProduct(@RequestBody ProductAddAndUpdateBO productAddAndUpdateBO) {
        try {
            productService.updateProduct(productAddAndUpdateBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProduct", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 更新产品排序
     *
     * @return
     */
    @PostMapping("/updateProductSort")
    @ApiOperation("更新产品排序")
    public ResponseEntity updateProductStatusAndSort(@RequestBody ProductUpdateStatusBO productUpdateStatusBO) {
        try {
            productService.updateProductSort(productUpdateStatusBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductSort", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductSort", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 产品上下架
     *
     * @return
     */
    @PostMapping("/updateProductStatus")
    @ApiOperation("产品上下架")
    public ResponseEntity updateProductStatus(@RequestBody ProductUpdateStatusBO productUpdateStatusBO) {
        try {
            productService.updateProductStatus(productUpdateStatusBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "updateProductStatus", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "updateProductStatus", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 修改佣金
     *
     * @return
     */
    @PostMapping("/updateProductCommission")
    @ApiOperation("修改佣金")
    public ResponseEntity updateProductCommission(@RequestBody ProductUpdateStatusBO productUpdateStatusBO) {
        try {
            productService.updateProductCommission(productUpdateStatusBO, getLoginUser());
            return ResponseEntity.success();
        } catch (BizException e) {
            e.printStackTrace();
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}方法异常:{}", "updateProductCommission", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 删除产品
     *
     * @return
     */
    @GetMapping("/deleteProduct")
    @ApiOperation("删除产品")
    public ResponseEntity deleteProduct(@RequestParam(required = false, value = "productId") Integer productId) {
        try {
            productService.deleteProduct(productId);
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
     * 刷新产品海报图
     *
     * @return
     */
    @GetMapping("/refreshProductPoster")
    @ApiOperation("刷新产品海报图")
    public ResponseEntity refreshProductPoster(@RequestParam(required = false, value = "productCode") String productCode) {
        try {
            productService.refreshProductPoster(productCode,getLoginUser());
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "refreshProductPoster", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 刷新产品佣金
     *
     * @return
     */
    /*@GetMapping("/refreshProductCommission")
    @ApiOperation("刷新产品佣金")
    public ResponseEntity refreshProductCommission() {
        try {
            productService.refreshProductCommission(getLoginUser());
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "refreshProductPoster", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }*/

    /**
     * 查询产品分类数统计
     * @return
     */
    @GetMapping("/getProductCategoryCount")
    @ApiOperation("查询产品分类数统计")
    public ResponseEntity<Map<String, Object>> getProductCategoryCount() {
        try {
            return ResponseEntity.success(productService.getProductCategoryCount());
        } catch (BizException e) {
            log.info("{} getProductCategoryCount方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{} getProductCategoryCount方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("查询产品分类数出错,请稍候重试",null);
        }
    }

}
