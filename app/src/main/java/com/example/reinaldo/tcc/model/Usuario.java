package com.example.reinaldo.tcc.model;

/**
 * Created by IgorJacopiDutra on 13/02/2018.
 */

public class Usuario {

    private String nomeCompleto;
    private String email;
    private String senha;
    private String sexo;
    private int idade;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Usuario(String nomeCompleto, String email, String senha, String sexo, int idade, String id) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.sexo = sexo;
        this.idade = idade;
        this.id = id;
    }

}
