package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc;

public class HistoricoTransacaoNaoEncontrado extends RuntimeException {
    public HistoricoTransacaoNaoEncontrado(String message) {
        super("Esse histórico de transação não foi encontrado");
    }
}
