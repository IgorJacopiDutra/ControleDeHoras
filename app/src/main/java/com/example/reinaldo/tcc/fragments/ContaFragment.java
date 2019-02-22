package com.example.reinaldo.tcc.fragments;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.activity.MainActivity;
import com.example.reinaldo.tcc.activity.Passos1Activity;
import com.example.reinaldo.tcc.activity.Passos2Activity;
import com.example.reinaldo.tcc.adapter.AbasAdapter;
import com.example.reinaldo.tcc.model.Funcoes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.reinaldo.tcc.model.Preferencias.InserirPreferenciasFirebaseEditar;
import static com.example.reinaldo.tcc.model.Preferencias.InserirPreferenciasFirebasePasso3;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContaFragment extends Fragment {

    private EditText hora1, hora2, hora3, hora4;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private Button btnAlterar;
    private String checking;

    public ContaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conta, container, false);

        mAuth = FirebaseAuth.getInstance();
        //UserRef = Funcoes.pegarReferencia("PREFERENCIAS", getContext());
        UserRef = FirebaseDatabase.getInstance().getReference().child("PREFERENCIAS").child(Funcoes.pegarKey(getContext()));


        hora1 = view.findViewById(R.id.edtHora1Edit);
        hora2 = view.findViewById(R.id.edtHora2Edit);
        hora3 = view.findViewById(R.id.edtHora3Edit);
        hora4 = view.findViewById(R.id.edtHora4Edit);
        btnAlterar = view.findViewById(R.id.btnSalvarNovoHorario);

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( InserirPreferenciasFirebaseEditar(
                        checking.toString(),
                        hora1.getText().toString(),
                        hora2.getText().toString(),
                        hora3.getText().toString(),
                        hora4.getText().toString(),
                        getContext())){
                    hora1.setEnabled(false);
                    hora2.setEnabled(false);
                    hora3.setEnabled(false);
                    hora4.setEnabled(false);
                    Toast.makeText(getContext(), "Dados salvos!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Erro! Revise Seus dados!", Toast.LENGTH_LONG).show();

                }
            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("horasPrefUm")) {
                        String hora1DB = dataSnapshot.child("horasPrefUm").getValue().toString();
                        hora1.setText(hora1DB);
                    }

                    if (dataSnapshot.hasChild("horasPrefDois")) {
                        String hora2DB = dataSnapshot.child("horasPrefDois").getValue().toString();
                        hora2.setText(hora2DB);
                    }

                    if (dataSnapshot.hasChild("horasPrefTres")) {
                        String hora3DB = dataSnapshot.child("horasPrefTres").getValue().toString();
                        hora3.setText(hora3DB);
                    }

                    if (dataSnapshot.hasChild("horasPrefQuatro")) {
                        String hora4DB = dataSnapshot.child("horasPrefQuatro").getValue().toString();
                        hora4.setText(hora4DB);
                    }

                    if (dataSnapshot.hasChild("checkin")) {
                        checking = dataSnapshot.child("checkin").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return (view);

    }


}
