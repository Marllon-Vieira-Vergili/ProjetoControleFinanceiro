package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom;

public class JaExisteException extends RuntimeException {
    public JaExisteException(String message) {
        super("Já existe na base de dados, valores iguais aos informados");
    }
}
