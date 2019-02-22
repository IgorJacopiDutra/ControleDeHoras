package com.example.reinaldo.tcc.model;

/**
 * Created by Reinaldo on 07/07/2018.
 */

public class Cidades {

    private String id;
    private String estado;
    private String nome;

    public Cidades(String id, String estado, String nome) {
        this.id = id;
        this.estado = estado;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
