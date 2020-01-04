package com.lenovo.studentClient.api;


import com.lenovo.studentClient.config.AppConfig;

/**
 * URL地址
 * @author asus
 */
public interface ApiService {

    /**
     * 刷新
     * 查询“所有传感器”的当前值
     */
    String getAllSense = AppConfig.BASE_URL + "GetAllSense.do";
    String carViolate = AppConfig.BASE_URL+"GetCarPeccancy.do";
    String getALLPhone = AppConfig.BASE_URL+"GetPeccancyType.do";

}
