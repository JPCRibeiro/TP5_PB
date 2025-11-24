package com.jpribeiro.controller;

import com.jpribeiro.service.JogoService;
import com.jpribeiro.view.JogoView;
import io.javalin.Javalin;

import java.util.HashMap;

public class JogoController {
    private final JogoService service;

    public JogoController(Javalin app, JogoService service) {
        this.service = service;

        app.get("/jogos", ctx -> ctx.html(JogoView.renderList(service.findAllJogos())));

        app.get("/jogos/adicionar", ctx -> ctx.html(JogoView.renderForm(new HashMap<>())));

        app.post("/jogos", ctx -> {
            String jogo = ctx.formParam("jogo");
            String descricao = ctx.formParam("descricao");

            HashMap<String, Object> model = new HashMap<>();

            try {
                double preco = ctx.formParamAsClass("preco", Double.class)
                        .check(p -> p > 0, "Preço deve ser maior que zero")
                        .get();

                service.addJogo(jogo, preco, descricao);
                ctx.redirect("/jogos");

            } catch (Exception e) {
                model.put("erro", "Erro ao adicionar jogo: " + e.getMessage());

                model.put("jogo", jogo);
                model.put("descricao", descricao);
                model.put("preco", ctx.formParam("preco"));

                ctx.html(JogoView.renderForm(model));
            }
        });

        app.get("/jogos/editar/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            HashMap<String, Object> model = new HashMap<>();

            try {
                var jogo = service.findJogoById(id);
                model.put("id", jogo.getId());
                model.put("jogo", jogo.getJogo());
                model.put("preco", jogo.getPreco());
                model.put("descricao", jogo.getDescricao());
                ctx.html(JogoView.renderForm(model));
            } catch (Exception e) {
                ctx.result("Erro: " + e.getMessage());
            }
        });

        app.post("/jogos/editar/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            String jogo = ctx.formParam("jogo");
            String descricao = ctx.formParam("descricao");

            HashMap<String, Object> model = new HashMap<>();

            try {
                double preco = ctx.formParamAsClass("preco", Double.class)
                        .check(p -> p > 0, "Preço deve ser maior que zero")
                        .get();

                service.updateJogo(id, jogo, preco, descricao);
                ctx.redirect("/jogos");

            } catch (Exception e) {
                model.put("erro", "Erro ao atualizar jogo: " + e.getMessage());
                model.put("id", id);
                model.put("jogo", jogo);
                model.put("descricao", descricao);
                model.put("preco", ctx.formParam("preco"));

                ctx.html(JogoView.renderForm(model));
            }
        });

        app.post("/jogos/excluir/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            try {
                service.deleteJogo(id);
                ctx.redirect("/jogos");
            } catch (Exception e) {
                ctx.result("Erro ao excluir jogo: " + e.getMessage());
            }
        });
    }
}