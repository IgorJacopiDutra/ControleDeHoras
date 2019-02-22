package com.example.reinaldo.tcc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Horas;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reinaldo on 25/08/2018.
 */

public class RelatoriosAdapter extends RecyclerView.Adapter<RelatoriosAdapter.MyViewHolder> {

        //implements Filterable{

    List<Horas> listaHoras;
    List<Horas> listaRelatoriosFull;

    public RelatoriosAdapter(List<Horas> List){
        this.listaHoras = List;
        this.listaRelatoriosFull = new ArrayList<>(List);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_relatorios_adapter, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RelatoriosAdapter.MyViewHolder holder, int position) {
        Horas hora = listaHoras.get(position);

        holder.tvData.setText(hora.getData());
        holder.tvHoraUm.setText("Entrada: " + hora.getHoraUm());
        holder.tvHoraDois.setText("Pausa para Almoço: " + hora.getHoraDois());
        holder.tvHoraTres.setText("Volta do Almoço: " + hora.getHoraTres());
        holder.tvHoraQuatro.setText("Saída: " + hora.getHoraQuatro());;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvData, tvHoraUm, tvHoraDois, tvHoraTres, tvHoraQuatro;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvData = (TextView) itemView.findViewById(R.id.tvData);
            tvHoraUm = (TextView) itemView.findViewById(R.id.tvHoraUm);
            tvHoraDois = (TextView) itemView.findViewById(R.id.tvHoraDois);
            tvHoraTres = (TextView) itemView.findViewById(R.id.tvHoraTres);
            tvHoraQuatro = (TextView) itemView.findViewById(R.id.tvHoraQuatro);
        }
    }

    @Override
    public int getItemCount() {
        return listaHoras.size();
    }

   /* @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Horas> listaHorasFiltrada = new ArrayList<>();

            if (constraint == null || constraint.length()== 0){
                listaHorasFiltrada.addAll(listaRelatoriosFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Horas horas : listaRelatoriosFull) {
                    if(horas.getData().toLowerCase().contains(filterPattern)){
                        listaHorasFiltrada.add(horas);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = listaHorasFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            listaHoras.clear();
            listaHoras.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };*/
}
