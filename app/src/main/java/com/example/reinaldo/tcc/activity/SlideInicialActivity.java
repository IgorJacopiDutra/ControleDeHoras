package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class SlideInicialActivity extends IntroActivity {
    private FirebaseAuth usuarioAtual = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.activity_login.activity_slide_inicial);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimaryDark)
                .fragment(R.layout.fragment_intro1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimaryDark)
                .fragment(R.layout.fragment_intro2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimaryDark)
                .fragment(R.layout.fragment_intro3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimaryDark)
                .fragment(R.layout.fragment_intro4)
                .canGoForward(false)
                .build());
    }

    public void btEntrar (View view){
        finish();
        startActivity(new Intent(this, LoginActivity.class));

    }

    public void btCadastrar (View view){
        finish();
        startActivity(new Intent(this, CadastroActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(usuarioAtual.getCurrentUser()!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
           // Toast.makeText(getApplicationContext(), "Passamos o (Slide Inicial) e (Primeiros Passos) pois você ja esta logado", Toast.LENGTH_LONG).show();
            Log.i("CurrentUser", "Usuario Logado!");
        } else {
         //   Toast.makeText(getApplicationContext(), "Você não estao logado, então faça o slide inicial", Toast.LENGTH_LONG).show();
            Log.i("CurrentUser", "Usuario Não Logado!");
        }
    }
}
