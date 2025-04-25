package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc;

public class PagamentoNaoEncontrado extends RuntimeException {
    public PagamentoNaoEncontrado(String message) {
        super("Este pagamento não foi encontrado");
    }
}
