package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.UsuarioResponse;

import java.util.List;

/**
 * Records de associações para serem mostradas ao usuário. neste caso, record para receber "Categorias")
 * e seus respectivos relacionamentos
 */
public record CategoriaFinanceiraAssociationResponse(List<CategoriaFinanceiraResponse> categoriasRelacionadas,
                                                     List<PagamentosResponse> pagamentosRelacionados,
                                                     List<HistoricoTransacaoResponse> historicoTransacoesRelacionados,
                                                     ContaUsuarioAssociationResponse contaRelacionada,
                                                     UsuarioResponse usuarioRelacionado) {
}
