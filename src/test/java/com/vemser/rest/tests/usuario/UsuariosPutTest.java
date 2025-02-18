package com.vemser.rest.tests.usuario;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Usuario;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class UsuariosPutTest {

    private static String usuarioId;
    UsuarioClient usuarioClient = new UsuarioClient();
    Usuario usuario = new Usuario();


    @BeforeEach
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValido()).path("_id");
        usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
    }

    @Test
    @Tag("schema")
    public void testSchemaDeveAtualizarUsuarioComSucesso() {
        Usuario usuarioAtualizado = UsuarioDataFactory.atualizarNomeSenhaUsuario(usuario);
        usuarioClient.atualizarUsuarios(usuarioId, usuarioAtualizado)
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas\\atualizar_usuario.json"))
        ;
    }

    @Test
    public void testDeveAtualizarUsuarioComSucesso() {

        Usuario usuarioAtualizado = UsuarioDataFactory.atualizarNomeSenhaUsuario(usuario);
        usuarioClient.atualizarUsuarios(usuarioId, usuarioAtualizado)
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @Test
    public void testDeveAtualizarUsuarioComCampoNomeVazioSemSucesso() {

        Usuario usuarioAtualizado = UsuarioDataFactory.atualizarNomeVazioUsuario(usuario);
        usuarioClient.atualizarUsuarios(usuarioId, usuarioAtualizado)
          .then()
                .statusCode(400)
                .body("nome", equalTo("nome não pode ficar em branco"))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @Test
    public void testDeveAtualizarUsuarioComNomeExcessivamenteLongoSemSucesso() {

        Usuario usuarioAtualizado = UsuarioDataFactory.atualizarNomeExcessivamenteLongoUsuario(usuario);
        usuarioClient.atualizarUsuarios(usuarioId, usuarioAtualizado)
            .then()
                    .statusCode(413)
                    .body("message", equalTo("Payload too large"))
                    .time(Matchers.lessThan(3000L))
            ;
    }

    @Test
    public void testDeveAtualizarUsuarioComIDInválidoEmailExistenteSemSucesso() {

        Usuario usuarioAtualizado = UsuarioDataFactory.atualizarNomeSenhaUsuario(usuario);
        usuarioClient.atualizarUsuarios(UsuarioDataFactory.usuarioIdInvalidoZero(), usuarioAtualizado)
        .then()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"))
        ;

    }

    @AfterEach
    public void tearDown() {
        usuarioClient.deletarUsuarios(usuarioId);
    }
}
