package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.order.bo.AddressSelectBO;
import com.ruoyi.common.order.vo.AddressSelectVO;
import com.ruoyi.console.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 省市区地址相关接口
 */
@RestController
@RequestMapping("/address")
@Slf4j
@Api(tags = "省市区地址相关接口")
public class AddressController {
    public static final String TAG = "AddressController";

    @Resource
    AddressService addressService;

    /**
     * 查询省市区地址列表
     *
     * @return
     */
    @PostMapping("/selectAddressList")
    @ApiOperation("查询省市区地址列表")
    public ResponseEntity<AddressSelectVO> selectAddressList(@RequestBody AddressSelectBO addressSelectBO) {
        try {
            return ResponseEntity.success(addressService.selectAddressList(addressSelectBO));
        } catch (Exception e) {
            log.info("{}方法异常:{}", "selectAddressList", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }









}
