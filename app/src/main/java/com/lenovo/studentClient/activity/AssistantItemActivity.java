package com.lenovo.studentClient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.myinfo.AssistantInfo;

import java.lang.annotation.ElementType;
import java.util.HashMap;

/**
 * 二维码信息
 *
 * @author asus
 */
public class AssistantItemActivity extends BaseActivity {
    AssistantInfo info;
    private TextView tInfoData;
    private ImageView iCode;
    private boolean status = true;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {
        Intent intent = getIntent();
        info = intent.getParcelableExtra("info");
    }

    @Override
    protected String getLayoutTitle() {
        return "支付页面";
    }

    @Override
    protected void onAfter() {
        fileList();
    }

    @Override
    protected void initData() {
        getCordBitMap();
    }

    private void getCordBitMap() {
        service.execute(() -> {
            while (status){
                long startTime = System.currentTimeMillis();
                QRCodeWriter writer = new QRCodeWriter();
                HashMap<EncodeHintType,String> typeString = new HashMap<>(1);
                typeString.put(EncodeHintType.CHARACTER_SET,"utf-8");
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = writer.encode("付款项目：" + info.getName() + " 付款金额：" + info.getTicket() + "元", BarcodeFormat.QR_CODE,200,200,typeString);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            int[] Colors = new int[200*200];
                for (int i = 0; i <200 ; i++) {
                    for (int j = 0; j <200 ; j++) {
                        if (bitMatrix.get(i,j)){
                            Colors[i*200*j] = Color.BLACK;
                        }else {
                            Colors[i*200*j] = Color.WHITE;
                        }
                    }
                }
                InitApp.getHandler().post(()->{
                    iCode.setImageBitmap(Bitmap.createBitmap(Colors,200,200,Bitmap.Config.RGB_565));
                });
                long endTime = System.currentTimeMillis();
                if ((endTime-startTime)<5000){
                    SystemClock.sleep(5000-(endTime-startTime));
                }
            }
        });
    }

    @Override
    protected void initView() {
        tInfoData = findViewById(R.id.tv_infoData);
        iCode = findViewById(R.id.iv_code);
        iCode.setOnLongClickListener(v -> {
            tInfoData.setText("付款项目：" + info.getName() + " 付款金额：" + info.getTicket() + "元");
            return false;
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assistant_item;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        status = false;
        super.onDestroy();
    }
}
