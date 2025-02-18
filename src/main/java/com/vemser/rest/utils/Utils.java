package com.vemser.rest.utils;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Login;
import com.vemser.rest.model.Produto;
import com.vemser.rest.model.Usuario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {

    public static String removeChaves(String input) {
        int start = input.indexOf('{');
        int end = input.indexOf('}');
        if (start != -1 && end != -1 && start < end) {
            return input.substring(start + 1, end);
        }
        return "";
    }

    public static String[] separarPorVirgulas(String input) {
        if (input == null || input.isEmpty()) {
            return new String[0];
        }
        return input.split(",");
    }

    public static int removerParteDecimalEConverter(String valor) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("Valor não pode ser nulo ou vazio.");
        }
        if (valor.endsWith(".0")) {
            valor = valor.substring(0, valor.length() - 2);
        }
        return Integer.parseInt(valor);
    }

    public static void logicaDaQueryProduto(String tipo, String variavel, RequestSpecification requestSpec) {
        String[] listaTipo = Utils.separarPorVirgulas(tipo);
        String[] listaVariavel = Utils.separarPorVirgulas(variavel);


        for(int i = 0; i < listaTipo.length && i < listaVariavel.length; i++){
            if (listaTipo[i].equalsIgnoreCase("preco") || listaTipo[i].equalsIgnoreCase("quantidade")) {
                int variavelInteira = Utils.removerParteDecimalEConverter(listaVariavel[i]);
                requestSpec.queryParam(listaTipo[i], variavelInteira);
            } else {
                requestSpec.queryParam(listaTipo[i].trim(), listaVariavel[i].trim());
            }
        }
    }

    public static Produto getProduto(Response response) {
        Produto produto = new Produto();
        produto.setNome(response.path("nome"));
        produto.setDescricao(response.path("descricao"));
        produto.setPreco(response.path("preco"));
        produto.setQuantidade(response.path("quantidade"));
        return produto;
    }

    public static void logicaDaQueryCliente(String tipo, String variavel, RequestSpecification requestSpec) {
        String[] listaTipo = Utils.separarPorVirgulas(tipo);
        String[] listaVariavel = Utils.separarPorVirgulas(variavel);

        for(int i = 0; i < listaTipo.length && i < listaVariavel.length; i++){
            requestSpec.queryParam(listaTipo[i].trim(), listaVariavel[i].trim());
        }
    }

    public static Usuario getUsuario(Response response) {
        Usuario usuario = new Usuario();
        usuario.setNome(response.path("nome"));
        usuario.setEmail(response.path("email"));
        usuario.setPassword(response.path("password"));
        usuario.setAdministrador(response.path("administrador"));
        return usuario;
    }

    public static ResultAdminFalse getResult() {
        UsuarioClient usuarioClient = new UsuarioClient();
        String usuarioIdAminFalse = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValidoAdminFalse()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioIdAminFalse);
        Login login = new Login(usuario.getEmail(), usuario.getPassword());
        ResultAdminFalse result = new ResultAdminFalse(usuarioIdAminFalse, login);
        return result;
    }

    public record ResultAdminFalse(String usuarioIdAminFalse, Login login) {
    }
}
