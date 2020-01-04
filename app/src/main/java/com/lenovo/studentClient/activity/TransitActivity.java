package com.lenovo.studentClient.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.SurroundingsAdapter;

import java.util.ArrayList;

/**
 *
 * @author asus
 */
public class TransitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ArrayList<View> arrayList = new ArrayList<>();
        for (int i = 0; i <6 ; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.text_viewpag,null);
            arrayList.add(view);
        }
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new SurroundingsAdapter(arrayList));
    }
}
