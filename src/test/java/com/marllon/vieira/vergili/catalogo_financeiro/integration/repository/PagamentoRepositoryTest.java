package com.marllon.vieira.vergili.catalogo_financeiro.integration.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de teste para testar os métodos personalizados do meu repositório Histórico transação
 * Métodos:existsTheSameData(booleano), encontrarPagamentoPelaData(Retorna Lista Pagamento),
 * * encontrarPagamentoPeloValor(Retorna Lista de Pagamento), encontrarPagamentoPelaDescricao(Lista Pagamento)
 */
@DataJpaTest
@TestPropertySource(value = "/application-test.properties")
public class PagamentoRepositoryTest {


    @Autowired
    private PagamentosRepository pagamentosRepository;


    @Test
    @DisplayName("Teste do método existsTheSameData retornando TRUE")
    @Sql("/sql/PagamentosDados.sql")
    public void testandoMetodoSeJaexisteComOsMesmosDados(){
        boolean oValorExiste  = pagamentosRepository.existTheSameData(BigDecimal.valueOf(1200.50)
                ,LocalDate.of(2025,1,20),"Pagamento do aluguel", TiposCategorias.DESPESA);
    assertTrue(oValorExiste,"O valor não pode retornar false, ele existe");
    }

    @Test
    @DisplayName("Teste do método encontrar pagamento pela data ")
    @Sql("/sql/PagamentosDados.sql")
    public void testandoMetodoEncontrandoPagamentosPelaData(){
        List<Pagamentos> pagamentosEncontradosComEssaData = pagamentosRepository
                .encontrarPagamentoPelaData(LocalDate.of(2025,1,20));
        for(Pagamentos pagamentoEncontrados: pagamentosEncontradosComEssaData){
            assertEquals(LocalDate.of(2025,1,20),pagamentoEncontrados.getData());
        }
    }

    @Test
    @DisplayName("Teste do método encontrarPagamentoPeloValor")
    @Sql("/sql/PagamentosDados.sql")
    public void testandoMetodoEncontrandoPagamentosPeloValor(){
        List<Pagamentos> pagamentosEncontradosComEsseValor= pagamentosRepository
                .encontrarPagamentoPelaValor(BigDecimal.valueOf(750.00));
        for(Pagamentos pagamentoEncontrado: pagamentosEncontradosComEsseValor){
            assertEquals(BigDecimal.valueOf(750.00).setScale(2,RoundingMode.HALF_EVEN)
                    ,pagamentoEncontrado.getValor(),"O método deve retornar os valores de acordo com o valor do pagamento");
        }
    }
    @Test
    @DisplayName("Teste do método encontrarPagamentoPelaDescricao")
    @Sql("/sql/PagamentosDados.sql")
    public void testarMetodoEncontrandoPagamentoPelaDescricao(){
        Pagamentos pagamentoEncontradoComEssaDescricao = pagamentosRepository
                .encontrarPagamentoPelaDescricao("Pagamento extra ao fornecedor");
        assertEquals("Pagamento extra ao fornecedor", pagamentoEncontradoComEssaDescricao.getDescricao(),
                "O método deve retornar o valor do pagamento, procurando pela sua descrição");
    }
}
