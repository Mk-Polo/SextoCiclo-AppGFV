package com.idat.appgfv.Interfaces.Marca;

import com.idat.appgfv.Modelo.Marca.Marca;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MarcaAPI {

    @GET("all")
    Call<List<Marca>> listar();

    @POST("save")
    Call<ResponseBody> guardar(@Body Marca marca);
    
    @PUT("editar")
    Call<ResponseBody> editar(@Body Marca marca);
    
    @DELETE("delete/{id}")
    Call<ResponseBody> eliminar(@Path("id") Long id );
}
