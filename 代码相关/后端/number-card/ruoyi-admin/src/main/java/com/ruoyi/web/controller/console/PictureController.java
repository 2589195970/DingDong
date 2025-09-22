package com.ruoyi.web.controller.console;

import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.console.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * 图片上传相关接口
 */
@RestController
@RequestMapping("/picture")
@Slf4j
@Api(tags = "图片相关接口")
public class PictureController {
    public static final String TAG = "PictureController";

    @Resource
    PictureService pictureService;


    /**
     * 图片上传
     *
     * @return
     */
    @PostMapping("/addPicture")
    @ApiOperation("图片上传")
    public ResponseEntity addPicture(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.success(pictureService.uploadPicture(file));
        } catch (Exception e) {
            log.info("{}方法异常:{}", "addPicture", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }


    /**
     * 文件删除
     *
     * @return
     */
    @GetMapping("/deletePicture")
    @ApiOperation("图片删除")
    public ResponseEntity deletePicture(@RequestParam(required = false, value = "pictureUrl") String pictureUrl) {
        try {
            pictureService.deletePicture(pictureUrl);
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", "deletePicture", e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:"+e.getMessage(), null);
        }
    }







}
