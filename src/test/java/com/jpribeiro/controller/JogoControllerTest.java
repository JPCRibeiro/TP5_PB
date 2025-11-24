package com.jpribeiro.controller;

import com.jpribeiro.model.Jogo;
import com.jpribeiro.service.JogoService;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JogoControllerTest {
    @Mock
    private JogoService service;

    private Javalin app;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        app = Javalin.create();
        new JogoController(app, service);
    }

    @Test
    void testPaginaAdicionar() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/jogos/adicionar");
            assertEquals(200, response.code());
            assertNotNull(response.body());
            assertTrue(response.body().string().contains("<form"));
        });
    }

    @Test
    void testPaginaEditar_Sucesso() throws SQLException {
        Jogo jogoMock = new Jogo(10, "Zelda", 300.0, "RPG");
        when(service.findJogoById(10)).thenReturn(jogoMock);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/jogos/editar/10");

            assertEquals(200, response.code());

            String html = response.body().string();

            // --- DEBUG: O QUE ESTÁ CHEGANDO? ---
            System.out.println("HTML RECEBIDO: \n" + html);
            // -----------------------------------

            assertTrue(html.contains("Zelda"));

            // O erro está aqui. Vamos ver no console como o número veio (300,00 ou 300.00)
            assertTrue(html.contains("300.0"));
        });
    }

    @Test
    void testPaginaEditar_Erro() throws SQLException {
        when(service.findJogoById(99)).thenThrow(new RuntimeException("Jogo não encontrado"));

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/jogos/editar/99");

            assertEquals(200, response.code());
            assertNotNull(response.body());
            String body = response.body().string();
            assertTrue(body.contains("Erro: Jogo não encontrado"));
        });
    }

    @Test
    void testListarJogos() throws SQLException {
        when(service.findAllJogos()).thenReturn(List.of(new Jogo(1, "God of War", 200.0, "Ação")));

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/jogos");

            assertEquals(200, response.code());
            assertNotNull(response.body());
            String html = response.body().string();
            assertTrue(html.contains("God of War"), "Deve renderizar o nome do jogo na lista");
        });
    }

    @Test
    void testAdicionarJogo_Sucesso() {
        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("jogo", "Minecraft")
                    .add("preco", "150.1")
                    .add("descricao", "Blocos")
                    .build();

            var response = client.request("/jogos", req -> req.post(formBody));

            assertEquals(200, response.code());

            verify(service).addJogo("Minecraft", 150.1, "Blocos");
        });
    }


    @Test
    void testAdicionarJogo_ErroPrecoNegativo() {
        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("jogo", "Jogo Ruim")
                    .add("preco", "-10.00")
                    .add("descricao", "Desc")
                    .build();

            var response = client.request("/jogos", req -> req.post(formBody));

            assertNotNull(response.body());
            String html = response.body().string();
            System.out.println("STATUS CODE: " + response.code());
            System.out.println("HTML RECEBIDO: " + html);

            assertTrue(html.contains("Erro ao adicionar jogo"), "O HTML recebido foi: " + html);
        });
    }

    @Test
    void testAtualizarJogo_Sucesso() {
        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("jogo", "Zelda Updated")
                    .add("preco", "300.0")
                    .add("descricao", "RPG Top")
                    .build();

            var response = client.request("/jogos/editar/10", req -> req.post(formBody));

            assertEquals(200, response.code());

            verify(service).updateJogo(10, "Zelda Updated", 300.0, "RPG Top");
        });
    }

    @Test
    void testAtualizarJogo_Erro() throws SQLException {
        doThrow(new RuntimeException("Falha no banco"))
                .when(service).updateJogo(anyInt(), anyString(), anyDouble(), anyString());

        JavalinTest.test(app, (server, client) -> {
            var formBody = new FormBody.Builder()
                    .add("jogo", "Zelda")
                    .add("preco", "300.0")
                    .add("descricao", "RPG")
                    .build();

            var response = client.request("/jogos/editar/10", req -> req.post(formBody));

            assertNotNull(response.body());
            String html = response.body().string();
            assertTrue(html.contains("Erro ao atualizar jogo"));
            assertTrue(html.contains("Falha no banco"));
        });
    }

    @Test
    void testExcluirJogo() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/jogos/excluir/5");

            assertEquals(200, response.code());

            verify(service).deleteJogo(5);
        });
    }

    @Test
    void testExcluirJogo_Erro() throws SQLException {
        doThrow(new RuntimeException("Não pode excluir"))
                .when(service).deleteJogo(5);

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/jogos/excluir/5");

            assertEquals(200, response.code());
            assertNotNull(response.body());
            String body = response.body().string();
            assertTrue(body.contains("Erro ao excluir jogo"));
        });
    }
}