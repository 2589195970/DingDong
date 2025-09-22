package com.ruoyi.common.enums;


import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.AddressCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址编码相关枚举
 * 包含省市区相关编码 来源于国家标准地址编码
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/31 18:47
 */
public enum AddressCodeEnum {



    ADDRESS_120101("120000","天津","120100","天津市","120101","和平区"),

    ;

    AddressCodeEnum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.countyCode = countyCode;
        this.countyName = countyName;
    }

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 县/区编码
     */
    private String countyCode;

    /**
     * 县/区名称
     */
    private String countyName;


    public String getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getCountyName() {
        return countyName;
    }


    /**
     * 根据省编码获取省市区信息
     * @return
     */
    public static List<AddressCode> getAddressCodeByProvinceCode(String provinceCode) throws BizException{
        if("110000".equals(provinceCode)) return AddressCode110000Enum.getAddressCode();
        if("120000".equals(provinceCode)) return AddressCode120000Enum.getAddressCode();
        if("130000".equals(provinceCode)) return AddressCode130000Enum.getAddressCode();
        if("140000".equals(provinceCode)) return AddressCode140000Enum.getAddressCode();
        if("150000".equals(provinceCode)) return AddressCode150000Enum.getAddressCode();
        if("210000".equals(provinceCode)) return AddressCode210000Enum.getAddressCode();
        if("220000".equals(provinceCode)) return AddressCode220000Enum.getAddressCode();
        if("230000".equals(provinceCode)) return AddressCode230000Enum.getAddressCode();
        if("310000".equals(provinceCode)) return AddressCode310000Enum.getAddressCode();
        if("320000".equals(provinceCode)) return AddressCode320000Enum.getAddressCode();
        if("330000".equals(provinceCode)) return AddressCode330000Enum.getAddressCode();
        if("340000".equals(provinceCode)) return AddressCode340000Enum.getAddressCode();
        if("350000".equals(provinceCode)) return AddressCode350000Enum.getAddressCode();
        if("360000".equals(provinceCode)) return AddressCode360000Enum.getAddressCode();
        if("370000".equals(provinceCode)) return AddressCode370000Enum.getAddressCode();
        if("410000".equals(provinceCode)) return AddressCode410000Enum.getAddressCode();
        if("420000".equals(provinceCode)) return AddressCode420000Enum.getAddressCode();
        if("430000".equals(provinceCode)) return AddressCode430000Enum.getAddressCode();
        if("440000".equals(provinceCode)) return AddressCode440000Enum.getAddressCode();
        if("450000".equals(provinceCode)) return AddressCode450000Enum.getAddressCode();
        if("460000".equals(provinceCode)) return AddressCode460000Enum.getAddressCode();
        if("500000".equals(provinceCode)) return AddressCode500000Enum.getAddressCode();
        if("510000".equals(provinceCode)) return AddressCode510000Enum.getAddressCode();
        if("520000".equals(provinceCode)) return AddressCode520000Enum.getAddressCode();
        if("530000".equals(provinceCode)) return AddressCode530000Enum.getAddressCode();
        if("540000".equals(provinceCode)) return AddressCode540000Enum.getAddressCode();
        if("610000".equals(provinceCode)) return AddressCode610000Enum.getAddressCode();
        if("620000".equals(provinceCode)) return AddressCode620000Enum.getAddressCode();
        if("630000".equals(provinceCode)) return AddressCode630000Enum.getAddressCode();
        if("640000".equals(provinceCode)) return AddressCode640000Enum.getAddressCode();
        if("650000".equals(provinceCode)) return AddressCode650000Enum.getAddressCode();

        throw new BizException("省编码不存在");
    }



    /**
     * 省编码 北京枚举
     */
    public enum AddressCode110000Enum {

        ADDRESS_110101("110000","北京","110100","北京市","110101","东城区"),
        ADDRESS_110102("110000","北京","110100","北京市","110102","西城区"),
        ADDRESS_110105("110000","北京","110100","北京市","110105","朝阳区"),
        ADDRESS_110106("110000","北京","110100","北京市","110106","丰台区"),
        ADDRESS_110107("110000","北京","110100","北京市","110107","石景山区"),
        ADDRESS_110108("110000","北京","110100","北京市","110108","海淀区"),
        ADDRESS_110109("110000","北京","110100","北京市","110109","门头沟区"),
        ADDRESS_110111("110000","北京","110100","北京市","110111","房山区"),
        ADDRESS_110112("110000","北京","110100","北京市","110112","通州区"),
        ADDRESS_110113("110000","北京","110100","北京市","110113","顺义区"),
        ADDRESS_110114("110000","北京","110100","北京市","110114","昌平区"),
        ADDRESS_110115("110000","北京","110100","北京市","110115","大兴区"),
        ADDRESS_110116("110000","北京","110100","北京市","110116","怀柔区"),
        ADDRESS_110117("110000","北京","110100","北京市","110117","平谷区"),
        ADDRESS_110118("110000","北京","110100","北京市","110118","密云区"),
        ADDRESS_110119("110000","北京","110100","北京市","110119","延庆区"),
        ;



        AddressCode110000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode110000Enum addressCodeEnum :AddressCode110000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }
    }


    /**
     * 省编码 天津枚举
     */
    public enum AddressCode120000Enum {

        ADDRESS_120101("120000","天津","120100","天津市","120101","和平区"),
        ADDRESS_120102("120000","天津","120100","天津市","120102","河东区"),
        ADDRESS_120103("120000","天津","120100","天津市","120103","河西区"),
        ADDRESS_120104("120000","天津","120100","天津市","120104","南开区"),
        ADDRESS_120105("120000","天津","120100","天津市","120105","河北区"),
        ADDRESS_120106("120000","天津","120100","天津市","120106","红桥区"),
        ADDRESS_120110("120000","天津","120100","天津市","120110","东丽区"),
        ADDRESS_120111("120000","天津","120100","天津市","120111","西青区"),
        ADDRESS_120112("120000","天津","120100","天津市","120112","津南区"),
        ADDRESS_120113("120000","天津","120100","天津市","120113","北辰区"),
        ADDRESS_120114("120000","天津","120100","天津市","120114","武清区"),
        ADDRESS_120115("120000","天津","120100","天津市","120115","宝坻区"),
        ADDRESS_120116("120000","天津","120100","天津市","120116","滨海新区"),
        ADDRESS_120117("120000","天津","120100","天津市","120117","宁河区"),
        ADDRESS_120118("120000","天津","120100","天津市","120118","静海区"),
        ADDRESS_120119("120000","天津","120100","天津市","120119","蓟州区"),
        ;



        AddressCode120000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode120000Enum addressCodeEnum :AddressCode120000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 河北枚举
     */
    public enum AddressCode130000Enum {

        ADDRESS_130102("130000","河北省","130100","石家庄市","130102","长安区"),
        ADDRESS_130104("130000","河北省","130100","石家庄市","130104","桥西区"),
        ADDRESS_130105("130000","河北省","130100","石家庄市","130105","新华区"),
        ADDRESS_130107("130000","河北省","130100","石家庄市","130107","井陉矿区"),
        ADDRESS_130108("130000","河北省","130100","石家庄市","130108","裕华区"),
        ADDRESS_130109("130000","河北省","130100","石家庄市","130109","藁城区"),
        ADDRESS_130110("130000","河北省","130100","石家庄市","130110","鹿泉区"),
        ADDRESS_130111("130000","河北省","130100","石家庄市","130111","栾城区"),
        ADDRESS_130121("130000","河北省","130100","石家庄市","130121","井陉县"),
        ADDRESS_130123("130000","河北省","130100","石家庄市","130123","正定县"),
        ADDRESS_130125("130000","河北省","130100","石家庄市","130125","行唐县"),
        ADDRESS_130126("130000","河北省","130100","石家庄市","130126","灵寿县"),
        ADDRESS_130127("130000","河北省","130100","石家庄市","130127","高邑县"),
        ADDRESS_130128("130000","河北省","130100","石家庄市","130128","深泽县"),
        ADDRESS_130129("130000","河北省","130100","石家庄市","130129","赞皇县"),
        ADDRESS_130130("130000","河北省","130100","石家庄市","130130","无极县"),
        ADDRESS_130131("130000","河北省","130100","石家庄市","130131","平山县"),
        ADDRESS_130132("130000","河北省","130100","石家庄市","130132","元氏县"),
        ADDRESS_130133("130000","河北省","130100","石家庄市","130133","赵县"),
        ADDRESS_130181("130000","河北省","130100","石家庄市","130181","辛集市"),
        ADDRESS_130183("130000","河北省","130100","石家庄市","130183","晋州市"),
        ADDRESS_130184("130000","河北省","130100","石家庄市","130184","新乐市"),
        ADDRESS_130202("130000","河北省","130200","唐山市","130202","路南区"),
        ADDRESS_130203("130000","河北省","130200","唐山市","130203","路北区"),
        ADDRESS_130204("130000","河北省","130200","唐山市","130204","古冶区"),
        ADDRESS_130205("130000","河北省","130200","唐山市","130205","开平区"),
        ADDRESS_130207("130000","河北省","130200","唐山市","130207","丰南区"),
        ADDRESS_130208("130000","河北省","130200","唐山市","130208","丰润区"),
        ADDRESS_130209("130000","河北省","130200","唐山市","130209","曹妃甸区"),
        ADDRESS_130224("130000","河北省","130200","唐山市","130224","滦南县"),
        ADDRESS_130225("130000","河北省","130200","唐山市","130225","乐亭县"),
        ADDRESS_130227("130000","河北省","130200","唐山市","130227","迁西县"),
        ADDRESS_130229("130000","河北省","130200","唐山市","130229","玉田县"),
        ADDRESS_130281("130000","河北省","130200","唐山市","130281","遵化市"),
        ADDRESS_130283("130000","河北省","130200","唐山市","130283","迁安市"),
        ADDRESS_130284("130000","河北省","130200","唐山市","130284","滦州市"),
        ADDRESS_130302("130000","河北省","130300","秦皇岛市","130302","海港区"),
        ADDRESS_130303("130000","河北省","130300","秦皇岛市","130303","山海关区"),
        ADDRESS_130304("130000","河北省","130300","秦皇岛市","130304","北戴河区"),
        ADDRESS_130306("130000","河北省","130300","秦皇岛市","130306","抚宁区"),
        ADDRESS_130321("130000","河北省","130300","秦皇岛市","130321","青龙满族自治县"),
        ADDRESS_130322("130000","河北省","130300","秦皇岛市","130322","昌黎县"),
        ADDRESS_130324("130000","河北省","130300","秦皇岛市","130324","卢龙县"),
        ADDRESS_130402("130000","河北省","130400","邯郸市","130402","邯山区"),
        ADDRESS_130403("130000","河北省","130400","邯郸市","130403","丛台区"),
        ADDRESS_130404("130000","河北省","130400","邯郸市","130404","复兴区"),
        ADDRESS_130406("130000","河北省","130400","邯郸市","130406","峰峰矿区"),
        ADDRESS_130407("130000","河北省","130400","邯郸市","130407","肥乡区"),
        ADDRESS_130408("130000","河北省","130400","邯郸市","130408","永年区"),
        ADDRESS_130423("130000","河北省","130400","邯郸市","130423","临漳县"),
        ADDRESS_130424("130000","河北省","130400","邯郸市","130424","成安县"),
        ADDRESS_130425("130000","河北省","130400","邯郸市","130425","大名县"),
        ADDRESS_130426("130000","河北省","130400","邯郸市","130426","涉县"),
        ADDRESS_130427("130000","河北省","130400","邯郸市","130427","磁县"),
        ADDRESS_130430("130000","河北省","130400","邯郸市","130430","邱县"),
        ADDRESS_130431("130000","河北省","130400","邯郸市","130431","鸡泽县"),
        ADDRESS_130432("130000","河北省","130400","邯郸市","130432","广平县"),
        ADDRESS_130433("130000","河北省","130400","邯郸市","130433","馆陶县"),
        ADDRESS_130434("130000","河北省","130400","邯郸市","130434","魏县"),
        ADDRESS_130435("130000","河北省","130400","邯郸市","130435","曲周县"),
        ADDRESS_130481("130000","河北省","130400","邯郸市","130481","武安市"),
        ADDRESS_130502("130000","河北省","130500","邢台市","130502","襄都区"),
        ADDRESS_130503("130000","河北省","130500","邢台市","130503","信都区"),
        ADDRESS_130505("130000","河北省","130500","邢台市","130505","任泽区"),
        ADDRESS_130506("130000","河北省","130500","邢台市","130506","南和区"),
        ADDRESS_130522("130000","河北省","130500","邢台市","130522","临城县"),
        ADDRESS_130523("130000","河北省","130500","邢台市","130523","内丘县"),
        ADDRESS_130524("130000","河北省","130500","邢台市","130524","柏乡县"),
        ADDRESS_130525("130000","河北省","130500","邢台市","130525","隆尧县"),
        ADDRESS_130528("130000","河北省","130500","邢台市","130528","宁晋县"),
        ADDRESS_130529("130000","河北省","130500","邢台市","130529","巨鹿县"),
        ADDRESS_130530("130000","河北省","130500","邢台市","130530","新河县"),
        ADDRESS_130531("130000","河北省","130500","邢台市","130531","广宗县"),
        ADDRESS_130532("130000","河北省","130500","邢台市","130532","平乡县"),
        ADDRESS_130533("130000","河北省","130500","邢台市","130533","威县"),
        ADDRESS_130534("130000","河北省","130500","邢台市","130534","清河县"),
        ADDRESS_130535("130000","河北省","130500","邢台市","130535","临西县"),
        ADDRESS_130581("130000","河北省","130500","邢台市","130581","南宫市"),
        ADDRESS_130582("130000","河北省","130500","邢台市","130582","沙河市"),
        ADDRESS_130602("130000","河北省","130600","保定市","130602","竞秀区"),
        ADDRESS_130606("130000","河北省","130600","保定市","130606","莲池区"),
        ADDRESS_130607("130000","河北省","130600","保定市","130607","满城区"),
        ADDRESS_130608("130000","河北省","130600","保定市","130608","清苑区"),
        ADDRESS_130609("130000","河北省","130600","保定市","130609","徐水区"),
        ADDRESS_130623("130000","河北省","130600","保定市","130623","涞水县"),
        ADDRESS_130624("130000","河北省","130600","保定市","130624","阜平县"),
        ADDRESS_130626("130000","河北省","130600","保定市","130626","定兴县"),
        ADDRESS_130627("130000","河北省","130600","保定市","130627","唐县"),
        ADDRESS_130628("130000","河北省","130600","保定市","130628","高阳县"),
        ADDRESS_130629("130000","河北省","130600","保定市","130629","容城县"),
        ADDRESS_130630("130000","河北省","130600","保定市","130630","涞源县"),
        ADDRESS_130631("130000","河北省","130600","保定市","130631","望都县"),
        ADDRESS_130632("130000","河北省","130600","保定市","130632","安新县"),
        ADDRESS_130633("130000","河北省","130600","保定市","130633","易县"),
        ADDRESS_130634("130000","河北省","130600","保定市","130634","曲阳县"),
        ADDRESS_130635("130000","河北省","130600","保定市","130635","蠡县"),
        ADDRESS_130636("130000","河北省","130600","保定市","130636","顺平县"),
        ADDRESS_130637("130000","河北省","130600","保定市","130637","博野县"),
        ADDRESS_130638("130000","河北省","130600","保定市","130638","雄县"),
        ADDRESS_130681("130000","河北省","130600","保定市","130681","涿州市"),
        ADDRESS_130682("130000","河北省","130600","保定市","130682","定州市"),
        ADDRESS_130683("130000","河北省","130600","保定市","130683","安国市"),
        ADDRESS_130684("130000","河北省","130600","保定市","130684","高碑店市"),
        ADDRESS_130702("130000","河北省","130700","张家口市","130702","桥东区"),
        ADDRESS_130703("130000","河北省","130700","张家口市","130703","桥西区"),
        ADDRESS_130705("130000","河北省","130700","张家口市","130705","宣化区"),
        ADDRESS_130706("130000","河北省","130700","张家口市","130706","下花园区"),
        ADDRESS_130708("130000","河北省","130700","张家口市","130708","万全区"),
        ADDRESS_130709("130000","河北省","130700","张家口市","130709","崇礼区"),
        ADDRESS_130722("130000","河北省","130700","张家口市","130722","张北县"),
        ADDRESS_130723("130000","河北省","130700","张家口市","130723","康保县"),
        ADDRESS_130724("130000","河北省","130700","张家口市","130724","沽源县"),
        ADDRESS_130725("130000","河北省","130700","张家口市","130725","尚义县"),
        ADDRESS_130726("130000","河北省","130700","张家口市","130726","蔚县"),
        ADDRESS_130727("130000","河北省","130700","张家口市","130727","阳原县"),
        ADDRESS_130728("130000","河北省","130700","张家口市","130728","怀安县"),
        ADDRESS_130730("130000","河北省","130700","张家口市","130730","怀来县"),
        ADDRESS_130731("130000","河北省","130700","张家口市","130731","涿鹿县"),
        ADDRESS_130732("130000","河北省","130700","张家口市","130732","赤城县"),
        ADDRESS_130802("130000","河北省","130800","承德市","130802","双桥区"),
        ADDRESS_130803("130000","河北省","130800","承德市","130803","双滦区"),
        ADDRESS_130804("130000","河北省","130800","承德市","130804","鹰手营子矿区"),
        ADDRESS_130821("130000","河北省","130800","承德市","130821","承德县"),
        ADDRESS_130822("130000","河北省","130800","承德市","130822","兴隆县"),
        ADDRESS_130824("130000","河北省","130800","承德市","130824","滦平县"),
        ADDRESS_130825("130000","河北省","130800","承德市","130825","隆化县"),
        ADDRESS_130826("130000","河北省","130800","承德市","130826","丰宁满族自治县"),
        ADDRESS_130827("130000","河北省","130800","承德市","130827","宽城满族自治县"),
        ADDRESS_130828("130000","河北省","130800","承德市","130828","围场满族蒙古族自治县"),
        ADDRESS_130881("130000","河北省","130800","承德市","130881","平泉市"),
        ADDRESS_130902("130000","河北省","130900","沧州市","130902","新华区"),
        ADDRESS_130903("130000","河北省","130900","沧州市","130903","运河区"),
        ADDRESS_130921("130000","河北省","130900","沧州市","130921","沧县"),
        ADDRESS_130922("130000","河北省","130900","沧州市","130922","青县"),
        ADDRESS_130923("130000","河北省","130900","沧州市","130923","东光县"),
        ADDRESS_130924("130000","河北省","130900","沧州市","130924","海兴县"),
        ADDRESS_130925("130000","河北省","130900","沧州市","130925","盐山县"),
        ADDRESS_130926("130000","河北省","130900","沧州市","130926","肃宁县"),
        ADDRESS_130927("130000","河北省","130900","沧州市","130927","南皮县"),
        ADDRESS_130928("130000","河北省","130900","沧州市","130928","吴桥县"),
        ADDRESS_130929("130000","河北省","130900","沧州市","130929","献县"),
        ADDRESS_130930("130000","河北省","130900","沧州市","130930","孟村回族自治县"),
        ADDRESS_130981("130000","河北省","130900","沧州市","130981","泊头市"),
        ADDRESS_130982("130000","河北省","130900","沧州市","130982","任丘市"),
        ADDRESS_130983("130000","河北省","130900","沧州市","130983","黄骅市"),
        ADDRESS_130984("130000","河北省","130900","沧州市","130984","河间市"),
        ADDRESS_131002("130000","河北省","131000","廊坊市","131002","安次区"),
        ADDRESS_131003("130000","河北省","131000","廊坊市","131003","广阳区"),
        ADDRESS_131022("130000","河北省","131000","廊坊市","131022","固安县"),
        ADDRESS_131023("130000","河北省","131000","廊坊市","131023","永清县"),
        ADDRESS_131024("130000","河北省","131000","廊坊市","131024","香河县"),
        ADDRESS_131025("130000","河北省","131000","廊坊市","131025","大城县"),
        ADDRESS_131026("130000","河北省","131000","廊坊市","131026","文安县"),
        ADDRESS_131028("130000","河北省","131000","廊坊市","131028","大厂回族自治县"),
        ADDRESS_131081("130000","河北省","131000","廊坊市","131081","霸州市"),
        ADDRESS_131082("130000","河北省","131000","廊坊市","131082","三河市"),
        ADDRESS_131102("130000","河北省","131100","衡水市","131102","桃城区"),
        ADDRESS_131103("130000","河北省","131100","衡水市","131103","冀州区"),
        ADDRESS_131121("130000","河北省","131100","衡水市","131121","枣强县"),
        ADDRESS_131122("130000","河北省","131100","衡水市","131122","武邑县"),
        ADDRESS_131123("130000","河北省","131100","衡水市","131123","武强县"),
        ADDRESS_131124("130000","河北省","131100","衡水市","131124","饶阳县"),
        ADDRESS_131125("130000","河北省","131100","衡水市","131125","安平县"),
        ADDRESS_131126("130000","河北省","131100","衡水市","131126","故城县"),
        ADDRESS_131127("130000","河北省","131100","衡水市","131127","景县"),
        ADDRESS_131128("130000","河北省","131100","衡水市","131128","阜城县"),
        ADDRESS_131182("130000","河北省","131100","衡水市","131182","深州市"),
        ;



        AddressCode130000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode130000Enum addressCodeEnum :AddressCode130000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }
    }


    /**
     * 省编码 河北枚举
     */
    public enum AddressCode140000Enum {

        ADDRESS_140105("140000","山西省","140100","太原市","140105","小店区"),
        ADDRESS_140106("140000","山西省","140100","太原市","140106","迎泽区"),
        ADDRESS_140107("140000","山西省","140100","太原市","140107","杏花岭区"),
        ADDRESS_140108("140000","山西省","140100","太原市","140108","尖草坪区"),
        ADDRESS_140109("140000","山西省","140100","太原市","140109","万柏林区"),
        ADDRESS_140110("140000","山西省","140100","太原市","140110","晋源区"),
        ADDRESS_140121("140000","山西省","140100","太原市","140121","清徐县"),
        ADDRESS_140122("140000","山西省","140100","太原市","140122","阳曲县"),
        ADDRESS_140123("140000","山西省","140100","太原市","140123","娄烦县"),
        ADDRESS_140181("140000","山西省","140100","太原市","140181","古交市"),
        ADDRESS_140212("140000","山西省","140200","大同市","140212","新荣区"),
        ADDRESS_140213("140000","山西省","140200","大同市","140213","平城区"),
        ADDRESS_140214("140000","山西省","140200","大同市","140214","云冈区"),
        ADDRESS_140215("140000","山西省","140200","大同市","140215","云州区"),
        ADDRESS_140221("140000","山西省","140200","大同市","140221","阳高县"),
        ADDRESS_140222("140000","山西省","140200","大同市","140222","天镇县"),
        ADDRESS_140223("140000","山西省","140200","大同市","140223","广灵县"),
        ADDRESS_140224("140000","山西省","140200","大同市","140224","灵丘县"),
        ADDRESS_140225("140000","山西省","140200","大同市","140225","浑源县"),
        ADDRESS_140226("140000","山西省","140200","大同市","140226","左云县"),
        ADDRESS_140302("140000","山西省","140300","阳泉市","140302","城区"),
        ADDRESS_140303("140000","山西省","140300","阳泉市","140303","矿区"),
        ADDRESS_140311("140000","山西省","140300","阳泉市","140311","郊区"),
        ADDRESS_140321("140000","山西省","140300","阳泉市","140321","平定县"),
        ADDRESS_140322("140000","山西省","140300","阳泉市","140322","盂县"),
        ADDRESS_140403("140000","山西省","140400","长治市","140403","潞州区"),
        ADDRESS_140404("140000","山西省","140400","长治市","140404","上党区"),
        ADDRESS_140405("140000","山西省","140400","长治市","140405","屯留区"),
        ADDRESS_140406("140000","山西省","140400","长治市","140406","潞城区"),
        ADDRESS_140423("140000","山西省","140400","长治市","140423","襄垣县"),
        ADDRESS_140425("140000","山西省","140400","长治市","140425","平顺县"),
        ADDRESS_140426("140000","山西省","140400","长治市","140426","黎城县"),
        ADDRESS_140427("140000","山西省","140400","长治市","140427","壶关县"),
        ADDRESS_140428("140000","山西省","140400","长治市","140428","长子县"),
        ADDRESS_140429("140000","山西省","140400","长治市","140429","武乡县"),
        ADDRESS_140430("140000","山西省","140400","长治市","140430","沁县"),
        ADDRESS_140431("140000","山西省","140400","长治市","140431","沁源县"),
        ADDRESS_140502("140000","山西省","140500","晋城市","140502","城区"),
        ADDRESS_140521("140000","山西省","140500","晋城市","140521","沁水县"),
        ADDRESS_140522("140000","山西省","140500","晋城市","140522","阳城县"),
        ADDRESS_140524("140000","山西省","140500","晋城市","140524","陵川县"),
        ADDRESS_140525("140000","山西省","140500","晋城市","140525","泽州县"),
        ADDRESS_140581("140000","山西省","140500","晋城市","140581","高平市"),
        ADDRESS_140602("140000","山西省","140600","朔州市","140602","朔城区"),
        ADDRESS_140603("140000","山西省","140600","朔州市","140603","平鲁区"),
        ADDRESS_140621("140000","山西省","140600","朔州市","140621","山阴县"),
        ADDRESS_140622("140000","山西省","140600","朔州市","140622","应县"),
        ADDRESS_140623("140000","山西省","140600","朔州市","140623","右玉县"),
        ADDRESS_140681("140000","山西省","140600","朔州市","140681","怀仁市"),
        ADDRESS_140702("140000","山西省","140700","晋中市","140702","榆次区"),
        ADDRESS_140703("140000","山西省","140700","晋中市","140703","太谷区"),
        ADDRESS_140721("140000","山西省","140700","晋中市","140721","榆社县"),
        ADDRESS_140722("140000","山西省","140700","晋中市","140722","左权县"),
        ADDRESS_140723("140000","山西省","140700","晋中市","140723","和顺县"),
        ADDRESS_140724("140000","山西省","140700","晋中市","140724","昔阳县"),
        ADDRESS_140725("140000","山西省","140700","晋中市","140725","寿阳县"),
        ADDRESS_140727("140000","山西省","140700","晋中市","140727","祁县"),
        ADDRESS_140728("140000","山西省","140700","晋中市","140728","平遥县"),
        ADDRESS_140729("140000","山西省","140700","晋中市","140729","灵石县"),
        ADDRESS_140781("140000","山西省","140700","晋中市","140781","介休市"),
        ADDRESS_140802("140000","山西省","140800","运城市","140802","盐湖区"),
        ADDRESS_140821("140000","山西省","140800","运城市","140821","临猗县"),
        ADDRESS_140822("140000","山西省","140800","运城市","140822","万荣县"),
        ADDRESS_140823("140000","山西省","140800","运城市","140823","闻喜县"),
        ADDRESS_140824("140000","山西省","140800","运城市","140824","稷山县"),
        ADDRESS_140825("140000","山西省","140800","运城市","140825","新绛县"),
        ADDRESS_140826("140000","山西省","140800","运城市","140826","绛县"),
        ADDRESS_140827("140000","山西省","140800","运城市","140827","垣曲县"),
        ADDRESS_140828("140000","山西省","140800","运城市","140828","夏县"),
        ADDRESS_140829("140000","山西省","140800","运城市","140829","平陆县"),
        ADDRESS_140830("140000","山西省","140800","运城市","140830","芮城县"),
        ADDRESS_140881("140000","山西省","140800","运城市","140881","永济市"),
        ADDRESS_140882("140000","山西省","140800","运城市","140882","河津市"),
        ADDRESS_140902("140000","山西省","140900","忻州市","140902","忻府区"),
        ADDRESS_140921("140000","山西省","140900","忻州市","140921","定襄县"),
        ADDRESS_140922("140000","山西省","140900","忻州市","140922","五台县"),
        ADDRESS_140923("140000","山西省","140900","忻州市","140923","代县"),
        ADDRESS_140924("140000","山西省","140900","忻州市","140924","繁峙县"),
        ADDRESS_140925("140000","山西省","140900","忻州市","140925","宁武县"),
        ADDRESS_140926("140000","山西省","140900","忻州市","140926","静乐县"),
        ADDRESS_140927("140000","山西省","140900","忻州市","140927","神池县"),
        ADDRESS_140928("140000","山西省","140900","忻州市","140928","五寨县"),
        ADDRESS_140929("140000","山西省","140900","忻州市","140929","岢岚县"),
        ADDRESS_140930("140000","山西省","140900","忻州市","140930","河曲县"),
        ADDRESS_140931("140000","山西省","140900","忻州市","140931","保德县"),
        ADDRESS_140932("140000","山西省","140900","忻州市","140932","偏关县"),
        ADDRESS_140981("140000","山西省","140900","忻州市","140981","原平市"),
        ADDRESS_141002("140000","山西省","141000","临汾市","141002","尧都区"),
        ADDRESS_141021("140000","山西省","141000","临汾市","141021","曲沃县"),
        ADDRESS_141022("140000","山西省","141000","临汾市","141022","翼城县"),
        ADDRESS_141023("140000","山西省","141000","临汾市","141023","襄汾县"),
        ADDRESS_141024("140000","山西省","141000","临汾市","141024","洪洞县"),
        ADDRESS_141025("140000","山西省","141000","临汾市","141025","古县"),
        ADDRESS_141026("140000","山西省","141000","临汾市","141026","安泽县"),
        ADDRESS_141027("140000","山西省","141000","临汾市","141027","浮山县"),
        ADDRESS_141028("140000","山西省","141000","临汾市","141028","吉县"),
        ADDRESS_141029("140000","山西省","141000","临汾市","141029","乡宁县"),
        ADDRESS_141030("140000","山西省","141000","临汾市","141030","大宁县"),
        ADDRESS_141031("140000","山西省","141000","临汾市","141031","隰县"),
        ADDRESS_141032("140000","山西省","141000","临汾市","141032","永和县"),
        ADDRESS_141033("140000","山西省","141000","临汾市","141033","蒲县"),
        ADDRESS_141034("140000","山西省","141000","临汾市","141034","汾西县"),
        ADDRESS_141081("140000","山西省","141000","临汾市","141081","侯马市"),
        ADDRESS_141082("140000","山西省","141000","临汾市","141082","霍州市"),
        ADDRESS_141102("140000","山西省","141100","吕梁市","141102","离石区"),
        ADDRESS_141121("140000","山西省","141100","吕梁市","141121","文水县"),
        ADDRESS_141122("140000","山西省","141100","吕梁市","141122","交城县"),
        ADDRESS_141123("140000","山西省","141100","吕梁市","141123","兴县"),
        ADDRESS_141124("140000","山西省","141100","吕梁市","141124","临县"),
        ADDRESS_141125("140000","山西省","141100","吕梁市","141125","柳林县"),
        ADDRESS_141126("140000","山西省","141100","吕梁市","141126","石楼县"),
        ADDRESS_141127("140000","山西省","141100","吕梁市","141127","岚县"),
        ADDRESS_141128("140000","山西省","141100","吕梁市","141128","方山县"),
        ADDRESS_141129("140000","山西省","141100","吕梁市","141129","中阳县"),
        ADDRESS_141130("140000","山西省","141100","吕梁市","141130","交口县"),
        ADDRESS_141181("140000","山西省","141100","吕梁市","141181","孝义市"),
        ADDRESS_141182("140000","山西省","141100","吕梁市","141182","汾阳市"),

        ;



        AddressCode140000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode140000Enum addressCodeEnum :AddressCode140000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }
    }

    /**
     * 省编码 内蒙古自治区枚举
     */
    public enum AddressCode150000Enum {

        ADDRESS_150102("150000","内蒙古自治区","150100","呼和浩特市","150102","新城区"),
        ADDRESS_150103("150000","内蒙古自治区","150100","呼和浩特市","150103","回民区"),
        ADDRESS_150104("150000","内蒙古自治区","150100","呼和浩特市","150104","玉泉区"),
        ADDRESS_150105("150000","内蒙古自治区","150100","呼和浩特市","150105","赛罕区"),
        ADDRESS_150121("150000","内蒙古自治区","150100","呼和浩特市","150121","土默特左旗"),
        ADDRESS_150122("150000","内蒙古自治区","150100","呼和浩特市","150122","托克托县"),
        ADDRESS_150123("150000","内蒙古自治区","150100","呼和浩特市","150123","和林格尔县"),
        ADDRESS_150124("150000","内蒙古自治区","150100","呼和浩特市","150124","清水河县"),
        ADDRESS_150125("150000","内蒙古自治区","150100","呼和浩特市","150125","武川县"),
        ADDRESS_150202("150000","内蒙古自治区","150200","包头市","150202","东河区"),
        ADDRESS_150203("150000","内蒙古自治区","150200","包头市","150203","昆都仑区"),
        ADDRESS_150204("150000","内蒙古自治区","150200","包头市","150204","青山区"),
        ADDRESS_150205("150000","内蒙古自治区","150200","包头市","150205","石拐区"),
        ADDRESS_150206("150000","内蒙古自治区","150200","包头市","150206","白云鄂博矿区"),
        ADDRESS_150207("150000","内蒙古自治区","150200","包头市","150207","九原区"),
        ADDRESS_150221("150000","内蒙古自治区","150200","包头市","150221","土默特右旗"),
        ADDRESS_150222("150000","内蒙古自治区","150200","包头市","150222","固阳县"),
        ADDRESS_150223("150000","内蒙古自治区","150200","包头市","150223","达尔罕茂明安联合旗"),
        ADDRESS_150302("150000","内蒙古自治区","150300","乌海市","150302","海勃湾区"),
        ADDRESS_150303("150000","内蒙古自治区","150300","乌海市","150303","海南区"),
        ADDRESS_150304("150000","内蒙古自治区","150300","乌海市","150304","乌达区"),
        ADDRESS_150402("150000","内蒙古自治区","150400","赤峰市","150402","红山区"),
        ADDRESS_150403("150000","内蒙古自治区","150400","赤峰市","150403","元宝山区"),
        ADDRESS_150404("150000","内蒙古自治区","150400","赤峰市","150404","松山区"),
        ADDRESS_150421("150000","内蒙古自治区","150400","赤峰市","150421","阿鲁科尔沁旗"),
        ADDRESS_150422("150000","内蒙古自治区","150400","赤峰市","150422","巴林左旗"),
        ADDRESS_150423("150000","内蒙古自治区","150400","赤峰市","150423","巴林右旗"),
        ADDRESS_150424("150000","内蒙古自治区","150400","赤峰市","150424","林西县"),
        ADDRESS_150425("150000","内蒙古自治区","150400","赤峰市","150425","克什克腾旗"),
        ADDRESS_150426("150000","内蒙古自治区","150400","赤峰市","150426","翁牛特旗"),
        ADDRESS_150428("150000","内蒙古自治区","150400","赤峰市","150428","喀喇沁旗"),
        ADDRESS_150429("150000","内蒙古自治区","150400","赤峰市","150429","宁城县"),
        ADDRESS_150430("150000","内蒙古自治区","150400","赤峰市","150430","敖汉旗"),
        ADDRESS_150502("150000","内蒙古自治区","150500","通辽市","150502","科尔沁区"),
        ADDRESS_150521("150000","内蒙古自治区","150500","通辽市","150521","科尔沁左翼中旗"),
        ADDRESS_150522("150000","内蒙古自治区","150500","通辽市","150522","科尔沁左翼后旗"),
        ADDRESS_150523("150000","内蒙古自治区","150500","通辽市","150523","开鲁县"),
        ADDRESS_150524("150000","内蒙古自治区","150500","通辽市","150524","库伦旗"),
        ADDRESS_150525("150000","内蒙古自治区","150500","通辽市","150525","奈曼旗"),
        ADDRESS_150526("150000","内蒙古自治区","150500","通辽市","150526","扎鲁特旗"),
        ADDRESS_150581("150000","内蒙古自治区","150500","通辽市","150581","霍林郭勒市"),
        ADDRESS_150602("150000","内蒙古自治区","150600","鄂尔多斯市","150602","东胜区"),
        ADDRESS_150603("150000","内蒙古自治区","150600","鄂尔多斯市","150603","康巴什区"),
        ADDRESS_150621("150000","内蒙古自治区","150600","鄂尔多斯市","150621","达拉特旗"),
        ADDRESS_150622("150000","内蒙古自治区","150600","鄂尔多斯市","150622","准格尔旗"),
        ADDRESS_150623("150000","内蒙古自治区","150600","鄂尔多斯市","150623","鄂托克前旗"),
        ADDRESS_150624("150000","内蒙古自治区","150600","鄂尔多斯市","150624","鄂托克旗"),
        ADDRESS_150625("150000","内蒙古自治区","150600","鄂尔多斯市","150625","杭锦旗"),
        ADDRESS_150626("150000","内蒙古自治区","150600","鄂尔多斯市","150626","乌审旗"),
        ADDRESS_150627("150000","内蒙古自治区","150600","鄂尔多斯市","150627","伊金霍洛旗"),
        ADDRESS_150702("150000","内蒙古自治区","150700","呼伦贝尔市","150702","海拉尔区"),
        ADDRESS_150703("150000","内蒙古自治区","150700","呼伦贝尔市","150703","扎赉诺尔区"),
        ADDRESS_150721("150000","内蒙古自治区","150700","呼伦贝尔市","150721","阿荣旗"),
        ADDRESS_150722("150000","内蒙古自治区","150700","呼伦贝尔市","150722","莫力达瓦达斡尔族自治旗"),
        ADDRESS_150723("150000","内蒙古自治区","150700","呼伦贝尔市","150723","鄂伦春自治旗"),
        ADDRESS_150724("150000","内蒙古自治区","150700","呼伦贝尔市","150724","鄂温克族自治旗"),
        ADDRESS_150725("150000","内蒙古自治区","150700","呼伦贝尔市","150725","陈巴尔虎旗"),
        ADDRESS_150726("150000","内蒙古自治区","150700","呼伦贝尔市","150726","新巴尔虎左旗"),
        ADDRESS_150727("150000","内蒙古自治区","150700","呼伦贝尔市","150727","新巴尔虎右旗"),
        ADDRESS_150781("150000","内蒙古自治区","150700","呼伦贝尔市","150781","满洲里市"),
        ADDRESS_150782("150000","内蒙古自治区","150700","呼伦贝尔市","150782","牙克石市"),
        ADDRESS_150783("150000","内蒙古自治区","150700","呼伦贝尔市","150783","扎兰屯市"),
        ADDRESS_150784("150000","内蒙古自治区","150700","呼伦贝尔市","150784","额尔古纳市"),
        ADDRESS_150785("150000","内蒙古自治区","150700","呼伦贝尔市","150785","根河市"),
        ADDRESS_150802("150000","内蒙古自治区","150800","巴彦淖尔市","150802","临河区"),
        ADDRESS_150821("150000","内蒙古自治区","150800","巴彦淖尔市","150821","五原县"),
        ADDRESS_150822("150000","内蒙古自治区","150800","巴彦淖尔市","150822","磴口县"),
        ADDRESS_150823("150000","内蒙古自治区","150800","巴彦淖尔市","150823","乌拉特前旗"),
        ADDRESS_150824("150000","内蒙古自治区","150800","巴彦淖尔市","150824","乌拉特中旗"),
        ADDRESS_150825("150000","内蒙古自治区","150800","巴彦淖尔市","150825","乌拉特后旗"),
        ADDRESS_150826("150000","内蒙古自治区","150800","巴彦淖尔市","150826","杭锦后旗"),
        ADDRESS_150902("150000","内蒙古自治区","150900","乌兰察布市","150902","集宁区"),
        ADDRESS_150921("150000","内蒙古自治区","150900","乌兰察布市","150921","卓资县"),
        ADDRESS_150922("150000","内蒙古自治区","150900","乌兰察布市","150922","化德县"),
        ADDRESS_150923("150000","内蒙古自治区","150900","乌兰察布市","150923","商都县"),
        ADDRESS_150924("150000","内蒙古自治区","150900","乌兰察布市","150924","兴和县"),
        ADDRESS_150925("150000","内蒙古自治区","150900","乌兰察布市","150925","凉城县"),
        ADDRESS_150926("150000","内蒙古自治区","150900","乌兰察布市","150926","察哈尔右翼前旗"),
        ADDRESS_150927("150000","内蒙古自治区","150900","乌兰察布市","150927","察哈尔右翼中旗"),
        ADDRESS_150928("150000","内蒙古自治区","150900","乌兰察布市","150928","察哈尔右翼后旗"),
        ADDRESS_150929("150000","内蒙古自治区","150900","乌兰察布市","150929","四子王旗"),
        ADDRESS_150981("150000","内蒙古自治区","150900","乌兰察布市","150981","丰镇市"),
        ADDRESS_152201("150000","内蒙古自治区","152200","兴安盟","152201","乌兰浩特市"),
        ADDRESS_152202("150000","内蒙古自治区","152200","兴安盟","152202","阿尔山市"),
        ADDRESS_152221("150000","内蒙古自治区","152200","兴安盟","152221","科尔沁右翼前旗"),
        ADDRESS_152222("150000","内蒙古自治区","152200","兴安盟","152222","科尔沁右翼中旗"),
        ADDRESS_152223("150000","内蒙古自治区","152200","兴安盟","152223","扎赉特旗"),
        ADDRESS_152224("150000","内蒙古自治区","152200","兴安盟","152224","突泉县"),
        ADDRESS_152501("150000","内蒙古自治区","152500","锡林郭勒盟","152501","二连浩特市"),
        ADDRESS_152502("150000","内蒙古自治区","152500","锡林郭勒盟","152502","锡林浩特市"),
        ADDRESS_152522("150000","内蒙古自治区","152500","锡林郭勒盟","152522","阿巴嘎旗"),
        ADDRESS_152523("150000","内蒙古自治区","152500","锡林郭勒盟","152523","苏尼特左旗"),
        ADDRESS_152524("150000","内蒙古自治区","152500","锡林郭勒盟","152524","苏尼特右旗"),
        ADDRESS_152525("150000","内蒙古自治区","152500","锡林郭勒盟","152525","东乌珠穆沁旗"),
        ADDRESS_152526("150000","内蒙古自治区","152500","锡林郭勒盟","152526","西乌珠穆沁旗"),
        ADDRESS_152527("150000","内蒙古自治区","152500","锡林郭勒盟","152527","太仆寺旗"),
        ADDRESS_152528("150000","内蒙古自治区","152500","锡林郭勒盟","152528","镶黄旗"),
        ADDRESS_152529("150000","内蒙古自治区","152500","锡林郭勒盟","152529","正镶白旗"),
        ADDRESS_152530("150000","内蒙古自治区","152500","锡林郭勒盟","152530","正蓝旗"),
        ADDRESS_152531("150000","内蒙古自治区","152500","锡林郭勒盟","152531","多伦县"),
        ADDRESS_152921("150000","内蒙古自治区","152900","阿拉善盟","152921","阿拉善左旗"),
        ADDRESS_152922("150000","内蒙古自治区","152900","阿拉善盟","152922","阿拉善右旗"),
        ADDRESS_152923("150000","内蒙古自治区","152900","阿拉善盟","152923","额济纳旗"),

        ;



        AddressCode150000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode150000Enum addressCodeEnum :AddressCode150000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 辽宁省枚举
     */
    public enum AddressCode210000Enum {

        ADDRESS_210102("210000","辽宁省","210100","沈阳市","210102","和平区"),
        ADDRESS_210103("210000","辽宁省","210100","沈阳市","210103","沈河区"),
        ADDRESS_210104("210000","辽宁省","210100","沈阳市","210104","大东区"),
        ADDRESS_210105("210000","辽宁省","210100","沈阳市","210105","皇姑区"),
        ADDRESS_210106("210000","辽宁省","210100","沈阳市","210106","铁西区"),
        ADDRESS_210111("210000","辽宁省","210100","沈阳市","210111","苏家屯区"),
        ADDRESS_210112("210000","辽宁省","210100","沈阳市","210112","浑南区"),
        ADDRESS_210113("210000","辽宁省","210100","沈阳市","210113","沈北新区"),
        ADDRESS_210114("210000","辽宁省","210100","沈阳市","210114","于洪区"),
        ADDRESS_210115("210000","辽宁省","210100","沈阳市","210115","辽中区"),
        ADDRESS_210123("210000","辽宁省","210100","沈阳市","210123","康平县"),
        ADDRESS_210124("210000","辽宁省","210100","沈阳市","210124","法库县"),
        ADDRESS_210181("210000","辽宁省","210100","沈阳市","210181","新民市"),
        ADDRESS_210202("210000","辽宁省","210200","大连市","210202","中山区"),
        ADDRESS_210203("210000","辽宁省","210200","大连市","210203","西岗区"),
        ADDRESS_210204("210000","辽宁省","210200","大连市","210204","沙河口区"),
        ADDRESS_210211("210000","辽宁省","210200","大连市","210211","甘井子区"),
        ADDRESS_210212("210000","辽宁省","210200","大连市","210212","旅顺口区"),
        ADDRESS_210213("210000","辽宁省","210200","大连市","210213","金州区"),
        ADDRESS_210214("210000","辽宁省","210200","大连市","210214","普兰店区"),
        ADDRESS_210224("210000","辽宁省","210200","大连市","210224","长海县"),
        ADDRESS_210281("210000","辽宁省","210200","大连市","210281","瓦房店市"),
        ADDRESS_210283("210000","辽宁省","210200","大连市","210283","庄河市"),
        ADDRESS_210302("210000","辽宁省","210300","鞍山市","210302","铁东区"),
        ADDRESS_210303("210000","辽宁省","210300","鞍山市","210303","铁西区"),
        ADDRESS_210304("210000","辽宁省","210300","鞍山市","210304","立山区"),
        ADDRESS_210311("210000","辽宁省","210300","鞍山市","210311","千山区"),
        ADDRESS_210321("210000","辽宁省","210300","鞍山市","210321","台安县"),
        ADDRESS_210323("210000","辽宁省","210300","鞍山市","210323","岫岩满族自治县"),
        ADDRESS_210381("210000","辽宁省","210300","鞍山市","210381","海城市"),
        ADDRESS_210402("210000","辽宁省","210400","抚顺市","210402","新抚区"),
        ADDRESS_210403("210000","辽宁省","210400","抚顺市","210403","东洲区"),
        ADDRESS_210404("210000","辽宁省","210400","抚顺市","210404","望花区"),
        ADDRESS_210411("210000","辽宁省","210400","抚顺市","210411","顺城区"),
        ADDRESS_210421("210000","辽宁省","210400","抚顺市","210421","抚顺县"),
        ADDRESS_210422("210000","辽宁省","210400","抚顺市","210422","新宾满族自治县"),
        ADDRESS_210423("210000","辽宁省","210400","抚顺市","210423","清原满族自治县"),
        ADDRESS_210502("210000","辽宁省","210500","本溪市","210502","平山区"),
        ADDRESS_210503("210000","辽宁省","210500","本溪市","210503","溪湖区"),
        ADDRESS_210504("210000","辽宁省","210500","本溪市","210504","明山区"),
        ADDRESS_210505("210000","辽宁省","210500","本溪市","210505","南芬区"),
        ADDRESS_210521("210000","辽宁省","210500","本溪市","210521","本溪满族自治县"),
        ADDRESS_210522("210000","辽宁省","210500","本溪市","210522","桓仁满族自治县"),
        ADDRESS_210602("210000","辽宁省","210600","丹东市","210602","元宝区"),
        ADDRESS_210603("210000","辽宁省","210600","丹东市","210603","振兴区"),
        ADDRESS_210604("210000","辽宁省","210600","丹东市","210604","振安区"),
        ADDRESS_210624("210000","辽宁省","210600","丹东市","210624","宽甸满族自治县"),
        ADDRESS_210681("210000","辽宁省","210600","丹东市","210681","东港市"),
        ADDRESS_210682("210000","辽宁省","210600","丹东市","210682","凤城市"),
        ADDRESS_210702("210000","辽宁省","210700","锦州市","210702","古塔区"),
        ADDRESS_210703("210000","辽宁省","210700","锦州市","210703","凌河区"),
        ADDRESS_210711("210000","辽宁省","210700","锦州市","210711","太和区"),
        ADDRESS_210726("210000","辽宁省","210700","锦州市","210726","黑山县"),
        ADDRESS_210727("210000","辽宁省","210700","锦州市","210727","义县"),
        ADDRESS_210781("210000","辽宁省","210700","锦州市","210781","凌海市"),
        ADDRESS_210782("210000","辽宁省","210700","锦州市","210782","北镇市"),
        ADDRESS_210802("210000","辽宁省","210800","营口市","210802","站前区"),
        ADDRESS_210803("210000","辽宁省","210800","营口市","210803","西市区"),
        ADDRESS_210804("210000","辽宁省","210800","营口市","210804","鲅鱼圈区"),
        ADDRESS_210811("210000","辽宁省","210800","营口市","210811","老边区"),
        ADDRESS_210881("210000","辽宁省","210800","营口市","210881","盖州市"),
        ADDRESS_210882("210000","辽宁省","210800","营口市","210882","大石桥市"),
        ADDRESS_210902("210000","辽宁省","210900","阜新市","210902","海州区"),
        ADDRESS_210903("210000","辽宁省","210900","阜新市","210903","新邱区"),
        ADDRESS_210904("210000","辽宁省","210900","阜新市","210904","太平区"),
        ADDRESS_210905("210000","辽宁省","210900","阜新市","210905","清河门区"),
        ADDRESS_210911("210000","辽宁省","210900","阜新市","210911","细河区"),
        ADDRESS_210921("210000","辽宁省","210900","阜新市","210921","阜新蒙古族自治县"),
        ADDRESS_210922("210000","辽宁省","210900","阜新市","210922","彰武县"),
        ADDRESS_211002("210000","辽宁省","211000","辽阳市","211002","白塔区"),
        ADDRESS_211003("210000","辽宁省","211000","辽阳市","211003","文圣区"),
        ADDRESS_211004("210000","辽宁省","211000","辽阳市","211004","宏伟区"),
        ADDRESS_211005("210000","辽宁省","211000","辽阳市","211005","弓长岭区"),
        ADDRESS_211011("210000","辽宁省","211000","辽阳市","211011","太子河区"),
        ADDRESS_211021("210000","辽宁省","211000","辽阳市","211021","辽阳县"),
        ADDRESS_211081("210000","辽宁省","211000","辽阳市","211081","灯塔市"),
        ADDRESS_211102("210000","辽宁省","211100","盘锦市","211102","双台子区"),
        ADDRESS_211103("210000","辽宁省","211100","盘锦市","211103","兴隆台区"),
        ADDRESS_211104("210000","辽宁省","211100","盘锦市","211104","大洼区"),
        ADDRESS_211122("210000","辽宁省","211100","盘锦市","211122","盘山县"),
        ADDRESS_211202("210000","辽宁省","211200","铁岭市","211202","银州区"),
        ADDRESS_211204("210000","辽宁省","211200","铁岭市","211204","清河区"),
        ADDRESS_211221("210000","辽宁省","211200","铁岭市","211221","铁岭县"),
        ADDRESS_211223("210000","辽宁省","211200","铁岭市","211223","西丰县"),
        ADDRESS_211224("210000","辽宁省","211200","铁岭市","211224","昌图县"),
        ADDRESS_211281("210000","辽宁省","211200","铁岭市","211281","调兵山市"),
        ADDRESS_211282("210000","辽宁省","211200","铁岭市","211282","开原市"),
        ADDRESS_211302("210000","辽宁省","211300","朝阳市","211302","双塔区"),
        ADDRESS_211303("210000","辽宁省","211300","朝阳市","211303","龙城区"),
        ADDRESS_211321("210000","辽宁省","211300","朝阳市","211321","朝阳县"),
        ADDRESS_211322("210000","辽宁省","211300","朝阳市","211322","建平县"),
        ADDRESS_211324("210000","辽宁省","211300","朝阳市","211324","喀喇沁左翼蒙古族自治县"),
        ADDRESS_211381("210000","辽宁省","211300","朝阳市","211381","北票市"),
        ADDRESS_211382("210000","辽宁省","211300","朝阳市","211382","凌源市"),
        ADDRESS_211402("210000","辽宁省","211400","葫芦岛市","211402","连山区"),
        ADDRESS_211403("210000","辽宁省","211400","葫芦岛市","211403","龙港区"),
        ADDRESS_211404("210000","辽宁省","211400","葫芦岛市","211404","南票区"),
        ADDRESS_211421("210000","辽宁省","211400","葫芦岛市","211421","绥中县"),
        ADDRESS_211422("210000","辽宁省","211400","葫芦岛市","211422","建昌县"),
        ADDRESS_211481("210000","辽宁省","211400","葫芦岛市","211481","兴城市"),

        ;



        AddressCode210000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode210000Enum addressCodeEnum :AddressCode210000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 吉林省枚举
     */
    public enum AddressCode220000Enum {

        ADDRESS_220102("220000","吉林省","220100","长春市","220102","南关区"),
        ADDRESS_220103("220000","吉林省","220100","长春市","220103","宽城区"),
        ADDRESS_220104("220000","吉林省","220100","长春市","220104","朝阳区"),
        ADDRESS_220105("220000","吉林省","220100","长春市","220105","二道区"),
        ADDRESS_220106("220000","吉林省","220100","长春市","220106","绿园区"),
        ADDRESS_220112("220000","吉林省","220100","长春市","220112","双阳区"),
        ADDRESS_220113("220000","吉林省","220100","长春市","220113","九台区"),
        ADDRESS_220122("220000","吉林省","220100","长春市","220122","农安县"),
        ADDRESS_220182("220000","吉林省","220100","长春市","220182","榆树市"),
        ADDRESS_220183("220000","吉林省","220100","长春市","220183","德惠市"),
        ADDRESS_220184("220000","吉林省","220100","长春市","220184","公主岭市"),
        ADDRESS_220202("220000","吉林省","220200","吉林市","220202","昌邑区"),
        ADDRESS_220203("220000","吉林省","220200","吉林市","220203","龙潭区"),
        ADDRESS_220204("220000","吉林省","220200","吉林市","220204","船营区"),
        ADDRESS_220211("220000","吉林省","220200","吉林市","220211","丰满区"),
        ADDRESS_220221("220000","吉林省","220200","吉林市","220221","永吉县"),
        ADDRESS_220281("220000","吉林省","220200","吉林市","220281","蛟河市"),
        ADDRESS_220282("220000","吉林省","220200","吉林市","220282","桦甸市"),
        ADDRESS_220283("220000","吉林省","220200","吉林市","220283","舒兰市"),
        ADDRESS_220284("220000","吉林省","220200","吉林市","220284","磐石市"),
        ADDRESS_220302("220000","吉林省","220300","四平市","220302","铁西区"),
        ADDRESS_220303("220000","吉林省","220300","四平市","220303","铁东区"),
        ADDRESS_220322("220000","吉林省","220300","四平市","220322","梨树县"),
        ADDRESS_220323("220000","吉林省","220300","四平市","220323","伊通满族自治县"),
        ADDRESS_220382("220000","吉林省","220300","四平市","220382","双辽市"),
        ADDRESS_220402("220000","吉林省","220400","辽源市","220402","龙山区"),
        ADDRESS_220403("220000","吉林省","220400","辽源市","220403","西安区"),
        ADDRESS_220421("220000","吉林省","220400","辽源市","220421","东丰县"),
        ADDRESS_220422("220000","吉林省","220400","辽源市","220422","东辽县"),
        ADDRESS_220502("220000","吉林省","220500","通化市","220502","东昌区"),
        ADDRESS_220503("220000","吉林省","220500","通化市","220503","二道江区"),
        ADDRESS_220521("220000","吉林省","220500","通化市","220521","通化县"),
        ADDRESS_220523("220000","吉林省","220500","通化市","220523","辉南县"),
        ADDRESS_220524("220000","吉林省","220500","通化市","220524","柳河县"),
        ADDRESS_220581("220000","吉林省","220500","通化市","220581","梅河口市"),
        ADDRESS_220582("220000","吉林省","220500","通化市","220582","集安市"),
        ADDRESS_220602("220000","吉林省","220600","白山市","220602","浑江区"),
        ADDRESS_220605("220000","吉林省","220600","白山市","220605","江源区"),
        ADDRESS_220621("220000","吉林省","220600","白山市","220621","抚松县"),
        ADDRESS_220622("220000","吉林省","220600","白山市","220622","靖宇县"),
        ADDRESS_220623("220000","吉林省","220600","白山市","220623","长白朝鲜族自治县"),
        ADDRESS_220681("220000","吉林省","220600","白山市","220681","临江市"),
        ADDRESS_220702("220000","吉林省","220700","松原市","220702","宁江区"),
        ADDRESS_220721("220000","吉林省","220700","松原市","220721","前郭尔罗斯蒙古族自治县"),
        ADDRESS_220722("220000","吉林省","220700","松原市","220722","长岭县"),
        ADDRESS_220723("220000","吉林省","220700","松原市","220723","乾安县"),
        ADDRESS_220781("220000","吉林省","220700","松原市","220781","扶余市"),
        ADDRESS_220802("220000","吉林省","220800","白城市","220802","洮北区"),
        ADDRESS_220821("220000","吉林省","220800","白城市","220821","镇赉县"),
        ADDRESS_220822("220000","吉林省","220800","白城市","220822","通榆县"),
        ADDRESS_220881("220000","吉林省","220800","白城市","220881","洮南市"),
        ADDRESS_220882("220000","吉林省","220800","白城市","220882","大安市"),
        ADDRESS_222401("220000","吉林省","222400","延边朝鲜族自治州","222401","延吉市"),
        ADDRESS_222402("220000","吉林省","222400","延边朝鲜族自治州","222402","图们市"),
        ADDRESS_222403("220000","吉林省","222400","延边朝鲜族自治州","222403","敦化市"),
        ADDRESS_222404("220000","吉林省","222400","延边朝鲜族自治州","222404","珲春市"),
        ADDRESS_222405("220000","吉林省","222400","延边朝鲜族自治州","222405","龙井市"),
        ADDRESS_222406("220000","吉林省","222400","延边朝鲜族自治州","222406","和龙市"),
        ADDRESS_222424("220000","吉林省","222400","延边朝鲜族自治州","222424","汪清县"),
        ADDRESS_222426("220000","吉林省","222400","延边朝鲜族自治州","222426","安图县"),

        ;



        AddressCode220000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode220000Enum addressCodeEnum :AddressCode220000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 黑龙江省枚举
     */
    public enum AddressCode230000Enum {

        ADDRESS_230102("230000","黑龙江省","230100","哈尔滨市","230102","道里区"),
        ADDRESS_230103("230000","黑龙江省","230100","哈尔滨市","230103","南岗区"),
        ADDRESS_230104("230000","黑龙江省","230100","哈尔滨市","230104","道外区"),
        ADDRESS_230108("230000","黑龙江省","230100","哈尔滨市","230108","平房区"),
        ADDRESS_230109("230000","黑龙江省","230100","哈尔滨市","230109","松北区"),
        ADDRESS_230110("230000","黑龙江省","230100","哈尔滨市","230110","香坊区"),
        ADDRESS_230111("230000","黑龙江省","230100","哈尔滨市","230111","呼兰区"),
        ADDRESS_230112("230000","黑龙江省","230100","哈尔滨市","230112","阿城区"),
        ADDRESS_230113("230000","黑龙江省","230100","哈尔滨市","230113","双城区"),
        ADDRESS_230123("230000","黑龙江省","230100","哈尔滨市","230123","依兰县"),
        ADDRESS_230124("230000","黑龙江省","230100","哈尔滨市","230124","方正县"),
        ADDRESS_230125("230000","黑龙江省","230100","哈尔滨市","230125","宾县"),
        ADDRESS_230126("230000","黑龙江省","230100","哈尔滨市","230126","巴彦县"),
        ADDRESS_230127("230000","黑龙江省","230100","哈尔滨市","230127","木兰县"),
        ADDRESS_230128("230000","黑龙江省","230100","哈尔滨市","230128","通河县"),
        ADDRESS_230129("230000","黑龙江省","230100","哈尔滨市","230129","延寿县"),
        ADDRESS_230183("230000","黑龙江省","230100","哈尔滨市","230183","尚志市"),
        ADDRESS_230184("230000","黑龙江省","230100","哈尔滨市","230184","五常市"),
        ADDRESS_230202("230000","黑龙江省","230200","齐齐哈尔市","230202","龙沙区"),
        ADDRESS_230203("230000","黑龙江省","230200","齐齐哈尔市","230203","建华区"),
        ADDRESS_230204("230000","黑龙江省","230200","齐齐哈尔市","230204","铁锋区"),
        ADDRESS_230205("230000","黑龙江省","230200","齐齐哈尔市","230205","昂昂溪区"),
        ADDRESS_230206("230000","黑龙江省","230200","齐齐哈尔市","230206","富拉尔基区"),
        ADDRESS_230207("230000","黑龙江省","230200","齐齐哈尔市","230207","碾子山区"),
        ADDRESS_230208("230000","黑龙江省","230200","齐齐哈尔市","230208","梅里斯达斡尔族区"),
        ADDRESS_230221("230000","黑龙江省","230200","齐齐哈尔市","230221","龙江县"),
        ADDRESS_230223("230000","黑龙江省","230200","齐齐哈尔市","230223","依安县"),
        ADDRESS_230224("230000","黑龙江省","230200","齐齐哈尔市","230224","泰来县"),
        ADDRESS_230225("230000","黑龙江省","230200","齐齐哈尔市","230225","甘南县"),
        ADDRESS_230227("230000","黑龙江省","230200","齐齐哈尔市","230227","富裕县"),
        ADDRESS_230229("230000","黑龙江省","230200","齐齐哈尔市","230229","克山县"),
        ADDRESS_230230("230000","黑龙江省","230200","齐齐哈尔市","230230","克东县"),
        ADDRESS_230231("230000","黑龙江省","230200","齐齐哈尔市","230231","拜泉县"),
        ADDRESS_230281("230000","黑龙江省","230200","齐齐哈尔市","230281","讷河市"),
        ADDRESS_230302("230000","黑龙江省","230300","鸡西市","230302","鸡冠区"),
        ADDRESS_230303("230000","黑龙江省","230300","鸡西市","230303","恒山区"),
        ADDRESS_230304("230000","黑龙江省","230300","鸡西市","230304","滴道区"),
        ADDRESS_230305("230000","黑龙江省","230300","鸡西市","230305","梨树区"),
        ADDRESS_230306("230000","黑龙江省","230300","鸡西市","230306","城子河区"),
        ADDRESS_230307("230000","黑龙江省","230300","鸡西市","230307","麻山区"),
        ADDRESS_230321("230000","黑龙江省","230300","鸡西市","230321","鸡东县"),
        ADDRESS_230381("230000","黑龙江省","230300","鸡西市","230381","虎林市"),
        ADDRESS_230382("230000","黑龙江省","230300","鸡西市","230382","密山市"),
        ADDRESS_230402("230000","黑龙江省","230400","鹤岗市","230402","向阳区"),
        ADDRESS_230403("230000","黑龙江省","230400","鹤岗市","230403","工农区"),
        ADDRESS_230404("230000","黑龙江省","230400","鹤岗市","230404","南山区"),
        ADDRESS_230405("230000","黑龙江省","230400","鹤岗市","230405","兴安区"),
        ADDRESS_230406("230000","黑龙江省","230400","鹤岗市","230406","东山区"),
        ADDRESS_230407("230000","黑龙江省","230400","鹤岗市","230407","兴山区"),
        ADDRESS_230421("230000","黑龙江省","230400","鹤岗市","230421","萝北县"),
        ADDRESS_230422("230000","黑龙江省","230400","鹤岗市","230422","绥滨县"),
        ADDRESS_230502("230000","黑龙江省","230500","双鸭山市","230502","尖山区"),
        ADDRESS_230503("230000","黑龙江省","230500","双鸭山市","230503","岭东区"),
        ADDRESS_230505("230000","黑龙江省","230500","双鸭山市","230505","四方台区"),
        ADDRESS_230506("230000","黑龙江省","230500","双鸭山市","230506","宝山区"),
        ADDRESS_230521("230000","黑龙江省","230500","双鸭山市","230521","集贤县"),
        ADDRESS_230522("230000","黑龙江省","230500","双鸭山市","230522","友谊县"),
        ADDRESS_230523("230000","黑龙江省","230500","双鸭山市","230523","宝清县"),
        ADDRESS_230524("230000","黑龙江省","230500","双鸭山市","230524","饶河县"),
        ADDRESS_230602("230000","黑龙江省","230600","大庆市","230602","萨尔图区"),
        ADDRESS_230603("230000","黑龙江省","230600","大庆市","230603","龙凤区"),
        ADDRESS_230604("230000","黑龙江省","230600","大庆市","230604","让胡路区"),
        ADDRESS_230605("230000","黑龙江省","230600","大庆市","230605","红岗区"),
        ADDRESS_230606("230000","黑龙江省","230600","大庆市","230606","大同区"),
        ADDRESS_230621("230000","黑龙江省","230600","大庆市","230621","肇州县"),
        ADDRESS_230622("230000","黑龙江省","230600","大庆市","230622","肇源县"),
        ADDRESS_230623("230000","黑龙江省","230600","大庆市","230623","林甸县"),
        ADDRESS_230624("230000","黑龙江省","230600","大庆市","230624","杜尔伯特蒙古族自治县"),
        ADDRESS_230717("230000","黑龙江省","230700","伊春市","230717","伊美区"),
        ADDRESS_230718("230000","黑龙江省","230700","伊春市","230718","乌翠区"),
        ADDRESS_230719("230000","黑龙江省","230700","伊春市","230719","友好区"),
        ADDRESS_230722("230000","黑龙江省","230700","伊春市","230722","嘉荫县"),
        ADDRESS_230723("230000","黑龙江省","230700","伊春市","230723","汤旺县"),
        ADDRESS_230724("230000","黑龙江省","230700","伊春市","230724","丰林县"),
        ADDRESS_230725("230000","黑龙江省","230700","伊春市","230725","大箐山县"),
        ADDRESS_230726("230000","黑龙江省","230700","伊春市","230726","南岔县"),
        ADDRESS_230751("230000","黑龙江省","230700","伊春市","230751","金林区"),
        ADDRESS_230781("230000","黑龙江省","230700","伊春市","230781","铁力市"),
        ADDRESS_230803("230000","黑龙江省","230800","佳木斯市","230803","向阳区"),
        ADDRESS_230804("230000","黑龙江省","230800","佳木斯市","230804","前进区"),
        ADDRESS_230805("230000","黑龙江省","230800","佳木斯市","230805","东风区"),
        ADDRESS_230811("230000","黑龙江省","230800","佳木斯市","230811","郊区"),
        ADDRESS_230822("230000","黑龙江省","230800","佳木斯市","230822","桦南县"),
        ADDRESS_230826("230000","黑龙江省","230800","佳木斯市","230826","桦川县"),
        ADDRESS_230828("230000","黑龙江省","230800","佳木斯市","230828","汤原县"),
        ADDRESS_230881("230000","黑龙江省","230800","佳木斯市","230881","同江市"),
        ADDRESS_230882("230000","黑龙江省","230800","佳木斯市","230882","富锦市"),
        ADDRESS_230883("230000","黑龙江省","230800","佳木斯市","230883","抚远市"),
        ADDRESS_230902("230000","黑龙江省","230900","七台河市","230902","新兴区"),
        ADDRESS_230903("230000","黑龙江省","230900","七台河市","230903","桃山区"),
        ADDRESS_230904("230000","黑龙江省","230900","七台河市","230904","茄子河区"),
        ADDRESS_230921("230000","黑龙江省","230900","七台河市","230921","勃利县"),
        ADDRESS_231002("230000","黑龙江省","231000","牡丹江市","231002","东安区"),
        ADDRESS_231003("230000","黑龙江省","231000","牡丹江市","231003","阳明区"),
        ADDRESS_231004("230000","黑龙江省","231000","牡丹江市","231004","爱民区"),
        ADDRESS_231005("230000","黑龙江省","231000","牡丹江市","231005","西安区"),
        ADDRESS_231025("230000","黑龙江省","231000","牡丹江市","231025","林口县"),
        ADDRESS_231081("230000","黑龙江省","231000","牡丹江市","231081","绥芬河市"),
        ADDRESS_231083("230000","黑龙江省","231000","牡丹江市","231083","海林市"),
        ADDRESS_231084("230000","黑龙江省","231000","牡丹江市","231084","宁安市"),
        ADDRESS_231085("230000","黑龙江省","231000","牡丹江市","231085","穆棱市"),
        ADDRESS_231086("230000","黑龙江省","231000","牡丹江市","231086","东宁市"),
        ADDRESS_231102("230000","黑龙江省","231100","黑河市","231102","爱辉区"),
        ADDRESS_231123("230000","黑龙江省","231100","黑河市","231123","逊克县"),
        ADDRESS_231124("230000","黑龙江省","231100","黑河市","231124","孙吴县"),
        ADDRESS_231181("230000","黑龙江省","231100","黑河市","231181","北安市"),
        ADDRESS_231182("230000","黑龙江省","231100","黑河市","231182","五大连池市"),
        ADDRESS_231183("230000","黑龙江省","231100","黑河市","231183","嫩江市"),
        ADDRESS_231202("230000","黑龙江省","231200","绥化市","231202","北林区"),
        ADDRESS_231221("230000","黑龙江省","231200","绥化市","231221","望奎县"),
        ADDRESS_231222("230000","黑龙江省","231200","绥化市","231222","兰西县"),
        ADDRESS_231223("230000","黑龙江省","231200","绥化市","231223","青冈县"),
        ADDRESS_231224("230000","黑龙江省","231200","绥化市","231224","庆安县"),
        ADDRESS_231225("230000","黑龙江省","231200","绥化市","231225","明水县"),
        ADDRESS_231226("230000","黑龙江省","231200","绥化市","231226","绥棱县"),
        ADDRESS_231281("230000","黑龙江省","231200","绥化市","231281","安达市"),
        ADDRESS_231282("230000","黑龙江省","231200","绥化市","231282","肇东市"),
        ADDRESS_231283("230000","黑龙江省","231200","绥化市","231283","海伦市"),
        ADDRESS_232701("230000","黑龙江省","232700","大兴安岭地区","232701","漠河市"),
        ADDRESS_232721("230000","黑龙江省","232700","大兴安岭地区","232721","呼玛县"),
        ADDRESS_232722("230000","黑龙江省","232700","大兴安岭地区","232722","塔河县"),

        ;



        AddressCode230000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode230000Enum addressCodeEnum :AddressCode230000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 上海枚举
     */
    public enum AddressCode310000Enum {

        ADDRESS_310101("310000","上海","310100","上海市","310101","黄浦区"),
        ADDRESS_310104("310000","上海","310100","上海市","310104","徐汇区"),
        ADDRESS_310105("310000","上海","310100","上海市","310105","长宁区"),
        ADDRESS_310106("310000","上海","310100","上海市","310106","静安区"),
        ADDRESS_310107("310000","上海","310100","上海市","310107","普陀区"),
        ADDRESS_310109("310000","上海","310100","上海市","310109","虹口区"),
        ADDRESS_310110("310000","上海","310100","上海市","310110","杨浦区"),
        ADDRESS_310112("310000","上海","310100","上海市","310112","闵行区"),
        ADDRESS_310113("310000","上海","310100","上海市","310113","宝山区"),
        ADDRESS_310114("310000","上海","310100","上海市","310114","嘉定区"),
        ADDRESS_310115("310000","上海","310100","上海市","310115","浦东新区"),
        ADDRESS_310116("310000","上海","310100","上海市","310116","金山区"),
        ADDRESS_310117("310000","上海","310100","上海市","310117","松江区"),
        ADDRESS_310118("310000","上海","310100","上海市","310118","青浦区"),
        ADDRESS_310120("310000","上海","310100","上海市","310120","奉贤区"),
        ADDRESS_310151("310000","上海","310100","上海市","310151","崇明区"),

        ;

        AddressCode310000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode310000Enum addressCodeEnum :AddressCode310000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 江苏省 枚举
     */
    public enum AddressCode320000Enum {

        ADDRESS_320102("320000","江苏省","320100","南京市","320102","玄武区"),
        ADDRESS_320104("320000","江苏省","320100","南京市","320104","秦淮区"),
        ADDRESS_320105("320000","江苏省","320100","南京市","320105","建邺区"),
        ADDRESS_320106("320000","江苏省","320100","南京市","320106","鼓楼区"),
        ADDRESS_320111("320000","江苏省","320100","南京市","320111","浦口区"),
        ADDRESS_320113("320000","江苏省","320100","南京市","320113","栖霞区"),
        ADDRESS_320114("320000","江苏省","320100","南京市","320114","雨花台区"),
        ADDRESS_320115("320000","江苏省","320100","南京市","320115","江宁区"),
        ADDRESS_320116("320000","江苏省","320100","南京市","320116","六合区"),
        ADDRESS_320117("320000","江苏省","320100","南京市","320117","溧水区"),
        ADDRESS_320118("320000","江苏省","320100","南京市","320118","高淳区"),
        ADDRESS_320205("320000","江苏省","320200","无锡市","320205","锡山区"),
        ADDRESS_320206("320000","江苏省","320200","无锡市","320206","惠山区"),
        ADDRESS_320211("320000","江苏省","320200","无锡市","320211","滨湖区"),
        ADDRESS_320213("320000","江苏省","320200","无锡市","320213","梁溪区"),
        ADDRESS_320214("320000","江苏省","320200","无锡市","320214","新吴区"),
        ADDRESS_320281("320000","江苏省","320200","无锡市","320281","江阴市"),
        ADDRESS_320282("320000","江苏省","320200","无锡市","320282","宜兴市"),
        ADDRESS_320302("320000","江苏省","320300","徐州市","320302","鼓楼区"),
        ADDRESS_320303("320000","江苏省","320300","徐州市","320303","云龙区"),
        ADDRESS_320305("320000","江苏省","320300","徐州市","320305","贾汪区"),
        ADDRESS_320311("320000","江苏省","320300","徐州市","320311","泉山区"),
        ADDRESS_320312("320000","江苏省","320300","徐州市","320312","铜山区"),
        ADDRESS_320321("320000","江苏省","320300","徐州市","320321","丰县"),
        ADDRESS_320322("320000","江苏省","320300","徐州市","320322","沛县"),
        ADDRESS_320324("320000","江苏省","320300","徐州市","320324","睢宁县"),
        ADDRESS_320381("320000","江苏省","320300","徐州市","320381","新沂市"),
        ADDRESS_320382("320000","江苏省","320300","徐州市","320382","邳州市"),
        ADDRESS_320402("320000","江苏省","320400","常州市","320402","天宁区"),
        ADDRESS_320404("320000","江苏省","320400","常州市","320404","钟楼区"),
        ADDRESS_320411("320000","江苏省","320400","常州市","320411","新北区"),
        ADDRESS_320412("320000","江苏省","320400","常州市","320412","武进区"),
        ADDRESS_320413("320000","江苏省","320400","常州市","320413","金坛区"),
        ADDRESS_320481("320000","江苏省","320400","常州市","320481","溧阳市"),
        ADDRESS_320505("320000","江苏省","320500","苏州市","320505","虎丘区"),
        ADDRESS_320506("320000","江苏省","320500","苏州市","320506","吴中区"),
        ADDRESS_320507("320000","江苏省","320500","苏州市","320507","相城区"),
        ADDRESS_320508("320000","江苏省","320500","苏州市","320508","姑苏区"),
        ADDRESS_320509("320000","江苏省","320500","苏州市","320509","吴江区"),
        ADDRESS_320581("320000","江苏省","320500","苏州市","320581","常熟市"),
        ADDRESS_320582("320000","江苏省","320500","苏州市","320582","张家港市"),
        ADDRESS_320583("320000","江苏省","320500","苏州市","320583","昆山市"),
        ADDRESS_320585("320000","江苏省","320500","苏州市","320585","太仓市"),
        ADDRESS_320612("320000","江苏省","320600","南通市","320612","通州区"),
        ADDRESS_320613("320000","江苏省","320600","南通市","320613","崇川区"),
        ADDRESS_320614("320000","江苏省","320600","南通市","320614","海门区"),
        ADDRESS_320623("320000","江苏省","320600","南通市","320623","如东县"),
        ADDRESS_320681("320000","江苏省","320600","南通市","320681","启东市"),
        ADDRESS_320682("320000","江苏省","320600","南通市","320682","如皋市"),
        ADDRESS_320685("320000","江苏省","320600","南通市","320685","海安市"),
        ADDRESS_320703("320000","江苏省","320700","连云港市","320703","连云区"),
        ADDRESS_320706("320000","江苏省","320700","连云港市","320706","海州区"),
        ADDRESS_320707("320000","江苏省","320700","连云港市","320707","赣榆区"),
        ADDRESS_320722("320000","江苏省","320700","连云港市","320722","东海县"),
        ADDRESS_320723("320000","江苏省","320700","连云港市","320723","灌云县"),
        ADDRESS_320724("320000","江苏省","320700","连云港市","320724","灌南县"),
        ADDRESS_320803("320000","江苏省","320800","淮安市","320803","淮安区"),
        ADDRESS_320804("320000","江苏省","320800","淮安市","320804","淮阴区"),
        ADDRESS_320812("320000","江苏省","320800","淮安市","320812","清江浦区"),
        ADDRESS_320813("320000","江苏省","320800","淮安市","320813","洪泽区"),
        ADDRESS_320826("320000","江苏省","320800","淮安市","320826","涟水县"),
        ADDRESS_320830("320000","江苏省","320800","淮安市","320830","盱眙县"),
        ADDRESS_320831("320000","江苏省","320800","淮安市","320831","金湖县"),
        ADDRESS_320902("320000","江苏省","320900","盐城市","320902","亭湖区"),
        ADDRESS_320903("320000","江苏省","320900","盐城市","320903","盐都区"),
        ADDRESS_320904("320000","江苏省","320900","盐城市","320904","大丰区"),
        ADDRESS_320921("320000","江苏省","320900","盐城市","320921","响水县"),
        ADDRESS_320922("320000","江苏省","320900","盐城市","320922","滨海县"),
        ADDRESS_320923("320000","江苏省","320900","盐城市","320923","阜宁县"),
        ADDRESS_320924("320000","江苏省","320900","盐城市","320924","射阳县"),
        ADDRESS_320925("320000","江苏省","320900","盐城市","320925","建湖县"),
        ADDRESS_320981("320000","江苏省","320900","盐城市","320981","东台市"),
        ADDRESS_321002("320000","江苏省","321000","扬州市","321002","广陵区"),
        ADDRESS_321003("320000","江苏省","321000","扬州市","321003","邗江区"),
        ADDRESS_321012("320000","江苏省","321000","扬州市","321012","江都区"),
        ADDRESS_321023("320000","江苏省","321000","扬州市","321023","宝应县"),
        ADDRESS_321081("320000","江苏省","321000","扬州市","321081","仪征市"),
        ADDRESS_321084("320000","江苏省","321000","扬州市","321084","高邮市"),
        ADDRESS_321102("320000","江苏省","321100","镇江市","321102","京口区"),
        ADDRESS_321111("320000","江苏省","321100","镇江市","321111","润州区"),
        ADDRESS_321112("320000","江苏省","321100","镇江市","321112","丹徒区"),
        ADDRESS_321181("320000","江苏省","321100","镇江市","321181","丹阳市"),
        ADDRESS_321182("320000","江苏省","321100","镇江市","321182","扬中市"),
        ADDRESS_321183("320000","江苏省","321100","镇江市","321183","句容市"),
        ADDRESS_321202("320000","江苏省","321200","泰州市","321202","海陵区"),
        ADDRESS_321203("320000","江苏省","321200","泰州市","321203","高港区"),
        ADDRESS_321204("320000","江苏省","321200","泰州市","321204","姜堰区"),
        ADDRESS_321281("320000","江苏省","321200","泰州市","321281","兴化市"),
        ADDRESS_321282("320000","江苏省","321200","泰州市","321282","靖江市"),
        ADDRESS_321283("320000","江苏省","321200","泰州市","321283","泰兴市"),
        ADDRESS_321302("320000","江苏省","321300","宿迁市","321302","宿城区"),
        ADDRESS_321311("320000","江苏省","321300","宿迁市","321311","宿豫区"),
        ADDRESS_321322("320000","江苏省","321300","宿迁市","321322","沭阳县"),
        ADDRESS_321323("320000","江苏省","321300","宿迁市","321323","泗阳县"),
        ADDRESS_321324("320000","江苏省","321300","宿迁市","321324","泗洪县"),

        ;



        AddressCode320000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode320000Enum addressCodeEnum :AddressCode320000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 浙江省 枚举
     */
    public enum AddressCode330000Enum {

        ADDRESS_330102("330000","浙江省","330100","杭州市","330102","上城区"),
        ADDRESS_330105("330000","浙江省","330100","杭州市","330105","拱墅区"),
        ADDRESS_330106("330000","浙江省","330100","杭州市","330106","西湖区"),
        ADDRESS_330108("330000","浙江省","330100","杭州市","330108","滨江区"),
        ADDRESS_330109("330000","浙江省","330100","杭州市","330109","萧山区"),
        ADDRESS_330110("330000","浙江省","330100","杭州市","330110","余杭区"),
        ADDRESS_330111("330000","浙江省","330100","杭州市","330111","富阳区"),
        ADDRESS_330112("330000","浙江省","330100","杭州市","330112","临安区"),
        ADDRESS_330113("330000","浙江省","330100","杭州市","330113","临平区"),
        ADDRESS_330114("330000","浙江省","330100","杭州市","330114","钱塘区"),
        ADDRESS_330122("330000","浙江省","330100","杭州市","330122","桐庐县"),
        ADDRESS_330127("330000","浙江省","330100","杭州市","330127","淳安县"),
        ADDRESS_330182("330000","浙江省","330100","杭州市","330182","建德市"),
        ADDRESS_330203("330000","浙江省","330200","宁波市","330203","海曙区"),
        ADDRESS_330205("330000","浙江省","330200","宁波市","330205","江北区"),
        ADDRESS_330206("330000","浙江省","330200","宁波市","330206","北仑区"),
        ADDRESS_330211("330000","浙江省","330200","宁波市","330211","镇海区"),
        ADDRESS_330212("330000","浙江省","330200","宁波市","330212","鄞州区"),
        ADDRESS_330213("330000","浙江省","330200","宁波市","330213","奉化区"),
        ADDRESS_330225("330000","浙江省","330200","宁波市","330225","象山县"),
        ADDRESS_330226("330000","浙江省","330200","宁波市","330226","宁海县"),
        ADDRESS_330281("330000","浙江省","330200","宁波市","330281","余姚市"),
        ADDRESS_330282("330000","浙江省","330200","宁波市","330282","慈溪市"),
        ADDRESS_330302("330000","浙江省","330300","温州市","330302","鹿城区"),
        ADDRESS_330303("330000","浙江省","330300","温州市","330303","龙湾区"),
        ADDRESS_330304("330000","浙江省","330300","温州市","330304","瓯海区"),
        ADDRESS_330305("330000","浙江省","330300","温州市","330305","洞头区"),
        ADDRESS_330324("330000","浙江省","330300","温州市","330324","永嘉县"),
        ADDRESS_330326("330000","浙江省","330300","温州市","330326","平阳县"),
        ADDRESS_330327("330000","浙江省","330300","温州市","330327","苍南县"),
        ADDRESS_330328("330000","浙江省","330300","温州市","330328","文成县"),
        ADDRESS_330329("330000","浙江省","330300","温州市","330329","泰顺县"),
        ADDRESS_330381("330000","浙江省","330300","温州市","330381","瑞安市"),
        ADDRESS_330382("330000","浙江省","330300","温州市","330382","乐清市"),
        ADDRESS_330383("330000","浙江省","330300","温州市","330383","龙港市"),
        ADDRESS_330402("330000","浙江省","330400","嘉兴市","330402","南湖区"),
        ADDRESS_330411("330000","浙江省","330400","嘉兴市","330411","秀洲区"),
        ADDRESS_330421("330000","浙江省","330400","嘉兴市","330421","嘉善县"),
        ADDRESS_330424("330000","浙江省","330400","嘉兴市","330424","海盐县"),
        ADDRESS_330481("330000","浙江省","330400","嘉兴市","330481","海宁市"),
        ADDRESS_330482("330000","浙江省","330400","嘉兴市","330482","平湖市"),
        ADDRESS_330483("330000","浙江省","330400","嘉兴市","330483","桐乡市"),
        ADDRESS_330502("330000","浙江省","330500","湖州市","330502","吴兴区"),
        ADDRESS_330503("330000","浙江省","330500","湖州市","330503","南浔区"),
        ADDRESS_330521("330000","浙江省","330500","湖州市","330521","德清县"),
        ADDRESS_330522("330000","浙江省","330500","湖州市","330522","长兴县"),
        ADDRESS_330523("330000","浙江省","330500","湖州市","330523","安吉县"),
        ADDRESS_330602("330000","浙江省","330600","绍兴市","330602","越城区"),
        ADDRESS_330603("330000","浙江省","330600","绍兴市","330603","柯桥区"),
        ADDRESS_330604("330000","浙江省","330600","绍兴市","330604","上虞区"),
        ADDRESS_330624("330000","浙江省","330600","绍兴市","330624","新昌县"),
        ADDRESS_330681("330000","浙江省","330600","绍兴市","330681","诸暨市"),
        ADDRESS_330683("330000","浙江省","330600","绍兴市","330683","嵊州市"),
        ADDRESS_330702("330000","浙江省","330700","金华市","330702","婺城区"),
        ADDRESS_330703("330000","浙江省","330700","金华市","330703","金东区"),
        ADDRESS_330723("330000","浙江省","330700","金华市","330723","武义县"),
        ADDRESS_330726("330000","浙江省","330700","金华市","330726","浦江县"),
        ADDRESS_330727("330000","浙江省","330700","金华市","330727","磐安县"),
        ADDRESS_330781("330000","浙江省","330700","金华市","330781","兰溪市"),
        ADDRESS_330782("330000","浙江省","330700","金华市","330782","义乌市"),
        ADDRESS_330783("330000","浙江省","330700","金华市","330783","东阳市"),
        ADDRESS_330784("330000","浙江省","330700","金华市","330784","永康市"),
        ADDRESS_330802("330000","浙江省","330800","衢州市","330802","柯城区"),
        ADDRESS_330803("330000","浙江省","330800","衢州市","330803","衢江区"),
        ADDRESS_330822("330000","浙江省","330800","衢州市","330822","常山县"),
        ADDRESS_330824("330000","浙江省","330800","衢州市","330824","开化县"),
        ADDRESS_330825("330000","浙江省","330800","衢州市","330825","龙游县"),
        ADDRESS_330881("330000","浙江省","330800","衢州市","330881","江山市"),
        ADDRESS_330902("330000","浙江省","330900","舟山市","330902","定海区"),
        ADDRESS_330903("330000","浙江省","330900","舟山市","330903","普陀区"),
        ADDRESS_330921("330000","浙江省","330900","舟山市","330921","岱山县"),
        ADDRESS_330922("330000","浙江省","330900","舟山市","330922","嵊泗县"),
        ADDRESS_331002("330000","浙江省","331000","台州市","331002","椒江区"),
        ADDRESS_331003("330000","浙江省","331000","台州市","331003","黄岩区"),
        ADDRESS_331004("330000","浙江省","331000","台州市","331004","路桥区"),
        ADDRESS_331022("330000","浙江省","331000","台州市","331022","三门县"),
        ADDRESS_331023("330000","浙江省","331000","台州市","331023","天台县"),
        ADDRESS_331024("330000","浙江省","331000","台州市","331024","仙居县"),
        ADDRESS_331081("330000","浙江省","331000","台州市","331081","温岭市"),
        ADDRESS_331082("330000","浙江省","331000","台州市","331082","临海市"),
        ADDRESS_331083("330000","浙江省","331000","台州市","331083","玉环市"),
        ADDRESS_331102("330000","浙江省","331100","丽水市","331102","莲都区"),
        ADDRESS_331121("330000","浙江省","331100","丽水市","331121","青田县"),
        ADDRESS_331122("330000","浙江省","331100","丽水市","331122","缙云县"),
        ADDRESS_331123("330000","浙江省","331100","丽水市","331123","遂昌县"),
        ADDRESS_331124("330000","浙江省","331100","丽水市","331124","松阳县"),
        ADDRESS_331125("330000","浙江省","331100","丽水市","331125","云和县"),
        ADDRESS_331126("330000","浙江省","331100","丽水市","331126","庆元县"),
        ADDRESS_331127("330000","浙江省","331100","丽水市","331127","景宁畲族自治县"),
        ADDRESS_331181("330000","浙江省","331100","丽水市","331181","龙泉市"),

        ;



        AddressCode330000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode330000Enum addressCodeEnum :AddressCode330000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 安徽省枚举
     * 省编码 安徽省枚举
     */
    public enum AddressCode340000Enum {

        ADDRESS_340102("340000","安徽省","340100","合肥市","340102","瑶海区"),
        ADDRESS_340103("340000","安徽省","340100","合肥市","340103","庐阳区"),
        ADDRESS_340104("340000","安徽省","340100","合肥市","340104","蜀山区"),
        ADDRESS_340111("340000","安徽省","340100","合肥市","340111","包河区"),
        ADDRESS_340121("340000","安徽省","340100","合肥市","340121","长丰县"),
        ADDRESS_340122("340000","安徽省","340100","合肥市","340122","肥东县"),
        ADDRESS_340123("340000","安徽省","340100","合肥市","340123","肥西县"),
        ADDRESS_340124("340000","安徽省","340100","合肥市","340124","庐江县"),
        ADDRESS_340181("340000","安徽省","340100","合肥市","340181","巢湖市"),
        ADDRESS_340202("340000","安徽省","340200","芜湖市","340202","镜湖区"),
        ADDRESS_340207("340000","安徽省","340200","芜湖市","340207","鸠江区"),
        ADDRESS_340209("340000","安徽省","340200","芜湖市","340209","弋江区"),
        ADDRESS_340210("340000","安徽省","340200","芜湖市","340210","湾沚区"),
        ADDRESS_340212("340000","安徽省","340200","芜湖市","340212","繁昌区"),
        ADDRESS_340223("340000","安徽省","340200","芜湖市","340223","南陵县"),
        ADDRESS_340281("340000","安徽省","340200","芜湖市","340281","无为市"),
        ADDRESS_340302("340000","安徽省","340300","蚌埠市","340302","龙子湖区"),
        ADDRESS_340303("340000","安徽省","340300","蚌埠市","340303","蚌山区"),
        ADDRESS_340304("340000","安徽省","340300","蚌埠市","340304","禹会区"),
        ADDRESS_340311("340000","安徽省","340300","蚌埠市","340311","淮上区"),
        ADDRESS_340321("340000","安徽省","340300","蚌埠市","340321","怀远县"),
        ADDRESS_340322("340000","安徽省","340300","蚌埠市","340322","五河县"),
        ADDRESS_340323("340000","安徽省","340300","蚌埠市","340323","固镇县"),
        ADDRESS_340402("340000","安徽省","340400","淮南市","340402","大通区"),
        ADDRESS_340403("340000","安徽省","340400","淮南市","340403","田家庵区"),
        ADDRESS_340404("340000","安徽省","340400","淮南市","340404","谢家集区"),
        ADDRESS_340405("340000","安徽省","340400","淮南市","340405","八公山区"),
        ADDRESS_340406("340000","安徽省","340400","淮南市","340406","潘集区"),
        ADDRESS_340421("340000","安徽省","340400","淮南市","340421","凤台县"),
        ADDRESS_340422("340000","安徽省","340400","淮南市","340422","寿县"),
        ADDRESS_340503("340000","安徽省","340500","马鞍山市","340503","花山区"),
        ADDRESS_340504("340000","安徽省","340500","马鞍山市","340504","雨山区"),
        ADDRESS_340506("340000","安徽省","340500","马鞍山市","340506","博望区"),
        ADDRESS_340521("340000","安徽省","340500","马鞍山市","340521","当涂县"),
        ADDRESS_340522("340000","安徽省","340500","马鞍山市","340522","含山县"),
        ADDRESS_340523("340000","安徽省","340500","马鞍山市","340523","和县"),
        ADDRESS_340602("340000","安徽省","340600","淮北市","340602","杜集区"),
        ADDRESS_340603("340000","安徽省","340600","淮北市","340603","相山区"),
        ADDRESS_340604("340000","安徽省","340600","淮北市","340604","烈山区"),
        ADDRESS_340621("340000","安徽省","340600","淮北市","340621","濉溪县"),
        ADDRESS_340705("340000","安徽省","340700","铜陵市","340705","铜官区"),
        ADDRESS_340706("340000","安徽省","340700","铜陵市","340706","义安区"),
        ADDRESS_340711("340000","安徽省","340700","铜陵市","340711","郊区"),
        ADDRESS_340722("340000","安徽省","340700","铜陵市","340722","枞阳县"),
        ADDRESS_340802("340000","安徽省","340800","安庆市","340802","迎江区"),
        ADDRESS_340803("340000","安徽省","340800","安庆市","340803","大观区"),
        ADDRESS_340811("340000","安徽省","340800","安庆市","340811","宜秀区"),
        ADDRESS_340822("340000","安徽省","340800","安庆市","340822","怀宁县"),
        ADDRESS_340825("340000","安徽省","340800","安庆市","340825","太湖县"),
        ADDRESS_340826("340000","安徽省","340800","安庆市","340826","宿松县"),
        ADDRESS_340827("340000","安徽省","340800","安庆市","340827","望江县"),
        ADDRESS_340828("340000","安徽省","340800","安庆市","340828","岳西县"),
        ADDRESS_340881("340000","安徽省","340800","安庆市","340881","桐城市"),
        ADDRESS_340882("340000","安徽省","340800","安庆市","340882","潜山市"),
        ADDRESS_341002("340000","安徽省","341000","黄山市","341002","屯溪区"),
        ADDRESS_341003("340000","安徽省","341000","黄山市","341003","黄山区"),
        ADDRESS_341004("340000","安徽省","341000","黄山市","341004","徽州区"),
        ADDRESS_341021("340000","安徽省","341000","黄山市","341021","歙县"),
        ADDRESS_341022("340000","安徽省","341000","黄山市","341022","休宁县"),
        ADDRESS_341023("340000","安徽省","341000","黄山市","341023","黟县"),
        ADDRESS_341024("340000","安徽省","341000","黄山市","341024","祁门县"),
        ADDRESS_341102("340000","安徽省","341100","滁州市","341102","琅琊区"),
        ADDRESS_341103("340000","安徽省","341100","滁州市","341103","南谯区"),
        ADDRESS_341122("340000","安徽省","341100","滁州市","341122","来安县"),
        ADDRESS_341124("340000","安徽省","341100","滁州市","341124","全椒县"),
        ADDRESS_341125("340000","安徽省","341100","滁州市","341125","定远县"),
        ADDRESS_341126("340000","安徽省","341100","滁州市","341126","凤阳县"),
        ADDRESS_341181("340000","安徽省","341100","滁州市","341181","天长市"),
        ADDRESS_341182("340000","安徽省","341100","滁州市","341182","明光市"),
        ADDRESS_341202("340000","安徽省","341200","阜阳市","341202","颍州区"),
        ADDRESS_341203("340000","安徽省","341200","阜阳市","341203","颍东区"),
        ADDRESS_341204("340000","安徽省","341200","阜阳市","341204","颍泉区"),
        ADDRESS_341221("340000","安徽省","341200","阜阳市","341221","临泉县"),
        ADDRESS_341222("340000","安徽省","341200","阜阳市","341222","太和县"),
        ADDRESS_341225("340000","安徽省","341200","阜阳市","341225","阜南县"),
        ADDRESS_341226("340000","安徽省","341200","阜阳市","341226","颍上县"),
        ADDRESS_341282("340000","安徽省","341200","阜阳市","341282","界首市"),
        ADDRESS_341302("340000","安徽省","341300","宿州市","341302","埇桥区"),
        ADDRESS_341321("340000","安徽省","341300","宿州市","341321","砀山县"),
        ADDRESS_341322("340000","安徽省","341300","宿州市","341322","萧县"),
        ADDRESS_341323("340000","安徽省","341300","宿州市","341323","灵璧县"),
        ADDRESS_341324("340000","安徽省","341300","宿州市","341324","泗县"),
        ADDRESS_341502("340000","安徽省","341500","六安市","341502","金安区"),
        ADDRESS_341503("340000","安徽省","341500","六安市","341503","裕安区"),
        ADDRESS_341504("340000","安徽省","341500","六安市","341504","叶集区"),
        ADDRESS_341522("340000","安徽省","341500","六安市","341522","霍邱县"),
        ADDRESS_341523("340000","安徽省","341500","六安市","341523","舒城县"),
        ADDRESS_341524("340000","安徽省","341500","六安市","341524","金寨县"),
        ADDRESS_341525("340000","安徽省","341500","六安市","341525","霍山县"),
        ADDRESS_341602("340000","安徽省","341600","亳州市","341602","谯城区"),
        ADDRESS_341621("340000","安徽省","341600","亳州市","341621","涡阳县"),
        ADDRESS_341622("340000","安徽省","341600","亳州市","341622","蒙城县"),
        ADDRESS_341623("340000","安徽省","341600","亳州市","341623","利辛县"),
        ADDRESS_341702("340000","安徽省","341700","池州市","341702","贵池区"),
        ADDRESS_341721("340000","安徽省","341700","池州市","341721","东至县"),
        ADDRESS_341722("340000","安徽省","341700","池州市","341722","石台县"),
        ADDRESS_341723("340000","安徽省","341700","池州市","341723","青阳县"),
        ADDRESS_341802("340000","安徽省","341800","宣城市","341802","宣州区"),
        ADDRESS_341821("340000","安徽省","341800","宣城市","341821","郎溪县"),
        ADDRESS_341823("340000","安徽省","341800","宣城市","341823","泾县"),
        ADDRESS_341824("340000","安徽省","341800","宣城市","341824","绩溪县"),
        ADDRESS_341825("340000","安徽省","341800","宣城市","341825","旌德县"),
        ADDRESS_341881("340000","安徽省","341800","宣城市","341881","宁国市"),
        ADDRESS_341882("340000","安徽省","341800","宣城市","341882","广德市"),

        ;



        AddressCode340000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode340000Enum addressCodeEnum :AddressCode340000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 福建省 枚举
     */
    public enum AddressCode350000Enum {

        ADDRESS_350102("350000","福建省","350100","福州市","350102","鼓楼区"),
        ADDRESS_350103("350000","福建省","350100","福州市","350103","台江区"),
        ADDRESS_350104("350000","福建省","350100","福州市","350104","仓山区"),
        ADDRESS_350105("350000","福建省","350100","福州市","350105","马尾区"),
        ADDRESS_350111("350000","福建省","350100","福州市","350111","晋安区"),
        ADDRESS_350112("350000","福建省","350100","福州市","350112","长乐区"),
        ADDRESS_350121("350000","福建省","350100","福州市","350121","闽侯县"),
        ADDRESS_350122("350000","福建省","350100","福州市","350122","连江县"),
        ADDRESS_350123("350000","福建省","350100","福州市","350123","罗源县"),
        ADDRESS_350124("350000","福建省","350100","福州市","350124","闽清县"),
        ADDRESS_350125("350000","福建省","350100","福州市","350125","永泰县"),
        ADDRESS_350128("350000","福建省","350100","福州市","350128","平潭县"),
        ADDRESS_350181("350000","福建省","350100","福州市","350181","福清市"),
        ADDRESS_350203("350000","福建省","350200","厦门市","350203","思明区"),
        ADDRESS_350205("350000","福建省","350200","厦门市","350205","海沧区"),
        ADDRESS_350206("350000","福建省","350200","厦门市","350206","湖里区"),
        ADDRESS_350211("350000","福建省","350200","厦门市","350211","集美区"),
        ADDRESS_350212("350000","福建省","350200","厦门市","350212","同安区"),
        ADDRESS_350213("350000","福建省","350200","厦门市","350213","翔安区"),
        ADDRESS_350302("350000","福建省","350300","莆田市","350302","城厢区"),
        ADDRESS_350303("350000","福建省","350300","莆田市","350303","涵江区"),
        ADDRESS_350304("350000","福建省","350300","莆田市","350304","荔城区"),
        ADDRESS_350305("350000","福建省","350300","莆田市","350305","秀屿区"),
        ADDRESS_350322("350000","福建省","350300","莆田市","350322","仙游县"),
        ADDRESS_350404("350000","福建省","350400","三明市","350404","三元区"),
        ADDRESS_350405("350000","福建省","350400","三明市","350405","沙县区"),
        ADDRESS_350421("350000","福建省","350400","三明市","350421","明溪县"),
        ADDRESS_350423("350000","福建省","350400","三明市","350423","清流县"),
        ADDRESS_350424("350000","福建省","350400","三明市","350424","宁化县"),
        ADDRESS_350425("350000","福建省","350400","三明市","350425","大田县"),
        ADDRESS_350426("350000","福建省","350400","三明市","350426","尤溪县"),
        ADDRESS_350428("350000","福建省","350400","三明市","350428","将乐县"),
        ADDRESS_350429("350000","福建省","350400","三明市","350429","泰宁县"),
        ADDRESS_350430("350000","福建省","350400","三明市","350430","建宁县"),
        ADDRESS_350481("350000","福建省","350400","三明市","350481","永安市"),
        ADDRESS_350502("350000","福建省","350500","泉州市","350502","鲤城区"),
        ADDRESS_350503("350000","福建省","350500","泉州市","350503","丰泽区"),
        ADDRESS_350504("350000","福建省","350500","泉州市","350504","洛江区"),
        ADDRESS_350505("350000","福建省","350500","泉州市","350505","泉港区"),
        ADDRESS_350521("350000","福建省","350500","泉州市","350521","惠安县"),
        ADDRESS_350524("350000","福建省","350500","泉州市","350524","安溪县"),
        ADDRESS_350525("350000","福建省","350500","泉州市","350525","永春县"),
        ADDRESS_350526("350000","福建省","350500","泉州市","350526","德化县"),
        ADDRESS_350527("350000","福建省","350500","泉州市","350527","金门县"),
        ADDRESS_350581("350000","福建省","350500","泉州市","350581","石狮市"),
        ADDRESS_350582("350000","福建省","350500","泉州市","350582","晋江市"),
        ADDRESS_350583("350000","福建省","350500","泉州市","350583","南安市"),
        ADDRESS_350602("350000","福建省","350600","漳州市","350602","芗城区"),
        ADDRESS_350603("350000","福建省","350600","漳州市","350603","龙文区"),
        ADDRESS_350604("350000","福建省","350600","漳州市","350604","龙海区"),
        ADDRESS_350605("350000","福建省","350600","漳州市","350605","长泰区"),
        ADDRESS_350622("350000","福建省","350600","漳州市","350622","云霄县"),
        ADDRESS_350623("350000","福建省","350600","漳州市","350623","漳浦县"),
        ADDRESS_350624("350000","福建省","350600","漳州市","350624","诏安县"),
        ADDRESS_350626("350000","福建省","350600","漳州市","350626","东山县"),
        ADDRESS_350627("350000","福建省","350600","漳州市","350627","南靖县"),
        ADDRESS_350628("350000","福建省","350600","漳州市","350628","平和县"),
        ADDRESS_350629("350000","福建省","350600","漳州市","350629","华安县"),
        ADDRESS_350702("350000","福建省","350700","南平市","350702","延平区"),
        ADDRESS_350703("350000","福建省","350700","南平市","350703","建阳区"),
        ADDRESS_350721("350000","福建省","350700","南平市","350721","顺昌县"),
        ADDRESS_350722("350000","福建省","350700","南平市","350722","浦城县"),
        ADDRESS_350723("350000","福建省","350700","南平市","350723","光泽县"),
        ADDRESS_350724("350000","福建省","350700","南平市","350724","松溪县"),
        ADDRESS_350725("350000","福建省","350700","南平市","350725","政和县"),
        ADDRESS_350781("350000","福建省","350700","南平市","350781","邵武市"),
        ADDRESS_350782("350000","福建省","350700","南平市","350782","武夷山市"),
        ADDRESS_350783("350000","福建省","350700","南平市","350783","建瓯市"),
        ADDRESS_350802("350000","福建省","350800","龙岩市","350802","新罗区"),
        ADDRESS_350803("350000","福建省","350800","龙岩市","350803","永定区"),
        ADDRESS_350821("350000","福建省","350800","龙岩市","350821","长汀县"),
        ADDRESS_350823("350000","福建省","350800","龙岩市","350823","上杭县"),
        ADDRESS_350824("350000","福建省","350800","龙岩市","350824","武平县"),
        ADDRESS_350825("350000","福建省","350800","龙岩市","350825","连城县"),
        ADDRESS_350881("350000","福建省","350800","龙岩市","350881","漳平市"),
        ADDRESS_350902("350000","福建省","350900","宁德市","350902","蕉城区"),
        ADDRESS_350921("350000","福建省","350900","宁德市","350921","霞浦县"),
        ADDRESS_350922("350000","福建省","350900","宁德市","350922","古田县"),
        ADDRESS_350923("350000","福建省","350900","宁德市","350923","屏南县"),
        ADDRESS_350924("350000","福建省","350900","宁德市","350924","寿宁县"),
        ADDRESS_350925("350000","福建省","350900","宁德市","350925","周宁县"),
        ADDRESS_350926("350000","福建省","350900","宁德市","350926","柘荣县"),
        ADDRESS_350981("350000","福建省","350900","宁德市","350981","福安市"),
        ADDRESS_350982("350000","福建省","350900","宁德市","350982","福鼎市"),

        ;



        AddressCode350000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode350000Enum addressCodeEnum :AddressCode350000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 江西省 枚举
     */
    public enum AddressCode360000Enum {

        ADDRESS_360102("360000","江西省","360100","南昌市","360102","东湖区"),
        ADDRESS_360103("360000","江西省","360100","南昌市","360103","西湖区"),
        ADDRESS_360104("360000","江西省","360100","南昌市","360104","青云谱区"),
        ADDRESS_360111("360000","江西省","360100","南昌市","360111","青山湖区"),
        ADDRESS_360112("360000","江西省","360100","南昌市","360112","新建区"),
        ADDRESS_360113("360000","江西省","360100","南昌市","360113","红谷滩区"),
        ADDRESS_360121("360000","江西省","360100","南昌市","360121","南昌县"),
        ADDRESS_360123("360000","江西省","360100","南昌市","360123","安义县"),
        ADDRESS_360124("360000","江西省","360100","南昌市","360124","进贤县"),
        ADDRESS_360202("360000","江西省","360200","景德镇市","360202","昌江区"),
        ADDRESS_360203("360000","江西省","360200","景德镇市","360203","珠山区"),
        ADDRESS_360222("360000","江西省","360200","景德镇市","360222","浮梁县"),
        ADDRESS_360281("360000","江西省","360200","景德镇市","360281","乐平市"),
        ADDRESS_360302("360000","江西省","360300","萍乡市","360302","安源区"),
        ADDRESS_360313("360000","江西省","360300","萍乡市","360313","湘东区"),
        ADDRESS_360321("360000","江西省","360300","萍乡市","360321","莲花县"),
        ADDRESS_360322("360000","江西省","360300","萍乡市","360322","上栗县"),
        ADDRESS_360323("360000","江西省","360300","萍乡市","360323","芦溪县"),
        ADDRESS_360402("360000","江西省","360400","九江市","360402","濂溪区"),
        ADDRESS_360403("360000","江西省","360400","九江市","360403","浔阳区"),
        ADDRESS_360404("360000","江西省","360400","九江市","360404","柴桑区"),
        ADDRESS_360423("360000","江西省","360400","九江市","360423","武宁县"),
        ADDRESS_360424("360000","江西省","360400","九江市","360424","修水县"),
        ADDRESS_360425("360000","江西省","360400","九江市","360425","永修县"),
        ADDRESS_360426("360000","江西省","360400","九江市","360426","德安县"),
        ADDRESS_360428("360000","江西省","360400","九江市","360428","都昌县"),
        ADDRESS_360429("360000","江西省","360400","九江市","360429","湖口县"),
        ADDRESS_360430("360000","江西省","360400","九江市","360430","彭泽县"),
        ADDRESS_360481("360000","江西省","360400","九江市","360481","瑞昌市"),
        ADDRESS_360482("360000","江西省","360400","九江市","360482","共青城市"),
        ADDRESS_360483("360000","江西省","360400","九江市","360483","庐山市"),
        ADDRESS_360502("360000","江西省","360500","新余市","360502","渝水区"),
        ADDRESS_360521("360000","江西省","360500","新余市","360521","分宜县"),
        ADDRESS_360602("360000","江西省","360600","鹰潭市","360602","月湖区"),
        ADDRESS_360603("360000","江西省","360600","鹰潭市","360603","余江区"),
        ADDRESS_360681("360000","江西省","360600","鹰潭市","360681","贵溪市"),
        ADDRESS_360702("360000","江西省","360700","赣州市","360702","章贡区"),
        ADDRESS_360703("360000","江西省","360700","赣州市","360703","南康区"),
        ADDRESS_360704("360000","江西省","360700","赣州市","360704","赣县区"),
        ADDRESS_360722("360000","江西省","360700","赣州市","360722","信丰县"),
        ADDRESS_360723("360000","江西省","360700","赣州市","360723","大余县"),
        ADDRESS_360724("360000","江西省","360700","赣州市","360724","上犹县"),
        ADDRESS_360725("360000","江西省","360700","赣州市","360725","崇义县"),
        ADDRESS_360726("360000","江西省","360700","赣州市","360726","安远县"),
        ADDRESS_360728("360000","江西省","360700","赣州市","360728","定南县"),
        ADDRESS_360729("360000","江西省","360700","赣州市","360729","全南县"),
        ADDRESS_360730("360000","江西省","360700","赣州市","360730","宁都县"),
        ADDRESS_360731("360000","江西省","360700","赣州市","360731","于都县"),
        ADDRESS_360732("360000","江西省","360700","赣州市","360732","兴国县"),
        ADDRESS_360733("360000","江西省","360700","赣州市","360733","会昌县"),
        ADDRESS_360734("360000","江西省","360700","赣州市","360734","寻乌县"),
        ADDRESS_360735("360000","江西省","360700","赣州市","360735","石城县"),
        ADDRESS_360781("360000","江西省","360700","赣州市","360781","瑞金市"),
        ADDRESS_360783("360000","江西省","360700","赣州市","360783","龙南市"),
        ADDRESS_360802("360000","江西省","360800","吉安市","360802","吉州区"),
        ADDRESS_360803("360000","江西省","360800","吉安市","360803","青原区"),
        ADDRESS_360821("360000","江西省","360800","吉安市","360821","吉安县"),
        ADDRESS_360822("360000","江西省","360800","吉安市","360822","吉水县"),
        ADDRESS_360823("360000","江西省","360800","吉安市","360823","峡江县"),
        ADDRESS_360824("360000","江西省","360800","吉安市","360824","新干县"),
        ADDRESS_360825("360000","江西省","360800","吉安市","360825","永丰县"),
        ADDRESS_360826("360000","江西省","360800","吉安市","360826","泰和县"),
        ADDRESS_360827("360000","江西省","360800","吉安市","360827","遂川县"),
        ADDRESS_360828("360000","江西省","360800","吉安市","360828","万安县"),
        ADDRESS_360829("360000","江西省","360800","吉安市","360829","安福县"),
        ADDRESS_360830("360000","江西省","360800","吉安市","360830","永新县"),
        ADDRESS_360881("360000","江西省","360800","吉安市","360881","井冈山市"),
        ADDRESS_360902("360000","江西省","360900","宜春市","360902","袁州区"),
        ADDRESS_360921("360000","江西省","360900","宜春市","360921","奉新县"),
        ADDRESS_360922("360000","江西省","360900","宜春市","360922","万载县"),
        ADDRESS_360923("360000","江西省","360900","宜春市","360923","上高县"),
        ADDRESS_360924("360000","江西省","360900","宜春市","360924","宜丰县"),
        ADDRESS_360925("360000","江西省","360900","宜春市","360925","靖安县"),
        ADDRESS_360926("360000","江西省","360900","宜春市","360926","铜鼓县"),
        ADDRESS_360981("360000","江西省","360900","宜春市","360981","丰城市"),
        ADDRESS_360982("360000","江西省","360900","宜春市","360982","樟树市"),
        ADDRESS_360983("360000","江西省","360900","宜春市","360983","高安市"),
        ADDRESS_361002("360000","江西省","361000","抚州市","361002","临川区"),
        ADDRESS_361003("360000","江西省","361000","抚州市","361003","东乡区"),
        ADDRESS_361021("360000","江西省","361000","抚州市","361021","南城县"),
        ADDRESS_361022("360000","江西省","361000","抚州市","361022","黎川县"),
        ADDRESS_361023("360000","江西省","361000","抚州市","361023","南丰县"),
        ADDRESS_361024("360000","江西省","361000","抚州市","361024","崇仁县"),
        ADDRESS_361025("360000","江西省","361000","抚州市","361025","乐安县"),
        ADDRESS_361026("360000","江西省","361000","抚州市","361026","宜黄县"),
        ADDRESS_361027("360000","江西省","361000","抚州市","361027","金溪县"),
        ADDRESS_361028("360000","江西省","361000","抚州市","361028","资溪县"),
        ADDRESS_361030("360000","江西省","361000","抚州市","361030","广昌县"),
        ADDRESS_361102("360000","江西省","361100","上饶市","361102","信州区"),
        ADDRESS_361103("360000","江西省","361100","上饶市","361103","广丰区"),
        ADDRESS_361104("360000","江西省","361100","上饶市","361104","广信区"),
        ADDRESS_361123("360000","江西省","361100","上饶市","361123","玉山县"),
        ADDRESS_361124("360000","江西省","361100","上饶市","361124","铅山县"),
        ADDRESS_361125("360000","江西省","361100","上饶市","361125","横峰县"),
        ADDRESS_361126("360000","江西省","361100","上饶市","361126","弋阳县"),
        ADDRESS_361127("360000","江西省","361100","上饶市","361127","余干县"),
        ADDRESS_361128("360000","江西省","361100","上饶市","361128","鄱阳县"),
        ADDRESS_361129("360000","江西省","361100","上饶市","361129","万年县"),
        ADDRESS_361130("360000","江西省","361100","上饶市","361130","婺源县"),
        ADDRESS_361181("360000","江西省","361100","上饶市","361181","德兴市"),

        ;



        AddressCode360000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode360000Enum addressCodeEnum :AddressCode360000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }



    /**
     * 省编码 山东枚举
     */
    public enum AddressCode370000Enum {

        ADDRESS_370102("370000","山东省","370100","济南市","370102","历下区"),
        ADDRESS_370103("370000","山东省","370100","济南市","370103","市中区"),
        ADDRESS_370104("370000","山东省","370100","济南市","370104","槐荫区"),
        ADDRESS_370105("370000","山东省","370100","济南市","370105","天桥区"),
        ADDRESS_370112("370000","山东省","370100","济南市","370112","历城区"),
        ADDRESS_370113("370000","山东省","370100","济南市","370113","长清区"),
        ADDRESS_370114("370000","山东省","370100","济南市","370114","章丘区"),
        ADDRESS_370115("370000","山东省","370100","济南市","370115","济阳区"),
        ADDRESS_370116("370000","山东省","370100","济南市","370116","莱芜区"),
        ADDRESS_370117("370000","山东省","370100","济南市","370117","钢城区"),
        ADDRESS_370124("370000","山东省","370100","济南市","370124","平阴县"),
        ADDRESS_370126("370000","山东省","370100","济南市","370126","商河县"),
        ADDRESS_370202("370000","山东省","370200","青岛市","370202","市南区"),
        ADDRESS_370203("370000","山东省","370200","青岛市","370203","市北区"),
        ADDRESS_370211("370000","山东省","370200","青岛市","370211","黄岛区"),
        ADDRESS_370212("370000","山东省","370200","青岛市","370212","崂山区"),
        ADDRESS_370213("370000","山东省","370200","青岛市","370213","李沧区"),
        ADDRESS_370214("370000","山东省","370200","青岛市","370214","城阳区"),
        ADDRESS_370215("370000","山东省","370200","青岛市","370215","即墨区"),
        ADDRESS_370281("370000","山东省","370200","青岛市","370281","胶州市"),
        ADDRESS_370283("370000","山东省","370200","青岛市","370283","平度市"),
        ADDRESS_370285("370000","山东省","370200","青岛市","370285","莱西市"),
        ADDRESS_370302("370000","山东省","370300","淄博市","370302","淄川区"),
        ADDRESS_370303("370000","山东省","370300","淄博市","370303","张店区"),
        ADDRESS_370304("370000","山东省","370300","淄博市","370304","博山区"),
        ADDRESS_370305("370000","山东省","370300","淄博市","370305","临淄区"),
        ADDRESS_370306("370000","山东省","370300","淄博市","370306","周村区"),
        ADDRESS_370321("370000","山东省","370300","淄博市","370321","桓台县"),
        ADDRESS_370322("370000","山东省","370300","淄博市","370322","高青县"),
        ADDRESS_370323("370000","山东省","370300","淄博市","370323","沂源县"),
        ADDRESS_370402("370000","山东省","370400","枣庄市","370402","市中区"),
        ADDRESS_370403("370000","山东省","370400","枣庄市","370403","薛城区"),
        ADDRESS_370404("370000","山东省","370400","枣庄市","370404","峄城区"),
        ADDRESS_370405("370000","山东省","370400","枣庄市","370405","台儿庄区"),
        ADDRESS_370406("370000","山东省","370400","枣庄市","370406","山亭区"),
        ADDRESS_370481("370000","山东省","370400","枣庄市","370481","滕州市"),
        ADDRESS_370502("370000","山东省","370500","东营市","370502","东营区"),
        ADDRESS_370503("370000","山东省","370500","东营市","370503","河口区"),
        ADDRESS_370505("370000","山东省","370500","东营市","370505","垦利区"),
        ADDRESS_370522("370000","山东省","370500","东营市","370522","利津县"),
        ADDRESS_370523("370000","山东省","370500","东营市","370523","广饶县"),
        ADDRESS_370602("370000","山东省","370600","烟台市","370602","芝罘区"),
        ADDRESS_370611("370000","山东省","370600","烟台市","370611","福山区"),
        ADDRESS_370612("370000","山东省","370600","烟台市","370612","牟平区"),
        ADDRESS_370613("370000","山东省","370600","烟台市","370613","莱山区"),
        ADDRESS_370614("370000","山东省","370600","烟台市","370614","蓬莱区"),
        ADDRESS_370681("370000","山东省","370600","烟台市","370681","龙口市"),
        ADDRESS_370682("370000","山东省","370600","烟台市","370682","莱阳市"),
        ADDRESS_370683("370000","山东省","370600","烟台市","370683","莱州市"),
        ADDRESS_370685("370000","山东省","370600","烟台市","370685","招远市"),
        ADDRESS_370686("370000","山东省","370600","烟台市","370686","栖霞市"),
        ADDRESS_370687("370000","山东省","370600","烟台市","370687","海阳市"),
        ADDRESS_370702("370000","山东省","370700","潍坊市","370702","潍城区"),
        ADDRESS_370703("370000","山东省","370700","潍坊市","370703","寒亭区"),
        ADDRESS_370704("370000","山东省","370700","潍坊市","370704","坊子区"),
        ADDRESS_370705("370000","山东省","370700","潍坊市","370705","奎文区"),
        ADDRESS_370724("370000","山东省","370700","潍坊市","370724","临朐县"),
        ADDRESS_370725("370000","山东省","370700","潍坊市","370725","昌乐县"),
        ADDRESS_370781("370000","山东省","370700","潍坊市","370781","青州市"),
        ADDRESS_370782("370000","山东省","370700","潍坊市","370782","诸城市"),
        ADDRESS_370783("370000","山东省","370700","潍坊市","370783","寿光市"),
        ADDRESS_370784("370000","山东省","370700","潍坊市","370784","安丘市"),
        ADDRESS_370785("370000","山东省","370700","潍坊市","370785","高密市"),
        ADDRESS_370786("370000","山东省","370700","潍坊市","370786","昌邑市"),
        ADDRESS_370811("370000","山东省","370800","济宁市","370811","任城区"),
        ADDRESS_370812("370000","山东省","370800","济宁市","370812","兖州区"),
        ADDRESS_370826("370000","山东省","370800","济宁市","370826","微山县"),
        ADDRESS_370827("370000","山东省","370800","济宁市","370827","鱼台县"),
        ADDRESS_370828("370000","山东省","370800","济宁市","370828","金乡县"),
        ADDRESS_370829("370000","山东省","370800","济宁市","370829","嘉祥县"),
        ADDRESS_370830("370000","山东省","370800","济宁市","370830","汶上县"),
        ADDRESS_370831("370000","山东省","370800","济宁市","370831","泗水县"),
        ADDRESS_370832("370000","山东省","370800","济宁市","370832","梁山县"),
        ADDRESS_370881("370000","山东省","370800","济宁市","370881","曲阜市"),
        ADDRESS_370883("370000","山东省","370800","济宁市","370883","邹城市"),
        ADDRESS_370902("370000","山东省","370900","泰安市","370902","泰山区"),
        ADDRESS_370911("370000","山东省","370900","泰安市","370911","岱岳区"),
        ADDRESS_370921("370000","山东省","370900","泰安市","370921","宁阳县"),
        ADDRESS_370923("370000","山东省","370900","泰安市","370923","东平县"),
        ADDRESS_370982("370000","山东省","370900","泰安市","370982","新泰市"),
        ADDRESS_370983("370000","山东省","370900","泰安市","370983","肥城市"),
        ADDRESS_371002("370000","山东省","371000","威海市","371002","环翠区"),
        ADDRESS_371003("370000","山东省","371000","威海市","371003","文登区"),
        ADDRESS_371082("370000","山东省","371000","威海市","371082","荣成市"),
        ADDRESS_371083("370000","山东省","371000","威海市","371083","乳山市"),
        ADDRESS_371102("370000","山东省","371100","日照市","371102","东港区"),
        ADDRESS_371103("370000","山东省","371100","日照市","371103","岚山区"),
        ADDRESS_371121("370000","山东省","371100","日照市","371121","五莲县"),
        ADDRESS_371122("370000","山东省","371100","日照市","371122","莒县"),
        ADDRESS_371302("370000","山东省","371300","临沂市","371302","兰山区"),
        ADDRESS_371311("370000","山东省","371300","临沂市","371311","罗庄区"),
        ADDRESS_371312("370000","山东省","371300","临沂市","371312","河东区"),
        ADDRESS_371321("370000","山东省","371300","临沂市","371321","沂南县"),
        ADDRESS_371322("370000","山东省","371300","临沂市","371322","郯城县"),
        ADDRESS_371323("370000","山东省","371300","临沂市","371323","沂水县"),
        ADDRESS_371324("370000","山东省","371300","临沂市","371324","兰陵县"),
        ADDRESS_371325("370000","山东省","371300","临沂市","371325","费县"),
        ADDRESS_371326("370000","山东省","371300","临沂市","371326","平邑县"),
        ADDRESS_371327("370000","山东省","371300","临沂市","371327","莒南县"),
        ADDRESS_371328("370000","山东省","371300","临沂市","371328","蒙阴县"),
        ADDRESS_371329("370000","山东省","371300","临沂市","371329","临沭县"),
        ADDRESS_371402("370000","山东省","371400","德州市","371402","德城区"),
        ADDRESS_371403("370000","山东省","371400","德州市","371403","陵城区"),
        ADDRESS_371422("370000","山东省","371400","德州市","371422","宁津县"),
        ADDRESS_371423("370000","山东省","371400","德州市","371423","庆云县"),
        ADDRESS_371424("370000","山东省","371400","德州市","371424","临邑县"),
        ADDRESS_371425("370000","山东省","371400","德州市","371425","齐河县"),
        ADDRESS_371426("370000","山东省","371400","德州市","371426","平原县"),
        ADDRESS_371427("370000","山东省","371400","德州市","371427","夏津县"),
        ADDRESS_371428("370000","山东省","371400","德州市","371428","武城县"),
        ADDRESS_371481("370000","山东省","371400","德州市","371481","乐陵市"),
        ADDRESS_371482("370000","山东省","371400","德州市","371482","禹城市"),
        ADDRESS_371502("370000","山东省","371500","聊城市","371502","东昌府区"),
        ADDRESS_371503("370000","山东省","371500","聊城市","371503","茌平区"),
        ADDRESS_371521("370000","山东省","371500","聊城市","371521","阳谷县"),
        ADDRESS_371522("370000","山东省","371500","聊城市","371522","莘县"),
        ADDRESS_371524("370000","山东省","371500","聊城市","371524","东阿县"),
        ADDRESS_371525("370000","山东省","371500","聊城市","371525","冠县"),
        ADDRESS_371526("370000","山东省","371500","聊城市","371526","高唐县"),
        ADDRESS_371581("370000","山东省","371500","聊城市","371581","临清市"),
        ADDRESS_371602("370000","山东省","371600","滨州市","371602","滨城区"),
        ADDRESS_371603("370000","山东省","371600","滨州市","371603","沾化区"),
        ADDRESS_371621("370000","山东省","371600","滨州市","371621","惠民县"),
        ADDRESS_371622("370000","山东省","371600","滨州市","371622","阳信县"),
        ADDRESS_371623("370000","山东省","371600","滨州市","371623","无棣县"),
        ADDRESS_371625("370000","山东省","371600","滨州市","371625","博兴县"),
        ADDRESS_371681("370000","山东省","371600","滨州市","371681","邹平市"),
        ADDRESS_371702("370000","山东省","371700","菏泽市","371702","牡丹区"),
        ADDRESS_371703("370000","山东省","371700","菏泽市","371703","定陶区"),
        ADDRESS_371721("370000","山东省","371700","菏泽市","371721","曹县"),
        ADDRESS_371722("370000","山东省","371700","菏泽市","371722","单县"),
        ADDRESS_371723("370000","山东省","371700","菏泽市","371723","成武县"),
        ADDRESS_371724("370000","山东省","371700","菏泽市","371724","巨野县"),
        ADDRESS_371725("370000","山东省","371700","菏泽市","371725","郓城县"),
        ADDRESS_371726("370000","山东省","371700","菏泽市","371726","鄄城县"),
        ADDRESS_371728("370000","山东省","371700","菏泽市","371728","东明县"),

        ;



        AddressCode370000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode370000Enum addressCodeEnum :AddressCode370000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }



    /**
     * 省编码 河南枚举
     */
    public enum AddressCode410000Enum {

        ADDRESS_410102("410000","河南省","410100","郑州市","410102","中原区"),
        ADDRESS_410103("410000","河南省","410100","郑州市","410103","二七区"),
        ADDRESS_410104("410000","河南省","410100","郑州市","410104","管城回族区"),
        ADDRESS_410105("410000","河南省","410100","郑州市","410105","金水区"),
        ADDRESS_410106("410000","河南省","410100","郑州市","410106","上街区"),
        ADDRESS_410108("410000","河南省","410100","郑州市","410108","惠济区"),
        ADDRESS_410122("410000","河南省","410100","郑州市","410122","中牟县"),
        ADDRESS_410181("410000","河南省","410100","郑州市","410181","巩义市"),
        ADDRESS_410182("410000","河南省","410100","郑州市","410182","荥阳市"),
        ADDRESS_410183("410000","河南省","410100","郑州市","410183","新密市"),
        ADDRESS_410184("410000","河南省","410100","郑州市","410184","新郑市"),
        ADDRESS_410185("410000","河南省","410100","郑州市","410185","登封市"),
        ADDRESS_410202("410000","河南省","410200","开封市","410202","龙亭区"),
        ADDRESS_410203("410000","河南省","410200","开封市","410203","顺河回族区"),
        ADDRESS_410204("410000","河南省","410200","开封市","410204","鼓楼区"),
        ADDRESS_410205("410000","河南省","410200","开封市","410205","禹王台区"),
        ADDRESS_410212("410000","河南省","410200","开封市","410212","祥符区"),
        ADDRESS_410221("410000","河南省","410200","开封市","410221","杞县"),
        ADDRESS_410222("410000","河南省","410200","开封市","410222","通许县"),
        ADDRESS_410223("410000","河南省","410200","开封市","410223","尉氏县"),
        ADDRESS_410225("410000","河南省","410200","开封市","410225","兰考县"),
        ADDRESS_410302("410000","河南省","410300","洛阳市","410302","老城区"),
        ADDRESS_410303("410000","河南省","410300","洛阳市","410303","西工区"),
        ADDRESS_410304("410000","河南省","410300","洛阳市","410304","瀍河回族区"),
        ADDRESS_410305("410000","河南省","410300","洛阳市","410305","涧西区"),
        ADDRESS_410306("410000","河南省","410300","洛阳市","410306","吉利区"),
        ADDRESS_410311("410000","河南省","410300","洛阳市","410311","洛龙区"),
        ADDRESS_410322("410000","河南省","410300","洛阳市","410322","孟津县"),
        ADDRESS_410323("410000","河南省","410300","洛阳市","410323","新安县"),
        ADDRESS_410324("410000","河南省","410300","洛阳市","410324","栾川县"),
        ADDRESS_410325("410000","河南省","410300","洛阳市","410325","嵩县"),
        ADDRESS_410326("410000","河南省","410300","洛阳市","410326","汝阳县"),
        ADDRESS_410327("410000","河南省","410300","洛阳市","410327","宜阳县"),
        ADDRESS_410328("410000","河南省","410300","洛阳市","410328","洛宁县"),
        ADDRESS_410329("410000","河南省","410300","洛阳市","410329","伊川县"),
        ADDRESS_410381("410000","河南省","410300","洛阳市","410381","偃师市"),
        ADDRESS_410402("410000","河南省","410400","平顶山市","410402","新华区"),
        ADDRESS_410403("410000","河南省","410400","平顶山市","410403","卫东区"),
        ADDRESS_410404("410000","河南省","410400","平顶山市","410404","石龙区"),
        ADDRESS_410411("410000","河南省","410400","平顶山市","410411","湛河区"),
        ADDRESS_410421("410000","河南省","410400","平顶山市","410421","宝丰县"),
        ADDRESS_410422("410000","河南省","410400","平顶山市","410422","叶县"),
        ADDRESS_410423("410000","河南省","410400","平顶山市","410423","鲁山县"),
        ADDRESS_410425("410000","河南省","410400","平顶山市","410425","郏县"),
        ADDRESS_410481("410000","河南省","410400","平顶山市","410481","舞钢市"),
        ADDRESS_410482("410000","河南省","410400","平顶山市","410482","汝州市"),
        ADDRESS_410502("410000","河南省","410500","安阳市","410502","文峰区"),
        ADDRESS_410503("410000","河南省","410500","安阳市","410503","北关区"),
        ADDRESS_410505("410000","河南省","410500","安阳市","410505","殷都区"),
        ADDRESS_410506("410000","河南省","410500","安阳市","410506","龙安区"),
        ADDRESS_410522("410000","河南省","410500","安阳市","410522","安阳县"),
        ADDRESS_410523("410000","河南省","410500","安阳市","410523","汤阴县"),
        ADDRESS_410526("410000","河南省","410500","安阳市","410526","滑县"),
        ADDRESS_410527("410000","河南省","410500","安阳市","410527","内黄县"),
        ADDRESS_410581("410000","河南省","410500","安阳市","410581","林州市"),
        ADDRESS_410602("410000","河南省","410600","鹤壁市","410602","鹤山区"),
        ADDRESS_410603("410000","河南省","410600","鹤壁市","410603","山城区"),
        ADDRESS_410611("410000","河南省","410600","鹤壁市","410611","淇滨区"),
        ADDRESS_410621("410000","河南省","410600","鹤壁市","410621","浚县"),
        ADDRESS_410622("410000","河南省","410600","鹤壁市","410622","淇县"),
        ADDRESS_410702("410000","河南省","410700","新乡市","410702","红旗区"),
        ADDRESS_410703("410000","河南省","410700","新乡市","410703","卫滨区"),
        ADDRESS_410704("410000","河南省","410700","新乡市","410704","凤泉区"),
        ADDRESS_410711("410000","河南省","410700","新乡市","410711","牧野区"),
        ADDRESS_410721("410000","河南省","410700","新乡市","410721","新乡县"),
        ADDRESS_410724("410000","河南省","410700","新乡市","410724","获嘉县"),
        ADDRESS_410725("410000","河南省","410700","新乡市","410725","原阳县"),
        ADDRESS_410726("410000","河南省","410700","新乡市","410726","延津县"),
        ADDRESS_410727("410000","河南省","410700","新乡市","410727","封丘县"),
        ADDRESS_410781("410000","河南省","410700","新乡市","410781","卫辉市"),
        ADDRESS_410782("410000","河南省","410700","新乡市","410782","辉县市"),
        ADDRESS_410783("410000","河南省","410700","新乡市","410783","长垣市"),
        ADDRESS_410802("410000","河南省","410800","焦作市","410802","解放区"),
        ADDRESS_410803("410000","河南省","410800","焦作市","410803","中站区"),
        ADDRESS_410804("410000","河南省","410800","焦作市","410804","马村区"),
        ADDRESS_410811("410000","河南省","410800","焦作市","410811","山阳区"),
        ADDRESS_410821("410000","河南省","410800","焦作市","410821","修武县"),
        ADDRESS_410822("410000","河南省","410800","焦作市","410822","博爱县"),
        ADDRESS_410823("410000","河南省","410800","焦作市","410823","武陟县"),
        ADDRESS_410825("410000","河南省","410800","焦作市","410825","温县"),
        ADDRESS_410882("410000","河南省","410800","焦作市","410882","沁阳市"),
        ADDRESS_410883("410000","河南省","410800","焦作市","410883","孟州市"),
        ADDRESS_410902("410000","河南省","410900","濮阳市","410902","华龙区"),
        ADDRESS_410922("410000","河南省","410900","濮阳市","410922","清丰县"),
        ADDRESS_410923("410000","河南省","410900","濮阳市","410923","南乐县"),
        ADDRESS_410926("410000","河南省","410900","濮阳市","410926","范县"),
        ADDRESS_410927("410000","河南省","410900","濮阳市","410927","台前县"),
        ADDRESS_410928("410000","河南省","410900","濮阳市","410928","濮阳县"),
        ADDRESS_411002("410000","河南省","411000","许昌市","411002","魏都区"),
        ADDRESS_411003("410000","河南省","411000","许昌市","411003","建安区"),
        ADDRESS_411024("410000","河南省","411000","许昌市","411024","鄢陵县"),
        ADDRESS_411025("410000","河南省","411000","许昌市","411025","襄城县"),
        ADDRESS_411081("410000","河南省","411000","许昌市","411081","禹州市"),
        ADDRESS_411082("410000","河南省","411000","许昌市","411082","长葛市"),
        ADDRESS_411102("410000","河南省","411100","漯河市","411102","源汇区"),
        ADDRESS_411103("410000","河南省","411100","漯河市","411103","郾城区"),
        ADDRESS_411104("410000","河南省","411100","漯河市","411104","召陵区"),
        ADDRESS_411121("410000","河南省","411100","漯河市","411121","舞阳县"),
        ADDRESS_411122("410000","河南省","411100","漯河市","411122","临颍县"),
        ADDRESS_411202("410000","河南省","411200","三门峡市","411202","湖滨区"),
        ADDRESS_411203("410000","河南省","411200","三门峡市","411203","陕州区"),
        ADDRESS_411221("410000","河南省","411200","三门峡市","411221","渑池县"),
        ADDRESS_411224("410000","河南省","411200","三门峡市","411224","卢氏县"),
        ADDRESS_411281("410000","河南省","411200","三门峡市","411281","义马市"),
        ADDRESS_411282("410000","河南省","411200","三门峡市","411282","灵宝市"),
        ADDRESS_411302("410000","河南省","411300","南阳市","411302","宛城区"),
        ADDRESS_411303("410000","河南省","411300","南阳市","411303","卧龙区"),
        ADDRESS_411321("410000","河南省","411300","南阳市","411321","南召县"),
        ADDRESS_411322("410000","河南省","411300","南阳市","411322","方城县"),
        ADDRESS_411323("410000","河南省","411300","南阳市","411323","西峡县"),
        ADDRESS_411324("410000","河南省","411300","南阳市","411324","镇平县"),
        ADDRESS_411325("410000","河南省","411300","南阳市","411325","内乡县"),
        ADDRESS_411326("410000","河南省","411300","南阳市","411326","淅川县"),
        ADDRESS_411327("410000","河南省","411300","南阳市","411327","社旗县"),
        ADDRESS_411328("410000","河南省","411300","南阳市","411328","唐河县"),
        ADDRESS_411329("410000","河南省","411300","南阳市","411329","新野县"),
        ADDRESS_411330("410000","河南省","411300","南阳市","411330","桐柏县"),
        ADDRESS_411381("410000","河南省","411300","南阳市","411381","邓州市"),
        ADDRESS_411402("410000","河南省","411400","商丘市","411402","梁园区"),
        ADDRESS_411403("410000","河南省","411400","商丘市","411403","睢阳区"),
        ADDRESS_411421("410000","河南省","411400","商丘市","411421","民权县"),
        ADDRESS_411422("410000","河南省","411400","商丘市","411422","睢县"),
        ADDRESS_411423("410000","河南省","411400","商丘市","411423","宁陵县"),
        ADDRESS_411424("410000","河南省","411400","商丘市","411424","柘城县"),
        ADDRESS_411425("410000","河南省","411400","商丘市","411425","虞城县"),
        ADDRESS_411426("410000","河南省","411400","商丘市","411426","夏邑县"),
        ADDRESS_411481("410000","河南省","411400","商丘市","411481","永城市"),
        ADDRESS_411502("410000","河南省","411500","信阳市","411502","浉河区"),
        ADDRESS_411503("410000","河南省","411500","信阳市","411503","平桥区"),
        ADDRESS_411521("410000","河南省","411500","信阳市","411521","罗山县"),
        ADDRESS_411522("410000","河南省","411500","信阳市","411522","光山县"),
        ADDRESS_411523("410000","河南省","411500","信阳市","411523","新县"),
        ADDRESS_411524("410000","河南省","411500","信阳市","411524","商城县"),
        ADDRESS_411525("410000","河南省","411500","信阳市","411525","固始县"),
        ADDRESS_411526("410000","河南省","411500","信阳市","411526","潢川县"),
        ADDRESS_411527("410000","河南省","411500","信阳市","411527","淮滨县"),
        ADDRESS_411528("410000","河南省","411500","信阳市","411528","息县"),
        ADDRESS_411602("410000","河南省","411600","周口市","411602","川汇区"),
        ADDRESS_411603("410000","河南省","411600","周口市","411603","淮阳区"),
        ADDRESS_411621("410000","河南省","411600","周口市","411621","扶沟县"),
        ADDRESS_411622("410000","河南省","411600","周口市","411622","西华县"),
        ADDRESS_411623("410000","河南省","411600","周口市","411623","商水县"),
        ADDRESS_411624("410000","河南省","411600","周口市","411624","沈丘县"),
        ADDRESS_411625("410000","河南省","411600","周口市","411625","郸城县"),
        ADDRESS_411627("410000","河南省","411600","周口市","411627","太康县"),
        ADDRESS_411628("410000","河南省","411600","周口市","411628","鹿邑县"),
        ADDRESS_411681("410000","河南省","411600","周口市","411681","项城市"),
        ADDRESS_411702("410000","河南省","411700","驻马店市","411702","驿城区"),
        ADDRESS_411721("410000","河南省","411700","驻马店市","411721","西平县"),
        ADDRESS_411722("410000","河南省","411700","驻马店市","411722","上蔡县"),
        ADDRESS_411723("410000","河南省","411700","驻马店市","411723","平舆县"),
        ADDRESS_411724("410000","河南省","411700","驻马店市","411724","正阳县"),
        ADDRESS_411725("410000","河南省","411700","驻马店市","411725","确山县"),
        ADDRESS_411726("410000","河南省","411700","驻马店市","411726","泌阳县"),
        ADDRESS_411727("410000","河南省","411700","驻马店市","411727","汝南县"),
        ADDRESS_411728("410000","河南省","411700","驻马店市","411728","遂平县"),
        ADDRESS_411729("410000","河南省","411700","驻马店市","411729","新蔡县"),
        ADDRESS_419001("410000","河南省","419000","省直辖县","419001","济源市"),

        ;



        AddressCode410000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode410000Enum addressCodeEnum :AddressCode410000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 湖北省枚举
     */
    public enum AddressCode420000Enum {

        ADDRESS_420102("420000","湖北省","420100","武汉市","420102","江岸区"),
        ADDRESS_420103("420000","湖北省","420100","武汉市","420103","江汉区"),
        ADDRESS_420104("420000","湖北省","420100","武汉市","420104","硚口区"),
        ADDRESS_420105("420000","湖北省","420100","武汉市","420105","汉阳区"),
        ADDRESS_420106("420000","湖北省","420100","武汉市","420106","武昌区"),
        ADDRESS_420107("420000","湖北省","420100","武汉市","420107","青山区"),
        ADDRESS_420111("420000","湖北省","420100","武汉市","420111","洪山区"),
        ADDRESS_420112("420000","湖北省","420100","武汉市","420112","东西湖区"),
        ADDRESS_420113("420000","湖北省","420100","武汉市","420113","汉南区"),
        ADDRESS_420114("420000","湖北省","420100","武汉市","420114","蔡甸区"),
        ADDRESS_420115("420000","湖北省","420100","武汉市","420115","江夏区"),
        ADDRESS_420116("420000","湖北省","420100","武汉市","420116","黄陂区"),
        ADDRESS_420117("420000","湖北省","420100","武汉市","420117","新洲区"),
        ADDRESS_420202("420000","湖北省","420200","黄石市","420202","黄石港区"),
        ADDRESS_420203("420000","湖北省","420200","黄石市","420203","西塞山区"),
        ADDRESS_420204("420000","湖北省","420200","黄石市","420204","下陆区"),
        ADDRESS_420205("420000","湖北省","420200","黄石市","420205","铁山区"),
        ADDRESS_420222("420000","湖北省","420200","黄石市","420222","阳新县"),
        ADDRESS_420281("420000","湖北省","420200","黄石市","420281","大冶市"),
        ADDRESS_420302("420000","湖北省","420300","十堰市","420302","茅箭区"),
        ADDRESS_420303("420000","湖北省","420300","十堰市","420303","张湾区"),
        ADDRESS_420304("420000","湖北省","420300","十堰市","420304","郧阳区"),
        ADDRESS_420322("420000","湖北省","420300","十堰市","420322","郧西县"),
        ADDRESS_420323("420000","湖北省","420300","十堰市","420323","竹山县"),
        ADDRESS_420324("420000","湖北省","420300","十堰市","420324","竹溪县"),
        ADDRESS_420325("420000","湖北省","420300","十堰市","420325","房县"),
        ADDRESS_420381("420000","湖北省","420300","十堰市","420381","丹江口市"),
        ADDRESS_420502("420000","湖北省","420500","宜昌市","420502","西陵区"),
        ADDRESS_420503("420000","湖北省","420500","宜昌市","420503","伍家岗区"),
        ADDRESS_420504("420000","湖北省","420500","宜昌市","420504","点军区"),
        ADDRESS_420505("420000","湖北省","420500","宜昌市","420505","猇亭区"),
        ADDRESS_420506("420000","湖北省","420500","宜昌市","420506","夷陵区"),
        ADDRESS_420525("420000","湖北省","420500","宜昌市","420525","远安县"),
        ADDRESS_420526("420000","湖北省","420500","宜昌市","420526","兴山县"),
        ADDRESS_420527("420000","湖北省","420500","宜昌市","420527","秭归县"),
        ADDRESS_420528("420000","湖北省","420500","宜昌市","420528","长阳土家族自治县"),
        ADDRESS_420529("420000","湖北省","420500","宜昌市","420529","五峰土家族自治县"),
        ADDRESS_420581("420000","湖北省","420500","宜昌市","420581","宜都市"),
        ADDRESS_420582("420000","湖北省","420500","宜昌市","420582","当阳市"),
        ADDRESS_420583("420000","湖北省","420500","宜昌市","420583","枝江市"),
        ADDRESS_420602("420000","湖北省","420600","襄阳市","420602","襄城区"),
        ADDRESS_420606("420000","湖北省","420600","襄阳市","420606","樊城区"),
        ADDRESS_420607("420000","湖北省","420600","襄阳市","420607","襄州区"),
        ADDRESS_420624("420000","湖北省","420600","襄阳市","420624","南漳县"),
        ADDRESS_420625("420000","湖北省","420600","襄阳市","420625","谷城县"),
        ADDRESS_420626("420000","湖北省","420600","襄阳市","420626","保康县"),
        ADDRESS_420682("420000","湖北省","420600","襄阳市","420682","老河口市"),
        ADDRESS_420683("420000","湖北省","420600","襄阳市","420683","枣阳市"),
        ADDRESS_420684("420000","湖北省","420600","襄阳市","420684","宜城市"),
        ADDRESS_420702("420000","湖北省","420700","鄂州市","420702","梁子湖区"),
        ADDRESS_420703("420000","湖北省","420700","鄂州市","420703","华容区"),
        ADDRESS_420704("420000","湖北省","420700","鄂州市","420704","鄂城区"),
        ADDRESS_420802("420000","湖北省","420800","荆门市","420802","东宝区"),
        ADDRESS_420804("420000","湖北省","420800","荆门市","420804","掇刀区"),
        ADDRESS_420822("420000","湖北省","420800","荆门市","420822","沙洋县"),
        ADDRESS_420881("420000","湖北省","420800","荆门市","420881","钟祥市"),
        ADDRESS_420882("420000","湖北省","420800","荆门市","420882","京山市"),
        ADDRESS_420902("420000","湖北省","420900","孝感市","420902","孝南区"),
        ADDRESS_420921("420000","湖北省","420900","孝感市","420921","孝昌县"),
        ADDRESS_420922("420000","湖北省","420900","孝感市","420922","大悟县"),
        ADDRESS_420923("420000","湖北省","420900","孝感市","420923","云梦县"),
        ADDRESS_420981("420000","湖北省","420900","孝感市","420981","应城市"),
        ADDRESS_420982("420000","湖北省","420900","孝感市","420982","安陆市"),
        ADDRESS_420984("420000","湖北省","420900","孝感市","420984","汉川市"),
        ADDRESS_421002("420000","湖北省","421000","荆州市","421002","沙市区"),
        ADDRESS_421003("420000","湖北省","421000","荆州市","421003","荆州区"),
        ADDRESS_421022("420000","湖北省","421000","荆州市","421022","公安县"),
        ADDRESS_421024("420000","湖北省","421000","荆州市","421024","江陵县"),
        ADDRESS_421081("420000","湖北省","421000","荆州市","421081","石首市"),
        ADDRESS_421083("420000","湖北省","421000","荆州市","421083","洪湖市"),
        ADDRESS_421087("420000","湖北省","421000","荆州市","421087","松滋市"),
        ADDRESS_421088("420000","湖北省","421000","荆州市","421088","监利市"),
        ADDRESS_421102("420000","湖北省","421100","黄冈市","421102","黄州区"),
        ADDRESS_421121("420000","湖北省","421100","黄冈市","421121","团风县"),
        ADDRESS_421122("420000","湖北省","421100","黄冈市","421122","红安县"),
        ADDRESS_421123("420000","湖北省","421100","黄冈市","421123","罗田县"),
        ADDRESS_421124("420000","湖北省","421100","黄冈市","421124","英山县"),
        ADDRESS_421125("420000","湖北省","421100","黄冈市","421125","浠水县"),
        ADDRESS_421126("420000","湖北省","421100","黄冈市","421126","蕲春县"),
        ADDRESS_421127("420000","湖北省","421100","黄冈市","421127","黄梅县"),
        ADDRESS_421181("420000","湖北省","421100","黄冈市","421181","麻城市"),
        ADDRESS_421182("420000","湖北省","421100","黄冈市","421182","武穴市"),
        ADDRESS_421202("420000","湖北省","421200","咸宁市","421202","咸安区"),
        ADDRESS_421221("420000","湖北省","421200","咸宁市","421221","嘉鱼县"),
        ADDRESS_421222("420000","湖北省","421200","咸宁市","421222","通城县"),
        ADDRESS_421223("420000","湖北省","421200","咸宁市","421223","崇阳县"),
        ADDRESS_421224("420000","湖北省","421200","咸宁市","421224","通山县"),
        ADDRESS_421281("420000","湖北省","421200","咸宁市","421281","赤壁市"),
        ADDRESS_421303("420000","湖北省","421300","随州市","421303","曾都区"),
        ADDRESS_421321("420000","湖北省","421300","随州市","421321","随县"),
        ADDRESS_421381("420000","湖北省","421300","随州市","421381","广水市"),
        ADDRESS_422801("420000","湖北省","422800","恩施土家族苗族自治州","422801","恩施市"),
        ADDRESS_422802("420000","湖北省","422800","恩施土家族苗族自治州","422802","利川市"),
        ADDRESS_422822("420000","湖北省","422800","恩施土家族苗族自治州","422822","建始县"),
        ADDRESS_422823("420000","湖北省","422800","恩施土家族苗族自治州","422823","巴东县"),
        ADDRESS_422825("420000","湖北省","422800","恩施土家族苗族自治州","422825","宣恩县"),
        ADDRESS_422826("420000","湖北省","422800","恩施土家族苗族自治州","422826","咸丰县"),
        ADDRESS_422827("420000","湖北省","422800","恩施土家族苗族自治州","422827","来凤县"),
        ADDRESS_422828("420000","湖北省","422800","恩施土家族苗族自治州","422828","鹤峰县"),
        ADDRESS_429004("420000","湖北省","429000","省直辖县","429004","仙桃市"),
        ADDRESS_429005("420000","湖北省","429000","省直辖县","429005","潜江市"),
        ADDRESS_429006("420000","湖北省","429000","省直辖县","429006","天门市"),
        ADDRESS_429021("420000","湖北省","429000","省直辖县","429021","神农架林区"),

        ;



        AddressCode420000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode420000Enum addressCodeEnum :AddressCode420000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 湖南省 河北枚举
     */
    public enum AddressCode430000Enum {

        ADDRESS_430102("430000","湖南省","430100","长沙市","430102","芙蓉区"),
        ADDRESS_430103("430000","湖南省","430100","长沙市","430103","天心区"),
        ADDRESS_430104("430000","湖南省","430100","长沙市","430104","岳麓区"),
        ADDRESS_430105("430000","湖南省","430100","长沙市","430105","开福区"),
        ADDRESS_430111("430000","湖南省","430100","长沙市","430111","雨花区"),
        ADDRESS_430112("430000","湖南省","430100","长沙市","430112","望城区"),
        ADDRESS_430121("430000","湖南省","430100","长沙市","430121","长沙县"),
        ADDRESS_430181("430000","湖南省","430100","长沙市","430181","浏阳市"),
        ADDRESS_430182("430000","湖南省","430100","长沙市","430182","宁乡市"),
        ADDRESS_430202("430000","湖南省","430200","株洲市","430202","荷塘区"),
        ADDRESS_430203("430000","湖南省","430200","株洲市","430203","芦淞区"),
        ADDRESS_430204("430000","湖南省","430200","株洲市","430204","石峰区"),
        ADDRESS_430211("430000","湖南省","430200","株洲市","430211","天元区"),
        ADDRESS_430212("430000","湖南省","430200","株洲市","430212","渌口区"),
        ADDRESS_430223("430000","湖南省","430200","株洲市","430223","攸县"),
        ADDRESS_430224("430000","湖南省","430200","株洲市","430224","茶陵县"),
        ADDRESS_430225("430000","湖南省","430200","株洲市","430225","炎陵县"),
        ADDRESS_430281("430000","湖南省","430200","株洲市","430281","醴陵市"),
        ADDRESS_430302("430000","湖南省","430300","湘潭市","430302","雨湖区"),
        ADDRESS_430304("430000","湖南省","430300","湘潭市","430304","岳塘区"),
        ADDRESS_430321("430000","湖南省","430300","湘潭市","430321","湘潭县"),
        ADDRESS_430381("430000","湖南省","430300","湘潭市","430381","湘乡市"),
        ADDRESS_430382("430000","湖南省","430300","湘潭市","430382","韶山市"),
        ADDRESS_430405("430000","湖南省","430400","衡阳市","430405","珠晖区"),
        ADDRESS_430406("430000","湖南省","430400","衡阳市","430406","雁峰区"),
        ADDRESS_430407("430000","湖南省","430400","衡阳市","430407","石鼓区"),
        ADDRESS_430408("430000","湖南省","430400","衡阳市","430408","蒸湘区"),
        ADDRESS_430412("430000","湖南省","430400","衡阳市","430412","南岳区"),
        ADDRESS_430421("430000","湖南省","430400","衡阳市","430421","衡阳县"),
        ADDRESS_430422("430000","湖南省","430400","衡阳市","430422","衡南县"),
        ADDRESS_430423("430000","湖南省","430400","衡阳市","430423","衡山县"),
        ADDRESS_430424("430000","湖南省","430400","衡阳市","430424","衡东县"),
        ADDRESS_430426("430000","湖南省","430400","衡阳市","430426","祁东县"),
        ADDRESS_430481("430000","湖南省","430400","衡阳市","430481","耒阳市"),
        ADDRESS_430482("430000","湖南省","430400","衡阳市","430482","常宁市"),
        ADDRESS_430502("430000","湖南省","430500","邵阳市","430502","双清区"),
        ADDRESS_430503("430000","湖南省","430500","邵阳市","430503","大祥区"),
        ADDRESS_430511("430000","湖南省","430500","邵阳市","430511","北塔区"),
        ADDRESS_430522("430000","湖南省","430500","邵阳市","430522","新邵县"),
        ADDRESS_430523("430000","湖南省","430500","邵阳市","430523","邵阳县"),
        ADDRESS_430524("430000","湖南省","430500","邵阳市","430524","隆回县"),
        ADDRESS_430525("430000","湖南省","430500","邵阳市","430525","洞口县"),
        ADDRESS_430527("430000","湖南省","430500","邵阳市","430527","绥宁县"),
        ADDRESS_430528("430000","湖南省","430500","邵阳市","430528","新宁县"),
        ADDRESS_430529("430000","湖南省","430500","邵阳市","430529","城步苗族自治县"),
        ADDRESS_430581("430000","湖南省","430500","邵阳市","430581","武冈市"),
        ADDRESS_430582("430000","湖南省","430500","邵阳市","430582","邵东市"),
        ADDRESS_430602("430000","湖南省","430600","岳阳市","430602","岳阳楼区"),
        ADDRESS_430603("430000","湖南省","430600","岳阳市","430603","云溪区"),
        ADDRESS_430611("430000","湖南省","430600","岳阳市","430611","君山区"),
        ADDRESS_430621("430000","湖南省","430600","岳阳市","430621","岳阳县"),
        ADDRESS_430623("430000","湖南省","430600","岳阳市","430623","华容县"),
        ADDRESS_430624("430000","湖南省","430600","岳阳市","430624","湘阴县"),
        ADDRESS_430626("430000","湖南省","430600","岳阳市","430626","平江县"),
        ADDRESS_430681("430000","湖南省","430600","岳阳市","430681","汨罗市"),
        ADDRESS_430682("430000","湖南省","430600","岳阳市","430682","临湘市"),
        ADDRESS_430702("430000","湖南省","430700","常德市","430702","武陵区"),
        ADDRESS_430703("430000","湖南省","430700","常德市","430703","鼎城区"),
        ADDRESS_430721("430000","湖南省","430700","常德市","430721","安乡县"),
        ADDRESS_430722("430000","湖南省","430700","常德市","430722","汉寿县"),
        ADDRESS_430723("430000","湖南省","430700","常德市","430723","澧县"),
        ADDRESS_430724("430000","湖南省","430700","常德市","430724","临澧县"),
        ADDRESS_430725("430000","湖南省","430700","常德市","430725","桃源县"),
        ADDRESS_430726("430000","湖南省","430700","常德市","430726","石门县"),
        ADDRESS_430781("430000","湖南省","430700","常德市","430781","津市市"),
        ADDRESS_430802("430000","湖南省","430800","张家界市","430802","永定区"),
        ADDRESS_430811("430000","湖南省","430800","张家界市","430811","武陵源区"),
        ADDRESS_430821("430000","湖南省","430800","张家界市","430821","慈利县"),
        ADDRESS_430822("430000","湖南省","430800","张家界市","430822","桑植县"),
        ADDRESS_430902("430000","湖南省","430900","益阳市","430902","资阳区"),
        ADDRESS_430903("430000","湖南省","430900","益阳市","430903","赫山区"),
        ADDRESS_430921("430000","湖南省","430900","益阳市","430921","南县"),
        ADDRESS_430922("430000","湖南省","430900","益阳市","430922","桃江县"),
        ADDRESS_430923("430000","湖南省","430900","益阳市","430923","安化县"),
        ADDRESS_430981("430000","湖南省","430900","益阳市","430981","沅江市"),
        ADDRESS_431002("430000","湖南省","431000","郴州市","431002","北湖区"),
        ADDRESS_431003("430000","湖南省","431000","郴州市","431003","苏仙区"),
        ADDRESS_431021("430000","湖南省","431000","郴州市","431021","桂阳县"),
        ADDRESS_431022("430000","湖南省","431000","郴州市","431022","宜章县"),
        ADDRESS_431023("430000","湖南省","431000","郴州市","431023","永兴县"),
        ADDRESS_431024("430000","湖南省","431000","郴州市","431024","嘉禾县"),
        ADDRESS_431025("430000","湖南省","431000","郴州市","431025","临武县"),
        ADDRESS_431026("430000","湖南省","431000","郴州市","431026","汝城县"),
        ADDRESS_431027("430000","湖南省","431000","郴州市","431027","桂东县"),
        ADDRESS_431028("430000","湖南省","431000","郴州市","431028","安仁县"),
        ADDRESS_431081("430000","湖南省","431000","郴州市","431081","资兴市"),
        ADDRESS_431102("430000","湖南省","431100","永州市","431102","零陵区"),
        ADDRESS_431103("430000","湖南省","431100","永州市","431103","冷水滩区"),
        ADDRESS_431121("430000","湖南省","431100","永州市","431121","祁阳县"),
        ADDRESS_431122("430000","湖南省","431100","永州市","431122","东安县"),
        ADDRESS_431123("430000","湖南省","431100","永州市","431123","双牌县"),
        ADDRESS_431124("430000","湖南省","431100","永州市","431124","道县"),
        ADDRESS_431125("430000","湖南省","431100","永州市","431125","江永县"),
        ADDRESS_431126("430000","湖南省","431100","永州市","431126","宁远县"),
        ADDRESS_431127("430000","湖南省","431100","永州市","431127","蓝山县"),
        ADDRESS_431128("430000","湖南省","431100","永州市","431128","新田县"),
        ADDRESS_431129("430000","湖南省","431100","永州市","431129","江华瑶族自治县"),
        ADDRESS_431202("430000","湖南省","431200","怀化市","431202","鹤城区"),
        ADDRESS_431221("430000","湖南省","431200","怀化市","431221","中方县"),
        ADDRESS_431222("430000","湖南省","431200","怀化市","431222","沅陵县"),
        ADDRESS_431223("430000","湖南省","431200","怀化市","431223","辰溪县"),
        ADDRESS_431224("430000","湖南省","431200","怀化市","431224","溆浦县"),
        ADDRESS_431225("430000","湖南省","431200","怀化市","431225","会同县"),
        ADDRESS_431226("430000","湖南省","431200","怀化市","431226","麻阳苗族自治县"),
        ADDRESS_431227("430000","湖南省","431200","怀化市","431227","新晃侗族自治县"),
        ADDRESS_431228("430000","湖南省","431200","怀化市","431228","芷江侗族自治县"),
        ADDRESS_431229("430000","湖南省","431200","怀化市","431229","靖州苗族侗族自治县"),
        ADDRESS_431230("430000","湖南省","431200","怀化市","431230","通道侗族自治县"),
        ADDRESS_431281("430000","湖南省","431200","怀化市","431281","洪江市"),
        ADDRESS_431302("430000","湖南省","431300","娄底市","431302","娄星区"),
        ADDRESS_431321("430000","湖南省","431300","娄底市","431321","双峰县"),
        ADDRESS_431322("430000","湖南省","431300","娄底市","431322","新化县"),
        ADDRESS_431381("430000","湖南省","431300","娄底市","431381","冷水江市"),
        ADDRESS_431382("430000","湖南省","431300","娄底市","431382","涟源市"),
        ADDRESS_433101("430000","湖南省","433100","湘西土家族苗族自治州","433101","吉首市"),
        ADDRESS_433122("430000","湖南省","433100","湘西土家族苗族自治州","433122","泸溪县"),
        ADDRESS_433123("430000","湖南省","433100","湘西土家族苗族自治州","433123","凤凰县"),
        ADDRESS_433124("430000","湖南省","433100","湘西土家族苗族自治州","433124","花垣县"),
        ADDRESS_433125("430000","湖南省","433100","湘西土家族苗族自治州","433125","保靖县"),
        ADDRESS_433126("430000","湖南省","433100","湘西土家族苗族自治州","433126","古丈县"),
        ADDRESS_433127("430000","湖南省","433100","湘西土家族苗族自治州","433127","永顺县"),
        ADDRESS_433130("430000","湖南省","433100","湘西土家族苗族自治州","433130","龙山县"),

        ;



        AddressCode430000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode430000Enum addressCodeEnum :AddressCode430000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 广东省枚举
     */
    public enum AddressCode440000Enum {

        ADDRESS_440103("440000","广东省","440100","广州市","440103","荔湾区"),
        ADDRESS_440104("440000","广东省","440100","广州市","440104","越秀区"),
        ADDRESS_440105("440000","广东省","440100","广州市","440105","海珠区"),
        ADDRESS_440106("440000","广东省","440100","广州市","440106","天河区"),
        ADDRESS_440111("440000","广东省","440100","广州市","440111","白云区"),
        ADDRESS_440112("440000","广东省","440100","广州市","440112","黄埔区"),
        ADDRESS_440113("440000","广东省","440100","广州市","440113","番禺区"),
        ADDRESS_440114("440000","广东省","440100","广州市","440114","花都区"),
        ADDRESS_440115("440000","广东省","440100","广州市","440115","南沙区"),
        ADDRESS_440117("440000","广东省","440100","广州市","440117","从化区"),
        ADDRESS_440118("440000","广东省","440100","广州市","440118","增城区"),
        ADDRESS_440203("440000","广东省","440200","韶关市","440203","武江区"),
        ADDRESS_440204("440000","广东省","440200","韶关市","440204","浈江区"),
        ADDRESS_440205("440000","广东省","440200","韶关市","440205","曲江区"),
        ADDRESS_440222("440000","广东省","440200","韶关市","440222","始兴县"),
        ADDRESS_440224("440000","广东省","440200","韶关市","440224","仁化县"),
        ADDRESS_440229("440000","广东省","440200","韶关市","440229","翁源县"),
        ADDRESS_440232("440000","广东省","440200","韶关市","440232","乳源瑶族自治县"),
        ADDRESS_440233("440000","广东省","440200","韶关市","440233","新丰县"),
        ADDRESS_440281("440000","广东省","440200","韶关市","440281","乐昌市"),
        ADDRESS_440282("440000","广东省","440200","韶关市","440282","南雄市"),
        ADDRESS_440303("440000","广东省","440300","深圳市","440303","罗湖区"),
        ADDRESS_440304("440000","广东省","440300","深圳市","440304","福田区"),
        ADDRESS_440305("440000","广东省","440300","深圳市","440305","南山区"),
        ADDRESS_440306("440000","广东省","440300","深圳市","440306","宝安区"),
        ADDRESS_440307("440000","广东省","440300","深圳市","440307","龙岗区"),
        ADDRESS_440308("440000","广东省","440300","深圳市","440308","盐田区"),
        ADDRESS_440309("440000","广东省","440300","深圳市","440309","龙华区"),
        ADDRESS_440310("440000","广东省","440300","深圳市","440310","坪山区"),
        ADDRESS_440311("440000","广东省","440300","深圳市","440311","光明区"),
        ADDRESS_440402("440000","广东省","440400","珠海市","440402","香洲区"),
        ADDRESS_440403("440000","广东省","440400","珠海市","440403","斗门区"),
        ADDRESS_440404("440000","广东省","440400","珠海市","440404","金湾区"),
        ADDRESS_440507("440000","广东省","440500","汕头市","440507","龙湖区"),
        ADDRESS_440511("440000","广东省","440500","汕头市","440511","金平区"),
        ADDRESS_440512("440000","广东省","440500","汕头市","440512","濠江区"),
        ADDRESS_440513("440000","广东省","440500","汕头市","440513","潮阳区"),
        ADDRESS_440514("440000","广东省","440500","汕头市","440514","潮南区"),
        ADDRESS_440515("440000","广东省","440500","汕头市","440515","澄海区"),
        ADDRESS_440523("440000","广东省","440500","汕头市","440523","南澳县"),
        ADDRESS_440604("440000","广东省","440600","佛山市","440604","禅城区"),
        ADDRESS_440605("440000","广东省","440600","佛山市","440605","南海区"),
        ADDRESS_440606("440000","广东省","440600","佛山市","440606","顺德区"),
        ADDRESS_440607("440000","广东省","440600","佛山市","440607","三水区"),
        ADDRESS_440608("440000","广东省","440600","佛山市","440608","高明区"),
        ADDRESS_440703("440000","广东省","440700","江门市","440703","蓬江区"),
        ADDRESS_440704("440000","广东省","440700","江门市","440704","江海区"),
        ADDRESS_440705("440000","广东省","440700","江门市","440705","新会区"),
        ADDRESS_440781("440000","广东省","440700","江门市","440781","台山市"),
        ADDRESS_440783("440000","广东省","440700","江门市","440783","开平市"),
        ADDRESS_440784("440000","广东省","440700","江门市","440784","鹤山市"),
        ADDRESS_440785("440000","广东省","440700","江门市","440785","恩平市"),
        ADDRESS_440802("440000","广东省","440800","湛江市","440802","赤坎区"),
        ADDRESS_440803("440000","广东省","440800","湛江市","440803","霞山区"),
        ADDRESS_440804("440000","广东省","440800","湛江市","440804","坡头区"),
        ADDRESS_440811("440000","广东省","440800","湛江市","440811","麻章区"),
        ADDRESS_440823("440000","广东省","440800","湛江市","440823","遂溪县"),
        ADDRESS_440825("440000","广东省","440800","湛江市","440825","徐闻县"),
        ADDRESS_440881("440000","广东省","440800","湛江市","440881","廉江市"),
        ADDRESS_440882("440000","广东省","440800","湛江市","440882","雷州市"),
        ADDRESS_440883("440000","广东省","440800","湛江市","440883","吴川市"),
        ADDRESS_440902("440000","广东省","440900","茂名市","440902","茂南区"),
        ADDRESS_440904("440000","广东省","440900","茂名市","440904","电白区"),
        ADDRESS_440981("440000","广东省","440900","茂名市","440981","高州市"),
        ADDRESS_440982("440000","广东省","440900","茂名市","440982","化州市"),
        ADDRESS_440983("440000","广东省","440900","茂名市","440983","信宜市"),
        ADDRESS_441202("440000","广东省","441200","肇庆市","441202","端州区"),
        ADDRESS_441203("440000","广东省","441200","肇庆市","441203","鼎湖区"),
        ADDRESS_441204("440000","广东省","441200","肇庆市","441204","高要区"),
        ADDRESS_441223("440000","广东省","441200","肇庆市","441223","广宁县"),
        ADDRESS_441224("440000","广东省","441200","肇庆市","441224","怀集县"),
        ADDRESS_441225("440000","广东省","441200","肇庆市","441225","封开县"),
        ADDRESS_441226("440000","广东省","441200","肇庆市","441226","德庆县"),
        ADDRESS_441284("440000","广东省","441200","肇庆市","441284","四会市"),
        ADDRESS_441302("440000","广东省","441300","惠州市","441302","惠城区"),
        ADDRESS_441303("440000","广东省","441300","惠州市","441303","惠阳区"),
        ADDRESS_441322("440000","广东省","441300","惠州市","441322","博罗县"),
        ADDRESS_441323("440000","广东省","441300","惠州市","441323","惠东县"),
        ADDRESS_441324("440000","广东省","441300","惠州市","441324","龙门县"),
        ADDRESS_441402("440000","广东省","441400","梅州市","441402","梅江区"),
        ADDRESS_441403("440000","广东省","441400","梅州市","441403","梅县区"),
        ADDRESS_441422("440000","广东省","441400","梅州市","441422","大埔县"),
        ADDRESS_441423("440000","广东省","441400","梅州市","441423","丰顺县"),
        ADDRESS_441424("440000","广东省","441400","梅州市","441424","五华县"),
        ADDRESS_441426("440000","广东省","441400","梅州市","441426","平远县"),
        ADDRESS_441427("440000","广东省","441400","梅州市","441427","蕉岭县"),
        ADDRESS_441481("440000","广东省","441400","梅州市","441481","兴宁市"),
        ADDRESS_441502("440000","广东省","441500","汕尾市","441502","城区"),
        ADDRESS_441521("440000","广东省","441500","汕尾市","441521","海丰县"),
        ADDRESS_441523("440000","广东省","441500","汕尾市","441523","陆河县"),
        ADDRESS_441581("440000","广东省","441500","汕尾市","441581","陆丰市"),
        ADDRESS_441602("440000","广东省","441600","河源市","441602","源城区"),
        ADDRESS_441621("440000","广东省","441600","河源市","441621","紫金县"),
        ADDRESS_441622("440000","广东省","441600","河源市","441622","龙川县"),
        ADDRESS_441623("440000","广东省","441600","河源市","441623","连平县"),
        ADDRESS_441624("440000","广东省","441600","河源市","441624","和平县"),
        ADDRESS_441625("440000","广东省","441600","河源市","441625","东源县"),
        ADDRESS_441702("440000","广东省","441700","阳江市","441702","江城区"),
        ADDRESS_441704("440000","广东省","441700","阳江市","441704","阳东区"),
        ADDRESS_441721("440000","广东省","441700","阳江市","441721","阳西县"),
        ADDRESS_441781("440000","广东省","441700","阳江市","441781","阳春市"),
        ADDRESS_441802("440000","广东省","441800","清远市","441802","清城区"),
        ADDRESS_441803("440000","广东省","441800","清远市","441803","清新区"),
        ADDRESS_441821("440000","广东省","441800","清远市","441821","佛冈县"),
        ADDRESS_441823("440000","广东省","441800","清远市","441823","阳山县"),
        ADDRESS_441825("440000","广东省","441800","清远市","441825","连山壮族瑶族自治县"),
        ADDRESS_441826("440000","广东省","441800","清远市","441826","连南瑶族自治县"),
        ADDRESS_441881("440000","广东省","441800","清远市","441881","英德市"),
        ADDRESS_441882("440000","广东省","441800","清远市","441882","连州市"),
        ADDRESS_441901("440000","广东省","441900","东莞市","441901","中堂镇"),
        ADDRESS_441903("440000","广东省","441900","东莞市","441903","南城街道"),
        ADDRESS_441904("440000","广东省","441900","东莞市","441904","长安镇"),
        ADDRESS_441905("440000","广东省","441900","东莞市","441905","东坑镇"),
        ADDRESS_441906("440000","广东省","441900","东莞市","441906","樟木头镇"),
        ADDRESS_441907("440000","广东省","441900","东莞市","441907","莞城街道"),
        ADDRESS_441908("440000","广东省","441900","东莞市","441908","石龙镇"),
        ADDRESS_441909("440000","广东省","441900","东莞市","441909","桥头镇"),
        ADDRESS_441910("440000","广东省","441900","东莞市","441910","万江街道"),
        ADDRESS_441911("440000","广东省","441900","东莞市","441911","麻涌镇"),
        ADDRESS_441912("440000","广东省","441900","东莞市","441912","虎门镇"),
        ADDRESS_441913("440000","广东省","441900","东莞市","441913","谢岗镇"),
        ADDRESS_441914("440000","广东省","441900","东莞市","441914","石碣镇"),
        ADDRESS_441915("440000","广东省","441900","东莞市","441915","茶山镇"),
        ADDRESS_441916("440000","广东省","441900","东莞市","441916","东城街道"),
        ADDRESS_441917("440000","广东省","441900","东莞市","441917","洪梅镇"),
        ADDRESS_441918("440000","广东省","441900","东莞市","441918","道滘镇"),
        ADDRESS_441919("440000","广东省","441900","东莞市","441919","高埗镇"),
        ADDRESS_441920("440000","广东省","441900","东莞市","441920","企石镇"),
        ADDRESS_441921("440000","广东省","441900","东莞市","441921","凤岗镇"),
        ADDRESS_441922("440000","广东省","441900","东莞市","441922","大岭山镇"),
        ADDRESS_441923("440000","广东省","441900","东莞市","441923","松山湖"),
        ADDRESS_441924("440000","广东省","441900","东莞市","441924","清溪镇"),
        ADDRESS_441925("440000","广东省","441900","东莞市","441925","望牛墩镇"),
        ADDRESS_441926("440000","广东省","441900","东莞市","441926","厚街镇"),
        ADDRESS_441927("440000","广东省","441900","东莞市","441927","常平镇"),
        ADDRESS_441928("440000","广东省","441900","东莞市","441928","寮步镇"),
        ADDRESS_441929("440000","广东省","441900","东莞市","441929","石排镇"),
        ADDRESS_441930("440000","广东省","441900","东莞市","441930","横沥镇"),
        ADDRESS_441931("440000","广东省","441900","东莞市","441931","塘厦镇"),
        ADDRESS_441932("440000","广东省","441900","东莞市","441932","黄江镇"),
        ADDRESS_441933("440000","广东省","441900","东莞市","441933","大朗镇"),
        ADDRESS_441934("440000","广东省","441900","东莞市","441934","东莞港"),
        ADDRESS_441935("440000","广东省","441900","东莞市","441935","东莞生态园"),
        ADDRESS_441990("440000","广东省","441900","东莞市","441990","沙田镇"),
        ADDRESS_442001("440000","广东省","442000","中山市","442001","南头镇"),
        ADDRESS_442002("440000","广东省","442000","中山市","442002","神湾镇"),
        ADDRESS_442003("440000","广东省","442000","中山市","442003","东凤镇"),
        ADDRESS_442004("440000","广东省","442000","中山市","442004","五桂山街道"),
        ADDRESS_442005("440000","广东省","442000","中山市","442005","黄圃镇"),
        ADDRESS_442006("440000","广东省","442000","中山市","442006","小榄镇"),
        ADDRESS_442007("440000","广东省","442000","中山市","442007","石岐街道"),
        ADDRESS_442008("440000","广东省","442000","中山市","442008","横栏镇"),
        ADDRESS_442009("440000","广东省","442000","中山市","442009","三角镇"),
        ADDRESS_442010("440000","广东省","442000","中山市","442010","三乡镇"),
        ADDRESS_442011("440000","广东省","442000","中山市","442011","港口镇"),
        ADDRESS_442012("440000","广东省","442000","中山市","442012","沙溪镇"),
        ADDRESS_442013("440000","广东省","442000","中山市","442013","板芙镇"),
        ADDRESS_442015("440000","广东省","442000","中山市","442015","东升镇"),
        ADDRESS_442016("440000","广东省","442000","中山市","442016","阜沙镇"),
        ADDRESS_442017("440000","广东省","442000","中山市","442017","民众镇"),
        ADDRESS_442018("440000","广东省","442000","中山市","442018","东区街道"),
        ADDRESS_442019("440000","广东省","442000","中山市","442019","火炬开发区街道办事处"),
        ADDRESS_442020("440000","广东省","442000","中山市","442020","西区街道"),
        ADDRESS_442021("440000","广东省","442000","中山市","442021","南区街道"),
        ADDRESS_442022("440000","广东省","442000","中山市","442022","古镇镇"),
        ADDRESS_442023("440000","广东省","442000","中山市","442023","坦洲镇"),
        ADDRESS_442024("440000","广东省","442000","中山市","442024","大涌镇"),
        ADDRESS_442025("440000","广东省","442000","中山市","442025","南朗镇"),
        ADDRESS_445102("440000","广东省","445100","潮州市","445102","湘桥区"),
        ADDRESS_445103("440000","广东省","445100","潮州市","445103","潮安区"),
        ADDRESS_445122("440000","广东省","445100","潮州市","445122","饶平县"),
        ADDRESS_445202("440000","广东省","445200","揭阳市","445202","榕城区"),
        ADDRESS_445203("440000","广东省","445200","揭阳市","445203","揭东区"),
        ADDRESS_445222("440000","广东省","445200","揭阳市","445222","揭西县"),
        ADDRESS_445224("440000","广东省","445200","揭阳市","445224","惠来县"),
        ADDRESS_445281("440000","广东省","445200","揭阳市","445281","普宁市"),
        ADDRESS_445302("440000","广东省","445300","云浮市","445302","云城区"),
        ADDRESS_445303("440000","广东省","445300","云浮市","445303","云安区"),
        ADDRESS_445321("440000","广东省","445300","云浮市","445321","新兴县"),
        ADDRESS_445322("440000","广东省","445300","云浮市","445322","郁南县"),
        ADDRESS_445381("440000","广东省","445300","云浮市","445381","罗定市"),

        ;



        AddressCode440000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode440000Enum addressCodeEnum :AddressCode440000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 广西壮族自治区 枚举
     */
    public enum AddressCode450000Enum {

        ADDRESS_450102("450000","广西壮族自治区","450100","南宁市","450102","兴宁区"),
        ADDRESS_450103("450000","广西壮族自治区","450100","南宁市","450103","青秀区"),
        ADDRESS_450105("450000","广西壮族自治区","450100","南宁市","450105","江南区"),
        ADDRESS_450107("450000","广西壮族自治区","450100","南宁市","450107","西乡塘区"),
        ADDRESS_450108("450000","广西壮族自治区","450100","南宁市","450108","良庆区"),
        ADDRESS_450109("450000","广西壮族自治区","450100","南宁市","450109","邕宁区"),
        ADDRESS_450110("450000","广西壮族自治区","450100","南宁市","450110","武鸣区"),
        ADDRESS_450123("450000","广西壮族自治区","450100","南宁市","450123","隆安县"),
        ADDRESS_450124("450000","广西壮族自治区","450100","南宁市","450124","马山县"),
        ADDRESS_450125("450000","广西壮族自治区","450100","南宁市","450125","上林县"),
        ADDRESS_450126("450000","广西壮族自治区","450100","南宁市","450126","宾阳县"),
        ADDRESS_450181("450000","广西壮族自治区","450100","南宁市","450181","横州市"),
        ADDRESS_450202("450000","广西壮族自治区","450200","柳州市","450202","城中区"),
        ADDRESS_450203("450000","广西壮族自治区","450200","柳州市","450203","鱼峰区"),
        ADDRESS_450204("450000","广西壮族自治区","450200","柳州市","450204","柳南区"),
        ADDRESS_450205("450000","广西壮族自治区","450200","柳州市","450205","柳北区"),
        ADDRESS_450206("450000","广西壮族自治区","450200","柳州市","450206","柳江区"),
        ADDRESS_450222("450000","广西壮族自治区","450200","柳州市","450222","柳城县"),
        ADDRESS_450223("450000","广西壮族自治区","450200","柳州市","450223","鹿寨县"),
        ADDRESS_450224("450000","广西壮族自治区","450200","柳州市","450224","融安县"),
        ADDRESS_450225("450000","广西壮族自治区","450200","柳州市","450225","融水苗族自治县"),
        ADDRESS_450226("450000","广西壮族自治区","450200","柳州市","450226","三江侗族自治县"),
        ADDRESS_450302("450000","广西壮族自治区","450300","桂林市","450302","秀峰区"),
        ADDRESS_450303("450000","广西壮族自治区","450300","桂林市","450303","叠彩区"),
        ADDRESS_450304("450000","广西壮族自治区","450300","桂林市","450304","象山区"),
        ADDRESS_450305("450000","广西壮族自治区","450300","桂林市","450305","七星区"),
        ADDRESS_450311("450000","广西壮族自治区","450300","桂林市","450311","雁山区"),
        ADDRESS_450312("450000","广西壮族自治区","450300","桂林市","450312","临桂区"),
        ADDRESS_450321("450000","广西壮族自治区","450300","桂林市","450321","阳朔县"),
        ADDRESS_450323("450000","广西壮族自治区","450300","桂林市","450323","灵川县"),
        ADDRESS_450324("450000","广西壮族自治区","450300","桂林市","450324","全州县"),
        ADDRESS_450325("450000","广西壮族自治区","450300","桂林市","450325","兴安县"),
        ADDRESS_450326("450000","广西壮族自治区","450300","桂林市","450326","永福县"),
        ADDRESS_450327("450000","广西壮族自治区","450300","桂林市","450327","灌阳县"),
        ADDRESS_450328("450000","广西壮族自治区","450300","桂林市","450328","龙胜各族自治县"),
        ADDRESS_450329("450000","广西壮族自治区","450300","桂林市","450329","资源县"),
        ADDRESS_450330("450000","广西壮族自治区","450300","桂林市","450330","平乐县"),
        ADDRESS_450332("450000","广西壮族自治区","450300","桂林市","450332","恭城瑶族自治县"),
        ADDRESS_450381("450000","广西壮族自治区","450300","桂林市","450381","荔浦市"),
        ADDRESS_450403("450000","广西壮族自治区","450400","梧州市","450403","万秀区"),
        ADDRESS_450405("450000","广西壮族自治区","450400","梧州市","450405","长洲区"),
        ADDRESS_450406("450000","广西壮族自治区","450400","梧州市","450406","龙圩区"),
        ADDRESS_450421("450000","广西壮族自治区","450400","梧州市","450421","苍梧县"),
        ADDRESS_450422("450000","广西壮族自治区","450400","梧州市","450422","藤县"),
        ADDRESS_450423("450000","广西壮族自治区","450400","梧州市","450423","蒙山县"),
        ADDRESS_450481("450000","广西壮族自治区","450400","梧州市","450481","岑溪市"),
        ADDRESS_450502("450000","广西壮族自治区","450500","北海市","450502","海城区"),
        ADDRESS_450503("450000","广西壮族自治区","450500","北海市","450503","银海区"),
        ADDRESS_450512("450000","广西壮族自治区","450500","北海市","450512","铁山港区"),
        ADDRESS_450521("450000","广西壮族自治区","450500","北海市","450521","合浦县"),
        ADDRESS_450602("450000","广西壮族自治区","450600","防城港市","450602","港口区"),
        ADDRESS_450603("450000","广西壮族自治区","450600","防城港市","450603","防城区"),
        ADDRESS_450621("450000","广西壮族自治区","450600","防城港市","450621","上思县"),
        ADDRESS_450681("450000","广西壮族自治区","450600","防城港市","450681","东兴市"),
        ADDRESS_450702("450000","广西壮族自治区","450700","钦州市","450702","钦南区"),
        ADDRESS_450703("450000","广西壮族自治区","450700","钦州市","450703","钦北区"),
        ADDRESS_450721("450000","广西壮族自治区","450700","钦州市","450721","灵山县"),
        ADDRESS_450722("450000","广西壮族自治区","450700","钦州市","450722","浦北县"),
        ADDRESS_450802("450000","广西壮族自治区","450800","贵港市","450802","港北区"),
        ADDRESS_450803("450000","广西壮族自治区","450800","贵港市","450803","港南区"),
        ADDRESS_450804("450000","广西壮族自治区","450800","贵港市","450804","覃塘区"),
        ADDRESS_450821("450000","广西壮族自治区","450800","贵港市","450821","平南县"),
        ADDRESS_450881("450000","广西壮族自治区","450800","贵港市","450881","桂平市"),
        ADDRESS_450902("450000","广西壮族自治区","450900","玉林市","450902","玉州区"),
        ADDRESS_450903("450000","广西壮族自治区","450900","玉林市","450903","福绵区"),
        ADDRESS_450921("450000","广西壮族自治区","450900","玉林市","450921","容县"),
        ADDRESS_450922("450000","广西壮族自治区","450900","玉林市","450922","陆川县"),
        ADDRESS_450923("450000","广西壮族自治区","450900","玉林市","450923","博白县"),
        ADDRESS_450924("450000","广西壮族自治区","450900","玉林市","450924","兴业县"),
        ADDRESS_450981("450000","广西壮族自治区","450900","玉林市","450981","北流市"),
        ADDRESS_451002("450000","广西壮族自治区","451000","百色市","451002","右江区"),
        ADDRESS_451003("450000","广西壮族自治区","451000","百色市","451003","田阳区"),
        ADDRESS_451022("450000","广西壮族自治区","451000","百色市","451022","田东县"),
        ADDRESS_451024("450000","广西壮族自治区","451000","百色市","451024","德保县"),
        ADDRESS_451026("450000","广西壮族自治区","451000","百色市","451026","那坡县"),
        ADDRESS_451027("450000","广西壮族自治区","451000","百色市","451027","凌云县"),
        ADDRESS_451028("450000","广西壮族自治区","451000","百色市","451028","乐业县"),
        ADDRESS_451029("450000","广西壮族自治区","451000","百色市","451029","田林县"),
        ADDRESS_451030("450000","广西壮族自治区","451000","百色市","451030","西林县"),
        ADDRESS_451031("450000","广西壮族自治区","451000","百色市","451031","隆林各族自治县"),
        ADDRESS_451081("450000","广西壮族自治区","451000","百色市","451081","靖西市"),
        ADDRESS_451082("450000","广西壮族自治区","451000","百色市","451082","平果市"),
        ADDRESS_451102("450000","广西壮族自治区","451100","贺州市","451102","八步区"),
        ADDRESS_451103("450000","广西壮族自治区","451100","贺州市","451103","平桂区"),
        ADDRESS_451121("450000","广西壮族自治区","451100","贺州市","451121","昭平县"),
        ADDRESS_451122("450000","广西壮族自治区","451100","贺州市","451122","钟山县"),
        ADDRESS_451123("450000","广西壮族自治区","451100","贺州市","451123","富川瑶族自治县"),
        ADDRESS_451202("450000","广西壮族自治区","451200","河池市","451202","金城江区"),
        ADDRESS_451203("450000","广西壮族自治区","451200","河池市","451203","宜州区"),
        ADDRESS_451221("450000","广西壮族自治区","451200","河池市","451221","南丹县"),
        ADDRESS_451222("450000","广西壮族自治区","451200","河池市","451222","天峨县"),
        ADDRESS_451223("450000","广西壮族自治区","451200","河池市","451223","凤山县"),
        ADDRESS_451224("450000","广西壮族自治区","451200","河池市","451224","东兰县"),
        ADDRESS_451225("450000","广西壮族自治区","451200","河池市","451225","罗城仫佬族自治县"),
        ADDRESS_451226("450000","广西壮族自治区","451200","河池市","451226","环江毛南族自治县"),
        ADDRESS_451227("450000","广西壮族自治区","451200","河池市","451227","巴马瑶族自治县"),
        ADDRESS_451228("450000","广西壮族自治区","451200","河池市","451228","都安瑶族自治县"),
        ADDRESS_451229("450000","广西壮族自治区","451200","河池市","451229","大化瑶族自治县"),
        ADDRESS_451302("450000","广西壮族自治区","451300","来宾市","451302","兴宾区"),
        ADDRESS_451321("450000","广西壮族自治区","451300","来宾市","451321","忻城县"),
        ADDRESS_451322("450000","广西壮族自治区","451300","来宾市","451322","象州县"),
        ADDRESS_451323("450000","广西壮族自治区","451300","来宾市","451323","武宣县"),
        ADDRESS_451324("450000","广西壮族自治区","451300","来宾市","451324","金秀瑶族自治县"),
        ADDRESS_451381("450000","广西壮族自治区","451300","来宾市","451381","合山市"),
        ADDRESS_451402("450000","广西壮族自治区","451400","崇左市","451402","江州区"),
        ADDRESS_451421("450000","广西壮族自治区","451400","崇左市","451421","扶绥县"),
        ADDRESS_451422("450000","广西壮族自治区","451400","崇左市","451422","宁明县"),
        ADDRESS_451423("450000","广西壮族自治区","451400","崇左市","451423","龙州县"),
        ADDRESS_451424("450000","广西壮族自治区","451400","崇左市","451424","大新县"),
        ADDRESS_451425("450000","广西壮族自治区","451400","崇左市","451425","天等县"),
        ADDRESS_451481("450000","广西壮族自治区","451400","崇左市","451481","凭祥市"),

        ;



        AddressCode450000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode450000Enum addressCodeEnum :AddressCode450000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 海南省枚举
     */
    public enum AddressCode460000Enum {

        ADDRESS_460105("460000","海南省","460100","海口市","460105","秀英区"),
        ADDRESS_460106("460000","海南省","460100","海口市","460106","龙华区"),
        ADDRESS_460107("460000","海南省","460100","海口市","460107","琼山区"),
        ADDRESS_460108("460000","海南省","460100","海口市","460108","美兰区"),
        ADDRESS_460202("460000","海南省","460200","三亚市","460202","海棠区"),
        ADDRESS_460203("460000","海南省","460200","三亚市","460203","吉阳区"),
        ADDRESS_460204("460000","海南省","460200","三亚市","460204","天涯区"),
        ADDRESS_460205("460000","海南省","460200","三亚市","460205","崖州区"),
        ADDRESS_460321("460000","海南省","460300","三沙市","460321","西沙区"),
        ADDRESS_460322("460000","海南省","460300","三沙市","460322","南沙区"),
        ADDRESS_460323("460000","海南省","460300","三沙市","460323","中沙群岛的岛礁及其海域"),
        ADDRESS_460324("460000","海南省","460300","三沙市","460324","永乐群岛的岛礁及其海域"),
        ADDRESS_460401("460000","海南省","460400","儋州市","460401","那大镇"),
        ADDRESS_460402("460000","海南省","460400","儋州市","460402","和庆镇"),
        ADDRESS_460403("460000","海南省","460400","儋州市","460403","南丰镇"),
        ADDRESS_460404("460000","海南省","460400","儋州市","460404","大成镇"),
        ADDRESS_460405("460000","海南省","460400","儋州市","460405","雅星镇"),
        ADDRESS_460406("460000","海南省","460400","儋州市","460406","兰洋镇"),
        ADDRESS_460407("460000","海南省","460400","儋州市","460407","光村镇"),
        ADDRESS_460408("460000","海南省","460400","儋州市","460408","木棠镇"),
        ADDRESS_460409("460000","海南省","460400","儋州市","460409","海头镇"),
        ADDRESS_460410("460000","海南省","460400","儋州市","460410","峨蔓镇"),
        ADDRESS_460411("460000","海南省","460400","儋州市","460411","王五镇"),
        ADDRESS_460412("460000","海南省","460400","儋州市","460412","白马井镇"),
        ADDRESS_460413("460000","海南省","460400","儋州市","460413","中和镇"),
        ADDRESS_460414("460000","海南省","460400","儋州市","460414","排浦镇"),
        ADDRESS_460415("460000","海南省","460400","儋州市","460415","东成镇"),
        ADDRESS_460416("460000","海南省","460400","儋州市","460416","新州镇"),
        ADDRESS_460417("460000","海南省","460400","儋州市","460417","洋浦经济开发区"),
        ADDRESS_460418("460000","海南省","460400","儋州市","460418","华南热作学院"),

        ADDRESS_469001("460000","海南省","469000","省直辖县","469001","五指山市"),
        ADDRESS_469002("460000","海南省","469000","省直辖县","469002","琼海市"),
        ADDRESS_469005("460000","海南省","469000","省直辖县","469005","文昌市"),
        ADDRESS_469006("460000","海南省","469000","省直辖县","469006","万宁市"),
        ADDRESS_469007("460000","海南省","469000","省直辖县","469007","东方市"),
        ADDRESS_469021("460000","海南省","469000","省直辖县","469021","定安县"),
        ADDRESS_469022("460000","海南省","469000","省直辖县","469022","屯昌县"),
        ADDRESS_469023("460000","海南省","469000","省直辖县","469023","澄迈县"),
        ADDRESS_469025("460000","海南省","469000","省直辖县","469025","白沙黎族自治县"),
        ADDRESS_469026("460000","海南省","469000","省直辖县","469026","昌江黎族自治县"),
        ADDRESS_469027("460000","海南省","469000","省直辖县","469027","乐东黎族自治县"),
        ADDRESS_469028("460000","海南省","469000","省直辖县","469028","陵水黎族自治县"),
        ADDRESS_469029("460000","海南省","469000","省直辖县","469029","保亭黎族苗族自治县"),
        ADDRESS_469030("460000","海南省","469000","省直辖县","469030","琼中黎族苗族自治县"),


        ;



        AddressCode460000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode460000Enum addressCodeEnum :AddressCode460000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 重庆枚举
     */
    public enum AddressCode500000Enum {

        ADDRESS_500101("500000","重庆","500100","重庆市","500101","万州区"),
        ADDRESS_500102("500000","重庆","500100","重庆市","500102","涪陵区"),
        ADDRESS_500103("500000","重庆","500100","重庆市","500103","渝中区"),
        ADDRESS_500104("500000","重庆","500100","重庆市","500104","大渡口区"),
        ADDRESS_500105("500000","重庆","500100","重庆市","500105","江北区"),
        ADDRESS_500106("500000","重庆","500100","重庆市","500106","沙坪坝区"),
        ADDRESS_500107("500000","重庆","500100","重庆市","500107","九龙坡区"),
        ADDRESS_500108("500000","重庆","500100","重庆市","500108","南岸区"),
        ADDRESS_500109("500000","重庆","500100","重庆市","500109","北碚区"),
        ADDRESS_500110("500000","重庆","500100","重庆市","500110","綦江区"),
        ADDRESS_500111("500000","重庆","500100","重庆市","500111","大足区"),
        ADDRESS_500112("500000","重庆","500100","重庆市","500112","渝北区"),
        ADDRESS_500113("500000","重庆","500100","重庆市","500113","巴南区"),
        ADDRESS_500114("500000","重庆","500100","重庆市","500114","黔江区"),
        ADDRESS_500115("500000","重庆","500100","重庆市","500115","长寿区"),
        ADDRESS_500116("500000","重庆","500100","重庆市","500116","江津区"),
        ADDRESS_500117("500000","重庆","500100","重庆市","500117","合川区"),
        ADDRESS_500118("500000","重庆","500100","重庆市","500118","永川区"),
        ADDRESS_500119("500000","重庆","500100","重庆市","500119","南川区"),
        ADDRESS_500120("500000","重庆","500100","重庆市","500120","璧山区"),
        ADDRESS_500151("500000","重庆","500100","重庆市","500151","铜梁区"),
        ADDRESS_500152("500000","重庆","500100","重庆市","500152","潼南区"),
        ADDRESS_500153("500000","重庆","500100","重庆市","500153","荣昌区"),
        ADDRESS_500154("500000","重庆","500100","重庆市","500154","开州区"),
        ADDRESS_500155("500000","重庆","500100","重庆市","500155","梁平区"),
        ADDRESS_500156("500000","重庆","500100","重庆市","500156","武隆区"),
        ADDRESS_500229("500000","重庆","500100","重庆市","500229","城口县"),
        ADDRESS_500230("500000","重庆","500100","重庆市","500230","丰都县"),
        ADDRESS_500231("500000","重庆","500100","重庆市","500231","垫江县"),
        ADDRESS_500233("500000","重庆","500100","重庆市","500233","忠县"),
        ADDRESS_500235("500000","重庆","500100","重庆市","500235","云阳县"),
        ADDRESS_500236("500000","重庆","500100","重庆市","500236","奉节县"),
        ADDRESS_500237("500000","重庆","500100","重庆市","500237","巫山县"),
        ADDRESS_500238("500000","重庆","500100","重庆市","500238","巫溪县"),
        ADDRESS_500240("500000","重庆","500100","重庆市","500240","石柱土家族自治县"),
        ADDRESS_500241("500000","重庆","500100","重庆市","500241","秀山土家族苗族自治县"),
        ADDRESS_500242("500000","重庆","500100","重庆市","500242","酉阳土家族苗族自治县"),
        ADDRESS_500243("500000","重庆","500100","重庆市","500243","彭水苗族土家族自治县"),

        ;




        AddressCode500000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode500000Enum addressCodeEnum :AddressCode500000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 四川省枚举
     */
    public enum AddressCode510000Enum {

        ADDRESS_510104("510000","四川省","510100","成都市","510104","锦江区"),
        ADDRESS_510105("510000","四川省","510100","成都市","510105","青羊区"),
        ADDRESS_510106("510000","四川省","510100","成都市","510106","金牛区"),
        ADDRESS_510107("510000","四川省","510100","成都市","510107","武侯区"),
        ADDRESS_510108("510000","四川省","510100","成都市","510108","成华区"),
        ADDRESS_510112("510000","四川省","510100","成都市","510112","龙泉驿区"),
        ADDRESS_510113("510000","四川省","510100","成都市","510113","青白江区"),
        ADDRESS_510114("510000","四川省","510100","成都市","510114","新都区"),
        ADDRESS_510115("510000","四川省","510100","成都市","510115","温江区"),
        ADDRESS_510116("510000","四川省","510100","成都市","510116","双流区"),
        ADDRESS_510117("510000","四川省","510100","成都市","510117","郫都区"),
        ADDRESS_510118("510000","四川省","510100","成都市","510118","新津区"),
        ADDRESS_510121("510000","四川省","510100","成都市","510121","金堂县"),
        ADDRESS_510129("510000","四川省","510100","成都市","510129","大邑县"),
        ADDRESS_510131("510000","四川省","510100","成都市","510131","蒲江县"),
        ADDRESS_510181("510000","四川省","510100","成都市","510181","都江堰市"),
        ADDRESS_510182("510000","四川省","510100","成都市","510182","彭州市"),
        ADDRESS_510183("510000","四川省","510100","成都市","510183","邛崃市"),
        ADDRESS_510184("510000","四川省","510100","成都市","510184","崇州市"),
        ADDRESS_510185("510000","四川省","510100","成都市","510185","简阳市"),
        ADDRESS_510302("510000","四川省","510300","自贡市","510302","自流井区"),
        ADDRESS_510303("510000","四川省","510300","自贡市","510303","贡井区"),
        ADDRESS_510304("510000","四川省","510300","自贡市","510304","大安区"),
        ADDRESS_510311("510000","四川省","510300","自贡市","510311","沿滩区"),
        ADDRESS_510321("510000","四川省","510300","自贡市","510321","荣县"),
        ADDRESS_510322("510000","四川省","510300","自贡市","510322","富顺县"),
        ADDRESS_510402("510000","四川省","510400","攀枝花市","510402","东区"),
        ADDRESS_510403("510000","四川省","510400","攀枝花市","510403","西区"),
        ADDRESS_510411("510000","四川省","510400","攀枝花市","510411","仁和区"),
        ADDRESS_510421("510000","四川省","510400","攀枝花市","510421","米易县"),
        ADDRESS_510422("510000","四川省","510400","攀枝花市","510422","盐边县"),
        ADDRESS_510502("510000","四川省","510500","泸州市","510502","江阳区"),
        ADDRESS_510503("510000","四川省","510500","泸州市","510503","纳溪区"),
        ADDRESS_510504("510000","四川省","510500","泸州市","510504","龙马潭区"),
        ADDRESS_510521("510000","四川省","510500","泸州市","510521","泸县"),
        ADDRESS_510522("510000","四川省","510500","泸州市","510522","合江县"),
        ADDRESS_510524("510000","四川省","510500","泸州市","510524","叙永县"),
        ADDRESS_510525("510000","四川省","510500","泸州市","510525","古蔺县"),
        ADDRESS_510603("510000","四川省","510600","德阳市","510603","旌阳区"),
        ADDRESS_510604("510000","四川省","510600","德阳市","510604","罗江区"),
        ADDRESS_510623("510000","四川省","510600","德阳市","510623","中江县"),
        ADDRESS_510681("510000","四川省","510600","德阳市","510681","广汉市"),
        ADDRESS_510682("510000","四川省","510600","德阳市","510682","什邡市"),
        ADDRESS_510683("510000","四川省","510600","德阳市","510683","绵竹市"),
        ADDRESS_510703("510000","四川省","510700","绵阳市","510703","涪城区"),
        ADDRESS_510704("510000","四川省","510700","绵阳市","510704","游仙区"),
        ADDRESS_510705("510000","四川省","510700","绵阳市","510705","安州区"),
        ADDRESS_510722("510000","四川省","510700","绵阳市","510722","三台县"),
        ADDRESS_510723("510000","四川省","510700","绵阳市","510723","盐亭县"),
        ADDRESS_510725("510000","四川省","510700","绵阳市","510725","梓潼县"),
        ADDRESS_510726("510000","四川省","510700","绵阳市","510726","北川羌族自治县"),
        ADDRESS_510727("510000","四川省","510700","绵阳市","510727","平武县"),
        ADDRESS_510781("510000","四川省","510700","绵阳市","510781","江油市"),
        ADDRESS_510802("510000","四川省","510800","广元市","510802","利州区"),
        ADDRESS_510811("510000","四川省","510800","广元市","510811","昭化区"),
        ADDRESS_510812("510000","四川省","510800","广元市","510812","朝天区"),
        ADDRESS_510821("510000","四川省","510800","广元市","510821","旺苍县"),
        ADDRESS_510822("510000","四川省","510800","广元市","510822","青川县"),
        ADDRESS_510823("510000","四川省","510800","广元市","510823","剑阁县"),
        ADDRESS_510824("510000","四川省","510800","广元市","510824","苍溪县"),
        ADDRESS_510903("510000","四川省","510900","遂宁市","510903","船山区"),
        ADDRESS_510904("510000","四川省","510900","遂宁市","510904","安居区"),
        ADDRESS_510921("510000","四川省","510900","遂宁市","510921","蓬溪县"),
        ADDRESS_510923("510000","四川省","510900","遂宁市","510923","大英县"),
        ADDRESS_510981("510000","四川省","510900","遂宁市","510981","射洪市"),
        ADDRESS_511002("510000","四川省","511000","内江市","511002","市中区"),
        ADDRESS_511011("510000","四川省","511000","内江市","511011","东兴区"),
        ADDRESS_511024("510000","四川省","511000","内江市","511024","威远县"),
        ADDRESS_511025("510000","四川省","511000","内江市","511025","资中县"),
        ADDRESS_511083("510000","四川省","511000","内江市","511083","隆昌市"),
        ADDRESS_511102("510000","四川省","511100","乐山市","511102","市中区"),
        ADDRESS_511111("510000","四川省","511100","乐山市","511111","沙湾区"),
        ADDRESS_511112("510000","四川省","511100","乐山市","511112","五通桥区"),
        ADDRESS_511113("510000","四川省","511100","乐山市","511113","金口河区"),
        ADDRESS_511123("510000","四川省","511100","乐山市","511123","犍为县"),
        ADDRESS_511124("510000","四川省","511100","乐山市","511124","井研县"),
        ADDRESS_511126("510000","四川省","511100","乐山市","511126","夹江县"),
        ADDRESS_511129("510000","四川省","511100","乐山市","511129","沐川县"),
        ADDRESS_511132("510000","四川省","511100","乐山市","511132","峨边彝族自治县"),
        ADDRESS_511133("510000","四川省","511100","乐山市","511133","马边彝族自治县"),
        ADDRESS_511181("510000","四川省","511100","乐山市","511181","峨眉山市"),
        ADDRESS_511302("510000","四川省","511300","南充市","511302","顺庆区"),
        ADDRESS_511303("510000","四川省","511300","南充市","511303","高坪区"),
        ADDRESS_511304("510000","四川省","511300","南充市","511304","嘉陵区"),
        ADDRESS_511321("510000","四川省","511300","南充市","511321","南部县"),
        ADDRESS_511322("510000","四川省","511300","南充市","511322","营山县"),
        ADDRESS_511323("510000","四川省","511300","南充市","511323","蓬安县"),
        ADDRESS_511324("510000","四川省","511300","南充市","511324","仪陇县"),
        ADDRESS_511325("510000","四川省","511300","南充市","511325","西充县"),
        ADDRESS_511381("510000","四川省","511300","南充市","511381","阆中市"),
        ADDRESS_511402("510000","四川省","511400","眉山市","511402","东坡区"),
        ADDRESS_511403("510000","四川省","511400","眉山市","511403","彭山区"),
        ADDRESS_511421("510000","四川省","511400","眉山市","511421","仁寿县"),
        ADDRESS_511423("510000","四川省","511400","眉山市","511423","洪雅县"),
        ADDRESS_511424("510000","四川省","511400","眉山市","511424","丹棱县"),
        ADDRESS_511425("510000","四川省","511400","眉山市","511425","青神县"),
        ADDRESS_511502("510000","四川省","511500","宜宾市","511502","翠屏区"),
        ADDRESS_511503("510000","四川省","511500","宜宾市","511503","南溪区"),
        ADDRESS_511504("510000","四川省","511500","宜宾市","511504","叙州区"),
        ADDRESS_511523("510000","四川省","511500","宜宾市","511523","江安县"),
        ADDRESS_511524("510000","四川省","511500","宜宾市","511524","长宁县"),
        ADDRESS_511525("510000","四川省","511500","宜宾市","511525","高县"),
        ADDRESS_511526("510000","四川省","511500","宜宾市","511526","珙县"),
        ADDRESS_511527("510000","四川省","511500","宜宾市","511527","筠连县"),
        ADDRESS_511528("510000","四川省","511500","宜宾市","511528","兴文县"),
        ADDRESS_511529("510000","四川省","511500","宜宾市","511529","屏山县"),
        ADDRESS_511602("510000","四川省","511600","广安市","511602","广安区"),
        ADDRESS_511603("510000","四川省","511600","广安市","511603","前锋区"),
        ADDRESS_511621("510000","四川省","511600","广安市","511621","岳池县"),
        ADDRESS_511622("510000","四川省","511600","广安市","511622","武胜县"),
        ADDRESS_511623("510000","四川省","511600","广安市","511623","邻水县"),
        ADDRESS_511681("510000","四川省","511600","广安市","511681","华蓥市"),
        ADDRESS_511702("510000","四川省","511700","达州市","511702","通川区"),
        ADDRESS_511703("510000","四川省","511700","达州市","511703","达川区"),
        ADDRESS_511722("510000","四川省","511700","达州市","511722","宣汉县"),
        ADDRESS_511723("510000","四川省","511700","达州市","511723","开江县"),
        ADDRESS_511724("510000","四川省","511700","达州市","511724","大竹县"),
        ADDRESS_511725("510000","四川省","511700","达州市","511725","渠县"),
        ADDRESS_511781("510000","四川省","511700","达州市","511781","万源市"),
        ADDRESS_511802("510000","四川省","511800","雅安市","511802","雨城区"),
        ADDRESS_511803("510000","四川省","511800","雅安市","511803","名山区"),
        ADDRESS_511822("510000","四川省","511800","雅安市","511822","荥经县"),
        ADDRESS_511823("510000","四川省","511800","雅安市","511823","汉源县"),
        ADDRESS_511824("510000","四川省","511800","雅安市","511824","石棉县"),
        ADDRESS_511825("510000","四川省","511800","雅安市","511825","天全县"),
        ADDRESS_511826("510000","四川省","511800","雅安市","511826","芦山县"),
        ADDRESS_511827("510000","四川省","511800","雅安市","511827","宝兴县"),
        ADDRESS_511902("510000","四川省","511900","巴中市","511902","巴州区"),
        ADDRESS_511903("510000","四川省","511900","巴中市","511903","恩阳区"),
        ADDRESS_511921("510000","四川省","511900","巴中市","511921","通江县"),
        ADDRESS_511922("510000","四川省","511900","巴中市","511922","南江县"),
        ADDRESS_511923("510000","四川省","511900","巴中市","511923","平昌县"),
        ADDRESS_512002("510000","四川省","512000","资阳市","512002","雁江区"),
        ADDRESS_512021("510000","四川省","512000","资阳市","512021","安岳县"),
        ADDRESS_512022("510000","四川省","512000","资阳市","512022","乐至县"),
        ADDRESS_513201("510000","四川省","513200","阿坝藏族羌族自治州","513201","马尔康市"),
        ADDRESS_513221("510000","四川省","513200","阿坝藏族羌族自治州","513221","汶川县"),
        ADDRESS_513222("510000","四川省","513200","阿坝藏族羌族自治州","513222","理县"),
        ADDRESS_513223("510000","四川省","513200","阿坝藏族羌族自治州","513223","茂县"),
        ADDRESS_513224("510000","四川省","513200","阿坝藏族羌族自治州","513224","松潘县"),
        ADDRESS_513225("510000","四川省","513200","阿坝藏族羌族自治州","513225","九寨沟县"),
        ADDRESS_513226("510000","四川省","513200","阿坝藏族羌族自治州","513226","金川县"),
        ADDRESS_513227("510000","四川省","513200","阿坝藏族羌族自治州","513227","小金县"),
        ADDRESS_513228("510000","四川省","513200","阿坝藏族羌族自治州","513228","黑水县"),
        ADDRESS_513230("510000","四川省","513200","阿坝藏族羌族自治州","513230","壤塘县"),
        ADDRESS_513231("510000","四川省","513200","阿坝藏族羌族自治州","513231","阿坝县"),
        ADDRESS_513232("510000","四川省","513200","阿坝藏族羌族自治州","513232","若尔盖县"),
        ADDRESS_513233("510000","四川省","513200","阿坝藏族羌族自治州","513233","红原县"),
        ADDRESS_513301("510000","四川省","513300","甘孜藏族自治州","513301","康定市"),
        ADDRESS_513322("510000","四川省","513300","甘孜藏族自治州","513322","泸定县"),
        ADDRESS_513323("510000","四川省","513300","甘孜藏族自治州","513323","丹巴县"),
        ADDRESS_513324("510000","四川省","513300","甘孜藏族自治州","513324","九龙县"),
        ADDRESS_513325("510000","四川省","513300","甘孜藏族自治州","513325","雅江县"),
        ADDRESS_513326("510000","四川省","513300","甘孜藏族自治州","513326","道孚县"),
        ADDRESS_513327("510000","四川省","513300","甘孜藏族自治州","513327","炉霍县"),
        ADDRESS_513328("510000","四川省","513300","甘孜藏族自治州","513328","甘孜县"),
        ADDRESS_513329("510000","四川省","513300","甘孜藏族自治州","513329","新龙县"),
        ADDRESS_513330("510000","四川省","513300","甘孜藏族自治州","513330","德格县"),
        ADDRESS_513331("510000","四川省","513300","甘孜藏族自治州","513331","白玉县"),
        ADDRESS_513332("510000","四川省","513300","甘孜藏族自治州","513332","石渠县"),
        ADDRESS_513333("510000","四川省","513300","甘孜藏族自治州","513333","色达县"),
        ADDRESS_513334("510000","四川省","513300","甘孜藏族自治州","513334","理塘县"),
        ADDRESS_513335("510000","四川省","513300","甘孜藏族自治州","513335","巴塘县"),
        ADDRESS_513336("510000","四川省","513300","甘孜藏族自治州","513336","乡城县"),
        ADDRESS_513337("510000","四川省","513300","甘孜藏族自治州","513337","稻城县"),
        ADDRESS_513338("510000","四川省","513300","甘孜藏族自治州","513338","得荣县"),
        ADDRESS_513401("510000","四川省","513400","凉山彝族自治州","513401","西昌市"),
        ADDRESS_513422("510000","四川省","513400","凉山彝族自治州","513422","木里藏族自治县"),
        ADDRESS_513423("510000","四川省","513400","凉山彝族自治州","513423","盐源县"),
        ADDRESS_513424("510000","四川省","513400","凉山彝族自治州","513424","德昌县"),
        ADDRESS_513426("510000","四川省","513400","凉山彝族自治州","513426","会东县"),
        ADDRESS_513427("510000","四川省","513400","凉山彝族自治州","513427","宁南县"),
        ADDRESS_513428("510000","四川省","513400","凉山彝族自治州","513428","普格县"),
        ADDRESS_513429("510000","四川省","513400","凉山彝族自治州","513429","布拖县"),
        ADDRESS_513430("510000","四川省","513400","凉山彝族自治州","513430","金阳县"),
        ADDRESS_513431("510000","四川省","513400","凉山彝族自治州","513431","昭觉县"),
        ADDRESS_513432("510000","四川省","513400","凉山彝族自治州","513432","喜德县"),
        ADDRESS_513433("510000","四川省","513400","凉山彝族自治州","513433","冕宁县"),
        ADDRESS_513434("510000","四川省","513400","凉山彝族自治州","513434","越西县"),
        ADDRESS_513435("510000","四川省","513400","凉山彝族自治州","513435","甘洛县"),
        ADDRESS_513436("510000","四川省","513400","凉山彝族自治州","513436","美姑县"),
        ADDRESS_513437("510000","四川省","513400","凉山彝族自治州","513437","雷波县"),
        ADDRESS_513481("510000","四川省","513400","凉山彝族自治州","513481","会理市"),

        ;



        AddressCode510000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode510000Enum addressCodeEnum :AddressCode510000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 贵州省
     */
    public enum AddressCode520000Enum {

        ADDRESS_520102("520000","贵州省","520100","贵阳市","520102","南明区"),
        ADDRESS_520103("520000","贵州省","520100","贵阳市","520103","云岩区"),
        ADDRESS_520111("520000","贵州省","520100","贵阳市","520111","花溪区"),
        ADDRESS_520112("520000","贵州省","520100","贵阳市","520112","乌当区"),
        ADDRESS_520113("520000","贵州省","520100","贵阳市","520113","白云区"),
        ADDRESS_520115("520000","贵州省","520100","贵阳市","520115","观山湖区"),
        ADDRESS_520121("520000","贵州省","520100","贵阳市","520121","开阳县"),
        ADDRESS_520122("520000","贵州省","520100","贵阳市","520122","息烽县"),
        ADDRESS_520123("520000","贵州省","520100","贵阳市","520123","修文县"),
        ADDRESS_520181("520000","贵州省","520100","贵阳市","520181","清镇市"),
        ADDRESS_520201("520000","贵州省","520200","六盘水市","520201","钟山区"),
        ADDRESS_520203("520000","贵州省","520200","六盘水市","520203","六枝特区"),
        ADDRESS_520204("520000","贵州省","520200","六盘水市","520204","水城区"),
        ADDRESS_520281("520000","贵州省","520200","六盘水市","520281","盘州市"),
        ADDRESS_520302("520000","贵州省","520300","遵义市","520302","红花岗区"),
        ADDRESS_520303("520000","贵州省","520300","遵义市","520303","汇川区"),
        ADDRESS_520304("520000","贵州省","520300","遵义市","520304","播州区"),
        ADDRESS_520322("520000","贵州省","520300","遵义市","520322","桐梓县"),
        ADDRESS_520323("520000","贵州省","520300","遵义市","520323","绥阳县"),
        ADDRESS_520324("520000","贵州省","520300","遵义市","520324","正安县"),
        ADDRESS_520325("520000","贵州省","520300","遵义市","520325","道真仡佬族苗族自治县"),
        ADDRESS_520326("520000","贵州省","520300","遵义市","520326","务川仡佬族苗族自治县"),
        ADDRESS_520327("520000","贵州省","520300","遵义市","520327","凤冈县"),
        ADDRESS_520328("520000","贵州省","520300","遵义市","520328","湄潭县"),
        ADDRESS_520329("520000","贵州省","520300","遵义市","520329","余庆县"),
        ADDRESS_520330("520000","贵州省","520300","遵义市","520330","习水县"),
        ADDRESS_520381("520000","贵州省","520300","遵义市","520381","赤水市"),
        ADDRESS_520382("520000","贵州省","520300","遵义市","520382","仁怀市"),
        ADDRESS_520402("520000","贵州省","520400","安顺市","520402","西秀区"),
        ADDRESS_520403("520000","贵州省","520400","安顺市","520403","平坝区"),
        ADDRESS_520422("520000","贵州省","520400","安顺市","520422","普定县"),
        ADDRESS_520423("520000","贵州省","520400","安顺市","520423","镇宁布依族苗族自治县"),
        ADDRESS_520424("520000","贵州省","520400","安顺市","520424","关岭布依族苗族自治县"),
        ADDRESS_520425("520000","贵州省","520400","安顺市","520425","紫云苗族布依族自治县"),
        ADDRESS_520502("520000","贵州省","520500","毕节市","520502","七星关区"),
        ADDRESS_520521("520000","贵州省","520500","毕节市","520521","大方县"),
        ADDRESS_520522("520000","贵州省","520500","毕节市","520522","黔西县"),
        ADDRESS_520523("520000","贵州省","520500","毕节市","520523","金沙县"),
        ADDRESS_520524("520000","贵州省","520500","毕节市","520524","织金县"),
        ADDRESS_520525("520000","贵州省","520500","毕节市","520525","纳雍县"),
        ADDRESS_520526("520000","贵州省","520500","毕节市","520526","威宁彝族回族苗族自治县"),
        ADDRESS_520527("520000","贵州省","520500","毕节市","520527","赫章县"),
        ADDRESS_520602("520000","贵州省","520600","铜仁市","520602","碧江区"),
        ADDRESS_520603("520000","贵州省","520600","铜仁市","520603","万山区"),
        ADDRESS_520621("520000","贵州省","520600","铜仁市","520621","江口县"),
        ADDRESS_520622("520000","贵州省","520600","铜仁市","520622","玉屏侗族自治县"),
        ADDRESS_520623("520000","贵州省","520600","铜仁市","520623","石阡县"),
        ADDRESS_520624("520000","贵州省","520600","铜仁市","520624","思南县"),
        ADDRESS_520625("520000","贵州省","520600","铜仁市","520625","印江土家族苗族自治县"),
        ADDRESS_520626("520000","贵州省","520600","铜仁市","520626","德江县"),
        ADDRESS_520627("520000","贵州省","520600","铜仁市","520627","沿河土家族自治县"),
        ADDRESS_520628("520000","贵州省","520600","铜仁市","520628","松桃苗族自治县"),
        ADDRESS_522301("520000","贵州省","522300","黔西南布依族苗族自治州","522301","兴义市"),
        ADDRESS_522302("520000","贵州省","522300","黔西南布依族苗族自治州","522302","兴仁市"),
        ADDRESS_522323("520000","贵州省","522300","黔西南布依族苗族自治州","522323","普安县"),
        ADDRESS_522324("520000","贵州省","522300","黔西南布依族苗族自治州","522324","晴隆县"),
        ADDRESS_522325("520000","贵州省","522300","黔西南布依族苗族自治州","522325","贞丰县"),
        ADDRESS_522326("520000","贵州省","522300","黔西南布依族苗族自治州","522326","望谟县"),
        ADDRESS_522327("520000","贵州省","522300","黔西南布依族苗族自治州","522327","册亨县"),
        ADDRESS_522328("520000","贵州省","522300","黔西南布依族苗族自治州","522328","安龙县"),
        ADDRESS_522601("520000","贵州省","522600","黔东南苗族侗族自治州","522601","凯里市"),
        ADDRESS_522622("520000","贵州省","522600","黔东南苗族侗族自治州","522622","黄平县"),
        ADDRESS_522623("520000","贵州省","522600","黔东南苗族侗族自治州","522623","施秉县"),
        ADDRESS_522624("520000","贵州省","522600","黔东南苗族侗族自治州","522624","三穗县"),
        ADDRESS_522625("520000","贵州省","522600","黔东南苗族侗族自治州","522625","镇远县"),
        ADDRESS_522626("520000","贵州省","522600","黔东南苗族侗族自治州","522626","岑巩县"),
        ADDRESS_522627("520000","贵州省","522600","黔东南苗族侗族自治州","522627","天柱县"),
        ADDRESS_522628("520000","贵州省","522600","黔东南苗族侗族自治州","522628","锦屏县"),
        ADDRESS_522629("520000","贵州省","522600","黔东南苗族侗族自治州","522629","剑河县"),
        ADDRESS_522630("520000","贵州省","522600","黔东南苗族侗族自治州","522630","台江县"),
        ADDRESS_522631("520000","贵州省","522600","黔东南苗族侗族自治州","522631","黎平县"),
        ADDRESS_522632("520000","贵州省","522600","黔东南苗族侗族自治州","522632","榕江县"),
        ADDRESS_522633("520000","贵州省","522600","黔东南苗族侗族自治州","522633","从江县"),
        ADDRESS_522634("520000","贵州省","522600","黔东南苗族侗族自治州","522634","雷山县"),
        ADDRESS_522635("520000","贵州省","522600","黔东南苗族侗族自治州","522635","麻江县"),
        ADDRESS_522636("520000","贵州省","522600","黔东南苗族侗族自治州","522636","丹寨县"),
        ADDRESS_522701("520000","贵州省","522700","黔南布依族苗族自治州","522701","都匀市"),
        ADDRESS_522702("520000","贵州省","522700","黔南布依族苗族自治州","522702","福泉市"),
        ADDRESS_522722("520000","贵州省","522700","黔南布依族苗族自治州","522722","荔波县"),
        ADDRESS_522723("520000","贵州省","522700","黔南布依族苗族自治州","522723","贵定县"),
        ADDRESS_522725("520000","贵州省","522700","黔南布依族苗族自治州","522725","瓮安县"),
        ADDRESS_522726("520000","贵州省","522700","黔南布依族苗族自治州","522726","独山县"),
        ADDRESS_522727("520000","贵州省","522700","黔南布依族苗族自治州","522727","平塘县"),
        ADDRESS_522728("520000","贵州省","522700","黔南布依族苗族自治州","522728","罗甸县"),
        ADDRESS_522729("520000","贵州省","522700","黔南布依族苗族自治州","522729","长顺县"),
        ADDRESS_522730("520000","贵州省","522700","黔南布依族苗族自治州","522730","龙里县"),
        ADDRESS_522731("520000","贵州省","522700","黔南布依族苗族自治州","522731","惠水县"),
        ADDRESS_522732("520000","贵州省","522700","黔南布依族苗族自治州","522732","三都水族自治县"),

        ;



        AddressCode520000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode520000Enum addressCodeEnum :AddressCode520000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 云南省枚举
     */
    public enum AddressCode530000Enum {

        ADDRESS_530102("530000","云南省","530100","昆明市","530102","五华区"),
        ADDRESS_530103("530000","云南省","530100","昆明市","530103","盘龙区"),
        ADDRESS_530111("530000","云南省","530100","昆明市","530111","官渡区"),
        ADDRESS_530112("530000","云南省","530100","昆明市","530112","西山区"),
        ADDRESS_530113("530000","云南省","530100","昆明市","530113","东川区"),
        ADDRESS_530114("530000","云南省","530100","昆明市","530114","呈贡区"),
        ADDRESS_530115("530000","云南省","530100","昆明市","530115","晋宁区"),
        ADDRESS_530124("530000","云南省","530100","昆明市","530124","富民县"),
        ADDRESS_530125("530000","云南省","530100","昆明市","530125","宜良县"),
        ADDRESS_530126("530000","云南省","530100","昆明市","530126","石林彝族自治县"),
        ADDRESS_530127("530000","云南省","530100","昆明市","530127","嵩明县"),
        ADDRESS_530128("530000","云南省","530100","昆明市","530128","禄劝彝族苗族自治县"),
        ADDRESS_530129("530000","云南省","530100","昆明市","530129","寻甸回族彝族自治县"),
        ADDRESS_530181("530000","云南省","530100","昆明市","530181","安宁市"),
        ADDRESS_530302("530000","云南省","530300","曲靖市","530302","麒麟区"),
        ADDRESS_530303("530000","云南省","530300","曲靖市","530303","沾益区"),
        ADDRESS_530304("530000","云南省","530300","曲靖市","530304","马龙区"),
        ADDRESS_530322("530000","云南省","530300","曲靖市","530322","陆良县"),
        ADDRESS_530323("530000","云南省","530300","曲靖市","530323","师宗县"),
        ADDRESS_530324("530000","云南省","530300","曲靖市","530324","罗平县"),
        ADDRESS_530325("530000","云南省","530300","曲靖市","530325","富源县"),
        ADDRESS_530326("530000","云南省","530300","曲靖市","530326","会泽县"),
        ADDRESS_530381("530000","云南省","530300","曲靖市","530381","宣威市"),
        ADDRESS_530402("530000","云南省","530400","玉溪市","530402","红塔区"),
        ADDRESS_530403("530000","云南省","530400","玉溪市","530403","江川区"),
        ADDRESS_530423("530000","云南省","530400","玉溪市","530423","通海县"),
        ADDRESS_530424("530000","云南省","530400","玉溪市","530424","华宁县"),
        ADDRESS_530425("530000","云南省","530400","玉溪市","530425","易门县"),
        ADDRESS_530426("530000","云南省","530400","玉溪市","530426","峨山彝族自治县"),
        ADDRESS_530427("530000","云南省","530400","玉溪市","530427","新平彝族傣族自治县"),
        ADDRESS_530428("530000","云南省","530400","玉溪市","530428","元江哈尼族彝族傣族自治县"),
        ADDRESS_530481("530000","云南省","530400","玉溪市","530481","澄江市"),
        ADDRESS_530502("530000","云南省","530500","保山市","530502","隆阳区"),
        ADDRESS_530521("530000","云南省","530500","保山市","530521","施甸县"),
        ADDRESS_530523("530000","云南省","530500","保山市","530523","龙陵县"),
        ADDRESS_530524("530000","云南省","530500","保山市","530524","昌宁县"),
        ADDRESS_530581("530000","云南省","530500","保山市","530581","腾冲市"),
        ADDRESS_530602("530000","云南省","530600","昭通市","530602","昭阳区"),
        ADDRESS_530621("530000","云南省","530600","昭通市","530621","鲁甸县"),
        ADDRESS_530622("530000","云南省","530600","昭通市","530622","巧家县"),
        ADDRESS_530623("530000","云南省","530600","昭通市","530623","盐津县"),
        ADDRESS_530624("530000","云南省","530600","昭通市","530624","大关县"),
        ADDRESS_530625("530000","云南省","530600","昭通市","530625","永善县"),
        ADDRESS_530626("530000","云南省","530600","昭通市","530626","绥江县"),
        ADDRESS_530627("530000","云南省","530600","昭通市","530627","镇雄县"),
        ADDRESS_530628("530000","云南省","530600","昭通市","530628","彝良县"),
        ADDRESS_530629("530000","云南省","530600","昭通市","530629","威信县"),
        ADDRESS_530681("530000","云南省","530600","昭通市","530681","水富市"),
        ADDRESS_530702("530000","云南省","530700","丽江市","530702","古城区"),
        ADDRESS_530721("530000","云南省","530700","丽江市","530721","玉龙纳西族自治县"),
        ADDRESS_530722("530000","云南省","530700","丽江市","530722","永胜县"),
        ADDRESS_530723("530000","云南省","530700","丽江市","530723","华坪县"),
        ADDRESS_530724("530000","云南省","530700","丽江市","530724","宁蒗彝族自治县"),
        ADDRESS_530802("530000","云南省","530800","普洱市","530802","思茅区"),
        ADDRESS_530821("530000","云南省","530800","普洱市","530821","宁洱哈尼族彝族自治县"),
        ADDRESS_530822("530000","云南省","530800","普洱市","530822","墨江哈尼族自治县"),
        ADDRESS_530823("530000","云南省","530800","普洱市","530823","景东彝族自治县"),
        ADDRESS_530824("530000","云南省","530800","普洱市","530824","景谷傣族彝族自治县"),
        ADDRESS_530825("530000","云南省","530800","普洱市","530825","镇沅彝族哈尼族拉祜族自治县"),
        ADDRESS_530826("530000","云南省","530800","普洱市","530826","江城哈尼族彝族自治县"),
        ADDRESS_530827("530000","云南省","530800","普洱市","530827","孟连傣族拉祜族佤族自治县"),
        ADDRESS_530828("530000","云南省","530800","普洱市","530828","澜沧拉祜族自治县"),
        ADDRESS_530829("530000","云南省","530800","普洱市","530829","西盟佤族自治县"),
        ADDRESS_530902("530000","云南省","530900","临沧市","530902","临翔区"),
        ADDRESS_530921("530000","云南省","530900","临沧市","530921","凤庆县"),
        ADDRESS_530922("530000","云南省","530900","临沧市","530922","云县"),
        ADDRESS_530923("530000","云南省","530900","临沧市","530923","永德县"),
        ADDRESS_530924("530000","云南省","530900","临沧市","530924","镇康县"),
        ADDRESS_530925("530000","云南省","530900","临沧市","530925","双江拉祜族佤族布朗族傣族自治县"),
        ADDRESS_530926("530000","云南省","530900","临沧市","530926","耿马傣族佤族自治县"),
        ADDRESS_530927("530000","云南省","530900","临沧市","530927","沧源佤族自治县"),
        ADDRESS_532301("530000","云南省","532300","楚雄彝族自治州","532301","楚雄市"),
        ADDRESS_532322("530000","云南省","532300","楚雄彝族自治州","532322","双柏县"),
        ADDRESS_532323("530000","云南省","532300","楚雄彝族自治州","532323","牟定县"),
        ADDRESS_532324("530000","云南省","532300","楚雄彝族自治州","532324","南华县"),
        ADDRESS_532325("530000","云南省","532300","楚雄彝族自治州","532325","姚安县"),
        ADDRESS_532326("530000","云南省","532300","楚雄彝族自治州","532326","大姚县"),
        ADDRESS_532327("530000","云南省","532300","楚雄彝族自治州","532327","永仁县"),
        ADDRESS_532328("530000","云南省","532300","楚雄彝族自治州","532328","元谋县"),
        ADDRESS_532329("530000","云南省","532300","楚雄彝族自治州","532329","武定县"),
        ADDRESS_532381("530000","云南省","532300","楚雄彝族自治州","532381","禄丰市"),
        ADDRESS_532501("530000","云南省","532500","红河哈尼族彝族自治州","532501","个旧市"),
        ADDRESS_532502("530000","云南省","532500","红河哈尼族彝族自治州","532502","开远市"),
        ADDRESS_532503("530000","云南省","532500","红河哈尼族彝族自治州","532503","蒙自市"),
        ADDRESS_532504("530000","云南省","532500","红河哈尼族彝族自治州","532504","弥勒市"),
        ADDRESS_532523("530000","云南省","532500","红河哈尼族彝族自治州","532523","屏边苗族自治县"),
        ADDRESS_532524("530000","云南省","532500","红河哈尼族彝族自治州","532524","建水县"),
        ADDRESS_532525("530000","云南省","532500","红河哈尼族彝族自治州","532525","石屏县"),
        ADDRESS_532527("530000","云南省","532500","红河哈尼族彝族自治州","532527","泸西县"),
        ADDRESS_532528("530000","云南省","532500","红河哈尼族彝族自治州","532528","元阳县"),
        ADDRESS_532529("530000","云南省","532500","红河哈尼族彝族自治州","532529","红河县"),
        ADDRESS_532530("530000","云南省","532500","红河哈尼族彝族自治州","532530","金平苗族瑶族傣族自治县"),
        ADDRESS_532531("530000","云南省","532500","红河哈尼族彝族自治州","532531","绿春县"),
        ADDRESS_532532("530000","云南省","532500","红河哈尼族彝族自治州","532532","河口瑶族自治县"),
        ADDRESS_532601("530000","云南省","532600","文山壮族苗族自治州","532601","文山市"),
        ADDRESS_532622("530000","云南省","532600","文山壮族苗族自治州","532622","砚山县"),
        ADDRESS_532623("530000","云南省","532600","文山壮族苗族自治州","532623","西畴县"),
        ADDRESS_532624("530000","云南省","532600","文山壮族苗族自治州","532624","麻栗坡县"),
        ADDRESS_532625("530000","云南省","532600","文山壮族苗族自治州","532625","马关县"),
        ADDRESS_532626("530000","云南省","532600","文山壮族苗族自治州","532626","丘北县"),
        ADDRESS_532627("530000","云南省","532600","文山壮族苗族自治州","532627","广南县"),
        ADDRESS_532628("530000","云南省","532600","文山壮族苗族自治州","532628","富宁县"),
        ADDRESS_532801("530000","云南省","532800","西双版纳傣族自治州","532801","景洪市"),
        ADDRESS_532822("530000","云南省","532800","西双版纳傣族自治州","532822","勐海县"),
        ADDRESS_532823("530000","云南省","532800","西双版纳傣族自治州","532823","勐腊县"),
        ADDRESS_532901("530000","云南省","532900","大理白族自治州","532901","大理市"),
        ADDRESS_532922("530000","云南省","532900","大理白族自治州","532922","漾濞彝族自治县"),
        ADDRESS_532923("530000","云南省","532900","大理白族自治州","532923","祥云县"),
        ADDRESS_532924("530000","云南省","532900","大理白族自治州","532924","宾川县"),
        ADDRESS_532925("530000","云南省","532900","大理白族自治州","532925","弥渡县"),
        ADDRESS_532926("530000","云南省","532900","大理白族自治州","532926","南涧彝族自治县"),
        ADDRESS_532927("530000","云南省","532900","大理白族自治州","532927","巍山彝族回族自治县"),
        ADDRESS_532928("530000","云南省","532900","大理白族自治州","532928","永平县"),
        ADDRESS_532929("530000","云南省","532900","大理白族自治州","532929","云龙县"),
        ADDRESS_532930("530000","云南省","532900","大理白族自治州","532930","洱源县"),
        ADDRESS_532931("530000","云南省","532900","大理白族自治州","532931","剑川县"),
        ADDRESS_532932("530000","云南省","532900","大理白族自治州","532932","鹤庆县"),
        ADDRESS_533102("530000","云南省","533100","德宏傣族景颇族自治州","533102","瑞丽市"),
        ADDRESS_533103("530000","云南省","533100","德宏傣族景颇族自治州","533103","芒市"),
        ADDRESS_533122("530000","云南省","533100","德宏傣族景颇族自治州","533122","梁河县"),
        ADDRESS_533123("530000","云南省","533100","德宏傣族景颇族自治州","533123","盈江县"),
        ADDRESS_533124("530000","云南省","533100","德宏傣族景颇族自治州","533124","陇川县"),
        ADDRESS_533301("530000","云南省","533300","怒江傈僳族自治州","533301","泸水市"),
        ADDRESS_533323("530000","云南省","533300","怒江傈僳族自治州","533323","福贡县"),
        ADDRESS_533324("530000","云南省","533300","怒江傈僳族自治州","533324","贡山独龙族怒族自治县"),
        ADDRESS_533325("530000","云南省","533300","怒江傈僳族自治州","533325","兰坪白族普米族自治县"),
        ADDRESS_533401("530000","云南省","533400","迪庆藏族自治州","533401","香格里拉市"),
        ADDRESS_533422("530000","云南省","533400","迪庆藏族自治州","533422","德钦县"),
        ADDRESS_533423("530000","云南省","533400","迪庆藏族自治州","533423","维西傈僳族自治县"),

        ;



        AddressCode530000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode530000Enum addressCodeEnum :AddressCode530000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 西藏自治区枚举
     */
    public enum AddressCode540000Enum {

        ADDRESS_540102("540000","西藏自治区","540100","拉萨市","540102","城关区"),
        ADDRESS_540103("540000","西藏自治区","540100","拉萨市","540103","堆龙德庆区"),
        ADDRESS_540104("540000","西藏自治区","540100","拉萨市","540104","达孜区"),
        ADDRESS_540121("540000","西藏自治区","540100","拉萨市","540121","林周县"),
        ADDRESS_540122("540000","西藏自治区","540100","拉萨市","540122","当雄县"),
        ADDRESS_540123("540000","西藏自治区","540100","拉萨市","540123","尼木县"),
        ADDRESS_540124("540000","西藏自治区","540100","拉萨市","540124","曲水县"),
        ADDRESS_540127("540000","西藏自治区","540100","拉萨市","540127","墨竹工卡县"),
        ADDRESS_540202("540000","西藏自治区","540200","日喀则市","540202","桑珠孜区"),
        ADDRESS_540221("540000","西藏自治区","540200","日喀则市","540221","南木林县"),
        ADDRESS_540222("540000","西藏自治区","540200","日喀则市","540222","江孜县"),
        ADDRESS_540223("540000","西藏自治区","540200","日喀则市","540223","定日县"),
        ADDRESS_540224("540000","西藏自治区","540200","日喀则市","540224","萨迦县"),
        ADDRESS_540225("540000","西藏自治区","540200","日喀则市","540225","拉孜县"),
        ADDRESS_540226("540000","西藏自治区","540200","日喀则市","540226","昂仁县"),
        ADDRESS_540227("540000","西藏自治区","540200","日喀则市","540227","谢通门县"),
        ADDRESS_540228("540000","西藏自治区","540200","日喀则市","540228","白朗县"),
        ADDRESS_540229("540000","西藏自治区","540200","日喀则市","540229","仁布县"),
        ADDRESS_540230("540000","西藏自治区","540200","日喀则市","540230","康马县"),
        ADDRESS_540231("540000","西藏自治区","540200","日喀则市","540231","定结县"),
        ADDRESS_540232("540000","西藏自治区","540200","日喀则市","540232","仲巴县"),
        ADDRESS_540233("540000","西藏自治区","540200","日喀则市","540233","亚东县"),
        ADDRESS_540234("540000","西藏自治区","540200","日喀则市","540234","吉隆县"),
        ADDRESS_540235("540000","西藏自治区","540200","日喀则市","540235","聂拉木县"),
        ADDRESS_540236("540000","西藏自治区","540200","日喀则市","540236","萨嘎县"),
        ADDRESS_540237("540000","西藏自治区","540200","日喀则市","540237","岗巴县"),
        ADDRESS_540302("540000","西藏自治区","540300","昌都市","540302","卡若区"),
        ADDRESS_540321("540000","西藏自治区","540300","昌都市","540321","江达县"),
        ADDRESS_540322("540000","西藏自治区","540300","昌都市","540322","贡觉县"),
        ADDRESS_540323("540000","西藏自治区","540300","昌都市","540323","类乌齐县"),
        ADDRESS_540324("540000","西藏自治区","540300","昌都市","540324","丁青县"),
        ADDRESS_540325("540000","西藏自治区","540300","昌都市","540325","察雅县"),
        ADDRESS_540326("540000","西藏自治区","540300","昌都市","540326","八宿县"),
        ADDRESS_540327("540000","西藏自治区","540300","昌都市","540327","左贡县"),
        ADDRESS_540328("540000","西藏自治区","540300","昌都市","540328","芒康县"),
        ADDRESS_540329("540000","西藏自治区","540300","昌都市","540329","洛隆县"),
        ADDRESS_540330("540000","西藏自治区","540300","昌都市","540330","边坝县"),
        ADDRESS_540402("540000","西藏自治区","540400","林芝市","540402","巴宜区"),
        ADDRESS_540421("540000","西藏自治区","540400","林芝市","540421","工布江达县"),
        ADDRESS_540422("540000","西藏自治区","540400","林芝市","540422","米林县"),
        ADDRESS_540423("540000","西藏自治区","540400","林芝市","540423","墨脱县"),
        ADDRESS_540424("540000","西藏自治区","540400","林芝市","540424","波密县"),
        ADDRESS_540425("540000","西藏自治区","540400","林芝市","540425","察隅县"),
        ADDRESS_540426("540000","西藏自治区","540400","林芝市","540426","朗县"),
        ADDRESS_540502("540000","西藏自治区","540500","山南市","540502","乃东区"),
        ADDRESS_540521("540000","西藏自治区","540500","山南市","540521","扎囊县"),
        ADDRESS_540522("540000","西藏自治区","540500","山南市","540522","贡嘎县"),
        ADDRESS_540523("540000","西藏自治区","540500","山南市","540523","桑日县"),
        ADDRESS_540524("540000","西藏自治区","540500","山南市","540524","琼结县"),
        ADDRESS_540525("540000","西藏自治区","540500","山南市","540525","曲松县"),
        ADDRESS_540526("540000","西藏自治区","540500","山南市","540526","措美县"),
        ADDRESS_540527("540000","西藏自治区","540500","山南市","540527","洛扎县"),
        ADDRESS_540528("540000","西藏自治区","540500","山南市","540528","加查县"),
        ADDRESS_540529("540000","西藏自治区","540500","山南市","540529","隆子县"),
        ADDRESS_540530("540000","西藏自治区","540500","山南市","540530","错那县"),
        ADDRESS_540531("540000","西藏自治区","540500","山南市","540531","浪卡子县"),
        ADDRESS_540602("540000","西藏自治区","540600","那曲市","540602","色尼区"),
        ADDRESS_540621("540000","西藏自治区","540600","那曲市","540621","嘉黎县"),
        ADDRESS_540622("540000","西藏自治区","540600","那曲市","540622","比如县"),
        ADDRESS_540623("540000","西藏自治区","540600","那曲市","540623","聂荣县"),
        ADDRESS_540624("540000","西藏自治区","540600","那曲市","540624","安多县"),
        ADDRESS_540625("540000","西藏自治区","540600","那曲市","540625","申扎县"),
        ADDRESS_540626("540000","西藏自治区","540600","那曲市","540626","索县"),
        ADDRESS_540627("540000","西藏自治区","540600","那曲市","540627","班戈县"),
        ADDRESS_540628("540000","西藏自治区","540600","那曲市","540628","巴青县"),
        ADDRESS_540629("540000","西藏自治区","540600","那曲市","540629","尼玛县"),
        ADDRESS_540630("540000","西藏自治区","540600","那曲市","540630","双湖县"),
        ADDRESS_542521("540000","西藏自治区","542500","阿里地区","542521","普兰县"),
        ADDRESS_542522("540000","西藏自治区","542500","阿里地区","542522","札达县"),
        ADDRESS_542523("540000","西藏自治区","542500","阿里地区","542523","噶尔县"),
        ADDRESS_542524("540000","西藏自治区","542500","阿里地区","542524","日土县"),
        ADDRESS_542525("540000","西藏自治区","542500","阿里地区","542525","革吉县"),
        ADDRESS_542526("540000","西藏自治区","542500","阿里地区","542526","改则县"),
        ADDRESS_542527("540000","西藏自治区","542500","阿里地区","542527","措勤县"),

        ;



        AddressCode540000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode540000Enum addressCodeEnum :AddressCode540000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 陕西省枚举
     */
    public enum AddressCode610000Enum {

        ADDRESS_610102("610000","陕西省","610100","西安市","610102","新城区"),
        ADDRESS_610103("610000","陕西省","610100","西安市","610103","碑林区"),
        ADDRESS_610104("610000","陕西省","610100","西安市","610104","莲湖区"),
        ADDRESS_610111("610000","陕西省","610100","西安市","610111","灞桥区"),
        ADDRESS_610112("610000","陕西省","610100","西安市","610112","未央区"),
        ADDRESS_610113("610000","陕西省","610100","西安市","610113","雁塔区"),
        ADDRESS_610114("610000","陕西省","610100","西安市","610114","阎良区"),
        ADDRESS_610115("610000","陕西省","610100","西安市","610115","临潼区"),
        ADDRESS_610116("610000","陕西省","610100","西安市","610116","长安区"),
        ADDRESS_610117("610000","陕西省","610100","西安市","610117","高陵区"),
        ADDRESS_610118("610000","陕西省","610100","西安市","610118","鄠邑区"),
        ADDRESS_610122("610000","陕西省","610100","西安市","610122","蓝田县"),
        ADDRESS_610124("610000","陕西省","610100","西安市","610124","周至县"),
        ADDRESS_610202("610000","陕西省","610200","铜川市","610202","王益区"),
        ADDRESS_610203("610000","陕西省","610200","铜川市","610203","印台区"),
        ADDRESS_610204("610000","陕西省","610200","铜川市","610204","耀州区"),
        ADDRESS_610222("610000","陕西省","610200","铜川市","610222","宜君县"),
        ADDRESS_610302("610000","陕西省","610300","宝鸡市","610302","渭滨区"),
        ADDRESS_610303("610000","陕西省","610300","宝鸡市","610303","金台区"),
        ADDRESS_610304("610000","陕西省","610300","宝鸡市","610304","陈仓区"),
        ADDRESS_610305("610000","陕西省","610300","宝鸡市","610305","凤翔区"),
        ADDRESS_610323("610000","陕西省","610300","宝鸡市","610323","岐山县"),
        ADDRESS_610324("610000","陕西省","610300","宝鸡市","610324","扶风县"),
        ADDRESS_610326("610000","陕西省","610300","宝鸡市","610326","眉县"),
        ADDRESS_610327("610000","陕西省","610300","宝鸡市","610327","陇县"),
        ADDRESS_610328("610000","陕西省","610300","宝鸡市","610328","千阳县"),
        ADDRESS_610329("610000","陕西省","610300","宝鸡市","610329","麟游县"),
        ADDRESS_610330("610000","陕西省","610300","宝鸡市","610330","凤县"),
        ADDRESS_610331("610000","陕西省","610300","宝鸡市","610331","太白县"),
        ADDRESS_610402("610000","陕西省","610400","咸阳市","610402","秦都区"),
        ADDRESS_610403("610000","陕西省","610400","咸阳市","610403","杨陵区"),
        ADDRESS_610404("610000","陕西省","610400","咸阳市","610404","渭城区"),
        ADDRESS_610422("610000","陕西省","610400","咸阳市","610422","三原县"),
        ADDRESS_610423("610000","陕西省","610400","咸阳市","610423","泾阳县"),
        ADDRESS_610424("610000","陕西省","610400","咸阳市","610424","乾县"),
        ADDRESS_610425("610000","陕西省","610400","咸阳市","610425","礼泉县"),
        ADDRESS_610426("610000","陕西省","610400","咸阳市","610426","永寿县"),
        ADDRESS_610428("610000","陕西省","610400","咸阳市","610428","长武县"),
        ADDRESS_610429("610000","陕西省","610400","咸阳市","610429","旬邑县"),
        ADDRESS_610430("610000","陕西省","610400","咸阳市","610430","淳化县"),
        ADDRESS_610431("610000","陕西省","610400","咸阳市","610431","武功县"),
        ADDRESS_610481("610000","陕西省","610400","咸阳市","610481","兴平市"),
        ADDRESS_610482("610000","陕西省","610400","咸阳市","610482","彬州市"),
        ADDRESS_610502("610000","陕西省","610500","渭南市","610502","临渭区"),
        ADDRESS_610503("610000","陕西省","610500","渭南市","610503","华州区"),
        ADDRESS_610522("610000","陕西省","610500","渭南市","610522","潼关县"),
        ADDRESS_610523("610000","陕西省","610500","渭南市","610523","大荔县"),
        ADDRESS_610524("610000","陕西省","610500","渭南市","610524","合阳县"),
        ADDRESS_610525("610000","陕西省","610500","渭南市","610525","澄城县"),
        ADDRESS_610526("610000","陕西省","610500","渭南市","610526","蒲城县"),
        ADDRESS_610527("610000","陕西省","610500","渭南市","610527","白水县"),
        ADDRESS_610528("610000","陕西省","610500","渭南市","610528","富平县"),
        ADDRESS_610581("610000","陕西省","610500","渭南市","610581","韩城市"),
        ADDRESS_610582("610000","陕西省","610500","渭南市","610582","华阴市"),
        ADDRESS_610602("610000","陕西省","610600","延安市","610602","宝塔区"),
        ADDRESS_610603("610000","陕西省","610600","延安市","610603","安塞区"),
        ADDRESS_610621("610000","陕西省","610600","延安市","610621","延长县"),
        ADDRESS_610622("610000","陕西省","610600","延安市","610622","延川县"),
        ADDRESS_610625("610000","陕西省","610600","延安市","610625","志丹县"),
        ADDRESS_610626("610000","陕西省","610600","延安市","610626","吴起县"),
        ADDRESS_610627("610000","陕西省","610600","延安市","610627","甘泉县"),
        ADDRESS_610628("610000","陕西省","610600","延安市","610628","富县"),
        ADDRESS_610629("610000","陕西省","610600","延安市","610629","洛川县"),
        ADDRESS_610630("610000","陕西省","610600","延安市","610630","宜川县"),
        ADDRESS_610631("610000","陕西省","610600","延安市","610631","黄龙县"),
        ADDRESS_610632("610000","陕西省","610600","延安市","610632","黄陵县"),
        ADDRESS_610681("610000","陕西省","610600","延安市","610681","子长市"),
        ADDRESS_610702("610000","陕西省","610700","汉中市","610702","汉台区"),
        ADDRESS_610703("610000","陕西省","610700","汉中市","610703","南郑区"),
        ADDRESS_610722("610000","陕西省","610700","汉中市","610722","城固县"),
        ADDRESS_610723("610000","陕西省","610700","汉中市","610723","洋县"),
        ADDRESS_610724("610000","陕西省","610700","汉中市","610724","西乡县"),
        ADDRESS_610725("610000","陕西省","610700","汉中市","610725","勉县"),
        ADDRESS_610726("610000","陕西省","610700","汉中市","610726","宁强县"),
        ADDRESS_610727("610000","陕西省","610700","汉中市","610727","略阳县"),
        ADDRESS_610728("610000","陕西省","610700","汉中市","610728","镇巴县"),
        ADDRESS_610729("610000","陕西省","610700","汉中市","610729","留坝县"),
        ADDRESS_610730("610000","陕西省","610700","汉中市","610730","佛坪县"),
        ADDRESS_610802("610000","陕西省","610800","榆林市","610802","榆阳区"),
        ADDRESS_610803("610000","陕西省","610800","榆林市","610803","横山区"),
        ADDRESS_610822("610000","陕西省","610800","榆林市","610822","府谷县"),
        ADDRESS_610824("610000","陕西省","610800","榆林市","610824","靖边县"),
        ADDRESS_610825("610000","陕西省","610800","榆林市","610825","定边县"),
        ADDRESS_610826("610000","陕西省","610800","榆林市","610826","绥德县"),
        ADDRESS_610827("610000","陕西省","610800","榆林市","610827","米脂县"),
        ADDRESS_610828("610000","陕西省","610800","榆林市","610828","佳县"),
        ADDRESS_610829("610000","陕西省","610800","榆林市","610829","吴堡县"),
        ADDRESS_610830("610000","陕西省","610800","榆林市","610830","清涧县"),
        ADDRESS_610831("610000","陕西省","610800","榆林市","610831","子洲县"),
        ADDRESS_610881("610000","陕西省","610800","榆林市","610881","神木市"),
        ADDRESS_610902("610000","陕西省","610900","安康市","610902","汉滨区"),
        ADDRESS_610921("610000","陕西省","610900","安康市","610921","汉阴县"),
        ADDRESS_610922("610000","陕西省","610900","安康市","610922","石泉县"),
        ADDRESS_610923("610000","陕西省","610900","安康市","610923","宁陕县"),
        ADDRESS_610924("610000","陕西省","610900","安康市","610924","紫阳县"),
        ADDRESS_610925("610000","陕西省","610900","安康市","610925","岚皋县"),
        ADDRESS_610926("610000","陕西省","610900","安康市","610926","平利县"),
        ADDRESS_610927("610000","陕西省","610900","安康市","610927","镇坪县"),
        ADDRESS_610929("610000","陕西省","610900","安康市","610929","白河县"),
        ADDRESS_610981("610000","陕西省","610900","安康市","610981","旬阳市"),
        ADDRESS_611002("610000","陕西省","611000","商洛市","611002","商州区"),
        ADDRESS_611021("610000","陕西省","611000","商洛市","611021","洛南县"),
        ADDRESS_611022("610000","陕西省","611000","商洛市","611022","丹凤县"),
        ADDRESS_611023("610000","陕西省","611000","商洛市","611023","商南县"),
        ADDRESS_611024("610000","陕西省","611000","商洛市","611024","山阳县"),
        ADDRESS_611025("610000","陕西省","611000","商洛市","611025","镇安县"),
        ADDRESS_611026("610000","陕西省","611000","商洛市","611026","柞水县"),

        ;



        AddressCode610000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode610000Enum addressCodeEnum :AddressCode610000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }


    /**
     * 省编码 甘肃省枚举
     */
    public enum AddressCode620000Enum {

        ADDRESS_620102("620000","甘肃省","620100","兰州市","620102","城关区"),
        ADDRESS_620103("620000","甘肃省","620100","兰州市","620103","七里河区"),
        ADDRESS_620104("620000","甘肃省","620100","兰州市","620104","西固区"),
        ADDRESS_620105("620000","甘肃省","620100","兰州市","620105","安宁区"),
        ADDRESS_620111("620000","甘肃省","620100","兰州市","620111","红古区"),
        ADDRESS_620121("620000","甘肃省","620100","兰州市","620121","永登县"),
        ADDRESS_620122("620000","甘肃省","620100","兰州市","620122","皋兰县"),
        ADDRESS_620123("620000","甘肃省","620100","兰州市","620123","榆中县"),
        ADDRESS_620201("620000","甘肃省","620200","嘉峪关市","620201","嘉峪关市"),
        ADDRESS_620202("620000","甘肃省","620200","嘉峪关市","620202","新城镇"),
        ADDRESS_620203("620000","甘肃省","620200","嘉峪关市","620203","雄关街道"),
        ADDRESS_620204("620000","甘肃省","620200","嘉峪关市","620204","钢城街道"),
        ADDRESS_620205("620000","甘肃省","620200","嘉峪关市","620205","雄关区"),
        ADDRESS_620206("620000","甘肃省","620200","嘉峪关市","620206","镜铁区"),
        ADDRESS_620207("620000","甘肃省","620200","嘉峪关市","620207","文殊镇"),
        ADDRESS_620208("620000","甘肃省","620200","嘉峪关市","620208","峪泉镇"),
        ADDRESS_620302("620000","甘肃省","620300","金昌市","620302","金川区"),
        ADDRESS_620321("620000","甘肃省","620300","金昌市","620321","永昌县"),
        ADDRESS_620402("620000","甘肃省","620400","白银市","620402","白银区"),
        ADDRESS_620403("620000","甘肃省","620400","白银市","620403","平川区"),
        ADDRESS_620421("620000","甘肃省","620400","白银市","620421","靖远县"),
        ADDRESS_620422("620000","甘肃省","620400","白银市","620422","会宁县"),
        ADDRESS_620423("620000","甘肃省","620400","白银市","620423","景泰县"),
        ADDRESS_620502("620000","甘肃省","620500","天水市","620502","秦州区"),
        ADDRESS_620503("620000","甘肃省","620500","天水市","620503","麦积区"),
        ADDRESS_620521("620000","甘肃省","620500","天水市","620521","清水县"),
        ADDRESS_620522("620000","甘肃省","620500","天水市","620522","秦安县"),
        ADDRESS_620523("620000","甘肃省","620500","天水市","620523","甘谷县"),
        ADDRESS_620524("620000","甘肃省","620500","天水市","620524","武山县"),
        ADDRESS_620525("620000","甘肃省","620500","天水市","620525","张家川回族自治县"),
        ADDRESS_620602("620000","甘肃省","620600","武威市","620602","凉州区"),
        ADDRESS_620621("620000","甘肃省","620600","武威市","620621","民勤县"),
        ADDRESS_620622("620000","甘肃省","620600","武威市","620622","古浪县"),
        ADDRESS_620623("620000","甘肃省","620600","武威市","620623","天祝藏族自治县"),
        ADDRESS_620702("620000","甘肃省","620700","张掖市","620702","甘州区"),
        ADDRESS_620721("620000","甘肃省","620700","张掖市","620721","肃南裕固族自治县"),
        ADDRESS_620722("620000","甘肃省","620700","张掖市","620722","民乐县"),
        ADDRESS_620723("620000","甘肃省","620700","张掖市","620723","临泽县"),
        ADDRESS_620724("620000","甘肃省","620700","张掖市","620724","高台县"),
        ADDRESS_620725("620000","甘肃省","620700","张掖市","620725","山丹县"),
        ADDRESS_620802("620000","甘肃省","620800","平凉市","620802","崆峒区"),
        ADDRESS_620821("620000","甘肃省","620800","平凉市","620821","泾川县"),
        ADDRESS_620822("620000","甘肃省","620800","平凉市","620822","灵台县"),
        ADDRESS_620823("620000","甘肃省","620800","平凉市","620823","崇信县"),
        ADDRESS_620825("620000","甘肃省","620800","平凉市","620825","庄浪县"),
        ADDRESS_620826("620000","甘肃省","620800","平凉市","620826","静宁县"),
        ADDRESS_620881("620000","甘肃省","620800","平凉市","620881","华亭市"),
        ADDRESS_620902("620000","甘肃省","620900","酒泉市","620902","肃州区"),
        ADDRESS_620921("620000","甘肃省","620900","酒泉市","620921","金塔县"),
        ADDRESS_620922("620000","甘肃省","620900","酒泉市","620922","瓜州县"),
        ADDRESS_620923("620000","甘肃省","620900","酒泉市","620923","肃北蒙古族自治县"),
        ADDRESS_620924("620000","甘肃省","620900","酒泉市","620924","阿克塞哈萨克族自治县"),
        ADDRESS_620981("620000","甘肃省","620900","酒泉市","620981","玉门市"),
        ADDRESS_620982("620000","甘肃省","620900","酒泉市","620982","敦煌市"),
        ADDRESS_621002("620000","甘肃省","621000","庆阳市","621002","西峰区"),
        ADDRESS_621021("620000","甘肃省","621000","庆阳市","621021","庆城县"),
        ADDRESS_621022("620000","甘肃省","621000","庆阳市","621022","环县"),
        ADDRESS_621023("620000","甘肃省","621000","庆阳市","621023","华池县"),
        ADDRESS_621024("620000","甘肃省","621000","庆阳市","621024","合水县"),
        ADDRESS_621025("620000","甘肃省","621000","庆阳市","621025","正宁县"),
        ADDRESS_621026("620000","甘肃省","621000","庆阳市","621026","宁县"),
        ADDRESS_621027("620000","甘肃省","621000","庆阳市","621027","镇原县"),
        ADDRESS_621102("620000","甘肃省","621100","定西市","621102","安定区"),
        ADDRESS_621121("620000","甘肃省","621100","定西市","621121","通渭县"),
        ADDRESS_621122("620000","甘肃省","621100","定西市","621122","陇西县"),
        ADDRESS_621123("620000","甘肃省","621100","定西市","621123","渭源县"),
        ADDRESS_621124("620000","甘肃省","621100","定西市","621124","临洮县"),
        ADDRESS_621125("620000","甘肃省","621100","定西市","621125","漳县"),
        ADDRESS_621126("620000","甘肃省","621100","定西市","621126","岷县"),
        ADDRESS_621202("620000","甘肃省","621200","陇南市","621202","武都区"),
        ADDRESS_621221("620000","甘肃省","621200","陇南市","621221","成县"),
        ADDRESS_621222("620000","甘肃省","621200","陇南市","621222","文县"),
        ADDRESS_621223("620000","甘肃省","621200","陇南市","621223","宕昌县"),
        ADDRESS_621224("620000","甘肃省","621200","陇南市","621224","康县"),
        ADDRESS_621225("620000","甘肃省","621200","陇南市","621225","西和县"),
        ADDRESS_621226("620000","甘肃省","621200","陇南市","621226","礼县"),
        ADDRESS_621227("620000","甘肃省","621200","陇南市","621227","徽县"),
        ADDRESS_621228("620000","甘肃省","621200","陇南市","621228","两当县"),
        ADDRESS_622901("620000","甘肃省","622900","临夏回族自治州","622901","临夏市"),
        ADDRESS_622921("620000","甘肃省","622900","临夏回族自治州","622921","临夏县"),
        ADDRESS_622922("620000","甘肃省","622900","临夏回族自治州","622922","康乐县"),
        ADDRESS_622923("620000","甘肃省","622900","临夏回族自治州","622923","永靖县"),
        ADDRESS_622924("620000","甘肃省","622900","临夏回族自治州","622924","广河县"),
        ADDRESS_622925("620000","甘肃省","622900","临夏回族自治州","622925","和政县"),
        ADDRESS_622926("620000","甘肃省","622900","临夏回族自治州","622926","东乡族自治县"),
        ADDRESS_622927("620000","甘肃省","622900","临夏回族自治州","622927","积石山保安族东乡族撒拉族自治县"),
        ADDRESS_623001("620000","甘肃省","623000","甘南藏族自治州","623001","合作市"),
        ADDRESS_623021("620000","甘肃省","623000","甘南藏族自治州","623021","临潭县"),
        ADDRESS_623022("620000","甘肃省","623000","甘南藏族自治州","623022","卓尼县"),
        ADDRESS_623023("620000","甘肃省","623000","甘南藏族自治州","623023","舟曲县"),
        ADDRESS_623024("620000","甘肃省","623000","甘南藏族自治州","623024","迭部县"),
        ADDRESS_623025("620000","甘肃省","623000","甘南藏族自治州","623025","玛曲县"),
        ADDRESS_623026("620000","甘肃省","623000","甘南藏族自治州","623026","碌曲县"),
        ADDRESS_623027("620000","甘肃省","623000","甘南藏族自治州","623027","夏河县"),

        ;



        AddressCode620000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode620000Enum addressCodeEnum :AddressCode620000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }

    }

    /**
     * 省编码 宁夏回族自治区枚举
     */
    public enum AddressCode640000Enum {

        ADDRESS_640104("640000","宁夏回族自治区","640100","银川市","640104","兴庆区"),
        ADDRESS_640105("640000","宁夏回族自治区","640100","银川市","640105","西夏区"),
        ADDRESS_640106("640000","宁夏回族自治区","640100","银川市","640106","金凤区"),
        ADDRESS_640121("640000","宁夏回族自治区","640100","银川市","640121","永宁县"),
        ADDRESS_640122("640000","宁夏回族自治区","640100","银川市","640122","贺兰县"),
        ADDRESS_640181("640000","宁夏回族自治区","640100","银川市","640181","灵武市"),
        ADDRESS_640202("640000","宁夏回族自治区","640200","石嘴山市","640202","大武口区"),
        ADDRESS_640205("640000","宁夏回族自治区","640200","石嘴山市","640205","惠农区"),
        ADDRESS_640221("640000","宁夏回族自治区","640200","石嘴山市","640221","平罗县"),
        ADDRESS_640302("640000","宁夏回族自治区","640300","吴忠市","640302","利通区"),
        ADDRESS_640303("640000","宁夏回族自治区","640300","吴忠市","640303","红寺堡区"),
        ADDRESS_640323("640000","宁夏回族自治区","640300","吴忠市","640323","盐池县"),
        ADDRESS_640324("640000","宁夏回族自治区","640300","吴忠市","640324","同心县"),
        ADDRESS_640381("640000","宁夏回族自治区","640300","吴忠市","640381","青铜峡市"),
        ADDRESS_640402("640000","宁夏回族自治区","640400","固原市","640402","原州区"),
        ADDRESS_640422("640000","宁夏回族自治区","640400","固原市","640422","西吉县"),
        ADDRESS_640423("640000","宁夏回族自治区","640400","固原市","640423","隆德县"),
        ADDRESS_640424("640000","宁夏回族自治区","640400","固原市","640424","泾源县"),
        ADDRESS_640425("640000","宁夏回族自治区","640400","固原市","640425","彭阳县"),
        ADDRESS_640502("640000","宁夏回族自治区","640500","中卫市","640502","沙坡头区"),
        ADDRESS_640521("640000","宁夏回族自治区","640500","中卫市","640521","中宁县"),
        ADDRESS_640522("640000","宁夏回族自治区","640500","中卫市","640522","海原县"),

        ;



        AddressCode640000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode640000Enum addressCodeEnum :AddressCode640000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 青海省枚举
     */
    public enum AddressCode630000Enum {

        ADDRESS_630102("630000","青海省","630100","西宁市","630102","城东区"),
        ADDRESS_630103("630000","青海省","630100","西宁市","630103","城中区"),
        ADDRESS_630104("630000","青海省","630100","西宁市","630104","城西区"),
        ADDRESS_630105("630000","青海省","630100","西宁市","630105","城北区"),
        ADDRESS_630106("630000","青海省","630100","西宁市","630106","湟中区"),
        ADDRESS_630121("630000","青海省","630100","西宁市","630121","大通回族土族自治县"),
        ADDRESS_630123("630000","青海省","630100","西宁市","630123","湟源县"),
        ADDRESS_630202("630000","青海省","630200","海东市","630202","乐都区"),
        ADDRESS_630203("630000","青海省","630200","海东市","630203","平安区"),
        ADDRESS_630222("630000","青海省","630200","海东市","630222","民和回族土族自治县"),
        ADDRESS_630223("630000","青海省","630200","海东市","630223","互助土族自治县"),
        ADDRESS_630224("630000","青海省","630200","海东市","630224","化隆回族自治县"),
        ADDRESS_630225("630000","青海省","630200","海东市","630225","循化撒拉族自治县"),
        ADDRESS_632221("630000","青海省","632200","海北藏族自治州","632221","门源回族自治县"),
        ADDRESS_632222("630000","青海省","632200","海北藏族自治州","632222","祁连县"),
        ADDRESS_632223("630000","青海省","632200","海北藏族自治州","632223","海晏县"),
        ADDRESS_632224("630000","青海省","632200","海北藏族自治州","632224","刚察县"),
        ADDRESS_632301("630000","青海省","632300","黄南藏族自治州","632301","同仁市"),
        ADDRESS_632322("630000","青海省","632300","黄南藏族自治州","632322","尖扎县"),
        ADDRESS_632323("630000","青海省","632300","黄南藏族自治州","632323","泽库县"),
        ADDRESS_632324("630000","青海省","632300","黄南藏族自治州","632324","河南蒙古族自治县"),
        ADDRESS_632521("630000","青海省","632500","海南藏族自治州","632521","共和县"),
        ADDRESS_632522("630000","青海省","632500","海南藏族自治州","632522","同德县"),
        ADDRESS_632523("630000","青海省","632500","海南藏族自治州","632523","贵德县"),
        ADDRESS_632524("630000","青海省","632500","海南藏族自治州","632524","兴海县"),
        ADDRESS_632525("630000","青海省","632500","海南藏族自治州","632525","贵南县"),
        ADDRESS_632621("630000","青海省","632600","果洛藏族自治州","632621","玛沁县"),
        ADDRESS_632622("630000","青海省","632600","果洛藏族自治州","632622","班玛县"),
        ADDRESS_632623("630000","青海省","632600","果洛藏族自治州","632623","甘德县"),
        ADDRESS_632624("630000","青海省","632600","果洛藏族自治州","632624","达日县"),
        ADDRESS_632625("630000","青海省","632600","果洛藏族自治州","632625","久治县"),
        ADDRESS_632626("630000","青海省","632600","果洛藏族自治州","632626","玛多县"),
        ADDRESS_632701("630000","青海省","632700","玉树藏族自治州","632701","玉树市"),
        ADDRESS_632722("630000","青海省","632700","玉树藏族自治州","632722","杂多县"),
        ADDRESS_632723("630000","青海省","632700","玉树藏族自治州","632723","称多县"),
        ADDRESS_632724("630000","青海省","632700","玉树藏族自治州","632724","治多县"),
        ADDRESS_632725("630000","青海省","632700","玉树藏族自治州","632725","囊谦县"),
        ADDRESS_632726("630000","青海省","632700","玉树藏族自治州","632726","曲麻莱县"),
        ADDRESS_632801("630000","青海省","632800","海西蒙古族藏族自治州","632801","格尔木市"),
        ADDRESS_632802("630000","青海省","632800","海西蒙古族藏族自治州","632802","德令哈市"),
        ADDRESS_632803("630000","青海省","632800","海西蒙古族藏族自治州","632803","茫崖市"),
        ADDRESS_632821("630000","青海省","632800","海西蒙古族藏族自治州","632821","乌兰县"),
        ADDRESS_632822("630000","青海省","632800","海西蒙古族藏族自治州","632822","都兰县"),
        ADDRESS_632823("630000","青海省","632800","海西蒙古族藏族自治州","632823","天峻县"),

        ;



        AddressCode630000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode630000Enum addressCodeEnum :AddressCode630000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }

    /**
     * 省编码 新疆维吾尔自治区枚举
     */
    public enum AddressCode650000Enum {

        ADDRESS_650102("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650102","天山区"),
        ADDRESS_650103("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650103","沙依巴克区"),
        ADDRESS_650104("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650104","新市区"),
        ADDRESS_650105("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650105","水磨沟区"),
        ADDRESS_650106("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650106","头屯河区"),
        ADDRESS_650107("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650107","达坂城区"),
        ADDRESS_650109("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650109","米东区"),
        ADDRESS_650121("650000","新疆维吾尔自治区","650100","乌鲁木齐市","650121","乌鲁木齐县"),
        ADDRESS_650202("650000","新疆维吾尔自治区","650200","克拉玛依市","650202","独山子区"),
        ADDRESS_650203("650000","新疆维吾尔自治区","650200","克拉玛依市","650203","克拉玛依区"),
        ADDRESS_650204("650000","新疆维吾尔自治区","650200","克拉玛依市","650204","白碱滩区"),
        ADDRESS_650205("650000","新疆维吾尔自治区","650200","克拉玛依市","650205","乌尔禾区"),
        ADDRESS_650402("650000","新疆维吾尔自治区","650400","吐鲁番市","650402","高昌区"),
        ADDRESS_650421("650000","新疆维吾尔自治区","650400","吐鲁番市","650421","鄯善县"),
        ADDRESS_650422("650000","新疆维吾尔自治区","650400","吐鲁番市","650422","托克逊县"),
        ADDRESS_650502("650000","新疆维吾尔自治区","650500","哈密市","650502","伊州区"),
        ADDRESS_650521("650000","新疆维吾尔自治区","650500","哈密市","650521","巴里坤哈萨克自治县"),
        ADDRESS_650522("650000","新疆维吾尔自治区","650500","哈密市","650522","伊吾县"),
        ADDRESS_652301("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652301","昌吉市"),
        ADDRESS_652302("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652302","阜康市"),
        ADDRESS_652323("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652323","呼图壁县"),
        ADDRESS_652324("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652324","玛纳斯县"),
        ADDRESS_652325("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652325","奇台县"),
        ADDRESS_652327("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652327","吉木萨尔县"),
        ADDRESS_652328("650000","新疆维吾尔自治区","652300","昌吉回族自治州","652328","木垒哈萨克自治县"),
        ADDRESS_652701("650000","新疆维吾尔自治区","652700","博尔塔拉蒙古自治州","652701","博乐市"),
        ADDRESS_652702("650000","新疆维吾尔自治区","652700","博尔塔拉蒙古自治州","652702","阿拉山口市"),
        ADDRESS_652722("650000","新疆维吾尔自治区","652700","博尔塔拉蒙古自治州","652722","精河县"),
        ADDRESS_652723("650000","新疆维吾尔自治区","652700","博尔塔拉蒙古自治州","652723","温泉县"),
        ADDRESS_652801("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652801","库尔勒市"),
        ADDRESS_652822("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652822","轮台县"),
        ADDRESS_652823("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652823","尉犁县"),
        ADDRESS_652824("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652824","若羌县"),
        ADDRESS_652825("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652825","且末县"),
        ADDRESS_652826("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652826","焉耆回族自治县"),
        ADDRESS_652827("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652827","和静县"),
        ADDRESS_652828("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652828","和硕县"),
        ADDRESS_652829("650000","新疆维吾尔自治区","652800","巴音郭楞蒙古自治州","652829","博湖县"),
        ADDRESS_652901("650000","新疆维吾尔自治区","652900","阿克苏地区","652901","阿克苏市"),
        ADDRESS_652902("650000","新疆维吾尔自治区","652900","阿克苏地区","652902","库车市"),
        ADDRESS_652922("650000","新疆维吾尔自治区","652900","阿克苏地区","652922","温宿县"),
        ADDRESS_652924("650000","新疆维吾尔自治区","652900","阿克苏地区","652924","沙雅县"),
        ADDRESS_652925("650000","新疆维吾尔自治区","652900","阿克苏地区","652925","新和县"),
        ADDRESS_652926("650000","新疆维吾尔自治区","652900","阿克苏地区","652926","拜城县"),
        ADDRESS_652927("650000","新疆维吾尔自治区","652900","阿克苏地区","652927","乌什县"),
        ADDRESS_652928("650000","新疆维吾尔自治区","652900","阿克苏地区","652928","阿瓦提县"),
        ADDRESS_652929("650000","新疆维吾尔自治区","652900","阿克苏地区","652929","柯坪县"),
        ADDRESS_653001("650000","新疆维吾尔自治区","653000","克孜勒苏柯尔克孜自治州","653001","阿图什市"),
        ADDRESS_653022("650000","新疆维吾尔自治区","653000","克孜勒苏柯尔克孜自治州","653022","阿克陶县"),
        ADDRESS_653023("650000","新疆维吾尔自治区","653000","克孜勒苏柯尔克孜自治州","653023","阿合奇县"),
        ADDRESS_653024("650000","新疆维吾尔自治区","653000","克孜勒苏柯尔克孜自治州","653024","乌恰县"),
        ADDRESS_653101("650000","新疆维吾尔自治区","653100","喀什地区","653101","喀什市"),
        ADDRESS_653121("650000","新疆维吾尔自治区","653100","喀什地区","653121","疏附县"),
        ADDRESS_653122("650000","新疆维吾尔自治区","653100","喀什地区","653122","疏勒县"),
        ADDRESS_653123("650000","新疆维吾尔自治区","653100","喀什地区","653123","英吉沙县"),
        ADDRESS_653124("650000","新疆维吾尔自治区","653100","喀什地区","653124","泽普县"),
        ADDRESS_653125("650000","新疆维吾尔自治区","653100","喀什地区","653125","莎车县"),
        ADDRESS_653126("650000","新疆维吾尔自治区","653100","喀什地区","653126","叶城县"),
        ADDRESS_653127("650000","新疆维吾尔自治区","653100","喀什地区","653127","麦盖提县"),
        ADDRESS_653128("650000","新疆维吾尔自治区","653100","喀什地区","653128","岳普湖县"),
        ADDRESS_653129("650000","新疆维吾尔自治区","653100","喀什地区","653129","伽师县"),
        ADDRESS_653130("650000","新疆维吾尔自治区","653100","喀什地区","653130","巴楚县"),
        ADDRESS_653131("650000","新疆维吾尔自治区","653100","喀什地区","653131","塔什库尔干塔吉克自治县"),
        ADDRESS_653201("650000","新疆维吾尔自治区","653200","和田地区","653201","和田市"),
        ADDRESS_653221("650000","新疆维吾尔自治区","653200","和田地区","653221","和田县"),
        ADDRESS_653222("650000","新疆维吾尔自治区","653200","和田地区","653222","墨玉县"),
        ADDRESS_653223("650000","新疆维吾尔自治区","653200","和田地区","653223","皮山县"),
        ADDRESS_653224("650000","新疆维吾尔自治区","653200","和田地区","653224","洛浦县"),
        ADDRESS_653225("650000","新疆维吾尔自治区","653200","和田地区","653225","策勒县"),
        ADDRESS_653226("650000","新疆维吾尔自治区","653200","和田地区","653226","于田县"),
        ADDRESS_653227("650000","新疆维吾尔自治区","653200","和田地区","653227","民丰县"),
        ADDRESS_654002("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654002","伊宁市"),
        ADDRESS_654003("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654003","奎屯市"),
        ADDRESS_654004("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654004","霍尔果斯市"),
        ADDRESS_654021("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654021","伊宁县"),
        ADDRESS_654022("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654022","察布查尔锡伯自治县"),
        ADDRESS_654023("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654023","霍城县"),
        ADDRESS_654024("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654024","巩留县"),
        ADDRESS_654025("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654025","新源县"),
        ADDRESS_654026("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654026","昭苏县"),
        ADDRESS_654027("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654027","特克斯县"),
        ADDRESS_654028("650000","新疆维吾尔自治区","654000","伊犁哈萨克自治州","654028","尼勒克县"),
        ADDRESS_654201("650000","新疆维吾尔自治区","654200","塔城地区","654201","塔城市"),
        ADDRESS_654202("650000","新疆维吾尔自治区","654200","塔城地区","654202","乌苏市"),
        ADDRESS_654221("650000","新疆维吾尔自治区","654200","塔城地区","654221","额敏县"),
        ADDRESS_654224("650000","新疆维吾尔自治区","654200","塔城地区","654224","托里县"),
        ADDRESS_654225("650000","新疆维吾尔自治区","654200","塔城地区","654225","裕民县"),
        ADDRESS_654226("650000","新疆维吾尔自治区","654200","塔城地区","654226","和布克赛尔蒙古自治县"),
        ADDRESS_654281("650000","新疆维吾尔自治区","654200","塔城地区","654281","沙湾市"),
        ADDRESS_654301("650000","新疆维吾尔自治区","654300","阿勒泰地区","654301","阿勒泰市"),
        ADDRESS_654321("650000","新疆维吾尔自治区","654300","阿勒泰地区","654321","布尔津县"),
        ADDRESS_654322("650000","新疆维吾尔自治区","654300","阿勒泰地区","654322","富蕴县"),
        ADDRESS_654323("650000","新疆维吾尔自治区","654300","阿勒泰地区","654323","福海县"),
        ADDRESS_654324("650000","新疆维吾尔自治区","654300","阿勒泰地区","654324","哈巴河县"),
        ADDRESS_654325("650000","新疆维吾尔自治区","654300","阿勒泰地区","654325","青河县"),
        ADDRESS_654326("650000","新疆维吾尔自治区","654300","阿勒泰地区","654326","吉木乃县"),

        ;



        AddressCode650000Enum(String provinceCode, String provinceName,String cityCode, String cityName,String countyCode, String countyName) {
            this.provinceCode = provinceCode;
            this.provinceName = provinceName;
            this.cityCode = cityCode;
            this.cityName = cityName;
            this.countyCode = countyCode;
            this.countyName = countyName;
        }

        /**
         * 返回枚举信息
         * @return
         */
        public static List<AddressCode> getAddressCode(){
            List<AddressCode> addressCodeList = new ArrayList<>();
            for(AddressCode650000Enum addressCodeEnum :AddressCode650000Enum.values()){
                AddressCode addressCode = new AddressCode();
                addressCode.setProvinceCode(addressCodeEnum.getProvinceCode());
                addressCode.setProvinceName(addressCodeEnum.getProvinceName());
                addressCode.setCityCode(addressCodeEnum.getCityCode());
                addressCode.setCityName(addressCodeEnum.getCityName());
                addressCode.setCountyCode(addressCodeEnum.getCountyCode());
                addressCode.setCountyName(addressCodeEnum.getCountyName());
                addressCodeList.add(addressCode);
            }
            return addressCodeList;
        }


        /**
         * 省编码
         */
        private String provinceCode;

        /**
         * 省名称
         */
        private String provinceName;

        /**
         * 市编码
         */
        private String cityCode;

        /**
         * 市名称
         */
        private String cityName;

        /**
         * 县/区编码
         */
        private String countyCode;

        /**
         * 县/区名称
         */
        private String countyName;


        public String getProvinceCode() {
            return provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public String getCountyName() {
            return countyName;
        }

    }


    /*ADDRESS_810101("810000","香港特别行政区","810000","香港特别行政区","810101","中西区"),
    ADDRESS_810102("810000","香港特别行政区","810000","香港特别行政区","810102","湾仔区"),
    ADDRESS_810103("810000","香港特别行政区","810000","香港特别行政区","810103","东区"),
    ADDRESS_810104("810000","香港特别行政区","810000","香港特别行政区","810104","南区"),
    ADDRESS_810105("810000","香港特别行政区","810000","香港特别行政区","810105","油尖旺区"),
    ADDRESS_810106("810000","香港特别行政区","810000","香港特别行政区","810106","深水埗区"),
    ADDRESS_810107("810000","香港特别行政区","810000","香港特别行政区","810107","九龙城区"),
    ADDRESS_810108("810000","香港特别行政区","810000","香港特别行政区","810108","黄大仙区"),
    ADDRESS_810109("810000","香港特别行政区","810000","香港特别行政区","810109","观塘区"),
    ADDRESS_810110("810000","香港特别行政区","810000","香港特别行政区","810110","北区"),
    ADDRESS_810111("810000","香港特别行政区","810000","香港特别行政区","810111","大埔区"),
    ADDRESS_810112("810000","香港特别行政区","810000","香港特别行政区","810112","沙田区"),
    ADDRESS_810113("810000","香港特别行政区","810000","香港特别行政区","810113","西贡区"),
    ADDRESS_810114("810000","香港特别行政区","810000","香港特别行政区","810114","荃湾区"),
    ADDRESS_810115("810000","香港特别行政区","810000","香港特别行政区","810115","屯门区"),
    ADDRESS_810116("810000","香港特别行政区","810000","香港特别行政区","810116","元朗区"),
    ADDRESS_810117("810000","香港特别行政区","810000","香港特别行政区","810117","葵青区"),
    ADDRESS_810118("810000","香港特别行政区","810000","香港特别行政区","810118","离岛区"),
    ADDRESS_820101("820000","澳门特别行政区","820000","澳门特别行政区","820101","花地玛堂区"),
    ADDRESS_820102("820000","澳门特别行政区","820000","澳门特别行政区","820102","圣安多尼堂区"),
    ADDRESS_820103("820000","澳门特别行政区","820000","澳门特别行政区","820103","大堂区"),
    ADDRESS_820104("820000","澳门特别行政区","820000","澳门特别行政区","820104","望德堂区"),
    ADDRESS_820105("820000","澳门特别行政区","820000","澳门特别行政区","820105","风顺堂区"),
    ADDRESS_820106("820000","澳门特别行政区","820000","澳门特别行政区","820106","嘉模堂区"),
    ADDRESS_820107("820000","澳门特别行政区","820000","澳门特别行政区","820107","圣方济各堂区"),
    ADDRESS_820108("820000","澳门特别行政区","820000","澳门特别行政区","820108","路氹城"),
    ADDRESS_820109("820000","澳门特别行政区","820000","澳门特别行政区","820109","澳门新城"),
    ADDRESS_830101("830000","台湾省","830100","台北市","830101","中正区"),
    ADDRESS_830102("830000","台湾省","830100","台北市","830102","大同区"),
    ADDRESS_830103("830000","台湾省","830100","台北市","830103","中山区"),
    ADDRESS_830104("830000","台湾省","830100","台北市","830104","万华区"),
    ADDRESS_830105("830000","台湾省","830100","台北市","830105","信义区"),
    ADDRESS_830106("830000","台湾省","830100","台北市","830106","松山区"),
    ADDRESS_830107("830000","台湾省","830100","台北市","830107","大安区"),
    ADDRESS_830108("830000","台湾省","830100","台北市","830108","南港区"),
    ADDRESS_830109("830000","台湾省","830100","台北市","830109","北投区"),
    ADDRESS_830110("830000","台湾省","830100","台北市","830110","内湖区"),
    ADDRESS_830111("830000","台湾省","830100","台北市","830111","士林区"),
    ADDRESS_830112("830000","台湾省","830100","台北市","830112","文山区"),
    ADDRESS_830201("830000","台湾省","830200","新北市","830201","板桥区"),
    ADDRESS_830202("830000","台湾省","830200","新北市","830202","土城区"),
    ADDRESS_830203("830000","台湾省","830200","新北市","830203","新庄区"),
    ADDRESS_830204("830000","台湾省","830200","新北市","830204","新店区"),
    ADDRESS_830205("830000","台湾省","830200","新北市","830205","深坑区"),
    ADDRESS_830206("830000","台湾省","830200","新北市","830206","石碇区"),
    ADDRESS_830207("830000","台湾省","830200","新北市","830207","坪林区"),
    ADDRESS_830208("830000","台湾省","830200","新北市","830208","乌来区"),
    ADDRESS_830209("830000","台湾省","830200","新北市","830209","五股区"),
    ADDRESS_830210("830000","台湾省","830200","新北市","830210","八里区"),
    ADDRESS_830211("830000","台湾省","830200","新北市","830211","林口区"),
    ADDRESS_830212("830000","台湾省","830200","新北市","830212","淡水区"),
    ADDRESS_830213("830000","台湾省","830200","新北市","830213","中和区"),
    ADDRESS_830214("830000","台湾省","830200","新北市","830214","永和区"),
    ADDRESS_830215("830000","台湾省","830200","新北市","830215","三重区"),
    ADDRESS_830216("830000","台湾省","830200","新北市","830216","芦洲区"),
    ADDRESS_830217("830000","台湾省","830200","新北市","830217","泰山区"),
    ADDRESS_830218("830000","台湾省","830200","新北市","830218","树林区"),
    ADDRESS_830219("830000","台湾省","830200","新北市","830219","莺歌区"),
    ADDRESS_830220("830000","台湾省","830200","新北市","830220","三峡区"),
    ADDRESS_830221("830000","台湾省","830200","新北市","830221","汐止区"),
    ADDRESS_830222("830000","台湾省","830200","新北市","830222","金山区"),
    ADDRESS_830223("830000","台湾省","830200","新北市","830223","万里区"),
    ADDRESS_830224("830000","台湾省","830200","新北市","830224","三芝区"),
    ADDRESS_830225("830000","台湾省","830200","新北市","830225","石门区"),
    ADDRESS_830226("830000","台湾省","830200","新北市","830226","瑞芳区"),
    ADDRESS_830227("830000","台湾省","830200","新北市","830227","贡寮区"),
    ADDRESS_830228("830000","台湾省","830200","新北市","830228","双溪区"),
    ADDRESS_830229("830000","台湾省","830200","新北市","830229","平溪区"),
    ADDRESS_830301("830000","台湾省","830300","桃园市","830301","桃园区"),
    ADDRESS_830302("830000","台湾省","830300","桃园市","830302","中坜区"),
    ADDRESS_830303("830000","台湾省","830300","桃园市","830303","平镇区"),
    ADDRESS_830304("830000","台湾省","830300","桃园市","830304","八德区"),
    ADDRESS_830305("830000","台湾省","830300","桃园市","830305","杨梅区"),
    ADDRESS_830306("830000","台湾省","830300","桃园市","830306","芦竹区"),
    ADDRESS_830307("830000","台湾省","830300","桃园市","830307","大溪区"),
    ADDRESS_830308("830000","台湾省","830300","桃园市","830308","龙潭区"),
    ADDRESS_830309("830000","台湾省","830300","桃园市","830309","龟山区"),
    ADDRESS_830310("830000","台湾省","830300","桃园市","830310","大园区"),
    ADDRESS_830311("830000","台湾省","830300","桃园市","830311","观音区"),
    ADDRESS_830312("830000","台湾省","830300","桃园市","830312","新屋区"),
    ADDRESS_830313("830000","台湾省","830300","桃园市","830313","复兴区"),
    ADDRESS_830401("830000","台湾省","830400","台中市","830401","中区"),
    ADDRESS_830402("830000","台湾省","830400","台中市","830402","东区"),
    ADDRESS_830403("830000","台湾省","830400","台中市","830403","西区"),
    ADDRESS_830404("830000","台湾省","830400","台中市","830404","南区"),
    ADDRESS_830405("830000","台湾省","830400","台中市","830405","北区"),
    ADDRESS_830406("830000","台湾省","830400","台中市","830406","西屯区"),
    ADDRESS_830407("830000","台湾省","830400","台中市","830407","南屯区"),
    ADDRESS_830408("830000","台湾省","830400","台中市","830408","北屯区"),
    ADDRESS_830409("830000","台湾省","830400","台中市","830409","丰原区"),
    ADDRESS_830410("830000","台湾省","830400","台中市","830410","大里区"),
    ADDRESS_830411("830000","台湾省","830400","台中市","830411","太平区"),
    ADDRESS_830412("830000","台湾省","830400","台中市","830412","东势区"),
    ADDRESS_830413("830000","台湾省","830400","台中市","830413","大甲区"),
    ADDRESS_830414("830000","台湾省","830400","台中市","830414","清水区"),
    ADDRESS_830415("830000","台湾省","830400","台中市","830415","沙鹿区"),
    ADDRESS_830416("830000","台湾省","830400","台中市","830416","梧栖区"),
    ADDRESS_830417("830000","台湾省","830400","台中市","830417","后里区"),
    ADDRESS_830418("830000","台湾省","830400","台中市","830418","神冈区"),
    ADDRESS_830419("830000","台湾省","830400","台中市","830419","潭子区"),
    ADDRESS_830420("830000","台湾省","830400","台中市","830420","大雅区"),
    ADDRESS_830421("830000","台湾省","830400","台中市","830421","新小区"),
    ADDRESS_830422("830000","台湾省","830400","台中市","830422","石冈区"),
    ADDRESS_830423("830000","台湾省","830400","台中市","830423","外埔区"),
    ADDRESS_830424("830000","台湾省","830400","台中市","830424","大安区"),
    ADDRESS_830425("830000","台湾省","830400","台中市","830425","乌日区"),
    ADDRESS_830426("830000","台湾省","830400","台中市","830426","大肚区"),
    ADDRESS_830427("830000","台湾省","830400","台中市","830427","龙井区"),
    ADDRESS_830428("830000","台湾省","830400","台中市","830428","雾峰区"),
    ADDRESS_830429("830000","台湾省","830400","台中市","830429","和平区"),
    ADDRESS_830501("830000","台湾省","830500","台南市","830501","中西区"),
    ADDRESS_830502("830000","台湾省","830500","台南市","830502","东区"),
    ADDRESS_830503("830000","台湾省","830500","台南市","830503","南区"),
    ADDRESS_830504("830000","台湾省","830500","台南市","830504","北区"),
    ADDRESS_830505("830000","台湾省","830500","台南市","830505","安平区"),
    ADDRESS_830506("830000","台湾省","830500","台南市","830506","安南区"),
    ADDRESS_830507("830000","台湾省","830500","台南市","830507","永康区"),
    ADDRESS_830508("830000","台湾省","830500","台南市","830508","归仁区"),
    ADDRESS_830509("830000","台湾省","830500","台南市","830509","新化区"),
    ADDRESS_830510("830000","台湾省","830500","台南市","830510","左镇区"),
    ADDRESS_830511("830000","台湾省","830500","台南市","830511","玉井区"),
    ADDRESS_830512("830000","台湾省","830500","台南市","830512","楠西区"),
    ADDRESS_830513("830000","台湾省","830500","台南市","830513","南化区"),
    ADDRESS_830514("830000","台湾省","830500","台南市","830514","仁德区"),
    ADDRESS_830515("830000","台湾省","830500","台南市","830515","关庙区"),
    ADDRESS_830516("830000","台湾省","830500","台南市","830516","龙崎区"),
    ADDRESS_830517("830000","台湾省","830500","台南市","830517","官田区"),
    ADDRESS_830518("830000","台湾省","830500","台南市","830518","麻豆区"),
    ADDRESS_830519("830000","台湾省","830500","台南市","830519","佳里区"),
    ADDRESS_830520("830000","台湾省","830500","台南市","830520","西港区"),
    ADDRESS_830521("830000","台湾省","830500","台南市","830521","七股区"),
    ADDRESS_830522("830000","台湾省","830500","台南市","830522","将军区"),
    ADDRESS_830523("830000","台湾省","830500","台南市","830523","学甲区"),
    ADDRESS_830524("830000","台湾省","830500","台南市","830524","北门区"),
    ADDRESS_830525("830000","台湾省","830500","台南市","830525","新营区"),
    ADDRESS_830526("830000","台湾省","830500","台南市","830526","后壁区"),
    ADDRESS_830527("830000","台湾省","830500","台南市","830527","白河区"),
    ADDRESS_830528("830000","台湾省","830500","台南市","830528","东山区"),
    ADDRESS_830529("830000","台湾省","830500","台南市","830529","六甲区"),
    ADDRESS_830530("830000","台湾省","830500","台南市","830530","下营区"),
    ADDRESS_830531("830000","台湾省","830500","台南市","830531","柳营区"),
    ADDRESS_830532("830000","台湾省","830500","台南市","830532","盐水区"),
    ADDRESS_830533("830000","台湾省","830500","台南市","830533","善化区"),
    ADDRESS_830534("830000","台湾省","830500","台南市","830534","大内区"),
    ADDRESS_830535("830000","台湾省","830500","台南市","830535","山上区"),
    ADDRESS_830536("830000","台湾省","830500","台南市","830536","新市区"),
    ADDRESS_830537("830000","台湾省","830500","台南市","830537","安定区"),
    ADDRESS_830601("830000","台湾省","830600","高雄市","830601","楠梓区"),
    ADDRESS_830602("830000","台湾省","830600","高雄市","830602","左营区"),
    ADDRESS_830603("830000","台湾省","830600","高雄市","830603","鼓山区"),
    ADDRESS_830604("830000","台湾省","830600","高雄市","830604","三民区"),
    ADDRESS_830605("830000","台湾省","830600","高雄市","830605","盐埕区"),
    ADDRESS_830606("830000","台湾省","830600","高雄市","830606","前金区"),
    ADDRESS_830607("830000","台湾省","830600","高雄市","830607","新兴区"),
    ADDRESS_830608("830000","台湾省","830600","高雄市","830608","苓雅区"),
    ADDRESS_830609("830000","台湾省","830600","高雄市","830609","前镇区"),
    ADDRESS_830610("830000","台湾省","830600","高雄市","830610","旗津区"),
    ADDRESS_830611("830000","台湾省","830600","高雄市","830611","小港区"),
    ADDRESS_830612("830000","台湾省","830600","高雄市","830612","凤山区"),
    ADDRESS_830613("830000","台湾省","830600","高雄市","830613","大寮区"),
    ADDRESS_830614("830000","台湾省","830600","高雄市","830614","鸟松区"),
    ADDRESS_830615("830000","台湾省","830600","高雄市","830615","林园区"),
    ADDRESS_830616("830000","台湾省","830600","高雄市","830616","仁武区"),
    ADDRESS_830617("830000","台湾省","830600","高雄市","830617","大树区"),
    ADDRESS_830618("830000","台湾省","830600","高雄市","830618","大社区"),
    ADDRESS_830619("830000","台湾省","830600","高雄市","830619","冈山区"),
    ADDRESS_830620("830000","台湾省","830600","高雄市","830620","路竹区"),
    ADDRESS_830621("830000","台湾省","830600","高雄市","830621","桥头区"),
    ADDRESS_830622("830000","台湾省","830600","高雄市","830622","梓官区"),
    ADDRESS_830623("830000","台湾省","830600","高雄市","830623","弥陀区"),
    ADDRESS_830624("830000","台湾省","830600","高雄市","830624","永安区"),
    ADDRESS_830625("830000","台湾省","830600","高雄市","830625","燕巢区"),
    ADDRESS_830626("830000","台湾省","830600","高雄市","830626","阿莲区"),
    ADDRESS_830627("830000","台湾省","830600","高雄市","830627","茄萣区"),
    ADDRESS_830628("830000","台湾省","830600","高雄市","830628","湖内区"),
    ADDRESS_830629("830000","台湾省","830600","高雄市","830629","田寮区"),
    ADDRESS_830630("830000","台湾省","830600","高雄市","830630","旗山区"),
    ADDRESS_830631("830000","台湾省","830600","高雄市","830631","美浓区"),
    ADDRESS_830632("830000","台湾省","830600","高雄市","830632","内门区"),
    ADDRESS_830633("830000","台湾省","830600","高雄市","830633","杉林区"),
    ADDRESS_830634("830000","台湾省","830600","高雄市","830634","甲仙区"),
    ADDRESS_830635("830000","台湾省","830600","高雄市","830635","六龟区"),
    ADDRESS_830636("830000","台湾省","830600","高雄市","830636","茂林区"),
    ADDRESS_830637("830000","台湾省","830600","高雄市","830637","桃源区"),
    ADDRESS_830638("830000","台湾省","830600","高雄市","830638","那玛夏区"),
    ADDRESS_830701("830000","台湾省","830700","基隆市","830701","中正区"),
    ADDRESS_830702("830000","台湾省","830700","基隆市","830702","七堵区"),
    ADDRESS_830703("830000","台湾省","830700","基隆市","830703","暖暖区"),
    ADDRESS_830704("830000","台湾省","830700","基隆市","830704","仁爱区"),
    ADDRESS_830705("830000","台湾省","830700","基隆市","830705","中山区"),
    ADDRESS_830706("830000","台湾省","830700","基隆市","830706","安乐区"),
    ADDRESS_830707("830000","台湾省","830700","基隆市","830707","信义区"),
    ADDRESS_830801("830000","台湾省","830800","新竹市","830801","东区"),
    ADDRESS_830802("830000","台湾省","830800","新竹市","830802","北区"),
    ADDRESS_830803("830000","台湾省","830800","新竹市","830803","香山区"),
    ADDRESS_830901("830000","台湾省","830900","嘉义市","830901","东区"),
    ADDRESS_830902("830000","台湾省","830900","嘉义市","830902","西区"),*/


}
