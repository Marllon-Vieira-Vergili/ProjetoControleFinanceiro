package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste dos métodos de associação e desassociação de categoriaFinanceira com as outras entidades
 * void associarCategoriaComConta(Long categoriaId, Long contaId);
 * void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId);
 * void associarCategoriaComTransacao(Long categoriaId, Long transacaoId);
 * void associarCategoriaComUsuario(Long categoriaId, Long usuarioId);
 * //////--------------------------------------------------------////////////////////////////////
 * // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================
 * void desassociarCategoriaAConta(Long categoriaId, Long contaId);
 * void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId);
 * void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId);
 * void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId);
 */

@SpringBootTest //Rodar o teste em um contexto de Teste em toda aplicação
@TestPropertySource("classpath:/application-test.properties") //Encontrar o arquivo .properties do banco Memoria
//Ou usar a anotação @ActiveProfiles("test") que ja encontra pelo nome, o properties de test
@Transactional //Realizar os roolbacks automaticamente após cada etapa
@AutoConfigureTestDatabase //Auto configurar o banco de dados teste
public class CategoriaFinanAssocImplTest {

//Instanciando as entidades que deverão ser utilizadas para teste

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    //Injetando a classe que será testada, quando for chamada
    @Autowired
    private CategoriaFinanceiraAssociation categoriaFinanceiraAssociation;

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

        categoriaCriadaPraTeste = new CategoriaFinanceira();
        categoriaCriadaPraTeste.setTiposCategorias(TiposCategorias.DESPESA);
        categoriaCriadaPraTeste.setSubTipo(SubTipoCategoria.ALIMENTACAO);


        usuarioCriadoParaTeste = new Usuario();
        usuarioCriadoParaTeste.setSenha("Teste123");
        usuarioCriadoParaTeste.setTelefone("(11)11111-1111");
        usuarioCriadoParaTeste.setEmail("usuario@email.com");
        usuarioCriadoParaTeste.setNome("teste");

        contaCriadaParaTeste = new ContaUsuario();
        contaCriadaParaTeste.setTipoConta(TiposContas.CONTA_INVESTIMENTO);
        contaCriadaParaTeste.setNome("teste");
        contaCriadaParaTeste.setSaldo(BigDecimal.valueOf(100));

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
    @DisplayName("Realizar a deleção dos dados do banco após cada teste")
    public void depoisDeExecutarTesteTodosMetodos(){
        //Execução de SQL para deleção de todos os dados de cada tabela de cada entidade
        jdbcTemplate.execute("DELETE FROM categoria_das_contas");
        jdbcTemplate.execute("DELETE FROM usuarios");
        jdbcTemplate.execute("DELETE FROM pagamentos");
        jdbcTemplate.execute("DELETE FROM historico_transacoes");
        jdbcTemplate.execute("DELETE FROM contas");
    }

    @Nested //Nested - Criando Subclasses dentro da classe Principal para Separação de Testes
    @DisplayName("Associações - Cenários de Sucesso") //Nome que será exibido no console, do bloco de Sucesso Associacoes
    class CenariosDeSucessoNasAssociacoes{


        @Test
        @DisplayName(" teste do método void associarCategoriaComConta(Long categoriaId, Long contaId) se associa")
        public void testeMetodoAssociarCategoriaComContaDeveAssociar(){

            //Encontrando os objetos no banco de dados
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            Optional<ContaUsuario> contaEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());

            //Assertando que os valores estão presentes no banco de dados H2
            assertTrue(categoriaEncontrada.isPresent(),"A categoria deveria estar presente, e ser encontrada");
            assertTrue(contaEncontrada.isPresent(),"A conta deveria estar presente, e ser encontrada");

            //Assertando que é verdade que ambos não estão associados
            assertNull(categoriaEncontrada.get().getContaRelacionada(),"A categoria encontrada não deveria estar associado a essa conta relacionada");
            assertTrue(contaEncontrada.get().getCategoriasRelacionadas().isEmpty(),"A conta encontrada não deveria estar associada a essa categoria encontrada");

            //Assertando que na hora de realizar a associação, ele não retorne nenhum erro ou exceção
            assertDoesNotThrow(()-> categoriaFinanceiraAssociation.associarCategoriaComConta(categoriaEncontrada.get().getId(),
                    contaEncontrada.get().getId()),"A associação não deveria dar erro ou Exception");


