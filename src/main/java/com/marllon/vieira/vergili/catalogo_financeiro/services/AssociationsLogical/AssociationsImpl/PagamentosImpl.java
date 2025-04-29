package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;

public class PagamentosImpl implements CategoriaFinanceiraAssociation {


    @Override
    public void associarCategoriaComConta(Long categoriaId, Long contaId) {

    }

    @Override
    public void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId) {

    }

    @Override
    public void associarCategoriaComTransacao(Long categoriaId, Long transacaoId) {

    }

    @Override
    public void associarCategoriaComUsuario(Long categoriaId, Long usuarioId) {

    }

    @Override
    public void associarSubTipoCategoriaComDespesa(TiposCategorias tipoCategoriaDespesa, SubTipoCategoria subTipoDespesa) {

    }

    @Override
    public void associarSubTipoCategoriaComReceita(TiposCategorias tipoCategoriaReceita, SubTipoCategoria subTipoReceita) {

    }

    @Override
    public void desassociarCategoriaAConta(Long categoriaId, Long contaId) {

    }

    @Override
    public void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId) {

    }

    @Override
    public void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId) {

    }

    @Override
    public void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId) {

    }

    @Override
    public void desassociarCategoriaCriadaComDespesa(Long categoriaId) {

    }

    @Override
    public void desassociarCategoriaCriadaComReceita(Long categoriaId) {

    }
}
