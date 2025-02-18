package com.vemser.rest.tests.usuario;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;


public class UsuariosDeleteTest {

    UsuarioClient usuarioClient = new UsuarioClient();
    private static String usuarioId;

    @BeforeEach
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValido()).path("_id");
    }

    @Test
    public void testSchemaDeveExcluirUsuarioComSucesso() {
        usuarioClient.deletarUsuarios(usuarioId)
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas\\deletar_usuario.json"))
        ;
    }

    @Test
    public void testDeveExcluirUsuarioComSucesso() {

        usuarioClient.deletarUsuarios(usuarioId)
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @Test
    public void testDeveExcluirUsuarioComIDInvalidoSemSucesso() {
        usuarioClient.deletarUsuarios(UsuarioDataFactory.usuarioIdInvalidoZero())
        .then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"))
        ;


    }

    @Test
    public void testDeveExcluirUsuarioComDoisIDsValidoSemSucesso() {
        usuarioClient.deletarUsuarios(usuarioId + UsuarioDataFactory.usuarioIdCriadoComCarrinho())
                .then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"))
        ;


    }

    @Test
    public void testDeveExcluirUsuarioComCarrinhoSemSucesso() {
        usuarioClient.deletarUsuarios(UsuarioDataFactory.usuarioIdCriadoComCarrinho())
        .then()
                .statusCode(400)
                .body("message", equalTo("Não é permitido excluir usuário com carrinho cadastrado"))
        ;
    }

    @AfterEach
    public void tearDown() {
        usuarioClient.deletarUsuarios(usuarioId);
    }
}
