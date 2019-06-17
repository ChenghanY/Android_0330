package com.checkin.util;

public class CheckFormUtil {

    private static final String[] result = new String[2];

    public static String[] checkRegisterForm(String userName,String userPwd, String userConfirmPwd) {

        if (userName.equals("")) {
            result[0] = "0";
            result[1] = "用户名不能为空";
        } else if (userPwd.equals("")) {
            result[0] = "0";
            result[1] = "密码不能为空";
        } else {
            if (userConfirmPwd.equals(userPwd)) {
                result[1] = "注册成功";
                result[0] = "1";
            } else {
                result[1] = "两次密码不一致";
                result[0] = "0";
            }
        }
        return result;
    }
}
