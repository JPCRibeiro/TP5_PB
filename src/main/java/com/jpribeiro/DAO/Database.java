package com.jpribeiro.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() throws SQLException {
        String createUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE
            );
        """;

        String createJogos = """
            CREATE TABLE IF NOT EXISTS jogos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                jogo TEXT NOT NULL,
                preco REAL NOT NULL,
                descricao TEXT NOT NULL
            );
        """;

        String createBiblioteca = """
            CREATE TABLE IF NOT EXISTS biblioteca (
                usuario_id INTEGER,
                jogo_id INTEGER,
                data_aquisicao DATETIME DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (usuario_id, jogo_id),
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
                FOREIGN KEY (jogo_id) REFERENCES jogos(id) ON DELETE CASCADE
            );
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsuarios);
            stmt.execute(createJogos);
            stmt.execute(createBiblioteca);
        }
    }
}