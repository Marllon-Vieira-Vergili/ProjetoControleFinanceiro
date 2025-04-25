package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;

import java.util.List;

/**
 * Interface para criar os métodos de associações de acordo com cada tipo de relacionamento
 * que essa entidade("Pagamentos") possuir com as demais
 */

public interface IPagamentos {



    //Criar
    PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento);
    PagamentoAssociationResponse criarEAssociarRecebimento(PagamentoAssociationRequest novoRecebimento);
    //Ler
    PagamentoAssociationResponse encontrarPagamentoAssociadoPorId(Long id);
    List<PagamentoAssociationResponse> encontrarTodosPagamentosAssociados();

    //Atualizar
    PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest pagamentoAtualizado);


    //Deletar
    void removerPagamentoAssociadoPelaId(Long id);
}
