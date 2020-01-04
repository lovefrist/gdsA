package com.lenovo.studentClient.utils;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * 毫秒转换器
 * @author asus
 */
public class MathUtil {
    public static String LongDate(int strLong){
        int data = strLong/1000;
        float timeDate = data/60f;
        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        String dataStr = decimalFormat.format(timeDate);
        char[] dateArray  = dataStr.toCharArray();
        String allTime = "";
        for (int i = 0; i <dateArray.length ; i++) {
            if (i==dataStr.indexOf(".")){
                allTime +=":";
                char[] arrayData = allTime.toCharArray();
                if (arrayData.length>2){
                    allTime = "";
                    for (int j = 0; j <arrayData.length ; j++) {
                        if (j==(arrayData.length-2)){
                            allTime +=":"+arrayData[j];
                        }else {
                            allTime+=arrayData[j];
                        }
                    }
                }
            }else {
                allTime +=dateArray[i];
            }
        }
        return allTime;
    }
}
