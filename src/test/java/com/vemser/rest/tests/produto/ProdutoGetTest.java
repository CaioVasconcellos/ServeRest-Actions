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
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.vemser.rest.data.provider.ProdutoProvider.PATH_PRODUTO_PROVIDER;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class ProdutoGetTest {
    UsuarioClient usuarioClient = new UsuarioClient();
    String usuarioId;
    LoginClient loginClient = new LoginClient();

    ProdutoClient produtoClient = new ProdutoClient();
    static String produtoId;
    static String nomeProduto;
    static String descricaoProduto;
    static double precoProduto;
    static int quantidadeProduto;
    Produto produto;
    String authorization;

    @BeforeEach
    @Tag("schema")
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValidoAdminTrue()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
        authorization = loginClient.realizarLogin(LoginDataFactory.loginValido(usuario)).path("authorization");

        produtoId = produtoClient.cadastrarProduto(ProdutoDataFactory.produtoValido(),authorization).path("_id");
        produto = produtoClient.retornarProduto(produtoId);
        nomeProduto = produto.getNome();
        descricaoProduto = produto.getDescricao();
        precoProduto = produto.getPreco();
        quantidadeProduto = produto.getQuantidade();
    }

//    @Test
//    @Tag("schema")
//    public void testSchemaListarProdutoVariavelSeparandoPorVirgulaComSucesso() {
//
//        String variaveis = String.valueOf(precoProduto) + "," + descricaoProduto + "," + nomeProduto;
//        produtoClient.listarProdutoDiferentesQuery("preco,descricao,nome", variaveis)
//        .then()
//                .statusCode(200)
//                .body(matchesJsonSchemaInClasspath("resgatar_produto_lista.json"))
//        ;
//    }

    @Test
    public void testListarProdutoVariavelSeparandoPorVirgulaComSucesso() {

        String variaveis = String.valueOf(precoProduto) + "," + descricaoProduto + "," + nomeProduto;
        produtoClient.listarProdutoDiferentesQuery("preco,descricao,nome", variaveis)
        .then()
                .statusCode(200)
        ;
    }

    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoResgatarListaInvalidoDataProvider")
    public void testListarProdutoInvalidoSemSucesso(String query, String parametro, String campo, int valor) {

        Response response = produtoClient.listarProdutoDiferentesQuery(query, parametro)
        .then()
                .statusCode(200)
                .extract().response()
        ;

        int path = response.path(campo);
        Assertions.assertEquals(path, valor);
    }

    @Test
    @Tag("schema")
    public void testSchemaBuscarProdutoPorIDCOmSucesso(){
        produtoClient.buscarProduto(produtoId)
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("resgatar_produto_id.json"))

        ;
    }

    @Test
    public void testBuscarProdutoPorIDCOmSucesso(){
        produtoClient.buscarProduto(produtoId)
        .then()
                .statusCode(200)

        ;
    }

    @ParameterizedTest
    @MethodSource(PATH_PRODUTO_PROVIDER + "#produtoBuscarInvalidoDataProvider")
    public void testBuscarProdutoInvalidoPorIDSemSucesso(String param, String campo, String valor){
        produtoClient.buscarProduto(param)
                .then()
                .statusCode(400)
                .body(campo, equalTo(valor))
        ;
    }

    @AfterEach
    public void tearDown() {
        produtoClient.deletarProduto(produtoId, authorization);
        usuarioClient.deletarUsuarios(usuarioId);
    }
}
