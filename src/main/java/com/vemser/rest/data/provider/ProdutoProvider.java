package com.vemser.rest.data.provider;

import com.vemser.rest.client.LoginClient;
import com.vemser.rest.data.factory.ProdutoDataFactory;
import org.junit.jupiter.params.provider.Arguments;


import java.util.stream.Stream;

import static com.vemser.rest.utils.ConstantProvider.*;


public class ProdutoProvider {

    static public final String PATH_PRODUTO_PROVIDER = "com.vemser.rest.data.provider.ProdutoProvider";
    public static String tokenIdFalse;




    public static Stream<Arguments> produtoInvalidoStatus400DataProvider() {
        return Stream.of(
                Arguments.of(ProdutoDataFactory.produtoComNomeRepetido(), MESSAGE, NOME_REPETIDO),
                Arguments.of(ProdutoDataFactory.cadastrarProdutoComNomeVazio(), NOME, NOME_EM_BRANCO),
                Arguments.of(ProdutoDataFactory.cadastrarProdutoComPrecoNaoNumerico(), PRECO, PRECO_NULL),
                Arguments.of(ProdutoDataFactory.cadastrarProdutoComQuantidadeNaoNumerico(), QUANTIDADE, QUANTIDADE_NULL),
                Arguments.of(ProdutoDataFactory.cadastrarProdutoComDescricaoVazio(), DESCRICAO, DESCRICAO_EM_BRANCO)
        );
    }

    public static Stream<Arguments> produtoTokenInvalidoDataProvider() {
        LoginClient loginClient = new LoginClient();
        String[] authorizationAndID = loginClient.retornarAuthorizationFalse();
        String authorization = authorizationAndID[0];
        tokenIdFalse = authorizationAndID[1];
        return Stream.of(
                Arguments.of(ProdutoDataFactory.produtoValido(), "", MESSAGE, TOKEN_INVALIDO, 401),
                Arguments.of(ProdutoDataFactory.produtoValido(), "Bearer 0", MESSAGE, TOKEN_INVALIDO, 401),
                Arguments.of(ProdutoDataFactory.produtoValido(), authorization, MESSAGE, TOKEN_ADMIN_FALSE, 403)
        );
    }

    public static Stream<Arguments> produtoResgatarListaInvalidoDataProvider() {
        return Stream.of(
                Arguments.of(ID, ID_INVALIDO, "quantidade", 0),
                Arguments.of(ID, ID_ZERO, "quantidade", 0),
                Arguments.of(QUERY_NOME, NOME_INVALIDO, "quantidade", 0)
        );
    }

    public static Stream<Arguments> produtoBuscarInvalidoDataProvider() {
        return Stream.of(
                Arguments.of(ID_INVALIDO, MESSAGE, ID_PRODUTO_INVALIDO),
                Arguments.of(ID_ZERO, MESSAGE, ID_PRODUTO_INVALIDO));
    }

    public static Stream<Arguments> produtoDeletarInvalidoDataProvider() {
        return Stream.of(
                Arguments.of(ID_INVALIDO, MESSAGE, DELETAR_PRODUTO_INVALIDO, 200),
                Arguments.of(ID_ZERO, MESSAGE, DELETAR_PRODUTO_INVALIDO, 200),
                Arguments.of(ID_PRODUTO_NO_CARRINHO, MESSAGE, PRODUTO_NO_CARRINHO, 400)
        );
    }


}



