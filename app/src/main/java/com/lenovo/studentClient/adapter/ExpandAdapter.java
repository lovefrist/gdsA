package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lenovo.studentClient.R;
import com.lenovo.studentClient.utils.ClickInter;

import java.util.ArrayList;

/**
 * 折叠框选择器
 *
 * @author asus
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> groupList;
    private ArrayList<String>[] childArrayList;
    private Context context;


    public ExpandAdapter(Context context, ArrayList<String> GroupList, ArrayList<String>[] childArrayList) {
        this.context = context;
        this.groupList = GroupList;
        this.childArrayList = childArrayList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArrayList[groupPosition].size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("TAG", "getChild: "+groupPosition+"\t"+childPosition+childArrayList[groupPosition].size());
        return childArrayList[groupPosition].get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

//    如果 hasStableIds()
//     为真，该函数返回的ID必须是固定不变的.
//    分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHandler handler = null;
        if (convertView == null) {
            handler = new GroupViewHandler();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            handler.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(handler);
        } else {
            handler = (GroupViewHandler) convertView.getTag();
        }
        handler.textView.setPadding(100,0,0,0);
        handler.textView.setText(groupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHandler handler = null;
        if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.test_list_item, parent, false);
                handler = new ChildViewHandler();
                handler.textView = convertView.findViewById(android.R.id.text1);
                convertView.setTag(handler);

        } else {
                handler = (ChildViewHandler) convertView.getTag();
        }
        handler.textView.setPadding(200,0,0,0);
        handler.textView.setText(childArrayList[groupPosition].get(childPosition));



        return convertView;
    }



    //二级目录的目标是否是可以选择的
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupViewHandler {
        TextView textView;
    }

    class ChildViewHandler {
        TextView textView;
    }

}
