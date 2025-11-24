package com.jpribeiro.model;

public class Usuario {
    private final int id;
    private final String apelido, email;

    public Usuario(int id, String apelido, String email) {
        this.id = id;
        this.apelido = apelido;
        this.email = email;
    }

    public Usuario(String apelido, String email) {
        this.id = 0;
        this.apelido = apelido;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return apelido;
    }

    public String getEmail() {
        return email;
    }
}