package com.ruoyi.common.apis.douyin;

import lombok.Data;

import java.util.List;

/**
 * @author 陈思伟
 * @version 1.0
 * @description: 批量解密接口
 * @date 2025/7/12 12:47
 */
@Data
public class DouDianBatchDecryptRequest {

    private List<CipherInfo> cipherInfos;
    private String accountId;
    private String accountType;


}
