package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;

import java.math.BigDecimal;


/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Categoria")
 * e seus respectivos relacionamentos
 */

public record CategoriaFinanceiraAssociationRequest(CategoriaFinanceiraRequest categoria, String nomeUsuario, String nomeConta,
                                                    BigDecimal valorPagamento, BigDecimal valorTransacao) {
}
