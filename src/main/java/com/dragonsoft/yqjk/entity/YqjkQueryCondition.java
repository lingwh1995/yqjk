package com.dragonsoft.yqjk.entity;

import java.io.Serializable;

/**
 * 疫情监控入查询参数对应实体
 * @author ronin
 */
public class YqjkQueryCondition implements Serializable{

    private static final long serialVersionUID = 6515454267058854545L;

    /**当前登录用户id*/
    private String userId;
    /**必填-请求单位代码*/
    private String qqdwdm;
    /**必填-请求单位名称*/
    private String qqdwmc;
    /**必填-请求人*/
    private String qqr;
    /**必填-请求人身份证号码*/
    private String qqrsfzhm;
    /**非必填-查询事由，查询条件*/
    private String cxsy;
    /**必填-患者姓名，查询条件*/
    private String xm;
    /**必填-患者身份证号码，查询条件*/
    private String sfzhm;
    /**
     * 0:为可能密切接触者数据查询服务接口
     * 1:为确诊和疑似病例数据查询服务接口
     */
    private String type;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQqdwdm() {
        return qqdwdm;
    }

    public void setQqdwdm(String qqdwdm) {
        this.qqdwdm = qqdwdm;
    }

    public String getQqdwmc() {
        return qqdwmc;
    }

    public void setQqdwmc(String qqdwmc) {
        this.qqdwmc = qqdwmc;
    }

    public String getQqr() {
        return qqr;
    }

    public void setQqr(String qqr) {
        this.qqr = qqr;
    }

    public String getQqrsfzhm() {
        return qqrsfzhm;
    }

    public void setQqrsfzhm(String qqrsfzhm) {
        this.qqrsfzhm = qqrsfzhm;
    }

    public String getCxsy() {
        return cxsy;
    }

    public void setCxsy(String cxsy) {
        this.cxsy = cxsy;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "YqjkParticipation{" +
                "userId='" + userId + '\'' +
                ", qqdwdm='" + qqdwdm + '\'' +
                ", qqdwmc='" + qqdwmc + '\'' +
                ", qqr='" + qqr + '\'' +
                ", qqrsfzhm='" + qqrsfzhm + '\'' +
                ", cxsy='" + cxsy + '\'' +
                ", xm='" + xm + '\'' +
                ", sfzhm='" + sfzhm + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
