package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;


public interface UsuariosAssociation {


    // ======================== MÉTODOS DE ASSOCIAÇÃO E DESASSOCIAÇÃO ========================

    /**
     * Associa um usuário a um pagamento.
     *
     * @param usuarioId ID do usuário
     * @param pagamentoid ID do pagamento
     * @throws AssociationErrorException em caso de falha na associação
     */
    void associarUsuarioComPagamento(Long usuarioId, Long pagamentoid);

    /**
     * Associa um usuário a uma transação.
     *
     * @param usuarioId ID do usuário
     * @param transacaoId ID da transação
     * @throws AssociationErrorException em caso de falha na associação
     */
    void associarUsuarioComTransacoes(Long usuarioId, Long transacaoId);

    /**
     * Associa um usuário a uma conta.
     *
     * @param usuarioId ID do usuário
     * @param contaId ID da conta
     * @throws AssociationErrorException em caso de falha na associação
     */
    void associarUsuarioComConta(Long usuarioId, Long contaId);

    /**
     * Associa um usuário a uma categoria financeira.
     *
     * @param usuarioId ID do usuário
     * @param categoriaId ID da categoria
     * @throws AssociationErrorException em caso de falha na associação
     */
    void associarUsuarioComCategoria(Long usuarioId, Long categoriaId);

    /**
     * Remove a associação entre um usuário e um pagamento.
     *
     * @param usuarioId ID do usuário
     * @param pagamentoId ID do pagamento
     * @throws DesassociationErrorException em caso de erro na desassociação
     */
    void desassociarUsuarioComPagamento(Long usuarioId, Long pagamentoId);

    /**
     * Remove a associação entre um usuário e uma transação.
     *
     * @param usuarioId ID do usuário
     * @param transacaoId ID da transação
     * @throws DesassociationErrorException em caso de erro na desassociação
     */
    void desassociarUsuarioComTransacao(Long usuarioId, Long transacaoId);

    /**
     * Remove a associação entre um usuário e uma conta.
     *
     * @param usuarioId ID do usuário
     * @param contaId ID da conta
     * @throws DesassociationErrorException em caso de erro na desassociação
     */
    void desassociarUsuarioComConta(Long usuarioId, Long contaId);

    /**
     * Remove a associação entre um usuário e uma categoria.
     *
     * @param usuarioId ID do usuário
     * @param categoriaId ID da categoria
     * @throws DesassociationErrorException em caso de erro na desassociação
     */
    void desassociarUsuarioComCategoria(Long usuarioId, Long categoriaId);


}
