package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;

import java.util.List;

public interface TransacoesInterface {

    /**Esta interface é para criação dos métodos CRUDS(CREATE, READ, UPDATE, DELETE) relacionado somente as
     * transacoes.
     *
     * A maioria dos métodos, retorna o DTO(Data Transfer Object) no valor
     *
     */


    //Criar (nova transação)
    HistoricoTransacaoResponse criarNovoHistoricoTransacao(HistoricoTransacaoRequest historicoTransacao);

    //Ler
    HistoricoTransacaoResponse encontrarTransacaoPorId(Long id);
    List<HistoricoTransacaoResponse> encontrarTodasTransacoes();
    HistoricoTransacaoResponse encontrarTransacaoPorNome(String nome);

    //Atualizar
    //HistoricoTransacaoResponse atualizarHistoricoTransacao(Long id, HistoricoTransacaoRequest historicoTransacao);

    //Remover
    HistoricoTransacaoResponse removerTransacaoPorId(Long id);
   HistoricoTransacaoResponse removerTransacaoPorNome(String nome);
}
