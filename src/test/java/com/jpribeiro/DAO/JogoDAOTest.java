package com.jpribeiro.DAO;

import com.jpribeiro.model.Jogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JogoDAOTest {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    private JogoDAO dao;

    @BeforeEach
    void setup() {
        dao = new JogoDAO();

        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
    }

    @Test
    void deveAdicionarJogo() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString()))
                    .thenReturn(preparedStatement);

            dao.add(new Jogo("Halo", 199.90, "FPS"));

            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void deveRetornarJogoSeEncontrado() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString()))
                    .thenReturn(preparedStatement);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("jogo")).thenReturn("Halo");
            when(resultSet.getInt("preco")).thenReturn(150);
            when(resultSet.getString("descricao")).thenReturn("FPS");

            Jogo jogo = dao.findById(1);

            assertNotNull(jogo);
            assertEquals("Halo", jogo.getJogo());
            assertEquals(150, jogo.getPreco());
        }
    }

    @Test
    void deveRetornarNullSeNaoEncontrado() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString()))
                    .thenReturn(preparedStatement);

            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(false);

            Jogo jogo = dao.findById(999);

            assertNull(jogo);
        }
    }

    @Test
    void deveRetornarTodosJogos() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);

            // Simula 2 linhas no resultSet
            when(resultSet.next()).thenReturn(true, true, false);

            when(resultSet.getInt("id")).thenReturn(1, 2);
            when(resultSet.getString("jogo")).thenReturn("A", "B");
            when(resultSet.getInt("preco")).thenReturn(10, 20);
            when(resultSet.getString("descricao")).thenReturn("Desc A", "Desc B");

            List<Jogo> jogos = dao.findAll();

            assertEquals(2, jogos.size());
            assertEquals("A", jogos.get(0).getJogo());
            assertEquals("B", jogos.get(1).getJogo());
        }
    }

    @Test
    void deveAtualizarJogo() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString()))
                    .thenReturn(preparedStatement);

            dao.update(new Jogo(1, "Novo", 99.99, "Descrição nova"));

            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void deveDeletarJogo() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString()))
                    .thenReturn(preparedStatement);

            dao.delete(5);

            verify(preparedStatement).executeUpdate();
        }
    }
}
