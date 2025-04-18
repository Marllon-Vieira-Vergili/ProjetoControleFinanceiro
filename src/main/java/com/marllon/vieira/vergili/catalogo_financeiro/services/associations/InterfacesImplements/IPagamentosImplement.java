package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPagamentosImplement implements IPagamentos {

    @Autowired
    private PagamentosService pagamentosService;


    @Override
    public PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento) {
        return null;
    }

    @Override
    public PagamentoAssociationResponse encontrarPagamentoAssociadoPorId(Long id) {
        return null;
    }

    @Override
    public List<PagamentoAssociationResponse> encontrarTodosPagamentosAssociados() {
        return List.of();
    }

    @Override
    public PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest pagamentoAtualizado) {
        return null;
    }

    @Override
    public PagamentoAssociationResponse removerPagamentoAssociadoPelaId(Long id) {
        return null;
    }
}
