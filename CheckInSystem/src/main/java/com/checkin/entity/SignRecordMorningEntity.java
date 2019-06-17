package com.checkin.entity;


public class SignRecordMorningEntity {
    private int id;
    private String signStartTime;
    private Integer isOntime;
    private String reason;
    private int workerId;

    public SignRecordMorningEntity() {
    }

    public SignRecordMorningEntity(int id, String signStartTime, Integer isOntime, String reason, int workerId) {
        this.id = id;
        this.signStartTime = signStartTime;
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

    public String getSignStartTime() {
        return signStartTime;
    }

    public void setSignStartTime(String signStartTime) {
        this.signStartTime = signStartTime;
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
        return "SignRecordMorningEntity{" +
                "id=" + id +
                ", signStartTime='" + signStartTime + '\'' +
                ", isOntime=" + isOntime +
                ", reason='" + reason + '\'' +
                ", workerId=" + workerId +
                '}';
    }
}
