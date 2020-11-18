package com.idat.appgfv.UI;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idat.appgfv.Adaptador.MarcaAdapter;
import com.idat.appgfv.Interfaces.ApiMarca;
import com.idat.appgfv.Interfaces.MarcaAPI;
import com.idat.appgfv.Modelo.Marca.Marca;
import com.idat.appgfv.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Marca_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private String url = "http://192.168.1.60:9001/api/marcas/";

    private FloatingActionButton agregarMarca;

    private List<Marca> marca = new ArrayList<>();
    private MarcaAdapter marcaAdapter;

    public Marca_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marca_fragment, container, false);
        refresh = view.findViewById(R.id.swipeMarca);
        recyclerView = view.findViewById(R.id.recyclerMarca);

        refresh.setOnRefreshListener(this);

        refresh.post(new Runnable() {
            @Override
            public void run() {
                marca.clear();
                getData();
            }
        });

        agregarMarca = view.findViewById(R.id.agregarMarca);
        agregarMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarca();
            }
        });

        return view;
    }

    private void addMarca() {
        TextView closeMarca,tituloMarca;
        final EditText txtMarca;
        Button submitMarca;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_marca);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeMarca = dialog.findViewById(R.id.closeMarca);
        tituloMarca = dialog.findViewById(R.id.tituloMarca);
        tituloMarca.setText("Agregar Marca");

        closeMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtMarca = dialog.findViewById(R.id.txtMarca);
        submitMarca = dialog.findViewById(R.id.submitMarca);
        
        submitMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marca marcaSubmit = new Marca();
                marcaSubmit.setMarca(txtMarca.getText().toString());
                enviarMarca(marcaSubmit);
            }
        });
        dialog.show();
    }

    private void enviarMarca(Marca marcaSubmit) {
        Call<ResponseBody> guardar = ApiMarca.getMarcaApi().guardar(marcaSubmit);
        guardar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    if(response.body() != null){
                        try {
                            Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dialog.dismiss();
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            marca.clear();
                            getData();
                        }
                    });
                }else{
                    if(response.errorBody() != null){
                        try {
                            Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Request: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        refresh.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        MarcaAPI marcaAPI = retrofit.create(MarcaAPI.class);
        Call<List<Marca>> call = marcaAPI.listar();
        call.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                marca = response.body();
                AdapterPush(marca);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AdapterPush(List<Marca> marca) {
        marcaAdapter = new MarcaAdapter(getContext(), marca);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(marcaAdapter);
    }

    @Override
    public void onRefresh() {
        marca.clear();
        getData();
    }
}