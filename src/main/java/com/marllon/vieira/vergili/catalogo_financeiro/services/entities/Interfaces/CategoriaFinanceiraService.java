package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;

import java.util.List;
import java.util.Optional;

/**
 * Interface para representar os métodos separadamente de acordo com cada entidade representada,
 * neste caso, todos os métodos representados somente a entidade ("Categoria Financeira")
 *
 */

public interface CategoriaFinanceiraService {

    // Criar
    CategoriaFinanceira criarCategoria(CategoriaFinanceiraRequest categoria);

    // Ler
    CategoriaFinanceira encontrarCategoriaPorId(Long id);
    List<TiposCategorias>encontrarTodasCategorias();
    List<SubTipoCategoria> encontrarCategoriasPorTipo(TiposCategorias tipo);
    CategoriaFinanceira encontrarCategoriaETipo(TiposCategorias tipoCategoria, SubTipoCategoria subTipoCategoria);

    // Atualizar
    CategoriaFinanceira atualizarValoresCategoria(Long id, CategoriaFinanceiraRequest categoria);

    // Remover
    CategoriaFinanceira removerCategoria(Long id);



    //Método para salvar em outras entidades
    void salvarNovaCategoria(CategoriaFinanceira novaCategoria);

}
