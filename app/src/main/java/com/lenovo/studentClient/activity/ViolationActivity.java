package com.lenovo.studentClient.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.api.ApiService;
import com.lenovo.studentClient.config.AppConfig;
import com.lenovo.studentClient.utils.MathUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 违章详情页面
 *
 * @author asus
 * @date 2020年1月2日17:02:19
 */
public class ViolationActivity extends BaseActivity {

    private static final String TAG = "ViolationActivity";
    int start = 0;
    AlertDialog dialog;
    MediaMetadataRetriever mmr;
    JSONArray arrayData;
    VideoView videoView;
    private TextView tTime, tLocation, tStatus, tContent, tViolationInfo, tVvNumber;
    private ImageView iEvidenceImg, iBark, iVhead;
    private JSONObject intentObject;
    private VideoView vEvidenceMp4;
    private int[] imgUri = new int[]{R.drawable.violation1, R.drawable.violation2, R.drawable.violation3, R.drawable.violation4,
            R.drawable.violation5, R.drawable.violation6, R.drawable.violation7, R.drawable.violation8};
    private int[] videoUri = new int[]{R.raw.car1, R.raw.car2, R.raw.car3, R.raw.car4, R.raw.car5, R.raw.car6, R.raw.car7, R.raw.car8};
    private int dataUri = 0;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String JSONData = intent.getStringExtra("carInfo");
        try {
            intentObject = new JSONObject(JSONData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getLayoutTitle() {
        return "查询结果";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        try {
            iEvidenceImg.setImageResource(imgUri[dataUri]);
            vEvidenceMp4.setVideoURI((Uri.parse("android.resource://com.lenovo.studentClient/" + videoUri[dataUri])));
            mmr.setDataSource(this, (Uri.parse("android.resource://com.lenovo.studentClient/" + videoUri[dataUri])));
            Bitmap bitmap = mmr.getFrameAtTime();
            iVhead.setImageBitmap(bitmap);
            //元数据键可检索数据源的播放持续时间
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int  duration = 0;
            if(!TextUtils.isEmpty(durationStr)){
                duration = Integer.valueOf(durationStr);
            }
            tVvNumber.setText("▶" + MathUtil.LongDate(duration));
            dataUri++;
            if (dataUri == imgUri.length) {
                dataUri = 0;
            }
            arrayData = intentObject.getJSONArray("ROWS_DETAIL");
            JSONObject object1 = arrayData.getJSONObject(start);
            start++;
            if (start == arrayData.length()) {
                iBark.setEnabled(false);
            }
            tTime.setText(object1.getString("pdatetime"));
            tLocation.setText(object1.getString("paddr"));
            getPcode(object1.getString("pcode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPcode(String pcode) {
        try {
            JSONObject object = new JSONObject();
            object.put("UserName", "user1");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL+"GetPeccancyType.do", object, jsonObject -> {
                service.execute(() -> {
                    try {
                        JSONArray Allpcode = jsonObject.getJSONArray("ROWS_DETAIL");
                        for (int i = 0; i < Allpcode.length(); i++) {
                            JSONObject PcodeObject = Allpcode.getJSONObject(i);
                            if (pcode.equals(PcodeObject.getString("pcode"))) {
                                InitApp.getHandler().post(() -> {
                                    try {
                                        tContent.setText(PcodeObject.getString("premarks"));
                                        tViolationInfo.setText("违章：" + arrayData.length() + "次 罚款" + PcodeObject.getString("pmoney") + "元 扣分：" + PcodeObject.getString("pscore") + " 次");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                });
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            }, volleyError -> {

            });
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void initView() {
        tTime = findViewById(R.id.tv_time);
        tLocation = findViewById(R.id.tv_location);
        tStatus = findViewById(R.id.tv_status);
        tContent = findViewById(R.id.tv_content);
        tViolationInfo = findViewById(R.id.tv_violationInfo);
        iEvidenceImg = findViewById(R.id.iv_evidenceImg);
        iBark = findViewById(R.id.iv_bark);
        vEvidenceMp4 = findViewById(R.id.vv_evidenceMp4);
        iVhead = findViewById(R.id.iv_vvHead);
        tVvNumber = findViewById(R.id.tv_vvNumber);
        iBark.setOnClickListener(this);
        vEvidenceMp4.setOnClickListener(this);
        int timeLong = vEvidenceMp4.getDuration();
        dialog = new AlertDialog.Builder(this, R.style.FullActivity).create();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        videoView = new VideoView(this);
        videoView.setLayoutParams(layoutParams);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(mp -> {
            videoView.pause();
            dialog.dismiss();
        });
        dialog.setView(videoView);
//        MMediaMetadataRetriever
        mmr = new MediaMetadataRetriever();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_violation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bark:
                initData();
                break;
            case R.id.vv_evidenceMp4:
                dialog.show();
                videoView.setVideoURI(Uri.parse("android.resource://com.lenovo.studentClient/" + videoUri[dataUri]));

                videoView.start();
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                break;
            default:
        }
    }
}
