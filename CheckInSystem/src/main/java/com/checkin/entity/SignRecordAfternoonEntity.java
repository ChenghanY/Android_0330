package com.checkin.entity;

public class SignRecordAfternoonEntity {
    private int id;
    private String signEndTime;
    private Integer isOntime;
    private String reason;
    private int workerId;

    public SignRecordAfternoonEntity() {
    }

    public SignRecordAfternoonEntity(int id, String signEndTime, Integer isOntime, String reason, int workerId) {
        this.id = id;
        this.signEndTime = signEndTime;
        this.isOntime = isOntime;
        this.reason = reason;
        this.workerId = workerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignEndTime() {
        return signEndTime;
    }

    public void setSignEndTime(String signEndTime) {
        this.signEndTime = signEndTime;
    }

    public Integer getIsOntime() {
        return isOntime;
    }

    public void setIsOntime(Integer isOntime) {
        this.isOntime = isOntime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Override
    public String toString() {
        return "SignRecordAfternoonEntity{" +
                "id=" + id +
                ", signEndTime='" + signEndTime + '\'' +
                ", isOntime=" + isOntime +
                ", reason='" + reason + '\'' +
                ", workerId=" + workerId +
                '}';
    }
}
