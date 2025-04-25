package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response;


import jakarta.validation.constraints.NotNull;

/**Record que irá retornar os retorno dos dados do usuário, quando ele inserir todos os dados
 *
 * @param id
 * @param nome
 * @param email
 * @param telefone
 */

public record UsuarioResponse(@NotNull
                              Long id,
                              String nome,
                              String email,
                              String telefone) {
}


