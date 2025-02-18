package com.vemser.rest.client;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Login;
import com.vemser.rest.model.Usuario;
import com.vemser.rest.utils.Utils;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LoginClient extends BaseClient {

    private final String LOGIN = "/login";


    public Response realizarLogin(Login login){
        return
                given()
                        .spec(super.set())
                        .body(login)
                .when()
                        .post(LOGIN)
                ;
    }

    public String[] retornarAuthorizationFalse(){
        Utils.ResultAdminFalse result = Utils.getResult();

        Response response =
                given()
                        .spec(super.set())
                        .body(result.login())
                .when()
                        .post(LOGIN)
                ;

        return new String[] {response.path("authorization"), result.usuarioIdAminFalse()};
    }



}
