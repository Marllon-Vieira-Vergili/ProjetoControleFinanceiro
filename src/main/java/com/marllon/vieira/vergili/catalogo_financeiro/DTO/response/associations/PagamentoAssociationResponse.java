package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;

import java.util.List;

/**
 * Records de associações para serem mostradas ao usuário. neste caso, record para receber "Pagamentos")
 * e seus respectivos relacionamentos
 */
public record PagamentoAssociationResponse(List<PagamentosResponse> pagamentosRelacionados,
                                           List<HistoricoTransacaoResponse> historicoTransacoesRelacionados,
                                           List<CategoriaFinanceiraResponse> categoriasRelacionadas,
                                           List<ContaUsuarioResponse> contaRelacionada) {
}
