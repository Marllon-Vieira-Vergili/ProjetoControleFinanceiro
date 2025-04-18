package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;

/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Pagamentos")
 * e seus respectivos relacionamentos
 */

public record PagamentoAssociationRequest(PagamentosRequest pagamento) {
}
