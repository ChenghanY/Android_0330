package com.checkin.entity;

public class LoginInfo {
    private Integer isSuccess;
    private String reason;
    private Integer workerId;

    public LoginInfo() {
    }

    public LoginInfo(Integer isSuccess, String reason, Integer workerId) {
        this.isSuccess = isSuccess;
        this.reason = reason;
        this.workerId = workerId;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }
}
