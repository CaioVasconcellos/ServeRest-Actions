package com.vemser.rest.model;

public class LoginResponse {
    String mensagem;
    String authorization;

    public LoginResponse() {
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "mensagem='" + mensagem + '\'' +
                ", authorization='" + authorization + '\'' +
                '}';
    }
}
