package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;

/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar
 * "Histórico de Transação") e seus respectivos relacionamentos
 */

public record HistoricoTAssociationRequest(HistoricoTransacaoRequest historicosTransacoes) {
}
