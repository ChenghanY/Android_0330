package com.checkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.checkin.R;
import com.checkin.util.CheckFormUtil;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {

    protected Button submitButton;
    protected EditText userNameEditText;
    protected EditText userPwdEditText;
    protected EditText userConfirmPwdEditText;
    protected ImageView quitImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submitButton = findViewById(R.id.submitButton);
        userNameEditText = findViewById(R.id.userName);
        userPwdEditText = findViewById(R.id.userPwd);
        userConfirmPwdEditText = findViewById(R.id.userConfirmPwd);
        quitImageView = findViewById(R.id.close);
        submitButton.setOnClickListener(new View.OnClickListener() {
            String userName;
            String userPwd;
            String userConfirmPwd;
            String[] checkResult = new  String[2];
            @Override
            public void onClick(View v) {
                userName = userNameEditText.getText().toString();
                userPwd = userPwdEditText.getText().toString();
                userConfirmPwd = userConfirmPwdEditText.getText().toString();
                checkResult = CheckFormUtil.checkRegisterForm(userName, userPwd, userConfirmPwd);
                Toast.makeText(RegisterActivity.this, checkResult[1], Toast.LENGTH_SHORT).show();
                // 表单验证通过即返回登录页
                if (checkResult[0].equals("1")) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 停顿2秒后执行的动作
                            finish();
                        }
                    }, 6000);
                }
            }
        });

        quitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
