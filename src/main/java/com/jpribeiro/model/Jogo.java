package com.jpribeiro.model;

public class Jogo {
    private final int id;
    private final String jogo, descricao;
    private final double preco;

    public Jogo(int id, String jogo, double preco, String descricao) {
        this.id = id;
        this.jogo = jogo;
        this.preco = preco;
        this.descricao = descricao;
    }

    public Jogo(String jogo, double preco, String descricao) {
        this.id = 0;
        this.jogo = jogo;
        this.preco = preco;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getJogo() {
        return jogo;
    }

    public double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }
}