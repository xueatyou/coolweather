package com.example.bianlifeng.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bianlifeng.coolweather.gson.Forecast;
import com.example.bianlifeng.coolweather.gson.Weather;
import com.example.bianlifeng.coolweather.util.HttpUtil;
import com.example.bianlifeng.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private  TextView titleUpdataTime;
    private  TextView degreeText;
    private TextView weatherInfoText;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private LinearLayout forecastLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Log.d(TAG, "onCreate: ");
        //初始化组件
        weatherLayout=(ScrollView) findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdataTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);

        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.spot_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);

        //申请缓存
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        //缓存中有数据
        if(weatherString !=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{  //缓存中没有数据，从服务器中解析
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d(TAG, "onCreate: weatherid-----"+weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWWeather(weatherId);

        }
    }

    private void requestWWeather(String weatherId) {
        Log.d(TAG, "requestWWeather: ");
        String weatherUrl= "http://guolin.tech/api/weather?cityid="
                            +weatherId+"&key=690a14b4bbbe4f37b372401492895508";
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载信息失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final  String responseText= response.body().string();
                Log.d(TAG, "onResponse: response==>"+responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                           // Log.d(TAG, "run: weather=null&&weather.status="+weather.status);
                            Toast.makeText(WeatherActivity.this,"加载信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    //在页面上显示信息
    private void showWeatherInfo(Weather weather) {
        Log.d(TAG, "showWeatherInfo: ");
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime;
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;

       titleCity.setText(cityName);
       titleUpdataTime.setText(updateTime);
       degreeText.setText(degree);
       weatherInfoText.setText(weatherInfo);

       forecastLayout.removeAllViews(); //现设为不可见，设置信息完成设为可见

        for (Forecast forecast: weather.forecastList
             ) {
             View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
             TextView data = (TextView)findViewById(R.id.date_text1);
             TextView info = (TextView)findViewById(R.id.info_text);
             TextView max= (TextView)findViewById(R.id.max_text);
             TextView min = (TextView)findViewById(R.id.min_text);

             data.setText(forecast.data);
             info.setText(forecast.more.info);
             max.setText(forecast.temperture.max);
             min.setText(forecast.temperture.min);

        }

        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);

        }

        TextView comfort = (TextView)findViewById(R.id.comfort_text);
        TextView car_wash=(TextView)findViewById(R.id.car_wash_text);
        TextView spot = (TextView)findViewById(R.id.spot_text);

        comfort.setText(weather.suggestion.comfort.info);
        car_wash.setText(weather.suggestion.carWash.info);
        spot.setText(weather.suggestion.spot.info);


    }
}
