package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom;

public class AssociationErrorException extends RuntimeException {
    public AssociationErrorException(String message) {
        super("Erro ao realizar a associação!");
    }
}
