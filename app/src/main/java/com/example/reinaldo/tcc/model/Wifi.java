package com.example.reinaldo.tcc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.reinaldo.tcc.activity.AdicionarWifiActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Reinaldo on 10/03/2018.
 */

public class Wifi implements Serializable {
    private Long id;
    private String nomeWifi;
    private String nomeWifiLocal;
//    public Wifi(Long id, String nomeWifi, String nomeWifiLocal) {
//        this.id = id;
//        this.nomeWifi = nomeWifi;
//        this.nomeWifiLocal = nomeWifiLocal;
//    }

    public String getNomeWifiLocal() {
        return nomeWifiLocal;
    }

    public void setNomeWifiLocal(String nomeWifiLocal) {
        this.nomeWifiLocal = nomeWifiLocal;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNomeWifi() {return nomeWifi;}

    public void setNomeWifi(String nomeWifi) {this.nomeWifi = nomeWifi;}

    public static boolean VerificaConexao(Context contexto){
        //Pego a conectividade do contexto o qual o metodo foi chamado
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Crio o objeto netInfo que recebe as informacoes da NEtwork
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //Se o objeto for nulo ou nao tem conectividade retorna false
        if ( (netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()) )
            return true;
        else
            return false;
    }

    public static boolean InserirWIFIFirebase(String nomeWifi, String nomeWifiLocal, Context contexto, String sequencia) {

        Wifi adicionarWifi = new Wifi();
        adicionarWifi.setNomeWifi(nomeWifi);
        adicionarWifi.setNomeWifiLocal(nomeWifiLocal);
        adicionarWifi.setId(Long.parseLong(sequencia));
        DatabaseReference wifiReferencia = Funcoes.pegarReferencia("WIFI",contexto).child(sequencia);
        wifiReferencia.setValue(adicionarWifi);

        // Pensar em alguma condição aqui Andrey
        //  if (wifiReferencia.push().setValue(adicionarWifi) != null)
        return true;
        //  else
        //      return false;

    }

    /*  public static ArrayList<Wifi> BuscarWIFIFirebase(){
        final ArrayList<Wifi> listaBuscaWifi = new ArrayList<>();
        listaBuscaWifi.add(new Wifi());
        final Integer numerador = 0;

        DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
        DatabaseReference wifiReferencia = referencia.child("WIFI");


        wifiReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaBuscaWifi.get(0).nomeWifi = dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listaBuscaWifi;
    }
*/


}
