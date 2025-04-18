package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.HistoricoTAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.HistoricoTAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IHistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IHistoricoTransacaoImplement implements IHistoricoTransacao {

    @Autowired
    private TransacoesService transacoesService;

    @Override
    public HistoricoTAssociationResponse criarEAssociarHistoricoTransacao(HistoricoTAssociationRequest novoHistorico) {
        return null;
    }

    @Override
    public HistoricoTAssociationResponse encontrarHistoricoTransacaoAssociadaPorId(Long id) {
        return null;
    }

    @Override
    public List<HistoricoTAssociationResponse> encontrarTodosHistoricosTransacoesAssociados() {
        return List.of();
    }

    @Override
    public HistoricoTAssociationResponse atualizarHistoricoTransacaoAssociado(Long id, HistoricoTAssociationRequest atualizarHistorico) {
        return null;
    }

    @Override
    public HistoricoTAssociationResponse removerHistoricoTransacaoAssociadoPelaId(Long id) {
        return null;
    }
}
