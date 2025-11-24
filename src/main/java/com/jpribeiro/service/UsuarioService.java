package com.jpribeiro.service;

import com.jpribeiro.DAO.BibliotecaDAO;
import com.jpribeiro.DAO.UsuarioDAO;
import com.jpribeiro.model.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO;
    private final BibliotecaDAO bibliotecaDAO;

    public UsuarioService(UsuarioDAO usuarioDAO, BibliotecaDAO bibliotecaDAO) {
        this.usuarioDAO = usuarioDAO;
        this.bibliotecaDAO = bibliotecaDAO;
    }

    public UsuarioService() {
        this(new UsuarioDAO(), new BibliotecaDAO());
    }

    public void addUsuario(String nome, String email, List<String> jogosIds) throws SQLException {
        Usuario usuario = new Usuario(nome, email);
        int usuarioId = usuarioDAO.add(usuario);

        List<Integer> jogosValidos = new ArrayList<>();

        if (jogosIds != null) {
            for (String idStr : jogosIds) {
                try {
                    jogosValidos.add(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido ignorado: " + idStr);
                }
            }
        }

        if (!jogosValidos.isEmpty()) {
            bibliotecaDAO.addJogosAoUsuario(usuarioId, jogosValidos);
        }
    }

    public Usuario findUsuarioById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Usuario usuario = usuarioDAO.findById(id);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        return usuario;
    }

    public List<Usuario> findAllUsuarios() throws SQLException {
        return usuarioDAO.findAll();
    }

    public void updateUsuario(int id, String nome, String email) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Usuario existente = usuarioDAO.findById(id);

        if (existente == null) {
            throw new IllegalArgumentException("Usuário não encontrado para atualização");
        }

        usuarioDAO.update(new Usuario(id, nome, email));
    }

    public void deleteUsuario(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        Usuario existente = usuarioDAO.findById(id);

        if (existente == null) {
            throw new IllegalArgumentException("Jogo não encontrado para exclusão");
        }

        usuarioDAO.delete(id);
    }
}