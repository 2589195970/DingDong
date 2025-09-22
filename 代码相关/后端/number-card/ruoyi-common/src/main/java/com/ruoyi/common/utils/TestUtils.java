package com.ruoyi.common.utils;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.enums.ProductEnum;
import com.ruoyi.common.order.bo.ProductAddAndUpdateBO;
import com.ruoyi.common.order.bo.ProductCheckConfigAddBO;
import com.ruoyi.common.order.entity.AddressCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class TestUtils {

    /**
     * {
     *   "operatorType": 0,
     *   "productAgeMax": 18,
     *   "productAgeMin": 20,
     *   "productCharacteristics": "只发省内、发货快、激活强冲",
     *   "productDemand": "激活强冲100元次月底结算（结算要求：次月底之前不欠费、不销户、不单停、不三无、不关机）禁止线下、地推、任务、AB单，发现所有佣金不结算",
     *   "productDetailMap": "https://cdn.h5.phone-card.cn/pic/2013/50c5df2317414f33a7898d0b10c891a0.jpg",
     *   "productDxll": "3123",
     *   "productFafs": "123123",
     *   "productGsdq": "qweqwe",
     *   "productHyqx": "werwerwr",
     *   "productMasterMap": "qweqweqew",
     *   "productName": "测试主产品",
     *   "productPlacardMap": "string",
     *   "productRemark": "备注",
     *   "productScsm": "13123123",
     *   "productSort": 10,
     *   "productTcjs": "123123123",
     *   "productTemplateJson": "123123",
     *   "productTemplateType": 0,
     *   "productThfz": "qweqwe",
     *   "productTyll": "qweqweqwe",
     *   "productType": 0,
     *   "productYhyz": "qweqwe",
     *   "productYsyz": "qweqweqwe",
     *   "upstreamApiCode": "37v-0BdFwP",
     *   "upstreamApiName": "挖金客测试产品测试产品11132213",
     *   "upstreamProductCode": "baYVZh",
     *   "upstreamProductName": "挖金客钱asdasd啊五千万日期任务"
     * }
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            /*ProductAddAndUpdateBO productAddAndUpdateBO = new ProductAddAndUpdateBO();
            productAddAndUpdateBO.setProductName("移动-29元80G产品-测试产品");
            productAddAndUpdateBO.setOperatorType(ProductEnum.OperatorTypeEnum.MOBILE.getStatus());
            productAddAndUpdateBO.setProductCharacteristics("只发省内、发货快、激活强冲");
            productAddAndUpdateBO.setProductType(ProductEnum.DAILY_SETTLEMENT.getStatus());
            productAddAndUpdateBO.setProductRemark("备注");
            productAddAndUpdateBO.setProductTyll("通用流量50G");
            productAddAndUpdateBO.setProductDxll("定向流量50G");
            productAddAndUpdateBO.setProductThfz("0分钟");
            productAddAndUpdateBO.setProductYhyz("29元");
            productAddAndUpdateBO.setProductYsyz("39元");
            productAddAndUpdateBO.setProductScsm("充50送120");
            productAddAndUpdateBO.setProductGsdq("收货归属地");
            productAddAndUpdateBO.setProductFafs("京东顺丰");
            productAddAndUpdateBO.setProductHyqx("2年");
            productAddAndUpdateBO.setProductTcjs("29元月租80G");
            productAddAndUpdateBO.setProductDemand("不得宣传9元");
            productAddAndUpdateBO.setIsDispatchUpstreamApi(BaseConstant.ZERO_INT);
            //上游API和产品信息
            productAddAndUpdateBO.setUpstreamApiCode("xyt5mct856");
            productAddAndUpdateBO.setUpstreamApiName("挖金客-测试API");
            productAddAndUpdateBO.setUpstreamProductCode("no6r7a");
            productAddAndUpdateBO.setUpstreamProductName("挖金客-移动29元80G");
            //年龄限制
            productAddAndUpdateBO.setProductAgeMin(18);
            productAddAndUpdateBO.setProductAgeMax(60);
            //模板图片信息
            productAddAndUpdateBO.setProductTemplateType(0);
            productAddAndUpdateBO.setProductMasterMap("https://cdn.h5.phone-card.cn/pic/2013/50c5df2317414f33a7898d0b10c891a0.jpg");
            productAddAndUpdateBO.setProductDetailMap("https://cdn.h5.phone-card.cn/pic/2013/50c5df2317414f33a7898d0b10c891a0.jpg");
            productAddAndUpdateBO.setProductPlacardMap("https://cdn.h5.phone-card.cn/pic/2013/50c5df2317414f33a7898d0b10c891a0.jpg");
            productAddAndUpdateBO.setProductTemplateJson(JSONObject.toJSONString(new Object()));
            List<String> agentCodeList = new ArrayList<>();
            agentCodeList.add("frfusaur");
            agentCodeList.add("uzhwwiam");
            productAddAndUpdateBO.setAgentCodeList(agentCodeList);
            log.info(JSONObject.toJSONString(productAddAndUpdateBO));*/

            ProductCheckConfigAddBO productCheckConfigAddBO = new ProductCheckConfigAddBO();
            productCheckConfigAddBO.setProductCode("gkqkwihw");
            productCheckConfigAddBO.setCheckConfigType(1);
            productCheckConfigAddBO.setTerritoryCheckType(1);
            productCheckConfigAddBO.setTactics(0);
            List<AddressCode> territoryProvinceList = new ArrayList<>();
            AddressCode addressCode = new AddressCode();
            addressCode.setProvinceCode("110000");
            addressCode.setProvinceName("北京市");
            territoryProvinceList.add(addressCode);
            productCheckConfigAddBO.setTerritoryProvinceList(territoryProvinceList);
            List<AddressCode> territoryCityList = new ArrayList<>();
            AddressCode addressCode1 = new AddressCode();
            addressCode1.setProvinceCode("110000");
            addressCode1.setProvinceName("北京市");
            addressCode1.setCityCode("110101");
            addressCode1.setCityName("东城区");
            territoryCityList.add(addressCode1);
            productCheckConfigAddBO.setTerritoryCityList(territoryCityList);
            productCheckConfigAddBO.setAgeMax(100);
            productCheckConfigAddBO.setAgeMin(0);
            log.info(JSONObject.toJSONString(productCheckConfigAddBO));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
