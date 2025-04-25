package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositório responsável pelas operações de persistência da entidade {@link Pagamentos}.
 * Fornece operações CRUD e consultas customizadas para busca de pagamentos por data, valor e descrição.
 */
public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {

    /**
     * Verifica se já existe um pagamento com a mesma data e descrição.
     * Evita duplicidade de lançamentos no sistema.
     *
     * @param dataPagamento a data do pagamento
     * @param descricao a descrição do pagamento
     * @return true se existir um pagamento com a mesma data e descrição
     */
    boolean existsByDataAndDescricao(LocalDate dataPagamento, String descricao);

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
