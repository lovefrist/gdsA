package com.lenovo.studentClient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.utils.MapUtil;

import java.util.ArrayList;

/**
 * @author asus
 */
public class MapActivity extends BaseActivity {
    private static final String TAG = "MapActivity";
    MapView mapView;
    private ImageView iBreak, iAddMarker, iDistance;
    private AMap aMap;
    private ArrayList<LatLng> lngArrayList = new ArrayList<>();
    private int[] srcId = new int[]{R.mipmap.marker_one, R.mipmap.marker_second, R.mipmap.marker_thrid, R.mipmap.marker_forth};
    private ArrayList<Marker> markers = new ArrayList<>();
    private TextView textView;

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected String getLayoutTitle() {
        return null;
    }

    @Override
    protected void onAfter() {

    }

    @Override
    protected void initData() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        lngArrayList.add(new LatLng(40.0339236000, 116.3204956100));
        lngArrayList.add(new LatLng(39.8496666200, 116.3479614300));
        lngArrayList.add(new LatLng(39.8338500800, 116.2518310500));
        lngArrayList.add(new LatLng(39.8486123000, 116.4880371100));
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                TextView textView = new TextView(MapActivity.this);

                Log.d(TAG, "getInfoWindow: " + marker.getId());
                switch (marker.getId()) {
                    case "Marker1":
                        textView.setText("一号小车");
                        break;
                    case "Marker2":
                        textView.setText("二号小车");
                        break;
                    case "Marker3":
                        textView.setText("三号小车");
                        break;
                    case "Marker4":
                        textView.setText("四号小车");
                        break;
                    default:
                        textView.setText("???");
                }
                return textView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        aMap.setOnMarkerClickListener(marker -> {
            Log.d(TAG, "initData: "+marker.getId());
            if (marker.isInfoWindowShown()){
             marker.hideInfoWindow();
            }
            return false;
        });
    }

    @Override
    protected void initView() {
        mapView = findViewById(R.id.mapView);
        iBreak = findViewById(R.id.iv_break);
        iAddMarker = findViewById(R.id.iv_addMarker);
        iDistance = findViewById(R.id.iv_distance);
        textView = findViewById(R.id.tv_content);
        iBreak.setOnClickListener(this);
        iAddMarker.setOnClickListener(this);
        iDistance.setOnClickListener(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_break:
                finish();
                break;
            case R.id.iv_addMarker:
                addMarker();
                break;
            case R.id.iv_distance:
                if (markers.size()!=0){
                    drawCircle();
                }else {
                    Toast.makeText(this,"请先添加点",Toast.LENGTH_LONG).show();
                }

                break;
            default:
        }
    }

    private void drawCircle() {
        aMap.addCircle(new CircleOptions().center(new LatLng(39.8496666200, 116.3479614300))
                .radius(20000)
                .strokeColor(Color.BLUE)
                .strokeWidth(10));
        textView.setText("当前二号车20KM的范围内有的小车车号有：");
        for (int i = 0; i < lngArrayList.size(); i++) {
            if (i!=1){
             float distance =  MapUtil.calculateLineDistance(lngArrayList.get(1),lngArrayList.get(i));
             if (distance<20000){
                 textView.setText(textView.getText().toString()+",\t "+(i+1)+"\t");
             }
            }
        }
    }

    private void addMarker() {
        for (int i = 0; i < srcId.length; i++) {
            //draggable设置图标是不是可以拖动的
            Marker marker = aMap.addMarker(new MarkerOptions().position(lngArrayList.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(srcId[i]))
                    .draggable(false));
            markers.add(marker);
        }
        textView.setText("1，2，3，4号小车地图标记已完成");
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

}
