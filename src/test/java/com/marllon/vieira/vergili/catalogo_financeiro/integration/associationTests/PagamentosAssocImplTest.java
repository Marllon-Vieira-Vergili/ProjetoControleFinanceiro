package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
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
 * Teste de integração para a classe PagamentosAssocImplTest.
 *
 * void associarPagamentoComUsuario(Long pagamentoId, Long usuarioId);
 * void associarPagamentoATransacao(Long pagamentoId, Long transacaoId);
 * void associarPagamentoComConta(Long pagamentoId, Long contaId);
 * void associarPagamentoComCategoria(Long pagamentoId, Long categoriaId);
 *
 * void desassociarPagamentoUsuario(Long pagamentoId, Long usuarioId);
 * void desassociarPagamentoDeTransacao(Long pagamentoId, Long transacaoId);
 * void desassociarPagamentoConta(Long pagamentoId, Long contaId);
 * void desassociarPagamentoCategoria(Long pagamentoId, Long categoriaId);
 */
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
public class PagamentosAssocImplTest {

    @Autowired
    private PagamentosAssociation pagamentosAssociation;

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
    @DisplayName("Associação - Cenários de Sucesso")
    public class CenariosDeSucessoAssociacao{

        @Test
        @DisplayName("Associar Pagamento com Usuário  - Cenário de sucesso")
        public void metodoPagamentoComUsuarioDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Pagamentos> pagamentoEncontrado = Optional.of
                    (pagamentosRepository.findById(pagamentoCriadoPraTeste.getId()).orElseThrow());

            Optional<Usuario> usuarioEncontrado = Optional.of
                    (usuarioRepository.findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(pagamentoEncontrado.isPresent(),"O Pagamento deveria ser encontrado");
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado");

            //Assertando agora que eles não estão associados
            assertNull(pagamentoEncontrado.get().getUsuarioRelacionado(),
                    "O pagamento não deveria estar associado ao usuário");

            assertTrue(usuarioEncontrado.get().getPagamentosRelacionados().isEmpty()
                    ,"O usuário não deveria ter o pagamento associado ao usuário");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> pagamentosAssociation.associarPagamentoComUsuario
                    (pagamentoEncontrado.get().getId(), usuarioEncontrado.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertEquals(pagamentoEncontrado.get().getUsuarioRelacionado(),usuarioEncontrado.get()
                    ,"O pagamento deveria estar associado ao usuário");

            assertTrue(usuarioEncontrado.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get())
                    ,"O usuário encontrado deveria estar associado a esse pagamento");
        }

        @Test
        @DisplayName("Associar Pagamento com Histórico Transação - Cenário de sucesso")
        public void metodoPagamentoComHistoricoTransacaoDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Pagamentos> pagamentoEncontrado = Optional.of
                    (pagamentosRepository.findById(pagamentoCriadoPraTeste.getId()).orElseThrow());

