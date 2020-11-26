package com.idat.appgfv.Interfaces.Categoria;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCategoria {
public static Retrofit getRetrofit(){
    String url = "http://192.168.1.60:9001/api/categoria/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
     return retrofit;
}

     public static CategoriaApi getCategoriaApi(){
           CategoriaApi categoriaApi = getRetrofit().create(CategoriaApi.class);
           return categoriaApi;
     }

}
