package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.studentClient.R;
import com.lenovo.studentClient.utils.ClickInter;

import java.util.ArrayList;

/**
 * 顶部的rv
 * @author asus
 */
public class TopDetailsAdapter  extends RecyclerView.Adapter<TopDetailsAdapter.MyViewHandler> {
    private Context context;
    private ArrayList<String> arrayList;
    private static ClickInter minter;
    private ArrayList<MyViewHandler> arrayListHandler = new ArrayList<>();
    public TopDetailsAdapter(Context context, ArrayList<String> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public TopDetailsAdapter.MyViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandler(LayoutInflater.from(context).inflate(R.layout.topadapter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandler holder, int position) {
        arrayListHandler.add(holder);
        if (position==0){
            holder.textView.setBackgroundResource(R.drawable.site_backgroud);
        }
        holder.textView.setText(arrayList.get(position));
        holder.textView.setOnClickListener(v -> {
            for (int i = 0; i <arrayListHandler.size() ; i++) {
                if (i==position){
                    arrayListHandler.get(i).textView.setBackgroundResource(R.drawable.site_backgroud);
                }else {
                    arrayListHandler.get(i).textView.setBackgroundResource(R.drawable.tips_info);
                }
            }
            minter.onClick(position);
        });
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
        TextView textView;
        public MyViewHandler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
        }
    }
    public static void getClick(ClickInter clickInter){
        minter = clickInter;
    }

}
