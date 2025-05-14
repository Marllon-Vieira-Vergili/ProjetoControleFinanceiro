package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     *
     * @param valor         o valor do histórico de transação
     * @param data          a data da transação
     * @param descricao     a descrição da transação
     * @return true se existir uma transação com os mesmos valores
     * COUNT(h) > 0 contará todos os valores da entidade, se for maior que 0 ele ja retornará
     */
    @Query("SELECT COUNT(h) > 0 FROM HistoricoTransacao h WHERE h.valor = :valor AND h.data = :data AND h.descricao = :descricao ")
    boolean existsTheSameData(@Param("valor") BigDecimal valor,
                              @Param("data") LocalDate data,
                              @Param("descricao") String descricao);

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


}

