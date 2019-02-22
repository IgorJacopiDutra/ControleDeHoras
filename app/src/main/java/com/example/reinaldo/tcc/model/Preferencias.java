package com.example.reinaldo.tcc.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/**
 * Created by Reinaldo on 01/09/2018.
 */

public class Preferencias implements Serializable {

    public Long usuario;
    public String opçaoChecking;
    public String horasPrefUm;
    public String horasPrefDois;
    public String horasPrefTres;
    public String horasPrefQuatro;

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String checkin;

    public Preferencias(Long usuario, String opçaoChecking, String horasPrefUm, String horasPrefDois, String horasPrefTres, String horasPrefQuatro) {
        this.usuario = usuario;
        this.opçaoChecking = opçaoChecking;
        this.horasPrefUm = horasPrefUm;
        this.horasPrefDois = horasPrefDois;
        this.horasPrefTres = horasPrefTres;
        this.horasPrefQuatro = horasPrefQuatro;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public String getOpçaoChecking() {
        return opçaoChecking;
    }

    public void setOpçaoChecking(String opçaoChecking) {
        this.opçaoChecking = opçaoChecking;
    }

    public String getHorasPrefUm() {
        return horasPrefUm;
    }

    public void setHorasPrefUm(String horasPrefUm) {
        this.horasPrefUm = horasPrefUm;
    }

    public String getHorasPrefDois() {
        return horasPrefDois;
    }

    public void setHorasPrefDois(String horasPrefDois) {
        this.horasPrefDois = horasPrefDois;
    }

    public String getHorasPrefTres() {
        return horasPrefTres;
    }

    public void setHorasPrefTres(String horasPrefTres) {
        this.horasPrefTres = horasPrefTres;
    }

    public String getHorasPrefQuatro() {
        return horasPrefQuatro;
    }

    public void setHorasPrefQuatro(String horasPrefQuatro) {
        this.horasPrefQuatro = horasPrefQuatro;
    }

    public Preferencias() {}

    public static boolean InserirPreferenciasFirebasePasso3(String horasPrefUm, String horasPrefDois, String horasPrefTres, String horasPrefQuatro, Context contexto){

        Preferencias preferencias = new Preferencias();
        preferencias.setHorasPrefUm(horasPrefUm);
        preferencias.setHorasPrefDois(horasPrefDois);
        preferencias.setHorasPrefTres(horasPrefTres);
        preferencias.setHorasPrefQuatro(horasPrefQuatro);

        DatabaseReference preferenciaReferencia = Funcoes.pegarReferencia("PREFERENCIAS",contexto);//referencia.child("LOCAL");
        preferenciaReferencia.setValue( preferencias );
        return true;

    }

    public static boolean InserirPreferenciasFirebaseEditar(String checkin, String horasPrefUm, String horasPrefDois, String horasPrefTres, String horasPrefQuatro, Context contexto){

        Preferencias preferencias = new Preferencias();
        preferencias.setCheckin(checkin);
        preferencias.setHorasPrefUm(horasPrefUm);
        preferencias.setHorasPrefDois(horasPrefDois);
        preferencias.setHorasPrefTres(horasPrefTres);
        preferencias.setHorasPrefQuatro(horasPrefQuatro);

        DatabaseReference preferenciaReferencia = Funcoes.pegarReferencia("PREFERENCIAS",contexto);//referencia.child("LOCAL");
        preferenciaReferencia.setValue( preferencias );
        return true;

    }

}
