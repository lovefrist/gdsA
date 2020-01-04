
package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Object[]> {
    private List<Object[]> objects;
    private Context context;
    private int count;

    public ListAdapter(Context context, List<Object[]> objects, int titleCount) {
        super(context, 0, objects);
        this.objects = objects;
        this.context = context;
        count = titleCount;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object[] getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout = new LinearLayout(context);
        for (int i = 0; i < count; i++) {
            TextView textView = new TextView(context);
            LayoutParams textParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
            textView.setLayoutParams(textParams);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);
            textView.setText(String.format("%s", getItem(position)[i]));
            layout.addView(textView);
        }
        return layout;
    }

}
