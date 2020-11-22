package com.idat.appgfv.Interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiArea {
    public static Retrofit getRetrofit(){
        String url = "http://192.168.1.60:9001/api/area/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static AreAPI getAreaApi(){
        AreAPI areaApi = getRetrofit().create(AreAPI.class);
        return areaApi;
    }

}
