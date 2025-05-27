package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
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
 *
 *  * Teste de integração para a classe PagamentosAssocImplTest.
 *
 void associarUsuarioComPagamento(Long usuarioId, Long pagamentoid);
 void associarUsuarioComTransacoes(Long usuarioId, Long transacaoId);
 void associarUsuarioComConta(Long usuarioId, Long contaId);
 void associarUsuarioComCategoria(Long usuarioId, Long categoriaId);

 void desassociarUsuarioComPagamento(Long usuarioId, Long pagamentoId);
 void desassociarUsuarioComTransacao(Long usuarioId, Long transacaoId);
 void desassociarUsuarioComConta(Long usuarioId, Long contaId);
 void desassociarUsuarioComCategoria(Long usuarioId, Long categoriaId);

 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class UsuarioAssocImplTest {

    @Autowired
    private UsuariosAssociation usuariosAssociation;

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
    public class TesteCenariosCasosDeSucessoAssociacao{

        @Test
        @DisplayName("Associar Usuario com pagamento - Cenário de sucesso")
        public void metodoAssociarUsuarioComPagamentoDeveFuncionar(){

            //Encontrando os objetos pela Id
            Optional<Usuario> usuarioEncontrado = Optional.of
                    (usuarioRepository.findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            Optional<Pagamentos> pagamentoEncontrado = Optional.of
                    (pagamentosRepository.findById(pagamentoCriadoPraTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(usuarioEncontrado.isPresent(),"O Usuario deveria ser encontrado");
            assertTrue(pagamentoEncontrado.isPresent(),"O Pagamento deveria ser encontrado");

            //Assertando agora que eles não estão associados
            assertTrue(usuarioEncontrado.get().getPagamentosRelacionados().isEmpty(),
                    "O usuário não deveria estar associado ao pagamento");

            assertNull(pagamentoEncontrado.get().getUsuarioRelacionado()
                    ,"O pagamento não deveria estar associado a esse usuário");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> usuariosAssociation.associarUsuarioComPagamento
                            (usuarioEncontrado.get().getId(), pagamentoEncontrado.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertEquals(pagamentoEncontrado.get().getUsuarioRelacionado(),usuarioEncontrado.get()
                    ,"O pagamento deveria estar associado ao usuário");

            assertTrue(usuarioEncontrado.get().getPagamentosRelacionados().contains(pagamentoEncontrado.get())
                    ,"O usuário encontrado deveria estar associado a esse pagamento");

        }
        @Test
        @DisplayName("Associar Usuario com Historico de Transação - Cenário de sucesso")
        public void metodoUsuarioComHistoricoTransacaoDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Usuario> usuarioEncontrado = Optional.of
                    (usuarioRepository.findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            Optional<HistoricoTransacao> historicoEncontrado = Optional.of
                    (historicoTransacaoRepository.findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado");
            assertTrue(historicoEncontrado.isPresent(),"O Histórico de Transação deveria ser encontrado");

            //Assertando agora que eles não estão associados
            assertTrue(usuarioEncontrado.get().getTransacoesRelacionadas().isEmpty(),
                    "O Usuário não deveria estar associado ao histórico de transação");

            assertNull(historicoEncontrado.get().getUsuarioRelacionado()
                    ,"O Historico de transação não deveria ter o usuário associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> usuariosAssociation.associarUsuarioComTransacoes
                            (usuarioEncontrado.get().getId(), historicoEncontrado.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertTrue(usuarioEncontrado.get().getTransacoesRelacionadas().contains(historicoEncontrado.get())
                    ,"O Usuario deveria estar associado ao Histórico");

            assertEquals(usuarioEncontrado.get(),historicoEncontrado.get().getUsuarioRelacionado()
                    ,"O Histórico encontrado deveria estar associado a esse usuario");

        }

        @Test
        @DisplayName("Associar Usuario com Conta Usuario - Cenário de sucesso")
        public void metodoUsuarioComContaUsuarioDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Usuario> usuarioEncontrado = Optional.of
                    (usuarioRepository.findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            Optional<ContaUsuario> contaUsuarioEncontrada = Optional.of
                    (contaUsuarioRepository.findById(contaCriadaParaTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado");
            assertTrue(contaUsuarioEncontrada.isPresent(),"A conta usuário deveria ser encontrada");

            //Assertando agora que eles não estão associados
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().isEmpty(),
                    "O usuário não deveria estar associado a conta de usuário");

            assertNull(contaUsuarioEncontrada.get().getUsuarioRelacionado()
                    ,"A conta de usuário não deveria ter o usuário associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> usuariosAssociation.associarUsuarioComConta
                            (usuarioEncontrado.get().getId(), contaUsuarioEncontrada.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().contains(contaUsuarioEncontrada.get())
                    ,"O usuario deveria estar associado a conta de usuário");

            assertEquals(contaUsuarioEncontrada.get().getUsuarioRelacionado(),usuarioEncontrado.get()
                    ,"A conta de usuário encontrada deveria estar associado a esse usuário");

        }

        @Test
        @DisplayName("Associar usuário com CategoriaFinanceira - Cenário de sucesso")
        public void metodoUsuarioComCategoriaFinanceiraDeveAssociar(){

            //Encontrando os objetos pela Id
            Optional<Usuario> usuarioEncontrado = Optional.of
                    (usuarioRepository.findById(usuarioCriadoParaTeste.getId()).orElseThrow());

            Optional<CategoriaFinanceira> categoriaFinanceiraEncontrada = Optional.of
                    (categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId()).orElseThrow());

            //assertando que os valores foram encontrados...
            assertTrue(usuarioEncontrado.isPresent(),"O Usuario deveria ser encontrado");
            assertTrue(categoriaFinanceiraEncontrada.isPresent(),"A categoria financeira deveria ser encontrada");

            //Assertando agora que eles não estão associados
            assertTrue(usuarioEncontrado.get().getCategoriasRelacionadas().isEmpty(),
                    "O usuário não deveria estar associado a essa categoria financeira");

            assertNull(categoriaFinanceiraEncontrada.get().getUsuarioRelacionado()
                    ,"A categoria financeira não deveria ter o usuário associado");

            //Agora chamando o método, assertando que ele irá associar esses 2, sem retornar erro ou exceção
            assertDoesNotThrow(()-> usuariosAssociation.associarUsuarioComCategoria
                            (usuarioEncontrado.get().getId(), categoriaFinanceiraEncontrada.get().getId())
                    ,"O método deveria ter associado os valores normalmente, sem retornar exceção");

            //Assertando que ele associou
            assertTrue(usuarioEncontrado.get().getCategoriasRelacionadas().contains(categoriaFinanceiraEncontrada.get())
                    ,"O Usuario deveria estar associado a categoria financeira");

            assertEquals(categoriaFinanceiraEncontrada.get().getUsuarioRelacionado(),usuarioEncontrado.get()
                    ,"A categoria financeira encontrada deveria estar associado a esse usuário");

        }
    }

    @Nested
    @DisplayName("Desassociacao - Cenários de Sucesso")

    public class TesteCenariosCasosDeSucessoDesassociacao{
        @Test
        @DisplayName("Desassociar Usuário com pagamento deve Desassociar")
        public void metodoUsuarioComPagamentoDeveDesassociar(){

            //Associando os valores para realizar o teste de desassociação
            usuarioCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que foram associados

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O usuário deveria estar associado a esse pagamento");
            assertEquals(pagamentoCriadoPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O pagamento deveria estar associado ao usuário");


            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->usuariosAssociation.desassociarUsuarioComPagamento
                            (usuarioCriadoParaTeste.getId(), pagamentoCriadoPraTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos


            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().isEmpty()
                    ,"O usuário não deveria estar mais associado a esse pagamento");

            assertNull(pagamentoCriadoPraTeste.getUsuarioRelacionado()
                    ,"Pagamento não deveria estar mais associado ao Usuário");

        }

        @Test
        @DisplayName("Desassociar Usuario com conta usuário deve Desassociar")
        public void metodoUsuarioDeContaUsuarioDeveDesassociar(){

            //Associando os valores para realizar o teste de desassociação
            usuarioCriadoParaTeste.setContasRelacionadas(new ArrayList<>(List.of(contaCriadaParaTeste)));
            contaCriadaParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que foram associados
            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().contains(contaCriadaParaTeste)
                    ,"O usuário deveria estar associado a essa conta de usuário");

            assertEquals(contaCriadaParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"A conta de usuário deveria estar associado a esse usuário");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->usuariosAssociation.desassociarUsuarioComConta
                            (usuarioCriadoParaTeste.getId(), contaCriadaParaTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().isEmpty()
                    ,"Usuário não deveria estar mais associado a essa conta de usuário");

            assertNull(contaCriadaParaTeste.getUsuarioRelacionado()
                    ,"A conta de usuário não deveria estar mais associado a esse usuário");

        }

        @Test
        @DisplayName("Desassociar Usuário com HistoricoTransacao deve Desassociar")
        public void metodoUsuarioDeHistoricoTransacaoDeveDesassociar(){

            //Associando os valores para realizar o teste de desassociação
            usuarioCriadoParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que foram associados
            assertTrue(usuarioCriadoParaTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    ,"O usuário deveria estar associado a essa transacao");

            assertEquals(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O histórico de transação deveria estar associado a esse usuário");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->usuariosAssociation.desassociarUsuarioComTransacao
                            (usuarioCriadoParaTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertTrue(usuarioCriadoParaTeste.getTransacoesRelacionadas().isEmpty()
                    ,"Usuário não deveria estar mais associado ao histórico de transação");

            assertNull(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado()
                    ,"O histórico de transação não deveria estar mais associado a esse usuário");

        }

        @Test
        @DisplayName("Desassociar Usuario com CategoriaFinanceira deve Desassociar")
        public void metodoUsuarioDeCategoriaFinanceiraDeveDesassociar(){
            //Associando os valores para realizar o teste de desassociação
            usuarioCriadoParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));
            categoriaCriadaPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que foram associados
            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste)
                    ,"O usuário deveria estar associado a essa categoria Financeira");

            assertEquals(categoriaCriadaPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"A categoria Financeira deveria estar associado a esse usuário");

            //Agora chamando o método para desassociar, assertando que não retornará nenhum erro ao desassociar
            assertDoesNotThrow(()->usuariosAssociation.desassociarUsuarioComCategoria
                            (usuarioCriadoParaTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"A desassociação deveria ocorrer normalmente");

            //Verificando se foi realmente desassociado entre ambos
            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().isEmpty()
                    ,"Usuário não deveria estar mais associado a essa categoria financeira");

            assertNull(categoriaCriadaPraTeste.getUsuarioRelacionado()
                    ,"A categoria financeira não deveria estar mais associado a esse usuário");

        }
    }

    @Nested
    @DisplayName("Associação - Cenários de Erros e Exceptions")
    public class TesteCenariosAssociacaoCasosDeErrosExceptions{
        @Test
        @DisplayName("Ao associar Usuario com pagamento novamente, deve retornar erro")
        public void metodoAssociarUsuarioComPagamentoAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            usuarioCriadoParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que ambos estão realmente associados

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    ,"O usuáriio deveria estar associado a esse pagamento");

            assertEquals(pagamentoCriadoPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O pagamento deveria estar associado ao usuário");


            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->usuariosAssociation
                            .associarUsuarioComPagamento(usuarioCriadoParaTeste.getId(), pagamentoCriadoPraTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar Usuario com Histórico transação novamente, deve retornar erro")
        public void metodoAssociarUsuarioComHistoricoTransacaoJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            usuarioCriadoParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que ambos estão realmente associados
            assertEquals(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"Usuario deveria estar associado ao histórico de transação");

            assertEquals(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"O Histórico de transação deveria estar associado a esse pagamento");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->usuariosAssociation
                            .associarUsuarioComTransacoes(usuarioCriadoParaTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar usuario com ContaUsuario novamente, deve retornar erro")
        public void metodoAssociarUsuarioComContaUsuarioJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            usuarioCriadoParaTeste.setContasRelacionadas(new ArrayList<>(List.of(contaCriadaParaTeste)));
            contaCriadaParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que ambos estão realmente associados
            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().contains(contaCriadaParaTeste)
                    ,"O Usuário deveria estar associado a essa conta de usuário");

            assertEquals(contaCriadaParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"A conta de usuário deveria estar associado a esse usuário");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->usuariosAssociation
                            .associarUsuarioComConta(usuarioCriadoParaTeste.getId(), contaCriadaParaTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }

        @Test
        @DisplayName("Ao associar Usuário com Categoria Financeira novamente, deve retornar erro")
        public void metodoAssociarUsuarioComCategoriaFinanceiraJaAssociadoDeveRetornarException(){

            //Associando os objetos manualmente
            usuarioCriadoParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));
            categoriaCriadaPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);

            //Assertando que ambos estão realmente associados
            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste)
                    ,"O usuário deveria estar associado a essa categoria Financeira");

            assertEquals(categoriaCriadaPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    ,"A categoria Financeira deveria estar associado a esse usuário");

            //Agora chamando o método pra ver se ele irá retornar o erro ao tentar associar, pois ja estao associados
            assertThrows(AssociationErrorException.class,()
                            ->usuariosAssociation
                            .associarUsuarioComCategoria(usuarioCriadoParaTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"O método deveria retornar a exceção AssociationErrorException");
        }
    }

    @Nested
    @DisplayName("Desassociação - Cenários de Erros e Exceptions")
    public class TesteCenariosDesassociacaoCasosDeErrosExceptions{

        @Test
        @DisplayName("Desassociação do Usuario com histórico de transação - Cenário de erro")
        public void metodoDesassociarUsuarioComHistoricoTransacaoNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertTrue(usuarioCriadoParaTeste.getTransacoesRelacionadas().isEmpty()
                    ,"Usuário não deveria estar associado a esse histórico de transação");

            assertNull(historicoTransacaoCriadoParaTeste.getUsuarioRelacionado(),
                    "O histórico de transação não deveria estar associado a esse usuário");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->usuariosAssociation.desassociarUsuarioComTransacao
                            (usuarioCriadoParaTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Usuário  com Pagamento - Cenário de erro")
        public void metodoDesassociarUsuarioComPagamentoNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertTrue(usuarioCriadoParaTeste.getPagamentosRelacionados().isEmpty(),
                    "O usuário não deveria estar associado a esse pagamento");

            assertNull(pagamentoCriadoPraTeste.getUsuarioRelacionado()
                    ,"Pagamento não deveria estar associado a usuário");


            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->usuariosAssociation.desassociarUsuarioComPagamento
                            (usuarioCriadoParaTeste.getId(), pagamentoCriadoPraTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Usuário com Conta Usuario - Cenário de erro")
        public void metodoDesassociarUsuarioComContaDeUsuarioNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().isEmpty()
                    ,"Usuario não deveria estar associado a essa conta de usuário");

            assertNull(contaCriadaParaTeste.getUsuarioRelacionado(),
                    "A conta de usuário não deveria estar associado a esse usuário");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->usuariosAssociation.desassociarUsuarioComConta
                            (usuarioCriadoParaTeste.getId(), contaCriadaParaTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }

        @Test
        @DisplayName("Desassociação do Usuario de CategoriaFinanceira - Cenário de erro")
        public void metodoDesassociarUsuarioDeCategoriaFinanceiraNaoAssociadosDeveRetornarExcecao(){

            //Ambos não estão associados... chamar o metodo para desassociar, ele deve retornar exceção

            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().isEmpty()
                    ,"Usuario não deveria estar associado a essa categoria financeira");

            assertNull(categoriaCriadaPraTeste.getUsuarioRelacionado(),
                    "A categoria financeira não deveria estar associado a esse usuário");

            //Assertando que agora vou chamar o método de desassociar, ele vai retornar exceção de erro
            assertThrows(DesassociationErrorException.class,
                    ()->usuariosAssociation.desassociarUsuarioComCategoria
                            (usuarioCriadoParaTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"O método deveria retornar DesassociationErrorException");
        }
    }
}

