package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;


/**
Record para o usuário, ao enviar uma requisicao pela pela entidade de
Usuario.
Irá solicitar que ele coloque o nome de seu usuário, seu email, sua senha, seu telefone..
(Somente para envio de dados pela Entidade)
 */

public record UsuarioRequest(String nome, String email, String senha, String telefone) {
}


