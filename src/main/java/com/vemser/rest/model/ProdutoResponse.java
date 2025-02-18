package com.vemser.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    @JsonProperty("_id")
    private String id;
    private String nome;
    private Integer preco;
    private String descricao;
    private Integer quantidade;

}
