package com.example.bianlifeng.coolweather.database;

import org.litepal.crud.DataSupport;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class Province extends DataSupport {
    int id;
    String provinceName;
    int provinceId;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
