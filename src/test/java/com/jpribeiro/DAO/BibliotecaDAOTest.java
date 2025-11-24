package com.jpribeiro.DAO;

import static org.junit.jupiter.api.Assertions.*;

import com.jpribeiro.model.Jogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BibliotecaDAOTest {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeEach
    void setup() {
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }

    @Test
    void testFindById_retornarJogosDoUsuario() throws Exception {
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {
            mockedDb.when(Database::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);

            when(stmt.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getInt("id")).thenReturn(1, 2);
            when(rs.getString("jogo")).thenReturn("Jogo A", "Jogo B");
            when(rs.getDouble("preco")).thenReturn(19.99, 29.99);
            when(rs.getString("descricao")).thenReturn("Desc A", "Desc B");

            BibliotecaDAO dao = new BibliotecaDAO();
            List<Jogo> jogos = dao.findById(10);

            assertEquals(2, jogos.size());
            assertEquals("Jogo A", jogos.get(0).getJogo());
            assertEquals("Jogo B", jogos.get(1).getJogo());

            verify(stmt).setInt(1, 10);
            verify(stmt).executeQuery();
        }
    }

    @Test
    void testAddJogosAoUsuario_adicionaCorretamente() throws Exception {

        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {
            mockedDb.when(Database::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);

            BibliotecaDAO dao = new BibliotecaDAO();

            dao.addJogosAoUsuario(5, List.of(10, 20, 30));

            verify(stmt, times(3)).setInt(eq(1), eq(5));
            verify(stmt, times(1)).executeBatch();
        }
    }
}