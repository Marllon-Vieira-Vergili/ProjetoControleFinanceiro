package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import java.util.List;

/**
 * Records de associações para serem mostradas ao usuário. neste caso, record para receber "Usuarios")
 * e seus respectivos relacionamentos
 */
public record UsuarioAssociationResponse(UsuarioResponse usuarioRelacionado,
                                         ContaUsuarioResponse contaRelacionada,
                                         List<CategoriaFinanceiraResponse> categoriasRelacionadas,
                                         List<PagamentosResponse> pagamentosRelacionados,
                                         List<HistoricoTransacaoResponse> historicosTransacoesRelacionadas) {
}
