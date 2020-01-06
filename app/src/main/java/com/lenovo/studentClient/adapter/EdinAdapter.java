package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 折叠框
 *
 * @author asus
 */
public class EdinAdapter extends BaseExpandableListAdapter {
    private String exData = "┆";
    private Context context;
    private ArrayList<String> arrayList;

    public EdinAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return exData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("", "getChild: "+arrayList.size());
        return arrayList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("TAG", "getGroupView: "+groupPosition+"\t"+convertView);
        GroupViewHandler handler = null;
        if (convertView==null){
            handler = new GroupViewHandler();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.test_list_item,parent,false);
            handler.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(handler);
        }else {
            handler = (GroupViewHandler) convertView.getTag();
        }
        handler.textView.setText(exData);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHandler handler = null;
        if (convertView == null){
            handler = new ChildViewHandler();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.test_list_item,parent,false);
            handler.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(handler);
        }else {
            handler = (ChildViewHandler) convertView.getTag();
        }
        handler.textView.setText(arrayList.get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return groupPosition;
    }

    class GroupViewHandler {
        TextView textView;
    }

    class ChildViewHandler {
        TextView textView;
    }

}
