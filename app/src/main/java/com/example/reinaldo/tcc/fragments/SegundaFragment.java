package com.example.reinaldo.tcc.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SegundaFragment extends Fragment{

    private RadioGroup rgOpcoesEdit;
    private RadioButton rbtnGpsEdit;
    private RadioButton rbtnManualEdit;
    private RadioButton rbtnWifiEdit;
    private String checkedBtnEdit;
    private Button btnAlterarCheckin;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private String checkin;
    private TextView tvResultado;


    public SegundaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_segunda, container, false);

        mAuth = FirebaseAuth.getInstance();
        //UserRef = Funcoes.pegarReferencia("PREFERENCIAS", getContext());
        UserRef = FirebaseDatabase.getInstance().getReference().child("PREFERENCIAS").child(Funcoes.pegarKey(getContext()));

        rbtnWifiEdit = view.findViewById(R.id.rbtnWifiEdit);
        rbtnGpsEdit = view.findViewById(R.id.rbtnGpsEdit);
        rbtnManualEdit = view.findViewById(R.id.rbtnManualEdit);
        rgOpcoesEdit = view.findViewById(R.id.rgOpcoesEdit);
        btnAlterarCheckin = view.findViewById(R.id.btnAlterarCheckin);
        tvResultado = view.findViewById(R.id.tvResultado);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("checkin")) {
                        checkin = dataSnapshot.child("checkin").getValue().toString();
                        tvResultado.setText("Atualmente sua opção de checking é " + checkin);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }
}
