package com.idat.appgfv.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.idat.appgfv.Modelo.Marca.Producto;
import com.idat.appgfv.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.MyViewHolderP> implements View.OnLongClickListener, View.OnClickListener {

    private Context context;
    private List<Producto> producto;

    //    //Para editar y eliminar
    private View.OnLongClickListener longListener;
    private View.OnClickListener clickListener;

    public ProductoAdapter(Context context, List<Producto> producto) {
        this.context = context;
        this.producto = producto;
    }

    //Eliminar
    @Override
    public void onClick(View v) {
    if (clickListener != null){
        clickListener.onClick(v);
    }
    }

    //Instanciamos los campos
    public class MyViewHolderP extends RecyclerView.ViewHolder {
        private TextView nameProducto, noProducto,areaProducto,categoriaProducto,marcaProducto, barraProducto,pesoProducto,diasProduto;
        private ImageView imgProducto;

        public MyViewHolderP(@NonNull View itemView) {
            super(itemView);

            noProducto = itemView.findViewById(R.id.noProducto);
            nameProducto = itemView.findViewById(R.id.nameProducto);
            areaProducto = itemView.findViewById(R.id.area_enProducto);
            categoriaProducto = itemView.findViewById(R.id.categoria_enProducto);
            marcaProducto = itemView.findViewById(R.id.marca_enProducto);
            barraProducto = itemView.findViewById(R.id.barraProducto);
            pesoProducto = itemView.findViewById(R.id.pesoProducto);
            diasProduto = itemView.findViewById(R.id.diasProducto);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }


    }

    //Editar
    @Override
    public boolean onLongClick(View v) {
        if (longListener != null){
            longListener.onLongClick(v);
        }
        return false;
    }

    @NonNull
    @Override
    public ProductoAdapter.MyViewHolderP onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.producto_lista, parent, false);

        //Para editar y eliminar
        view.setOnLongClickListener(this);
        view.setOnClickListener(this);

        return new MyViewHolderP(view);
    }

    //Constructor para editar
    public void setOnLongClickListener(View.OnLongClickListener listener){
        this.longListener = listener;
    }

    //Constructor para eliminar
    public void setOnClickListener(View.OnClickListener listenerClick){
        this.clickListener = listenerClick;
    }


    //Enviamos los datos a la vista
    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.MyViewHolderP holder, int position) {
                holder.noProducto.setText("#" + String.valueOf(position + 1));
                holder.nameProducto.setText(producto.get(position).getProducto());
                holder.areaProducto.setText("0"+String.valueOf(position + 1));
                holder.categoriaProducto.setText("0"+String.valueOf(position + 1));
                holder.marcaProducto.setText("0"+String.valueOf(position + 1));
             //   holder.barraProducto.setText(producto.get(position).getBarra());
                holder.pesoProducto.setText(producto.get(position).getPeso());
                holder.diasProduto.setText(String.valueOf(position + 1));
    }

    //Retornamos el tamaño de la lista
    @Override
    public int getItemCount() {
        return producto.size();
    }


}
