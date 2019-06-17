package com.checkin.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.checkin.entity.SignRecordAfternoonEntity;
import com.checkin.entity.SignRecordMorningEntity;
import com.checkin.App;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CheckSignUtil {

    public static double getDistance(double lng_a, double lat_a, double lng_b, double lat_b ){
        double pk = 180 / 3.14169;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6371000 * tt;
    }

    /**
     * 提供给外部调用，封装了所有本类的方法
     * @param locationMsg
     * @return
     */
    public static String[] checkSignState(Map<String, Object> locationMsg) {
        // 设置返回参数，第一个是状态标识，第二个是请求接口的URL
        String[] result = new String[2];
        // 抽出百度数据的时间
        String time = locationMsg.get("time").toString();
        Log.i("time", time);
        String description;
        // 抽出百度数据的经纬度
        double lng = (Double)locationMsg.get("lng");
        double lat = (Double)locationMsg.get("lat");

        // 计算目前坐标与公司坐标的距离
        Object[] signAllowState = CheckSignUtil.signDistanceState(lng, lat);
        // 如果大于签到范围，则前端提示错误，直接失败
        if ((Integer)signAllowState[0] == 0) {
            result[0] = "outOfRange";
            // 超出范围则不再执行，直接返回错误
            return result;
        }
        // 初始化实体参数
        int workerId = App.workerId;
        int recordId = initRecordId(workerId);
        int isOntime = 0;
        String reason;

        // 从日期中抽取时间存入数组
        String baiduStr = (String)locationMsg.get("time");
        String[] timeStr = baiduStr.split(" ");

        // 获取是否迟到/早退的状态码
        int signTimeCode = CheckSignUtil.signTimeState(timeStr[1]);

        switch (signTimeCode) {
            case 80 :
                isOntime = 1; reason = "上午签到成功";break;
            case -80 :
                isOntime = 0; reason = "签到成功，您已迟到"; break;
            case 170 :
                isOntime = 1; reason = "下午签到成功";break;
            case -170 :
                isOntime = 0; reason = "下午签到成功，您已早退"; break;
            default:
                result[0] = "error";
                result[1] = "获取签到状态码异常，请联系管理员";
                return  result;
        }

        if (signTimeCode == 80 || signTimeCode == -80) {
            SignRecordMorningEntity signRecordMorningEntity = new SignRecordMorningEntity(recordId, time, isOntime, reason, workerId);
            result[0] = "morning";
            String jsonString = JSON.toJSONString(signRecordMorningEntity);
            result[1] = jsonString;

        } else {
            SignRecordAfternoonEntity signRecordAfternoonEntity = new SignRecordAfternoonEntity(recordId, time, isOntime, reason, workerId);
            result[0] = "afternoon";
            String jsonString = JSON.toJSONString(signRecordAfternoonEntity);
            result[1] = jsonString;
        }
        return result;

    }

    /**
     * 根据距离算法检测是否符合超出签到范围
     * @param userLng
     * @param userLat
     * @return
     */
    public static Object[] signDistanceState(double userLng, double userLat) {
        Object[] result = new Object[2];
        int signStateCode = -1;
        double enterpriseLng = App.lng;
        double enterpriseLat = App.lat;
        // 宝能城市广场 114.254867,22.731053
        // 城市花园     114.246082,22.733149
        double distance = getDistance(enterpriseLng, enterpriseLat, userLng, userLat);
        DecimalFormat df = new DecimalFormat("#.00");
        if (distance > 500) {
            // description调试用
            String description = "超出签到范围,距离公司距离为：" + df.format(distance);
            // result为1时签到记录生成成功，并未处理是否迟到和早退
            result[0] = 0; result[1] = description;
            return  result;

        } else {
            String description = "签到成功,距离公司距离为：" + df.format(distance);
            result[0] = 1; result[1] = description;
            return  result;
        }
    }

    /**
     * 根据百度API抽取的时间进行解析，判断是否异常
     * @param str
     * @return
     */
    public static int signTimeState(String str) {
        // 早晚签到的状态码
        int moriningStateCode = -1;
        int afteroonStateCode = -1;

        Time ruleMorningEndTime = new Time(12,0,0);
        Time ruleMorningStartTime = new Time(8,30,0);
        Time ruleAfternoonEndTime = new Time(17,30,0);

        Time userSignTime = Time.valueOf(str);


        if ( userSignTime.before(ruleMorningEndTime)) {
            // 用户正在进行【上班】签到
            if (userSignTime.before(ruleMorningStartTime)) {
                // 早上正常出勤
                moriningStateCode = 80;
            } else {
                //  迟到
                moriningStateCode = -80;
            }
            return moriningStateCode;
        }   else {
            // 用户正在进行【下班】签到
            if (userSignTime.after(ruleAfternoonEndTime)) {
                // 下午正常出勤
                afteroonStateCode = 170;
            } else{
                // 早退
                afteroonStateCode = -170;
            }
            return afteroonStateCode;
        }
    }

    /**
     * 初始化签到Id
     * @param workerId
     * @return
     */
    public static int initRecordId(int workerId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateStr = sdf.format(date);
        int recordId = Integer.valueOf(dateStr + workerId);
        return  recordId;
    }

}
