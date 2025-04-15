package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosResponse;


import java.util.List;

public interface PagamentosInterface {


    //Interface para criação dos Métodos CRUDS SEPARADAMENTE, um por entidade

    //Criar (novo pagamento)

    PagamentosResponse criarNovoPagamento(PagamentosRequest pagamento);

    //Ler
    PagamentosResponse encontrarPagamentoPorId(Long id);
    List<PagamentosResponse> encontrarTodosPagamentos();
    PagamentosResponse encontrarPagamentoPorDescricao(String descricao);

    //Atualizar
    //PagamentosResponse atualizarPagamento(Long id, PagamentosRequest pagamento);

    //Remover
    PagamentosResponse removerPagamentoPorId(Long id);
    PagamentosResponse removerPagamentoPorDescricao(String descricao);
}
