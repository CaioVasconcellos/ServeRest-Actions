package com.vemser.rest.tests.produto;

import com.vemser.rest.client.LoginClient;
import com.vemser.rest.client.ProdutoClient;
import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.LoginDataFactory;
import com.vemser.rest.data.factory.ProdutoDataFactory;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Produto;
import com.vemser.rest.model.Usuario;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.vemser.rest.data.provider.ProdutoProvider.PATH_PRODUTO_PROVIDER;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ProdutoPutTest {

    UsuarioClient usuarioClient = new UsuarioClient();
    String usuarioId;
    LoginClient loginClient = new LoginClient();
    String authorization;
    ProdutoClient produtoClient = new ProdutoClient();
    String produtoId;


    @BeforeEach
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValidoAdminTrue()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
        authorization = loginClient.realizarLogin(LoginDataFactory.loginValido(usuario)).path("authorization");

        produtoId = produtoClient.cadastrarProduto(ProdutoDataFactory.produtoValido(),authorization).path("_id");
    }

    @Test
    public void testSchemaAtualizarProdutoValidoComSucesso(){
        produtoClient.atualizarProduto(produtoId,ProdutoDataFactory.atualizarPrecoProdutoValido(),authorization)
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas\\atualizar_produto.json"))
        ;
    }

    @Test
    public void testAtualizarProdutoValidoComSucesso(){
        produtoClient.atualizarProduto(produtoId,ProdutoDataFactory.atualizarPrecoProdutoValido(),authorization)
        .then().statusCode(200);
    }

    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoInvalidoStatus400DataProvider")
    public void testAtualizarProdutoInvalidoStatus400SemSucesso(Produto produto, String campo, String value){
        produtoClient.atualizarProduto(produtoId,produto, authorization)
        .then().statusCode(400)
                .body(campo, Matchers.equalTo(value));
    }


    @AfterEach
    public void tearDown() {
        produtoClient.deletarProduto(produtoId, authorization);
        usuarioClient.deletarUsuarios(usuarioId);

    }
}
