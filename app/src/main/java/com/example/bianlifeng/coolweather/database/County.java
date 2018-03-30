package com.example.bianlifeng.coolweather.database;

import org.litepal.crud.DataSupport;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class County extends DataSupport {
    int id;
    int provinceId;
    int cityId;
    int countyid;
    String conutyName;
    String weatherId;


    public void setCountyid(int countyid) {
        this.countyid = countyid;
    }

    public int getCountyid() {
        return countyid;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setConutryName(String conutryName) {
        this.conutyName = conutryName;
    }

    public String getConutryName() {
        return conutyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherId() {
        return weatherId;
    }
}
