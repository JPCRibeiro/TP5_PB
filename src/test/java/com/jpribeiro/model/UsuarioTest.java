package com.jpribeiro.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void testCriarUsuarioCorretamente() {
        Usuario usuario = new Usuario(1, "João", "joao@email.com");

        assertEquals(1, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
    }
}