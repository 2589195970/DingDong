package com.ruoyi.common.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/17 17:36
 */
public class BaseConstant {

    private BaseConstant() {
    }

    /**
     * 数字常量 避免魔法值
     */
    public static final String ZERO_STRING = "0";

    public static final String ONE_STRING = "1";

    public static final String TWO_STRING = "2";

    public static final String THREE_STRING = "3";


    /**
     * 匹配失败返回值
     */
    public static final int NEGATIVE_ONE_INT = -1;

    public static final int ZERO_INT = 0;

    public static final int ONE_INT = 1;

    public static final int TWO_INT = 2;

    public static final int THREE_INT = 3;

    public static final int FOUR_INT = 4;

    public static final int FIVE_INT = 5;

    public static final int SEX_INT = 6;

    public static final int EIGHT_INT = 8;

    public static final int SEVEN_INT = 7;

    public static final int TEN_INT = 10;

    public static final int ONE_HUNDRED = 100;



    public static final int AGENT_LEVEL_INT = 5;


    /**
     * AES 加解密KEY
     */
    public static final String AES_KEY = "ADGcp7Kiixe1x3Sn";

    /**
     * 图片前缀
     */
    public static final String PICTURE_URL = "numberCard/";

    /**
     * 产品海报图底图
     */
    public static final String BASE_PICTURE_URL = "https://yun.shengda.live/numberCard/fb96c08f-0dfa-4af6-86bc-7913dbdeb917.jpeg";



    /**
     * 省编码
     */
    public static final Map<String, String> PROVINCES = new HashMap<>();

    static {
        PROVINCES.put("北京", "110000");
        PROVINCES.put("天津市", "120000");
        PROVINCES.put("河北省", "130000");
        PROVINCES.put("山西省", "140000");
        PROVINCES.put("内蒙古自治区", "150000");
        PROVINCES.put("辽宁省", "210000");
        PROVINCES.put("吉林省", "220000");
        PROVINCES.put("黑龙江省", "230000");
        PROVINCES.put("上海市", "310000");
        PROVINCES.put("江苏省", "320000");
        PROVINCES.put("浙江省", "330000");
        PROVINCES.put("安徽省", "340000");
        PROVINCES.put("福建省", "350000");
        PROVINCES.put("江西省", "360000");
        PROVINCES.put("山东省", "370000");
        PROVINCES.put("河南省", "410000");
        PROVINCES.put("湖北省", "420000");
        PROVINCES.put("湖南省", "430000");
        PROVINCES.put("广东省", "440000");
        PROVINCES.put("广西壮族自治区", "450000");
        PROVINCES.put("海南省", "460000");
        PROVINCES.put("重庆市", "500000");
        PROVINCES.put("四川省", "510000");
        PROVINCES.put("贵州省", "520000");
        PROVINCES.put("云南省", "530000");
        PROVINCES.put("西藏自治区", "540000");
        PROVINCES.put("陕西省", "610000");
        PROVINCES.put("甘肃省", "620000");
        PROVINCES.put("青海省", "630000");
        PROVINCES.put("宁夏回族自治区", "640000");
        PROVINCES.put("新疆维吾尔自治区", "650000");
    }


}
