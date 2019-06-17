package com.checkin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.checkin.entity.LngLatInfo;
import com.checkin.service.LocationService;
import com.checkin.util.CheckSignUtil;
import com.checkin.util.api.SignInAfternoonApi;
import com.checkin.util.api.SignInMorningApi;
import com.checkin.util.http.ApiListener;
import com.checkin.util.http.ApiUtil;
import com.checkin.util.http.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;


public class SignActivity extends Activity {
    private LocationService locationService;
    private Button signButton;
    protected ImageView signLoginImage;
    protected ImageView signSuccessImage;
    protected ImageView signFailImage;
    protected TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        signButton = findViewById(R.id.signButton);
        signLoginImage = findViewById(R.id.signLogo);
        signSuccessImage = findViewById(R.id.success);
        signFailImage = findViewById(R.id.fail);
        textView = findViewById(R.id.textView);
        locationService = new LocationService(this);

        /*
            多个activity
            locationService = ((App) getApplication()).locationService;
         */
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignActivity.this, "按钮已经被点击", Toast.LENGTH_LONG).show();
                if (App.isLogin == true) {
                    locationService.start();
                } else {
                    Toast.makeText(SignActivity.this, "请先登录", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /**
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        Map<String, Object> locationMsg = new HashMap<>();

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuilder sb = new StringBuilder(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());

                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不通导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                locationMsg.put("time",location.getTime());
                locationMsg.put("lng",location.getLongitude());
                locationMsg.put("lat",location.getLatitude());

                locationMsg.put("addr", location.getAddrStr());
                locationMsg.put("description", location.getDirection());
                // 调试可用：String apiMsg = sb.toString(); logMsg(apiMsg)

                // 本地获取签到状态并组装Json
                String[] paramStr = CheckSignUtil.checkSignState(locationMsg) ;
                userSign(paramStr);


            }
        }

    };

    private void userSign(String[] paramStr) {
        String signType = paramStr[0];
        String requestJson = paramStr[1];

        if (signType.equals("morning")) {
            new SignInMorningApi().sendRequest(new ApiListener() {
                @Override
                public void success(ApiUtil apiUtil) {
                    // 获取请求参数并输出到界面
                    changeImage(1,JSON.parseObject(requestJson).getString("reason"));
                }
                @Override
                public void failure(ApiUtil apiUtil) {
                    changeImage(0,"上午签到服务器返回错误");
                    Log.e("<<","上午签到服务器返回错误" + paramStr[1] );
                }
            }, requestJson, OkHttpUtil.REQUEST_TYPE.POST);

        } else if (signType.equals("afternoon")){
            new SignInAfternoonApi().sendRequest(new ApiListener() {
                @Override
                public void success(ApiUtil apiUtil) {
                    changeImage(1,JSON.parseObject(requestJson).getString("reason"));
                }

                @Override
                public void failure(ApiUtil apiUtil) {
                    changeImage(0,"下午签到服务器返回错误");
                    Log.e("<<","下午签到服务器返回错误" + paramStr[1] );
                }
            }, requestJson, OkHttpUtil.REQUEST_TYPE.POST);

        } else {
            changeImage(0, "超出公司签到范围，签到失败");
        }
        locationService.stop();
    }

    /**
     * 根据API返回的值确定界面图片的显示
     * @param i
     */
    private void changeImage(int i, String str) {
        if (i == 1) {
            signLoginImage.setVisibility(View.INVISIBLE);
            signFailImage.setVisibility(View.INVISIBLE);
            signSuccessImage.setVisibility(View.VISIBLE);
            textView.setText(str);

        } else {
            signLoginImage.setVisibility(View.INVISIBLE);
            signSuccessImage.setVisibility(View.INVISIBLE);
            signFailImage.setVisibility(View.VISIBLE);
            textView.setText(str);
        }
    }

}
