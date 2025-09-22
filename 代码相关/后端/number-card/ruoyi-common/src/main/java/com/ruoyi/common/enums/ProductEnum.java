package com.ruoyi.common.enums;


/**
 * 产品状态相关枚举
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/11 18:47
 */
public enum ProductEnum {
    //产品类型相关枚举
    DAILY_SETTLEMENT(0, "日结秒返"),
    MONTHLY_STATEMENT(1, "月结产品"),
    LONG_TIME(2, "长期产品"),
    OTHER(3, "其他产品"),
    COMBINATION(4, "组合返佣");

    ProductEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private Integer status;
    private String message;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public static String getProductMessageByStatus(Integer status) {
        for (ProductEnum value : ProductEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getMessage();
            }
        }
        return "未知状态";
    }


    /**
     * 运营商类型相关枚举
     */
    public enum OperatorTypeEnum {

        MOBILE(0, "中国移动"),
        TELECOM(1, "中国电信"),
        UNICOM(2, "中国联通"),
        CMG(3, "中国广电"),
        VIRTUAL_OPERATOR(4, "虚拟运营商"),
        ;

        private Integer status;
        private String message;

        public String getMessage() {
            return message;
        }

        public Integer getStatus() {
            return status;
        }

        OperatorTypeEnum(Integer status, String message) {
            this.status = status;
            this.message = message;
        }

        public static String getOperatorTypeByStatus(Integer status) {
            for (OperatorTypeEnum value : OperatorTypeEnum.values()) {
                if (value.getStatus().equals(status)) {
                    return value.getMessage();
                }
            }
            return "未知状态";
        }


    }

    /**
     * 产品状态类型
     */
    public enum ProductStatusEnum {

        NO(0, "下架"),
        YES(1, "上架")
        ;

        private Integer status;
        private String message;

        public String getMessage() {
            return message;
        }

        public Integer getStatus() {
            return status;
        }

        ProductStatusEnum(Integer status, String message) {
            this.status = status;
            this.message = message;
        }

        public static String getProductByStatus(Integer status) {
            for (ProductStatusEnum value : ProductStatusEnum.values()) {
                if (value.getStatus().equals(status)) {
                    return value.getMessage();
                }
            }
            return "未知状态";
        }
    }
    /**
     * public static final int ZERO_INT = 0;
     *
     *     public static final int ONE_INT = 1;
     *
     *     public static final int TWO_INT = 2;
     *
     *     public static final int THREE_INT = 3;
     *
     *     public static final int FOUR_INT = 4;
     *
     *     public static final int FIVE_INT = 5;
     *
     *     public static final int SEX_INT = 6;
     *
     *     public static final int EIGHT_INT = 8;
     *
     *     public static final int SEVEN_INT = 7;
     * 号码请求状态类型相关枚举
     * 0 炫咖移动余额查询 1炫咖联通余额查询 2炫咖电信余额查询 3炫咖携号转网 4 炫咖号码查询 5 炫咖空号检测 6 额查查余额查询 7 额查查携号查询 8 额查查空号查询
     */
    public enum NumberRequestStatusTypeEnum {
        ZERO(0, "炫咖移动余额查询"),
        ONE(1, "炫咖联通余额查询"),
        TWO(2, "炫咖电信余额查询"),
        THREE(3, "炫咖携号转网"),
        FOUR(4, "炫咖号码查询"),
        FIVE(5, "炫咖空号检测"),
        SEX(6, "额查查余额查询"),
        SEVEN(7, "额查查携号查询"),
        EIGHT(8, "额查查空号查询"),
        ;

        private Integer status;
        private String message;

        public String getMessage() {
            return message;
        }

        public Integer getStatus() {
            return status;
        }

        NumberRequestStatusTypeEnum(Integer status, String message) {
            this.status = status;
            this.message = message;
        }

        public static String getNumberRequestStatusType(Integer status) {
            for (NumberRequestStatusTypeEnum value : NumberRequestStatusTypeEnum.values()) {
                if (value.getStatus().equals(status)) {
                    return value.getMessage();
                }
            }
            return "未知状态";
        }


    }



}
