package com.jpribeiro.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JogoTest {
    @Test
    void testCriarJogoCorretamente() {
        Jogo jogo = new Jogo(1, "God of War Ragnarök", 249.00, "Kratos e Atreus embarcam numa viagem mítica em busca de respostas antes da chegada do Ragnarök — agora no PC.");

        assertEquals(1, jogo.getId());
        assertEquals("God of War Ragnarök", jogo.getJogo());
        assertEquals(249.00, jogo.getPreco());
        assertEquals("Kratos e Atreus embarcam numa viagem mítica em busca de respostas antes da chegada do Ragnarök — agora no PC.", jogo.getDescricao());
    }
}