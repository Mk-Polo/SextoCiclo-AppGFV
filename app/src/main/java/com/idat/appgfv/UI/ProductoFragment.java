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
import com.idat.appgfv.Adaptador.ProductoAdapter;
import com.idat.appgfv.Interfaces.ApiProducto;
import com.idat.appgfv.Modelo.Marca.Producto;
import com.idat.appgfv.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private FloatingActionButton agregarProducto;
    private List<Producto> producto = new ArrayList<>();
    private ProductoAdapter productoAdapter;

    public ProductoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_producto_fragment, container, false);
        refresh = view.findViewById(R.id.swipeProducto);
        recyclerView = view.findViewById(R.id.recyclerProducto);

        refresh.setOnRefreshListener(this);

        //refrescar la lista l iniciar
        refresh.post(new Runnable() {
            @Override
            public void run() {
                producto.clear();
                getData();
            }
        });

        agregarProducto = view.findViewById(R.id.agregarProducto);
        agregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducto();
            }
        });

        return view;
    }
    //Agregar nuevo registro
    private void addProducto() {
        TextView closeProducto, tituloProducto;
        final EditText txtProducto;
        Button submitProducto;
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

        txtProducto = dialog.findViewById(R.id.txtProducto);
        submitProducto = dialog.findViewById(R.id.submitProducto);

        submitProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validacion de campos vacíos
                if (txtProducto.getText().toString().trim().equals("")){
                    txtProducto.setError("Dato obligatorio");
                }else {
                    Producto productoSubmit = new Producto();
                    productoSubmit.setProducto(txtProducto.getText().toString());
                    enviarProducto(productoSubmit);
                }
            }
        });
        dialog.show();
    }

    //Hacer el envio de la informacion al Api Rest
    private void enviarProducto(Producto productoSubmit){
         Call<ResponseBody> guardar = ApiProducto.getProductoApi().guardar(productoSubmit);
         guardar.enqueue(new Callback<ResponseBody>() {
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
                             producto.clear();
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
    //Hacemos el llamado de la informacion desde el api rest
    private void getData() {
        refresh.setRefreshing(true);
        Call<List<Producto>> call = ApiProducto.getProductoApi().listar();
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {

                if (!response.isSuccessful()){
                Toast.makeText(getContext(), "Codigo: " + response.code(), Toast.LENGTH_SHORT).show();
                return;
            }
            producto = response.body();
            AdapterPush(producto);
            refresh.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<List<Producto>> call, Throwable t) {
            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
      });

   }

    //Pasamos la lista al adaptador para poder visualizarlo
    private void AdapterPush(final List<Producto> producto) {
        productoAdapter = new ProductoAdapter(getContext(), producto);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productoAdapter);

        //Función paraq editar el registro
        productoAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Long idProducto = producto.get(recyclerView.getChildAdapterPosition(v)).getId();
                final String nombre = producto.get(recyclerView.getChildAdapterPosition(v)).getProducto();


                //Mensaje de confirmación para editar el registro
                TextView tituloPopup, mensajePopUp;
                Button btnSi, btnNo;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_edidat_p);
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
                        editarProducto(idProducto, nombre);
                    }
                });
                dialog.show();
                return false;
            }
        });

        //Función para eliminar el registro
        productoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Long idProducto = producto.get(recyclerView.getChildAdapterPosition(v)).getId();

                //Mensaje de confirmación para eliminar el registro
                TextView tituloPopup, mensajePopUp;
                Button btnSi, btnNo;
                ImageView icono;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_edidat_p);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                tituloPopup = dialog.findViewById(R.id.txtTituloPopup);
                mensajePopUp = dialog.findViewById(R.id.txtmensajePopUp);
                icono = dialog.findViewById(R.id.iconoEditar);
                btnSi = dialog.findViewById(R.id.btn_si);
                btnNo = dialog.findViewById(R.id.btn_no);
                icono.setImageResource(R.drawable.delete_icon);
                tituloPopup.setText("Imporante");
                mensajePopUp.setText("¿Desea pordr eliminar este registro?");

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
                        eliminarProducto(idProducto);
                    }
                });
                dialog.show();
            }
        });

    }
    //Mandamos la información a eliminar al Api Rest
    private void eliminarProducto(Long idProducto) {
        Call<ResponseBody> eliminar = ApiProducto.getProductoApi().eliminar(idProducto);
        eliminar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        try {
                            Toast.makeText(getContext(),response.body().string(), Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            producto.clear();
                            getData();
                        }
                    });
                }else {
                    if (response.errorBody() != null){
                        try {
                            Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
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

    private void editarProducto(final Long idProducto, String nombre) {
        TextView closeProducto,tituloProducto;
        final EditText txtProducto;
        Button submitPorducto;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_producto);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        closeProducto = dialog.findViewById(R.id.closeProducto);
        tituloProducto = dialog.findViewById(R.id.tituloProducto);
        tituloProducto.setText("Editar Producto");

        closeProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtProducto = dialog.findViewById(R.id.txtProducto);
        submitPorducto = dialog.findViewById(R.id.submitProducto);

        txtProducto.setText(nombre);

        submitPorducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtProducto.getText().toString().trim().equals("")){
                    txtProducto.setError("Dato Obligatorio");
                }else {
                    Producto productoEditar = new Producto();
                    productoEditar.setId(idProducto);
                    productoEditar.setProducto(txtProducto.getText().toString());;
                    actualizarProducto(productoEditar);
                }
            }
        });

        dialog.show();

    }

    private void actualizarProducto(final Producto productoEditar) {
    Call<ResponseBody> editar = ApiProducto.getProductoApi().editar(productoEditar);
    editar.enqueue(new Callback<ResponseBody>() {
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
                        producto.clear();
                        getData();
                    }
                });
            } else {
                if (response.errorBody() != null) {
                    try {
                        Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                        ;

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
    producto.clear();
    getData();
    }
}