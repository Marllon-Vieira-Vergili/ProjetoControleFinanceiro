package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;


import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import jakarta.validation.constraints.NotNull;

/**Record que irá retornar os dados da categoria ao usuário, quando ele inserir todos os dados
 *
 * @param id
 * @param tipoCategoria
 */
public record CategoriaFinanceiraResponse(@NotNull
                                          Long id,
                                          TiposCategorias tipoCategoria,
                                          SubTipoCategoria subTipo) {
}
