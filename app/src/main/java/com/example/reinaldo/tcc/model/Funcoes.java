package com.example.reinaldo.tcc.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by IgorJacopiDutra on 04/08/2018.
 */

public class Funcoes {


    // COMO O SISTEMA QUE CONTEM O MENU EXISTEM VARIAS TELAS E BOTÕES, VAMOS DEIXAR SÓ UM.
    public static void hideFloatingActionButton(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(false);
        }

        fab.hide();
    }

    // COMO O SISTEMA QUE CONTEM O MENU EXISTEM VARIAS TELAS E BOTÕES, VAMOS DEIXAR SÓ UM.
    public static void showFloatingActionButton(FloatingActionButton fab) {
        fab.show();
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(true);
        }
    }

    public static void deslogarDoSistema(SQLiteDatabase bancoDados){
        FirebaseAuth usuarioAtual = FirebaseAuth.getInstance();
        apagarBD(bancoDados);
        usuarioAtual.signOut();
    }

    // USAR A FUNÇÃO PARA PADRONIZAR O SISTEMA, QUANDO FORMOS REFORMULAR A ESTRUTURA DO BD EM JSON, MEXEMOS SÓ NESSA FUNÇÃO
    public static DatabaseReference pegarReferencia(String Referencia, Context contexto){
        DatabaseReference referenciaRaiz = FirebaseDatabase.getInstance().getReference();
        DatabaseReference padraoReferencia = referenciaRaiz.child("CADASTROS");
        DatabaseReference escolhido = padraoReferencia = referenciaRaiz.child("CADASTROS");

        if (Referencia == "USUARIO") {
            DatabaseReference usuariosReferencia = referenciaRaiz.child("CADASTROS");
            escolhido = usuariosReferencia;
        }

        if (Referencia == "GPS") {
            DatabaseReference gpsReferencia = referenciaRaiz.child("GPS").child(Funcoes.pegarKey(contexto));
            escolhido = gpsReferencia;
        }

        if (Referencia == "WIFI") {
            DatabaseReference wifiReferencia = referenciaRaiz.child("WIFI").child(Funcoes.pegarKey(contexto));
            escolhido = wifiReferencia;
        }

        if (Referencia == "LOCAL") {
            DatabaseReference localReferencia = referenciaRaiz.child("LOCAL").child(Funcoes.pegarKey(contexto));
            escolhido = localReferencia;
        }

        if (Referencia == "ESTADOS") {
            DatabaseReference estadosReferencia = referenciaRaiz.child("ESTADOS");
            escolhido = estadosReferencia;
        }
        if (Referencia == "HORAS_TRABALHADAS") {
            DatabaseReference horaReferencia = referenciaRaiz.child("HORAS_TRABALHADAS").child(Funcoes.pegarKey(contexto));
            escolhido = horaReferencia;
        }

        if (Referencia == "PREFERENCIAS") {
            DatabaseReference preferenciasReferencia = referenciaRaiz.child("PREFERENCIAS").child(Funcoes.pegarKey(contexto));
            escolhido = preferenciasReferencia;
        }

        if (Referencia == "IMAGENS_PERFIL") {
            DatabaseReference imagemPerfil = referenciaRaiz.child("IMAGENS_PERFIL").child(Funcoes.pegarKey(contexto));
            escolhido = imagemPerfil;
        }

        return escolhido;
    }

