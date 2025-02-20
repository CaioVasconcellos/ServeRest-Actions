package com.vemser.rest.data.factory;

import com.github.javafaker.Faker;
import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.model.Usuario;
import com.vemser.rest.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class UsuarioDataFactory {

    static Faker faker = new Faker(new Locale("PT-BR"));
    static UsuarioClient usuarioClient = new UsuarioClient();

    public static Usuario usuarioValido(){
        return novoUsuario();
    }

    public static Usuario usuarioValidoAdminTrue(){
        Usuario usuario = novoUsuario();
        usuario.setAdministrador("true");
        return usuario;
    }

    public static Usuario usuarioValidoAdminFalse(){
        Usuario usuario = novoUsuario();
        usuario.setAdministrador("false");
        return usuario;
    }

    public static Usuario usuarioSemEmail(){
        Usuario usuario = usuarioValido();
        usuario.setEmail(StringUtils.EMPTY);

        return usuario;
    }

    public static Usuario usuarioSemSenha(){
        Usuario usuario = usuarioValido();
        usuario.setPassword(StringUtils.EMPTY);

        return usuario;
    }

    public static Usuario usuarioEmailInvalido(){
        Usuario usuario = usuarioValido();
        String email = "emailinvalido.com";
        usuario.setEmail(email);
        return usuario;
    }

    public static Usuario usuarioEmailRepetido(){
        Usuario usuario = usuarioValido();
        String email = usuarioClient.buscarTodosUsuarioRetornarEmail();
        usuario.setEmail(email);
        return usuario;
    }

    public static Usuario atualizarNomeSenhaUsuario(Usuario usuario){
        usuario.setPassword(faker.internet().password());
        usuario.setNome(faker.name().fullName());
        return usuario;
    }

    public static Usuario atualizarNomeVazioUsuario(Usuario usuario){
        usuario.setNome(StringUtils.EMPTY);
        return usuario;
    }

    public static Usuario atualizarNomeExcessivamenteLongoUsuario(Usuario usuario){
        try {
            String caminhoArquivo = "src/test/resources/txt/NomeLongo";
            String nomeExcessivamenteLongo = Files.readString(Path.of(caminhoArquivo));
            usuario.setNome(nomeExcessivamenteLongo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

   public static String usuarioIdInvalidoZero() {
       return "0";
   }

    public static String usuarioIdCriadoComCarrinho() {
        return "0uxuPY0cbmQhpEz1";
    }


    private static Usuario novoUsuario(){

        Usuario usuario = new Usuario();
        usuario.setNome(faker.name() + faker.name().fullName());
        usuario.setEmail(faker.internet().emailAddress());
        usuario.setPassword(faker.internet().password());
        usuario.setAdministrador(String.valueOf(faker.bool().bool()));

        return usuario;
    }
}