            //Assertando que os valores agora foram devidamente associados, com os valores passados e o valor real
            //Valor esperado                                                  valor real
            assertNotNull(categoriaEncontrada.get().getContaRelacionada(),"Categoria deve estar associada a uma conta");
            assertTrue(contaEncontrada.get().getCategoriasRelacionadas().contains(categoriaEncontrada.get()),
                    "Conta encontrada deve estar associada a uma categoria");

        }

        @Test
        @DisplayName("teste metodo void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId) se associa")
        public void testeMetodoAssociarCategoriaComPagamentoDeveAssociar(){

            //Encontrando os objetos no banco de dados
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            Optional<Pagamentos> pagamentoEncontrado = pagamentosRepository.findById(pagamentoCriadoPraTeste.getId());

            //Assertando que é verdade que esses objetos foram encontrados.. e estão presentes
            assertTrue(categoriaEncontrada.isPresent(),"A categoria encontrada deveria ser encontrada");
            assertTrue(pagamentoEncontrado.isPresent(),"O pagamento encontrado deveria ser encontrado");

            //Assertando que eles não estão associados em nenhum dos lados.
            assertTrue(categoriaEncontrada.get().getPagamentosRelacionados().isEmpty(),"A categoria não deveria estar relacionado a esse pagamento");
            assertNull(pagamentoEncontrado.get().getCategoriaRelacionada(),"O pagamento não deveria estar relacionado a essa categoria");

            //Assertando que ele não joga nenhuma exceção de erro quando eu realizar a associação...
            assertDoesNotThrow(()->categoriaFinanceiraAssociation
                    .associarCategoriaComPagamento
                            (categoriaEncontrada.get().getId(), pagamentoEncontrado.get().getId()),"A associação não deveria dar Erro ou Exceção");

            //Assertando que as associações foram realizadas
            assertTrue(categoriaEncontrada.get().getPagamentosRelacionados()
                    .contains(pagamentoEncontrado.get()),"A categoria encontrada deveria encontrar o pagamento Associado");
            assertEquals(pagamentoEncontrado.get().getCategoriaRelacionada(), categoriaEncontrada.get()
                    ,"O pagamento encontrado deveria encontrar a associação com a categoria");
        }

        @Test
        @DisplayName("teste metodo void associarCategoriaComTransacao(Long categoriaId, Long transacaoId) se associa")
        public void testeMetodoAssociarCategoriaComHistoricoTransacaoDeveAssociar(){

            //Encontrando os objetos no banco de dados
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            Optional<HistoricoTransacao> transacaoEncontrada = historicoTransacaoRepository.findById(historicoTransacaoCriadoParaTeste.getId());

            //Assertando que é verdade que esses objetos foram encontrados.. e estão presentes
            assertTrue(transacaoEncontrada.isPresent(),"A transação encontrada deveria ser encontrada");
            assertTrue(categoriaEncontrada.isPresent(),"A categoria encontrada deveria ser encontrada");

            //Assertando que eles não estão associados em nenhum dos lados.
            assertTrue(categoriaEncontrada.get().getTransacoesRelacionadas().isEmpty(),"A transação não deveria estar relacionado a essa categoria ");
            assertNull(transacaoEncontrada.get().getCategoriaRelacionada(),"A categoria não deveria estar relacionado a essa transação");

            //Assertando que ele não joga nenhuma exceção de erro quando eu realizar a associação...

            assertDoesNotThrow(()->categoriaFinanceiraAssociation.associarCategoriaComTransacao(categoriaEncontrada.get().getId(), transacaoEncontrada.get().getId()));

            //Assertando que a associação foi realizada
            assertTrue(categoriaEncontrada.get().getTransacoesRelacionadas().contains(transacaoEncontrada.get()),"A transação relacionada  do lado da categoria deveria estar associado");
            assertEquals(transacaoEncontrada.get().getCategoriaRelacionada(), categoriaEncontrada.get(),"A categoria relacionada do lado da transação deveria estar associada");
        }

        @Test
        @DisplayName("teste metodo void associarCategoriaComUsuario(Long categoriaId, Long usuarioId) se associa")
        public void testeMetodoAssociarCategoriaComUsuarioDeveAssociar(){

            //Encontrando os valores pela ID
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioCriadoParaTeste.getId());

