package com.ruoyi.console.service.impl;

import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.common.utils.QiniuUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.service.PictureService;
import com.ruoyi.console.service.ToolConfigService;
//import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;


/**
 * 图片上传相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2025/01/07 17:13
 */
@Service
public class PictureServiceImpl  implements PictureService {


    @Resource
    private QiniuUtils qiniuUtils;

    @Resource
    ToolConfigService toolConfigService;



    /**
     * 文件上传
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String uploadPicture(MultipartFile multipartFile) throws BizException {
        try {
            //获取文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //为了避免同名覆盖问题,构建新的文件名
            String fileName = UUID.randomUUID().toString() + suffix;
            //调用七牛云OSS工具类
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            String url = qiniuUtils.uploadByBytes(multipartFile.getBytes(), BaseConstant.PICTURE_URL +fileName,toolConfig);
            //将图片上传完成后的url返回，用于浏览器回显展示
            return url;
        }catch (Exception e){
            throw new BizException("文件上传失败:{}",e.getMessage());
        }
    }

    /**
     * 文件删除
     * @return
     * @throws IOException
     */
    public void deletePicture(String url) throws BizException {
        if(StringUtils.isEmpty(url)){
            return;
        }
        if(!url.contains(BaseConstant.PICTURE_URL)){
            throw new BizException("文件目录不存在");
        }
        try {
            String deleteUrl = url.substring(url.indexOf(BaseConstant.PICTURE_URL));
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            qiniuUtils.delete(deleteUrl,toolConfig);
        }catch (Exception e){
            throw new BizException("文件删除失败:{}",e.getMessage());
        }
    }

}
