package com.jpribeiro.view;

import com.jpribeiro.model.Jogo;
import com.jpribeiro.model.Usuario;

import java.util.List;

public class BibliotecaView {
    public static String render(Usuario usuario, List<Jogo> biblioteca, List<Jogo> todosJogos) {
        StringBuilder html = new StringBuilder(
                String.format("""
                        <!DOCTYPE html>
                        <html lang="pt">
                        <head>
                            <meta charset="UTF-8">
                            <title>GameTrack - Biblioteca de %s</title>
                            <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/darkly/bootstrap.min.css" rel="stylesheet">
                            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                        </head>
                        <body>
                        """, usuario.getNome())
        );

        html.append(HeaderView.render());

        html.append("<div class='container mt-5'>");
        html.append("<h1>Biblioteca de ").append(usuario.getNome()).append("</h1>");
        html.append("<h4>Jogos que possui</h4>");
        html.append("<ul class='list-group mt-3'>");
        for (Jogo j : biblioteca) {
            html.append("<li class='list-group-item d-flex justify-content-between'>")
                    .append(j.getJogo())
                    .append("<span>R$ ").append(j.getPreco()).append("</span>")
                    .append("</li>");
        }
        html.append("</ul>");
        html.append("</div>");

        html.append("""
                </body>
                </html>
                """);

        return html.toString();
    }
}