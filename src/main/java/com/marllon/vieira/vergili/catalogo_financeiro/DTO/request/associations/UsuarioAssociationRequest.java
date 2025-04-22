package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;

/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Usuarios")
 * e seus respectivos relacionamentos
 */
public record UsuarioAssociationRequest(UsuarioRequest usuario, ContaUsuarioRequest contaUsuario) {
}
