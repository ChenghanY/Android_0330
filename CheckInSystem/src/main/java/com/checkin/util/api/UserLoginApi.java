package com.checkin.util.api;

import com.checkin.util.http.ApiUtil;
import com.checkin.util.http.URL;

import org.json.JSONObject;

public class UserLoginApi extends ApiUtil {

    // 暂存一个JSON字符串
    public String jsonStr;

    public UserLoginApi(String userName, String userPwd) {
        addParams("userName", userName);
        addParams("userPwd", userPwd);
    }

    /**
     * 默认的解析方式
     *
     */
    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        jsonStr = jsonObject.toString();
    }

    @Override
    protected String getUrl() {
        return URL.IP + "/login";
    }
}
