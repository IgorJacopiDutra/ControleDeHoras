package com.example.reinaldo.tcc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Preferencias;

import java.util.List;

/**
 * Created by Reinaldo on 03/11/2018.
 */

public class HorasPreferenciasAdapter extends RecyclerView.Adapter<HorasPreferenciasAdapter.MyViewHolder> {

    List<Preferencias> listaHorasPreferencias;

    public HorasPreferenciasAdapter(List<Preferencias> List){
        this.listaHorasPreferencias = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferencias_horas_adapter, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HorasPreferenciasAdapter.MyViewHolder holder, int position) {
        Preferencias preferencias = listaHorasPreferencias.get(position);

        holder.tvHoraPrefUm.setText("Hora de Entrada: " + preferencias.getHorasPrefUm());
        holder.tvHoraPrefDois.setText("Hora da Pausa Para Almoço: " + preferencias.getHorasPrefDois());
        holder.tvHoraPrefTres.setText("Hora da Volta do Almoço: " + preferencias.getHorasPrefTres());
        holder.tvHoraPrefQuatro.setText("Hora de Saída: " + preferencias.getHorasPrefQuatro());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvHoraPrefUm, tvHoraPrefDois, tvHoraPrefTres, tvHoraPrefQuatro;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvHoraPrefUm = (TextView) itemView.findViewById(R.id.tvHoraPrefUm);
            tvHoraPrefDois = (TextView) itemView.findViewById(R.id.tvHoraPrefDois);
            tvHoraPrefTres = (TextView) itemView.findViewById(R.id.tvHoraPrefTres);
            tvHoraPrefQuatro = (TextView) itemView.findViewById(R.id.tvHoraPrefQuatro);
        }
    }

    @Override
    public int getItemCount() {
        return listaHorasPreferencias.size();
    }
}
