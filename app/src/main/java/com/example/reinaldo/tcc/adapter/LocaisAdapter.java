package com.example.reinaldo.tcc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Local;

import java.util.List;

/**
 * Created by IgorJacopiDutra on 10/03/2018.
 */

public class LocaisAdapter extends RecyclerView.Adapter<LocaisAdapter.MyViewHolder> {

    private List<Local> listaLocais;
    private Context context;

    public LocaisAdapter(List<Local> lista, Context c) {
        this.listaLocais = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_local_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Local local = listaLocais.get(position);
        holder.nomeLocal.setText( "Local: " + local.getNomeLocal() );
        holder.endereco.setText(local.getEndereco() + " - " + local.getBairro() + " - " + local.getCidade() + " - " + local.getEstado());
    }

    @Override
    public int getItemCount() {
        return this.listaLocais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeLocal, endereco;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeLocal = itemView.findViewById(R.id.tvNomeLocal);
            endereco = itemView.findViewById(R.id.tvNomeLocalTitulo);
        }
    }
}
