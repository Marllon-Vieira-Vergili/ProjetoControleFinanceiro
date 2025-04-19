package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities;


import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;

/**Record que irá retornar os dados da categoria ao usuário, quando ele inserir todos os dados
 *
 * @param id
 * @param tipoCategoria
 */
public record CategoriaFinanceiraResponse(Long id, TiposCategorias tipoCategoria, SubTipoCategoria subTipo) {
}
