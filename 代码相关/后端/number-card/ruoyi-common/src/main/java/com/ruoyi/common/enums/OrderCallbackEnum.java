package com.ruoyi.common.enums;

/**
 * 用于标识回调公用消息的类型
 */
public enum OrderCallbackEnum {

    INVALID(-1, "无效", "10"),
    CREATE(0, "订单预创建", "0"),
    APPLY(1, "订单向运营商申请成功", "1"),
    SHIPPED(2, "已发货", "2"),
    ACTIVATED(4, "已激活", "4"),

    ;


    OrderCallbackEnum(Integer status, String message, String callbackStatus) {
        this.status = status;
        this.message = message;
        this.callbackStatus = callbackStatus;
    }

    public static String getOrderMessageByStatus(Integer status) {
        for (OrderCallbackEnum value : OrderCallbackEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getMessage();
            }
        }
        return "未知状态";
    }

    public static String getOrderCallbackStatusByStatus(Integer status) {
        if (status == null) {
            return OrderCallbackEnum.INVALID.getCallbackStatus();
        }
        for (OrderCallbackEnum value : OrderCallbackEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getCallbackStatus();
            }
        }
        return OrderCallbackEnum.INVALID.getCallbackStatus();
    }

    /**
     * 根据状态返回枚举值
     *
     * @param status
     * @return
     */
    public static OrderCallbackEnum getCallbackStatus(Integer status) {
        for (OrderCallbackEnum value : OrderCallbackEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return OrderCallbackEnum.INVALID;
    }


    private Integer status;
    private String message;


    /**
     * 0:已成功
     * 1:已发货
     * 2:已激活
     * 3:已签收
     * 10:审核失败
     * 20:订单细节变化
     */
    private String callbackStatus;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }


    /**
     * 是否发送扣量回调
     */
    public enum OrderIsDeductionCallbackEnum {

        NO(0, "否"),
        YES(1, "是"),
        ;

        private Integer type;

        private String typeName;

        public Integer getType() {
            return type;
        }

        public String getTypeName() {
            return typeName;
        }

        OrderIsDeductionCallbackEnum(Integer type, String typeName) {
            this.type = type;
            this.typeName = typeName;
        }

    }
}
