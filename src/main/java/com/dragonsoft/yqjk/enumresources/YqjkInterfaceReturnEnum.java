package com.dragonsoft.yqjk.enumresources;

/**
 * 调用公安部接口返回CODE枚举
 */
public enum  YqjkInterfaceReturnEnum {
    CXCG("200","查询成功"),
    NBCW("300","内部错误"),
    WGFWYC("400","网关访问异常"),
    QQCSGSYWT("21001","请求参数格式有问题"),
    RIDZCW("21002","请求者标识不存在(rid值错误)"),
    SIDZCW("21003","目标API接口不存在(sid值错误)"),
    QQZMYQXDYCJK("21004","请求者没有权限调用此API 接口"),
    SIGNCW("21005","签名错误(sign值错误)"),
    QMYGQ("21006","签名已过期"),
    FWYXX("21007","服务已下线"),
    RZXXGSCW("21008","认证信息格式错误"),
    QXRZSB("21009","权限认证失败"),
    HTTPSZZBCZ("21010","HTTPS证书不存在"),
    HTTPSZZRZSB("21011","HTTPS证书证书认证失败"),
    HQFWSQMYTPF("21012","获取服务授权秘钥(appsecret)太频繁，请稍后再试。"),
    BFFWLTG("22001","并发访问量太高，请稍后再试。"),
    FWJZZCSJDDY("22002","服务接口禁止在此时间段调用"),
    FWDYCSPEYYJ("22003","申请的服务调用次数配额已用尽"),
    YSJKDYXYCS("23001","原始接口调用响应超时"),
    YSJKDYCW("23002","原始接口调用错误"),
    YSFWJJLJ("23003","原始服务拒绝连接"),
    YYSFWJLLJCS("23004","与原始服务建立连接超时"),
    FWYBRD("23005","服务已被熔断，稍后再试"),
    WGXTNBCW("24001 ","网关系统内部错误"),
    WZCW("24002 ","未知错误"),
    RZLXCW("1000 ","认证类型错误"),
    YHRZSB("1001 ","用户认证失败"),
    LKLXCW("2000 ","流控类型错误"),
    FFIP("2001 ","非法IP"),
    FWPDCGSX("2002 ","访问频度超过上限"),
    FWLLCGSX("2003 ","访问流量超过上限"),
    FFGJZ("2004 ","非法关键字"),
    AQLXCW("3000 ","安全类型错误"),
    JMJMLXCW("4000 ","加密解密类型错误"),
    GSJYCW("5000 ","格式校验错误"),
    QQBWJYCW("5001 ","请求报文校验错误"),
    XTYC("9000 ","系统异常"),
    FWQQCS("9001 ","服务请求超时");

    private final String code;
    private final String desc;
    private YqjkInterfaceReturnEnum(String code, String desc){
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
