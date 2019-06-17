package com.checkin.util.api;

import com.checkin.util.http.ApiUtil;
import com.checkin.util.http.URL;

import org.json.JSONObject;

public class SignInMorningApi extends ApiUtil {
    public String jsonStr;

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        jsonStr = jsonObject.toString();
    }

    @Override
    protected String getUrl() {
        return URL.IP + "/sign/in/morning";
    }
}
