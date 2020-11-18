package com.idat.appgfv.Interfaces;

import com.idat.appgfv.Modelo.Marca.Marca;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MarcaAPI {

    @GET("all")
    Call<List<Marca>> listar();

    @POST("save")
    Call<ResponseBody> guardar(@Body Marca marca);
}
