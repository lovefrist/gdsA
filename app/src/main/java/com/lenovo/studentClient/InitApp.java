package com.lenovo.studentClient;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lenovo.studentClient.config.AppConfig;

/**
 *初始化加载的类
 * @author asus
 */
public class InitApp extends Application {
    private static InitApp initApp;
    private static Context context;
    private static Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp = this;
        context = getApplicationContext();
        handler = new Handler();
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    public static InitApp getInitApp() {
        return initApp;
    }
}
