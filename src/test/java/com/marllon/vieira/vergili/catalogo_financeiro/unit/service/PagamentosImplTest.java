package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Pagamentos.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.PagamentosImpl;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Ref;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(value = "/application-test.properties")
public class PagamentosImplTest {


    @Mock
    private PagamentosRepository pagamentosRepository;

    @InjectMocks
    private PagamentosImpl pagamentosService;

    @Mock
    private ContaUsuarioRepository contaUsuarioRepository;

    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;


    @Mock
    private PagamentosAssociation pagamentosAssociation;

    @Mock
    private HistoricoTransacaoAssociation historicoTransacaoAssociation;

    @Mock
    private ContaUsuarioService contaUsuarioService;

    @Mock
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @BeforeEach
    public void inicializarOsMocks() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("Métodos De Pagamentos/Recebimentos - Cenário de Sucesso")
    public class CenariosDeSucesso {

        @Test
        @DisplayName("Método criar Transação deve realizar criação e Histórico transação também")
        public void metodoCriarTransacaoDeveFuncionar() {

            CategoriaFinanceira categoriaTeste = new CategoriaFinanceira();
            categoriaTeste.setTiposCategorias(TiposCategorias.RECEITA);
            categoriaTeste.setSubTipo(SubTipoCategoria.SALARIO);
            ReflectionTestUtils.setField(categoriaTeste, "id", 1L);

            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tiposCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subTipo = SubTipoCategoria.SALARIO;


            when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                Pagamentos p = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(p, "id", 1L);
                return p;
            });
            when(historicoTransacaoRepository.save(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
                HistoricoTransacao ht = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(ht, "id", 1L);
                return ht;
            });

            when(categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(SubTipoCategoria.SALARIO)).thenReturn(categoriaTeste);

            doNothing().when(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            doNothing().when(historicoTransacaoAssociation).associarTransacaoComCategoria(anyLong(), anyLong());


            assertDoesNotThrow(() -> {
                pagamentosService.criarTransacao(valor, data,
                        descricao, tiposCategoria, subTipo);
                //assertEquals(respostaEsperada, response);
            });

            verify(pagamentosRepository).save(any(Pagamentos.class));
            verify(historicoTransacaoRepository).save(any(HistoricoTransacao.class));
            verify(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());
            verify(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            verify(historicoTransacaoAssociation).associarTransacaoComCategoria(anyLong(), anyLong());
            verify(categoriaFinanceiraRepository).encontrarCategoriaPeloSubTipo(subTipo);

        }

        @Test
        @DisplayName("Método de criar Recebimento deve Funcionar")
        public void metodoCriarRecebimentoDeValor() {

            Long contaId = 2L;
            Long usuarioId = 3L;
            Long transacaoId = 4L;
            Long categoriaId = 5l;

            CategoriaFinanceira categoria = new CategoriaFinanceira();
            categoria.setTiposCategorias(TiposCategorias.RECEITA);
            categoria.setSubTipo(SubTipoCategoria.SALARIO);
            ReflectionTestUtils.setField(categoria, "id", 1L);

            ContaUsuario contaMock = new ContaUsuario();
            contaMock.setPagamentosRelacionados(new ArrayList<>());
            contaMock.setSaldo(BigDecimal.valueOf(1000));
            ReflectionTestUtils.setField(contaMock, "id", contaId);

            Usuario usuarioMock = new Usuario();
            usuarioMock.setPagamentosRelacionados(new ArrayList<>());
            ReflectionTestUtils.setField(usuarioMock, "id", usuarioId);


            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tiposCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subTipo = SubTipoCategoria.SALARIO;

            when(categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(SubTipoCategoria.SALARIO)).thenReturn(categoria);

            when(contaUsuarioRepository.findById(contaId)).thenReturn(Optional.of(contaMock));
            when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));

