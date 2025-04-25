package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;


import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.validation.constraints.NotBlank;
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
