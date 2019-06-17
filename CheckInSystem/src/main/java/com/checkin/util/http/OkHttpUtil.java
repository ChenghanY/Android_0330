package com.checkin.util.http;

import com.alibaba.fastjson.JSON;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 负责初始化0kHttpClient，在直接CallBack上面封装一层，也就是继承CallBack加入一些可重用的逻辑
 */
public class OkHttpUtil {
    // 用于初始化
    private static OkHttpClient okHttpClient = null;

    // 定义请求类型
    public enum REQUEST_TYPE {
        POST, PUT, DELETE
    }

    public static void init() {
        if (okHttpClient == null) {
            // 建造者模式，显式配置
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS);
            okHttpClient = builder.build();
        }
    }

    /**
     * Get请求
     *
     * @param url
     * @param okHttpCallBack
     */
    public static void get(String url, OkHttpCallBack okHttpCallBack, HashMap<String, String> bodyMap) {
        Call call = null;
        HashMap<String, String> commonHashMap = bodyMap;
        try {
            // 拼接参数
            url = getFinalString(url, commonHashMap);
            Request request = new Request.Builder().url(url).build();
            call = okHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * http请求方式一 ：使用解析好的json字符串构建RequestBody
     * @param url
     * @param okHttpCallBack
     * @param jsonStr 解析好的json字符
     * @param type
     */
    public static void sendRequest(String url, OkHttpCallBack okHttpCallBack, String jsonStr, REQUEST_TYPE type) {
        Call call = null;

        try {
            // 直接传入解析好的JSON字符串
            RequestBody body = null;
            if(jsonStr != null){
                body = FormBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
            }
            Request request = null;
            switch (type) {
                case POST:
                    request = new Request.Builder().post(body).url(url).build();
                    break;
                case PUT:
                    request = new Request.Builder().put(body).url(url).build();
                    break;
                case DELETE:
                    request = new Request.Builder().delete(body).url(url).build();
                    break;
            }
            call = okHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * http请求方式二：使用map构建RequestBody
     * @param url   请求的URL
     * @param okHttpCallBack
     * @param bodyMap  请求参数Map
     * @param type     请求类型
     */
    public static void sendRequest(String url, OkHttpCallBack okHttpCallBack, HashMap<String, String> bodyMap, REQUEST_TYPE type) {
        Call call = null;

        try {
            // 将传入参数bodyMap转换为json
            RequestBody body = null;
            if(bodyMap != null){
                String jsonStr = JSON.toJSONString(bodyMap);
                body = FormBody.create(MediaType.parse("application/json; charset=utf-8"),jsonStr);
            }
            Request request = null;
            switch (type) {
                case POST:
                    request = new Request.Builder().post(body).url(url).build();
                    break;
                case PUT:
                    request = new Request.Builder().put(body).url(url).build();
                    break;
                case DELETE:
                    request = new Request.Builder().delete(body).url(url).build();
                    break;
            }
            call = okHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到追加参数的URL
     *
     * @param url
     * @param urlParamsMap
     * @return
     */
    public static String getFinalString(String url, HashMap<String, String> urlParamsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (urlParamsMap.isEmpty()) {
            return url;
        }
        try {
            for (HashMap.Entry<String, String> entry : urlParamsMap.entrySet()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                // 这里进行了URLEncoder编码
                stringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            String paraString = stringBuilder.toString();
            if (url.contains("?")) {
                url += ("&" + paraString);
            } else {
                url += ("?" + paraString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

}
