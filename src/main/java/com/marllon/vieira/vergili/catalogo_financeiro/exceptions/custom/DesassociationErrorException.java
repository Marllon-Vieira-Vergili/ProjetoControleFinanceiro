package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom;

public class DesassociationErrorException extends RuntimeException {
    public DesassociationErrorException(String message) {
        super("Erro ao realizar a associação!");
    }
}
