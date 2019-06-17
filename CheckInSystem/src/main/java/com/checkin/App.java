package com.checkin;

import android.app.Application;

import com.checkin.service.LocationService;
import com.checkin.util.http.OkHttpUtil;

public class App extends Application {
    public LocationService locationService;
    public static boolean isLogin;
    public static int workerId;
    public static double lat ;
    public static double lng ;

    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(this);
        OkHttpUtil.init();
        isLogin = false;
        workerId = -1;
        // 获取动态经纬度失败就使用当前设定好的默认值
        lat = 22.369249;
        lng = 113.545741;
        /* 调试用
        isLogin = true;
        workerId = 11;
         */
    }
}
