package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import java.util.List;

/**
 * Interface de serviços para operações relacionadas à CategoriaFinanceira.
 * Métodos abrangem criação, leitura, atualização e remoção de categorias financeiras.
 * Inclui validação baseada no tipo de categoria (RECEITA ou DESPESA).
 */
public interface CategoriaFinanceiraService {

    // Criar
    CategoriaFinanceiraResponse criarCategoria(CategoriaFinanceiraRequest categoria);

    // Ler
    CategoriaFinanceiraResponse encontrarCategoriaPorId(Long id);
    List<CategoriaFinanceiraResponse> encontrarTodasCategorias();
    List<CategoriaFinanceiraResponse> encontrarCategoriasPorTipo(TiposCategorias tipo);

    // Atualizar
    CategoriaFinanceiraResponse atualizarCategoria(Long id, CategoriaFinanceiraRequest categoria);

    // Remover
    CategoriaFinanceiraResponse removerCategoria(Long id);
}
