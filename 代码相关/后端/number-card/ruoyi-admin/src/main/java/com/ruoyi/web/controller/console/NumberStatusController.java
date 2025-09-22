package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.OrderNumberStatusLogSelectBO;
import com.ruoyi.common.order.bo.UploadNumberListExcelBO;
import com.ruoyi.common.order.entity.NumberStatusLog;
import com.ruoyi.console.service.NumberStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 号码状态查询相关接口
 */
@RestController
@RequestMapping("/numberStatus")
@Slf4j
@Api(tags = "号码状态查询相关接口")
public class NumberStatusController {
    public static final String TAG = "AddressController";

    @Resource
    NumberStatusService numberStatusService;


    /**
     * 根据类型查询号码数据
     * @return
     */
    @GetMapping("/getPhoneByType")
    @ApiOperation("根据类型查询号码数据")
    public ResponseEntity getPhoneByType(@RequestParam(required = false, value = "phone") String phone,@RequestParam(required = false, value = "type") Integer type) {
        try {
            return ResponseEntity.success(numberStatusService.getPhoneByType(phone,type));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 余额查询日志查询
     *
     * @return
     */
    @PostMapping("/selectNumberStatusLogListPage")
    @ApiOperation("余额查询日志查询")
    public ResponseEntity<PageResult<NumberStatusLog>> selectNumberStatusLogListPage(@RequestBody OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO) {
        try {
            return ResponseEntity.success(numberStatusService.selectNumberStatusLogListPage(orderNumberStatusLogSelectBO));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "selectOrderCommissionListPage", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectOrderCommissionListPage", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }

    /**
     * 余额查询日志导出
     *
     * @return
     */
    @PostMapping("/exportNumberStatusLogList")
    @ApiOperation("余额查询日志导出")
    public void exportNumberStatusLogList(@RequestBody OrderNumberStatusLogSelectBO orderNumberStatusLogSelectBO, HttpServletResponse response) {
        try {
            numberStatusService.exportNumberStatusLogList(orderNumberStatusLogSelectBO,response);
        } catch (BizException e) {
            log.info("{}方法异常:{}", "exportNumberStatusLogList", e.getMessage());
        } catch (Exception e) {
            log.info("{}方法异常:{}", "exportNumberStatusLogList", e.getMessage());
        }
    }

    /**
     * 号码查询导入订单
     *
     * @return
     */
    @PostMapping("/uploadNumberListExcel")
    @ApiOperation("号码导入")
    public ResponseEntity uploadNumberListExcel(UploadNumberListExcelBO uploadOrderListExcelBO) {
        try {
            numberStatusService.uploadNumberListExcel(uploadOrderListExcelBO);
            return ResponseEntity.success();
        } catch (BizException e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "uploadOrderListExcel", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 炫咖移动号码话费余额查询
     * @return
     */
    @GetMapping("/getMobileBalance")
    @ApiOperation("炫咖移动号码话费余额查询")
    public ResponseEntity getMobileBalance(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getMobileBalance(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 炫咖联通号码话费余额查询
     * @return
     */
    /*@GetMapping("/getUnicomBalance")
    @ApiOperation("炫咖联通号码话费余额查询")
    public ResponseEntity getUnicomBalance(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getUnicomBalance(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }*/

    /**
     * 炫咖电信号码话费余额查询
     * @return
     */
    /*@GetMapping("/getTelecomBalance")
    @ApiOperation("炫咖电信号码话费余额查询")
    public ResponseEntity getTelecomBalance(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getTelecomBalance(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }*/

    /**
     * 炫咖携号转网查询
     * @return
     */
    @GetMapping("/getNumberShift")
    @ApiOperation("炫咖携号转网查询")
    public ResponseEntity getNumberShift(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getNumberShift(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 炫咖号码查询查询
     * @return
     */
    @GetMapping("/getNumberQuery")
    @ApiOperation("炫咖号码查询查询")
    public ResponseEntity getNumberQuery(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getNumberQuery(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 炫咖空号检测
     * @return
     */
    @GetMapping("/getEmptyNumberQuery")
    @ApiOperation("炫咖空号检测")
    public ResponseEntity getEmptyNumberQuery(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getEmptyNumberQuery(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 额查查 号码话费余额查询
     * @return
     */
    @GetMapping("/getEccBalance")
    @ApiOperation("额查查号码话费余额查询")
    public ResponseEntity getEccBalance(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getEccBalance(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 额查查 携号转网查询
     * @return
     */
    @GetMapping("/getEccNumberShift")
    @ApiOperation("额查查携号转网查询")
    public ResponseEntity getEccNumberShift(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getEccNumberShift(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 额查查 空号检测
     * @return
     */
    @GetMapping("/getEccEmptyNumberQuery")
    @ApiOperation("额查查空号检测")
    public ResponseEntity getEccEmptyNumberQuery(@RequestParam(required = false, value = "phone") String phone) {
        try {
            return ResponseEntity.success(numberStatusService.getEccEmptyNumberQuery(phone));
        } catch (BizException e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error(e.getMessage(),null);
        } catch (Exception e) {
            log.info("{}方法异常:{}", "getMobileBalance", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }



}
