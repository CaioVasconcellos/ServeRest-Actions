package com.vemser.rest.tests.usuario;

import com.vemser.rest.client.UsuarioClient;
import com.vemser.rest.data.factory.UsuarioDataFactory;
import com.vemser.rest.model.Usuario;
import com.vemser.rest.model.ListaUsuarioResponse;
import com.vemser.rest.model.UsuarioResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class UsuariosGetTest {

    private static String usuarioId;
    private static String usuarioEmail;
    private static String usuarioPassword;
    private static String usuarioNome;
    private static String usuarioAdmin;
    UsuarioClient usuarioClient = new UsuarioClient();




    @BeforeEach
    public void setUp() {
        usuarioId = usuarioClient.cadastrarUsuarios(UsuarioDataFactory.usuarioValido()).path("_id");
        Usuario usuario = usuarioClient.retornarUsuarioPathParam(usuarioId);
        usuarioEmail = usuario.getEmail();
        usuarioPassword = usuario.getPassword();
        usuarioNome = usuario.getNome();
        usuarioAdmin = usuario.getAdministrador();
    }

    @Test
    @Tag("schema")
    public void testSchemaListarTodosUsuariosComSucesso() {
                usuarioClient.buscarTodosUsuario()
                .then()
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("todos_usuarios.json"))
                ;
    }

    @Test
    public void testListarTodosUsuariosComSucesso() {
        ListaUsuarioResponse response =
        usuarioClient.buscarTodosUsuario()
        .then()
                .statusCode(200)
                .extract()
                .as(ListaUsuarioResponse.class)
        ;

        Assertions.assertAll(
                () -> Assertions.assertNotNull(response.getUsuarioResponse().get(0).getNome()),
                () -> Assertions.assertNotNull(response.getUsuarioResponse().get(0).getEmail()),
                () -> Assertions.assertNotNull(response.getUsuarioResponse().get(0).getPassword()),
                () -> Assertions.assertNotNull(response.getUsuarioResponse().get(0).getAdministrador()),
                () -> Assertions.assertNotNull(response.getUsuarioResponse().get(0).getId())
        );
    }


    @Test
    public void testPatternListarTodosUsuariosComSucesso() {

        String nomePattern = "^[a-zA-Z0-9.-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$";
        String emailPattern = "^[a-zA-Z0-9._%+-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String idPattern = "^[A-Za-z0-9_]+$";

        ListaUsuarioResponse response =

         usuarioClient.buscarTodosUsuario()
        .then()
                .statusCode(200)
                .extract()
                .as(ListaUsuarioResponse.class)
        ;
        Assertions.assertTrue(response.getQuantidade() > 1);
        int quantidade = response.getQuantidade();
        for (int i = 0; i < quantidade; i++) {
            int contador = i;
            Assertions.assertAll(
                    () -> Assertions.assertTrue(response.getUsuarioResponse().get(contador).getNome().matches(nomePattern)),
                    () -> Assertions.assertTrue(response.getUsuarioResponse().get(contador).getEmail().matches(emailPattern)),
                    () -> Assertions.assertTrue(response.getUsuarioResponse().get(contador).getId().matches(idPattern))
            );
        }

    }

    @Test
    @Tag("schema")
    public void testSchemaListarUsuariosPorNomeComSucesso() {

        usuarioClient.listarUsuarioUmTipoQuery("nome", UsuarioDataFactory.usuarioValido().getNome())
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("usuarios_por_nome.json"))
        ;
    }

    @Test
    public void testListarUsuariosPorNomeComSucesso() {

        ListaUsuarioResponse response =
         usuarioClient.listarUsuarioUmTipoQuery("nome", usuarioNome)
        .then()
                .statusCode(200)
                .extract()
                .as(ListaUsuarioResponse.class);
        ;
        for (int i = 0; i < response.getQuantidade(); i++) {
            Assertions.assertEquals(usuarioNome, response.getUsuarioResponse().get(i).getNome());
        }
    }

    @Test
    public void testListarUsuariosVariavelComSucesso() {

        String variaveis = usuarioNome + "," + usuarioEmail + "," + usuarioAdmin;

        ListaUsuarioResponse response =
                usuarioClient.listarUsuarioVariasQuery("nome,email,administrador", variaveis)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(ListaUsuarioResponse.class);
        ;
        for (int i = 0; i < response.getQuantidade(); i++) {
            Assertions.assertEquals(usuarioNome, response.getUsuarioResponse().get(i).getNome());
        }
    }

    @Test
    public void testListarUsuariosPorEmailInvalidoSemSucesso() {

        usuarioClient.listarUsuarioUmTipoQuery("email", UsuarioDataFactory.usuarioEmailInvalido().getEmail())
        .then()
                .statusCode(400)
                .time(Matchers.lessThan(3000L))
                .body("email", containsStringIgnoringCase("email deve ser um email válido"))
        ;
    }

    @Test
    public void testListarUsuariosPorIDInvalidoSemSucesso() {


        usuarioClient.listarUsuarioUmTipoQuery("_id", UsuarioDataFactory.usuarioIdInvalidoZero())
        .then()
                .statusCode(200).body("quantidade", equalTo(0))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @Test
    public void testBuscarUsuariosPorIDComSucessoHamCrest() {


        usuarioClient.buscarUsuarioPathParam(usuarioId)
        .then()
                .header("Content-Type", "application/json; charset=utf-8")
                .time(Matchers.lessThan(3000L))
                .statusCode(200)
                .body("_id", equalTo(usuarioId))
                .body("email", containsStringIgnoringCase(usuarioEmail))
        ;
    }

    @Test
    public void testBuscarUsuariosPorIDComSucessoAssertions() {

        String idUsuario = usuarioId;

        UsuarioResponse response =
                usuarioClient.buscarUsuarioPathParam(idUsuario)
                .then()
                        .statusCode(200)
                        .extract()
                        .as(UsuarioResponse.class);
        ;

        Assertions.assertEquals(usuarioNome,response.getNome());
        Assertions.assertEquals(usuarioEmail,response.getEmail());
        Assertions.assertEquals(usuarioPassword,response.getPassword());
        Assertions.assertEquals(usuarioAdmin,response.getAdministrador());
        Assertions.assertNotNull(response.getId());
    }

    @Test
    public void testBuscarUsuariosPorIDComSucessoAssertionsAll() {

        String idUsuario = usuarioId;

        UsuarioResponse response =
                usuarioClient.buscarUsuarioPathParam(idUsuario)
                .then()
                        .statusCode(200)
                        .extract()
                        .as(UsuarioResponse.class)
                ;

        Assertions.assertAll(
                () -> Assertions.assertEquals(usuarioNome,response.getNome()),
                () -> Assertions.assertEquals(usuarioEmail,response.getEmail()),
                () -> Assertions.assertEquals(usuarioPassword,response.getPassword()),
                () -> Assertions.assertEquals(usuarioAdmin,response.getAdministrador())
        );

    }

    @Test
    @Tag("schema")
    public void testSchemaBuscarUsuariosPorID() {

        String idUsuario = usuarioId;

        usuarioClient.buscarUsuarioPathParam(idUsuario)
        .then()
                .body(matchesJsonSchemaInClasspath("usuarios_por_id.json"))
        ;
    }

    @Test
    public void testBuscarUsuariosPorIDInvalidoSemSucesso() {

        usuarioClient.buscarUsuarioPathParam(UsuarioDataFactory.usuarioIdInvalidoZero())
        .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @Test
    public void testBuscarUsuariosPorIDComCaixaAltaSemSucesso() {
        String idUsuario = usuarioId.toUpperCase();

        usuarioClient.buscarUsuarioPathParam(idUsuario)
        .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"))
                .time(Matchers.lessThan(3000L))
        ;
    }

    @AfterEach
    public void tearDown() {
        usuarioClient.deletarUsuarios(usuarioId);
    }
}
