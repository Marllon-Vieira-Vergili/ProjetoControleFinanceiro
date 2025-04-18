package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.HistoricoTAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.HistoricoTAssociationResponse;

import java.util.List;

/**
 * Interface para criar os métodos de associações de acordo com cada tipo de relacionamento
 * que essa entidade("HistoricoTransacao") possuir com as demais
 */

public interface IHistoricoTransacao {


        //Criar
        HistoricoTAssociationResponse criarEAssociarHistoricoTransacao(HistoricoTAssociationRequest novoHistorico);

        //Ler
        HistoricoTAssociationResponse encontrarHistoricoTransacaoAssociadaPorId(Long id);
        List<HistoricoTAssociationResponse> encontrarTodosHistoricosTransacoesAssociados();

        //Atualizar
        HistoricoTAssociationResponse atualizarHistoricoTransacaoAssociado(Long id, HistoricoTAssociationRequest atualizarHistorico);


        //Deletar
        HistoricoTAssociationResponse removerHistoricoTransacaoAssociadoPelaId(Long id);
    }

