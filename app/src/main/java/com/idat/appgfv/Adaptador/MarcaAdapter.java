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

import java.util.ArrayList;
import java.util.List;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MyViewHolder> {

    private Context context;
    private List<Marca> marca;

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

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarcaAdapter.MyViewHolder holder, int position) {
        holder.noMarca.setText("#" + String.valueOf(position + 1));
        holder.nameMarca.setText(marca.get(position).getMarca());
    }

    @Override
    public int getItemCount() {
        return marca.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameMarca, noMarca;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noMarca = itemView.findViewById(R.id.noMarca);
            nameMarca = itemView.findViewById(R.id.nombreMarca);
        }
    }
}
