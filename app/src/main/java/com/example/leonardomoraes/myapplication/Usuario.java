package com.example.leonardomoraes.myapplication;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by aiko.oliveira and gabriela.barbosa on 02/09/2017.
 */

public class Usuario {
    private String nomeReceita;

    public Usuario(String nomeReceita) {
        this.nomeReceita = nomeReceita;
    }

    public String getNomeReceita() {
        return nomeReceita;
    }

    public void setNomeReceita(String nomeReceita) {
        this.nomeReceita = nomeReceita;
    }
}
