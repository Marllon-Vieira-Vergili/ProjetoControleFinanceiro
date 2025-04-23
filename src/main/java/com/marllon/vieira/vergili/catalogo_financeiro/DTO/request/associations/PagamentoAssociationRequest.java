package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;

/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Pagamentos")
 * e seus respectivos relacionamentos
 * Historico de pagamento não faz sentido vir aqui! ele só irá armazenar os valores!
 */

public record PagamentoAssociationRequest(PagamentosRequest pagamento,
                                          SubTipoCategoria subTipoCategoria,
                                          String nomeContaAssociada) {
}


