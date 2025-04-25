package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

public class CategoriaNaoEncontrada extends RuntimeException {
    public CategoriaNaoEncontrada(String message) {
        super("Esta Categoria não foi encontrada");
    }
}
