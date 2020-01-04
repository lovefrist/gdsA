package com.lenovo.studentClient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.SurroundingsAdapter;
import com.lenovo.studentClient.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author asus
 */
public class TransitActivity extends BaseActivity {
    private final static String BUSINFO_URL = "GetBusStationInfo.do";
    private final static String SENSE_URL = "GetAllSense";
    TextView textViewTitle;
    private ArrayList<View> viewArrayList;
    private ViewPager viewPager;
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "一号站台";
    }

    @Override
    protected void onAfter() {

        finish();
    }

    @Override
    protected void initData() {
        viewPager.setAdapter(new SurroundingsAdapter(viewArrayList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面的滚动
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面的位置
            @Override
            public void onPageSelected(int position) {
                changTextBack(position);
                changDataOne();
            }

            //页面的滚动状态
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < textViewArrayList.size(); i++) {
            int finalI = i;
            textViewArrayList.get(i).setOnClickListener(v -> {
                viewPager.setCurrentItem(finalI);
            });
        }

        getDataInfo();
    }

    private void getDataInfo() {
        service.execute(() -> {

            while (true) {
                String json = "{\"BusStationId\":" + (viewPager.getCurrentItem() + 1) + ",\"UserName\":\"user1\"}";
                String jsonindex = "{\"UserName\":\"user1\"}";
                JSONObject indexObject = null;
                JSONObject distanceObject = null;
                try {
                    indexObject = new JSONObject(jsonindex);
                    distanceObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                long starTime = System.currentTimeMillis();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest distanceRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + SENSE_URL, indexObject,
                        jsonObject -> {

                            TextView textView = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_indexAll);
                            try {
                                textView.setText("PM2.5: " + jsonObject.getInt("pm2.5") + "ug/m³， 温度：" + jsonObject.getInt("temperature") + "℃\n湿度" +
                                        jsonObject.getInt("humidity") + "%,CO2: " + jsonObject.getInt("co2") + "PPM");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, volleyError -> {

                });
                requestQueue.add(distanceRequest);

                JsonObjectRequest indexRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + BUSINFO_URL, distanceObject,
                        jsonObject -> {

                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                                TextView textView1 = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_firstDistance);
                                TextView textView2 = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_twoDistance);
                                textView1.setText(jsonArray.getJSONObject(0).getString("Distance") + "m");
                                textView2.setText(jsonArray.getJSONObject(1).getString("Distance") + "m");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, volleyError -> {

                });
                requestQueue.add(indexRequest);
                long entTime = System.currentTimeMillis();
                if ((entTime - starTime) < 3000) {
                    SystemClock.sleep(3000 - (entTime - starTime));
                }

            }
        });
    }

    private void changDataOne() {

        String json = "{\"BusStationId\":" + (viewPager.getCurrentItem() + 1) + ",\"UserName\":\"user1\"}";
        String jsonindex = "{\"UserName\":\"user1\"}";
        JSONObject indexObject = null;
        JSONObject distanceObject = null;
        try {
            indexObject = new JSONObject(jsonindex);
            distanceObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest distanceRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + SENSE_URL, indexObject,
                jsonObject -> {

                    TextView textView = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_indexAll);
                    try {
                        textView.setText("PM2.5: " + jsonObject.getInt("pm2.5") + "ug/m³， 温度：" + jsonObject.getInt("temperature") + "℃\n湿度" +
                                jsonObject.getInt("humidity") + "%,CO2: " + jsonObject.getInt("co2") + "PPM");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError -> {

        });
        requestQueue.add(distanceRequest);

        JsonObjectRequest indexRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + BUSINFO_URL, distanceObject,
                jsonObject -> {

                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                        TextView textView1 = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_firstDistance);
                        TextView textView2 = viewArrayList.get(viewPager.getCurrentItem()).findViewById(R.id.tv_twoDistance);
                        textView1.setText(jsonArray.getJSONObject(0).getString("Distance") + "m");
                        textView2.setText(jsonArray.getJSONObject(1).getString("Distance") + "m");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError -> {

        });
        requestQueue.add(indexRequest);
    }

    private void changTextBack(int position) {
        textViewTitle.setText(position == 0 ? "一号站台" : "二号站台");
        for (int i = 0; i < textViewArrayList.size(); i++) {
            if (i == position) {
                textViewArrayList.get(i).setBackgroundColor(Color.parseColor("#0099cc"));
            } else {
                textViewArrayList.get(i).setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }

    @Override
    protected void initView() {
        viewArrayList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.text_viewpag, null);
            TextView textView = view.findViewById(R.id.textView1);
            textView.setText(i == 0 ? "距离一号站台的距离：" : "距离二号站台的距离：");
            viewArrayList.add(view);
        }
        viewPager = findViewById(R.id.viewPager);
        textViewArrayList.add(findViewById(R.id.tv_firstPlatform));
        textViewArrayList.add(findViewById(R.id.tv_twoPlatform));
        textViewTitle = findViewById(R.id.title);
        textViewTitle.setTextColor(Color.parseColor("#000000"));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_pager;
    }

    @Override
    public void onClick(View v) {

    }
}
