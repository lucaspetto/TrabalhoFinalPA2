package br.edu.ifspsaocarlos.sdm.trabalhofinalpa2.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Mensagem implements Serializable, Comparable {
    private int id;
    private int origemId;
    private int destinoId;
    private String assunto;
    private String corpo;

    private Usuario origem;
    private Usuario destino;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigemId() {
        return origemId;
    }

    public void setOrigemId(int origemId) {
        this.origemId = origemId;
    }

    public int getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(int destinoId) {
        this.destinoId = destinoId;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Usuario getOrigem() {
        return origem;
    }

    public void setOrigem(Usuario origem) {
        this.origem = origem;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    @Override
    public int compareTo(Object another) {
        Mensagem mensagem = (Mensagem) another;
        if(this.id < mensagem.getId())
            return -1;
        if(this.id > mensagem.getId())
            return 1;

        return 0;
    }

    public static Mensagem parseMensagemFromJSON(JSONObject jsonMensagem) throws JSONException {
        JSONObject jsonOrigem = (JSONObject) jsonMensagem.get("origem");

        Gson gson = new Gson();
        Mensagem mensagem = gson.fromJson(jsonMensagem.toString(), Mensagem.class);
        mensagem.getOrigem().setNome(jsonOrigem.get("nome_completo").toString());

        return mensagem;
    }
}
