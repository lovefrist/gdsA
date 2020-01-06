package com.lenovo.studentClient.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.MenuListAdapter;
import com.lenovo.studentClient.bean.MenuModel;
import com.lenovo.studentClient.config.AppConfig;
import com.lenovo.studentClient.utils.ClickInter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 框架的主页
 * @author asus
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    private SlidingMenu mMenu;
    private ListView mLeftMenuLV;

    private Button btnSetCarAction;
    private TextView tvCarActionResult;
    private NewWork work;

    private static final String TAG = "MainActivity";

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onRequestFinish();
            switch (msg.what) {
                case 0:
                    tvCarActionResult.setText(msg.obj == null ? "" : msg.obj.toString());
                    break;
                case 1:
                    tvCarActionResult.setText(msg.obj.toString());
                    break;
                case -1:
                    tvCarActionResult.setText(msg.obj.toString());
                    break;
                default:
            }

        }
    };


    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return getString(R.string.title_main);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        initSlidingMenu();
        initLeftMenu();
        btnSetCarAction = findViewById(R.id.btn_setCarAction);
        tvCarActionResult = findViewById(R.id.tv_carActionResult);
        initListener();


    }


    /**
     * 初始化菜单栏
     */
    private void initLeftMenu() {
        View view = mMenu.getMenu();

        mLeftMenuLV = view.findViewById(R.id.listView1);

        prepare4ListView();
    }

    private void prepare4ListView() {
        List<MenuModel> list = new ArrayList<>();
        String[] lvs = {"车辆违章", "离线地图", "环境检测", "公交查询", "实时交通","旅行助手","设置"};
        int[] icons = {
                R.mipmap.car, R.mipmap.offline,R.mipmap.surroundings, R.mipmap.bus_query, R.mipmap.liveing,R.mipmap.sort_travel,R.mipmap.set
        };
        for (int i = 0; i < lvs.length; i++) {
            list.add(new MenuModel(icons[i], lvs[i]));
        }

        MenuListAdapter adapter = new MenuListAdapter(this, list);
        mLeftMenuLV.setAdapter(adapter);
        Context context = this;
        MenuListAdapter.getOnclick(new ClickInter() {
            @Override
            public void onClick(int parent) {
                switch (parent){
                    //第一个Item的标识符
                    case 0:
                        mMenu.toggle();
                        startActivity(new Intent(context,CarQueryActivity.class));
                        break;
                    case 1:
                        mMenu.toggle();
                        startActivity(new Intent(context,MapActivity.class));
                        break;
                    case 2:
                        mMenu.toggle();
                        startActivity(new Intent(context,SurroundingsActivity.class));
                        break;
                    case 3:
                        mMenu.toggle();
                        startActivity(new Intent(context, TransitActivity.class));
                        break;
                    case 4:
                        mMenu.toggle();
                        startActivity(new Intent(context, TrafficActivity.class));
                        break;
                    case 5:
                        mMenu.toggle();
                        startActivity(new Intent(context,AssistantActivity.class));
                        break;
                    case 6:
                        mMenu.toggle();
                        startActivity(new Intent(MainActivity.this, IpSetActivity.class));
                        break;
                    default:
                }
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化侧边菜单栏
     */
    private void initSlidingMenu() {
        mMenu = new SlidingMenu(this);
        //设置mMenu的位置在哪里
        mMenu.setMode(SlidingMenu.LEFT);
        //运行使用手势打开
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置后面的偏移量。
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置后面的宽度
        mMenu.setBehindWidth(700);
        //设置SlidingMenu淡入和淡出的数量
        mMenu.setFadeDegree(1f);
        //将SlidingMenu附加到整个Activity
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        //设置布局
        mMenu.setMenu(R.layout.leftmenu1);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onAfter() {
        //切换滑动菜单
        mMenu.toggle();
    }

    @SuppressLint("StaticFieldLeak")
    private void initListener() {
        btnSetCarAction.setOnClickListener(v -> {
            MainActivity.this.showLoadingDialog();
            service.execute(
                    () -> {
                        URL url;
                        try {
                            url = new URL(AppConfig.BASE_URL + "GetAllSense.do");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("POST"); // 设置请求方式
                            conn.setRequestProperty("Charset", "UTF-8");// 设置编码格式
                            conn.setUseCaches(false);
                            conn.setInstanceFollowRedirects(true);
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.connect();
                            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("UserName", "user1");
                            out.writeBytes(jsonObj.toString());
                            //读取响应
                            int responseCode = conn.getResponseCode();
                            if (responseCode == 200) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String lines = reader.readLine();
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = lines;
                                handler.sendMessage(msg);
                                reader.close();
                            } else {
                                Message msg = Message.obtain();
                                msg.what = 1;
                                msg.obj = "请求失败，请求码为：" + responseCode;
                                handler.sendMessage(msg);
                            }
                            // 断开连接
                            out.flush();
                            out.close();
                            conn.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = Message.obtain();
                            msg.what = -1;
                            msg.obj = "出错了：" + e;
                            handler.sendMessage(msg);
                        }
                    });
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果根活动，则为true，否则为false。
        Log.d(TAG, "onKeyDown: " + isTaskRoot());
        //返回按键不销毁退出
        moveTaskToBack(true);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
    }

    @Override
    protected void onDestroy() {

        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }





}


