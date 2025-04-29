package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;

import java.math.BigDecimal;


public interface PagamentosAssociation {


    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Consulta o valor total dos pagamentos de uma conta.
     *
     * @param contaId ID da conta.
     * @return Valor total dos pagamentos.
     */
    BigDecimal consultarValorPagamento(Long contaId);

    /**
     * Verifica se o pagamento é uma despesa.
     *
     * @param pagamentoId ID do pagamento.
     * @return true se for despesa.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForDespesa(Long pagamentoId);

    /**
     * Verifica se o pagamento é uma receita.
     *
     * @param pagamentoId ID do pagamento.
     * @return true se for receita.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForReceita(Long pagamentoId);

    // ======================== MÉTODOS DE ASSOCIAÇÃO E DESASSOCIAÇÃO ========================

    /**
     * Associa um pagamento a um usuário.
     *
     * @param pagamentoId ID do pagamento.
     * @param usuarioId ID do usuário.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoComUsuario(Long pagamentoId, Long usuarioId);

    /**
     * Associa um pagamento a uma transação.
     *
     * @param pagamentoId ID do pagamento.
     * @param transacaoId ID da transação.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoATransacao(Long pagamentoId, Long transacaoId);

    /**
     * Associa um pagamento a uma conta.
     *
     * @param pagamentoId ID do pagamento.
     * @param contaId ID da conta.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoComConta(Long pagamentoId, Long contaId);

    /**
     * Associa um pagamento a uma categoria.
     *
     * @param pagamentoId ID do pagamento.
     * @param categoriaId ID da categoria.
     * @throws AssociationErrorException em caso de falha na associação.
     * @throws SubTipoNaoEncontrado caso o subtipo não combine com a categoria.
     */
    void associarPagamentoComCategoria(Long pagamentoId, Long categoriaId);

    /**
     * Desassocia um pagamento de um usuário.
     *
     * @param pagamentoId ID do pagamento.
     * @param usuarioId ID do usuário.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoUsuario(Long pagamentoId, Long usuarioId);

    /**
     * Desassocia um pagamento de uma transação.
     *
     * @param pagamentoId ID do pagamento.
     * @param transacaoId ID da transação.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoDeTransacao(Long pagamentoId, Long transacaoId);

    /**
     * Desassocia um pagamento de uma conta.
     *
     * @param pagamentoId ID do pagamento.
     * @param contaId ID da conta.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoConta(Long pagamentoId, Long contaId);

    /**
     * Desassocia um pagamento de uma categoria.
     *
     * @param pagamentoId ID do pagamento.
     * @param categoriaId ID da categoria.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoCategoria(Long pagamentoId, Long categoriaId);


}
