package com.idat.appgfv.Interfaces.Producto;

import com.idat.appgfv.Modelo.Producto.Producto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductoApi {

    @GET("all")
    Call<List<Producto>> listar();

    @POST("save")
    Call<ResponseBody> guardar(@Body Producto producto);

    @PUT("editar")
    Call<ResponseBody> editar(@Body Producto producto);

    @DELETE("delete/{id}")
    Call<ResponseBody> eliminar(@Path("id") Long id);
}
