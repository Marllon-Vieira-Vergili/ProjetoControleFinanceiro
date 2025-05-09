package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;

import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**Record que irá retornar os dados do Histórico de transações, quando ele inserir todos os dados
 *
 * @param id
 * @param valor
 * @param data
 * @param descricao
 * @param categoria
 */

public record HistoricoTransacaoResponse(@NotNull
                                         Long id, BigDecimal valor, LocalDate data, String descricao,
                                         TiposCategorias categoria) {
}
