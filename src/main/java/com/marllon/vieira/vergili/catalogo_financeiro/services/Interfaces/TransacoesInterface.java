package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.HistoricoTransacoesRequest;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacoesResponse;

import java.util.List;

public interface TransacoesInterface {

    //Interface para criação dos Métodos CRUDS SEPARADAMENTE, um por entidade


    //Criar (nova transação)
    HistoricoTransacoesResponse criarNovoHistoricoTransacao(HistoricoTransacoesRequest historicoTransacao);

    //Ler
    HistoricoTransacoesResponse encontrarTransacaoPorId(Long id);
    List<HistoricoTransacoesResponse> encontrarTodasTransacoes();
    HistoricoTransacoesResponse encontrarTransacaoPorNome(String nome);

    //Atualizar
    //HistoricoTransacoesResponse atualizarHistoricoTransacao(Long id, HistoricoTransacoesRequest historicoTransacao);

    //Remover
    HistoricoTransacoesResponse removerTransacaoPorId(Long id);
   HistoricoTransacoesResponse removerTransacaoPorNome(String nome);
}
