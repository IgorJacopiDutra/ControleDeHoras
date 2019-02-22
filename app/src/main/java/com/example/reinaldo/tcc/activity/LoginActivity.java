package com.example.reinaldo.tcc.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class
LoginActivity extends AppCompatActivity {

    private Button botaoLogin;
    private TextInputEditText email;
    private TextInputEditText senha;
    private TextView novaSenha;
    private FirebaseAuth usuarioAtual = FirebaseAuth.getInstance();
    private boolean bFezLogin;
    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botaoLogin = (Button) findViewById(R.id.btn_login);
        email = (TextInputEditText) findViewById(R.id.edtEmail);
        senha = (TextInputEditText) findViewById(R.id.edtSenha);
        novaSenha = (TextView) findViewById(R.id.tvEsqueci);
        bFezLogin = false;

        // REMOVENDO BARRA DO TÍTULO DO APP
        getSupportActionBar().hide();

        // ESTAMOS DESLOGANDO PARA TESTAR
        //usuarioAtual.signOut();
    }

    public void proximaTela (View view){
//        if ((email.getText().length() == 0) || (senha.getText().length() == 0)){
//            Toast.makeText(getApplicationContext(), "Informe o Login e a Senha", Toast.LENGTH_SHORT).show();
//        } else {
        usuarioAtual.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity (intent);
                            finish();
                            Log.i("SignIn", "Sucesso ao Logar");
                            bFezLogin = true;
                        } else {
                            Log.i("SignIn", "Erro ao Logar");
                            Toast.makeText(getApplicationContext(), "Problemas ao logar", Toast.LENGTH_LONG).show();
                            bFezLogin = false;
                        }
                    }
                });
//        }

        if (bFezLogin = true) {
            DatabaseReference usuariosteste = referencia.child("CADASTROS");
            Query pesquisa = usuariosteste.orderByChild("email").equalTo(email.getText().toString());

            pesquisa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Toast.makeText(getApplicationContext(), snapshot.getKey(), Toast.LENGTH_LONG).show();
                        // -------- AGORA COM ELE SALVO, GEROU O PUSH, PEGAMOS E SALVAMOS NO SQLLITE --------
                        String key = snapshot.getKey();

                        final Usuario usuario = new Usuario();
                        usuario.setEmail(email.getText().toString());
                        usuario.setSenha(senha.getText().toString());
                        usuario.setId(key);

                        SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
                        Funcoes.tableDadosPessoaisSQLite(usuario,"SALVAR",bancoDados);
                        Funcoes.tableDadosPessoaisSQLite(usuario,"RECUPERAR",bancoDados);
                        // -------- AGORA COM ELE SALVO, GEROU O ID DO PUSH, PEGAMOS E SALVAMOS NO SQLLITE --------
                        // -------- ATUALIZAMOS O FIREBIRD COM O ID --------
                        Funcoes.pegarReferencia("CADASTROS",getApplicationContext()).child(key).setValue(usuario);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(usuarioAtual.getCurrentUser()!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Lembre-se você está logado", Toast.LENGTH_LONG).show();
            finish();
            Log.i("CurrentUser", "Usuario Logado!");
        } else {
            Toast.makeText(getApplicationContext(), "Você não esta logado", Toast.LENGTH_LONG).show();
            Log.i("CurrentUser", "Usuario Não Logado!");
        }
    }




    public void esqPassword (View view){
        Intent intent = new Intent(this, EsqueciSenhaActivity.class);
        startActivity(intent);
    }
}
