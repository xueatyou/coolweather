package com.example.bianlifeng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("sport")
    public Spot spot;


    public class Comfort{

        @SerializedName("txt")
        public String info;
    }

    public class CarWash{

        @SerializedName("txt")
        public String info;
    }

    public class Spot{

        @SerializedName("txt")
        public String info;

    }
}
