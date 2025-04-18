package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.UsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.UsuarioAssociationResponse;

import java.util.List;

/**
 * Interface para criar os métodos de associações de acordo com cada tipo de relacionamento
 * que essa entidade("Usuário") possuir com as demais
 */

public interface IUsuario {

    //Criar
    UsuarioAssociationResponse criarEAssociarUsuario(UsuarioAssociationRequest novoUsuario);

    //Ler
    UsuarioAssociationResponse encontrarUsuarioAssociadoPorId(Long id);
    List<UsuarioAssociationResponse> encontrarTodosUsuariosAssociados();

    //Atualizar
    UsuarioAssociationResponse atualizarUsuarioAssociado(Long id, UsuarioAssociationRequest usuarioAtualizado);


    //Deletar
    UsuarioAssociationResponse removerUsuarioAssociadoPelaId(Long id);
}
