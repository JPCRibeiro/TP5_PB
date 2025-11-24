package com.jpribeiro.DAO;

import com.jpribeiro.model.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaDAO {
    public List<Jogo> findById(int usuarioId) throws SQLException {
        String sql = """
            SELECT j.id, j.jogo, j.preco, j.descricao
            FROM biblioteca b
            JOIN jogos j ON b.jogo_id = j.id
            WHERE b.usuario_id = ?
        """;

        List<Jogo> jogos = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jogos.add(new Jogo(
                        rs.getInt("id"),
                        rs.getString("jogo"),
                        rs.getDouble("preco"),
                        rs.getString("descricao")
                ));
            }
        }

        return jogos;
    }

    public void addJogosAoUsuario(int usuarioId, List<Integer> jogosIds) throws SQLException {
        String sql = "INSERT INTO biblioteca (usuario_id, jogo_id) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Integer jogoId : jogosIds) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, jogoId);
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }
}