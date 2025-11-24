package com.jpribeiro.DAO;

import com.jpribeiro.model.Jogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoDAO {
    public void add(Jogo jogo) throws SQLException {
        String sql = "INSERT INTO jogos (jogo, preco, descricao) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jogo.getJogo());
            stmt.setDouble(2, jogo.getPreco());
            stmt.setString(3, jogo.getDescricao());
            stmt.executeUpdate();
        }
    }

    public Jogo findById(int id) throws SQLException {
        String sql = "SELECT * FROM jogos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Jogo(
                        rs.getInt("id"),
                        rs.getString("jogo"),
                        rs.getInt("preco"),
                        rs.getString("descricao")
                );
            }
        }
        return null;
    }

    public List<Jogo> findAll() throws SQLException {
        List<Jogo> jogos = new ArrayList<>();
        String sql = "SELECT * FROM jogos";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                jogos.add(new Jogo(
                        rs.getInt("id"),
                        rs.getString("jogo"),
                        rs.getInt("preco"),
                        rs.getString("descricao")
                ));
            }
        }
        return jogos;
    }

    public void update(Jogo jogo) throws SQLException {
        String sql = "UPDATE jogos SET jogo = ?, preco = ?, descricao = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jogo.getJogo());
            stmt.setDouble(2, jogo.getPreco());
            stmt.setString(3, jogo.getDescricao());
            stmt.setInt(4, jogo.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM jogos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}