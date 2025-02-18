package com.vemser.rest.tests.usuario;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Usuario;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UsuariosPostTest {

    UsuarioClient usuarioClient = new UsuarioClient();
    String idUsuario;


    @Test
    public void testSchemaDeveCadastrarUsuarioComSucesso() {

        Usuario usuario = UsuarioDataFactory.usuarioValido();
        Response response =
                usuarioClient.cadastrarUsuarios(usuario)
                .then()
                        .statusCode(201)
                        .body(matchesJsonSchemaInClasspath("schemas\\criar_usuario.json"))
                        .extract().response()
                ;
        idUsuario = response.path("_id");


    }

    @Test
    public void testDeveCadastrarUsuarioComSucesso() {

        Usuario usuario = UsuarioDataFactory.usuarioValido();
        Response response =
                usuarioClient.cadastrarUsuarios(usuario)
                .then()
                        .statusCode(201)
                        .body("message",equalTo("Cadastro realizado com sucesso"))
                        .body("_id", notNullValue())
                        .extract().response()
                ;
        idUsuario = response.path("_id");


    }



    @Test
    public void testDeveCadastrarUsuarioComEmailRepetidoSemSucesso() {

        Usuario usuario = UsuarioDataFactory.usuarioEmailRepetido();
        usuarioClient.cadastrarUsuarios(usuario)
        .then()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"))
                .body("_id", Matchers.nullValue())
        ;

    }

    @Test
    public void testDeveCadastrarUsuarioComCampoSenhaVazioSemSucesso() {

        Usuario usuario = UsuarioDataFactory.usuarioSemSenha();

        usuarioClient.cadastrarUsuarios(usuario)
                .then()
                .statusCode(400)
                .body("password", equalTo("password não pode ficar em branco"))
        ;

    }

    @AfterEach
    public void tearDown(){
        if (idUsuario != null){
            usuarioClient.deletarUsuarios(idUsuario);
        }
    }
}
