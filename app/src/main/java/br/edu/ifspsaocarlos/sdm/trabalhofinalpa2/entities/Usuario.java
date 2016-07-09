package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Classe que representa o usuário do sistema.
 *
 * @author Anderson Canale Garcia
 * @author Lucas Petto
 */
public class Usuario implements Serializable{

    private String nome;

    private String apelido;

    private Integer id;

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getApelido() {

        return apelido;
    }

    public void setApelido(String apelido) {

        this.apelido = apelido;
    }
}

