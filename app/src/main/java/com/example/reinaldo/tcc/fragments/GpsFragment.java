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
import com.example.reinaldo.tcc.activity.AdicionarGPSActivity;
import com.example.reinaldo.tcc.adapter.GPSAdapter;
import com.example.reinaldo.tcc.helper.RecyclerItemClickListener;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.GPS;
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
public class GpsFragment extends Fragment {

    private RecyclerView recyclerViewGPS;
    private List<GPS> listaGPS = new ArrayList<>();
    private GPSAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference gpsRef;
    private ChildEventListener childEventListenerGPS;
    private FloatingActionButton fabGps;
    private ProgressDialog Dialog;
    public GpsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gps, container, false);

        Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Carregando...");
        Dialog.show();

        recyclerViewGPS = view.findViewById(R.id.recyclerListaGPS);

        // Esconder outro botão FloatAction
        fabGps = view.findViewById(R.id.fabGps);
        Funcoes.hideFloatingActionButton(fabGps);

        // Configurar adapter
        adapter = new GPSAdapter(listaGPS, getActivity());

        // Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewGPS.setLayoutManager( layoutManager );
        recyclerViewGPS.setHasFixedSize(true);
        recyclerViewGPS.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewGPS.setAdapter( adapter );

        // configurar referencia
        database = FirebaseDatabase.getInstance().getReference();
        //gpsRef = database.child("GPS");
        gpsRef = Funcoes.pegarReferencia("GPS", getContext());

        recyclerViewGPS.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(),
                        recyclerViewGPS,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getContext(), "Modo de Edição", Toast.LENGTH_LONG).show();
                                GPS gpsSelecionado = listaGPS.get( position );

                                Intent intent = new Intent(getContext(), AdicionarGPSActivity.class);
                                intent.putExtra("gpsSelecionado", gpsSelecionado);
                                startActivity( intent );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getContext(), "Deletado com Sucesso!", Toast.LENGTH_LONG).show();
                                GPS gpsSelecionado = listaGPS.get( position );
                                Query queryRemoverGps = gpsRef.orderByChild("nomeGPS").equalTo(gpsSelecionado.getNomeGPS());

                                queryRemoverGps.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot removerGpsSnapshot : dataSnapshot.getChildren()) {
                                            removerGpsSnapshot.getRef().removeValue();
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

                ));

        recuperarGPS();
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
        gpsRef.removeEventListener(childEventListenerGPS);
    }

    @Override
    public void onStop() {
        super.onStop();
        gpsRef.removeEventListener(childEventListenerGPS);
    }
    public void recuperarGPS(){


        childEventListenerGPS = gpsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GPS gps =  dataSnapshot.getValue(GPS.class);
                listaGPS.add(gps);
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
