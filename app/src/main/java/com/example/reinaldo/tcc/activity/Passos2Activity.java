package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * Created by Reinaldo on 06/09/2018.
 */

public class Passos2Activity extends AppCompatActivity {

    private RadioGroup rgOpcoes;
    private RadioButton rbtnGps;
    private RadioButton rbtnManual;
    private RadioButton rbtnWifi;
    private String checkedBtn;
    private Button btnPasso2;
    long numChildren;
    private DatabaseReference UsersRef;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passos2);

        rbtnWifi = findViewById(R.id.rbtnWifi);
        rbtnGps = findViewById(R.id.rbtnGps);
        rbtnManual = findViewById(R.id.rbtnManual);
        rgOpcoes = findViewById(R.id.rgOpcoes);
        btnPasso2 = findViewById(R.id.btnPasso2);


        numChildren = 0;

        UsersRef = FirebaseDatabase.getInstance().getReference().child("PREFERENCIAS").child(Funcoes.pegarKey(getApplicationContext()));

        btnPasso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkin = checkedBtn.toString();

                HashMap userMap = new HashMap();
                userMap.put("checkin", checkin);

                UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                           // Toast.makeText(Passos2Activity.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String message =  task.getException().getMessage();
                            Toast.makeText(Passos2Activity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                startActivity(new Intent(Passos2Activity.this, AdicionarLocalActivity.class));

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbtnGps:
                if (checked)
                    checkedBtn = "GPS";
                break;
            case R.id.rbtnManual:
                if (checked)
                    checkedBtn = "MANUAL";
                break;
            case R.id.rbtnWifi:
                if (checked)
                    checkedBtn = "WIFI";
                break;
        }
    }
}