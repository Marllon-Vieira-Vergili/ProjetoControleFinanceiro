package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
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
import java.util.ArrayList;
import java.util.List;
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
            assertTrue(contaUsuarioEncontrada.isPresent(),"A conta de usuário deveria ser encontrada");
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria estar encontrado");

            //Assertando que é verdade que esses valores não estão associados entre si
            assertNull(contaUsuarioEncontrada.get().getUsuarioRelacionado(),"Conta de usuário não deveria ter usuário relacionado");
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().isEmpty(),"O Usuario encontrado não deveria ter conta relacionada");

            //Assertando que quando eu chamar o método de associar, ele não jogue nenhuma exceção de erro
            assertDoesNotThrow(() -> contaUsuarioAssociation.associarContaComUsuario
                    (contaUsuarioEncontrada.get().getId(), usuarioEncontrado.get().getId()),
                    "A associação deveria funcionar, sem retornar exceção");

            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertEquals(contaUsuarioEncontrada.get().getUsuarioRelacionado(),usuarioEncontrado.get(),"Deveria encontrar do lado conta usuario o usuário associado");
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().contains(contaUsuarioEncontrada.get()),"Deveria encontrar do lado conta usuário, o usuário associado");
        }
    }

    @Nested
    @DisplayName("Desassociações - Cenários de Sucesso")
    class CenariosDeSucessoNasDesassociacoes{

        @Test
        @DisplayName("Teste do método void desassociarContaDeCategoria(Long contaId, Long categoriaId);")
        public void testeMetodoDesassociarContaDeCategoriaDeveRealizarDesassociacao(){

            //Associando primeiramente os objetos para depois simular o comportamento de achar eles pra desassociar
            contaCriadaParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));
            categoriaCriadaPraTeste.setContaRelacionada(contaCriadaParaTeste);

            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste)
                    ,"A conta deveria estar associada a essa categoria relacionada");

            assertEquals(categoriaCriadaPraTeste.getContaRelacionada(), contaCriadaParaTeste
                    ,"A categoria deveria estar relacionado a essa conta");

            //Agora encontrando os valores pela id
            Optional<ContaUsuario> contaEncontrada =
                    Optional.of(contaUsuarioRepository.findById(contaCriadaParaTeste.getId()).orElseThrow());

            Optional<CategoriaFinanceira> categoriaEncontrada =
                    Optional.of(categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId()).orElseThrow());

            //Assertando que os valores foram encontrados no banco de dados
            assertTrue(true,"A conta não foi encontrada");
            assertTrue(true,"A categoria financeira não foi encontrada");

            //Chamando o método que será testando, assertando que ele não irá retornar nenhum erro ao desassociar
            assertDoesNotThrow(()-> contaUsuarioAssociation.desassociarContaDeCategoria(contaEncontrada.get().getId(),
                    categoriaEncontrada.get().getId()),"O método não deve retornar nenhuma exceção ou erro ao desassociar");

            //Realizando as verificações pra ver se realmente desassociou
            assertTrue(contaEncontrada.get().getCategoriasRelacionadas().isEmpty(),"Conta encontrada não deveria ter categoria relacionada, deveria ter desassociado");
            assertNull(categoriaEncontrada.get().getContaRelacionada(),"Categoria encontrada não deveria ter conta relacionado, deveria ter sido desassociado");
        }

        @Test
        @DisplayName("Teste do método void desassociarContaDePagamento(Long contaId, Long pagamentoId)")
        public void testeMetodoDesassociarContaDePagamentoDeveRealizarDesassociacao(){

            //Associando os valores para depois testar o metodo de desassociar

            contaCriadaParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setContaRelacionada(contaCriadaParaTeste);

            //Verificar se os valores foram associados
            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste),
                    "A conta deve ter o pagamento associado");

            assertEquals(pagamentoCriadoPraTeste.getContaRelacionada(),contaCriadaParaTeste,
                    "O pagamento deve ter a conta associado");


            //Encontrar esses objetos no banco de dados - A partir daqui, to simulando a logica do metodo desassociação
            Optional<ContaUsuario> contaEncontrada = Optional.of(contaUsuarioRepository
                    .findById(contaCriadaParaTeste.getId()).orElseThrow());

            Optional<Pagamentos> pagamentoEncontrado = Optional.of(pagamentosRepository
                    .findById(pagamentoCriadoPraTeste.getId())).orElseThrow();

            //Agora vou assertar que esses objetos foram localizados pela id
            assertTrue(contaEncontrada.isPresent(),"A conta deveria ser encontrada");
            assertTrue(pagamentoEncontrado.isPresent(),"o usuário deveria ser encontrado");

            //Agora chamando o método desassociar pra testá-lo, assertando que ele nao retorna nenhum erro
            assertDoesNotThrow(()-> contaUsuarioAssociation.desassociarContaDePagamento
                    (contaEncontrada.get().getId(), pagamentoEncontrado.get().getId())
                    ,"O método deveria desassociar sem dar erro");

            //Verificando se desassociou
            assertTrue(contaEncontrada.get().getPagamentosRelacionados().isEmpty()
                    ,"Conta encontrada nao deveria ter esse pagamento relacionado mais");
            assertNull(pagamentoEncontrado.get().getContaRelacionada()
                    ,"Deveria ser nulo o pagamento associado a essa conta");

        }

        @Test
        @DisplayName("Teste do método void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId)")
        public void testeMetodoDesassociarContaDeHistoricoDeTransacaoDeveRealizarTransacao(){

            //Associando os valores para depois testar o metodo de desassociar
            contaCriadaParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setContaRelacionada(contaCriadaParaTeste);

            //Verificar se os valores foram associados
            assertTrue(contaCriadaParaTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    ,"A categoria deve ter a transação associada a ele");

            assertEquals(historicoTransacaoCriadoParaTeste.getContaRelacionada(),contaCriadaParaTeste
                    ,"O histórico de transação deve ter categoria associado a ele");

            //Encontrar esses objetos no banco de dados - A partir daqui, to simulando a logica do metodo desassociação
            Optional<ContaUsuario> contaEncontrada = Optional.of(contaUsuarioRepository
                    .findById(contaCriadaParaTeste.getId()).orElseThrow());

            Optional<HistoricoTransacao> historicoEncontrado = Optional.of(historicoTransacaoRepository
                    .findById(historicoTransacaoCriadoParaTeste.getId()).orElseThrow());

            //Agora vou assertar que esses objetos foram localizados pela id
            assertTrue(contaEncontrada.isPresent(),"A conta deveria ser encontrada pela id");
            assertTrue(historicoEncontrado.isPresent(),"O Historico deveria ser encontrado pela id");

            //Agora chamando o método desassociar pra testá-lo, assertando que ele nao retorna nenhum erro
            assertDoesNotThrow(()->contaUsuarioAssociation.desassociarContaDeHistoricoDeTransacao
                    (contaEncontrada.get().getId(), historicoEncontrado.get().getId()),
                    "O método de desassociar deveria desassociar sem retornar erro");

            //Verificando se desassociou
            assertTrue(contaEncontrada.get().getTransacoesRelacionadas().isEmpty()
                    ,"A conta encontrada não deveria ter nenhuma transação associada");
            assertNull(historicoEncontrado.get().getContaRelacionada(),
                    "Do lado historico de transação, não deveria ter nenhuma conta relacionada, deveria estar nulo");
        }

        @Test
        @DisplayName("Teste do método void desassociarContaDeUsuario(Long contaId, Long usuarioId)")
        public void testeMetodoDesassociarContaDeUsuarioDeveRealizarDesassociacao(){

            //Associando os valores para depois testar o metodo de desassociar
            contaCriadaParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setContasRelacionadas(new ArrayList<>(List.of(contaCriadaParaTeste)));
            //Verificar se os valores foram associados
            assertSame(contaCriadaParaTeste.getUsuarioRelacionado(), usuarioCriadoParaTeste
                    ,"A conta criada deve ter esse usuário relacionado associado a ele");

            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().contains(contaCriadaParaTeste)
                    ,"O usuario criado deve ter essa conta associado a ele");

            //Encontrar esses objetos no banco de dados - A partir daqui, to simulando a logica do metodo desassociação
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioCriadoParaTeste.getId());

            //Agora vou assertar que esses objetos foram localizados pela id
            assertTrue(contaUsuarioEncontrada.isPresent(),"A conta de usuário deveria ser encontrada");
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado");

            //Agora chamando o método desassociar pra testá-lo, assertando que ele nao retorna nenhum erro
            assertDoesNotThrow(()->contaUsuarioAssociation.desassociarContaDeUsuario
                    (contaUsuarioEncontrada.get().getId(), usuarioEncontrado.get().getId())
                    ,"A desassociação deveria ocorrer sem nenhum erro ou exceção");

            //Verificando se desassociou
            assertNull(contaUsuarioEncontrada.get().getUsuarioRelacionado(),"A Conta não deveria estar associado ao usuário");
            assertTrue(usuarioEncontrado.get().getContasRelacionadas().isEmpty(),"Não deveria ter nenhuma conta associado a esse usuário ");
        }

    }

    @Nested
    @DisplayName("Associações - Cenários de Erro")
    class CenariosDeErrosAndExceptionsNasAssociacoes{

        @Test
        @DisplayName("Teste Cenário de erro no método associarContaComCategoria")
        public void testeCenarioErroMetodoAssociarContaComCategoriaDeveRetornarExcecao(){

            //Instanciar os objetos já associados
            contaCriadaParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));
            categoriaCriadaPraTeste.setContaRelacionada(contaCriadaParaTeste);

            //Verificar que eles foram associados
            assertEquals(categoriaCriadaPraTeste.getContaRelacionada(),contaCriadaParaTeste,
                    "A categoria deveria ter essa conta associada");

            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas()
                    .contains(categoriaCriadaPraTeste),"A conta deveria estar associado a categoria");

            //Assertando que quando eu tentar associar novamente, ele irá dar erro, pois ja está associado
            assertThrows(AssociationErrorException.class,() ->
                    contaUsuarioAssociation.associarContaComCategoria
                    (contaCriadaParaTeste.getId(), categoriaCriadaPraTeste.getId()),
                    "O método deveria retornar a exception AssociationErrorException, pois ja está associado");
        }

        @Test
        @DisplayName("Teste Cenário de erro no método associarContaComPagamento")
        public void testeCenarioErroMetodoAssociarContaComPagamentoDeveRetornarExcecao() {

            //Instanciar os objetos já associados
            contaCriadaParaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setContaRelacionada(contaCriadaParaTeste);

            //Verificar que eles foram associados
            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    , "Deveria ser verdade que a conta tem essa categoria associada");

            assertEquals(pagamentoCriadoPraTeste.getContaRelacionada(),contaCriadaParaTeste
                    , "Deveria ser igual ao pagamento criado para teste associado a conta");

            //Assertando que quando eu tentar associar novamente, ele irá dar erro, pois ja está associado
            assertThrows(AssociationErrorException.class,()->
                    contaUsuarioAssociation.associarContaComPagamento
                            (contaCriadaParaTeste.getId(), pagamentoCriadoPraTeste.getId())
                    ,"Deveria jogar exceção que não foi possível associar, pois ja está associado");
        }

        @Test
        @DisplayName("Teste cenário de erro ao associarContaComTransacao")
        public void testeCenarioErroMetodoAssociarContaComTransacaoDeveRetornarExcecao(){

            //Instanciar os objetos já associados
            contaCriadaParaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setContaRelacionada(contaCriadaParaTeste);

            //Verificar que eles foram associados
            assertTrue(contaCriadaParaTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    , "Deveria ser verdade que a conta tem esse histórico de transação associado");

            assertEquals(historicoTransacaoCriadoParaTeste.getContaRelacionada(),contaCriadaParaTeste
                    , "Deveria ser igual o histórico de transacao criada para teste associado a conta");

            //Assertando que quando eu tentar associar novamente, ele irá dar erro, pois ja está associado
            assertThrows(AssociationErrorException.class,()->
                            contaUsuarioAssociation.associarContaComTransacao
                                    (contaCriadaParaTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"Deveria jogar exceção que não foi possível associar, pois ja está associado");
        }


        @Test
        @DisplayName("Teste cenário de erro ao associarContaComUsuario")
        public void testeCenarioErroMetodoAssociarContaComUsuarioDeveRetornarExcecao(){

            //Instanciar os objetos já associados
            contaCriadaParaTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setContasRelacionadas(new ArrayList<>(List.of(contaCriadaParaTeste)));

            //Verificar que eles foram associados
            assertEquals(contaCriadaParaTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste
                    , "Deveria ser verdade que a conta tem esse usuário associado");

            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().contains(contaCriadaParaTeste)
                    , "Deveria ser igual o o usuario criada para teste associado a conta");

            //Assertando que quando eu tentar associar novamente, ele irá dar erro, pois ja está associado
            assertThrows(AssociationErrorException.class,()->
                            contaUsuarioAssociation.associarContaComUsuario
                                    (contaCriadaParaTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"Deveria jogar exceção que não foi possível associar, pois ja está associado");
        }

    }

    @Nested
    @DisplayName("Desassociações - Cenários de Erro")
    class CenariosDeErrosAndExceptionsNasDesassociacoes{

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar conta de categoria")
        public void testeCenarioErroAoDesassociarContaDeCategoriaDeveRetornarExcecao(){

            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas().isEmpty(),"A conta não deve ter categoria relacionada");
            assertNull(categoriaCriadaPraTeste.getContaRelacionada(),"A categoria não deve ter conta relacionada");

            //Assertando que jogará exceção ao tentar desassociar valores nulos
            assertThrows(DesassociationErrorException.class,()->
                    contaUsuarioAssociation.desassociarContaDeCategoria
                            (contaCriadaParaTeste.getId(), categoriaCriadaPraTeste.getId())
                    ,"Deveria retornar DesassociationErrorException, pois ambos nao estão associados um com o outro");

        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar conta de pagamento")
        public void testeCenarioErroAoDesassociarContaDePagamentoDeveRetornarExcessao(){

            //Assertando que as associações não existam, que sejam nulas
            assertTrue(contaCriadaParaTeste.getPagamentosRelacionados().isEmpty(),"Conta não deve ter pagamento associado");
            assertNull(pagamentoCriadoPraTeste.getContaRelacionada(),"Pagamento não deve ter conta relacionada");

            //Assertando que quando chamar o método de deassociar, ele deve retornar a exceção de desassociacao
            assertThrows(DesassociationErrorException.class,()
                    ->contaUsuarioAssociation.desassociarContaDePagamento
                    (contaCriadaParaTeste.getId(),pagamentoCriadoPraTeste.getId()),
                    "Deveria retornar DesassociationErrorException, pois " +
                            "ou as ids estão nulas, ou uma ja está associada a outra id");
        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar conta de histórico de transação")
        public void testeCenarioErroAoDesassociarContaDeHistoricoTransacaoDeveRetornarExcecao(){

            //Assertando que os valores estão nulos e não estão associados
            assertTrue(contaCriadaParaTeste.getTransacoesRelacionadas().isEmpty()
                    ,"A conta não deveria ter transações associadas");

            assertNull(historicoTransacaoCriadoParaTeste.getContaRelacionada(),
                    "O Histórico de transação não deveria ter conta relacionada");

            //Assertando que quando chamar o método de deassociar, ele deve retornar a exceção de desassociacao
            assertThrows(DesassociationErrorException.class,()
                    ->contaUsuarioAssociation.desassociarContaDeHistoricoDeTransacao
                    (contaCriadaParaTeste.getId(), historicoTransacaoCriadoParaTeste.getId())
                    ,"\"Deveria retornar DesassociationErrorException, pois \" +\n" +
                            "                            \"ou as ids estão nulas, ou uma ja está associada a outra id");

        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar conta de usuario")
        public void testeCenarioErroAoDesassociarContaDeUsuarioDeveRetornarExcecao(){

            //Assertando que os valores estão nulos e não estão associados
            assertNull(contaCriadaParaTeste.getUsuarioRelacionado()
                    ,"A conta não deveria ter usuário associado");

            assertTrue(usuarioCriadoParaTeste.getContasRelacionadas().isEmpty(),
                    "O usuário não não deveria ter conta relacionada");

            //Assertando que quando chamar o método de deassociar, ele deve retornar a exceção de desassociacao
            assertThrows(DesassociationErrorException.class,()
                            ->contaUsuarioAssociation.desassociarContaDeUsuario
                            (contaCriadaParaTeste.getId(), usuarioCriadoParaTeste.getId())
                    ,"\"Deveria retornar DesassociationErrorException, pois \" +\n" +
                            "                            \"ou as ids estão nulas, ou uma ja está associada a outra id");
        }
    }
}



