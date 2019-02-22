package com.example.reinaldo.tcc.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/**
 * Created by IgorJacopiDutra on 11/03/2018.
 */

public class GPS implements Serializable {

    private Long id;
    private String nomeGPS;
    private String latitude;
    private String longitude;
    private String nomeGPSLocal;
    private String cidadeGPS;
    private String estadoGPS;
    private String paisGPS;
    private String bairroGPS;
    private String cepGPS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCepGPS() {
        return cepGPS;
    }

    public void setCepGPS(String cepGPS) {
        this.cepGPS = cepGPS;
    }

    public String getBairroGPS() {
        return bairroGPS;
    }

    public void setBairroGPS(String bairroGPS) {
        this.bairroGPS = bairroGPS;
    }

    public String getCidadeGPS() {
        return cidadeGPS;
    }

    public void setCidadeGPS(String cidadeGPS) {
        this.cidadeGPS = cidadeGPS;
    }

    public String getEstadoGPS() {
        return estadoGPS;
    }

    public void setEstadoGPS(String estadoGPS) {
        this.estadoGPS = estadoGPS;
    }

    public String getPaisGPS() {
        return paisGPS;
    }

    public void setPaisGPS(String paisGPS) {
        this.paisGPS = paisGPS;
    }

    public String getNomeGPSLocal() {
        return nomeGPSLocal;
    }

    public void setNomeGPSLocal(String nomeGPSLocal) {
        this.nomeGPSLocal = nomeGPSLocal;
    }

    public String getNomeGPS() {
        return nomeGPS;
    }

    public void setNomeGPS(String nomeLocal) {
        this.nomeGPS = nomeLocal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static boolean InserirGPSFirebase(String sequencia,String nomeGPS,String latitude,String longitude,String nomeGPSLocal,String cidadeGPS,String estadoGPS,String paisGPS,String bairroGPS, String cepGPS, Context contexto){

        GPS adicionarGPS = new GPS();
        adicionarGPS.setId(Long.parseLong(sequencia));
        adicionarGPS.setNomeGPS(nomeGPS);
        adicionarGPS.setLatitude(latitude);
        adicionarGPS.setLongitude(longitude);
        adicionarGPS.setNomeGPSLocal(nomeGPSLocal);
        adicionarGPS.setCidadeGPS(cidadeGPS);
        adicionarGPS.setEstadoGPS(estadoGPS);
        adicionarGPS.setPaisGPS(paisGPS);
        adicionarGPS.setBairroGPS(bairroGPS);
        adicionarGPS.setCepGPS(cepGPS);
        DatabaseReference gpsReferencia = Funcoes.pegarReferencia("GPS",contexto).child(sequencia);
        gpsReferencia.setValue(adicionarGPS);

        return true;
    }
}
