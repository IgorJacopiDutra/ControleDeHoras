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

public class OpcaoPreferenciasAdapter extends RecyclerView.Adapter<OpcaoPreferenciasAdapter.MyViewHolder> {

    List<Preferencias> listaOpcaoPreferencias;

    public OpcaoPreferenciasAdapter(List<Preferencias> List){
        this.listaOpcaoPreferencias = List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferencias_opcao_adapter, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OpcaoPreferenciasAdapter.MyViewHolder holder, int position) {
        Preferencias preferencias = listaOpcaoPreferencias.get(position);

        holder.tvOpcaoPref.setText(preferencias.getOpçaoChecking());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvOpcaoPref;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvOpcaoPref = (TextView) itemView.findViewById(R.id.tvOpcaoPref);
        }
    }

    @Override
    public int getItemCount() {
        return listaOpcaoPreferencias.size();
    }
}