            //Assertando que os objetos estão presentes no banco de dados
            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado com essa ID");
            assertTrue(categoriaEncontrada.isPresent(), "A categoria deveria ser encontrada com essa ID");

            //Assertando que ambos os lados não estão associados entre si
            assertNull(categoriaEncontrada.get().getUsuarioRelacionado(),"A categoria não deveria estar associado a esse usuário");
            assertTrue(usuarioEncontrado.get().getCategoriasRelacionadas().isEmpty(),"O usuário não deveria estar associado a essa categoria");

            //Assertando que ele não irá jogar exceção de erro, ao associar
            assertDoesNotThrow(()->categoriaFinanceiraAssociation
                    .associarCategoriaComUsuario(categoriaEncontrada.get().getId(), usuarioEncontrado.get().getId()),"O método deveria associar ambos os lados");


            //Assertando que eles foram associados
            assertEquals(categoriaEncontrada.get().getUsuarioRelacionado(), usuarioEncontrado.get(),"Deveria estar associado do lado categoria pra usuário");
            assertTrue(usuarioEncontrado.get()
                    .getCategoriasRelacionadas().contains(categoriaEncontrada.get()),"Deveria estar associado do lado usuário pra categoria");
        }
    }

    @Nested
    @DisplayName("Desassociações - Cenários de Sucesso")
    class CenariosDeSucessoNasDesassociacoes{

        @Test
        @DisplayName("teste do método void desassociarCategoriaAConta(Long categoriaId, Long contaId)")
        public void testeMetodoDesassociarCategoriaComContaDeveRealizarDesassociacao(){

            //Associei os valores já criados
            categoriaCriadaPraTeste.setContaRelacionada(contaCriadaParaTeste);
            contaCriadaParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));

            //Assertando que em ambos os lados estão associados..
            assertEquals(categoriaCriadaPraTeste.getContaRelacionada(),contaCriadaParaTeste);
            assertNotNull(contaCriadaParaTeste.getCategoriasRelacionadas());
            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste));

            //agora vou chamar o método de desassociar pra testar

            Optional<CategoriaFinanceira> categoriaAssociadaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            assertTrue(categoriaAssociadaEncontrada.isPresent());
            Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaCriadaParaTeste.getId());
            assertTrue(contaUsuarioEncontrada.isPresent());

            CategoriaFinanceira categoriaFinanceira = categoriaAssociadaEncontrada.get();
            ContaUsuario contaUsuario = contaUsuarioEncontrada.get();
            assertDoesNotThrow(()->categoriaFinanceiraAssociation
                    .desassociarCategoriaAConta(categoriaFinanceira.getId(), contaUsuario.getId()));

            assertNull(categoriaAssociadaEncontrada.get().getContaRelacionada());
            assertTrue(contaUsuarioEncontrada.get().getCategoriasRelacionadas().isEmpty());
        }

        @Test
        @DisplayName("teste do metodo void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId")
        public void testeMetodoDesassociarCategoriaComPagamentoDeveRealizarDesassociacao(){

            //Realizando as associações
            categoriaCriadaPraTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);

            //Encontro os valores
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            assertTrue(categoriaEncontrada.isPresent());

            Optional<Pagamentos> pagamentoEncontrado = pagamentosRepository.findById(pagamentoCriadoPraTeste.getId());
            assertTrue(pagamentoEncontrado.isPresent());


            assertDoesNotThrow(()->categoriaFinanceiraAssociation
                    .desassociarCategoriaAPagamento(categoriaEncontrada.get().getId(), pagamentoEncontrado.get().getId()));

            assertTrue(categoriaEncontrada.get().getPagamentosRelacionados().isEmpty());
            assertNull(pagamentoEncontrado.get().getCategoriaRelacionada());
        }

        @Test
        @DisplayName("teste do metodo void desassociarCategoriaATransacao(Long categoriaId, Long transacaoId")
        public void testeMetodoDesassociarCategoriaComHistoricoTransacaoDeveRealizarDesassociacao(){

            //Realizo a associação de ambos os lados
            categoriaCriadaPraTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);

            //Salvei.. agora vou procurar a id desses valores criados, igual o método de desassociar faz, pra cobrir aqueles OPtional..
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            assertTrue(categoriaEncontrada.isPresent());
            Optional<HistoricoTransacao> historicoEncontrado = historicoTransacaoRepository.findById(historicoTransacaoCriadoParaTeste.getId());
            assertTrue(historicoEncontrado.isPresent());

            //Vou chamar agora o método pra desassociar eles
            assertDoesNotThrow(()-> categoriaFinanceiraAssociation
                    .desassociarCategoriaTransacao(categoriaEncontrada.get().getId(), historicoEncontrado.get().getId()));


            assertTrue(categoriaEncontrada.get().getTransacoesRelacionadas().isEmpty());
            assertNull(historicoEncontrado.get().getCategoriaRelacionada());
        }

        @Test
        @DisplayName("teste do metodo void desassociarCategoriaAUsuario(Long categoriaId, Long usuarioId")
        public void testeMetodoDesassociarCategoriaComUsuarioDeveRealizarDesassociacao(){

            //Realizo a associação de ambos os lados
            categoriaCriadaPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));

            //Salvei.. agora vou procurar a id desses valores criados, igual o método de desassociar faz, pra cobrir aqueles OPtional..
            Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaCriadaPraTeste.getId());
            assertTrue(categoriaEncontrada.isPresent());

            Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioCriadoParaTeste.getId());
            assertTrue(usuarioEncontrado.isPresent());

            //Vou chamar agora o método pra desassociar eles
            assertDoesNotThrow(()-> categoriaFinanceiraAssociation
                    .desassociarCategoriaUsuario(categoriaEncontrada.get().getId(), usuarioEncontrado.get().getId()));


            assertTrue(categoriaEncontrada.get().getTransacoesRelacionadas().isEmpty());
            assertTrue(usuarioEncontrado.get().getCategoriasRelacionadas().isEmpty());
        }
    }

    @Nested
    @DisplayName("Associações - Cenários de Erros")
    class TesteCenariosDeErrosAndExceptionsNasAssociacoes{

        @Test
        @DisplayName("Teste Cenário de erro ao associar categoria com conta")
        public void associarCategoriaComContaDeveRetornarExcecao(){

            //Realizando a associação antes de chamar o método
            categoriaCriadaPraTeste.setContaRelacionada(contaCriadaParaTeste);
            contaCriadaParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));

            //Assertando que os valores da lógica estejam corretos
            assertNotNull(categoriaCriadaPraTeste.getContaRelacionada(),"Categoria ja deve ter conta associado");
            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste));
            assertEquals(categoriaCriadaPraTeste.getContaRelacionada(),contaCriadaParaTeste,
                    "A conta relacionada deve ser igual a da associada");

            assertThrows(AssociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.associarCategoriaComConta
                    (categoriaCriadaPraTeste.getId(),contaCriadaParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos ja estão associados");

        }

        @Test
        @DisplayName("Teste Cenário de erro ao associar categoria com pagamento")
        public void associarCategoriaComPagamentoDeveRetornarExcecao(){

            //Realizando a associação antes de chamar o método
            categoriaCriadaPraTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoCriadoPraTeste)));
            pagamentoCriadoPraTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);

            //Assertando que os valores da lógica estejam corretos
            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().contains(pagamentoCriadoPraTeste)
                    , "A Categoria deveria ter o pagamento associado");

                assertEquals(pagamentoCriadoPraTeste.getCategoriaRelacionada(),categoriaCriadaPraTeste,
                    "A categoria deve ter o pagamento relacionado");

            assertThrows(AssociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.associarCategoriaComPagamento
                    (categoriaCriadaPraTeste.getId(),pagamentoCriadoPraTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos ja estão associados");
        }

        @Test
        @DisplayName("Teste Cenário de erro ao associar categoria com transacao")
        public void associarCategoriaComTransacaoDeveRetornarExcecao(){

            //Realizando a associação antes de chamar o método
            categoriaCriadaPraTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTransacaoCriadoParaTeste)));
            historicoTransacaoCriadoParaTeste.setCategoriaRelacionada(categoriaCriadaPraTeste);

            //Assertando que os valores da lógica estejam corretos
            assertTrue(categoriaCriadaPraTeste.getTransacoesRelacionadas().contains(historicoTransacaoCriadoParaTeste)
                    , "A Categoria deveria ter o pagamento associado");

                assertEquals(historicoTransacaoCriadoParaTeste.getCategoriaRelacionada(),categoriaCriadaPraTeste,
                    "A categoria deve ter o pagamento relacionado");

            assertThrows(AssociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.associarCategoriaComTransacao
                    (categoriaCriadaPraTeste.getId(),historicoTransacaoCriadoParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos ja estão associados");
        }

        @Test
        @DisplayName("Teste Cenário de erro ao associar categoria com usuario")
        public void associarCategoriaComUsuarioDeveRetornarExcecao(){

            //Realizando a associação antes de chamar o método
            categoriaCriadaPraTeste.setUsuarioRelacionado(usuarioCriadoParaTeste);
            usuarioCriadoParaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaCriadaPraTeste)));

            //Assertando que os valores da lógica estejam corretos
            assertEquals(categoriaCriadaPraTeste.getUsuarioRelacionado(),usuarioCriadoParaTeste,
                    "A categoria deve ter o usuário relacionado");
            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().contains(categoriaCriadaPraTeste)
                    , "O Usuário deveria ter a categoria associado");

            assertThrows(AssociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.associarCategoriaComUsuario
                    (categoriaCriadaPraTeste.getId(),usuarioCriadoParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos ja estão associados");
        }


    }


    @Nested
    @DisplayName("Desassociações - Cenários de erros")
    class TesteCenariosDeErrosAndExceptionsNasDesassociacoes{

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar categoria com conta")
        public void desassociarCategoriaComContaDeveRetornarExcecao(){

            //Assertando que os valores não estão associados
            assertNull(categoriaCriadaPraTeste.getContaRelacionada(),
                    "A categoria não deveria estar associada a essa conta");

            assertTrue(contaCriadaParaTeste.getCategoriasRelacionadas().isEmpty()
                    ,"A conta não deveria estar associado a essa categoria");


            //Agora vou chamar o método de desassociar, assertando que ele vai retornar a exceção
            assertThrows(DesassociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.desassociarCategoriaAConta
                    (categoriaCriadaPraTeste.getId(),contaCriadaParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos não estão associados");

        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar categoria com pagamento")
        public void desassociarCategoriaComPagamentoDeveRetornarExcecao(){

            //Assertando que os valores não estão associados
            assertTrue(categoriaCriadaPraTeste.getPagamentosRelacionados().isEmpty()
                    ,"A categoria não deveria estar associado a esse pagamento");

            assertNull(pagamentoCriadoPraTeste.getCategoriaRelacionada()
                    ,"O pagamento não deveria estar associado a essa categoria");

            //Agora vou chamar o método de desassociar, assertando que ele vai retornar a exceção
            assertThrows(DesassociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.desassociarCategoriaAPagamento
                    (categoriaCriadaPraTeste.getId(),pagamentoCriadoPraTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos não estão associados");
        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar categoria com transacao")
        public void desassociarCategoriaComTransacaoDeveRetornarExcecao(){

            //Assertando que os valores não estão associados
            assertTrue(categoriaCriadaPraTeste.getTransacoesRelacionadas().isEmpty()
                    ,"A categoria não deveria estar associado a essa transação");

            assertNull(historicoTransacaoCriadoParaTeste.getCategoriaRelacionada()
                    ,"A transação não deveria estar associado a essa categoria");

            //Agora vou chamar o método de desassociar, assertando que ele vai retornar a exceção
            assertThrows(DesassociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.desassociarCategoriaTransacao
                    (categoriaCriadaPraTeste.getId(),historicoTransacaoCriadoParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos não estão associados");
        }

        @Test
        @DisplayName("Teste Cenário de erro ao desassociar categoria com usuario")
        public void desassociarCategoriaComUsuarioDeveRetornarExcecao(){

            //Assertando que os valores não estão associados
            assertNull(categoriaCriadaPraTeste.getUsuarioRelacionado()
                    ,"A categoria não deveria estar associado a esse usuário");

            assertTrue(usuarioCriadoParaTeste.getCategoriasRelacionadas().isEmpty()
                    ,"O usuário não deveria estar associado a essa categoria");

            //Agora vou chamar o método de desassociar, assertando que ele vai retornar a exceção
            assertThrows(DesassociationErrorException.class,()
                    ->categoriaFinanceiraAssociation.desassociarCategoriaUsuario
                    (categoriaCriadaPraTeste.getId(),usuarioCriadoParaTeste.getId())
                            ,"Deveria retornar Exceção, pois ambos não estão associados");
        }

    }
}





