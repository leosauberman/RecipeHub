package org.ort.leonardomoraes.recipehub;

/**
 * Created by aiko2 on 29/09/2017.
 */

public class User {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private String nomeReceita;

    public User() {
    }

    public User(String id, String name, String email, String imageUrl, String nomeReceita) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.nomeReceita = nomeReceita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNomeReceita() {
        return nomeReceita;
    }

    public void setNomeReceita(String nomeReceita) {
        this.nomeReceita = nomeReceita;
    }
}
