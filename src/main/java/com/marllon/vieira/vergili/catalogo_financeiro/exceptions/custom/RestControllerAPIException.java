package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom;

public class RestControllerAPIException extends RuntimeException {
    public RestControllerAPIException(){
        super("Erro na chamada da API rest, para realizar a função");
    }

}
