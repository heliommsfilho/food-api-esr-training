package com.github.heliommsfilho.foodapi.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ERRO_INTERNO_SERVIDOR("Erro interno do servidor", "/erro-interno-servidor"),
    PARAMETRO_INVALIDO("Parâmetro Inválido", "/parametro-invalido"),
    MENSAGEM_INVALIDA("Mensagem inválida", "/mensagem-invalida"),
    RECURSO_NAO_ENCONTRADO("Recoruso não encontrado", "/recurso-nao-encontrado"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio");

    private String title;
    private String path;

    ProblemType(String title, String path) {
        this.title = title;
        this.path = "https://api.com"  + path;
    }
}
