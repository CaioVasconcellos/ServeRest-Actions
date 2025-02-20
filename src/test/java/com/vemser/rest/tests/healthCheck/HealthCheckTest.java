package com.vemser.rest.tests.healthCheck;

import com.vemser.rest.client.ProdutoClient;
import org.junit.jupiter.api.Test;

public class HealthCheckTest {

    ProdutoClient produtoClient = new ProdutoClient();


    @Test
    public void testHealthCheck(){
        if(true){
            System.out.println("Teste");
        }else{
            System.out.println("Teste");
        }
        produtoClient.listarProduto().then().statusCode(200);
    }
}
