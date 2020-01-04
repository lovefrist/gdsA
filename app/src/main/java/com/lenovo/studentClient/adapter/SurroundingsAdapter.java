package com.lenovo.studentClient.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.studentClient.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 环境监测的适配器
 * @author asus
 */
public class SurroundingsAdapter extends PagerAdapter {
    private ArrayList<View> textViewArrayList;
    public SurroundingsAdapter(ArrayList<View> textViewArrayList){
        this.textViewArrayList = textViewArrayList;
    }
    @Override
    public int getCount() {
        return textViewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(textViewArrayList.get(position));
        return textViewArrayList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(textViewArrayList.get(position));
    }
}
