package com.jpribeiro.controller;

import com.jpribeiro.model.Jogo;
import com.jpribeiro.model.Usuario;
import com.jpribeiro.service.BibliotecaService;
import com.jpribeiro.service.JogoService;
import com.jpribeiro.service.UsuarioService;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeralControllerTest {
    @Test
    void testHomePage() {
        Javalin app = Javalin.create();

        new GeralController(app, mock(BibliotecaService.class), mock(UsuarioService.class), mock(JogoService.class));

        JavalinTest.test(app, (server, client) -> {
            try (var response = client.get("/")) {
                assertEquals(200, response.code());
            }
        });
    }

    @Test
    void testBibliotecaUsuario() throws Exception {
        BibliotecaService bibliotecaService = mock(BibliotecaService.class);
        UsuarioService usuarioService = mock(UsuarioService.class);
        JogoService jogoService = mock(JogoService.class);

        Usuario usuario = new Usuario(1, "João", "joao@email");
        List<Jogo> jogosUser = List.of(new Jogo(10, "Mario", 50, "Aventura"));
        List<Jogo> todosJogos = List.of(
                new Jogo(10, "Mario", 50, "Aventura"),
                new Jogo(11, "Zelda", 100, "RPG")
        );

        when(usuarioService.findUsuarioById(1)).thenReturn(usuario);
        when(bibliotecaService.listarJogosDoUsuario(1)).thenReturn(jogosUser);
        when(jogoService.findAllJogos()).thenReturn(todosJogos);

        Javalin app = Javalin.create();
        new GeralController(app, bibliotecaService, usuarioService, jogoService);

        JavalinTest.test(app, (server, client) -> {
            var res = client.get("/usuarios/1/biblioteca");

            assertEquals(200, res.code());
            assertNotNull(res.body());
            String html = res.body().string();

            assertTrue(html.contains("Biblioteca de João"));

            assertTrue(html.contains("Mario"));

            assertTrue(html.contains("R$ 50"));

            assertFalse(html.contains("Zelda"));
        });

        verify(usuarioService).findUsuarioById(1);
        verify(bibliotecaService).listarJogosDoUsuario(1);
        verify(jogoService).findAllJogos();
    }
}