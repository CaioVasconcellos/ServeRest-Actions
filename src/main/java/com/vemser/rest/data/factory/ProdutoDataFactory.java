package com.vemser.rest.data.factory;

import com.github.javafaker.Faker;
import com.vemser.rest.client.ProdutoClient;
import com.vemser.rest.model.Produto;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ProdutoDataFactory {

    static Faker faker = new Faker(new Locale("PT-BR"));
    static ProdutoClient produtoClient = new ProdutoClient();

    public static Produto produtoValido(){
        return novoProduto();
    }

    public static Produto atualizarPrecoProdutoValido(){
        Produto produto = produtoValido();
        produto.setPreco(faker.number().randomDigitNotZero());
        return produto;
    }


    public static Produto produtoComNomeRepetido(){
        Produto produto = produtoValido();
        produto.setNome(produtoClient.buscarTodosProdutosRetornarNome());
        return produto;
    }

    public static Produto cadastrarProdutoComNomeVazio(){
        Produto produto = produtoValido();
        produto.setNome(StringUtils.EMPTY);
        return produto;
    }

    public static Produto cadastrarProdutoComDescricaoVazio(){
        Produto produto = produtoValido();
        produto.setDescricao(StringUtils.EMPTY);
        return produto;
    }

    public static Produto cadastrarProdutoComPrecoNaoNumerico(){
        Produto produto = produtoValido();
        produto.setPreco(null);
        return produto;
    }

    public static Produto cadastrarProdutoComQuantidadeNaoNumerico(){
        Produto produto = produtoValido();
        produto.setQuantidade(null);
        return produto;
    }




    private static Produto novoProduto(){
        Produto produto = new Produto();
        produto.setNome("ABC" + faker.commerce().productName());
        produto.setPreco(faker.number().randomDigitNotZero());
        produto.setDescricao(faker.lorem().characters());
        produto.setQuantidade(faker.number().randomDigitNotZero());

        return produto;
    }
}
