package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
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
 * Repositório responsável pelas operações de persistência da entidade {@link Pagamentos}.
 * Fornece operações CRUD e consultas customizadas para busca de pagamentos por data, valor e descrição.
 */
public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {


    /**
     * Verifica se já existe um pagamento igualmente criado
     * Útil para evitar duplicidade de pagamentos.
     * @param valor, o valor do pagamento
     * @param data a data do pagamento
     * @param descricao a descrição do pagamento
     * @return true se existir uma transação com os mesmos valores
     * COUNT(h) > 0 contará todos os valores da entidade, se for maior que 0 ele ja retornará
     */

    @Query("SELECT COUNT(p) >0 from Pagamentos p WHERE p.valor = :valor" +
            " AND p.data = :data" +
            " AND p.descricao = :descricao" +
            " AND p.tiposCategorias = :tipoCategoria")
    boolean existTheSameData(@Param("valor") BigDecimal valor,
                             @Param("data") LocalDate data,
                             @Param("descricao") String descricao,
                             @Param("tipoCategoria") TiposCategorias tipoCategoria);

    /**
     * Busca todos os pagamentos realizados em uma determinada data.
     *
     * @param dataPagamento a data dos pagamentos
     * @return uma lista de {@link Pagamentos} correspondentes à data
     */
    @Query("SELECT p FROM Pagamentos p WHERE p.data = :dataPagamento")
    List<Pagamentos> encontrarPagamentoPelaData(@Param("dataPagamento") LocalDate dataPagamento);

    /**
     * Busca todos os pagamentos com um valor específico.
     *
     * @param valorPagamento o valor do pagamento
     * @return uma lista de {@link Pagamentos} com o valor correspondente
     */
    @Query("SELECT p FROM Pagamentos p WHERE p.valor = :valorPagamento")
    List<Pagamentos> encontrarPagamentoPelaValor(@Param("valorPagamento") BigDecimal valorPagamento);

    /**
     * Busca um pagamento com base na descrição.
     *
     * @param descricaoPagamento a descrição do pagamento
     * @return o {@link Pagamentos} correspondente, ou null se não encontrado
     */
    @Query("SELECT p FROM Pagamentos p WHERE p.descricao = :descricaoPagamento")
    Pagamentos encontrarPagamentoPelaDescricao(@Param("descricaoPagamento") String descricaoPagamento);


}
