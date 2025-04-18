package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {


    /**Métodos customizados para o repositório de Pagamentos
     */

    //Método customizado para comparação de valores(para não salvar o mesmo valor no banco de dados
    boolean existsByDataAndDescricao(LocalDate dataPagamento,String descricao);

    //Método para encontrar o pagamento pelo nome
    @Query("SELECT p FROM Pagamentos p WHERE p.data =:dataPagamento")
    List<Pagamentos> encontrarPagamentoPelaData(@Param("dataPagamento") LocalDate dataPagamento);

    //Método para encontrar o pagamento pelo valor
    @Query("SELECT p FROM Pagamentos p WHERE p.valor =:valorPagamento")
    List<Pagamentos> encontrarPagamentoPelaValor(@Param("valorPagamento") BigDecimal valorPagamento);

    //Método para encontrar o pagamento pela descrição
    @Query("SELECT p FROM Pagamentos p WHERE p.descricao =:descricaoPagamento")
    Pagamentos encontrarPagamentoPelaDescricao(@Param("descricaoPagamento")String descricaoPagamento);


}
