package com.lenovo.studentClient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.activity.AssistantItemActivity;
import com.lenovo.studentClient.myinfo.AssistantInfo;

import java.util.ArrayList;

/**
 * 旅游出行的适配器
 * @author asus
 */
public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.MyViewHandler> {
    private final String ImgHead = "http://192.168.3.5:8088/transportservice";
    private Context context;
    private ArrayList<AssistantInfo> assistantInfoS;
   public AssistantAdapter(Context context, ArrayList<AssistantInfo> assistantInfoS){
        this.context = context;
        this.assistantInfoS = assistantInfoS;
    }
    @NonNull
    @Override
    public AssistantAdapter.MyViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandler(LayoutInflater.from(context).inflate(R.layout.assistant_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandler holder, int position) {
        AssistantInfo info = assistantInfoS.get(position);
        Glide.with(context).load(ImgHead+info.getImgUrl()).into(holder.iContent);
        holder.tTitle.setText(info.getName());
        holder.tFare.setText("票价：￥"+info.getName()+"元");
        holder.tBuy.setOnClickListener(v -> {
            Intent intent = new Intent(context,AssistantItemActivity.class);
            intent.putExtra("info",info);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assistantInfoS.size();
    }



    class MyViewHandler extends RecyclerView.ViewHolder {
        ImageView iContent;
        TextView tTitle,tBuy,tFare;
        public MyViewHandler(View itemView) {
            super(itemView);
            iContent = itemView.findViewById(R.id.iv_content);
            tTitle = itemView.findViewById(R.id.tv_title);
            tBuy = itemView.findViewById(R.id.tv_buy);
            tFare = itemView.findViewById(R.id.tv_fare);
        }
    }
}
