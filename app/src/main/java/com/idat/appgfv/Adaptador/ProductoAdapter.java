package com.idat.appgfv.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idat.appgfv.Modelo.Producto.Producto;
import com.idat.appgfv.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.MyViewProducto> implements View.OnLongClickListener, View.OnClickListener{

    private Context context;
    private List<Producto> producto;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

    public ProductoAdapter(Context context, List<Producto> producto) {
        this.context = context;
        this.producto = producto;
    }

    @NonNull
    @Override
    public ProductoAdapter.MyViewProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.producto_lista, parent, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return new MyViewProducto(view);
    }

    public void setOnLongClickListener(View.OnLongClickListener listener){
        this.longClickListener = listener;
    }

    public void setListenerClick(View.OnClickListener producListener){
        this.clickListener = producListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.MyViewProducto holder, int position) {
        holder.marcaProducto.setText(producto.get(position).getMarca().getMarca());
        holder.nombreProducto.setText(producto.get(position).getProducto());
        holder.pesoProducto.setText(producto.get(position).getPeso());
        holder.diasProducto.setText(String.valueOf(producto.get(position).getDias()));
        if(producto.get(position).getImagen() != null){
            Picasso.with(context).load(producto.get(position).getImagen()).into(holder.imagen);
        }else{
            holder.imagen.setImageResource(R.drawable.ic_launcher_foto_muestra_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return producto.size();
    }

    @Override
    public void onClick(View v) {
        if(clickListener != null){
            clickListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(longClickListener != null){
            longClickListener.onLongClick(v);
        }
        return false;
    }

    public class MyViewProducto extends RecyclerView.ViewHolder {
        private TextView nombreProducto, marcaProducto, diasProducto, pesoProducto;
        private ImageView imagen;

        public MyViewProducto(@NonNull View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.imagenProductoLista);
            marcaProducto = (TextView) itemView.findViewById(R.id.productoMarcaLista);
            nombreProducto = (TextView) itemView.findViewById(R.id.nombreProductoLista);
            pesoProducto = (TextView) itemView.findViewById(R.id.pesoProductoLista);
            diasProducto = (TextView) itemView.findViewById(R.id.fechaVencLista);
        }
    }
}
