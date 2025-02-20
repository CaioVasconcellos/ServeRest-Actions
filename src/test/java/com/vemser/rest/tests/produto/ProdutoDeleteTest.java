package com.vemser.rest.tests.produto;

import com.vemser.rest.client.LoginClient;
import com.vemser.rest.client.ProdutoClient;
import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.LoginDataFactory;
import com.vemser.rest.data.factory.ProdutoDataFactory;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.vemser.rest.data.provider.ProdutoProvider.PATH_PRODUTO_PROVIDER;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class ProdutoDeleteTest {
    UsuarioClient usuarioClient = new UsuarioClient();
    String usuarioId;
    LoginClient loginClient = new LoginClient();
    String authorization;
    ProdutoClient produtoClient = new ProdutoClient();
    String produtoId;

    @BeforeEach
    public void setUp() {

        if(true){
            System.out.println("Teste");
        }else{
            System.out.println("Teste");
        }
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValidoAdminTrue()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
        authorization = loginClient.realizarLogin(LoginDataFactory.loginValido(usuario)).path("authorization");

        produtoId = produtoClient.cadastrarProduto(ProdutoDataFactory.produtoValido(),authorization).path("_id");
    }

    @Test
    @Tag("schema")
    public void testSchemaDeletarProdutoValidoComSucesso(){
        produtoClient.deletarProduto(produtoId, authorization)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("deletar_produto.json"))
        ;
    }

    @Test
    public void testDeletarProdutoValidoComSucesso(){
        produtoClient.deletarProduto(produtoId, authorization)
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"))
        ;
    }

    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoDeletarInvalidoDataProvider")
    public void testDeletarProdutoInvalidoSemSucesso(String id, String campo, String valor, int status){
        produtoClient.deletarProduto(id, authorization)
                .then()
                .statusCode(status)
                .body(campo, equalTo(valor))
        ;
    }

    @AfterEach
    public void tearDown() {
        usuarioClient.deletarUsuarios(usuarioId);

    }


}
