package com.lenovo.studentClient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.lenovo.studentClient.InitApp;
import com.lenovo.studentClient.R;
import com.lenovo.studentClient.adapter.SurroundingsAdapter;
import com.lenovo.studentClient.config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 环境监测页面
 *
 * @author asus
 */
public class SurroundingsActivity extends BaseActivity {
    int dataIndex = 0;

    private boolean control = true;
    private ViewPager viewPager;
    private ArrayList<View> viewArrayList = new ArrayList<>();
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private String[] nameInfo = new String[]{"空气温度(℃)", "空气湿度（RH%）", "PM2.5（μg/m³）", "CO2（m3/m3）", "光照（lm）", "道路状态"};
    private ArrayList<Integer> temperatureArrayList = new ArrayList<>();
    private ArrayList<Integer> humidityArrayList = new ArrayList<>();
    private ArrayList<Integer> pm25ArrayList = new ArrayList<>();
    private ArrayList<Integer> co2ArrayList = new ArrayList<>();
    private ArrayList<Integer> lightIntensityArrayList = new ArrayList<>();
    private ArrayList<Integer> statusArrayList = new ArrayList<>();
    private ArrayList<String> dateArrayList = new ArrayList<>();
    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<LineChart> lineCharts = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();

    @Override
    protected void initializeWithState(Bundle savedInstanceState) {

    }

    @Override
    protected String getLayoutTitle() {
        return "环境监测";
    }

    @Override
    protected void onAfter() {
        finish();
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 6; i++) {
            viewArrayList.add(LayoutInflater.from(this).inflate(R.layout.indexchart_item, null));
        }
        textViewArrayList.add(findViewById(R.id.tv_temperature));
        textViewArrayList.add(findViewById(R.id.tv_humidity));
        textViewArrayList.add(findViewById(R.id.tv_PM25));
        textViewArrayList.add(findViewById(R.id.tv_CO2));
        textViewArrayList.add(findViewById(R.id.tv_illumination));
        textViewArrayList.add(findViewById(R.id.tv_situation));

        arrayLists.add(temperatureArrayList);
        arrayLists.add(humidityArrayList);
        arrayLists.add(pm25ArrayList);
        arrayLists.add(co2ArrayList);
        arrayLists.add(lightIntensityArrayList);
        arrayLists.add(statusArrayList);
        for (int i = 0; i < textViewArrayList.size(); i++) {
            int finalI = i;
            textViewArrayList.get(i).setOnClickListener(v -> {
                //true 可以平滑的过度 ， flols
                viewPager.setCurrentItem(finalI, true);
                changTextView(finalI);
            });
        }
        viewPager.setAdapter(new SurroundingsAdapter(viewArrayList));
        for (int i = 0; i < viewArrayList.size(); i++) {
            LineChart lineChart = viewArrayList.get(i).findViewById(R.id.pieChart);
            TextView textView = viewArrayList.get(i).findViewById(R.id.tv_info);
            textView.setText(nameInfo[i]);
            initLineChart(lineChart);
            lineCharts.add(lineChart);
        }
        getDataIndext();
    }

    private void initLineData(int poax) {
        LineChart lineChart = lineCharts.get(poax);
        entries.clear();
        Log.d(TAG, "initLineData: " + arrayLists.get(poax).size());
        for (int i = 0; i < arrayLists.get(poax).size(); i++) {

            entries.add(new Entry(i, arrayLists.get(poax).get(i)));
        }
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineSet.setValues(entries);
            lineSet.setDrawValues(false);
            lineSet.setDrawCircles(false);
            lineChart.getData().notifyDataChanged();
            lineChart.invalidate();
            lineChart.notifyDataSetChanged();
        } else {
            LineDataSet set = new LineDataSet(entries, "");
            set.setColor(Color.BLUE);
            set.setDrawCircles(false);
            set.setDrawValues(false);
            set.setHighlightEnabled(false);
            LineData data = new LineData(set);
            lineChart.setData(data);
            lineChart.invalidate();
        }
    }

    private void initLineChart(LineChart lineChart) {
        lineChart.setNoDataText("暂未数据");
        lineChart.getLegend().setEnabled(false);
        lineChart.setDescription(null);
        lineChart.setDoubleTapToZoomEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(19);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(19);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v > dateArrayList.size() - 1) {
                    return "";
                } else {
                    return dateArrayList.get((int) v);
                }
            }
        });
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setLabelCount(5,true);
    }

    private void getDataIndext() {
        service.execute(() -> {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonWay = null;
            try {
                jsonObject.put("UserName", "user1");
                jsonWay = jsonObject;
                jsonWay.put("RoadId", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            while (control) {
                Log.d(TAG, "getDataIndext: 循环" + dataIndex);
                long startTime = System.currentTimeMillis();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + "GetAllSense.do", jsonObject
                        , jsonObject1 -> {
                    try {
                        int pm25 = jsonObject1.getInt("pm2.5");
                        int co2 = jsonObject1.getInt("co2");
                        int lightIntensity = jsonObject1.getInt("LightIntensity");
                        int temperature = jsonObject1.getInt("temperature");
                        int humidity = jsonObject1.getInt("humidity");
                        temperatureArrayList.add(temperature);
                        pm25ArrayList.add(pm25);
                        co2ArrayList.add(co2);
                        lightIntensityArrayList.add(lightIntensity);
                        humidityArrayList.add(humidity);
                        Date date = new Date();
                        SimpleDateFormat formatData = new SimpleDateFormat("mm:ss");
                        dateArrayList.add(formatData.format(date));
                        if (temperatureArrayList.size() > 20) {
                            temperatureArrayList.remove(0);
                        }
                        if (pm25ArrayList.size() > 20) {
                            pm25ArrayList.remove(0);
                        }
                        if (co2ArrayList.size() > 20) {
                            co2ArrayList.remove(0);
                        }
                        if (lightIntensityArrayList.size() > 20) {
                            lightIntensityArrayList.remove(0);
                        }
                        if (humidityArrayList.size() > 20) {
                            humidityArrayList.remove(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }, volleyError -> {

                    Log.d(TAG, "getDataIndext:1 " + volleyError);

                });
                requestQueue.add(jsonObjectRequest);
                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, AppConfig.BASE_URL + "GetRoadStatus.do", jsonWay,
                        jsonObject12 -> {
                            try {
                                int Status = jsonObject12.getInt("Status");
                                statusArrayList.add(Status);
                                if (statusArrayList.size() > 20) {
                                    statusArrayList.remove(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, volleyError -> {
                });
                requestQueue.add(request2);
                if (dateArrayList.size() > 20) {
                    dateArrayList.remove(0);
                }
                long endTime = System.currentTimeMillis();
                if ((startTime - endTime) < 3000) {
                    SystemClock.sleep(3000 - (startTime - endTime));
                }
                InitApp.getHandler().post(() -> {
                        initLineData(viewPager.getCurrentItem());
                });
            }
        });
    }

    private void changTextView(int finalI) {
        for (int j = 0; j < textViewArrayList.size(); j++) {
            if (j == finalI) {
                textViewArrayList.get(j).setBackgroundResource(R.drawable.yuangray_background);
            } else {
                textViewArrayList.get(j).setBackgroundResource(R.drawable.yuanwhite_background);
            }
        }
    }


    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        control = false;
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_surroundings;
    }

    @Override
    public void onClick(View v) {
    }
}
