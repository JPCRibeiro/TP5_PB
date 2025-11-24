package com.jpribeiro.service;

import com.jpribeiro.DAO.JogoDAO;
import com.jpribeiro.model.Jogo;

import java.sql.SQLException;
import java.util.List;

public class JogoService {
    private final JogoDAO jogoDAO;

    public JogoService() {
        this.jogoDAO = new JogoDAO();
    }

    public JogoService(JogoDAO jogoDAO) {
        this.jogoDAO = jogoDAO;
    }

    public void addJogo(String nome, double preco, String descricao) throws SQLException {
        jogoDAO.add(new Jogo(nome, preco, descricao));
    }

    public Jogo findJogoById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Jogo jogo = jogoDAO.findById(id);

        if (jogo == null) {
            throw new IllegalArgumentException("Jogo não encontrado");
        }

        return jogo;
    }

    public List<Jogo> findAllJogos() throws SQLException {
        return jogoDAO.findAll();
    }

    public void updateJogo(int id, String nome, double preco, String descricao) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Jogo existente = jogoDAO.findById(id);

        if (existente == null) {
            throw new IllegalArgumentException("Jogo não encontrado para atualização");
        }

        jogoDAO.update(new Jogo(id, nome, preco, descricao));
    }

    public void deleteJogo(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Jogo existente = jogoDAO.findById(id);

        if (existente == null) {
            throw new IllegalArgumentException("Jogo não encontrado para exclusão");
        }

        jogoDAO.delete(id);
    }
}