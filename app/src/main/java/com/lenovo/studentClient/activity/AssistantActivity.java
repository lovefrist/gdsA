package com.lenovo.studentClient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.AssistantAdapter;
import com.lenovo.studentClient.config.AppConfig;
import com.lenovo.studentClient.myinfo.AssistantInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * 旅游助手页面
 * @author asus
 */
public class AssistantActivity extends BaseActivity {
    private final static String SPOT_URL = "GetSpotInfo.do";
    private ArrayList<AssistantInfo> assistantInfoList = new ArrayList<>();
    AssistantAdapter assistantAdapter;
    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "旅游助手";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        getSpot();
    }

    private void getSpot() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject spotObject = null;
        try {
            spotObject = new JSONObject("{\"UserName\":\"user1\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest SptsRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + SPOT_URL, spotObject, jsonObject -> {
            try {
                JSONArray dataSpots = jsonObject.getJSONArray("ROWS_DETAIL");
                for (int i = 0; i <dataSpots.length() ; i++) {
                    AssistantInfo info = new AssistantInfo();
                    JSONObject interimObject = dataSpots.getJSONObject(i);
                    info.setImgUrl(interimObject.getString("img"));
                    info.setTicket(interimObject.getInt("ticket"));
                    info.setName(interimObject.getString("name"));
                    assistantInfoList.add(info);
                }
                assistantAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> {

        });
        requestQueue.add(SptsRequest);
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_Travel);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        assistantAdapter = new AssistantAdapter(this,assistantInfoList);
        recyclerView.setAdapter(assistantAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assistant;
    }

    @Override
    public void onClick(View v) {

    }
}
