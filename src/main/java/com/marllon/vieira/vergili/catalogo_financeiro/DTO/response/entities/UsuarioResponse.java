package com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities;


/**Record que irá retornar os retorno dos dados do usuário, quando ele inserir todos os dados
 *
 * @param id
 * @param nome
 * @param email
 * @param senha
 * @param telefone
 */

public record UsuarioResponse(Long id, String nome, String email, String senha, String telefone) {
}
