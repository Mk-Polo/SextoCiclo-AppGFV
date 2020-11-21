package com.idat.appgfv.Interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiMarca {

    public static Retrofit getRetrofit(){
        String url = "http://192.168.1.36:9001/api/marcas/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static MarcaAPI getMarcaApi(){
        MarcaAPI marcaApi = getRetrofit().create(MarcaAPI.class);
        return marcaApi;
    }
}
