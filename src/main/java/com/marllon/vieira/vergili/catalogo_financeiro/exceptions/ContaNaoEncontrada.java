package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

public class ContaNaoEncontrada extends RuntimeException {
    public ContaNaoEncontrada(String message) {
        super("Esta Conta não foi encontrada");
    }
}
