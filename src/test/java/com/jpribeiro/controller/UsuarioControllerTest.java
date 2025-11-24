package com.jpribeiro.controller;

import com.jpribeiro.DAO.BibliotecaDAO;
import com.jpribeiro.model.Jogo;
import com.jpribeiro.model.Usuario;
import com.jpribeiro.service.JogoService;
import com.jpribeiro.service.UsuarioService;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private JogoService jogoService;
    @Mock
    private BibliotecaDAO bibliotecaDAO;

    private Javalin app;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        app = Javalin.create();
        new UsuarioController(app, usuarioService, jogoService, bibliotecaDAO);
    }

    @Test
    void testListarUsuarios() throws SQLException {
        when(usuarioService.findAllUsuarios()).thenReturn(List.of(
                new Usuario(1, "Maria", "maria@email.com")
        ));

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/usuarios");

            assertEquals(200, response.code());
            Assertions.assertNotNull(response.body());
            assertTrue(response.body().string().contains("Maria"));
        });
    }

    @Test
    void testExibirFormularioAdicionar() throws SQLException {
        when(jogoService.findAllJogos()).thenReturn(List.of(
                new Jogo(10, "Mario", 50.0, "Aventura")
        ));

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/usuarios/adicionar");

            assertEquals(200, response.code());
            Assertions.assertNotNull(response.body());
            String html = response.body().string();
            assertTrue(html.contains("Mario"), "Deve listar jogos para seleção");
        });
    }

    @Test
    void testAdicionarUsuario_Sucesso() {
        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("nome", "Carlos")
                    .add("email", "carlos@teste.com")
                    .add("jogosIds", "10")
                    .add("jogosIds", "20")
                    .build();

            var response = client.request("/usuarios", req -> req.post(formBody));

            assertEquals(200, response.code());

            verify(usuarioService).addUsuario(
                    eq("Carlos"),
                    eq("carlos@teste.com"),
                    eq(List.of("10", "20"))
            );
        });
    }

    @Test
    void testAdicionarUsuario_Erro() throws SQLException {
        doThrow(new RuntimeException("Email já existe"))
                .when(usuarioService).addUsuario(anyString(), anyString(), anyList());

        when(jogoService.findAllJogos()).thenReturn(Collections.emptyList());

        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("nome", "Carlos")
                    .add("email", "duplicado@teste.com")
                    .build();

            var response = client.request("/usuarios", req -> req.post(formBody));

            assertEquals(200, response.code());
            Assertions.assertNotNull(response.body());
            String html = response.body().string();

            assertTrue(html.contains("Erro ao adicionar usuário"), "Deve exibir erro na tela");
            assertTrue(html.contains("Email já existe"));
        });
    }

    @Test
    void testExibirFormularioEdicao() throws SQLException {
        int userId = 1;
        Usuario usuario = new Usuario(userId, "Ana", "ana@orig.com");
        List<Jogo> jogosDoUsuario = List.of(new Jogo(5, "Zelda", 100.0, "RPG"));
        List<Jogo> todosJogos = List.of(new Jogo(5, "Zelda", 100.0, "RPG"), new Jogo(6, "Outro", 50.0, "Ação"));

        when(usuarioService.findUsuarioById(userId)).thenReturn(usuario);
        when(bibliotecaDAO.findById(userId)).thenReturn(jogosDoUsuario);
        when(jogoService.findAllJogos()).thenReturn(todosJogos);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/usuarios/editar/1");

            assertEquals(200, response.code());
            Assertions.assertNotNull(response.body());
            String html = response.body().string();

            assertTrue(html.contains("Ana"), "Deve preencher o nome");
            assertTrue(html.contains("Zelda"), "Deve listar os jogos");
        });
    }

    @Test
    void testAtualizarUsuario_Sucesso() {
        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("nome", "Ana Updated")
                    .add("email", "ana@new.com")
                    .build();

            var response = client.request("/usuarios/editar/1", req -> req.post(formBody));

            assertEquals(200, response.code());

            verify(usuarioService).updateUsuario(1, "Ana Updated", "ana@new.com");
        });
    }

    @Test
    void testExcluirUsuario() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/usuarios/excluir/99");

            assertEquals(200, response.code());
            verify(usuarioService).deleteUsuario(99);
        });
    }
}