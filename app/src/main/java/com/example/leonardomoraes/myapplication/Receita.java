package com.example.leonardomoraes.myapplication;

/**
    Classe receita
 */

class Receita {

    public Receita(){}

    public Receita(String nome, String ingrediente, String tempo, String sabor, String tipo, String preparo) {
        this.nome = nome;
        this.ingrediente = ingrediente;
        this.tempo = tempo;
        this.sabor = sabor;
        this.tipo = tipo;
        this.preparo = preparo;
    }
    private String nome;
    private String ingrediente;
    private String tempo;
    private String sabor;
    private String tipo;
    private String preparo;


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

}
