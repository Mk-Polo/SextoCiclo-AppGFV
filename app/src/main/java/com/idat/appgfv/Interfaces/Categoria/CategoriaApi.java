package com.idat.appgfv.Interfaces.Categoria;

import com.idat.appgfv.Modelo.Categoria.Categoria;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriaApi {

    @GET("all")
    Call<List<Categoria>> listar();

    @POST("save")
    Call<ResponseBody> guardar(@Body Categoria categoria);

    @PUT("editar")
    Call<ResponseBody> editar(@Body Categoria categoria);

    @DELETE("delete/{id}")
    Call<ResponseBody> eliminar(@Path("id") Long id);

}
