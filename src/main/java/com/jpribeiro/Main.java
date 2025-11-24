package com.jpribeiro;

import com.jpribeiro.DAO.BibliotecaDAO;
import com.jpribeiro.DAO.Database;
import com.jpribeiro.controller.GeralController;
import com.jpribeiro.controller.JogoController;
import com.jpribeiro.controller.UsuarioController;
import com.jpribeiro.service.BibliotecaService;
import com.jpribeiro.service.JogoService;
import com.jpribeiro.service.UsuarioService;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database.init();

        Javalin app = Javalin.create().start(7000);

        UsuarioService usuarioService = new UsuarioService();
        JogoService jogoService = new JogoService();
        BibliotecaService bibliotecaService = new BibliotecaService();
        BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();

        new UsuarioController(app, usuarioService, jogoService, bibliotecaDAO);
        new JogoController(app, jogoService);
        new GeralController(app, bibliotecaService, usuarioService, jogoService);
    }
}