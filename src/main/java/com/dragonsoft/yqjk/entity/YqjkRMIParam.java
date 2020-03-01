package com.dragonsoft.yqjk.entity;


/**
 * 疫情监控远程调用参数实体
 * @author ronin
 */
public class YqjkRMIParam {

    private String id;
    /**
     * token相关参数
     */
    /**token的Url**/
    private String tokenUrl;
    /**token标识请求类型*/
    private String tokenContentType;
    /**应用的Api Key*/
    private String clientId;
    /**应用的Secret Key*/
    private String clientSecret;
    /**获取token的模式 默认implicit*/
    private String grantType;

    /**
     * API网关相关参数
     */
    /**API网关的Url*/
    private String apiUrl;
    /**API网关标识请求类型*/
    private String apiContentType;
    /**方案编号 随机值，用于标识当前请求，建议以当前时间戳来作为内容*/
    private String processNum;
    /**步骤编号*/
    private String stepNum;
    /**接收方应用ApiKey*/
    private String receiveAppId;
    /**发起方应用ApiKey*/
    private String invokingAppId;

    /**
     * 0:为可能密切接触者数据查询服务接口
     * 1:为确诊和疑似病例数据查询服务接口
     */
    private String type;

    public YqjkRMIParam() {
    }

    public YqjkRMIParam(String id, String tokenUrl, String tokenContentType, String clientId, String clientSecret, String grantType, String apiUrl, String apiContentType, String processNum, String stepNum, String receiveAppId, String invokingAppId, String type) {
        this.id = id;
        this.tokenUrl = tokenUrl;
        this.tokenContentType = tokenContentType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.apiUrl = apiUrl;
        this.apiContentType = apiContentType;
        this.processNum = processNum;
        this.stepNum = stepNum;
        this.receiveAppId = receiveAppId;
        this.invokingAppId = invokingAppId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getTokenContentType() {
        return tokenContentType;
    }

    public void setTokenContentType(String tokenContentType) {
        this.tokenContentType = tokenContentType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiContentType() {
        return apiContentType;
    }

    public void setApiContentType(String apiContentType) {
        this.apiContentType = apiContentType;
    }

    public String getProcessNum() {
        return processNum;
    }

    public void setProcessNum(String processNum) {
        this.processNum = processNum;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getReceiveAppId() {
        return receiveAppId;
    }

    public void setReceiveAppId(String receiveAppId) {
        this.receiveAppId = receiveAppId;
    }

    public String getInvokingAppId() {
        return invokingAppId;
    }

    public void setInvokingAppId(String invokingAppId) {
        this.invokingAppId = invokingAppId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "YqjkParam{" +
                "id='" + id + '\'' +
                ", tokenUrl='" + tokenUrl + '\'' +
                ", tokenContentType='" + tokenContentType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", apiContentType='" + apiContentType + '\'' +
                ", processNum='" + processNum + '\'' +
                ", stepNum='" + stepNum + '\'' +
                ", receiveAppId='" + receiveAppId + '\'' +
                ", invokingAppId='" + invokingAppId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
