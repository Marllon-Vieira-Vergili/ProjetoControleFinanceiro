package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.HistoricoTAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.HistoricoTAssociationResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface para criar os métodos de associações de acordo com cada tipo de relacionamento
 * que essa entidade("HistoricoTransacao") possuir com as demais
 */

public interface IHistoricoTransacao {




        //Ler
        HistoricoTAssociationResponse encontrarHistoricoTransacaoAssociadaPorId(Long id);
        List<HistoricoTAssociationResponse> encontrarTodosHistoricosTransacoesAssociados();
        List<HistoricoTAssociationResponse> encontrarHistoricoTransacaoPorData(LocalDate data);
        List<HistoricoTAssociationResponse> encontrarHistoricosPorValor(BigDecimal valor);
    }

