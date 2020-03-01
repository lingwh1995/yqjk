package com.dragonsoft.yqjk.entity;
import	java.io.Serializable;

/**
 * 从公安部开放的接口中获取到的TOKEN数据对应实体
 * @author ronin
 */
public class YqjkToken implements Serializable{
    private static final long serialVersionUID = -6089634814229844823L;
    private String access_token;
    private String refresh_token;
    private Long expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "YqjkToken{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
