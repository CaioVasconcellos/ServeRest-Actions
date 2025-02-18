package com.vemser.rest.client;

import com.vemser.rest.model.Usuario;
import com.vemser.rest.utils.Utils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

public class UsuarioClient extends BaseClient {

    private final String USUARIOS = "/usuarios";
    private final String ID = "/{_id}";


    public Response cadastrarUsuarios(Usuario usuario){

        return
                given()
                        .spec(super.set())
                        .body(usuario)
                .when()
                        .post(USUARIOS)
        ;
    }

    public Response atualizarUsuarios(String id, Usuario usuario){
        return
                given()
                        .spec(super.set())
                        .body(usuario)
                        .pathParams(Utils.removeChaves(ID), id)
                 .when()
                        .put(USUARIOS + ID)
                ;
    }

    public Response deletarUsuarios(String id){
        return
                given()
                        .spec(super.set())
                        .pathParams(Utils.removeChaves(ID), id)
                .when()
                        .delete(USUARIOS + ID)
                ;
    }

    public Response buscarTodosUsuario(){
        return
                given()
                        .spec(super.set())
                .when()
                        .get(USUARIOS)
                ;

    }

    public String buscarTodosUsuarioRetornarEmail(){
        Response response =
            given()
                    .spec(super.set())
            .when()
                    .get(USUARIOS)
            ;

        return response.path("usuarios[0].email");
    }

    public Response buscarUsuarioPathParam(String id){
        String idSemChave = Utils.removeChaves(ID);
        return
                given()
                        .spec(super.set())
                        .pathParams(idSemChave, id)
                .when()
                        .get(USUARIOS + ID)
                ;

    }

    public Usuario retornarUsuarioPathParam(String id){
        String idSemChave = Utils.removeChaves(ID);

        Response response =
            given()
                    .spec(super.set())
                    .pathParams(idSemChave, id)
            .when()
                    .get(USUARIOS + ID)
            ;
        return Utils.getUsuario(response);
    }



    public <T> Response listarUsuarioUmTipoQuery(String tipo, T valor){
        return
                given()
                        .spec(super.set())
                        .queryParam(tipo, valor)
                .when()
                        .get(USUARIOS)
                ;
    }

    public Response listarUsuarioVariasQuery(String tipo, String variavel){
        RequestSpecification given =
                given()
                        .spec(super.set());
                        Utils.logicaDaQueryCliente(tipo, variavel, given);
        return
                given
                .when()
                        .get(USUARIOS)
                ;
    }




}
