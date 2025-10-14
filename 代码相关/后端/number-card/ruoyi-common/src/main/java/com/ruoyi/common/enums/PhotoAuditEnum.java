package com.ruoyi.common.enums;

/**
 * 照片审核状态枚举
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/12 16:40
 */
public enum PhotoAuditEnum {

    NONE(0, "无需审核"),
    UPLOAD_PENDING(1, "待上传照片"),
    AGENT_PENDING(2, "代理商待提交"),
    ADMIN_PENDING(3, "管理员待审核"),
    APPROVED(4, "审核通过"),
    REJECTED(5, "审核拒绝");

    PhotoAuditEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getPhotoAuditMessageByStatus(Integer status) {
        for (PhotoAuditEnum value : PhotoAuditEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value.getMessage();
            }
        }
        return "未知状态";
    }

    public static PhotoAuditEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (PhotoAuditEnum value : PhotoAuditEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 检查是否可以上传照片
     */
    public static boolean canUploadPhoto(Integer status) {
        return UPLOAD_PENDING.getStatus().equals(status);
    }

    /**
     * 检查是否可以提交审核
     */
    public static boolean canSubmitAudit(Integer status) {
        return AGENT_PENDING.getStatus().equals(status);
    }

    /**
     * 检查是否可以审核
     */
    public static boolean canAudit(Integer status) {
        return ADMIN_PENDING.getStatus().equals(status);
    }

    /**
     * 检查审核是否通过
     */
    public static boolean isApproved(Integer status) {
        return APPROVED.getStatus().equals(status);
    }

    /**
     * 检查审核是否拒绝
     */
    public static boolean isRejected(Integer status) {
        return REJECTED.getStatus().equals(status);
    }

    /**
     * 检查是否需要用户重新上传照片
     */
    public static boolean needReupload(Integer status) {
        return REJECTED.getStatus().equals(status);
    }

    private Integer status;
    private String message;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}