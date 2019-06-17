package com.checkin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.checkin.entity.LoginInfo;
import com.checkin.entity.LoginResponseEntity;
import com.checkin.util.api.UserLoginApi;
import com.checkin.util.http.ApiListener;
import com.checkin.util.http.ApiUtil;
import com.checkin.util.http.OkHttpUtil;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    protected Button registerButton;
    protected Button loginButton;
    protected EditText userNameText;
    protected EditText userPwdText;
    protected ImageView quitImageView;

    LoginInfo loginInfo;
    String userName;
    String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = this.findViewById(R.id.btn_register);
        loginButton =  this.findViewById(R.id.loginButton);
        userNameText =  this.findViewById(R.id.edt_username);
        userPwdText =  this.findViewById(R.id.edt_pwd);
        quitImageView = this.findViewById(R.id.close);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        quitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 解析响应数据，使用阿里巴巴的解析工具
     * @param jsonStr
     * @return
     */
    private LoginInfo parseJsonStr(String jsonStr) {
        LoginResponseEntity loginResponseEntity =
                JSON.parseObject(jsonStr,LoginResponseEntity.class);
         return loginResponseEntity.getData();

    }

    /**
     * 用户登录
     */
    private void userLogin() {
        userName = userNameText.getText().toString();
        userPwd = userPwdText.getText().toString();

        /**
         *
         */
        new UserLoginApi(userName, userPwd).sendRequest(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                UserLoginApi userLoginApi = (UserLoginApi)apiUtil;
                if(!TextUtils.isEmpty(userLoginApi.jsonStr)){
                    // 将Json字符串解析成对象
                   loginInfo = parseJsonStr(userLoginApi.jsonStr);
                }
                // 登录成功，修改登录状态
                if(loginInfo.getIsSuccess() == 1){
                    App.isLogin = true;
                    App.workerId = loginInfo.getWorkerId();
                }
                String workerId = String.valueOf(App.workerId);
                String isLogin = String.valueOf(App.isLogin);
                //String debug = loginInfo.getReason() + workerId + isLogin;
                String debug = loginInfo.getReason();
                if (loginInfo.getReason().equals("校验账号密码通过")) {
                    debug = "登录成功";
                }
                Toast.makeText(LoginActivity.this, debug, Toast.LENGTH_LONG ).show();
                /**
                 *  登录成功后修改全局变量
                 *  使用延时操作，让界面显示不突兀
                 */
                if(debug.equals("登录成功")){
                    Intent data = new Intent();
                    data.putExtra("loginState",true );
                    setResult(2, data);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 停顿2秒后执行的动作
                            finish();
                        }
                    }, 1000);
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.e("<<","登录服务器产生错误" );
            }
        }, OkHttpUtil.REQUEST_TYPE.POST);
    }


}
