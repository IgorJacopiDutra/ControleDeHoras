package com.example.reinaldo.tcc.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.activity.AdicionarWifiActivity;
import com.example.reinaldo.tcc.adapter.WifiAdapter;
import com.example.reinaldo.tcc.helper.RecyclerItemClickListener;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Wifi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WifiFragment extends Fragment {

    private RecyclerView recyclerViewWifi;
    private List<Wifi> listaWifi = new ArrayList<>();
    private WifiAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference wifiRef;
    private ChildEventListener childEventListenerWifi;
    private FloatingActionButton fabWifi;
    private ProgressDialog Dialog;

    public WifiFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the activity_login for this fragment
        View view = inflater.inflate(R.layout.activity_wifi, container, false);

        Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Carregando...");
        Dialog.show();

        recyclerViewWifi = view.findViewById(R.id.recyclerListaWifi);

        // Esconder outro botão FloatAction
        fabWifi = view.findViewById(R.id.fabWifi);
        Funcoes.hideFloatingActionButton(fabWifi);

        // Configurar adapter
        adapter = new WifiAdapter(listaWifi, getActivity());

        // Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewWifi.setLayoutManager( layoutManager );
        recyclerViewWifi.setHasFixedSize(true);
        recyclerViewWifi.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewWifi.setAdapter( adapter );

        // configurar referencia
        database = FirebaseDatabase.getInstance().getReference();
        wifiRef = Funcoes.pegarReferencia("WIFI", getContext());

        // adicionar evento de clique
        recyclerViewWifi.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewWifi,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getContext(), "Modo de Edição", Toast.LENGTH_LONG).show();
                                Wifi wifiSelecionado = listaWifi.get( position );

                                Intent intent = new Intent(getContext(), AdicionarWifiActivity.class);
                                intent.putExtra("wifiSelecionado", wifiSelecionado);
                                startActivity( intent );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getContext(), "Deletado com Sucesso!", Toast.LENGTH_LONG).show();
                                Wifi wifiSelecionado = listaWifi.get( position );
                                Query queryRemoverWifi = wifiRef.orderByChild("nomeWifi").equalTo(wifiSelecionado.getNomeWifi());

                                queryRemoverWifi.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot removerWifiSnapshot : dataSnapshot.getChildren()) {
                                            removerWifiSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
        recuperarWifi();
        Dialog.hide();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        wifiRef.removeEventListener(childEventListenerWifi);
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiRef.removeEventListener(childEventListenerWifi);
    }

    public void recuperarWifi(){
        childEventListenerWifi = wifiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Wifi wifi = dataSnapshot.getValue( Wifi.class );
                listaWifi.add( wifi );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
