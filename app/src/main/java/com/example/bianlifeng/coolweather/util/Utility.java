package com.example.bianlifeng.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.bianlifeng.coolweather.database.City;
import com.example.bianlifeng.coolweather.database.County;
import com.example.bianlifeng.coolweather.database.Province;
import com.example.bianlifeng.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class Utility {
    //String TAG="Utility";
    //处理省信息
    public static boolean handleProvinceResponse(String response){

        System.out.println("Utility-》handleProvinceResponse" +response);

        if( !TextUtils.isEmpty(response)){  //如果response！=null
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceId(jsonObject.getInt("id"));
                    province.save();
                    Log.i("Utility", "province name: "+province.getProvinceName());
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    public static boolean handleCityResponse(String response,int pronvinceId){
        System.out.print("Utility-》handleCityResponse" +response+"  "+pronvinceId);
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city=new City();
                    city.setCityId(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(pronvinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
        System.out.println("Utility-》handleCountyResponse" +response+"  "+cityId);

    if(!TextUtils.isEmpty(response)){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                County county = new County();
                county.setCityId(cityId);
                county.setConutryName(jsonObject.getString("name"));
                county.setWeatherId(jsonObject.getString("weather_id"));
                county.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        System.out.println("Utility-》handleWeatherResponse" +response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
           Weather weather= new Gson().fromJson(weatherContent,Weather.class);
            Log.d(TAG, "handleWeatherResponse: "+weather.now.temperature);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }


}
