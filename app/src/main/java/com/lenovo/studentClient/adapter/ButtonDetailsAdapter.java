package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.studentClient.R;
import com.lenovo.studentClient.myinfo.TransitInfo;
import com.lenovo.studentClient.utils.UpdateInter;

import java.util.ArrayList;

/**
 * 底部rv的的适配器
 * @author asus
 *
 */
public class ButtonDetailsAdapter extends RecyclerView.Adapter<ButtonDetailsAdapter.MyViewHandler> {
    private Context context;
    private ArrayList<String> arrayList;
    private ArrayList<TransitInfo> transitInfoArrayList;
    private static UpdateInter mUpdateInter;
    public ButtonDetailsAdapter(Context context, ArrayList<String> arrayList, ArrayList<TransitInfo> transitInfoArrayList){
        this.context = context;
        this.arrayList = arrayList;
        this.transitInfoArrayList = transitInfoArrayList;
    }
    @NonNull
    @Override
    public ButtonDetailsAdapter.MyViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandler(LayoutInflater.from(context).inflate(R.layout.buttonadapter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandler holder, int position) {
        holder.tContent.setText(arrayList.get(position));
        holder.tTransit.setVisibility(View.INVISIBLE);
        mUpdateInter.changData();
        if (position==0){
            holder.leftView.setVisibility(View.INVISIBLE);
        }
        holder.tVariety.setBackgroundResource(R.drawable.blue_yuan);
        holder.tTransit.setText("可换乘");
        for (int i = 0; i <transitInfoArrayList.size() ; i++) {
            TransitInfo transitInfo = transitInfoArrayList.get(i);
            if (transitInfo.getTransitName().equals(arrayList.get(position))){
                holder.tVariety.setBackgroundResource(R.drawable.end_yuan);
                holder.tTransit.setVisibility(View.VISIBLE);
                holder.tTransit.setText(holder.tTransit.getText().toString()+","+transitInfo.getTransitSite());
            }
        }
        if (position==arrayList.size()-1){
            holder.rightView.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHandler extends RecyclerView.ViewHolder {
        TextView tTransit,tVariety,tContent;
        View leftView,rightView;
        public MyViewHandler(View itemView) {
            super(itemView);
            tTransit =itemView.findViewById(R.id.tv_transit);
            tVariety = itemView.findViewById(R.id.tv_Variety);
            tContent = itemView.findViewById(R.id.tv_content);
            leftView = itemView.findViewById(R.id.leftView);
            rightView = itemView.findViewById(R.id.rightView);
        }
    }

    public static void ChangOnData(UpdateInter updateInter){
        mUpdateInter = updateInter;
    }
}
