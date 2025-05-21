package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.PagamentosImpl;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

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
    private UsuarioRepository usuarioRepository;

    @Mock
    private HistoricoTransacaoRepository historicoRepository;

    @Mock
    private PagamentosAssociation pagamentosAssociation;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @BeforeEach
    public void inicializarOsMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do método de criar Recebimento, deve retornar Exception se usuário não digitar dataCorreta")
    @Order(1)
    public void deveRetornarExceptionSeUsuarioNaoDigitarDataValidaAoCriarRecebimento() {
        Long categoriaFinanceiraId = 1L;
        Long usuarioId = 1L;
        Long contaUsuarioId = 1L;
        //Criando um Recebimento para teste
        BigDecimal valor = BigDecimal.valueOf(100);
        LocalDate dataErrada = LocalDate.of(2000, 1, 1);
        Pagamentos novoRecebimento = new Pagamentos();
        novoRecebimento.setData(dataErrada);
        novoRecebimento.setValor(valor);
        novoRecebimento.setTiposCategorias(TiposCategorias.RECEITA);

        novoRecebimento.setDescricao("teste");

        PagamentosRequest request = new PagamentosRequest(valor, dataErrada, "teste",
                TiposCategorias.RECEITA, SubTipoCategoria.DIVIDENDOS, categoriaFinanceiraId, usuarioId, contaUsuarioId);

        assertFalse(pagamentosService.dataEstaCorreta(dataErrada));

        assertThrows(DadosInvalidosException.class, () -> {
            pagamentosService.criarRecebimento(request);
        });

    }

    @Test
    @DisplayName("Teste do método de criar Recebimento, deve retornar Exception se usuário não digitar nenhum valor certo")
    @Order(2)
    public void deveRetornarExceptionSeUsuarioNaoDigitarAlgumValorValidoAoCriarRecebimento() {
        //Criando um Recebimento para teste
        Long categoriaFinanceiraId = 1L;
        Long contaUsuarioId = 1L;
        Long usuarioId = 1L;
        LocalDate dataValida = LocalDate.now().plusDays(1);
        BigDecimal valorIncorreto = BigDecimal.valueOf(0);
        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setData(LocalDate.now().plusDays(1));
        novoPagamento.setValor(valorIncorreto);
        novoPagamento.setDescricao("teste");

        //Pulando a primeira verificação do If
        assertTrue(pagamentosService.dataEstaCorreta(dataValida));

        PagamentosRequest request = new PagamentosRequest(valorIncorreto,
                dataValida, "teste",
                TiposCategorias.RECEITA, SubTipoCategoria.DIVIDENDOS, categoriaFinanceiraId, usuarioId, contaUsuarioId);


        assertFalse(pagamentosService.valorEstaCorreto(valorIncorreto));

        assertThrows(DadosInvalidosException.class, () -> {
            pagamentosService.criarRecebimento(request);
        });
    }


    @Test
    @DisplayName("Teste do método de criar Recebimento, já associado a suas respectivas entidades")
    @Order(4)
    public void deveCriarRecebimentoJaAssociadoASeusRespectivosRelacionamentos() {
        //Criando um Recebimento para teste
        Long recebimentoId = 1L;

        Long categoriaId = 1L;
        CategoriaFinanceira categoriaSemNada = new CategoriaFinanceira();
        ReflectionTestUtils.setField(categoriaSemNada, "id", categoriaId);

        Long contaUsuarioId = 1L;
        ContaUsuario contaSemNada = new ContaUsuario();
        ReflectionTestUtils.setField(contaSemNada, "id", contaUsuarioId);

        Long usuarioId = 1L;
        Usuario usuarioSemNada = new Usuario();
        ReflectionTestUtils.setField(usuarioSemNada, "id", usuarioId);

    //    Long historicoTransacaoId = 1L;
      //  HistoricoTransacao historicoSemNada = new HistoricoTransacao();
       // ReflectionTestUtils.setField(historicoSemNada,"id",historicoTransacaoId);

        Pagamentos recebimentoCriadoTeste = new Pagamentos();
        recebimentoCriadoTeste.setTiposCategorias(TiposCategorias.RECEITA);
        recebimentoCriadoTeste.setData(LocalDate.now().plusDays(1));
        recebimentoCriadoTeste.setValor(BigDecimal.valueOf(100));
        recebimentoCriadoTeste.setDescricao("teste");
        ReflectionTestUtils.setField(recebimentoCriadoTeste,"id",recebimentoId);
        recebimentoCriadoTeste.setCategoriaRelacionada(new CategoriaFinanceira(TiposCategorias.RECEITA, SubTipoCategoria.DIVIDENDOS));

        assertTrue(pagamentosService.dataEstaCorreta(recebimentoCriadoTeste.getData()));
        assertTrue(pagamentosService.valorEstaCorreto(recebimentoCriadoTeste.getValor()));

        when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(
                invocationOnMock -> {
                    Pagamentos recebimento = invocationOnMock.getArgument(0);
                    ReflectionTestUtils.setField(recebimento,"id",recebimentoId);
                    return recebimento;
                });

        PagamentosResponse responseEsperada = new PagamentosResponse(recebimentoId
                , recebimentoCriadoTeste.getValor()
                , recebimentoCriadoTeste.getData(),
                recebimentoCriadoTeste.getDescricao(),
                recebimentoCriadoTeste.getTiposCategorias(),
                recebimentoCriadoTeste.getCategoriaRelacionada().getSubTipo()
                , contaUsuarioId
                , usuarioId);


        PagamentosRequest request = new PagamentosRequest(recebimentoCriadoTeste.getValor(),
                recebimentoCriadoTeste.getData()
                ,recebimentoCriadoTeste.getDescricao()
                ,recebimentoCriadoTeste.getTiposCategorias()
                ,recebimentoCriadoTeste.getCategoriaRelacionada().getSubTipo()
                , categoriaId,usuarioId,contaUsuarioId);

        pagamentosService.criarRecebimento(request);


       //doNothing().when(pagamentosAssociation).associarPagamentoATransacao(recebimentoId, historicoTransacaoId);
        doNothing().when(pagamentosAssociation).associarPagamentoComUsuario(recebimentoId, usuarioId);
        doNothing().when(pagamentosAssociation).associarPagamentoComCategoria(recebimentoId, categoriaId);
        doNothing().when(pagamentosAssociation).associarPagamentoComConta(recebimentoId, contaUsuarioId);

        when(pagamentoMapper.retornarDadosPagamento(recebimentoCriadoTeste)).thenReturn(responseEsperada);


        verify(pagamentosRepository).save(any());
        verify(pagamentoMapper).retornarDadosPagamento(recebimentoCriadoTeste);
       //verify(pagamentosAssociation).associarPagamentoATransacao(recebimentoId, historicoTransacaoId);
        verify(pagamentosAssociation).associarPagamentoComUsuario(recebimentoId, usuarioId);
        verify(pagamentosAssociation).associarPagamentoComCategoria(recebimentoId, categoriaId);
        verify(pagamentosAssociation).associarPagamentoComConta(recebimentoId, contaUsuarioId);
    }

    @Test
    @Order(5)
    @DisplayName("Teste do método criar pagamento deve retornar exceção se data estiver incorreta")
    public void seDataEstiverErradaDeveRetornarExceptionAoCriarPagamento(){
        Long idPagamento = 1L;
        Pagamentos pagamentoComValorNaDataErrado = new Pagamentos();
        ReflectionTestUtils.setField(pagamentoComValorNaDataErrado,"id",idPagamento);
        LocalDate dataErrada = LocalDate.of(2004,8,16);
        pagamentoComValorNaDataErrado.setData(dataErrada);
        pagamentoComValorNaDataErrado.setValor(BigDecimal.valueOf(100));
        pagamentoComValorNaDataErrado.setDescricao("teste");
        pagamentoComValorNaDataErrado.setTiposCategorias(TiposCategorias.DESPESA);
        pagamentoComValorNaDataErrado.setCategoriaRelacionada(new CategoriaFinanceira(TiposCategorias.DESPESA,SubTipoCategoria.ALIMENTACAO));

        Long idUsuario = 1L;
        Usuario usuarioSemValor = new Usuario();
        ReflectionTestUtils.setField(usuarioSemValor,"id",idUsuario);

        Long idContaUsuario = 1L;
        ContaUsuario contaUsuarioSemValor = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuarioSemValor,"id",idContaUsuario);

        Long idCategoriaFinanceira = 1L;
        CategoriaFinanceira categoriaFinanceiraSemValor = new CategoriaFinanceira();
        ReflectionTestUtils.setField(categoriaFinanceiraSemValor,"id",idCategoriaFinanceira);

        PagamentosRequest request = new PagamentosRequest(pagamentoComValorNaDataErrado.getValor(),
                pagamentoComValorNaDataErrado.getData(),
                pagamentoComValorNaDataErrado.getDescricao(),
                pagamentoComValorNaDataErrado.getTiposCategorias(),
                pagamentoComValorNaDataErrado.getCategoriaRelacionada().getSubTipo(),
                idCategoriaFinanceira, idUsuario,idContaUsuario);

        when(pagamentosRepository.save(any(Pagamentos.class))).thenReturn(pagamentoComValorNaDataErrado);

        assertThrows(DadosInvalidosException.class,(()->{
            pagamentosService.criarPagamento(request);
        }));

    }

    @Test
    @Order(6)
    @DisplayName("Teste do método criar pagamento deve retornar exceção se valor estiver incorreto")
    public void seValorPagamentoEstiverErradoDeveRetornarExcecaoDadosInvalidosException(){
        Long pagamentoId = 1L;
        Pagamentos pagamentoCriadoValorMonetarioErrado = new Pagamentos();
        ReflectionTestUtils.setField(pagamentoCriadoValorMonetarioErrado,"id",pagamentoId);
        pagamentoCriadoValorMonetarioErrado.setValor(BigDecimal.valueOf(0));
        pagamentoCriadoValorMonetarioErrado.setData(LocalDate.now().plusDays(1));
        pagamentoCriadoValorMonetarioErrado.setDescricao("teste");
        pagamentoCriadoValorMonetarioErrado.setTiposCategorias(TiposCategorias.DESPESA);
        pagamentoCriadoValorMonetarioErrado.setCategoriaRelacionada(new CategoriaFinanceira(TiposCategorias.DESPESA,SubTipoCategoria.CARTAO_CREDITO));

        Long categoriaFinanceiraId = 1L;
        CategoriaFinanceira categoriaFinanceiraSemValor = new CategoriaFinanceira();
        ReflectionTestUtils.setField(categoriaFinanceiraSemValor,"id",categoriaFinanceiraId);

        Long usuarioId = 1L;
        Usuario usuarioSemValor = new Usuario();
        ReflectionTestUtils.setField(usuarioSemValor,"id",usuarioId);

        Long contaUsuarioId = 1L;
        ContaUsuario contaUsuarioSemValor = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuarioSemValor,"id",contaUsuarioId);

        PagamentosRequest request = new PagamentosRequest(pagamentoCriadoValorMonetarioErrado.getValor(),
                pagamentoCriadoValorMonetarioErrado.getData(),
                pagamentoCriadoValorMonetarioErrado.getDescricao(),
                pagamentoCriadoValorMonetarioErrado.getTiposCategorias(),
                pagamentoCriadoValorMonetarioErrado.getCategoriaRelacionada().getSubTipo(),
                categoriaFinanceiraId,usuarioId,contaUsuarioId);

        when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(
                invocationOnMock -> {
                    Pagamentos pagamento = invocationOnMock.getArgument(0);
                    ReflectionTestUtils.setField(pagamento,"id",pagamentoId);
                    return pagamento;
                });

        assertThrows(DadosInvalidosException.class,()->{
            pagamentosService.criarPagamento(request);
        });
    }
    @Test
    @Order(7)
    @DisplayName("Teste do método criar Pagamento ja associado")
    public void verificarSePagamentoCriadoFunciona(){

        Long pagamentoId = 1L;
        Pagamentos pagamentoCriado = new Pagamentos();
        ReflectionTestUtils.setField(pagamentoCriado,"id",pagamentoId);
        pagamentoCriado.setData(LocalDate.now().plusDays(1));
        pagamentoCriado.setValor(BigDecimal.valueOf(100));
        pagamentoCriado.setDescricao("teste");
        pagamentoCriado.setTiposCategorias(TiposCategorias.DESPESA);
        pagamentoCriado.setCategoriaRelacionada(new CategoriaFinanceira(TiposCategorias.DESPESA,SubTipoCategoria.EMPRESTIMOS));

        PagamentosRequest request = new PagamentosRequest(pagamentoCriado.getValor(),
                pagamentoCriado.getData(),
                pagamentoCriado.getDescricao(),
                pagamentoCriado.getTiposCategorias(),
                SubTipoCategoria.EMPRESTIMOS,
                1L,1L,1L);
        pagamentosService.criarPagamento(request);
        
        when(pagamentosRepository.save(any(Pagamentos.class))).thenAnswer(
                invocationOnMock -> {
                    Pagamentos pagamento = invocationOnMock.getArgument(0);
                    ReflectionTestUtils.setField(pagamento,"id",pagamentoId);
                    return pagamento;
                });

        Long contaUsuarioId = 1L;
        ContaUsuario contaSemDados = new ContaUsuario();
        ReflectionTestUtils.setField(contaSemDados,"id",contaUsuarioId);

        Long usuarioId = 1L;
        Usuario usuarioSemdados = new Usuario();
        ReflectionTestUtils.setField(usuarioSemdados,"id",usuarioId);

        PagamentosResponse respostaEsperada = new PagamentosResponse(pagamentoId,
                pagamentoCriado.getValor(),
                pagamentoCriado.getData(),
                pagamentoCriado.getDescricao(),
                pagamentoCriado.getTiposCategorias(),
                pagamentoCriado.getCategoriaRelacionada().getSubTipo(),
                contaUsuarioId,usuarioId);


    }
}