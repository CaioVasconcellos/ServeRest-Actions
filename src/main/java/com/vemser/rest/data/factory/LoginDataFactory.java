package com.vemser.rest.data.factory;

import com.vemser.rest.model.Login;
import com.vemser.rest.model.Usuario;
import org.apache.commons.lang3.StringUtils;

public class LoginDataFactory {

    public static Login loginValido(Usuario usuario){
        Login login = new Login();
        String email = usuario.getEmail();
        String password = usuario.getPassword();

        login.setEmail(email);
        login.setPassword(password);

        return login;
    }

    public static Login loginSenhaInvalida(Usuario usuario){
        Login login = new Login();
        String email = usuario.getEmail();
        String password = "0";

        login.setEmail(email);
        login.setPassword(password);

        return login;
    }

    public static Login loginSenhaEmBranco(Usuario usuario){
        Login login = new Login();
        String email = usuario.getEmail();
        String password = StringUtils.EMPTY;

        login.setEmail(email);
        login.setPassword(password);

        return login;
    }
}
