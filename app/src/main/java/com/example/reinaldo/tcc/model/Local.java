package com.example.reinaldo.tcc.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by IgorJacopiDutra on 10/03/2018.
 */

public class Local implements Serializable {

    private String identificador; // Cuidado não tirar esse identificados, é usado pra salver no SQLite a Key pra usar depois na estrutura dos dados
    private String nomeLocal;
    private String endereco;
    private String numero;
    private String bairro;
    private String estado;
    private String cidade;
    private String cep;
    private String CNPJ;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String pais) {
        this.cidade = pais;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public Local(String identificador, String nomeLocal, String endereco, String numero, String bairro, String estado, String cidade, String cep, String CNPJ) {
        this.identificador = identificador;
        this.nomeLocal = nomeLocal;
        this.endereco = endereco;
        this.numero = numero;
        this.bairro = bairro;
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.CNPJ = CNPJ;
    }

    public Local() {}

    public Local(String identificador, String nomeLocal) {
        this.identificador = identificador;
        this.nomeLocal = nomeLocal;
    }

    public static boolean InserirLocalFirebase(String sequencia,String nomeLocal,String CEP,String endereco,String numero_endereco,String bairro_endereco,String local_cnpj,String estado, String cidade, Context contexto){

        Local adicionarLocal = new Local();
        adicionarLocal.setNomeLocal(nomeLocal);
        adicionarLocal.setCep(CEP);
        adicionarLocal.setEndereco(endereco);
        adicionarLocal.setNumero(numero_endereco);
        adicionarLocal.setBairro(bairro_endereco);
        adicionarLocal.setCNPJ(local_cnpj);
        adicionarLocal.setCidade(cidade);
        adicionarLocal.setEstado(estado);
        adicionarLocal.setId(Long.parseLong(sequencia));

        DatabaseReference localReferencia = Funcoes.pegarReferencia("LOCAL",contexto).child(sequencia);//referencia.child("LOCAL");
        localReferencia.setValue( adicionarLocal );
        return true;


    }



}