package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

public class UsuarioNaoEncontrado extends RuntimeException {
    public UsuarioNaoEncontrado(String message) {
        super("Este usuário não foi encontrado");
    }
}
