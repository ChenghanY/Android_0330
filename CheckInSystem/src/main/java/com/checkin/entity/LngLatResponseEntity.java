package com.checkin.entity;

public class LngLatResponseEntity {
    private LngLatInfo data;
    public Integer status;
    public String msg;
    public LngLatResponseEntity() {
    }

    public LngLatInfo getData() {
        return data;
    }

    public void setData(LngLatInfo data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
