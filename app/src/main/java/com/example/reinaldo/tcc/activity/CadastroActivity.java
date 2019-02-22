package com.example.reinaldo.tcc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText edtNomeCompletoCadastro;
    private EditText edtEmailCadastro, edtSenhaCadastro, edtConfirmarSenhaCadastro, edtIdadeCadastro;
    private Spinner spSexoCadastro;
    private boolean validador, bFezLogin;
    private Button btnCadastrarCadastro;
    private String mensagemErro;
    private Integer idadeTemp;
    private FirebaseAuth usuarioAtual;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        edtNomeCompletoCadastro = findViewById(R.id.edtNomeCompletoCadastro);
        edtEmailCadastro = findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = findViewById(R.id.edtSenhaCadastro);
        edtConfirmarSenhaCadastro = findViewById(R.id.edtConfirmarSenhaCadastro);
        spSexoCadastro = findViewById(R.id.spSexoCadastro);
        edtIdadeCadastro = findViewById(R.id.edtIdadeCadastro);
        btnCadastrarCadastro = findViewById(R.id.btnCadastrarCadastro);
        validador = true;
        bFezLogin = false;
        mensagemErro = "";
        usuarioAtual = FirebaseAuth.getInstance();
//

        String[] itemsSpSexoCadastro = new String[]{"Sexo", "Feminino", "Masculino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpSexoCadastro);
        spSexoCadastro.setAdapter(adapter);

        btnCadastrarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Usuario usuario = new Usuario();
                usuario.setNomeCompleto(edtNomeCompletoCadastro.getText().toString());
                usuario.setEmail(edtEmailCadastro.getText().toString());
                usuario.setSenha(edtSenhaCadastro.getText().toString());
                idadeTemp = Integer.parseInt(edtIdadeCadastro.getText().toString());
                if (idadeTemp == null) {
                    idadeTemp = 0;
                } else {
                    idadeTemp = Integer.parseInt(edtIdadeCadastro.getText().toString());
                }
                usuario.setIdade(idadeTemp);
                usuario.setSexo(spSexoCadastro.getSelectedItem().toString());
                // Validador do SEXO
                switch (spSexoCadastro.getSelectedItem().toString()) {
                    case "Feminino":
                        break;
                    case "Masculino":
                        break;
                    case "Sexo":
                        validador = false;
                        mensagemErro = mensagemErro + " [ Preecha o sexo ] ";
                        break;
                }

                // Validador de nome
                if (edtNomeCompletoCadastro.getText().length() == 0) {
                    validador = false;
                    mensagemErro = mensagemErro + " [ Preecha o nome ] ";
                }

                // Validador de email
                if (edtEmailCadastro.getText().length() == 0) {
                    validador = false;
                    mensagemErro = mensagemErro + " [ Preecha o email ] ";
                }

                // Validador senha
                if (edtSenhaCadastro.getText().length() == 0) {
                    validador = false;
                    mensagemErro = mensagemErro + " [ Preecha a senha ] ";
                } else if (edtConfirmarSenhaCadastro.getText().length() == 0) {
                    validador = false;
                    mensagemErro = mensagemErro + " [ Preecha a senha de Confirmação ] ";
                } else {

                }

                if (validador == true) {
                    usuarioAtual.createUserWithEmailAndPassword(edtEmailCadastro.getText().toString(), edtSenhaCadastro.getText().toString()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("CreateUser", "Sucesso ao cadastrar o usuario");
                                usuarioAtual.signInWithEmailAndPassword(edtEmailCadastro.getText().toString(), edtSenhaCadastro.getText().toString()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            bFezLogin = true;
                                            Log.i("SignIn", "Sucesso ao Logar");
                                        } else {
                                            Log.i("SignIn", "Erro ao Logar");
                                            bFezLogin = false;
                                        }
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Cadastro Efetuado", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ImageProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.i("CreateUser", "Erro ao cadastrar usuario");
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), mensagemErro, Toast.LENGTH_LONG).show();
                    mensagemErro = "";
                }
                if (bFezLogin = true) {
                    // -------- VAMOS SALVAR TAMBÉM NO DATABASE MESMO JA CADASTRANDO O EMAIL E SENHA NO AUTHENTIC --------
                    Funcoes.pegarReferencia("CADASTROS",getApplicationContext()).push().setValue(usuario); //usuariosReferencia.setValue( usuario );
                    // -------- VAMOS SALVAR TAMBÉM NO DATABASE MESMO JA CADASTRANDO O EMAIL E SENHA NO AUTHENTIC --------
                    // -------- VAMOS DESCOBRIR QUAL É O IDENTIFICADOR GERADO DO PUSH PARA PODER USAR NO FUTURO --------
                    DatabaseReference usuariosteste = referencia.child("CADASTROS");
                    Query pesquisa = usuariosteste.orderByChild("email").equalTo(usuario.getEmail());
                    // DatabaseReference pesquisa = usuariosteste.child("-LKC4IwOob1LEBALTlcS");
                    pesquisa.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                            //Toast.makeText(getApplicationContext(), dadosUsuario.getNomeCompleto(), Toast.LENGTH_LONG).show();
                            for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                                //Toast.makeText(getApplicationContext(), snapshot.getKey(), Toast.LENGTH_LONG).show();
                                // -------- AGORA COM ELE SALVO, GEROU O PUSH, PEGAMOS E SALVAMOS NO SQLLITE --------
                                String key = snapshot.getKey();
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
                    // -------- VAMOS DESCOBRIR QUAL É O IDENTIFICADOR GERADO DO PUSH PARA PODER USAR NO FUTURO --------

                }

            }

        });
        //writeNewUser("1", "Igor Jacopi Dutra", "igorjacopidutra@gmail.com","501203357","M",18 );


    }


    @Override
    protected void onStart() {
        super.onStart();


    }

//    private void writeNewUser(String userId, String nomeCompleto, String email, String senha, String sexo, int idade) {
//        Usuario usuario = new Usuario(nomeCompleto, email, senha, sexo, idade );

    //  mDatabase.child("Usuarios").child(userId).setValue(usuario);
//    }
}