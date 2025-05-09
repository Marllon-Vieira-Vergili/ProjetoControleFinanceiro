package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request;

import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;


/**Record para o usuário, ao enviar uma requisicao pela categoria de contas,
Irá solicitar que ele coloque o tipo de categoria, e a descrição da mesma.
 (Somente para envio de dados pela Entidade)
 */
public record CategoriaFinanceiraRequest(@Enumerated(EnumType.STRING)
                                         @NotNull(message = "O campo tipo de categoria não pode ser nulo")
                                         TiposCategorias tipoCategoria,
                                         @Enumerated(EnumType.STRING)
                                         @NotNull(message = "O campo tipo de categoria não pode ser nulo")
                                         SubTipoCategoria subtipo) {
}

