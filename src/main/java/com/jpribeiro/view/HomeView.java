package com.jpribeiro.view;

public class HomeView {
    public static String renderHome() {
        StringBuilder html = new StringBuilder("""
        <!DOCTYPE html>
        <html lang="pt">
            <head>
                <meta charset="UTF-8">
                <title>GameTrack</title>
                <link href="https://cdn.jsdelivr.net/npm/bootswatch@5.3.0/dist/darkly/bootstrap.min.css" rel="stylesheet">
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </head>
            <body>
        """);

        html.append(HeaderView.render());

        html.append("""
                <div class="container mt-5">
                    <h1>GameTrack</h1>
                    <div class="mt-4">
                        <div class="mt-4">
                            <p class="fs-5">Veja todos os jogos que nossa comunidade cadastrou em nosso site e adicione eles ao seu perfil para mostrar sua coleção para todos!</p>
                            <a href="/jogos" class="btn btn-primary">Ver jogos</a>
                        </div>
                        <div class="mt-5">
                            <p class="fs-5">Veja todos os usuários que se juntaram a nossa comunidade!</p>
                            <a href="/usuarios" class="btn btn-success">Ver usuários</a>
                        </div>
                    </div>
                </div>
            </body>
        </html>
        """);
        return html.toString();
    }
}