            Optional<HistoricoTransacao> historicoEncontrado = Optional.of
                    (historicoTransacaoRepository.findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(pagamentoEncontrado.isPresent(),"O Pagamento deveria ser encontrado");
            assertTrue(historicoEncontrado.isPresent(),"O Histórico de Transação deveria ser encontrado");

            //Assertando agora que eles não estão associados
            assertTrue(pagamentoEncontrado.get().getTransacoesRelacionadas().isEmpty(),
                    "O pagamento não deveria estar associado ao histórico de transação");

            assertTrue(historicoEncontrado.get().getPagamentosRelacionados().isEmpty()
                    ,"O Historico de transação não deveria ter o pagamento associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> pagamentosAssociation.associarPagamentoATransacao
                            (pagamentoEncontrado.get().getId(), historicoEncontrado.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertTrue(pagamentoEncontrado.get().getTransacoesRelacionadas().contains(historicoEncontrado.get())
                    ,"O pagamento deveria estar associado ao Histórico");

            assertTrue(historicoEncontrado.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get())
                    ,"O Histórico encontrado deveria estar associado a esse pagamento");
        }

        @Test
        @DisplayName("Associar Pagamento com Conta Usuario - Cenário de sucesso")
        public void metodoPagamentoComContaUsuarioDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Pagamentos> pagamentoEncontrado = Optional.of
                    (pagamentosRepository.findById(pagamentoCriadoPraTeste.getId()).orElseThrow());

            Optional<ContaUsuario> contaUsuarioEncontrada = Optional.of
                    (contaUsuarioRepository.findById(contaCriadaParaTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(pagamentoEncontrado.isPresent(),"O Pagamento deveria ser encontrado");
            assertTrue(contaUsuarioEncontrada.isPresent(),"A conta usuário deveria ser encontrada");

            //Assertando agora que eles não estão associados
            assertNull(pagamentoEncontrado.get().getContaRelacionada(),
                    "O pagamento não deveria estar associado a conta de usuário");

            assertTrue(contaUsuarioEncontrada.get().getPagamentosRelacionados().isEmpty()
                    ,"A conta de usuário não deveria ter o pagamento associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> pagamentosAssociation.associarPagamentoComConta
                            (pagamentoEncontrado.get().getId(), contaUsuarioEncontrada.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertEquals(pagamentoEncontrado.get().getContaRelacionada(),contaUsuarioEncontrada.get()
                    ,"O pagamento deveria estar associado a conta de usuário");

            assertTrue(contaUsuarioEncontrada.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get())
                    ,"A conta de usuário encontrada deveria estar associado a esse pagamento");
        }

        @Test
        @DisplayName("Associar Pagamento com CategoriaFinanceira - Cenário de sucesso")
        public void metodoPagamentoComCategoriaFinanceiraDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Pagamentos> pagamentoEncontrado = Optional.of
                    (pagamentosRepository.findById(pagamentoCriadoPraTeste.getId()).orElseThrow());

            Optional<CategoriaFinanceira> categoriaFinanceiraEncontrada = Optional.of
                    (categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(pagamentoEncontrado.isPresent(),"O Pagamento deveria ser encontrado");
            assertTrue(categoriaFinanceiraEncontrada.isPresent(),"A categoria financeira deveria ser encontrada");

            //Assertando agora que eles não estão associados
            assertNull(pagamentoEncontrado.get().getCategoriaRelacionada(),
                    "O pagamento não deveria estar associado a essa categoria financeira");

            assertTrue(categoriaFinanceiraEncontrada.get().getPagamentosRelacionados().isEmpty()
                    ,"A categoria financeira não deveria ter o pagamento associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> pagamentosAssociation.associarPagamentoComCategoria
                            (pagamentoEncontrado.get().getId(), categoriaFinanceiraEncontrada.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertEquals(pagamentoEncontrado.get().getCategoriaRelacionada(),categoriaFinanceiraEncontrada.get()
                    ,"O pagamento deveria estar associado a categoria financeira");

            assertTrue(categoriaFinanceiraEncontrada.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get())
                    ,"A categoria financeira encontrada deveria estar associado a esse pagamento");
        }

    }

    @Nested
    @DisplayName("Desassociação - Cenários de Sucesso")
    public class CenariosDeSucessoDesassociacao{

        @Test
        @DisplayName("Desassociar Pagamento com Usuario deve Desassociar")
        public void metodoPagamentoComUsuarioDeveDesassociar(){
            //Associando os valores para realizar o teste de desassociação
            pagamentoCriadoPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que foram associados
            assertEquals(pagamentoCriadoPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O pagamento deveria estar associado ao usuário");

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O usuário deveria estar associado a esse pagamento");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->pagamentosAssociation.desassociarPagamentoUsuario
                    (pagamentoCriadoPraTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertNull(pagamentoCriadoPraTeste.getUsuarioRelacionado()
                    ,"Pagamento não deveria estar mais associado ao Usuário");

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().isEmpty()
                    ,"O usuário não deveria estar mais associado a esse pagamento");

        }

        @Test
        @DisplayName("Desassociar Pagamento com ContaUsuario deve Desassociar")
        public void metodoPagamentoDeContaUsuarioDeveDesassociar(){
            //Associando os valores para realizar o teste de desassociação
            pagamentoCriadoPraTeste.setContaRelacionada(contaCriadaParaTeste);
            contaCriadaParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que foram associados
            assertEquals(pagamentoCriadoPraTeste.getContaRelacionada(),contaCriadaParaTeste
                    ,"O pagamento deveria estar associado a essa conta de usuário");

            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"A conta de usuário deveria estar associado a esse pagamento");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->pagamentosAssociation.desassociarPagamentoConta
                            (pagamentoCriadoPraTeste.getId(), contaCriadaParaTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertNull(pagamentoCriadoPraTeste.getContaRelacionada()
                    ,"Pagamento não deveria estar mais associado a essa conta de usuário");

            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().isEmpty()
                    ,"A conta de usuário não deveria estar mais associado a esse pagamento");

        }

        @Test
        @DisplayName("Desassociar Pagamento com HistoricoTransacao deve Desassociar")
        public void metodoPagamentoDeTransacaoDeveDesassociar(){
            //Associando os valores para realizar o teste de desassociação
            pagamentoCriadoPraTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que foram associados
            assertTrue(pagamentoCriadoPraTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    ,"O pagamento deveria estar associado a essa transacao");

            assertTrue(historicoTransacaoCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O histórico de transação deveria estar associado a esse pagamento");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->pagamentosAssociation.desassociarPagamentoDeTransacao
                            (pagamentoCriadoPraTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertTrue(pagamentoCriadoPraTeste.getTransacoesRelacionadas().isEmpty()
                    ,"Pagamento não deveria estar mais associado ao histórico de transação");

            assertTrue(historicoTransacaoCriadoParaTeste.getPagamentosRelacionados().isEmpty()
                    ,"O histórico de transação não deveria estar mais associado a esse pagamento");

        }

        @Test
        @DisplayName("Desassociar Pagamento com CategoriaFinanceira deve Desassociar")
        public void metodoPagamentoDeCategoriaFinanceiraDeveDesassociar(){
            //Associando os valores para realizar o teste de desassociação
            pagamentoCriadoPraTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);
            categoriaCriadaPraTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que foram associados
            assertEquals(pagamentoCriadoPraTeste.getCategoriaRelacionada(),categoriaCriadaPraTeste
                    ,"O pagamento deveria estar associado a essa categoria Financeira");

            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"A categoria Financeira deveria estar associado a esse pagamento");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->pagamentosAssociation.desassociarPagamentoCategoria
                            (pagamentoCriadoPraTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertNull(pagamentoCriadoPraTeste.getCategoriaRelacionada()
                    ,"Pagamento não deveria estar mais associado a essa categoria financeira");

            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().isEmpty()
                    ,"A categoria financeira não deveria estar mais associado a esse pagamento");

        }

    }

    @Nested
    @DisplayName("Associação - Cenários de Erros e Exceptions")
    public class CenariosDeErrosAssociacao{

        @Test
        @DisplayName("Ao associar pagamento com usuário novamente, deve retornar erro")
        public void metodoAssociarPagamentoComUsuarioJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            pagamentoCriadoPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que ambos estão realmente associados
            assertEquals(pagamentoCriadoPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O pagamento deveria estar associado ao usuário");

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O usuáriio deveria estar associado a esse pagamento");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                    ->pagamentosAssociation
                    .associarPagamentoComUsuario(pagamentoCriadoPraTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar pagamento com Histórico transação novamente, deve retornar erro")
        public void metodoAssociarPagamentoComHistoricoTransacaoJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            pagamentoCriadoPraTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que ambos estão realmente associados
            assertTrue(pagamentoCriadoPraTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    ,"O pagamento deveria estar associado ao histórico de transação");

            assertTrue(historicoTransacaoCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O Histórico de transação deveria estar associado a esse pagamento");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->pagamentosAssociation
                            .associarPagamentoATransacao(pagamentoCriadoPraTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar pagamento com ContaUsuario novamente, deve retornar erro")
        public void metodoAssociarPagamentoComContaUsuarioJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            pagamentoCriadoPraTeste.setContaRelacionada(contaCriadaParaTeste);
            contaCriadaParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que ambos estão realmente associados
            assertEquals(pagamentoCriadoPraTeste.getContaRelacionada(),contaCriadaParaTeste
                    ,"O pagamento deveria estar associado a essa conta de usuário");

            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"A conta de usuário deveria estar associado a esse pagamento");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->pagamentosAssociation
                            .associarPagamentoComConta(pagamentoCriadoPraTeste.getId(), contaCriadaParaTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar pagamento com Categoria Financeira novamente, deve retornar erro")
        public void metodoAssociarPagamentoComCategoriaFinanceiraJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            pagamentoCriadoPraTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);
            categoriaCriadaPraTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));

            //Assertando que ambos estão realmente associados
            assertEquals(pagamentoCriadoPraTeste.getCategoriaRelacionada(),categoriaCriadaPraTeste
                    ,"O pagamento deveria estar associado a essa categoria Financeira");

            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"A categoria Financeira deveria estar associado a esse pagamento");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->pagamentosAssociation
                            .associarPagamentoComCategoria(pagamentoCriadoPraTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }
    }

    @Nested
    @DisplayName("Desassociação - Cenários de Erros e Exceptions")
    public class CenariosDeErrosDesassociacao{

        @Test
        @DisplayName("Desassociação do Pagamento com histórico de transação - Cenário de erro")
        public void metodoDesassociarPagamentoComHistoricoTransacaoNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertTrue(pagamentoCriadoPraTeste.getTransacoesRelacionadas().isEmpty()
                    ,"Pagamento não deveria estar associado a esse histórico de transação");

            assertTrue(historicoTransacaoCriadoParaTeste.getPagamentosRelacionados().isEmpty(),
                    "O histórico de transação não deveria estar associado a esse pagamento");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->pagamentosAssociation.desassociarPagamentoDeTransacao
                            (pagamentoCriadoPraTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Pagamento com Usuário - Cenário de erro")
        public void metodoDesassociarPagamentoComUsuarioNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertNull(pagamentoCriadoPraTeste.getUsuarioRelacionado()
                    ,"Pagamento não deveria estar associado a usuário");

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().isEmpty(),
                    "O usuário não deveria estar associado a esse pagamento");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->pagamentosAssociation.desassociarPagamentoUsuario
                            (pagamentoCriadoPraTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Pagamento com Conta Usuario - Cenário de erro")
        public void metodoDesassociarPagamentoComContaDeUsuarioNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertNull(pagamentoCriadoPraTeste.getContaRelacionada()
                    ,"Pagamento não deveria estar associado a essa conta de usuário");

            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().isEmpty(),
                    "A conta de usuário não deveria estar associado a esse pagamento");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->pagamentosAssociation.desassociarPagamentoConta
                            (pagamentoCriadoPraTeste.getId(), contaCriadaParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Pagamento de CategoriaFinanceira - Cenário de erro")
        public void metodoDesassociarPagamentoDeCategoriaFinanceiraNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertNull(pagamentoCriadoPraTeste.getCategoriaRelacionada()
                    ,"Pagamento não deveria estar associado a essa categoria financeira");

            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().isEmpty(),
                    "A categoria financeira não deveria estar associado a esse pagamento");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->pagamentosAssociation.desassociarPagamentoCategoria
                            (pagamentoCriadoPraTeste.getId(), contaCriadaParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }
    }
}




