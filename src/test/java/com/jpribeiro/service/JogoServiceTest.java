package com.jpribeiro.service;

import com.jpribeiro.DAO.JogoDAO;
import com.jpribeiro.model.Jogo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JogoServiceTest {
    @Mock
    private JogoDAO jogoDAO;

    @InjectMocks
    private JogoService service;

    @Test
    void deveAdicionarJogo() throws SQLException {
        service.addJogo("God of War", 199.99, "Ação");

        verify(jogoDAO).add(any(Jogo.class));
    }

    @Test
    void deveLancarErroFindIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findJogoById(0));
    }

    @Test
    void deveLancarErroJogoNaoEncontrado() throws SQLException {
        when(jogoDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.findJogoById(1));
    }

    @Test
    void deveRetornarJogoSeEncontrado() throws SQLException {
        Jogo jogo = new Jogo(1, "Halo", 100.0, "FPS");

        when(jogoDAO.findById(1)).thenReturn(jogo);

        Jogo encontrado = service.findJogoById(1);

        assertEquals(jogo, encontrado);
    }

    @Test
    void deveRetornarTodosJogos() throws SQLException {
        List<Jogo> lista = List.of(new Jogo(1, "A", 10, "D"));

        when(jogoDAO.findAll()).thenReturn(lista);

        assertEquals(lista, service.findAllJogos());
    }

    @Test
    void deveLancarErroUpdateIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateJogo(0, "A", 10, "B"));
    }

    @Test
    void deveLancarErroUpdateJogoNaoExiste() throws SQLException {
        when(jogoDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateJogo(1, "A", 10, "B"));
    }

    @Test
    void deveAtualizarJogo() throws SQLException {
        when(jogoDAO.findById(1)).thenReturn(new Jogo(1, "Old", 10, "Desc"));

        service.updateJogo(1, "Novo", 20, "Nova desc");

        verify(jogoDAO).update(any(Jogo.class));
    }

    @Test
    void deveLancarErroDeleteIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteJogo(0));
    }

    @Test
    void deveLancarErroDeleteJogoNaoExiste() throws SQLException {
        when(jogoDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.deleteJogo(1));
    }

    @Test
    void deveDeletarJogo() throws SQLException {
        when(jogoDAO.findById(2)).thenReturn(new Jogo(2, "A", 10, "B"));

        service.deleteJogo(2);

        verify(jogoDAO).delete(2);
    }
}
