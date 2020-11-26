package com.idat.appgfv.Interfaces;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProducto {
    public static Retrofit getRetrofit(){
        String url= "http://192.168.60:9001/api/producto/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    public static ProductoAPI getProductoApi(){
        ProductoAPI productoAPI = getRetrofit().create(ProductoAPI.class);
        return productoAPI;
    }
}
