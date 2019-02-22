package com.example.reinaldo.tcc.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Wifi;

import java.util.List;

/**
 * Created by IgorJacopiDutra on 11/03/2018.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {

    private List<Wifi> listaTarefas;
    private Context context;

    public WifiAdapter(List<Wifi> lista, Context c ) {
        this.listaTarefas = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_wifi_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Wifi wifi = listaTarefas.get(position);
        holder.nomeWifiLocal.setText( "Local: " + wifi.getNomeWifiLocal() );
        holder.nomeWifiWifi.setText( wifi.getNomeWifi() );
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeWifiLocal,nomeWifiWifi;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeWifiLocal = itemView.findViewById(R.id.tvWifiLocal);
            nomeWifiWifi = itemView.findViewById(R.id.tvWifiNome);
        }
    }

}
