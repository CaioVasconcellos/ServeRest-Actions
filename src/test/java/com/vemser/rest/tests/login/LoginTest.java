package com.vemser.rest.tests.login;

import com.vemser.rest.client.LoginClient;
import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.LoginDataFactory;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Usuario;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class LoginTest {

    UsuarioClient usuarioClient = new UsuarioClient();
    LoginClient client = new LoginClient();
    private static Usuario usuario;
    private static String usuarioId;



    @BeforeEach
    public void setUp() {
         usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValido()).path("_id");
         usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
    }

    @Test
    @Tag("schema")
    public void testSchemaRealizarLoginComSucesso() {
        client.realizarLogin(LoginDataFactory.loginValido(usuario))
        .then()
                 .statusCode(200)
                 .body(matchesJsonSchemaInClasspath("schemas/login_usuarios.json"))
        ;
    }

    @Test
    public void testRealizarLoginComSucesso() {
        client.realizarLogin(LoginDataFactory.loginValido(usuario))
        .then()
                .statusCode(200)
                .time(Matchers.lessThan(3000L))
                .body("message", equalTo("Login realizado com sucesso"))
                .body("authorization",containsStringIgnoringCase("bearer"))
        ;
    }

    @Test
    public void testRealizarLoginComSenhaInvalidaSemSucesso() {
        client.realizarLogin(LoginDataFactory.loginSenhaInvalida(usuario))
        .then()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"))
                .body("authorization", Matchers.nullValue())
        ;
    }

    @Test
    public void testRealizarLoginComCampoSenhaVazioSemSucesso() {
        client.realizarLogin(LoginDataFactory.loginSenhaEmBranco(usuario))
        .then()
                .statusCode(400)
                .body("password", equalTo("password não pode ficar em branco"))
                .body("authorization", Matchers.nullValue())
        ;
    }

    @AfterEach
    public void tearDown() {
        usuarioClient.deletarUsuarios(usuarioId);
    }

}

