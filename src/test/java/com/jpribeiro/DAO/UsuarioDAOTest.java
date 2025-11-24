package com.jpribeiro.DAO;

import com.jpribeiro.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDAOTest {
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    Statement statement;
    @Mock
    ResultSet resultSet;

    private UsuarioDAO dao;

    @BeforeEach
    void setup() {
        dao = new UsuarioDAO();
    }

    @Test
    void deveAdicionarUsuarioERetornarId() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);

            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(10);

            int id = dao.add(new Usuario("João", "j@j.com"));

            assertEquals(10, id);
            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void deveRetornarUsuarioQuandoEncontrado() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("nome")).thenReturn("Maria");
            when(resultSet.getString("email")).thenReturn("m@m.com");

            Usuario u = dao.findById(1);

            assertNotNull(u);
            assertEquals("Maria", u.getNome());
        }
    }

    @Test
    void deveRetornarNullQuandoUsuarioNaoEncontrado() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(false);

            Usuario u = dao.findById(99);

            assertNull(u);
        }
    }

    @Test
    void deveRetornarTodosUsuarios() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true, true, false);

            when(resultSet.getInt("id")).thenReturn(1, 2);
            when(resultSet.getString("nome")).thenReturn("A", "B");
            when(resultSet.getString("email")).thenReturn("a@a.com", "b@b.com");

            List<Usuario> usuarios = dao.findAll();

            assertEquals(2, usuarios.size());
            assertEquals("A", usuarios.get(0).getNome());
        }
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            dao.update(new Usuario(1, "Novo", "n@n.com"));

            verify(preparedStatement).executeUpdate();
        }
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            dao.delete(5);

            verify(preparedStatement).executeUpdate();
        }
    }
}
