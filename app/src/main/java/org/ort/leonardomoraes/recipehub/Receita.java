package org.ort.leonardomoraes.recipehub;

/**
    Classe receitatatata
 */

class Receita {

    public Receita(){}

    public Receita(String nome, String ingrediente, String tempo, String sabor, String tipo, String preparo, String urlFoto, String idDono, String idPai, String idProprio) {
        this.nome = nome;
        this.ingrediente = ingrediente;
        this.tempo = tempo;
        this.sabor = sabor;
        this.tipo = tipo;
        this.preparo = preparo;
        this.urlFoto = urlFoto;
        this.idDono = idDono;
        this.idPai = idPai;
        this.idProprio = idProprio;
        //this.nomeUsuario = nomeUsuario;
    }

    private String nome;
    private String ingrediente;
    private String tempo;
    private String sabor;
    private String tipo;
    private String preparo;
    private String urlFoto;
    private String idDono;
    private String idPai;
    private String idProprio;

    /*public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }*/

    private String nomeUsuario;

    public String getIdProprio() {
        return idProprio;
    }

    public void setIdProprio(String idProprio) {
        this.idProprio = idProprio;
    }

    public String getIdPai() {
        return idPai;
    }

    public void setIdPai(String idPai) {
        this.idPai = idPai;
    }


    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPreparo() {
        return preparo;
    }

    public void setPreparo(String preparo) {
        this.preparo = preparo;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
