package com.jpribeiro.DAO;

import com.jpribeiro.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public int add(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Falha ao inserir usuário");
    }

    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
            }
        }
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                ));
            }
        }
        return usuarios;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}