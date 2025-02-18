package com.vemser.rest.tests.produto;

import com.vemser.rest.client.LoginClient;
import com.vemser.rest.client.ProdutoClient;
import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.LoginDataFactory;
import com.vemser.rest.data.factory.ProdutoDataFactory;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Produto;
import com.vemser.rest.model.Usuario;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.vemser.rest.data.provider.ProdutoProvider.PATH_PRODUTO_PROVIDER;
import static com.vemser.rest.data.provider.ProdutoProvider.tokenIdFalse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ProdutoPostTest {

    static UsuarioClient usuarioClient = new UsuarioClient();
    String usuarioId;
    String produtoId;
    LoginClient loginClient = new LoginClient();
    String authorization;
    ProdutoClient produtoClient = new ProdutoClient();

    @BeforeEach
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValidoAdminTrue()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
        authorization = loginClient.realizarLogin(LoginDataFactory.loginValido(usuario)).path("authorization");
    }

    @Test
    public void testSchemaCadastrarProdutoValidoComSucesso() {

        Response response =
                produtoClient.cadastrarProduto(ProdutoDataFactory.produtoValido(), authorization)
                        .then().statusCode(201)
                        .body(matchesJsonSchemaInClasspath("schemas\\criar_produto.json"))
                        .extract().response()
                ;
        produtoId = response.path("_id");
    }

    @Test
    public void testCadastrarProdutoValidoComSucesso() {

        Response response =
        produtoClient.cadastrarProduto(ProdutoDataFactory.produtoValido(), authorization)
        .then().statusCode(201)
                .extract().response()
        ;
        produtoId = response.path("_id");
    }


    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoInvalidoStatus400DataProvider")
    public void testCadastrarProdutoInvalidoSemSucesso(Produto produto, String campo, String mensagem) {
        produtoClient.cadastrarProduto(produto, authorization)
        .then()
                .statusCode(400)
                .body(campo, Matchers.equalTo(mensagem))
        ;
    }

    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoTokenInvalidoDataProvider")
    public void testCadastrarProdutoInvalidoSemSucesso(Produto produto, String token, String campo, String mensagem, int status) {
        produtoClient.cadastrarProduto(produto, token)
                .then()
                .statusCode(status)
                .body(campo, Matchers.equalTo(mensagem))
        ;

    }



    @AfterEach
    public void tearDown() {
        if (produtoId != null) {
        produtoClient.deletarProduto(produtoId, authorization);
        }
        usuarioClient.deletarUsuarios(usuarioId);
    }

    @AfterAll
    public static void afterAll() {
        if(tokenIdFalse != null) {
            usuarioClient.deletarUsuarios(tokenIdFalse);
        }
    }
}
