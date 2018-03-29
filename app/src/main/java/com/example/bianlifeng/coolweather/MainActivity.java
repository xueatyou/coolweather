package com.example.bianlifeng.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//db 存放数据库模型相关的代码
//gson 存放gson模型相关的代码
//service   存放服务相关的代码
//util  存放相关的工具包🔧



public class MainActivity extends AppCompatActivity {

    String TAG ="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("weather",null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);

            finish();
        }
    }
}
