package com.dragonsoft.yqjk.entity;

/**
 * 响应返回结果的实体
 * @author ronin
 */
public class ResponseMessage {
    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "300";
    public static final String SESSION_TIMEOUT_CODE = "301";
    public static final String DEFAULT_TITLE = "提示";
    private String statusCode;
    private String message;

    public ResponseMessage() {
        this("200", "操作成功");
    }

    public ResponseMessage(String message) {
        this("200", "操作成功");
    }

    public ResponseMessage(String status, String message) {
        this.statusCode = status;
        this.message = message;
        if (this.message == null) {
            if ("200".equals(status)) {
                this.message = "操作成功";
            } else {
                this.message = "操作失败";
            }
        }

    }

    public static ResponseMessage fail() {
        return fail("300", (String)null);
    }

    public static ResponseMessage fail(String message) {
        return new ResponseMessage("300", message);
    }

    public static ResponseMessage fail(String status, String message) {
        return new ResponseMessage(status, message);
    }

    public static ResponseMessage success() {
        return success((String)null);
    }

    public static ResponseMessage success(String message) {
        return new ResponseMessage("200", message);
    }

    public Boolean isSuccess() {
        return "200".equals(this.statusCode);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
