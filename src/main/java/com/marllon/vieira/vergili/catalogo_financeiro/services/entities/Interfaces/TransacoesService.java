package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacoesService {
    /**
     * Interface para representar os métodos separadamente de acordo com cada entidade representada,
     * neste caso, todos os métodos representados somente a entidade ("Histirico de Transação")
     *
     */

    //Criar (nova transação)
    HistoricoTransacao criarNovoHistoricoTransacao(HistoricoTransacaoRequest historicoTransacao);

    //Ler
    HistoricoTransacao encontrarTransacaoPorId(Long id);
    List<HistoricoTransacao> encontrarTodasTransacoes();
    List<HistoricoTransacao> encontrarTransacaoPorValor(BigDecimal valor);
    List<HistoricoTransacao> encontrarTransacaoPorData(LocalDate data);


    //Atualizar
    HistoricoTransacao atualizarHistoricoTransacao(Long id, HistoricoTransacaoRequest historicoTransacao);

    //Remover
    HistoricoTransacao removerTransacaoPorId(Long id);


}
