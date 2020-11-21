package com.idat.appgfv.UI;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
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
import com.idat.appgfv.Adaptador.AreaAdapter;
import com.idat.appgfv.Adaptador.MarcaAdapter;
import com.idat.appgfv.Interfaces.ApiArea;
import com.idat.appgfv.Interfaces.ApiMarca;
import com.idat.appgfv.Modelo.Marca.Area;
import com.idat.appgfv.Modelo.Marca.Marca;
import com.idat.appgfv.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Area_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;

    private FloatingActionButton agregarArea;

    private List<Area> area = new ArrayList<>();
    private AreaAdapter areaAdapter;

    public Area_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area_fragment, container, false);
        refresh = view.findViewById(R.id.swipeArea);
        recyclerView = view.findViewById(R.id.recyclerArea);
        refresh.setOnRefreshListener(this);

        //Refrescar la lista al iniciar
        refresh.post(new Runnable() {
            @Override
            public void run() {
                area.clear();
                getData();
            }
        });

        agregarArea = view.findViewById(R.id.agregarArea);
        agregarArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArea();
            }
        });


        return view;

    }

    //Agregar nuevo registro
    private void addArea() {
        TextView closeArea,tituloArea;
        final EditText txtArea;
        Button submitArea;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_area);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeArea = dialog.findViewById(R.id.closeArea);
        tituloArea = dialog.findViewById(R.id.tituloArea);
        tituloArea.setText("Agregar Área");

        closeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtArea = dialog.findViewById(R.id.txtArea);
        submitArea = dialog.findViewById(R.id.submitArea);

        submitArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validacion de campos vacíos
                if(txtArea.getText().toString().trim().equals("")){
                    txtArea.setError("Dato obligatorio");
                }else{
                    Area areaSubmit = new Area();
                    areaSubmit.setArea(txtArea.getText().toString());
                   // Toast.makeText(getContext(), txtArea.getText().toString(), Toast.LENGTH_SHORT).show();
                    enviarArea(areaSubmit);
                }
            }
        });
        dialog.show();
    }


    //Hacer el envio de la informacion al Api Rest
    private void enviarArea(Area areaSubmit) {
        Call<ResponseBody> guardar = ApiArea.getAreaApi().guardar(areaSubmit);
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
                            area.clear();
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
        Call<List<Area>> call = ApiArea.getAreaApi().listar();
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                area = response.body();
                AdapterPush(area);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    //Pasamos la lista al adaptador para poder visualizarlo
    private void AdapterPush(final List<Area> area) {
        areaAdapter = new AreaAdapter(getContext(), area);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(areaAdapter);

        //Función paraq editar el registro
       areaAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Long idArea = area.get(recyclerView.getChildAdapterPosition(v)).getId();
                final String nombre = area.get(recyclerView.getChildAdapterPosition(v)).getArea();

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
                        editarArea(idArea, nombre);
                    }
                });
                dialog.show();
                return false;
            }
        });



        //Función para eliminar el registro
        areaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long idArea = area.get(recyclerView.getChildAdapterPosition(v)).getId();

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
                        eliminarArea(idArea);
                    }
                });
                dialog.show();
            }
        });
    }


    //Mandamos la información a eliminar al Api Rest
    private void eliminarArea(Long idArea) {
        Call<ResponseBody> eliminar = ApiArea.getAreaApi().eliminar(idArea);
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
                            area.clear();
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



    private void editarArea(final Long idArea, String nombre) {
        TextView closeArea,tituloArea;
        final EditText txtArea;
        Button submitArea;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_area);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeArea = dialog.findViewById(R.id.closeArea);
        tituloArea = dialog.findViewById(R.id.tituloArea);
        tituloArea.setText("Editar Área");

        closeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtArea = dialog.findViewById(R.id.txtArea);
        submitArea = dialog.findViewById(R.id.submitArea);

        txtArea.setText(nombre);

        submitArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtArea.getText().toString().trim().equals("")){
                    txtArea.setError("Dato obligatorio");
                }else {
                    Area areaEditar = new Area();
                    areaEditar.setId(idArea);
                    areaEditar.setArea(txtArea.getText().toString());
                    actualizarArea(areaEditar);
                }
            }
        });

        dialog.show();
    }


    private void actualizarArea(Area areaEditar) {
        Call<ResponseBody> editar = ApiArea.getAreaApi().editar(areaEditar);
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
                            area.clear();
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
        area.clear();
        getData();
    }
}