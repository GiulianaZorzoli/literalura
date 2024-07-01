package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoApi(
        @JsonAlias("count") Integer cantLibros,
        @JsonAlias("next") String proximaPagina,
        @JsonAlias("previous") String anteriorPagina,
        @JsonAlias("results") List<DatoLibro> resultado
){

}
