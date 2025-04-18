package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.UsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.UsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUsuarioImplement implements IUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UsuarioAssociationResponse criarEAssociarUsuario(UsuarioAssociationRequest novoUsuario) {
        return null;
    }

    @Override
    public UsuarioAssociationResponse encontrarUsuarioAssociadoPorId(Long id) {
        return null;
    }

    @Override
    public List<UsuarioAssociationResponse> encontrarTodosUsuariosAssociados() {
        return List.of();
    }

    @Override
    public UsuarioAssociationResponse atualizarUsuarioAssociado(Long id, UsuarioAssociationRequest usuarioAtualizado) {
        return null;
    }

    @Override
    public UsuarioAssociationResponse removerUsuarioAssociadoPelaId(Long id) {
        return null;
    }
}
