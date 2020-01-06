package com.lenovo.studentClient.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.ExpandAdapter;
import com.lenovo.studentClient.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 实时交通页面
 *
 * @author asus
 */
public class TrafficActivity extends BaseActivity {
    private final static String METRO_URL = "GetMetroInfo.do";
    PopupWindow popupWindow;
    View view;
    private ExpandableListView expandableListView;
    private ArrayList<String> groudList = new ArrayList<>();
    private ArrayList<String>[] childArrayList = new ArrayList[8];
    private ExpandAdapter expandAdapter;
    TextView textView;
    private static String startSite = "";
    private static String endSite ="";
    private int group = 0;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "实时交通";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        view = LayoutInflater.from(this).inflate(R.layout.dialog_expand, null);
        //这里的View代表popupMenu需要依附的view
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置点击外面消失
        popupWindow.setOutsideTouchable(true);
        view.findViewById(R.id.ll_outset).setOnClickListener(this);
        view.findViewById(R.id.ll_end).setOnClickListener(this);
        view.findViewById(R.id.tv_details).setOnClickListener(this);
        getDataMetro();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Log.d(TAG, "onChildClick: "+expandableListView.getSelectedItem());
//                expandableListView.setSelectedChild(groupPosition,childPosition,true);
                group = groupPosition;
                textView = view.findViewById(R.id.tv_site);
                textView.setText(childArrayList[groupPosition].get(childPosition));
                popupWindow.showAsDropDown(v, 0, -200);
                return false;
            }
        });
    }


    private void getDataMetro() {
        service.execute(() -> {
            String jsonData = "{\"Line\":0,\"UserName\":\"user1\"}";
            JSONObject postObject = null;
            try {
                postObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + METRO_URL, postObject
                    , jsonObject -> {

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        childArrayList[i] = new ArrayList<>();
                        JSONObject allMetroObject = jsonArray.getJSONObject(i);
                        groudList.add(allMetroObject.getString("name"));
                        JSONArray contentArray = allMetroObject.getJSONArray("sites");
                        for (int j = 0; j < contentArray.length(); j++) {
                            childArrayList[i].add(contentArray.optString(j));
                        }
                    }
                    InitApp.getHandler().post(() -> {
                        expandAdapter.notifyDataSetChanged();
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, volleyError -> {

            });
            requestQueue.add(request);
        });
    }

    @Override
    protected void initView() {
        expandableListView = findViewById(R.id.expandableListView);
        expandAdapter = new ExpandAdapter(this, groudList, childArrayList);
        expandableListView.setAdapter(expandAdapter);
        TextView textView = findViewById(R.id.tv_map);
        textView.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_traffic;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.ll_outset:
                startSite = textView.getText().toString();
                break;
            case R.id.ll_end:
                endSite = textView.getText().toString();
                break;
            case R.id.tv_details:
                Intent intent = new Intent(this,DetailsActivity.class);
                intent.putExtra("Site",textView.getText().toString());
                intent.putExtra("id",group);
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }
}
