package com.lenovo.studentClient.utils;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.model.LatLng;

/**
 * Map的计算类
 * @author asus
 */
public class MapUtil {

    public static float calculateLineDistance(LatLng var0, LatLng var1) {
        if (var0 != null && var1 != null) {
            try {
                double var2 = var0.longitude;
                double var4 = var0.latitude;
                double var6 = var1.longitude;
                double var8 = var1.latitude;
                var2 *= 0.01745329251994329D;
                var4 *= 0.01745329251994329D;
                var6 *= 0.01745329251994329D;
                var8 *= 0.01745329251994329D;
                double var10 = Math.sin(var2);
                double var12 = Math.sin(var4);
                double var14 = Math.cos(var2);
                double var16 = Math.cos(var4);
                double var18 = Math.sin(var6);
                double var20 = Math.sin(var8);
                double var22 = Math.cos(var6);
                double var24 = Math.cos(var8);
                double[] var26 = new double[3];
                double[] var27 = new double[3];
                var26[0] = var16 * var14;
                var26[1] = var16 * var10;
                var26[2] = var12;
                var27[0] = var24 * var22;
                var27[1] = var24 * var18;
                var27[2] = var20;
                double var28 = Math.sqrt((var26[0] - var27[0]) * (var26[0] - var27[0]) + (var26[1] - var27[1]) * (var26[1] - var27[1]) + (var26[2] - var27[2]) * (var26[2] - var27[2]));
                return (float)(Math.asin(var28 / 2.0D) * 1.27420015798544E7D);
            } catch (Throwable var30) {
                var30.printStackTrace();
                return 0.0F;
            }
        } else {
            try {
                throw new AMapException("非法坐标值");
            } catch (AMapException var31) {
                var31.printStackTrace();
                return 0.0F;
            }
        }
    }
}
