package com.ruoyi.common.enums;


/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/11 18:47
 */
public enum OrderEnum {
    INVALID(-1, "订单失败"),
    CREATE(0, "订单预创建"),
    APPLY(1, "订单申请成功"),
    SHIPPED(2, "订单已发货"),
    RECEIVED(3, "订单已签收"),
    ACTIVATED(4, "订单已激活");

    OrderEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getOrderMessageByStatus(Integer status) {
        for (OrderEnum value : OrderEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getMessage();
            }
        }
        return "未知状态";
    }

    private Integer status;
    private String message;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }



    /**
     * 订单来源 0 信息流 1合作方API进单 2导单 3重推 4快手 5抖店 6转单
     */
    public enum OrderSourceEnum {

        XIN_XI_LIU(0, "信息流"),
        API(1, "合作方API进单"),
        IMPORT(2, "导单"),
        RE_PUSH(3, "重推"),
        KUAI_SHOU(4, "快手"),
        DOU_DIAN(5, "抖店"),
        SYNC_TRANSFER_ORDER(6, "同步转单"),
        ERROR_TRANSFER_ORDER(7, "失败转单"),
        ;

        private Integer sourceType;

        private String sourceName;


        public Integer getSourceType() {
            return sourceType;
        }

        public String getSourceName() {
            return sourceName;
        }

        OrderSourceEnum(Integer sourceType, String sourceName) {
            this.sourceType = sourceType;
            this.sourceName = sourceName;
        }

        public static String getSourceName(Integer sourceType) {
            if (sourceType == null) {
                return "未知";
            }
            for (OrderSourceEnum value : OrderSourceEnum.values()) {
                if (value.sourceType.equals(sourceType)) {
                    return value.getSourceName();
                }
            }
            return "未知";
        }


    }


    /**
     * 订单结算状态 0 未到结算状态 1 待结算 22 部分结算 3已结算 4无法结算
     */
    public enum OrderCommissionEnum {

        TYPE_0(0, "未到结算状态"),
        TYPE_1(1, "待结算"),
        TYPE_2(2, "部分结算"),
        TYPE_3(3, "已结算"),
        TYPE_4(4, "无法结算"),
        ;

        private Integer commissionType;

        private String commissionName;


        public Integer getCommissionType() {
            return commissionType;
        }

        public String getCommissionName() {
            return commissionName;
        }

        OrderCommissionEnum(Integer commissionType, String commissionName) {
            this.commissionType = commissionType;
            this.commissionName = commissionName;
        }

        public static String getCommissionByStatus(Integer status) {
            for (OrderCommissionEnum value : OrderCommissionEnum.values()) {
                if (value.commissionType.equals(status)) {
                    return value.commissionName;
                }
            }
            return "未知状态";
        }

    }



}
