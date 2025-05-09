package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;

import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import jakarta.validation.constraints.NotNull;

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
public record PagamentosResponse(@NotNull
                                 Long id,
                                 BigDecimal valor,
                                 LocalDate data,
                                 String descricao,
                                 TiposCategorias categoria,
                                 SubTipoCategoria subTipoCategoria,
                                 Long contaAssociada,
                                 Long usuarioAssociado) {
}
