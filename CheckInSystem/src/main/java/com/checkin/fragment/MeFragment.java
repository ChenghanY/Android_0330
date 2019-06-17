package com.checkin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.checkin.App;
import com.checkin.LoginActivity;
import com.checkin.R;

/**
 * 我的
 */
public class MeFragment extends Fragment {

    protected Button loginButton;
    protected TextView logoutTextView;
    protected Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = getActivity().findViewById(R.id.loginButton);
        logoutButton = getActivity().findViewById(R.id.logoutButton);
        logoutTextView = getActivity().findViewById(R.id.logoutTextView);
        setButtonByIsLogin(App.isLogin);

    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonByIsLogin(App.isLogin);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //登录
                    Intent  login=new Intent(getActivity(),LoginActivity.class);
                    startActivity(login);

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT).show();
                App.isLogin = false;
                setButtonByIsLogin(App.isLogin);
            }
        });
    }

    private void setButtonByIsLogin(boolean isLogin){
        if(isLogin) {
            loginButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            logoutTextView.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
            logoutTextView.setVisibility(View.INVISIBLE);
        }
    }

}
