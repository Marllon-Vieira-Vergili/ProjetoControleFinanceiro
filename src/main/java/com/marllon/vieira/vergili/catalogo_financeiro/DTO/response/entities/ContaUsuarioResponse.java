package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities;


import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;

import java.math.BigDecimal;

/**Record que irá retornar os dados da conta ao usuário, quando ele inserir todos os dados
 *
 */
public record ContaUsuarioResponse(Long id,
                                   String nome,
                                   BigDecimal saldo,
                                   TiposContas tiposContas) {
}

