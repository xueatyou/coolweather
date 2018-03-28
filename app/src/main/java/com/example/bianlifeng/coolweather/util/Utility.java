package com.example.bianlifeng.coolweather.util;

import android.text.TextUtils;

import com.example.bianlifeng.coolweather.database.City;
import com.example.bianlifeng.coolweather.database.County;
import com.example.bianlifeng.coolweather.database.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class Utility {

    //处理省信息
    public static boolean handleProvinceResponse(String response){

        if( !TextUtils.isEmpty(response)){  //如果response！=null
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceId(jsonObject.getInt("provinceId"));
                    province.save();
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    public static boolean handleCityResponse(String response,int pronvinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city=new City();
                    city.setCityId(jsonObject.getInt("id"));
                    city.setCityName("name");
                    city.setProvinceId(pronvinceId);
                    city.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
    if(!TextUtils.isEmpty(response)){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                County county = new County();
                county.setCityId(cityId);
                county.setConutryName(jsonObject.getString("name"));
                county.setWeatherId(jsonObject.getString("weather_id"));
                county.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
        return false;
    }


}
