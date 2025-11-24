package com.jpribeiro.service;

import com.jpribeiro.DAO.BibliotecaDAO;
import com.jpribeiro.model.Jogo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliotecaServiceTest {
    @Mock
    private BibliotecaDAO bibliotecaDAO;

    @InjectMocks
    private BibliotecaService service;

    @Test
    void deveInstanciarServiceComConstrutorPadrao() {
        BibliotecaService serviceDefault = new BibliotecaService();
        assertNotNull(serviceDefault);
    }

    @Test
    void deveListarJogosDoUsuario() throws Exception {
        List<Jogo> jogos = List.of(
                new Jogo(1, "Jogo 1", 10.0, "Desc 1"),
                new Jogo(2, "Jogo 2", 20.0, "Desc 2")
        );

        when(bibliotecaDAO.findById(5)).thenReturn(jogos);

        List<Jogo> result = service.listarJogosDoUsuario(5);

        assertEquals(jogos, result);
        verify(bibliotecaDAO).findById(5);
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverJogos() throws Exception {
        when(bibliotecaDAO.findById(3)).thenReturn(List.of());

        List<Jogo> result = service.listarJogosDoUsuario(3);

        assertTrue(result.isEmpty());
        verify(bibliotecaDAO).findById(3);
    }
}
