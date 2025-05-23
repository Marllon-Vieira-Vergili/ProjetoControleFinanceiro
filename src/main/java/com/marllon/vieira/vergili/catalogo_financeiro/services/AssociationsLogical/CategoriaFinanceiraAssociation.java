package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;

public interface CategoriaFinanceiraAssociation {


    // ======================== MÉTODOS DE ASSOCIAÇÃO E DESASSOCIAÇÃO ========================

    /**
     * Associa uma categoria financeira a uma conta de usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param contaId O ID da conta de usuário.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComConta(Long categoriaId, Long contaId);

    /**
     * Associa uma categoria financeira a um pagamento.
     *
     * @param categoriaId O ID da categoria.
     * @param pagamentoId O ID do pagamento.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId);

    /**
     * Associa uma categoria financeira a uma transação.
     *
     * @param categoriaId O ID da categoria.
     * @param transacaoId O ID da transação.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComTransacao(Long categoriaId, Long transacaoId);

    /**
     * Associa uma categoria financeira a um usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param usuarioId O ID do usuário.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComUsuario(Long categoriaId, Long usuarioId);

    // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================

    /**
     * Desassocia uma categoria de uma conta de usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param contaId O ID da conta de usuário.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaAConta(Long categoriaId, Long contaId);

    /**
     * Desassocia uma categoria de um pagamento.
     *
     * @param categoriaId O ID da categoria.
     * @param pagamentoId O ID do pagamento.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId);

    /**
     * Desassocia uma categoria de uma transação.
     *
     * @param categoriaId O ID da categoria.
     * @param transacaoId O ID da transação.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId);

    /**
     * Desassocia uma categoria de um usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param usuarioId O ID do usuário.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId);



}
