package com.checkin.util.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class OkHttpCallBack implements Callback {

    public abstract void onSuccess(final Call call, JSONObject jsonObject);
    public Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(Call call, IOException e){
        Log.e("<<","OkHttpCallBack类出错" );
    }

    /**
     *  默认可以ApiListener.success里面访问UI，因为这里开辟了一个新的线程
     * @throws IOException
     */
    @Override
    public void onResponse(final Call call, Response response) throws IOException {
        if (response != null) {
            if (response.isSuccessful()) {
                try {
                    String str = response.body().string().trim();
                    final  JSONObject jsonObject = (JSONObject) new JSONTokener(str).nextValue();
                    if (jsonObject != null) {
                        //  默认回到UI线程
                        if (isPostInMainThread()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onSuccess(call, jsonObject);
                                }
                            });
                        } else {
                            onSuccess(call, jsonObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (isPostInMainThread()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("<<","okHttpCallBack返回错误" );
                            onFailure(call,null );
                        }
                    });
                } else {
                    onFailure(call,null );
                }
            }
        }
    }

    protected boolean isPostInMainThread() {
        return true;
    }
}
