package com.example.bianlifeng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class Basic {

    @SerializedName("city")     //让json字段与java类建立映射关系
    public  String name;

    @SerializedName("id")
    public  String weatherid;

    public Update update;




    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
