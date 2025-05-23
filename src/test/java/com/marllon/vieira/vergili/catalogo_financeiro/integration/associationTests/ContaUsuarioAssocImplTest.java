package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


/**
 *   Teste dos métodos de associação e desassociação de contaUsuario com as outras entidades
 * void associarContaComCategoria(Long contaId, Long categoriaId);
 * void associarContaComPagamento(Long contaId, Long pagamentoId);
 * void associarContaComTransacao(Long contaId, Long transacaoId);
 * void associarContaComUsuario(Long contaId, Long usuarioId);
 *
 * // ================= DESASSOCIAÇÕES =================
 *
 * void desassociarContaDeCategoria(Long contaId, Long categoriaId);
 * void desassociarContaDePagamento(Long contaId, Long pagamentoId);
 * void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId);
 * void desassociarContaDeUsuario(Long contaId, Long usuarioId);
 */

@SpringBootTest
@Transactional
//@TestPropertySource(value = "classpath:/application-test.properties") //Ou @ActiveProfiles("test")
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ContaUsuarioAssocImplTest {

    @Autowired
    private ContaUsuarioAssociation contaUsuarioAssociation;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Instanciando objetos para teste, criará no banco
    CategoriaFinanceira categoriaCriadaPraTeste;
    Usuario usuarioCriadoParaTeste;
    ContaUsuario contaCriadaParaTeste;
    HistoricoTransacao historicoTransacaoCriadoParaTeste;
    Pagamentos pagamentoCriadoPraTeste;

    @BeforeEach
    public void antesDeExecutarCadaMetodoCrioObjetosManualmente(){

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
        usuarioRepository.save(usuarioCriadoParaTeste);
        pagamentosRepository.save(pagamentoCriadoPraTeste);
        categoriaFinanceiraRepository.save(categoriaCriadaPraTeste);
        contaUsuarioRepository.save(contaCriadaParaTeste);
        historicoTransacaoRepository.save(historicoTransacaoCriadoParaTeste);
    }

    @AfterEach
    @DisplayName("Realizar a deleção dos dados do banco após cada teste ")
    public void depoisDeExecutarTesteTodosMetodos(){
        jdbcTemplate.execute("DELETE FROM contas");
        jdbcTemplate.execute("DELETE FROM pagamentos");
        jdbcTemplate.execute("DELETE FROM categoria_das_contas");
        jdbcTemplate.execute("DELETE FROM historico_transacoes");
        jdbcTemplate.execute("DELETE FROM usuarios");
    }


    @Nested //Criando uma subclasse dentro dessa classe para testar cada tipo de cenário
    @DisplayName("Associações - Casos de Sucesso")
    class CenariosDeSucessoNasAssociacoes {

        @Test
        @DisplayName("teste do método void associarContaComCategoria(Long contaId, Long categoriaId)")
        public void testeMetodoAssociarContaComCategoriaDeveRealizarAssociacao() {

            //Encontrar os objetos pela ID
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            Optional<CategoriaFinanceira> categoriaFinanceiraEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());

            //Assertando que esses valores existem no banco de dados e estão presentes
            assertTrue(contaUsuarioEncontrada.isPresent());
            assertTrue(categoriaFinanceiraEncontrada.isPresent());

            //assertando que é nulo que os dois não possuem associações
            assertTrue(contaUsuarioEncontrada.get().getCategoriasRelacionadas().isEmpty());
            assertNull(categoriaFinanceiraEncontrada.get().getContaRelacionada());
            assertNotEquals(null, contaUsuarioEncontrada);

            //Chamar o método de associar as 2 entidades, assertando que ele não jogará nenhuma exceção de erro, contido no método
            assertDoesNotThrow(() -> contaUsuarioAssociation.associarContaComCategoria
                    (contaUsuarioEncontrada.get().getId(), categoriaFinanceiraEncontrada.get().getId()));

            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertFalse(contaUsuarioEncontrada.get().getCategoriasRelacionadas().isEmpty());
            assertTrue(contaUsuarioEncontrada.get().getCategoriasRelacionadas().contains(categoriaFinanceiraEncontrada.get()));
            assertNotNull(categoriaFinanceiraEncontrada.get().getContaRelacionada());
            assertEquals(categoriaFinanceiraEncontrada.get().getContaRelacionada(), contaUsuarioEncontrada.get());
        }

        @Test
        @DisplayName("teste do método void associarContaComPagamento(Long contaId, Long pagamentoId)")
        public void testeMetodoAssociarContaComPagamentoDeveRealizarAssociacao() {

            //Encontrar os Objetos pela id
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            Optional<Pagamentos> pagamentoEncontrado = pagamentosRepository.findById(pagamentoCriadoPraTeste.getId());

            //Assertando que esses objetos foram encontrados no banco de dados H2
            assertTrue(contaUsuarioEncontrada.isPresent());
            assertTrue(pagamentoEncontrado.isPresent());

            //Assertando que esses relacionamentos não possuem associações
            assertTrue(contaUsuarioEncontrada.get().getPagamentosRelacionados().isEmpty());
            assertNull(pagamentoEncontrado.get().getContaRelacionada());

            //Assertando que quando eu chamar o método de associar, ele não jogue nenhuma exceção de erro
            assertDoesNotThrow(()-> contaUsuarioAssociation.associarContaComPagamento
                    (contaUsuarioEncontrada.get().getId(), pagamentoEncontrado.get().getId()));

            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertTrue(contaUsuarioEncontrada.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get()));
            assertEquals(pagamentoEncontrado.get().getContaRelacionada(), contaUsuarioEncontrada.get());
        }

        @Test
        @DisplayName(" teste do metodo void associarContaComTransacao(Long contaId, Long transacaoId)")
        public void testeMetodoAssociarContaComHistoricoTransacaoDeveRealizarAssociacao(){

            //Encontrando os valores
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            Optional<HistoricoTransacao> historicoTransacaoEncontrado = historicoTransacaoRepository.findById(historicoTransacaoCriadoParaTeste.getId());

            //Assertando que é verdade que esses valores existem no banco de dados H2 pela sua id
            assertTrue(contaUsuarioEncontrada.isPresent());
            assertTrue(historicoTransacaoEncontrado.isPresent());

            //Assertando que os dois lados relacionados, suas associações são nulas
            assertTrue(contaUsuarioEncontrada.get().getTransacoesRelacionadas().isEmpty());
            assertNull(historicoTransacaoEncontrado.get().getContaRelacionada());

            //Assertando que quando eu chamar o método de associar, ele não jogue nenhuma exceção de erro
            assertDoesNotThrow(()-> contaUsuarioAssociation.associarContaComTransacao
                    (contaUsuarioEncontrada.get().getId(), historicoTransacaoCriadoParaTeste.getId()));

            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertTrue(contaUsuarioEncontrada.get().getTransacoesRelacionadas().contains(historicoTransacaoEncontrado.get()));
            assertEquals(historicoTransacaoEncontrado.get().getContaRelacionada(),contaUsuarioEncontrada.get());
        }

        @Test
        @DisplayName("teste do método void associarContaComUsuario(Long contaId, Long usuarioId")
        public void testeMetodoAssociarContaComUsuarioDeveRealizarAssociacao() {

            //Encontrando os valores
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioCriadoParaTeste.getId());

            //Assertando que é verdade que esses valores existem no banco de dados H2 pela sua id
            assertTrue(contaUsuarioEncontrada.isPresent());
            assertTrue(usuarioEncontrado.isPresent());

            //Assertando que é verdade que esses valores não estão associados entre si
            assertNull(contaUsuarioEncontrada.get().getUsuarioRelacionado());
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().isEmpty());

            //Assertando que quando eu chamar o método de associar, ele não jogue nenhuma exceção de erro
            assertDoesNotThrow(() -> contaUsuarioAssociation.associarContaComUsuario
                    (contaUsuarioEncontrada.get().getId(), usuarioEncontrado.get().getId()));

            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertEquals(contaUsuarioEncontrada.get().getUsuarioRelacionado(),usuarioEncontrado.get());
            assertEquals(usuarioEncontrado.get().getContasRelacionadas(),contaUsuarioEncontrada.get());
        }
    }

    @Nested
    @DisplayName("Desassociações - Cenários de Sucesso")
    class CenariosDeSucessoNasDesassociacoes{

        @Test
        @DisplayName("testees")
        public void teste(){

        }
    }

    @Nested
    @DisplayName("Associações - Cenários de Erro")
    class CenariosDeErrosAndExceptionsNasAssociacoes{

        @Test
        @DisplayName("testeee")
        public void teste(){

        }
    }

    @Nested
    @DisplayName("Desassociações - Cenários de Erro")
    class CenariosDeErrosAndExceptionsNasDesassociacoes{

        @Test
        @DisplayName("testeees")
        public void testesesea(){

        }
    }
}



