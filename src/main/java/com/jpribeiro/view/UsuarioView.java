package com.jpribeiro.view;

import com.jpribeiro.model.Jogo;
import com.jpribeiro.model.Usuario;

import java.util.List;
import java.util.Map;

public class UsuarioView {
    public static String renderList(List<Usuario> usuarios) {
        StringBuilder html = new StringBuilder("""
                        <!DOCTYPE html>
                        <html lang="pt">
                        <head>
                            <meta charset="UTF-8">
                            <title>GameTrack - Usuários</title>
                            <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/darkly/bootstrap.min.css" rel="stylesheet">
                            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                        </head>
                        <body>
                """);

        html.append(HeaderView.render());

        html.append("""
                    <div class="container mt-5">
                        <h1>Lista de Usuários</h1>
                        <a href="/usuarios/adicionar" class="btn btn-primary mb-3 mt-4">Adicionar Novo Usuário</a>
                        <table class="table table-striped text-center">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Apelido</th>
                                    <th>Email</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                """);

        for (Usuario usuario : usuarios) {
            html.append(String.format("""
                        <tr>
                            <td>%d</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>
                                <a href="/usuarios/%d/biblioteca" class="btn btn-sm btn-success">Ver Biblioteca</a>
                                <a href="/usuarios/editar/%d" class="btn btn-sm btn-warning">Editar</a>
                                <form action="/usuarios/excluir/%d" method="post" style="display:inline;">
                                    <button type="submit" class="btn btn-sm btn-danger">Deletar</button>
                                </form>
                            </td>
                        </tr>
                    """, usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getId(), usuario.getId(), usuario.getId()));
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

    public static String renderForm(Map<String, Object> model, List<Jogo> jogos) {
        Object id = model.get("id");
        String action = id != null ? "/usuarios/editar/" + id : "/usuarios";
        String title = id != null ? "Editar Usuário" : "Novo Usuário";

        String nome = (String) model.getOrDefault("nome", "");
        String email = (String) model.getOrDefault("email", "");
        String erro = (String) model.get("erro");

        String erroHtml = (erro != null && !erro.isBlank())
                ? String.format("<div class='alert alert-danger'>%s</div>", erro)
                : "";

        StringBuilder jogosHtml = new StringBuilder();
        jogosHtml.append("<div class='mb-3'><label class='form-label'>Jogos</label>");

        for (Jogo jogo : jogos) {
            boolean checked = false;

            List<Integer> biblioteca = (List<Integer>) model.get("biblioteca");
            if (biblioteca != null && biblioteca.contains(jogo.getId())) {
                checked = true;
            }

            jogosHtml.append(String.format("""
        <div class='form-check'>
            <input class='form-check-input' type='checkbox'
                   name='jogosIds' value='%d' %s>
            <label class='form-check-label'>%s</label>
        </div>
        """,
                    jogo.getId(),
                    checked ? "checked" : "",
                    jogo.getJogo()
            ));
        }

        jogosHtml.append("</div>");

        return String.format("""
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
                                <label for="nome" class="form-label">Nome</label>
                                <input type="text" class="form-control" id="nome" name="nome" value="%s" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" value="%s" required>
                            </div>
                
                            %s
                
                            <button type="submit" class="btn btn-success">Salvar</button>
                            <a href="/usuarios" class="btn btn-secondary">Cancelar</a>
                        </form>
                    </div>
                </body>
                </html>
                """, title, HeaderView.render(), title, erroHtml, action, nome, email, jogosHtml);
    }
}