package com.checkin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.checkin.entity.LngLatInfo;
import com.checkin.entity.LngLatResponseEntity;
import com.checkin.fragment.FindFragment;
import com.checkin.fragment.MainFragment;
import com.checkin.fragment.MeFragment;
import com.checkin.util.api.GetLngLatApi;
import com.checkin.util.http.ApiListener;
import com.checkin.util.http.ApiUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    LngLatInfo lngLatInfo;
    protected LinearLayout mMenuMain;
    protected LinearLayout mMenuFind;
    protected LinearLayout mMenuMe;
    protected MainFragment mMainFragment=new MainFragment();//首页
    protected FindFragment mFindFragmenr=new FindFragment();//发现
    protected MeFragment mMeFragment=new MeFragment();//我的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLnglat();
        initView();
        //获取管理类
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content,mMainFragment)
                .add(R.id.container_content,mFindFragmenr)
                .hide(mFindFragmenr)
                .add(R.id.container_content,mMeFragment)
                .hide(mMeFragment)
                //事物添加  默认：显示首页  其他页面：隐藏
                //提交
                .commit();
    }

    private void initLnglat() {
        new GetLngLatApi().get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                // 请求成功后回调，拿到自身的引用
                GetLngLatApi getLngLatApi = (GetLngLatApi)apiUtil;
                // 在回调中拿到已经解析好的Json字符串：jsonStr
                if(!TextUtils.isEmpty(getLngLatApi.jsonStr)){
                    // 将Json字符串解析成对象
                    lngLatInfo = parseJsonStr(getLngLatApi.jsonStr);
                    // 修改默认的企业经纬度
                    String[] temp = lngLatInfo.getLngLat().split(",");
                    App.lng = Double.valueOf(temp[0]);
                    App.lat = Double.valueOf(temp[1]);
                }
            }
            @Override
            public void failure(ApiUtil apiUtil) {
                Toast.makeText(MainActivity.this, "获取企业定位坐标服务器返回错误", Toast.LENGTH_SHORT).show();
                Log.e("<<","获取企业定位坐标服务器返回错误" );
            }
        });
    }

    /**
     * 解析响应数据，使用阿里巴巴的解析工具
     * @param jsonStr
     * @return
     */
    private LngLatInfo parseJsonStr(String jsonStr) {
        LngLatResponseEntity LngLatResponseEntity =
                JSON.parseObject(jsonStr, LngLatResponseEntity.class);
        return LngLatResponseEntity.getData();

    }

    /**
     * 初始化视图
     */
    public void initView(){
        mMenuMain= (LinearLayout) this.findViewById(R.id.menu_main);
        mMenuFind= (LinearLayout) this.findViewById(R.id.menu_find);
        mMenuMe= (LinearLayout) this.findViewById(R.id.menu_me);

        mMenuMain.setOnClickListener(this);
        mMenuFind.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.menu_main://首页
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMainFragment)
                        .hide(mFindFragmenr)
                        .hide(mMeFragment)
                        .commit();
                break;
            case  R.id.menu_find://发现
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mFindFragmenr)
                        .hide(mMeFragment)
                        .commit();
                break;
            case  R.id.menu_me://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .hide(mFindFragmenr)
                        .show(mMeFragment)
                        .commit();
                break;
        }
    }
}
