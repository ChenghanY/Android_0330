package com.checkin.util.http;

/**
 *  最终取到数据要做的操作（最外层）
 *  只有实例化ApiUtil后才能完成业务逻辑
 */
public interface ApiListener {

    //public abstract 为接口的默认修饰符
    void success(ApiUtil apiUtil);

    void failure(ApiUtil apiUtil);
}
