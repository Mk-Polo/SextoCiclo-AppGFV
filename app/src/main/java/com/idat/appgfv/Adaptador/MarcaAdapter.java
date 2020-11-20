package com.idat.appgfv.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idat.appgfv.Modelo.Marca.Marca;
import com.idat.appgfv.R;

import java.util.List;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MyViewHolder> implements View.OnLongClickListener, View.OnClickListener{

    private Context context;
    private List<Marca> marca;

    //Para editar y eliminar
    private View.OnLongClickListener longListener;
    private View.OnClickListener clickListener;

    public MarcaAdapter(Context context, List<Marca> marca) {
        this.context = context;
        this.marca = marca;
    }

    @NonNull
    @Override
    public MarcaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.marca_lista, parent,false);

        //Para editar y eliminar
        view.setOnLongClickListener(this);
        view.setOnClickListener(this);

        return new MyViewHolder(view);
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
    public void onBindViewHolder(@NonNull MarcaAdapter.MyViewHolder holder, int position) {
        holder.noMarca.setText("#" + String.valueOf(position + 1));
        holder.nameMarca.setText(marca.get(position).getMarca());
    }

    //Retornamos el tamaño de la lista
    @Override
    public int getItemCount() {
        return marca.size();
    }

    //Editar
    @Override
    public boolean onLongClick(View v) {
        if(longListener != null){
            longListener.onLongClick(v);
        }
        return false;
    }

    //Eliminar
    @Override
    public void onClick(View v) {
        if(clickListener != null){
            clickListener.onClick(v);
        }
    }

    //Instanciamos los campos
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameMarca, noMarca;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noMarca = itemView.findViewById(R.id.noMarca);
            nameMarca = itemView.findViewById(R.id.nombreMarca);
        }
    }
}