/* TENTATIVA SEM SUCESSO DE CRIAR UMA FUNÇÃO QUE BUSACA A KEY do usuario passando o seu e-mail que pagamos no LOGIN/CADASTRO
    ESTA KEY TEM POR OBJETIVO SER USADA NA HR DE GRAVAR O WIFI/GPS/LOCAL


    public static String pegarKeyDoUsuario(String email, Context context){

        DatabaseReference usuariosteste = pegarReferencia("CADASTROS");
        Query pesquisa = usuariosteste.orderByChild("email").equalTo("igorja@gmail.com");
        // DatabaseReference pesquisa = usuariosteste.child("-LKC4IwOob1LEBALTlcS");

        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                //Toast.makeText(getApplicationContext(), dadosUsuario.getNomeCompleto(), Toast.LENGTH_LONG).show();

                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    IdentificadorUnico = snapshot.getKey();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return IdentificadorUnico;
    }
*/

    // USAR PARA FACILITAR ESSA MANIPULAÇÃO NO SQL LITE
    public static void tableDadosPessoaisSQLite(Usuario usuarioParam, String acao, SQLiteDatabase bancoDados) {
        Usuario usuario = usuarioParam;

        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS DadosPessoais (nomeCompleto VARCHAR, email VARCHAR, senha String, sexo String ,idade INT(3), id String)");

        if (acao == "SALVAR") {
            try {
                bancoDados.execSQL("INSERT INTO DadosPessoais (nomeCompleto, email, senha, sexo, idade, id) VALUES ('" + usuario.getNomeCompleto() + "','" + usuario.getEmail() + "','" + usuario.getSenha() + "','" + usuario.getSexo() + "'," + usuario.getIdade() + ",'"+ usuario.getId() + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (acao == "ATUALIZAR") {
            try {
                bancoDados.execSQL("UPDATE dadosPESSOAIS set nome =" + usuario.getNomeCompleto() + ", senha =" + usuario.getSenha() + ", sexo = " + usuario.getSexo() + ", idade = " + usuario.getIdade() + ", id =" + usuario.getId() + " where email =" + usuario.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (acao == "RECUPERAR") {
            try {
                Cursor cursor = bancoDados.rawQuery("SELECT nomeCompleto, email, id FROM DadosPessoais", null);
                int indiceNome = cursor.getColumnIndex("nomeCompleto");
                int indiceEmail = cursor.getColumnIndex("email");
                int indiceId = cursor.getColumnIndex("id");
                cursor.moveToFirst();
                while (cursor != null) {
                    Log.i("Resultado", cursor.getString(indiceNome));
                    Log.i("Resultado - email: ", cursor.getString(indiceEmail));
                    Log.i("Resultado - id: ", cursor.getString(indiceId));
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (acao == "DELETAR") {
            try {
                bancoDados.execSQL("DELETE FROM DadosPessoais WHERE email ="+usuario.getEmail());
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    // APAGAR BD QUANDO INICA O PROGRAMA COMO PRIMEIRA INSTALAÇÃO, OU CLICA EM SAIR, NÃO SABEMOS SER FICA RESIDUO
    public static void apagarBD(SQLiteDatabase bancoDados){
        try {
            bancoDados.execSQL("DELETE FROM DadosPessoais");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static String pegarKey(Context contexto){
        SQLiteDatabase bancoDados = contexto.openOrCreateDatabase("app", MODE_PRIVATE, null);
        String key = "erro@gmail.com";
        try{
            Cursor cursor = bancoDados.rawQuery("SELECT nomeCompleto, email, id FROM DadosPessoais", null);
            int indiceNome = cursor.getColumnIndex("nomeCompleto");
            int indiceEmail = cursor.getColumnIndex("email");
            int indiceId = cursor.getColumnIndex("id");
            cursor.moveToFirst();
            while (cursor != null) {
                Log.i("Resultado", cursor.getString(indiceNome));
                Log.i("Resultado - email: ", cursor.getString(indiceEmail));
                Log.i("Resultado - id: ", cursor.getString(indiceId));
                key = cursor.getString(indiceId);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static long pegarSequenciaLocal(final Context contexto){
        long numChildren = 0;
        DatabaseReference wifiTeste = pegarReferencia("LOCAL",contexto);
        Query pesquisa = wifiTeste.orderByChild("nomeLocal");

        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                long numChildren = dataSnapshot.getChildrenCount();
                //Toast.makeText(getApplicationContext(), dadosUsuario.getNomeCompleto(), Toast.LENGTH_LONG).show();
                //Toast.makeText(contexto, Long.toString(numChildren), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return numChildren;
    }

    public static long pegarSequenciaGPS(final Context contexto){
        long numChildren = 0;
        DatabaseReference wifiTeste = pegarReferencia("GPS",contexto);
        Query pesquisa = wifiTeste.orderByChild("nomeGPS");

        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                long numChildren = dataSnapshot.getChildrenCount();
                //Toast.makeText(getApplicationContext(), dadosUsuario.getNomeCompleto(), Toast.LENGTH_LONG).show();
                //Toast.makeText(contexto, Long.toString(numChildren), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return numChildren;
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info==null || !info.isConnected())
            return "-"; //sem conexão
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : troque por 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : troque por 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : troque por 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : troque por 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : troque por 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    /* Função para verificar existência de conexão com a internet
	 */
    public static boolean verificaConexao(Context contexto) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }


}