package com.vemser.rest.client;

import com.vemser.rest.model.Produto;
import com.vemser.rest.utils.Utils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

public class ProdutoClient extends BaseClient{

    private final String PRODUTOS = "/produtos";
    private final String ID = "/{_id}";

    LoginClient login = new LoginClient();

    public Response cadastrarProduto(Produto produto, String token){


        return
                given()
                        .spec(super.set())
                        .body(produto)
                        .header("Authorization", token)
                .when()
                        .post(PRODUTOS)
                ;
    }

    public Response deletarProduto(String id, String token){
        return
                given()
                        .spec(super.set())
                        .pathParam(Utils.removeChaves(ID), id)
                        .header("Authorization", token)
                .when()
                        .delete(PRODUTOS + ID)
                ;
    }

    public Response atualizarProduto(String id, Produto produto, String token){
        return
                given()
                        .spec(super.set())
                        .pathParams(Utils.removeChaves(ID), id)
                        .body(produto)
                        .header("Authorization", token)
                .when()
                        .put(PRODUTOS + ID)
                ;
    }



    public Response listarProdutoDiferentesQuery(String tipo, String variavel){
        RequestSpecification given =
                given()
                        .spec(super.set());
                        Utils.logicaDaQueryProduto(tipo, variavel, given);
        return
                given
                .when()
                        .get(PRODUTOS)
                ;
    }


    public Response buscarProduto(String id){
        return
                given()
                        .spec(super.set())
                        .pathParams(Utils.removeChaves(ID), id)
                .when()
                        .get(PRODUTOS + ID)
                ;
    }

    public Produto retornarProduto(String id){
        Response response =
                given()
                        .spec(super.set())
                        .pathParams(Utils.removeChaves(ID), id)
                .when()
                        .get(PRODUTOS + ID)
                ;

        return Utils.getProduto(response);
    }



    public String buscarTodosProdutosRetornarNome(){
        Response response =
                given()
                        .spec(super.set())
                .when()
                        .get(PRODUTOS)
                ;

        return response.path("produtos[0].nome");
    }


}
