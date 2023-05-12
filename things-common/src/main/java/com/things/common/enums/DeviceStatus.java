package com.things.common.enums;

/**
 * 用户状态
 *
 * @author ruoyi
 */
public enum DeviceStatus {
    /**
     * 启用
     */
    ENABLE("0", "启用"),

    /**
     * 停用
     */
    DISABLE("1", "停用");

    private final String code;
    private final String info;

    DeviceStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
