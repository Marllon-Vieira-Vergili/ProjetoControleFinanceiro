package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.ContaUsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.ContaUsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IContaUsuarioImplement implements IContaUsuario {

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public ContaUsuarioAssociationResponse criarEAssociarConta(ContaUsuarioAssociationRequest novoUsuario) {
        return null;
    }

    @Override
    public ContaUsuarioAssociationResponse encontrarContaAssociadaPorId(Long id) {
        return null;
    }

    @Override
    public List<ContaUsuarioAssociationResponse> encontrarTodasContasAssociadas() {
        return List.of();
    }

    @Override
    public ContaUsuarioAssociationResponse atualizarContaAssociada(Long id, ContaUsuarioAssociationRequest contaAtualizada) {
        return null;
    }

    @Override
    public ContaUsuarioAssociationResponse removerContaAssociadaPelaId(Long id) {
        return null;
    }
}