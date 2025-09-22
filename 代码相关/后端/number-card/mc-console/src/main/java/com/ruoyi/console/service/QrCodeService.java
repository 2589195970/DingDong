package com.ruoyi.console.service;




/**
 * 二维码生成相关工具类
 * @Description
 * @Author 陈思伟
 * @Date 2024/08/12 17:11
 */
public interface QrCodeService  {

    /***
     * 生成带二维码的 海报图片
     */
    String getQrCodePosterUrl(String content);

    /**
     * 生成产品海报
     * @param productPictureUrl
     * @param basePictureUrl
     * @param content
     * @return
     */
    String getProductPoster(String productPictureUrl,String basePictureUrl, String content);


    /***
     * 生成推广海报图 格式1
     */
    String getRegisterQrcodeMap1Url(String basePictureUrl,String content);


    /***
     * 生成推广海报图 格式3
     */
    String getRegisterQrcodeMap3Url(String basePictureUrl,String content);
}
