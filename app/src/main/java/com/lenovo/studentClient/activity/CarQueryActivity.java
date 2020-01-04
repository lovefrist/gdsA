package com.lenovo.studentClient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.services.geocoder.RegeocodeQuery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.api.ApiService;
import com.lenovo.studentClient.config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 查询违章
 * @author asus
 */
public class CarQueryActivity extends BaseActivity {
    EditText editText;
    Button button;
    private static final String TAG = "CarQueryActivity";

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "车辆违章";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {

    }

    private void getCatData() {
//        RequestQueue

//        {"UserName":"user1","carnumber":"鲁B10001"}
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject PostObject = new JSONObject();
            PostObject.put("UserName","user1");
            PostObject.put("carnumber","鲁"+editText.getText().toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL+"GetCarPeccancy.do", PostObject, jsonObject1 -> {
                Intent intent = new Intent(this,ViolationActivity.class);
                intent.putExtra("carInfo",jsonObject1.toString());
                startActivity(intent);
                Log.d(TAG, "getCatData: "+jsonObject1.toString());
            }, volleyError -> {
                Toast.makeText(this,"没有查询到 鲁"+editText.getText().toString()+"车的违章数据！",Toast.LENGTH_LONG).show();
            });
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        editText = findViewById(R.id.et_carId);
        button = findViewById(R.id.btn_query);
        button.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carquery;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.btn_query ){
            getCatData();
        }

    }
}
