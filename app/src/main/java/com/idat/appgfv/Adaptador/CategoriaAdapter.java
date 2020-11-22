package com.idat.appgfv.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idat.appgfv.Modelo.Marca.Categoria;
import com.idat.appgfv.R;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolderC> implements View.OnLongClickListener, View.OnClickListener  {

    private Context context;
    private List<Categoria> categoria;

    //Para editar y eliminar
    private View.OnLongClickListener longListener;
    private View.OnClickListener clickListener;

    public CategoriaAdapter(Context context, List<Categoria> categoria){
        this.context = context;
        this.categoria = categoria;
    }

    @NonNull
    @Override
    public CategoriaAdapter.MyViewHolderC onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.categoria_lista, parent, false);

        //Para editar y eliminar
        view.setOnLongClickListener(this);
        view.setOnClickListener(this);

        return new MyViewHolderC(view);
    }

    //Constructor para editar
    public  void setOnLongClickListener(View.OnLongClickListener listener){
        this.longListener = listener;
    }

    //Constructor para eliminar
    public void setOnClickListener(View.OnClickListener listenerClick){
        this.clickListener = listenerClick;
    }

    // //Enviamos los datos a la vista

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolderC holder, int position) {
       holder.noCategoria.setText("#"+String.valueOf(position + 1));
       holder.nameCategoria.setText(categoria.get(position).getCategoria());
    }
    //Retornamos el tamaño de la lista
    @Override
    public int getItemCount() {
        return categoria.size();
    }


    //Editar
    @Override
    public boolean onLongClick(View v) {
        if (longListener != null){
            longListener.onLongClick(v);
        }
        return false;
    }

    //Eliminar
    @Override
    public void onClick(View v) {
    if (clickListener != null){
        clickListener.onClick(v);
    }
    }


    //Instaciamos los campos
    public class MyViewHolderC extends RecyclerView.ViewHolder{
        private TextView nameCategoria, noCategoria;

        public MyViewHolderC(@NonNull View itemView) {
            super(itemView);

            noCategoria = itemView.findViewById(R.id.noCategoria);
            nameCategoria = itemView.findViewById(R.id.nombreCategoria);

        }


    }
}
