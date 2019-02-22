package com.example.reinaldo.tcc.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.reinaldo.tcc.activity.AdicionarLocalActivity;
import com.example.reinaldo.tcc.adapter.LocaisAdapter;
import com.example.reinaldo.tcc.helper.RecyclerItemClickListener;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Local;
import com.example.reinaldo.tcc.model.Wifi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends Fragment {

    private RecyclerView recyclerViewLocais;
    private List<Local> listaLocal = new ArrayList<>();
    private LocaisAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference locaisRef;
    private ChildEventListener childEventListenerLocais;
    private FloatingActionButton fabLocal;
    private ProgressDialog Dialog;
    public LocalFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the activity_login for this fragment

        View view = inflater.inflate(R.layout.activity_local, container, false);

        Dialog = new ProgressDialog(getContext());
        Dialog.setMessage("Carregando...");
        Dialog.show();

        // Esconder outro botão FloatAction
        fabLocal = view.findViewById(R.id.fabLocal);
        Funcoes.hideFloatingActionButton(fabLocal);

        recyclerViewLocais = view.findViewById(R.id.recyclerListaLocais);

        // Configurar adapter
        adapter = new LocaisAdapter(listaLocal, getActivity());

        // Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLocais.setLayoutManager( layoutManager );
        recyclerViewLocais.setHasFixedSize(true);
        recyclerViewLocais.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerViewLocais.setAdapter( adapter );

        // configurar referencia
        database = FirebaseDatabase.getInstance().getReference();
        //locaisRef = database.child(Funcoes.pegarKey(view.getContext()));
        //locaisRef = database;//Funcoes.pegarReferencia("LOCAL",getContext());
        //locaisRef = database.child("LOCAL");
        locaisRef = Funcoes.pegarReferencia("LOCAL", getContext());

        recyclerViewLocais.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerViewLocais,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), "Modo de Edição", Toast.LENGTH_LONG).show();
                        Local localSelecionado = listaLocal.get( position );

                        Intent intent = new Intent(getContext(), AdicionarLocalActivity.class);
                        intent.putExtra("localSelecionado", localSelecionado);
                        startActivity( intent );
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getContext(), "Deletado com Sucesso!", Toast.LENGTH_LONG).show();
                        Local wifiSelecionado = listaLocal.get( position );
                        Query queryRemoverLocal = locaisRef.orderByChild("nomeLocal").equalTo(wifiSelecionado.getNomeLocal());

                        queryRemoverLocal.addListenerForSingleValueEvent(new ValueEventListener() {
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

        recuperarLocais();
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
        locaisRef.removeEventListener(childEventListenerLocais);
    }

    @Override
    public void onStop() {
        super.onStop();
        locaisRef.removeEventListener(childEventListenerLocais);
    }

    public void recuperarLocais(){

        childEventListenerLocais = locaisRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Local local =  dataSnapshot.getValue(Local.class);
                listaLocal.add(local);
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
