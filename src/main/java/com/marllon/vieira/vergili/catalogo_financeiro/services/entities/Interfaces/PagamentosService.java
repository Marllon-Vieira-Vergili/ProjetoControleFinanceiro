package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;

import java.math.BigDecimal;
import java.util.List;

public interface PagamentosService {


    /**
     * Interface para representar os métodos separadamente de acordo com cada entidade representada,
     * neste caso, todos os métodos representados somente a entidade ("Pagamento")
     *
     */

    //Criar (novo pagamento)

    Pagamentos criarNovoPagamento(PagamentosRequest pagamento);

    //Ler
    Pagamentos encontrarPagamentoPorId(Long id);
    List<Pagamentos> encontrarTodosPagamentos();
    List<Pagamentos> encontrarPagamentoPorValor(BigDecimal valor);


    //Atualizar
    Pagamentos atualizarPagamento(Long id, PagamentosRequest pagamento);

    //Remover
    Pagamentos removerPagamentoPorId(Long id);


}
