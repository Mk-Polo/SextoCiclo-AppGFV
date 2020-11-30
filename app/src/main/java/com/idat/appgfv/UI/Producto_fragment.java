package com.idat.appgfv.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.idat.appgfv.Adaptador.ProductoAdapter;
import com.idat.appgfv.Interfaces.Area.ApiArea;
import com.idat.appgfv.Interfaces.Categoria.ApiCategoria;
import com.idat.appgfv.Interfaces.Marca.ApiMarca;
import com.idat.appgfv.Interfaces.Producto.ApiProducto;
import com.idat.appgfv.Modelo.Area.Area;
import com.idat.appgfv.Modelo.Categoria.Categoria;
import com.idat.appgfv.Modelo.Marca.Marca;
import com.idat.appgfv.Modelo.Producto.Producto;
import com.idat.appgfv.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Producto_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private FloatingActionButton agregarProducto;
    private List<Producto> producto = new ArrayList<>();
    private ProductoAdapter productoAdapter;

    //Llenar Spinners
    private List<Area> areaSpinner;
    private List<Categoria> categoriaSpinner;
    private List<Marca> marcaSpinner;
    private List<String> comboMarca = new ArrayList<>();
    private List<String> comboCategoria = new ArrayList<>();
    private List<String> comboArea = new ArrayList<>();

    //Scanner
    private EditText txtBarras;

    public Producto_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_fragment, container, false);
        refresh = view.findViewById(R.id.swipeProducto);
        recyclerView = view.findViewById(R.id.recyclerProducto);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                producto.clear();
                getData();
            }
        });

        consultarSpinnerArea();
        consultarSpinnerCategoria();
        consultarSpinnerMarca();

        agregarProducto = view.findViewById(R.id.agregarProducto);
        agregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducto();
            }
        });
        return view;
    }

    private void consultarSpinnerMarca() {
        marcaSpinner = new ArrayList<>();
        Call<List<Marca>> callMarca = ApiMarca.getMarcaApi().listar();
        callMarca.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                marcaSpinner = response.body();
                mostrarListaMarca();
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable t) {

            }
        });
    }

    private void mostrarListaMarca() {
        for(int i = 0; i < marcaSpinner.size(); i++){
            comboMarca.add(marcaSpinner.get(i).getMarca());
        }
    }

    private void consultarSpinnerCategoria() {
        categoriaSpinner = new ArrayList<>();
        Call<List<Categoria>> callCategoria = ApiCategoria.getCategoriaApi().listar();
        callCategoria.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                categoriaSpinner = response.body();
                mostrarListaCategoria();
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {

            }
        });
    }

    private void mostrarListaCategoria() {
        for(int i = 0; i < categoriaSpinner.size(); i++){
            comboCategoria.add(categoriaSpinner.get(i).getCategoria());
        }
    }

    private void consultarSpinnerArea() {
        areaSpinner = new ArrayList<>();
        Call<List<Area>> callArea = ApiArea.getAreaApi().listar();
        callArea.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                areaSpinner = response.body();
                mostrarListaArea();
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {

            }
        });
    }

    private void mostrarListaArea() {
        for(int i = 0; i < areaSpinner.size(); i++ ){
            comboArea.add(areaSpinner.get(i).getArea());
        }
    }

    private void getData() {
        refresh.setRefreshing(true);
        Call<List<Producto>> call = ApiProducto.getProductoApi().listar();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                producto = response.body();
                AdapterPush(producto);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
              //  Toast.makeText(getContext(), "No se puede listar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AdapterPush(List<Producto> producto) {
        productoAdapter = new ProductoAdapter(getContext(),producto);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productoAdapter);
    }

    private void addProducto() {
        TextView closeProducto,tituloProducto;
        final EditText txtnombreProducto, txtpesoProducto, txtdiasProducto;
        final Spinner spnAreas, spnMarcas, spnCategorias;;
        Button submitProducto, agregarFoto, btnBarra;;

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_producto);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeProducto = dialog.findViewById(R.id.closeProducto);
        tituloProducto = dialog.findViewById(R.id.tituloProducto);
        tituloProducto.setText("Agregar Producto");

        closeProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        spnMarcas = dialog.findViewById(R.id.spnMarcaProducto);
        spnAreas = dialog.findViewById(R.id.spnAreaProducto);
        spnCategorias = dialog.findViewById(R.id.spnCategoriaProducto);

        ArrayAdapter<CharSequence> adaptadorSpnMarcas = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item, comboMarca);
        spnMarcas.setAdapter(adaptadorSpnMarcas);

        ArrayAdapter<CharSequence> adaptadorSpnCategorias = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item, comboCategoria);
        spnCategorias.setAdapter(adaptadorSpnCategorias);

        ArrayAdapter<CharSequence> adaptadorSpnAreas = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, comboArea);
        spnAreas.setAdapter(adaptadorSpnAreas);
        
        btnBarra = dialog.findViewById(R.id.btnEscanearBarra);
        txtBarras = dialog.findViewById(R.id.txtBarra);
        btnBarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearCodigo();
            }
        });
    }

    private void escanearCodigo() {
        IntentIntegrator.forSupportFragment(Producto_fragment.this)
                .setCaptureActivity(CaptureAct.class)
                .setOrientationLocked(false)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("Escaneando código")
                .initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result =IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IntentIntegrator.REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    txtBarras.setText(result.getContents());
                }else {
                    Toast.makeText(getContext(), "Cancelaste el escáner", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onRefresh() {
        producto.clear();
        getData();
    }
}