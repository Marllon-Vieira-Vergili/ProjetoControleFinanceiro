package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.ContaUsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.ContaUsuarioAssociationResponse;

import java.util.List;

/**
 * Cria uma nova Conta de usuário e realiza as associações necessárias com usuario e outros dados relacionados.
 *
 */

public interface IContaUsuario {

    //Criar
    ContaUsuarioAssociationResponse criarEAssociarConta(ContaUsuarioAssociationRequest novoUsuario);

    //Ler
    ContaUsuarioAssociationResponse encontrarContaAssociadaPorId(Long id);
    List<ContaUsuarioAssociationResponse> encontrarTodasContasAssociadas();

    //Atualizar
    ContaUsuarioAssociationResponse atualizarContaAssociada(Long id, ContaUsuarioRequest contaAtualizada);


    //Deletar
    void removerContaAssociadaPelaId(Long id);
}
