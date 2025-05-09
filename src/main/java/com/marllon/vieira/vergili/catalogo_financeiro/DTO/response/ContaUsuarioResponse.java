package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;


import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**Record que irá retornar os dados da conta ao usuário, quando ele inserir todos os dados
 *
 */
public record ContaUsuarioResponse(@NotNull
                                   Long id,
                                   String nome,
                                   BigDecimal saldo,
                                   TiposContas tiposContas) {
}

