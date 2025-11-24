package com.jpribeiro.view;

import com.jpribeiro.model.Jogo;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JogoView {
    public static String renderList(List<Jogo> jogos) {
        StringBuilder html = new StringBuilder("""
                <!DOCTYPE html>
                <html lang="pt">
                <head>
                    <meta charset="UTF-8">
                    <title>GameTrack - Jogos</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/darkly/bootstrap.min.css" rel="stylesheet">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                </head>
                <body>
        """);

        html.append(HeaderView.render());

        html.append("""
                <div class="container mt-5">
                    <h1>Lista de Jogos</h1>
                    <a href="/jogos/adicionar" class="btn btn-primary mb-3 mt-4">Adicionar Novo Jogo</a>
                    <table class="table table-striped text-center">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Jogo</th>
                                <th>Preço</th>
                                <th>Descrição</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                """);

        for (Jogo jogo : jogos) {
            html.append(String.format("""
                    <tr>
                        <td>%d</td>
                        <td>%s</td>
                        <td>R$ %.2f</td>
                        <td>%s</td>
                        <td>
                            <a href="/jogos/editar/%d" class="btn btn-sm btn-warning">Editar</a>
                            <form action="/jogos/excluir/%d" method="post" style="display:inline;">
                                <button type="submit" class="btn btn-sm btn-danger">Deletar</button>
                            </form>
                        </td>
                    </tr>
                    """, jogo.getId(), jogo.getJogo(), jogo.getPreco(), jogo.getDescricao(), jogo.getId(), jogo.getId()));
        }

        html.append("""
                        </tbody>
                    </table>
                    </div>
                </body>
                </html>
                """);

        return html.toString();
    }

    public static String renderForm(Map<String, Object> model) {
        Object id = model.get("id");
        String action = id != null ? "/jogos/editar/" + id : "/jogos";
        String pageTitle = id != null ? "GameTrack - Editar Jogo" : "GameTrack - Novo Jogo";
        String title = id != null ? "Editar Jogo" : "Novo Jogo";

        String jogo = (String) model.getOrDefault("jogo", "");
        String descricao = (String) model.getOrDefault("descricao", "");
        Object precoObj = model.getOrDefault("preco", 0.0);
        Double preco;

        if (precoObj instanceof Number) {
            preco = ((Number) precoObj).doubleValue();
        } else {
            try {
                String precoStr = precoObj.toString().replace(",", ".");
                preco = Double.parseDouble(precoStr);
            } catch (Exception e) {
                preco = 0.0;
            }
        }
        String erro = (String) model.get("erro");

        String erroHtml = (erro != null && !erro.isBlank())
                ? String.format("<div class='alert alert-danger'>%s</div>", erro)
                : "";

        return String.format(Locale.US, """
        <!DOCTYPE html>
        <html lang="pt">
        <head>
            <meta charset="UTF-8">
            <title>%s</title>
            <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/darkly/bootstrap.min.css" rel="stylesheet">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </head>
        <body>
            %s
            <div class="container mt-5">
                <h1>%s</h1>
                %s
                <form action="%s" method="post">
                    <div class="mb-3">
                        <label for="jogo" class="form-label">Nome do Jogo</label>
                        <input type="text" class="form-control" id="jogo" name="jogo" value="%s" required>
                    </div>
                    <div class="mb-3">
                        <label for="preco" class="form-label">Preço</label>
                        <input type="number" step="0.01" class="form-control" id="preco" name="preco" value="%.2f" required>
                    </div>
                    <div class="mb-3">
                        <label for="descricao" class="form-label">Descrição</label>
                        <textarea class="form-control" id="descricao" name="descricao" rows="3" required>%s</textarea>
                    </div>
                    <button type="submit" class="btn btn-success">Salvar</button>
                    <a href="/jogos" class="btn btn-secondary">Cancelar</a>
                </form>
            </div>
        </body>
        </html>
        """,
                pageTitle,
                HeaderView.render(),
                title,
                erroHtml,
                action,
                jogo,
                preco,
                descricao
        );
    }

}