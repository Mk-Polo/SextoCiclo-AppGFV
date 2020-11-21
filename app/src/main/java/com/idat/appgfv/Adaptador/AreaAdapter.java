package com.idat.appgfv.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.idat.appgfv.Modelo.Marca.Area;

import com.idat.appgfv.R;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MyViewHolder> implements View.OnLongClickListener, View.OnClickListener {

    private Context context;
    private List<Area> area;

    //Para editar y eliminar
    private View.OnLongClickListener longListener;
    private View.OnClickListener clickListener;

    public AreaAdapter(Context context, List<Area> area) {
        this.context = context;
        this.area = area;
    }

    @NonNull
    @Override
    public AreaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.area_lista, parent,false);

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
    public void onBindViewHolder(@NonNull AreaAdapter.MyViewHolder holder, int position) {
        holder.noArea.setText("#" + String.valueOf(position + 1));
        holder.nameArea.setText(area.get(position).getArea());
    }

    //Retornamos el tamaño de la lista
    @Override
    public int getItemCount() {
        return area.size();
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
        private TextView nameArea, noArea;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noArea = itemView.findViewById(R.id.noArea);
            nameArea = itemView.findViewById(R.id.nombreArea);
        }
    }
}
