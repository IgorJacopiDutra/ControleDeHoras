package com.example.reinaldo.tcc.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Reinaldo on 18/08/2018.
 */

public class Horas {
    private Long usuario;
    private String data;
    private String horaUm;
    private String horaDois;
    private String horaTres;
    private String horaQuatro;
    private Date horasTrabalhadas;

    public Horas() {
        this.usuario = usuario;
        this.data = data;
        this.horaUm = horaUm;
        this.horaDois = horaDois;
        this.horaTres = horaTres;

        this.horaQuatro = horaQuatro;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoraUm() {
        return horaUm;
    }

    public void setHoraUm(String horaUm) {
        this.horaUm = horaUm;
    }

    public String getHoraDois() {
        return horaDois;
    }

    public void setHoraDois(String horaDois) {
        this.horaDois = horaDois;
    }

    public String getHoraTres() {
        return horaTres;
    }

    public void setHoraTres(String horaTres) {
        this.horaTres = horaTres;
    }

    public String getHoraQuatro() {
        return horaQuatro;
    }

    public void setHoraQuatro(String horaQuatro) {
        this.horaQuatro = horaQuatro;
    }

    public String subtraiHoras(String horaUm, String horaDois, String horaTres, String horaQuatro){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date dataHoraUm = null;
        Date dataHoraDois = null;
        Date dataHoraTres = null;
        Date dataHoraQuatro = null;
        try {
            dataHoraUm = sdf.parse(horaUm);
            dataHoraDois = sdf.parse(horaDois);
            dataHoraTres = sdf.parse(horaTres);
            dataHoraQuatro = sdf.parse(horaQuatro);
        } catch (Exception e) {

        }
        Date horasTrabalhadas = new Date((dataHoraDois.getTime() - dataHoraUm.getTime()) + (dataHoraQuatro.getTime() - dataHoraTres.getTime()));
        return sdf.format(horasTrabalhadas);
    }

    public static boolean marcarHorasFirebase (String data,
                                                String horaUm,
                                                String horaDois,
                                                String horaTres,
                                                String horaQuatro,
                                                Context contexto){
        Horas marcarHoras = new Horas();
        marcarHoras.setData(data);
        marcarHoras.setHoraUm(horaUm);
        marcarHoras.setHoraDois(horaDois);
        marcarHoras.setHoraTres(horaTres);
        marcarHoras.setHoraQuatro(horaQuatro);

        DatabaseReference horaReferencia = Funcoes.pegarReferencia("HORAS_TRABALHADAS",contexto).child(data);
        horaReferencia.setValue( marcarHoras );
        return true;
    }

}
