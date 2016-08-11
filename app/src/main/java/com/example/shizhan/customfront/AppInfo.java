package com.example.shizhan.customfront;

import com.example.shizhan.customfront.model.User;

/**
 * Created by Administrator on 2016/8/4.
 */
//获取用户名
public class AppInfo {
public static User mUser=null;
    public static User getUser(){
        return mUser;
    }
    public static void setUser(User user){
        mUser=user;
    }
    //IP地址
    public static  String BASE_URL="http://192.168.1.103:8080/";
}
