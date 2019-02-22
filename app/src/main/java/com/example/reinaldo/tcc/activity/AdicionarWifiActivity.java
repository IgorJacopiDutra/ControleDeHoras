package com.example.reinaldo.tcc.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reinaldo.tcc.R;
import com.example.reinaldo.tcc.model.Funcoes;
import com.example.reinaldo.tcc.model.Local;
import com.example.reinaldo.tcc.model.Wifi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.reinaldo.tcc.model.Wifi.InserirWIFIFirebase;
import static com.example.reinaldo.tcc.model.Wifi.VerificaConexao;

public class AdicionarWifiActivity extends AppCompatActivity {

    private Button btnPegarWifi;
    private static String  resultadoString;
    private static String resString;
    private boolean resultado;
    private TextView tvResultado, tvDescricao;
    private Context contexto;
    private TextInputEditText edAddWifi;
    private Spinner spn_locais;
    private Wifi wifiAtual;
    long numChildren;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_wifi);

        spn_locais = findViewById(R.id.spn_locais);
        edAddWifi = findViewById(R.id.edAddWifi);
        btnPegarWifi = findViewById(R.id.btnPegarWifi);
        tvResultado = findViewById(R.id.tvResultado);
        tvDescricao = findViewById(R.id.tvDescricao);

        numChildren = 0;
        // Recuperar Wifi
        wifiAtual = (Wifi) getIntent().getSerializableExtra("wifiSelecionado");
        if ( wifiAtual != null ){
            edAddWifi.setText(wifiAtual.getNomeWifi());
            numChildren = wifiAtual.getId();
        } else {
            DatabaseReference wifiTeste = Funcoes.pegarReferencia("WIFI",getApplicationContext());
            Query pesquisa = wifiTeste.orderByChild("id").limitToFirst(1);

            pesquisa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                        Wifi dadosUsuario = objSnapshot.getValue(Wifi.class);
                        //Toast.makeText(getApplicationContext(), String.valueOf(dadosUsuario.getId()) , Toast.LENGTH_LONG).show();
                        numChildren = dadosUsuario.getId() + 1;
                    }

                    //numChildren = Long.parseLong(dadosUsuario.getId().toString() ) + 1;
                    //Toast.makeText(getApplicationContext(), dadosUsuario.getNomeCompleto(), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        // Fazer funcionar o Spinner de uma array-prestabelecida
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                //R.array.locais_array, android.R.layout.simple_spinner_item);
        // Specify the activity_login to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //spn_locais.setAdapter(adapter);
        //tvResultado.setText(BuscarWIFIFirebase());

        btnPegarWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultado = VerificaConexao(getApplicationContext());
                contexto = getApplicationContext();
                ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE); //Pego a conectividade do contexto o qual o metodo foi chamado
                NetworkInfo netInfo = cm.getActiveNetworkInfo(); //Crio o objeto netInfo que recebe as informacoes da NEtwork
                if(resultado){ // Caso precise ver o TRUE ou FALSE do resultado acima String.valueOf(resultado)
                    resultadoString = netInfo.getExtraInfo();
                    tvResultado.setText( "Buscarmos com sucesso informações do WIF de nome: " + resultadoString);
                    resString = resultadoString.replaceAll("\"", "");
                    //System.out.println(resultadoString);
                    edAddWifi.setText(resString);
                } else {
                    tvResultado.setText("Sem conexão com WIFI");
                }
            }
        });

        Funcoes.pegarReferencia("LOCAL", getApplicationContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> locais = new ArrayList<String>();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Local local =  objSnapshot.getValue(Local.class);
                    locais.add(local.getNomeLocal());
                    spn_locais = findViewById(R.id.spn_locais);
                    ArrayAdapter<String> locaisAdapter = new ArrayAdapter<String>(AdicionarWifiActivity.this, android.R.layout.simple_spinner_item, locais);
                    locaisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_locais.setAdapter(locaisAdapter);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_gps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ){
            case R.id.itemSalvar :
             if ( (spn_locais.getSelectedItem() != null) && (edAddWifi.getText().length() != 0) ){
                 if (InserirWIFIFirebase(edAddWifi.getText().toString(),spn_locais.getSelectedItem().toString(),getApplicationContext(),Long.toString(numChildren))){
                     Toast.makeText(AdicionarWifiActivity.this,"Salvo Com Sucesso!", Toast.LENGTH_LONG).show();
                     startActivity(new Intent(AdicionarWifiActivity.this, MainActivity.class));
                 } else {
                     Toast.makeText(AdicionarWifiActivity.this,"Erro Ao Salvar o Wifi!", Toast.LENGTH_LONG).show();
                 }
                 finish();
                 break;
             } else{
                 Toast.makeText(AdicionarWifiActivity.this,"Por Favor, Preencha Todos os Campos.", Toast.LENGTH_LONG).show();
             }

        }
        return super.onOptionsItemSelected(item);
    }
}
