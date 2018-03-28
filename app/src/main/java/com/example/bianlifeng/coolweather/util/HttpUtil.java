package com.example.bianlifeng.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class HttpUtil {
                                        //      请求的地址           回掉函数
    public static void sendOKHttpRequest(String address,okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(address).build();

        client.newCall(request).enqueue(callback);
    }


}
