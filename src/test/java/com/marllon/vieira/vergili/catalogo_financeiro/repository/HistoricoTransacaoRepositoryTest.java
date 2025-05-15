package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
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
 * Métodos:existsTheSameData(booleano), encontrarTransacoesPelaData(Retorna Lista HistoricoTransacao),
 * * encontrarTransacoesPeloValor(Retorna Lista de valores), encontrarpeloTipoDeConta(ContaUsuario)
 */
@DataJpaTest
@TestPropertySource("/application-test.properties")

public class HistoricoTransacaoRepositoryTest {


    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;


    @Test
    @Sql("/sql/HistoricoTransacaoDados.sql")
    @DisplayName("Teste do metodo do repositório existsTheSameData para verificar se já existe um historico criado identico")
    public void testarMetodoExistsTheSameDataParaVerificarSeEleRetornaTrue(){
        boolean umIgualEncontrado = historicoTransacaoRepository.existsTheSameData( BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_EVEN),
                LocalDate.of(2025,1,27), "Parcela do empréstimo");
        assertTrue(umIgualEncontrado,"O método deveria ser encontrado, pois esse objeto existe na base de dados");
    }

    @Test
    @Sql("/sql/HistoricoTransacaoDados.sql")
    @DisplayName("Mesmot teste do método acima, mas agora testando se eu passar um valor falso, ele não deve encontrar nada")
    public void testarMetodoExistsTheSameDataParaVerificarSeAoDigitarValorQueNaoExisteNoBancoEleRetornaFalse(){
        boolean oValorNaoExisteNoBanco = historicoTransacaoRepository.existsTheSameData(BigDecimal.valueOf(100.000)
                ,LocalDate.of(2000,1,1),"Pagamento em janeiro de 2000");
        assertFalse(oValorNaoExisteNoBanco,"Esse valor não deveria voltar True como se tivesse sido encontrado!");
    }

    @Test
    @Sql("/sql/HistoricoTransacaoDados.sql")
    @DisplayName("Teste do método para encontrar históricos de transações pela data")
    public void testarMetodoEncontrarTransacoesPelaData(){
        List<HistoricoTransacao> historicosEncontradosNessaData = historicoTransacaoRepository
                .encontrarTransacoesPelaData(LocalDate.of(2025,12,25));
        for(HistoricoTransacao historicoEncontrado: historicosEncontradosNessaData){
            assertEquals(LocalDate.of(2025,12,25),historicoEncontrado.getData(),
                    "O método não encontrou nenhum histórico por esta data, mas ela existe em um repositório de dados");
        }
    }

    @Test
    @Sql("/sql/HistoricoTransacaoDados.sql")
    @DisplayName("Teste do método para encontrar histórico de transação pelo valor do histórico")
    public void testarMetodoEncontrarTransacaoPeloValor(){
        List<HistoricoTransacao> valoresEncontrados = historicoTransacaoRepository.encontrarTransacoesPeloValor(BigDecimal.valueOf(300.00));
        for(HistoricoTransacao historicoEncontrado: valoresEncontrados){
            assertEquals(historicoEncontrado.getValor(),BigDecimal.valueOf(300.00).setScale(2,RoundingMode.HALF_EVEN),"O valor encontrado em algum " +
                    "histórico de transação encontrado nesse informado, deveser igual ao valor que eu passei");
        }
    }
}


