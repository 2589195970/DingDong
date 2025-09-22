package com.ruoyi.web.controller.console;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.ResponseEntity;
import com.ruoyi.common.order.entity.ParentAgentJson;
import com.ruoyi.console.service.QrCodeService;
import com.ruoyi.console.service.TestToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 测试方法相关接口
 */
@RestController
@RequestMapping("/testTool")
@Slf4j
@Api(tags = "测试方法相关接口")
public class TestToolController {
    public static final String TAG = "TestController";

    @Resource
    TestToolService testService;

    @Resource
    QrCodeService qrCodeService;

    /**
     * 处理地区json
     *
     * @return
     */
    @PostMapping("/getDistrict")
    @ApiOperation("处理地区json")
    public ResponseEntity addAgentAccount(@RequestBody Map<String,Object> map) {
        try {
            /*List<ParentAgentJson> parentAgentJsonList = new ArrayList<>();
            ParentAgentJson parentAgentJson = new ParentAgentJson();
            parentAgentJson.setSysUserId(1L);
            parentAgentJson.setLevel(0);
            parentAgentJson.setAgentCode("msyn2kps");
            parentAgentJsonList.add(parentAgentJson);
            log.info(JSONObject.toJSONString(parentAgentJsonList));*/

            testService.getDistrict(map.get("json")+"");
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 生成带图片的二维码
     * @param map
     * @return
     */
    @PostMapping("/getQrCodePosterUrl")
    @ApiOperation("")
    public ResponseEntity getQrCodePosterUrl(@RequestBody Map<String,Object> map) {
        try {
            qrCodeService.getQrCodePosterUrl("https://h5.haoka.asia/?productCode=tyfzviqz&agentCode=msyn2kps");
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }


    /**
     * 生成产品海报二维码
     * @param map
     * @return
     */
    @PostMapping("/getProductPoster")
    public ResponseEntity getProductPoster(@RequestBody Map<String,Object> map) {
        try {
            qrCodeService.getProductPoster("http://yun.shengda.live/numberCard/287f6ad5-fbf5-498d-8dfd-da641089f01c.jpg","https://yun.shengda.live/numberCard/fb96c08f-0dfa-4af6-86bc-7913dbdeb917.jpeg","https://h5.haoka.asia/?productCode=tyfzviqz&agentCode=msyn2kps");
            return ResponseEntity.success();
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 生成推广海报图 格式1
     * @param map
     * @return
     */
    @PostMapping("/getRegisterQrcodeMap1Url")
    @ApiOperation("")
    public ResponseEntity getRegisterQrcodeMap1Url(@RequestBody Map<String,Object> map) {
        try {
            String str = qrCodeService.getRegisterQrcodeMap1Url("https://yun.shengda.live/numberCard/8a98eabd-de44-4c30-9e40-e15218363b97.jpg","https://test.haoka.asia/index#/register?agentCode=msyn2kps");
            return ResponseEntity.success(str);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 生成推广海报图 格式3
     * @param map
     * @return
     */
    @PostMapping("/getRegisterQrcodeMap3Url")
    @ApiOperation("")
    public ResponseEntity getRegisterQrcodeMap3Url(@RequestBody Map<String,Object> map) {
        try {
            String str = qrCodeService.getRegisterQrcodeMap3Url("https://yun.shengda.live/numberCard/bbd984c8-bd22-4b27-8945-17b99420ffd2.jpg","https://test.haoka.asia/index#/register?agentCode=msyn2kps");
            return ResponseEntity.success(str);
        } catch (Exception e) {
            log.info("{}方法异常:{}", TAG, e.getMessage());
            return ResponseEntity.error("出错了,请稍候重试:{}", null);
        }
    }

    /**
     * 处理地区json
     *
     * @return
     */
    @PostMapping("/getList")
    @ApiOperation("处理地区json")
    public void getList(@RequestBody Map<String,Object> map) {
        List<Map> list = JSONObject.parseArray(JSONObject.toJSONString(map.get("list")),Map.class);
        log.info("开始------");
        for (Map map1:list){
            log.info(JSONObject.toJSONString(map1));
        }
        log.info("结束------");
    }






}
