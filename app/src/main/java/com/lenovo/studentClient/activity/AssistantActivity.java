package com.lenovo.studentClient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lenovo.studentClient.R;

/**
 *
 * 旅游助手页面
 * @author asus
 */
public class AssistantActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);
    }

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "旅游助手";
    }

    @Override
    protected void onAfter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assistant;
    }

    @Override
    public void onClick(View v) {

    }
}
