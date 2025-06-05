package com.marllon.vieira.vergili.catalogo_financeiro.integration.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe para teste de integração de Categoria Financeira, testar somente os método se pega
 * com o banco de dados
 */
@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest
@Transactional
public class CategoriaFinanceiraIntegrationServiceTest {

    @Autowired
    private CategoriaFinanceiraService categoriaservice;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private CategoriaFinanceiraMapper mapper;

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
        usuarioRepository.saveAndFlush(usuarioCriadoParaTeste);
        pagamentosRepository.save(pagamentoCriadoPraTeste);
        categoriaFRepository.save(categoriaCriadaPraTeste);
        contaUsuarioRepository.save(contaCriadaParaTeste);
        historicoTransacaoRepository.save(historicoTransacaoCriadoParaTeste);
    }

    @AfterEach
    @DisplayName("Realizar a deleção dos dados temporários do banco após cada teste")
    public void limparValoresTemporariosDasTabelasNoBanco(){
        //Execução de SQL para deleção de todos os dados de cada tabela de cada entidade
        jdbcTemplate.execute("DELETE FROM categoria_das_contas");
        jdbcTemplate.execute("DELETE FROM usuarios");
        jdbcTemplate.execute("DELETE FROM pagamentos");
        jdbcTemplate.execute("DELETE FROM historico_transacoes");
        jdbcTemplate.execute("DELETE FROM contas");
    }

    @Nested
    @DisplayName("Teste de Integração dos métodos Categoria Financeira - Cenários de Sucesso")
    public class CenariosDeSucessoDaCategoriaFinanceira{

        @Test
        @DisplayName("Cenário de Criar Categoria Financeira - Sucesso")
        public void cenárioDeCriarCategoriaFinanceiraComSucesso(){


            CategoriaFinanceiraRequest request = new CategoriaFinanceiraRequest
                    (TiposCategorias.RECEITA,SubTipoCategoria.HERANCA);

            CategoriaFinanceiraResponse respostaEsperada = new CategoriaFinanceiraResponse
                    (2L,TiposCategorias.RECEITA,SubTipoCategoria.HERANCA);

            assertDoesNotThrow(()->{
                CategoriaFinanceiraResponse respostaDoMetodo = categoriaservice.criarCategoriaFinanceira(request);
                assertEquals(respostaEsperada,respostaDoMetodo,"A resposta que estou aguardando deve" +
                        " ser igual a resposta que o método me enviar");
            },"O método não deveria retornar nenhuma exceção e deveria criar a categoria normalmente");

        }

    }

    @Nested
    @DisplayName("Teste de Integração dos métodos Categoria Financeira - Cenários de Erros")
    public class CenariosDeErrosDaCategoriaFinanceira {

        @Test
        @DisplayName("Cenário de Criar Categoria Financeira - Erro Já Existe")
        public void cenárioDeCriarCategoriaFinanceiraComSucesso() {


            CategoriaFinanceiraRequest request = new CategoriaFinanceiraRequest
                    (TiposCategorias.DESPESA, SubTipoCategoria.ALIMENTACAO);

            assertThrowsExactly(JaExisteException.class, () -> {
                categoriaservice.criarCategoriaFinanceira(request);
            }, "O método deveria retornar exception que já existe essa mesma categoria criada");
        }
    }
}
