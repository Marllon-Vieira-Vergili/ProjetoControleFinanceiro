package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {


    //Métodos customizados para o repositório de Pagamentos


    //Método para encontrar o pagamento pelo nome
    @Query("SELECT p FROM Pagamentos p WHERE p.data =: dataPagamento")
    Pagamentos encontrarPagamentoPelaData(@Param("dataPagamento") Pagamentos dataPagamento);

    //Método para encontrar o pagamento pelo valor
    @Query("SELECT p FROM Pagamentos p WHERE p.valor =: valorPagamento")
    Pagamentos encontrarPagamentoPelaValor(@Param("valorPagamento") Pagamentos valorPagamento);

    //Método para encontrar o pagamento pela descrição
    @Query("SELECT p FROM Pagamentos p WHERE p.descricao =: descricaoPagamento")
    Pagamentos encontrarPagamentoPelaDescricao(@Param("descricaoPagamento")Pagamentos descricaoPagamento);


}
