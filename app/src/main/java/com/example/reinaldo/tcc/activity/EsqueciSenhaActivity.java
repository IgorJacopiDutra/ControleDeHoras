package com.example.reinaldo.tcc.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciSenhaActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String emailAddress;
    private TextInputEditText tietEmailSenhaNova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        tietEmailSenhaNova = (TextInputEditText) findViewById(R.id.tietEmailSenhaNova);
        auth = FirebaseAuth.getInstance();

    }

    public void enviarRedefinicao(View view) {
        emailAddress = tietEmailSenhaNova.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Email enviado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Estamos com um problema", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        onDestroy();

    }
}
