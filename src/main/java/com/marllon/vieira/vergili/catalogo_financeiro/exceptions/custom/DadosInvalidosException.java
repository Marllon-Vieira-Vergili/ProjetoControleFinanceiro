package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom;

public class DadosInvalidosException extends RuntimeException {
    public DadosInvalidosException(String message) {
        super("Você inseriu dados inválidos. Por favor, verifique os dados e tente novamente");
    }
}
