package com.dragonsoft.yqjk.enumresources;


/**
 * 疫情接口类型枚举
 */

public enum YqInterfaceTypeEnum{
    KNMQJCZ("0", "可能密切接触者查询服务","可能密切接触者.xls"),
    QZHYSBL("1", "确诊和疑似病例查询服务","确诊和疑似病例.xls");

    private final String code;
    private final String desc;
    private final String fileName;

    private YqInterfaceTypeEnum(String code, String desc, String fileName) {
        this.code = code;
        this.desc = desc;
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFileName() {
        return fileName;
    }
}
