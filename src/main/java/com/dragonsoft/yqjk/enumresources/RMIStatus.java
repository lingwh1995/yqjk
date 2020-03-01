package com.dragonsoft.yqjk.enumresources;

/**
 * 远程调用状态
 * @author ronin
 */
public enum RMIStatus{

    ZWPPJL("1","暂无匹配记录"),
    DYJGBZ("2","调用结果比中"),
    YCDYYC("3","远程调用异常");

    private final String code;
    private final String desc;

    private RMIStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
