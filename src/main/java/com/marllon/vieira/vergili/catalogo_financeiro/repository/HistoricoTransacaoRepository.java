package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HistoricoTransacaoRepository extends JpaRepository<HistoricoTransacao, Long> {


    //Metodos customizados do repositório de Transações

    //Método customizado para comparação de valores(para não salvar o mesmo valor no banco de dados
    boolean existsByDataAndDescricao(LocalDate dataPagamento,String descricao);

    //Método para procurar a transação pela data da mesma
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.data =:data")
    List<HistoricoTransacao> encontrarTransacoesPelaData(@Param("data") LocalDate data);


    //Método para procurar a transação pelo valor
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.valor =:valorTransacao")
    List<HistoricoTransacao> encontrarTransacoesPeloValor(@Param("valorTransacao")BigDecimal valorTransacao);

    //Método para procurar a transação pela Descricao
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.descricao =:descricaoTransacao")
    HistoricoTransacao encontrarTransacaoPelaDescricao(@Param("descricaoTransacao")String descricao);

    //Método para procurar a transação do pagamento pela Categoria dela
    @Query("SELECT t FROM HistoricoTransacao t WHERE t.categorias =:tiposCategoria")
    HistoricoTransacao encontrarTransacaoPelaCategoria(@Param("tiposCategoria")TiposCategorias categoria);

}
