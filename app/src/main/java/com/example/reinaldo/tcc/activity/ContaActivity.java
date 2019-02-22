package com.example.reinaldo.tcc.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.adapter.AbasAdapter;
import com.example.reinaldo.tcc.fragments.ContaFragment;
import com.example.reinaldo.tcc.fragments.SegundaFragment;
import com.example.reinaldo.tcc.model.Funcoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ContaActivity extends AppCompatActivity {

    private RadioGroup rgOpcoesEdit;
    private RadioButton rbtnGpsEdit;
    private RadioButton rbtnManualEdit;
    private RadioButton rbtnWifiEdit;
    private String checkedBtnEdit;
    private Button btnAlterarCheckin;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        AbasAdapter adapter = new AbasAdapter(getSupportFragmentManager());
        adapter.adicionar(new ContaFragment(), "Horário de Trabalho");
        adapter.adicionar(new SegundaFragment(), "Opção de Chekin-In");

        ViewPager viewPager = (ViewPager) findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        //UserRef = Funcoes.pegarReferencia("PREFERENCIAS", getContext());
        UserRef = FirebaseDatabase.getInstance().getReference().child("PREFERENCIAS").child(Funcoes.pegarKey(getApplicationContext()));

        rbtnWifiEdit = findViewById(R.id.rbtnWifiEdit);
        rbtnGpsEdit = findViewById(R.id.rbtnGpsEdit);
        rbtnManualEdit = findViewById(R.id.rbtnManualEdit);
        rgOpcoesEdit = findViewById(R.id.rgOpcoesEdit);
        btnAlterarCheckin = findViewById(R.id.btnAlterarCheckin);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void salvarCheckin(View view){

        String checkin = checkedBtnEdit.toString();

        HashMap userMap = new HashMap();
        userMap.put("checkin", checkin);

        UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(ContaActivity.this, "Dados salvos com sucesso.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String message =  task.getException().getMessage();
                    Toast.makeText(ContaActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbtnGpsEdit:
                if (checked)
                    checkedBtnEdit = "GPS";
                break;
            case R.id.rbtnManualEdit:
                if (checked)
                    checkedBtnEdit = "MANUAL";
                break;
            case R.id.rbtnWifiEdit:
                if (checked)
                    checkedBtnEdit = "WIFI";
                break;
        }
    }
}
