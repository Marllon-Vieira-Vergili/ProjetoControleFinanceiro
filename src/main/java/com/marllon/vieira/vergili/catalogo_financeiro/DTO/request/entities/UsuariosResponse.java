package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;


//Record que irá retornar os retorno dos dados do usuário, quando ele inserir todos os dados
public record UsuariosResponse(Long id, String nome, String email, String senha, String telefone) {
}
