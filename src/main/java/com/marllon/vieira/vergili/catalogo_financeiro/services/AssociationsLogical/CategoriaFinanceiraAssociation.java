package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
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

    /**
     * Associa um subtipo de categoria (ex: "Alimentação") a uma categoria do tipo **DESPESA**.
     *
     * @param tipoCategoriaDespesa somente aceito categoria despesa.
     * @param subTipoDespesa O subtipo de categoria (DESPESA).
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarSubTipoCategoriaComDespesa(TiposCategorias tipoCategoriaDespesa, SubTipoCategoria subTipoDespesa);

    /**
     * Associa um subtipo de categoria a uma categoria do tipo **RECEITA**.
     *
     * @param tipoCategoriaReceita o nome do tipo da categoria RECEITA.
     * @param subTipoReceita O subtipo de categoria (RECEITA).
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarSubTipoCategoriaComReceita(TiposCategorias tipoCategoriaReceita, SubTipoCategoria subTipoReceita);

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

    /**
     * Remove a associação de uma categoria com o tipo **DESPESA**.
     *
     * @param categoriaId O ID da categoria.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaCriadaComDespesa(Long categoriaId);

    /**
     * Remove a associação de uma categoria com o tipo **RECEITA**.
     *
     * @param categoriaId O ID da categoria.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaCriadaComReceita(Long categoriaId);

}
