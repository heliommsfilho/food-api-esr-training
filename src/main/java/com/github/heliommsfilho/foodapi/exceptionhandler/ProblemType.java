package com.github.heliommsfilho.foodapi.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INVALIDA("Mensagem inválida", "/mensagem-invalida"),
    ENTIDADE_NAO_ENCONTRADA("Entidade não encontrada", "/entidade-nao-encontrada"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio");

    private String title;
    private String path;

    ProblemType(String title, String path) {
        this.title = title;
        this.path = "https://api.com"  + path;
    }
}
