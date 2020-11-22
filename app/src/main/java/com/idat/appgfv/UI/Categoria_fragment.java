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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idat.appgfv.Adaptador.CategoriaAdapter;
import com.idat.appgfv.Interfaces.ApiCategoria;
import com.idat.appgfv.Modelo.Marca.Categoria;
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

public class Categoria_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;

    private FloatingActionButton agregarCategoria;

    private List<Categoria> categoria = new ArrayList<>();
    private CategoriaAdapter categoriaAdapter;

    public Categoria_fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categoria_fragment, container, false);
        refresh = view.findViewById(R.id.swipeCategoria);
        recyclerView = view.findViewById(R.id.recyclerCategoria);

        refresh.setOnRefreshListener(this);
        //Refrescar la lista al iniciar
        refresh.post(new Runnable() {
            @Override
            public void run() {
                categoria.clear();
                getData();
            }
        });

        agregarCategoria = view.findViewById(R.id.agregarCategoria);
        agregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoria();
            }
        });
        return view;
    }

    //Agregar nuevo registro
    private void addCategoria(){
        TextView closeCategoria, tituloCategoria;
        final EditText txtCategoria;
        Button submitCategoria;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_categoria);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeCategoria = dialog.findViewById(R.id.closeCategoria);
        tituloCategoria = dialog.findViewById(R.id.tituloCategoria);
        tituloCategoria.setText("Agregar Categoria");

        closeCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtCategoria = dialog.findViewById(R.id.txtCategoria);
        submitCategoria = dialog.findViewById(R.id.submitCategoria);

        submitCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validacion de campos vacíos
                if (txtCategoria.getText().toString().trim().equals("")){
                    txtCategoria.setError("Dato Obligatorio");
                }else {
                    Categoria categoriaSubmit = new Categoria();
                    categoriaSubmit.setCategoria(txtCategoria.getText().toString());
                    enviarCategoria(categoriaSubmit);
                }
            }
        });
        dialog.show();
    }
    //Hacer el envio de la informacion al Api Rest
    private void enviarCategoria(Categoria categoriaSubmit){
        Call<ResponseBody> guardar = ApiCategoria.getCategoriaApi().guardar(categoriaSubmit);
        guardar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null){
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
                        categoria.clear();
                        getData();
                    }
                });
            }else {

                if (response.errorBody() != null) {
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
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Hacemos el llamado de la informacion desde el api rest
    private void getData(){
    refresh.setRefreshing(true);
    Call<List<Categoria>> call = ApiCategoria.getCategoriaApi().listar();
    call.enqueue(new Callback<List<Categoria>>() {
        @Override
        public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
            if (!response.isSuccessful()){
                Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                return;
            }
            categoria = response.body();
            AdapterPush(categoria);
            refresh.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<List<Categoria>> call, Throwable t) {
                 Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    //Pasamos la lista al adaptador para poder visualizarlo
    private void AdapterPush(final List<Categoria> categoria){
        categoriaAdapter = new CategoriaAdapter(getContext(), categoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(categoriaAdapter);


        //Función paraq editar el registro
       categoriaAdapter.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                final Long idCategoria = categoria.get(recyclerView.getChildAdapterPosition(v)).getId();
                final String nombre = categoria.get(recyclerView.getChildAdapterPosition(v)).getCategoria();

                //Mensaje de confirmación para editar el registro
                TextView tituloPopup, mensajePoUp;
                Button btnSi, btnNo;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_editar_c);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                tituloPopup = dialog.findViewById(R.id.txtTituloPopup);
                mensajePoUp = dialog.findViewById(R.id.txtmensajePopUp);
                btnSi = dialog.findViewById(R.id.btn_si);
                btnNo = dialog.findViewById(R.id.btn_no);
                tituloPopup.setText("Importante");
                mensajePoUp.setText("¿Desea Editar este registro?");

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        editarCategoria(idCategoria, nombre);
                    }
                });
                dialog.show();
                return false;
            }
        });

        //Función para eliminar el registro
        categoriaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Long idCategoria = categoria.get(recyclerView.getChildAdapterPosition(v)).getId();

                //Mensaje de confirmación para eliminar el registro
                TextView tituloPopup, mensajePopUp;
                Button tbnSi, btnNo;
                ImageView icono;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_editar_c);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                tituloPopup = dialog.findViewById(R.id.txtTituloPopup);
                mensajePopUp = dialog.findViewById(R.id.txtmensajePopUp);
                icono = dialog.findViewById(R.id.iconoEditar);
                tbnSi = dialog.findViewById(R.id.btn_si);
                btnNo = dialog.findViewById(R.id.btn_no);
                icono.setImageResource(R.drawable.delete_icon);
                tituloPopup.setText("Importante");
                mensajePopUp.setText("¿Desea eliminar este registro?");

             btnNo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dialog.dismiss();
                 }
             });

             tbnSi.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dialog.dismiss();
                     eliminarCategoria(idCategoria);
                 }
             });
             dialog.show();
            }
        });
    }
    //Mandamos la información a eliminar al Api Rest
    private void eliminarCategoria(Long idCategoria){
    Call<ResponseBody> eliminar = ApiCategoria.getCategoriaApi().eliminar(idCategoria);
    eliminar.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
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
                        categoria.clear();
                        getData();
                    }
                });
            } else {
                if (response.errorBody() != null) {
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
             Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void editarCategoria(final Long idCategoria, String nombre){
        TextView closeCategoria,tituloCategoria;
        final EditText txtCategoria;
        Button submitCategoria;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_categoria);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeCategoria = dialog.findViewById(R.id.closeCategoria);
        tituloCategoria = dialog.findViewById(R.id.txtCategoria);
        tituloCategoria.setText("Editar Categoria");

        closeCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtCategoria = dialog.findViewById(R.id.txtCategoria);
        submitCategoria= dialog.findViewById(R.id.submitCategoria);

        txtCategoria.setText(nombre);

        submitCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCategoria.getText().toString().trim().equals("")){
                    txtCategoria.setError("Dato Obligatorio");
                }else {
                    Categoria categoriaEditar = new Categoria();
                    categoriaEditar.setId(idCategoria);
                    categoriaEditar.setCategoria(txtCategoria.getText().toString());
                    actualizarCategoria(categoriaEditar);
                }

            }
        });
        dialog.show();
    }

    private void actualizarCategoria(final Categoria categoriaEditar){
    Call<ResponseBody> editar = ApiCategoria.getCategoriaApi().editar(categoriaEditar);
    editar.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
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
                        categoria.clear();
                        getData();
                    }
                });
            } else {
                if (response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }


    @Override
    public void onRefresh() {
       categoria.clear();
       getData();
    }
}