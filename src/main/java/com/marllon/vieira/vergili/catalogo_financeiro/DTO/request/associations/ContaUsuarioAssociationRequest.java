package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;

/**
 * Records de associações para serem enviadas como parâmetro, pelo usuário. neste caso, record para enviar "Conta Usuário")
 * e seus respectivos relacionamentos
 */

public record ContaUsuarioAssociationRequest(ContaUsuarioRequest dadosUsuario) {
}
