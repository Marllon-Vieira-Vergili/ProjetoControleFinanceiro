package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

/**Record que irá retornar os retorno dos pagamentos, quando ele inserir todos os dados
 *
 * @param id
 * @param valor
 * @param data
 * @param descricao
 * @param categoria
 */
public record PagamentosResponse(Long id, BigDecimal valor, LocalDate data, String descricao, com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.@jakarta.validation.constraints.NotNull(message = "O campo de categoria não pode ser null!") TiposCategorias categoria) {
}
