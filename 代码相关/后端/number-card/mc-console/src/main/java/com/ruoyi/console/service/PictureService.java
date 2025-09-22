package com.ruoyi.console.service;


import com.ruoyi.common.exception.BizException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:11
 */
public interface PictureService  {

    /**
     * 文件上传
     * @param multipartFile
     * @return
     * @throws IOException
     */
     String uploadPicture(MultipartFile multipartFile) throws BizException;


    /**
     * 文件删除
     * @return
     * @throws IOException
     */
     void deletePicture(String url) throws BizException;

}
