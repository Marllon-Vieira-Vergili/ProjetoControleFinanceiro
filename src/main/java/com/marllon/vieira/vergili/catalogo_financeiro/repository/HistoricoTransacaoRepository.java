package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelas operações de persistência da entidade {@link HistoricoTransacao}.
 * Fornece operações CRUD e métodos customizados para buscar transações por data, valor e descrição.
 */
public interface HistoricoTransacaoRepository extends JpaRepository<HistoricoTransacao, Long> {

    /**
     * Verifica se já existe uma transação igualmente criada
     * Útil para evitar duplicidade de lançamentos no histórico.
     * @param valor o valor do histórico de transação
     * @param data a data da transação
     * @param descricao a descrição da transação
     * @param tipoCategoria o tipo de categoria do histórico
     * @param subTipo o subtipo de categoria
     * @return true se existir uma transação com os mesmos valores
     * COUNT(h) > 0 contará todos os valores da entidade, se for maior que 0 ele ja retornará
     */
    @Query("SELECT COUNT(h) > 0 FROM HistoricoTransacao h WHERE h.valor = :valor AND h.data = :data AND h.descricao = :descricao AND h.categorias = :tipoCategoria AND h.subTipo = :subTipo")
    boolean existsTheSameData(@Param("valor") BigDecimal valor,
                              @Param("data") LocalDate data,
                              @Param("descricao") String descricao,
                              @Param("tipoCategoria") TiposCategorias tipoCategoria,
                              @Param("subTipo") SubTipoCategoria subTipo);

    /**
     * Busca todas as transações realizadas em uma determinada data.
     *
     * @param data a data das transações
     * @return uma lista de {@link HistoricoTransacao} correspondentes
     */
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.data = :data")
    List<HistoricoTransacao> encontrarTransacoesPelaData(@Param("data") LocalDate data);

    /**
     * Busca todas as transações com um determinado valor.
     *
     * @param valorTransacao o valor da transação
     * @return uma lista de {@link HistoricoTransacao} com o valor correspondente
     */
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.valor = :valorTransacao")
    List<HistoricoTransacao> encontrarTransacoesPeloValor(@Param("valorTransacao") BigDecimal valorTransacao);

    /**
     * Busca todas as transações com um determinado tipo de categoria, pelo enum.. seja DESPESA ou RECEITA.
     *
     * @param tiposCategoria o tipo de categoria pretendido
     * @return uma lista de {@link HistoricoTransacao} com o valor correspondente
     */
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.categorias =: tiposCategorias")
    List<HistoricoTransacao> encontrarTransacoesPeloTipoCategoria(@Param("tiposCategorias") TiposCategorias tiposCategoria);
}

