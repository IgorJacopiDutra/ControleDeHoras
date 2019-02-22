package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.fragments.PrincipalFragment;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Local;
import com.example.reinaldo.tcc.model.MaskEditUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.reinaldo.tcc.model.Local.InserirLocalFirebase;


public class AdicionarLocalActivity extends AppCompatActivity {

    private EditText edAddLocal, edCEP, edEndereco, edNumero, edBairro, edCNPJ, edCidade;
    private Spinner estado_spn;
    private Local localAtual;
    long numChildren;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_local);

        estado_spn = findViewById(R.id.estado_spn);

        edAddLocal = findViewById(R.id.edAddLocal);
        edCEP = findViewById(R.id.edCEP);
        edEndereco = findViewById(R.id.edEndereco);
        edNumero = findViewById(R.id.edNumero);
        edBairro = findViewById(R.id.edBairro);
        edCNPJ = findViewById(R.id.edCNPJ);
        edCidade = findViewById(R.id.edCidade);

        edCEP.addTextChangedListener(MaskEditUtil.mask(edCEP, MaskEditUtil.FORMAT_CEP));
        edCNPJ.addTextChangedListener(MaskEditUtil.mask(edCNPJ, MaskEditUtil.FORMAT_CPF));

        numChildren = 0;
        // Recuperar Wifi
        localAtual = (Local) getIntent().getSerializableExtra("localSelecionado");
        if ( localAtual != null ){
            edAddLocal.setText(localAtual.getNomeLocal());
            numChildren = localAtual.getId();
        } else {
            DatabaseReference localTeste = Funcoes.pegarReferencia("LOCAL", getApplicationContext());
            Query pesquisa = localTeste.orderByChild("id").limitToFirst(1);

            pesquisa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Local dadosLocal = objSnapshot.getValue(Local.class);
                        //Toast.makeText(getApplicationContext(), String.valueOf(dadosUsuario.getId()) , Toast.LENGTH_LONG).show();
                        numChildren = dadosLocal.getId() + 1;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //referencia.child("ESTADOS").addValueEventListener(new ValueEventListener() {
        Funcoes.pegarReferencia("ESTADOS", getApplicationContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> estados = new ArrayList<String>();

                for (DataSnapshot estadosSnapshot : dataSnapshot.getChildren()) {
                    String estadosNome = estadosSnapshot.child("Nome").getValue(String.class);
                    estados.add(estadosNome);
                }

                estado_spn = findViewById(R.id.estado_spn);
                ArrayAdapter<String> estadosAdapter = new ArrayAdapter<String>(AdicionarLocalActivity.this, android.R.layout.simple_spinner_item, estados);
                estadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                estado_spn.setAdapter(estadosAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*estado_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_gps, menu);
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:
                if ( (estado_spn.getSelectedItem() != null) && (edAddLocal.getText().length() != 0) ){
                    if (InserirLocalFirebase(Long.toString(numChildren),edAddLocal.getText().toString(),edCEP.getText().toString(),
                        edEndereco.getText().toString(),
                        edNumero.getText().toString(),
                        edBairro.getText().toString(),
                        edCNPJ.getText().toString(),
                        estado_spn.getSelectedItem().toString(),
                        edCidade.getText().toString(),
                        getApplicationContext())){
                        Toast.makeText(AdicionarLocalActivity.this, "Local Salvo Com Sucesso!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdicionarLocalActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(AdicionarLocalActivity.this, "Erro ao Salvar o Local!", Toast.LENGTH_LONG).show();
                    }
                    finish();
                    break;
                    } else{
                        Toast.makeText(AdicionarLocalActivity.this,"Por Favor, Preencha Todos os Campos.", Toast.LENGTH_LONG).show();
                    }
        }
        return super.onOptionsItemSelected(item);
    }
}