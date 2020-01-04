package com.lenovo.studentClient.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.studentClient.R;
import com.lenovo.studentClient.api.ApiService;
import com.lenovo.studentClient.config.AppConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * ip设置activity，包涵网络连接方式切换，IP设置
 *
 * @author asus
 */
public class IpSetActivity extends BaseActivity {
    private static final String TAG = "IpSetActivity";
    private String IPJudpg = "(2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2}";
    private String IPData = "^(0|[1-9]\\d{1,9})$";
    private ArrayList<EditText> textArrayList = new ArrayList<>();
    private Button button;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return getString(R.string.title_ip);
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        try {
            AppConfig appConfig = new AppConfig();
            Class cla = appConfig.getClass();
            Object object = cla.newInstance();
            Field field = cla.getDeclaredField("IP");
            field.setAccessible(true);
           String data = (String) field.get(object);
           String[] dataAll = data.split("\\.");
            for (int i = 0; i <textArrayList.size() ; i++) {
                textArrayList.get(i).setText(dataAll[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < textArrayList.size(); i++) {
            int finalI = i;
            textArrayList.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().matches(IPJudpg) && s.length() != 0) {
                        textArrayList.get(finalI).setText("255");
                        Toast.makeText(IpSetActivity.this, "最大不能超过255", Toast.LENGTH_LONG).show();
                    }
                    if (!s.toString().matches(IPData)){
                        textArrayList.get(finalI).setText("0");
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void initView() {
        textArrayList.add(findViewById(R.id.ip_et_ip0));
        textArrayList.add(findViewById(R.id.ip_et_ip1));
        textArrayList.add(findViewById(R.id.ip_et_ip2));
        textArrayList.add(findViewById(R.id.ip_et_ip3));
        button = findViewById(R.id.ip_btn_sure);
        button.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ipset;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < textArrayList.size(); i++) {
            if (!textArrayList.get(i).getText().toString().matches(IPJudpg)) {
                Toast.makeText(this, "请输入正确第" + (i + 1) + "出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (v.getId() == R.id.ip_btn_sure) {
            try {

                String IPet = "";
                for (int i = 0; i < textArrayList.size(); i++) {
                    if (i != textArrayList.size() - 1) {
                        IPet += textArrayList.get(i).getText().toString() + ".";
                    } else {
                        IPet += textArrayList.get(i).getText().toString();
                    }

                }
                AppConfig appConfig = new AppConfig();
                Class cla = appConfig.getClass();
                Object object = cla.newInstance();
                Field field = cla.getDeclaredField("IP");
                field.setAccessible(true);
                field.set(object, IPet);
                AppConfig.setURL();
                Log.d(TAG, "onClick: "+AppConfig.BASE_URL + "GetAllSense.do");
                Toast.makeText(this, "设置成功设置的IP为" + IPet, Toast.LENGTH_LONG).show();
                onAfter();
            } catch (Exception e) {
                Log.d("", "onClick: 出错了");
                e.printStackTrace();
            }
        }
    }
}
