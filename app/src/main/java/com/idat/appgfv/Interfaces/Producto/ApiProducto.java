package com.idat.appgfv.Interfaces.Producto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProducto {

    public static Retrofit getRetrofit() {
        String url = "http://192.168.1.60:9001/api/productos/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static ProductoApi getProductoApi(){
        ProductoApi productoApi = getRetrofit().create(ProductoApi.class);
        return productoApi;
    }
}
