package com.idat.appgfv.Interfaces.Area;

import com.idat.appgfv.Modelo.Area.Area;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AreAPI {

    @GET("all")
    Call<List<Area>> listar();

    @POST("save")
    Call<ResponseBody> guardar(@Body Area area);

    @PUT("editar")
    Call<ResponseBody> editar(@Body Area area);

    @DELETE("delete/{id}")
    Call<ResponseBody> eliminar(@Path("id") Long id);
}
