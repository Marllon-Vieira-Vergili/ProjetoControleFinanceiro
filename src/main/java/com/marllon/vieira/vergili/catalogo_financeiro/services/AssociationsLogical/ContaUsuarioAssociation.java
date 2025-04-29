package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;

public interface ContaUsuarioAssociation {




    // ======================== MÈTODOS DE ASSOCIAÇÂO E DESASSOCIAÇÂO DESSA ENTIDADE ========================

    /**
     * Associa um tipo de conta (enum) à conta do usuário.
     *
     * @param contaId     identificador da conta.
     * @param tiposConta  tipo de conta a ser associado.
     * @throws AssociationErrorException se a conta não for encontrada ou se o tipo já estiver associado.
     */
    void associarTipoConta(Long contaId, TiposContas tiposConta);

    /**
     * Associa uma categoria financeira a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador da categoria financeira.
     * @throws AssociationErrorException se a conta ou categoria não forem encontradas,
     *                                   ou se a associação já existir.
     */
    void associarContaComCategoria(Long contaId, Long categoriaId);

    /**
     * Associa um pagamento a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param pagamentoId identificador do pagamento.
     * @throws AssociationErrorException se a conta ou pagamento não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComPagamentos(Long contaId, Long pagamentoId);

    /**
     * Associa uma transação a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param transacaoId identificador da transação.
     * @throws AssociationErrorException se a conta ou transação não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComTransacoes(Long contaId, Long transacaoId);

    /**
     * Associa um usuário à conta.
     *
     * @param contaId   identificador da conta.
     * @param usuarioId identificador do usuário.
     * @throws AssociationErrorException se a conta ou usuário não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComUsuario(Long contaId, Long usuarioId);


    // ================= DESASSOCIAÇÕES =================

    /**
     * Remove a associação entre uma conta e uma categoria financeira.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador da categoria.
     * @throws DesassociationErrorException se a conta ou categoria não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeCategorias(Long contaId, Long categoriaId);

    /**
     * Remove a associação entre uma conta e um pagamento.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador do pagamento (verificar se o nome está correto).
     * @throws DesassociationErrorException se a conta ou pagamento não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDePagamento(Long contaId, Long categoriaId); // categoriaId deve ser pagamentoId?

    /**
     * Remove a associação entre uma conta e um histórico de transação.
     *
     * @param contaId    identificador da conta.
     * @param historicoId identificador do histórico.
     * @throws DesassociationErrorException se a conta ou transação não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId);

    /**
     * Remove a associação entre uma conta e um tipo de conta
     *
     * @param contaid    identificador da conta.
     * @param tiposContas o tipo de conta encontrado
     * @throws DesassociationErrorException se a conta ou transação não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeTipoConta(Long contaid, TiposContas tiposContas);

    /**
     * Remove a associação entre uma conta e um usuário.
     *
     * @param contaId   identificador da conta.
     * @param usuarioId identificador do usuário.
     * @throws DesassociationErrorException se a conta ou usuário não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeUsuario(Long contaId, Long usuarioId);

}