            when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                Pagamentos rec = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(rec, "id", 1L);
                return rec;
            });

            when(historicoTransacaoRepository.save(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
                HistoricoTransacao ht = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(ht, "id", 1L);
                return ht;
            });

            doNothing().when(pagamentosAssociation).associarPagamentoComConta(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoComUsuario(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());

            doNothing().when(contaUsuarioService).adicionarSaldo(contaId, valor);

            PagamentosResponse responseEsperada = new PagamentosResponse(1L, valor, data, descricao, tiposCategoria, subTipo);
            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenReturn(responseEsperada);

            PagamentosRequest request = new PagamentosRequest(valor, data, descricao, tiposCategoria, subTipo, categoriaId, usuarioId, contaId);
            assertDoesNotThrow(() -> {
                PagamentosResponse respostaMetodo = pagamentosService.criarRecebimento(request);
                assertEquals(responseEsperada, respostaMetodo, "A resposta de criação do valor do método deveria ser igual a resposta esperada");
            });


            verify(categoriaFinanceiraRepository).encontrarCategoriaPeloSubTipo(SubTipoCategoria.SALARIO);
            verify(pagamentosAssociation).associarPagamentoComConta(1L, contaId);
            verify(contaUsuarioService).adicionarSaldo(contaId, valor);
            verify(pagamentosAssociation).associarPagamentoComUsuario(anyLong(), anyLong());
            verify(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            verify(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Método de criar Pagamento deve Funcionar")
        public void metodoCriarPagamentoDeValor() {

            Long contaId = 2L;
            Long usuarioId = 3L;
            Long transacaoId = 4L;
            Long categoriaFinanceiraId = 5L;

            CategoriaFinanceira categoria = new CategoriaFinanceira();
            categoria.setTiposCategorias(TiposCategorias.DESPESA);
            categoria.setSubTipo(SubTipoCategoria.CONTA_INTERNET);
            ReflectionTestUtils.setField(categoria, "id", categoriaFinanceiraId);

            ContaUsuario contaMock = new ContaUsuario();
            contaMock.setPagamentosRelacionados(new ArrayList<>());
            contaMock.setSaldo(BigDecimal.valueOf(1000));
            ReflectionTestUtils.setField(contaMock, "id", contaId);

            Usuario usuarioMock = new Usuario();
            usuarioMock.setPagamentosRelacionados(new ArrayList<>());
            ReflectionTestUtils.setField(usuarioMock, "id", usuarioId);


            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tiposCategoria = TiposCategorias.DESPESA;
            SubTipoCategoria subTipo = SubTipoCategoria.CONTA_INTERNET;

            when(categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(SubTipoCategoria.CONTA_INTERNET)).thenReturn(categoria);

            when(contaUsuarioRepository.findById(contaId)).thenReturn(Optional.of(contaMock));
            when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioMock));

            when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                Pagamentos rec = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(rec, "id", 1L);
                return rec;
            });

            when(historicoTransacaoRepository.save(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
                HistoricoTransacao ht = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(ht, "id", 1L);
                return ht;
            });

            doNothing().when(pagamentosAssociation).associarPagamentoComConta(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoComUsuario(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            doNothing().when(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());

            doNothing().when(contaUsuarioService).subtrairSaldo(contaId, valor);

            PagamentosResponse responseEsperada = new PagamentosResponse(1L, valor, data, descricao, tiposCategoria, subTipo);
            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenReturn(responseEsperada);

            PagamentosRequest request = new PagamentosRequest(valor, data, descricao, tiposCategoria, subTipo, categoriaFinanceiraId, usuarioId, contaId);
            assertDoesNotThrow(() -> {
                PagamentosResponse respostaMetodo = pagamentosService.criarPagamento(request);
                assertEquals(responseEsperada, respostaMetodo, "A resposta de criação do valor do método deveria ser igual a resposta esperada");
            });

            //assertEquals(BigDecimal.valueOf(2000),contaMock.getSaldo(),"O saldo deveria estar somado 1000+1000=2000");


            verify(categoriaFinanceiraRepository).encontrarCategoriaPeloSubTipo(SubTipoCategoria.CONTA_INTERNET);
            verify(pagamentosAssociation).associarPagamentoComConta(anyLong(), anyLong());
            verify(contaUsuarioService).subtrairSaldo(contaId, valor);
            verify(pagamentosAssociation).associarPagamentoComUsuario(anyLong(), anyLong());
            verify(pagamentosAssociation).associarPagamentoComCategoria(anyLong(), anyLong());
            verify(pagamentosAssociation).associarPagamentoATransacao(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Metodo encontrar pela Id deve localizar")
        public void deveLocalizarTransacaoSejaPagamentoOuRecebimentoPelaId() {

            Pagamentos pagamentoTeste = new Pagamentos();
            ReflectionTestUtils.setField(pagamentoTeste, "id", 1L);

            when(pagamentosRepository.findById(anyLong())).thenReturn(Optional.of(pagamentoTeste));

            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenReturn(null);

            assertDoesNotThrow(() -> {
                pagamentosService.encontrarPagamentoOuRecebimentoPorid(1L);
            });

            verify(pagamentosRepository).findById(anyLong());
            verify(pagamentoMapper).retornarDadosPagamento(any(Pagamentos.class));
        }

        @Test
        @DisplayName("Metodo encontrar pela data deve localizar os pagamentos/recebimentos")
        public void deveLocalizarTransacaoSejaPagamentoOuRecebimentoPelaData() {

            Pagamentos pagamentoTeste = new Pagamentos();
            pagamentoTeste.setData(LocalDate.now());
            ReflectionTestUtils.setField(pagamentoTeste, "id", 1L);

            when(pagamentosRepository.encontrarPagamentoPelaData(LocalDate.now())).thenReturn(List.of(pagamentoTeste));

            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenReturn(null);

            assertDoesNotThrow(() -> {
                pagamentosService.encontrarPagamentoOuRecebimentoPorData(LocalDate.now());
            });

            verify(pagamentosRepository).encontrarPagamentoPelaData(LocalDate.now());
            verify(pagamentoMapper).retornarDadosPagamento(any(Pagamentos.class));
        }

        @Test
        @DisplayName("Encontrar pagamentos pelo nome de um usuário")
        public void deveEncontrarPagamentosPeloNomeDeUmUsuario() {

            Pagamentos pagamento1 = new Pagamentos();
            Usuario usuario = new Usuario();
            ReflectionTestUtils.setField(pagamento1, "id", 1L);
            ReflectionTestUtils.setField(usuario, "id", 1L);

            Pagamentos pagamento2 = new Pagamentos();
            ReflectionTestUtils.setField(pagamento1, "id", 2L);

            List<Pagamentos> lista = new ArrayList<>(List.of(pagamento1, pagamento2));


            pagamento1.setUsuarioRelacionado(usuario);
            pagamento2.setUsuarioRelacionado(usuario);

            usuario.setPagamentosRelacionados(new ArrayList<>(lista));

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            when(pagamentoMapper.retornarDadosPagamento(pagamento1)).thenAnswer(invocationOnMock -> {
                return new PagamentosResponse(1L, BigDecimal.TEN,
                        LocalDate.now(),
                        "teste", TiposCategorias.RECEITA, SubTipoCategoria.SALARIO);
            });

            when(pagamentoMapper.retornarDadosPagamento(pagamento2)).thenAnswer(invocationOnMock -> {
                return new PagamentosResponse(2L, BigDecimal.ONE,
                        LocalDate.now().minusDays(1),
                        "teste2", TiposCategorias.DESPESA, SubTipoCategoria.DESPESA_ALUGUEL);
            });


            assertDoesNotThrow(() -> {
                pagamentosService.encontrarPagamentosPorUsuario(usuario.getId());
            });

            verify(usuarioRepository).findById(1L);
            verify(pagamentoMapper).retornarDadosPagamento(pagamento1);
            verify(pagamentoMapper).retornarDadosPagamento(pagamento2);
        }

        @Test
        @DisplayName("Metodo encontrar todos os pagamentos e recebumentos deve retornar a lista de todas")
        public void metodoEncontrarTodosPagamentosOuRecebimentosDeveRetornarPageable() {

            Long idPagamento1 = 1L;
            Pagamentos pagamentos1 = new Pagamentos();
            ReflectionTestUtils.setField(pagamentos1, "id", idPagamento1);

            Long idRecebimento2 = 2L;
            Pagamentos recebimento2 = new Pagamentos();
            ReflectionTestUtils.setField(recebimento2, "id", idRecebimento2);

            List<Pagamentos> pagamentosEncontrados = new ArrayList<>(List.of(pagamentos1, recebimento2));

            when(pagamentosRepository.findAll()).thenReturn(pagamentosEncontrados);

            Page<Pagamentos> paginaContas = new PageImpl<>(pagamentosEncontrados);

            assertDoesNotThrow(() -> {
                pagamentosService.encontrarTodosPagamentos(paginaContas.getPageable());
            }, "O método não deveria retornar nenhuma exceção de erro");

            assertFalse(paginaContas.isEmpty(), "Paginas contas deveria possuir 2 elementos");
            verify(pagamentosRepository).findAll();
        }

        @Test
        @DisplayName("Método remover Pagamento ou Recebimento com sucesso")
        public void removerPagamentoOuRecebimentoComSucesso() {

            //Instanciando os valores e associando
            Long usuarioId = 1L;
            Long categoriaFinanId = 1L;
            Long contaUserId = 1L;
            Long historicoTransacaoId = 1L;
            Long pagamentoId = 1L;

            Usuario usuarioTeste = new Usuario();
            ReflectionTestUtils.setField(usuarioTeste, "id", usuarioId);

            ContaUsuario contaTeste = new ContaUsuario();
            ReflectionTestUtils.setField(contaTeste, "id", contaUserId);

            CategoriaFinanceira categoriaTeste = new CategoriaFinanceira();
            ReflectionTestUtils.setField(categoriaTeste, "id", categoriaFinanId);

            Pagamentos pagamentoTeste = new Pagamentos();
            ReflectionTestUtils.setField(pagamentoTeste, "id", pagamentoId);

            HistoricoTransacao historicoTeste = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoTeste, "id", historicoTransacaoId);

            //Associando os valores
            pagamentoTeste.setUsuarioRelacionado(usuarioTeste);
            pagamentoTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTeste)));
            pagamentoTeste.setContaRelacionada(contaTeste);
            pagamentoTeste.setCategoriaRelacionada(categoriaTeste);

            //Assertando que estão associados...
            assertEquals(usuarioTeste, pagamentoTeste.getUsuarioRelacionado(), "O pagamento deveria estar associado ao usuário");
            assertTrue(pagamentoTeste.getTransacoesRelacionadas().contains(historicoTeste), "O pagamento deveria estar com a transação associada");
            assertEquals(categoriaTeste, pagamentoTeste.getCategoriaRelacionada(), "O pagamento deveria estar com a categoria financeira associada");

            //Agora simulando os resultados
            when(pagamentosRepository.findById(pagamentoId)).thenReturn(Optional.of(pagamentoTeste));

            doNothing().when(pagamentosAssociation).desassociarPagamentoUsuario(pagamentoId, usuarioId);
            doNothing().when(pagamentosAssociation).desassociarPagamentoConta(pagamentoId, contaUserId);
            doNothing().when(pagamentosAssociation).desassociarPagamentoCategoria(pagamentoId, categoriaFinanId);
            doNothing().when(pagamentosAssociation).desassociarPagamentoDeTransacao(pagamentoId, historicoTransacaoId);

            assertDoesNotThrow(() -> pagamentosService.deletarPagamento(pagamentoId)
                    , "O método deveria remover a conta normalmente");


            //Verificar se chamou os mocks
            verify(pagamentosRepository).findById(pagamentoId);
            verify(pagamentosAssociation).desassociarPagamentoUsuario(pagamentoId, usuarioId);
            verify(pagamentosAssociation).desassociarPagamentoConta(pagamentoId, contaUserId);
            verify(pagamentosAssociation).desassociarPagamentoCategoria(pagamentoId, categoriaFinanId);
            verify(pagamentosAssociation).desassociarPagamentoDeTransacao(pagamentoId, historicoTransacaoId);
            verify(pagamentosRepository).delete(pagamentoTeste);
        }

        @Test
        @DisplayName("Deve encontrar o pagamento Ou Recebimento pelo Tipo de Categoria fornecido")
        public void deveEncontrarOPagamentoOuRecebimentoPeloTipoCategoria() {

            Pagamentos pagamento1 = new Pagamentos();
            pagamento1.setTiposCategorias(TiposCategorias.RECEITA);
            ReflectionTestUtils.setField(pagamento1, "id", 1L);

            Pagamentos pagamento2 = new Pagamentos();
            pagamento2.setTiposCategorias(TiposCategorias.RECEITA);
            ReflectionTestUtils.setField(pagamento1, "id", 2L);

            List<Pagamentos> listaPagamentos = new ArrayList<>(List.of(pagamento1, pagamento2));
            Page<Pagamentos> paginaPagamentos = new PageImpl<>(listaPagamentos);

            when(pagamentosRepository.findAll()).thenReturn(listaPagamentos);

            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                return new PagamentosResponse(null, null, null, null, null, null);
            });

            assertDoesNotThrow(() -> {
                pagamentosService.encontrarPagamentoOuRecebimentoPorTipo(TiposCategorias.RECEITA);
            });

            verify(pagamentosRepository).findAll();
            verify(pagamentoMapper, times(2)).retornarDadosPagamento(any(Pagamentos.class));
        }

        @Test
        @DisplayName("Deve atualizar o pagamento ou recebimento sem retornar erro")
        public void deveAtualizarPagamentoOuRecebimentoSemRetornarErro() {

            Long pagamentoId = 1L;
            Pagamentos pagamento = new Pagamentos();
            ReflectionTestUtils.setField(pagamento, "id", pagamentoId);

            Long historicoTransacaoId = 1L;
            HistoricoTransacao historicoDoPagamento = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoDoPagamento, "id", historicoTransacaoId);

            pagamento.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoDoPagamento)));

            when(pagamentosRepository.findById(anyLong())).thenReturn(Optional.of(pagamento));

            when(pagamentosRepository.save(any(Pagamentos.class))).thenReturn(pagamento);

            when(historicoTransacaoRepository.save(any(HistoricoTransacao.class))).thenReturn(historicoDoPagamento);

            when(pagamentoMapper.retornarDadosPagamento(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                return new PagamentosResponse(1L, BigDecimal.valueOf(1000), LocalDate.now(), "teste", TiposCategorias.RECEITA, SubTipoCategoria.HERANCA);
            });

            assertDoesNotThrow(() -> {
                pagamentosService.atualizarPagamentoOuRecebimento(1L, BigDecimal.valueOf(1000), LocalDate.now()
                        , "teste", TiposCategorias.RECEITA, SubTipoCategoria.HERANCA);
            });

            verify(pagamentosRepository).findById(anyLong());
            verify(pagamentosRepository).save(any(Pagamentos.class));
            verify(historicoTransacaoRepository).save(any(HistoricoTransacao.class));
            verify(pagamentoMapper).retornarDadosPagamento(any(Pagamentos.class));

        }

        @Test
        @DisplayName("Teste do método de consultar valor de um pagamento ou Recebimento deve retornar valor")
        public void consultarValorDePagamentoOuRecebimento() {


            ContaUsuario saldoConta = new ContaUsuario();
            saldoConta.setSaldo(BigDecimal.valueOf(1200.00));

            when(contaUsuarioRepository.findById(anyLong())).thenReturn(Optional.of(saldoConta));

            assertDoesNotThrow(() -> {
                pagamentosService.consultarSaldoDaConta(1L);
                assertEquals(BigDecimal.valueOf(1200.00), saldoConta.getSaldo()
                        , "O saldo deveria ser igual a 1200");
            });

            verify(contaUsuarioRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Método criar Pagamento deve verificar se o saldo da conta possui valor para pagamento")
        public void metodoCriarPagamentoDeveTerValorSuficienteESerUmaDeterminadaContaParaRealizar() {

            String descricao = "teste";
            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            TiposCategorias tiposCategoria = TiposCategorias.DESPESA;
            SubTipoCategoria subTipoCategoria = SubTipoCategoria.EMPRESTIMOS;

            Long pagamentoId = 1L;
            Pagamentos pagamento = new Pagamentos();
            ReflectionTestUtils.setField(pagamento,"id",pagamentoId);

            Long historicoTransacaoId = 1L;
            HistoricoTransacao historicoTransacao = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoTransacao,"id",historicoTransacaoId);

            Long categoriaFinanceiraId = 1L;
            CategoriaFinanceira categoriaFinanceira = new CategoriaFinanceira();
            ReflectionTestUtils.setField(categoriaFinanceira, "id", categoriaFinanceiraId);

            Long usuarioId = 1L;
            Usuario usuario = new Usuario();
            ReflectionTestUtils.setField(usuario, "id", usuarioId);

            Long contaUsuarioId = 1L;
            ContaUsuario contaUsuario = new ContaUsuario();
            contaUsuario.setSaldo(BigDecimal.valueOf(1000));
            ReflectionTestUtils.setField(contaUsuario, "id", contaUsuarioId);

            when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(invocationOnMock -> {
                Pagamentos p = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(p,"id",pagamentoId);
                return p;
            });
            when(historicoTransacaoRepository.save(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
                HistoricoTransacao ht = invocationOnMock.getArgument(0);
                ReflectionTestUtils.setField(ht,"id",historicoTransacaoId);
                return ht;
            });
            when(categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(subTipoCategoria)).thenReturn(categoriaFinanceira);
            doNothing().when(pagamentosAssociation).associarPagamentoComCategoria(pagamentoId, categoriaFinanceiraId);
            doNothing().when(pagamentosAssociation).associarPagamentoATransacao(pagamentoId, historicoTransacaoId);

            when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
            when(contaUsuarioRepository.findById(anyLong())).thenReturn(Optional.of(contaUsuario));

            doNothing().when(pagamentosAssociation).associarPagamentoComConta(pagamentoId, contaUsuarioId);
            doNothing().when(pagamentosAssociation).associarPagamentoComUsuario(pagamentoId, usuarioId);

            when(pagamentoMapper.retornarDadosPagamento(pagamento)).thenAnswer(invocationOnMock -> {
                return new PagamentosResponse(1L, valor, data, descricao, tiposCategoria, subTipoCategoria);
            });


            assertEquals(BigDecimal.valueOf(1000), contaUsuario.getSaldo()
                    , "O saldo da conta de usuário deveria ser 1000");

            PagamentosRequest request = new PagamentosRequest(valor, data, descricao, tiposCategoria,
                    subTipoCategoria, categoriaFinanceiraId, usuarioId, contaUsuarioId);
            assertDoesNotThrow(() -> {
                pagamentosService.criarPagamento(request);
            });

            verify(categoriaFinanceiraRepository).encontrarCategoriaPeloSubTipo(subTipoCategoria);
            verify(pagamentosAssociation).associarPagamentoComCategoria(pagamentoId, categoriaFinanceiraId);
            verify(pagamentosAssociation).associarPagamentoATransacao(pagamentoId, historicoTransacaoId);
            verify(usuarioRepository).findById(anyLong());
            verify(contaUsuarioRepository,times(2)).findById(anyLong());
            verify(pagamentosAssociation).associarPagamentoComConta(pagamentoId, contaUsuarioId);
            verify(pagamentosAssociation).associarPagamentoComUsuario(pagamentoId, usuarioId);
            verify(pagamentoMapper).retornarDadosPagamento(pagamento);
            verify(pagamentosRepository).save(any(Pagamentos.class));
            verify(historicoTransacaoRepository).save(any(HistoricoTransacao.class));

        }


        @Nested
        @DisplayName("Métodos De Pagamentos/Recebimentos - Cenário de Erros")
        public class CenariosDeErros {


            @Test
            @DisplayName("Metodo criar Transação - Erro se data estiver incorreta")
            public void dataIncorretaNaoPodeDeixarCriarTransacao() {

                BigDecimal valor = BigDecimal.valueOf(1000);
                LocalDate data = LocalDate.of(2003, 1, 1);
                String descricao = "teste";
                TiposCategorias tiposCategoria = TiposCategorias.DESPESA;
                SubTipoCategoria subTipo = SubTipoCategoria.CONTA_INTERNET;

                assertThrowsExactly(DadosInvalidosException.class, () -> {
                    pagamentosService.criarTransacao(valor, data, descricao, tiposCategoria, subTipo);
                });
            }

            @Test
            @DisplayName("Metodo criar Transação - Erro se valor monetário estiver incorreto")
            public void valorMonetarioIncorretoNaoPodeDeixarCriarTransacao() {

                BigDecimal valor = BigDecimal.valueOf(0);
                LocalDate data = LocalDate.now();
                String descricao = "teste";
                TiposCategorias tiposCategoria = TiposCategorias.DESPESA;
                SubTipoCategoria subTipo = SubTipoCategoria.CONTA_INTERNET;

                assertThrowsExactly(DadosInvalidosException.class, () -> {
                    pagamentosService.criarTransacao(valor, data, descricao, tiposCategoria, subTipo);
                });
            }

            @Test
            @DisplayName("Metodo criar Recebimento - Erro se valor estiver como DESPESA")
            public void valorIncorretoNaoPodeDeixarCriarRecebimento() {

                BigDecimal valor = BigDecimal.valueOf(100);
                LocalDate data = LocalDate.now();
                String descricao = "teste";
                TiposCategorias tiposCategoria = TiposCategorias.DESPESA;
                SubTipoCategoria subTipo = SubTipoCategoria.CONTA_INTERNET;

                PagamentosRequest request = new PagamentosRequest
                        (valor, data, descricao, tiposCategoria, subTipo, 1L, 2L, 3L);
                assertThrowsExactly(DadosInvalidosException.class, () -> {
                    pagamentosService.criarRecebimento(request);
                });
            }

            @Test
            @DisplayName("Metodo criar Pagamento - Erro se valor estiver como RECEITA")
            public void valorIncorretoNaoPodeDeixarCriarPagamento() {

                BigDecimal valor = BigDecimal.valueOf(100);
                LocalDate data = LocalDate.now();
                String descricao = "teste";
                TiposCategorias tiposCategoria = TiposCategorias.RECEITA;
                SubTipoCategoria subTipo = SubTipoCategoria.SALARIO;

                PagamentosRequest request = new PagamentosRequest
                        (valor, data, descricao, tiposCategoria, subTipo, 1L, 2L, 3L);
                assertThrowsExactly(DadosInvalidosException.class, () -> {
                    pagamentosService.criarPagamento(request);
                });
            }

            @Test
            @DisplayName("Método criar um novo recebimento quando já existe um valor igual idêntico - Deve dar erro")
            public void jaExisteUmObjetoIgualCriado() {

                BigDecimal valor = BigDecimal.valueOf(1000);
                LocalDate data = LocalDate.now();
                String descricao = "teste";
                TiposCategorias tiposCategorias = TiposCategorias.RECEITA;
                SubTipoCategoria subTipoCategoria = SubTipoCategoria.DIVIDENDOS;

                Long recebimentoId = 1L;
                Long categoriaFinanId = 1L;
                Long usuarioId = 1L;
                Long contaUsuarioId = 1L;

                Pagamentos recebimento = new Pagamentos();
                recebimento.setValor(valor);
                recebimento.setData(data);
                recebimento.setDescricao(descricao);
                recebimento.setTiposCategorias(tiposCategorias);
                ReflectionTestUtils.setField(recebimento, "id", recebimentoId);

                when(pagamentosRepository.existTheSameData(valor, data, descricao, tiposCategorias)).thenReturn(true);

                PagamentosRequest recebimentoRequest = new
                        PagamentosRequest(valor, data, descricao, tiposCategorias, subTipoCategoria,
                        categoriaFinanId, usuarioId, contaUsuarioId);

                assertThrowsExactly(JaExisteException.class, () -> {
                    pagamentosService.criarRecebimento(recebimentoRequest);
                }, "O método de criar recebimento, deveria retornar um JaexisteException");

                assertTrue(pagamentosService.jaExisteUmPagamentoIgual(valor, data, descricao, tiposCategorias, subTipoCategoria)
                        , "O método não deveria deixar criar outro objeto idêntico ao que já existe");

                verify(pagamentosRepository, times(2)).existTheSameData(valor, data, descricao, tiposCategorias);
            }

        }
    }
}