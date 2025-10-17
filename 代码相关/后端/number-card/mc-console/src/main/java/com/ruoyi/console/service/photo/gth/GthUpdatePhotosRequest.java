package com.ruoyi.console.service.photo.gth;

import lombok.Data;

/**
 * 感叹号更新照片请求
 *
 * @author Claude
 * @date 2025/01/17
 */
@Data
public class GthUpdatePhotosRequest {

    /**
     * 秘钥（从接口获取）
     */
    private String str_rand;

    /**
     * 身份证人像面URL地址
     */
    private String face_url;

    /**
     * 身份证背面URL地址
     */
    private String back_url;

    /**
     * 人像面URL地址(手持身份证)
     */
    private String hand_url;

    /**
     * 自定义照片URL地址（学生证）
     */
    private String custom_photos_url;
}