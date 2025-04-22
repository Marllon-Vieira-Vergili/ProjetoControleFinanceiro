package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.ContaUsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.ContaUsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IContaUsuarioImplement implements IContaUsuario {

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public ContaUsuarioAssociationResponse criarEAssociarConta(ContaUsuarioAssociationRequest novaConta) {

        //Verificando se já existe um usuário
        Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPorNome(novaConta.usuario().nome());

        //Criando uma nova conta
        if(usuarioEncontrado != null)
        //Associando a um usuário


        //Converter os valores para DTO

        //Retornar o DTO ao usuário
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