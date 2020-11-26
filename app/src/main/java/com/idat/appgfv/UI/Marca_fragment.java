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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idat.appgfv.Adaptador.MarcaAdapter;
import com.idat.appgfv.Interfaces.Marca.ApiMarca;
import com.idat.appgfv.Modelo.Marca.Marca;
import com.idat.appgfv.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Marca_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;

    private FloatingActionButton agregarMarca;

    private List<Marca> marca = new ArrayList<>();
    private MarcaAdapter marcaAdapter;

    public Marca_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marca_fragment, container, false);
        refresh = view.findViewById(R.id.swipeMarca);
        recyclerView = view.findViewById(R.id.recyclerMarca);

        refresh.setOnRefreshListener(this);

        //Refrescar la lista al iniciar
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

    //Agregar nuevo registro
    private void addMarca() {
        TextView closeMarca,tituloMarca;
        final EditText txtMarca;
        Button submitMarca;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_marca);
        dialog.setCancelable(false);
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



                //Validacion de campos vacíos
                if(txtMarca.getText().toString().trim().equals("")){
                    txtMarca.setError("Dato obligatorio");
                }else{
                    Marca marcaSubmit = new Marca();
                    marcaSubmit.setMarca(txtMarca.getText().toString());
                    enviarMarca(marcaSubmit);
                }
            }
        });
        dialog.show();
    }

    //Hacer el envio de la informacion al Api Rest
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
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Hacemos el llamado de la informacion desde el api rest
    private void getData() {
        refresh.setRefreshing(true);
        Call<List<Marca>> call = ApiMarca.getMarcaApi().listar();
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
                  //  Toast.makeText(getContext(), "No se puede listar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Pasamos la lista al adaptador para poder visualizarlo
    private void AdapterPush(final List<Marca> marca) {
        marcaAdapter = new MarcaAdapter(getContext(), marca);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(marcaAdapter);

        //Función paraq editar el registro
        marcaAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Long idMarca = marca.get(recyclerView.getChildAdapterPosition(v)).getId();
                final String nombre = marca.get(recyclerView.getChildAdapterPosition(v)).getMarca();

                //Mensaje de confirmación para editar el registro
                TextView tituloPopup, mensajePopUp;
                Button btnSi, btnNo;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_editar);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                tituloPopup = dialog.findViewById(R.id.txtTituloPopup);
                mensajePopUp = dialog.findViewById(R.id.txtmensajePopUp);
                btnSi = dialog.findViewById(R.id.btn_si);
                btnNo = dialog.findViewById(R.id.btn_no);
                tituloPopup.setText("Importante");
                mensajePopUp.setText("¿Desea editar este registro?");

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
                        editarMarca(idMarca, nombre);
                    }
                });
                dialog.show();
                return false;
            }
        });

        //Función para eliminar el registro
        marcaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long idMarca = marca.get(recyclerView.getChildAdapterPosition(v)).getId();

                //Mensaje de confirmación para eliminar el registro
                TextView tituloPopup, mensajePopUp;
                Button btnSi, btnNo;
                ImageView icono;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_editar);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                tituloPopup = dialog.findViewById(R.id.txtTituloPopup);
                mensajePopUp = dialog.findViewById(R.id.txtmensajePopUp);
                icono = dialog.findViewById(R.id.iconoEditar);
                btnSi = dialog.findViewById(R.id.btn_si);
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

                btnSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        eliminarMarca(idMarca);
                    }
                });
                dialog.show();
            }
        });
    }
    //Mandamos la información a eliminar al Api Rest
    private void eliminarMarca(Long idMarca) {
        Call<ResponseBody> eliminar = ApiMarca.getMarcaApi().eliminar(idMarca);
        eliminar.enqueue(new Callback<ResponseBody>() {
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
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarMarca(final Long idMarca, String nombre) {
        TextView closeMarca,tituloMarca;
        final EditText txtMarca;
        Button submitMarca;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_marca);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeMarca = dialog.findViewById(R.id.closeMarca);
        tituloMarca = dialog.findViewById(R.id.tituloMarca);
        tituloMarca.setText("Editar Marca");

        closeMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtMarca = dialog.findViewById(R.id.txtMarca);
        submitMarca = dialog.findViewById(R.id.submitMarca);

        txtMarca.setText(nombre);

        submitMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtMarca.getText().toString().trim().equals("")){
                    txtMarca.setError("Dato obligatorio");
                }else {
                    Marca marcaEditar = new Marca();
                    marcaEditar.setId(idMarca);
                    marcaEditar.setMarca(txtMarca.getText().toString());
                    actualizarMarca(marcaEditar);
                }
            }
        });

        dialog.show();
    }

    private void actualizarMarca(Marca marcaEditar) {
        Call<ResponseBody> editar = ApiMarca.getMarcaApi().editar(marcaEditar);
        editar.enqueue(new Callback<ResponseBody>() {
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
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        marca.clear();
        getData();
    }
}