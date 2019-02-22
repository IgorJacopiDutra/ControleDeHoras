package com.example.reinaldo.tcc.model;

/**
 * Created by Reinaldo on 07/07/2018.
 */

public class Estados {

    private String id;
    private String nome;
    private String sigla;

    public Estados(String id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
