
package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.lenovo.studentClient.R;
import com.lenovo.studentClient.bean.MenuModel;
import com.lenovo.studentClient.utils.ClickInter;

import java.util.List;


public class MenuListAdapter extends BaseAdapter {
    private List<MenuModel> list;
    private LayoutInflater inflater;
    private static ClickInter mClickInter;
    public MenuListAdapter(Context context, List<MenuModel> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MenuModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu_list, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuModel model = getItem(position);
        if (model != null) {
            holder.icon.setBackgroundResource(model.getIcon());
            holder.content.setText(model.getContent());
        }
        convertView.setOnClickListener(v -> {
            mClickInter.onClick(position);
        });
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView content;
    }

    public static void getOnclick(ClickInter clickInter){
        mClickInter = clickInter;
    }
}
