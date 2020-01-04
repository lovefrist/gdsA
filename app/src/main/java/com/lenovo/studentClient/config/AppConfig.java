
package com.lenovo.studentClient.config;


import android.util.Log;

/**
 * 常数类
 * @author asus
 */
 public  class  AppConfig {
    private static String IP = "192.168.3.5";
    private static final int PORT = 8088;
    public static String BASE_URL = "http://" + IP + ":" + PORT + "/transportservice/action/";
    public static void setURL(){
        BASE_URL = "http://" + IP + ":" + PORT + "/transportservice/action/";
    }
    public static final String SET_CAR_ACTION = "SetCarMove.do";
    public static final String KEY_CAR_ID = "CarId";
    public static final String KEY_CAR_ACTION = "CarAction";
}
