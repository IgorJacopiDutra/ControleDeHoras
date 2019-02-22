package com.example.reinaldo.tcc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.GPS;

import java.util.List;

/**
 * Created by IgorJacopiDutra on 11/03/2018.
 */

public class GPSAdapter extends RecyclerView.Adapter<GPSAdapter.MyViewHolder>{

    private List<GPS> listaTarefas;
    private Context context;

    public GPSAdapter(List<GPS> lista, Context c) {

        this.listaTarefas = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_gps_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GPS gps = listaTarefas.get(position);

        holder.bairroGPS.setText( gps.getBairroGPS() + ", " + gps.getCidadeGPS() + ", " + gps.getEstadoGPS() );
        holder.localGPS.setText( "Local: " + gps.getNomeGPSLocal() );
    }

    @Override
    public int getItemCount() {

        return this.listaTarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bairroGPS, localGPS;
        public MyViewHolder(View itemView) {

            super(itemView);
            bairroGPS = itemView.findViewById(R.id.tvGPSBairro);
            localGPS = itemView.findViewById(R.id.tvGPSLocal);
        }
    }


}
