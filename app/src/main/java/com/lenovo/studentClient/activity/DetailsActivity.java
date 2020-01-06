package com.lenovo.studentClient.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.ButtonDetailsAdapter;
import com.lenovo.studentClient.adapter.TopDetailsAdapter;
import com.lenovo.studentClient.config.AppConfig;
import com.lenovo.studentClient.myinfo.TransitInfo;
import com.lenovo.studentClient.utils.UpdateInter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 地铁详情页面
 *
 * @author asus
 */
public class DetailsActivity extends BaseActivity {
    private final static String METRO_URL = "GetMetroInfo.do";
    private final static String RATE_URL = "GetEtcRate.do";
    private int IDSite = 0;
    private String Site = "";
    private RecyclerView topRecyclerView, buttonRecyclerView;
    private TextView tMoney, tTopContent, tButtonContent, tTopStartTime, tTopEndTime, tButtonStartTime, tButtonEndTime, tTitle;
    private ArrayList<String> topArrayList = new ArrayList<>();
    private ArrayList<String> buttonArrayList = new ArrayList<>();
    private ArrayList<TransitInfo> transitInfoArrayList = new ArrayList<>();
    private TopDetailsAdapter  adapter;
    private ButtonDetailsAdapter buttonDetailsAdapter;
    private boolean keyData = true;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Site = intent.getStringExtra("Site");
        IDSite = intent.getIntExtra("id", 0);
    }

    @Override
    protected String getLayoutTitle() {
        return "站点查询";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        adapter = new TopDetailsAdapter(this, topArrayList);
        LinearLayoutManager topLinearLayoutManager = new LinearLayoutManager(this);
        topLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topRecyclerView.setLayoutManager(topLinearLayoutManager);
        topRecyclerView.setAdapter(adapter);
        LinearLayoutManager buttonLinearLayoutManager = new LinearLayoutManager(this);
        buttonLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        buttonRecyclerView.setLayoutManager(buttonLinearLayoutManager);
        buttonDetailsAdapter = new ButtonDetailsAdapter(this, buttonArrayList, transitInfoArrayList);
        buttonRecyclerView.setAdapter(buttonDetailsAdapter);
        getDataMetro();
    }

    private  synchronized void getDataMetro() {
        service.execute(() -> {
            buttonArrayList.clear();
            transitInfoArrayList.clear();
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject metroObject = new JSONObject("{\"Line\":0,\"UserName\":\"user1\"}");
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + METRO_URL, metroObject,
                        jsonObject -> {
                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(IDSite);
                                JSONArray array = jsonObject1.getJSONArray("sites");
                                topArrayList.add(jsonObject1.getString("name"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectData = jsonArray.getJSONObject(i);
                                    JSONArray arrayData = objectData.getJSONArray("sites");
                                    if (i != (IDSite)) {
                                        for (int j = 0; j < arrayData.length(); j++) {
                                            String dataMet = arrayData.optString(j);
                                            if (keyData) {
                                                if (dataMet.equals(Site)) {
                                                    topArrayList.add(objectData.getString("name"));
                                                }
                                            }
                                            for (int k = 0; k < array.length(); k++) {
                                                if (dataMet.equals(array.optString(k))) {
                                                    TransitInfo transitInfo = new TransitInfo();
                                                    transitInfo.setTransitName(array.optString(k));
                                                    transitInfo.setTransitSite(objectData.getString("name"));
                                                    transitInfoArrayList.add(transitInfo);
                                                }
                                            }

                                        }
                                    } else {
                                        for (int j = 0; j < arrayData.length(); j++) {
                                            buttonArrayList.add(arrayData.optString(j));
                                        }
                                        JSONArray timeJson = objectData.getJSONArray("time");
                                        for (int j = 0; j < timeJson.length(); j++) {
                                            JSONObject staObject = timeJson.getJSONObject(j);
                                            int finalJ = j;
                                            try {
                                                if (finalJ == 0) {
                                                    tTopContent.setText(staObject.getString("site"));
                                                    tTopStartTime.setText(staObject.getString("starttime"));
                                                    tTopEndTime.setText(staObject.getString("endtime"));
                                                } else {
                                                    tButtonContent.setText(staObject.getString("site"));
                                                    tButtonStartTime.setText(staObject.getString("starttime"));
                                                    tButtonEndTime.setText(staObject.getString("endtime"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                tTitle.setText(Site);
                                if (keyData) {
                                    adapter.notifyDataSetChanged();
                                }
                                buttonDetailsAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, volleyError -> {
                });

                JSONObject RatePostObject = new JSONObject("{\"UserName\":\"user1\"}");
                JsonObjectRequest RateRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + RATE_URL, RatePostObject,
                        jsonObject -> {
                            try {
                                tMoney.setText("全程票价： " + jsonObject.getString("Money") + "元");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, volleyError -> {

                });
                requestQueue.add(RateRequest);
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void initView() {

        topRecyclerView = findViewById(R.id.rv_top);
        buttonRecyclerView = findViewById(R.id.rv_button);
        tMoney = findViewById(R.id.tv_money);
        tTopContent = findViewById(R.id.tv_topContent);
        tTopStartTime = findViewById(R.id.tv_topStartTime);
        tTopEndTime = findViewById(R.id.tv_topEndTime);
        tButtonContent = findViewById(R.id.tv_buttonContent);
        tButtonStartTime = findViewById(R.id.tv_buttonStartTime);
        tButtonEndTime = findViewById(R.id.tv_buttonEndTime);
        tTitle = findViewById(R.id.tv_title);
        TopDetailsAdapter.getClick(parent -> {
               service.execute(()->{
                   InitApp.getHandler().post(()->{
                       Log.d(TAG, "initView: 1");
                      buttonRecyclerView.setOnTouchListener((v, event) -> true);
                   });
                   keyData = false;
                   String SiteName = topArrayList.get(parent);
                   JSONObject metroObject = null;
                   try {
                       metroObject = new JSONObject("{\"Line\":0,\"UserName\":\"user1\"}");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   RequestQueue requestQueue = Volley.newRequestQueue(this);
                   JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + METRO_URL, metroObject,
                           jsonObject -> {
                               try {
                                   JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                                   for (int i = 0; i < jsonArray.length(); i++) {
                                       JSONObject dataName = jsonArray.getJSONObject(i);
                                       if (dataName.getString("name").equals(SiteName)) {
                                           IDSite = (dataName.getInt("id")-1);
                                           getDataMetro();
                                           break;
                                       }
                                   }
                                   InitApp.getHandler().post(()->{

                                   });
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                           }, volleyError -> {

                   });
                   requestQueue.add(request);

               });

        });

        ButtonDetailsAdapter.ChangOnData(() -> {
            Log.d(TAG, "initView: 2");
            buttonRecyclerView.setOnTouchListener((v, event) -> false);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
