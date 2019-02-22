package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.MaskEditUtil;
import com.example.reinaldo.tcc.model.Preferencias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.reinaldo.tcc.model.Preferencias.InserirPreferenciasFirebasePasso3;

/**
 * Created by Reinaldo on 06/09/2018.
 */

public class Passos1Activity extends AppCompatActivity {

    private EditText edtHora1;
    private EditText edtHora3;
    private EditText edtHora4;
    private EditText edtHora2;

    private Button btnConfirma;

    //long numChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos1);

        edtHora1 = findViewById(R.id.edtHora1);
        edtHora2 = findViewById(R.id.edtHora2);
        edtHora3 = findViewById(R.id.edtHora3);
        edtHora4 = findViewById(R.id.edtHora4);

        edtHora1.addTextChangedListener(MaskEditUtil.mask(edtHora1, MaskEditUtil.FORMAT_HOUR));
        edtHora2.addTextChangedListener(MaskEditUtil.mask(edtHora2, MaskEditUtil.FORMAT_HOUR));
        edtHora3.addTextChangedListener(MaskEditUtil.mask(edtHora3, MaskEditUtil.FORMAT_HOUR));
        edtHora4.addTextChangedListener(MaskEditUtil.mask(edtHora4, MaskEditUtil.FORMAT_HOUR));

        btnConfirma = findViewById(R.id.btnConfirma);

       // numChildren = 0;

        DatabaseReference passos1Teste = Funcoes.pegarReferencia("PREFERENCIAS",getApplicationContext());

        btnConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( InserirPreferenciasFirebasePasso3(
                        edtHora1.getText().toString(),
                        edtHora2.getText().toString(),
                        edtHora3.getText().toString(),
                        edtHora4.getText().toString(),
                        getApplicationContext())){
                    startActivity(new Intent(Passos1Activity.this, Passos2Activity.class));
                } else {
                    Toast.makeText(Passos1Activity.this, "Erro! Revise Seus dados!", Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}
