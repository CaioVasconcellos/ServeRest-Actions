package com.vemser.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListaUsuarioResponse {
    private int quantidade;
    @JsonProperty("usuarios")
    private List<UsuarioResponse> usuarioResponse;

    public ListaUsuarioResponse() {
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<UsuarioResponse> getUsuarioResponse() {
        return usuarioResponse;
    }

    public void setUsuarioResponse(List<UsuarioResponse> usuarioResponse) {
        this.usuarioResponse = usuarioResponse;
    }

    @Override
    public String toString() {
        return "ListaUsuarioResponse{" +
                "quantidade=" + quantidade +
                ", usuarioResponse=" + usuarioResponse +
                '}';
    }
}


