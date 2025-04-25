package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;

import java.math.BigDecimal;


/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Categoria")
 * e seus respectivos relacionamentos
 */

public record CategoriaFinanceiraAssociationRequest(CategoriaFinanceiraRequest categoria,
                                                    PagamentosRequest pagamento,
                                                    ContaUsuarioRequest conta) {
}
