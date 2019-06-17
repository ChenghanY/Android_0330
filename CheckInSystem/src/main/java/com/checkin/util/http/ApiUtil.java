package com.checkin.util.http;


import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;


/**
 * 处理服务端的回调
 */
public abstract class ApiUtil {

    // 拥有一个监听器接口的成员变量
    private  ApiListener apiListener = null;
    private String status;

    // 调用该方法的构造函数后自动初始化参数，不传参数则设为null
    public HashMap<String, String> bodyMap = new HashMap<>();

    // 抽象方法
    protected abstract void parseData(JSONObject jsonObject) throws  Exception ;
    protected abstract String getUrl();

    private boolean isSuccess() {
        return  ("0".equals(status))||("200".equals(status));
    }

    /**
     * GET 请求
     * @param listener 告诉客户端请求是否成功
     */
    public void get(ApiListener listener) {
        apiListener = listener;
        OkHttpUtil.get(getUrl(), sendListener, bodyMap);
    }

    /**
     *  POST/PUT/DELETE 请求方式一：传入Map
     * @param listener 告诉客户端请求是否成功
     */
    public void sendRequest(ApiListener listener, OkHttpUtil.REQUEST_TYPE type) {
        // 发送请求必须要改写监听器的实现
        apiListener = listener;
        // 把封装好的CallBack传给OkHttpUtil去处理
        OkHttpUtil.sendRequest(getUrl(), sendListener, bodyMap, type);
    }

    /**
     * POST/PUT/DELETE 请求方式二：传入解析好的JSON字符串
     *
     */
    public void sendRequest(ApiListener listener, String jsonStr, OkHttpUtil.REQUEST_TYPE type) {
        // 发送请求必须要改写监听器的实现
        apiListener = listener;
        // 把封装好的CallBack传给OkHttpUtil去处理
        OkHttpUtil.sendRequest(getUrl(), sendListener, jsonStr, type);
    }

    private OkHttpCallBack sendListener = new OkHttpCallBack() {
        @Override
        protected boolean isPostInMainThread() {
            return isBackInMainThread();
        }

        /**
         * 若请求成功，获取上层JSONObject,解析出状态码。为200或者0则解析data，否则将自身的应用抛出给调用者【最外层】，让failure自己处理
         *
         */
        @Override
        public void onSuccess(Call call, JSONObject jsonObject) {
            status = jsonObject.optString("status");
            if (isSuccess()) {
                try {
                    parseData(jsonObject);
                    apiListener.success(ApiUtil.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("<<<","ApiUtil请求错误" );
                apiListener.failure(ApiUtil.this);
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            apiListener.failure(ApiUtil.this);
        }
    };

    /**
     * 提供追加参数的方法，具体要在子类去实现
     */
    public void addParams(String key, String value) {
        bodyMap.put(key,value);
    }

    /**
     * 默认是返回主线程
     * @return
     */
    protected boolean isBackInMainThread() {
        return true;
     }
}
