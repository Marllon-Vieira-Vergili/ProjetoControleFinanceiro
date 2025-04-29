package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;


public interface HistoricoTransacaoAssociation {



    // ======================== MÉTODOS DE ASSOCIAÇÃO ========================

    /**
     * Associa um pagamento a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param pagamentoId identificador do pagamento.
     * @throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComPagamento(Long transacaoId, Long pagamentoId);

    /**
     * Associa uma conta a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param contaId identificador da conta.
     * @throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComConta(Long transacaoId, Long contaId);

    /**
     * Associa um usuário a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário.
     *@throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComUsuario(Long transacaoId, Long usuarioId);

    /**
     * Associa uma categoria financeira a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param categoriaId identificador da categoria financeira.
     *@throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComCategoria(Long transacaoId, Long categoriaId);


    // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================

    /**
     * Desassocia um pagamento de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário (caso necessário para validação).
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDePagamento(Long transacaoId, Long usuarioId);

    /**
     * Desassocia uma conta de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param contaId identificador da conta.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeConta(Long transacaoId, Long contaId);

    /**
     * Desassocia um usuário de uma transação.
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeUsuario(Long transacaoId, Long usuarioId);

    /**
     * Desassocia uma categoria financeira de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param categoriaId identificador da categoria.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeCategoria(Long transacaoId, Long categoriaId);



}
