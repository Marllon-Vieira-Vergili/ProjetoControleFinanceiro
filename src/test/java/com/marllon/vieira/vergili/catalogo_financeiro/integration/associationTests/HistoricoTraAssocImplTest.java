package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Teste de integração para a classe HistoricoTransacaoAssociationImpl.
 *
 *    void associarTransacaoComPagamento(Long transacaoId, Long pagamentoId);
 *     void associarTransacaoComConta(Long transacaoId, Long contaId);
 *     void associarTransacaoComUsuario(Long transacaoId, Long usuarioId);
 *     void associarTransacaoComCategoria(Long transacaoId, Long categoriaId);
 *     // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================
 *     void desassociarTransacaoDePagamento(Long transacaoId, Long pagamentoId);
 *     void desassociarTransacaoDeConta(Long transacaoId, Long contaId);
 *     void desassociarTransacaoDeUsuario(Long transacaoId, Long usuarioId);
 *     void desassociarTransacaoDeCategoria(Long transacaoId, Long categoriaId);
 */

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class HistoricoTraAssocImplTest {

    @Autowired
    private HistoricoTransacaoAssociation historicoTransacaoAssociation;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Instanciando objetos para teste, criará no banco
    CategoriaFinanceira categoriaCriadaPraTeste;
    Usuario usuarioCriadoParaTeste;
    ContaUsuario contaCriadaParaTeste;
    HistoricoTransacao historicoTransacaoCriadoParaTeste;
    Pagamentos pagamentoCriadoPraTeste;

    @BeforeEach
    public void antesDeExecutarCadaMetodoCrioObjetosManualmente() {

        contaCriadaParaTeste = new ContaUsuario();
        contaCriadaParaTeste.setTipoConta(TiposContas.CONTA_INVESTIMENTO);
        contaCriadaParaTeste.setNome("teste");
        contaCriadaParaTeste.setSaldo(BigDecimal.valueOf(100));

        categoriaCriadaPraTeste = new CategoriaFinanceira();
        categoriaCriadaPraTeste.setTiposCategorias(TiposCategorias.DESPESA);
        categoriaCriadaPraTeste.setSubTipo(SubTipoCategoria.ALIMENTACAO);

        usuarioCriadoParaTeste = new Usuario();
        usuarioCriadoParaTeste.setSenha("Teste123");
        usuarioCriadoParaTeste.setTelefone("(11)11111-1111");
        usuarioCriadoParaTeste.setEmail("usuario@email.com");
        usuarioCriadoParaTeste.setNome("teste");

        historicoTransacaoCriadoParaTeste = new HistoricoTransacao();
        historicoTransacaoCriadoParaTeste.setTiposCategorias(TiposCategorias.DESPESA);
        historicoTransacaoCriadoParaTeste.setData(LocalDate.now());
        historicoTransacaoCriadoParaTeste.setValor(BigDecimal.valueOf(1000));
        historicoTransacaoCriadoParaTeste.setDescricao("teste");

        pagamentoCriadoPraTeste = new Pagamentos();
        pagamentoCriadoPraTeste.setTiposCategorias(TiposCategorias.DESPESA);
        pagamentoCriadoPraTeste.setData(LocalDate.now());
        pagamentoCriadoPraTeste.setValor(BigDecimal.valueOf(1000));
        pagamentoCriadoPraTeste.setDescricao("teste");

        //Persistir os objetos no banco de dados em memoria H2
        pagamentosRepository.save(pagamentoCriadoPraTeste);
        categoriaFinanceiraRepository.save(categoriaCriadaPraTeste);
        usuarioRepository.save(usuarioCriadoParaTeste);
        contaUsuarioRepository.save(contaCriadaParaTeste);
        historicoTransacaoRepository.save(historicoTransacaoCriadoParaTeste);
    }

    @AfterEach
    @DisplayName("Realizar a deleção dos dados do banco após cada teste ")
    public void depoisDeExecutarTesteTodosMetodos() {
        jdbcTemplate.execute("DELETE FROM contas");
        jdbcTemplate.execute("DELETE FROM pagamentos");
        jdbcTemplate.execute("DELETE FROM categoria_das_contas");
        jdbcTemplate.execute("DELETE FROM historico_transacoes");
        jdbcTemplate.execute("DELETE FROM usuarios");
    }

    @Nested
    @DisplayName("Associação - Cenário de sucesso")
    public class TesteDosCenariosDeSucesso {


        @Test
        @DisplayName("Associar Histórico de transação com  Pagamento - Cenário de sucesso")
        public void metodoAssociarHistoricoTransacaoComPagamentoDeveAssociarComSucesso() {

            //encontrando os métodos pela ID
            Optional<HistoricoTransacao> historicoEncontrado = Optional.of(historicoTransacaoRepository
                    .findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            Optional<Pagamentos> pagamentoEncontrado = Optional.of(pagamentosRepository
                    .findById(pagamentoCriadoPraTeste.getId()).orElseThrow());


            //Assertando que esses valores não estão presentes
            assertTrue(historicoEncontrado.get().getPagamentosRelacionados().isEmpty()
                    , "O pagamento relacionado ainda não deveria estar associado");

            assertTrue(pagamentoEncontrado.get().getTransacoesRelacionadas().isEmpty()
                    , "A transação relacionada ainda não deveria estar associada");

            //Agora chamando o método de associação, simulando que ele não jogue a exceção
            assertDoesNotThrow(() -> historicoTransacaoAssociation.associarTransacaoComPagamento(historicoEncontrado.get().getId(),
                    pagamentoEncontrado.get().getId()), "O método deveria associar com sucesso");

        }

        @Test
        @DisplayName("Associar Histórico de transação com  Conta - Cenário de sucesso")
        public void metodoAssociarHistoricoTransacaoComContaDeveAssociarComSucesso() {

            //encontrando os métodos pela ID
            Optional<HistoricoTransacao> historicoEncontrado = Optional.of(historicoTransacaoRepository
                    .findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            Optional<ContaUsuario> contaEncontrada = Optional.of(contaUsuarioRepository
                    .findById(contaCriadaParaTeste.getId()).orElseThrow());


            //Assertando que esses valores não estão presentes
            assertNull(historicoEncontrado.get().getContaRelacionada()
                    , "A conta relacionada ainda não deveria estar associada");

            assertTrue(contaEncontrada.get().getTransacoesRelacionadas().isEmpty()
                    , "A transação relacionada ainda não deveria estar associada");

            //Agora chamando o método de associação, simulando que ele não jogue a exceção
            assertDoesNotThrow(() -> historicoTransacaoAssociation.associarTransacaoComConta(historicoEncontrado.get().getId(),
                    contaEncontrada.get().getId()), "O método deveria associar com sucesso");
        }

        @Test
        @DisplayName("Associar Histórico de transação com  Usuário - Cenário de sucesso")
        public void metodoAssociarHistoricoTransacaoComUsuarioDeveAssociarComSucesso() {

            //encontrando os métodos pela ID
            Optional<HistoricoTransacao> historicoEncontrado = Optional.of(historicoTransacaoRepository
                    .findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository
                    .findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            //Assertando que esses valores não estão presentes
            assertNull(historicoEncontrado.get().getUsuarioRelacionado()
                    , "O usuário relacionado ainda não deveria estar associado");

            assertTrue(usuarioEncontrado.get().getTransacoesRelacionadas().isEmpty()
                    , "A transação relacionada ainda não deveria estar associada");

            //Agora chamando o método de associação, simulando que ele não jogue a exceção
            assertDoesNotThrow(() -> historicoTransacaoAssociation.associarTransacaoComUsuario(historicoEncontrado.get().getId(),
                    usuarioEncontrado.get().getId()), "O método deveria associar com sucesso, sem retornar nenhum erro ou exceção");
        }

        @Test
        @DisplayName("Associar Histórico de transação com  Categoria - Cenário de sucesso")
        public void metodoAssociarHistoricoTransacaoComCategoriaDeveAssociarComSucesso() {

            //encontrando os métodos pela ID
            Optional<HistoricoTransacao> historicoEncontrado = Optional.of(historicoTransacaoRepository
                    .findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            Optional<CategoriaFinanceira> categoriaEncontrada = Optional.of(categoriaFinanceiraRepository
                    .findById(categoriaCriadaPraTeste.getId()).orElseThrow());

            //Assertando que esses valores não estão presentes
            assertNull(historicoEncontrado.get().getCategoriaRelacionada()
                    , "A categoria relacionada ainda não deveria estar associada");

            assertTrue(categoriaEncontrada.get().getTransacoesRelacionadas().isEmpty()
                    , "A transação relacionada ainda não deveria estar associada");

            //Agora chamando o método de associação, simulando que ele não jogue a exceção
            assertDoesNotThrow(() -> historicoTransacaoAssociation.associarTransacaoComCategoria(historicoEncontrado.get().getId(),
                    categoriaEncontrada.get().getId()), "O método deveria associar com sucesso, sem retornar nenhum erro ou exceção");
        }

    }

    @Nested
    @DisplayName("Associação - Cenário de erro")
    public class TesteDosCenariosDeErro {

        @Test
        @DisplayName("Associar Histórico de transação com  Pagamento - Cenário de erro")
        public void metodoAssociarHistoricoTransacaoComPagamentoDeveLancarExcecao() {

            //Já associando a transacao com pagamento e pagamento com transação
            historicoTransacaoAssociation.associarTransacaoComPagamento(historicoTransacaoCriadoParaTeste.getId(),
                    pagamentoCriadoPraTeste.getId());

            pagamentoCriadoPraTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));

            //Assertando que as associações foram realizadas
            assertTrue(historicoTransacaoCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    , "O pagamento deveria estar associado ao histórico de transação");

            assertTrue(pagamentoCriadoPraTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    , "O histórico de transação deveria estar associado ao pagamento");


            //Agora chamando o método de associação, simulando que ele jogue a exceção de erro
            assertThrows(AssociationErrorException.class, () -> historicoTransacaoAssociation
                            .associarTransacaoComPagamento(historicoTransacaoCriadoParaTeste.getId(),
                                    pagamentoCriadoPraTeste.getId())
                    , "O Método deve lançar AssociationErrorException, pois ja está associado");
        }

        @Test
        @DisplayName("Associar Histórico de transação com  Conta - Cenário de erro")
        public void metodoAssociarHistoricoTransacaoComContaDeveLancarExcecao() {

            //Realizar as associações em ambos os lados, pra depois tentar associar novamente pelo método
            historicoTransacaoCriadoParaTeste.setContaRelacionada(contaCriadaParaTeste);

            contaCriadaParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));

            //Assertando que as associações foram realizadas
            assertEquals(historicoTransacaoCriadoParaTeste.getContaRelacionada(), contaCriadaParaTeste
                    , "A conta deveria estar associada ao histórico de transação");

            assertTrue(contaCriadaParaTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    , "O histórico de transação deveria estar associado à conta");

            //Agora chamando o método de associação, simulando que ele jogue a exceção de erro
            assertThrows(AssociationErrorException.class, () -> historicoTransacaoAssociation
                            .associarTransacaoComConta(historicoTransacaoCriadoParaTeste.getId(),
                                    contaCriadaParaTeste.getId())
                    , "O Método deve lançar AssociationErrorException, pois ja está associado");
        }

        @Test
        @DisplayName("Associar Histórico de transação com  Usuário - Cenário de erro")
        public void metodoAssociarHistoricoTransacaoComUsuarioDeveLancarExcecao() {

            //Realizar as associações em ambos os lados, pra depois tentar associar novamente pelo método
            historicoTransacaoCriadoParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            usuarioCriadoParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));

            //Assertando que as associações foram realizadas
            assertEquals(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado(), usuarioCriadoParaTeste
                    , "O usuário deveria estar associado ao histórico de transação");

            assertTrue(usuarioCriadoParaTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    , "O histórico de transação deveria estar associado ao usuário");

            //Agora chamando o método de associação, simulando que ele jogue a exceção de erro
            assertThrows(AssociationErrorException.class, () -> historicoTransacaoAssociation
                            .associarTransacaoComUsuario(historicoTransacaoCriadoParaTeste.getId(),
                                    usuarioCriadoParaTeste.getId())
                    , "O Método deve lançar AssociationErrorException, pois ja está associado");

        }

        @Nested
        @DisplayName("Desassociação - Cenário de sucesso")
        public class TesteDosCenariosDeSucessoDesassociacao {

            @Test
            @DisplayName("Desassociar Histórico de transação com  Pagamento - Cenário de sucesso")
            public void teste() {

            }
        }

        @Nested
        @DisplayName("Desassociação - Cenário de erro")
        public class TesteDosCenariosDeErroDesassociacao {

            @Test
            @DisplayName("teste")
            public void teste() {

            }
        }
    }
}




