package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.PagamentosResponse;


import java.util.List;

public interface PagamentosInterface {


    /**Esta interface é para criação dos métodos CRUDS(CREATE, READ, UPDATE, DELETE) relacionado somente ao
     * Pagamento.
     *
     * A maioria dos métodos, retorna o DTO(Data Transfer Object) no valor
     *
     */

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
