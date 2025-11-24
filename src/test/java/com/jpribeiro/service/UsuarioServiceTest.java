package com.jpribeiro.service;

import com.jpribeiro.DAO.BibliotecaDAO;
import com.jpribeiro.DAO.UsuarioDAO;
import com.jpribeiro.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private BibliotecaDAO bibliotecaDAO;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deveCriarUsuarioEAdicionarJogos() throws SQLException {
        String nome = "Teste";
        String email = "t@t.com";
        List<String> jogosIds = Arrays.asList("10", "20");

        when(usuarioDAO.add(any())).thenReturn(99);

        service.addUsuario(nome, email, jogosIds);

        verify(usuarioDAO).add(any(Usuario.class));
        verify(bibliotecaDAO).addJogosAoUsuario(99, Arrays.asList(10, 20));
    }

    @Test
    void deveCriarUsuarioSemJogosQuandoListaForNull() throws SQLException {
        when(usuarioDAO.add(any())).thenReturn(1);

        service.addUsuario("Joao", "j@j.com", null);

        verify(usuarioDAO).add(any());
        verify(bibliotecaDAO, never()).addJogosAoUsuario(anyInt(), anyList());
    }

    @Test
    void deveIgnorarJogosInvalidos() throws SQLException {
        when(usuarioDAO.add(any())).thenReturn(5);

        List<String> invalido = Arrays.asList("abc", "10");

        service.addUsuario("Teste", "t@t.com", invalido);

        verify(bibliotecaDAO).addJogosAoUsuario(5, List.of(10));
    }

    @Test
    void deveLancarErroSeIdInvalidoFind() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findUsuarioById(0));
    }

    @Test
    void deveLancarErroSeUsuarioNaoExiste() throws SQLException {
        when(usuarioDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.findUsuarioById(1));
    }

    @Test
    void deveRetornarUsuarioSeEncontrado() throws SQLException {
        Usuario u = new Usuario(1, "A", "B");
        when(usuarioDAO.findById(1)).thenReturn(u);

        Usuario resultado = service.findUsuarioById(1);

        assertEquals(u, resultado);
    }

    @Test
    void deveRetornarTodosUsuarios() throws SQLException {
        List<Usuario> lista = List.of(new Usuario(1, "A", "B"));

        when(usuarioDAO.findAll()).thenReturn(lista);

        assertEquals(lista, service.findAllUsuarios());
    }

    @Test
    void deveLancarErroUpdateSeIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateUsuario(0, "A", "B"));
    }

    @Test
    void deveLancarErroUpdateSeUsuarioNaoExiste() throws SQLException {
        when(usuarioDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateUsuario(1, "A", "B"));
    }

    @Test
    void deveAtualizarUsuario() throws SQLException {
        when(usuarioDAO.findById(1)).thenReturn(new Usuario(1, "Antigo", "antigo@mail.com"));

        service.updateUsuario(1, "Novo", "novo@mail.com");

        verify(usuarioDAO).update(any(Usuario.class));
    }

    @Test
    void deveLancarErroDeleteIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteUsuario(0));
    }

    @Test
    void deveLancarErroDeleteSeUsuarioNaoExiste() throws SQLException {
        when(usuarioDAO.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> service.deleteUsuario(1));
    }

    @Test
    void deveDeletarUsuario() throws SQLException {
        when(usuarioDAO.findById(2)).thenReturn(new Usuario(2, "A", "B"));

        service.deleteUsuario(2);

        verify(usuarioDAO).delete(2);
    }
}