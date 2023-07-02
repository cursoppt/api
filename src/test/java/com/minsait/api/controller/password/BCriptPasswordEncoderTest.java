package com.minsait.api.controller.password;

import com.minsait.api.repository.UsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Teste Classe de Codificação e Decodificação de Senhas")
@RunWith(SpringRunner.class)
@SpringBootTest
class BCriptPasswordEncoderTest {

    @Test
    @DisplayName("Deve gerar uma senha codificada")
    public void generateEncodedPasswordTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Root");
        usuario.setLogin("root");
        usuario.setSenha("12345");

        String senhaCodificada = encoder.encode(usuario.getSenha());

        assertNotNull(senhaCodificada, "A senha não foi codificada");
    }

    @Test
    @DisplayName("Deve verificar uma senha válida")
    public void verifiEncodedPasswordTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Root");
        usuario.setLogin("root");
        usuario.setSenha("12345");

        String senhaCodificada = encoder.encode(usuario.getSenha());
        boolean isSenhaIdentica = encoder.matches(usuario.getSenha(), senhaCodificada);

        assertTrue(isSenhaIdentica, "A senha não é idêntica");
    }

}