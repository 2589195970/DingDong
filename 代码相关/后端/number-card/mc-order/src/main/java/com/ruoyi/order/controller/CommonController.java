package com.ruoyi.order.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.order.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 订单模块通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = "订单模块通用接口")
public class CommonController {

    @Resource
    PictureService pictureService;

    /**
     * 通用上传请求（单个）
     * 支持H5项目图片上传
     */
    @PostMapping("/upload")
    @ApiOperation("通用图片上传接口")
    public AjaxResult uploadFile(MultipartFile file, HttpServletRequest request) {
        try {
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", pictureService.uploadPicture(file));
            return ajax;
        } catch (Exception e) {
            log.error("H5项目文件上传失败: 文件名={}, 错误信息={}",
                    file.getOriginalFilename(), e.getMessage(), e);
            return AjaxResult.error("文件上传失败: " + e.getMessage());
        }
    }

}