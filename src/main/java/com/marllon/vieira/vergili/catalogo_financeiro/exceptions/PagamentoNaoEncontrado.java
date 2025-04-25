package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

public class PagamentoNaoEncontrado extends RuntimeException {
    public PagamentoNaoEncontrado(String message) {
        super("Este pagamento não foi encontrado");
    }
}
