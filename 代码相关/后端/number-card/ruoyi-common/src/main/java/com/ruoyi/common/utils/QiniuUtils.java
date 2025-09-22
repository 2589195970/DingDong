package com.ruoyi.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.ruoyi.common.order.entity.ToolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class QiniuUtils {

    /*@Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.hostName}")
    private String hostName;*/

    public String uploadByBytes(byte[] bytes, String fileName, ToolConfig toolConfig){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration();
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(toolConfig.getAccessKey(), toolConfig.getSecretKey());
        String upToken = auth.uploadToken(toolConfig.getBucket());
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
            return toolConfig.getDomainNameUrl()+putRet.key;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    /**
     *删除图片
     * @return
     */
    public String delete(String url, ToolConfig toolConfig){
        Auth auth = Auth.create(toolConfig.getAccessKey(),toolConfig.getSecretKey());
        Configuration cfg = new Configuration();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(toolConfig.getBucket(),url);
        }catch (QiniuException Q){
            return Q.toString();
        }
        System.out.println("success");
        return null;
    }

}
