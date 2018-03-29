package com.example.bianlifeng.coolweather.database;

import org.litepal.crud.DataSupport;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class City extends DataSupport {
    int id;
    String cityName;
    int cityId;
    int provinceId;

    public City(){

        cityId=1;
        cityName = "北京";

    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
