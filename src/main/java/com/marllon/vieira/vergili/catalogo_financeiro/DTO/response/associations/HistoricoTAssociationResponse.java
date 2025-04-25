package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;

import java.util.List;

/**
 * Records de associações para serem mostradas ao usuário. neste caso, record para receber "Historico de transações")
 * e seus respectivos relacionamentos
 */
public record HistoricoTAssociationResponse(List<HistoricoTransacaoResponse> historicoTransacoesRelacionados,
                                            List<PagamentosResponse> pagamentoRelacionados,
                                            List<CategoriaFinanceiraResponse> categoriasRelacionadas,
                                            List<ContaUsuarioResponse> contaRelacionada,
                                            List<UsuarioResponse> usuarioRelacionado) {
}
