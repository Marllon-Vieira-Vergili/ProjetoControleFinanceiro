package com.marllon.vieira.vergili.catalogo_financeiro.unit;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.HistoricoTransacaoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.HistoricoTransacaoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.HistoricoTransacaoService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.HistoricoTransacaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class HistoricoTransacaoImplTest {

    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @InjectMocks
    private HistoricoTransacaoImpl historicoService;

    @Mock
    private HistoricoTransacaoAssociation historicoTransacaoAssociation;

    @Mock
    private HistoricoTransacaoMapper mapper;

    @Mock
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Histórico de Transação - Cenários de Sucesso")
    public class CenariosDeSucesso{

        @Test
        @DisplayName("Encontrar Transação por Id - Cenário de Sucesso")
        public void deveEncontrarATransacaoPelaId(){

            HistoricoTransacao transacao = new HistoricoTransacao();
            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tipoCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subtipo = SubTipoCategoria.SALARIO;

            when(historicoTransacaoRepository.findById(anyLong())).thenReturn(Optional.of(transacao));

            when(mapper.retornarHistoricoTransacao(transacao)).thenAnswer(invocationOnMock -> {
                return new HistoricoTransacaoResponse(1L, valor,data,descricao,tipoCategoria,subtipo);
            });

            assertDoesNotThrow(()->{
                historicoService.encontrarTransacaoPorid(1L);
            },"O método deveria retornar o histórico de transação criado por essa ID");
            verify(historicoTransacaoRepository).findById(any());
            verify(mapper).retornarHistoricoTransacao(transacao);
        }

        @Test
        @DisplayName("Encontrar Transação pela Data - Cenário de Sucesso")
        public void deveEncontrarATransacaoPelaData(){
            HistoricoTransacao historicoLocalizado = new HistoricoTransacao();
            historicoLocalizado.setData(LocalDate.now());

            HistoricoTransacao historicodois = new HistoricoTransacao();
            historicodois.setData(LocalDate.now().plusMonths(1));

            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tipoCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subtipo = SubTipoCategoria.SALARIO;

            List<HistoricoTransacao> listaHistoricos = new ArrayList<>(List.of(historicodois,historicoLocalizado));

            when(historicoTransacaoRepository.encontrarTransacoesPelaData(LocalDate.now())).thenReturn(listaHistoricos);

            when(mapper.retornarHistoricoTransacao(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
                return  new HistoricoTransacaoResponse(1L,valor,data,descricao,tipoCategoria,subtipo);
            });
            assertDoesNotThrow(()->{
                historicoService.encontrarTransacaoPorData(LocalDate.now());
            });
        }

        @Test
        @DisplayName("Encontrar Todas as Transações - Cenário de Sucesso")
        public void deveEncontrarTodasAsTransacoes(){

            HistoricoTransacao historicoLocalizado = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoLocalizado,"id",1L);

            HistoricoTransacao historicodois = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoLocalizado,"id",2L);

            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tipoCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subtipo = SubTipoCategoria.SALARIO;

            List<HistoricoTransacao> listaHistoricos = new ArrayList<>(List.of(historicodois,historicoLocalizado));
            Page<HistoricoTransacao> paginaHistoricos = new PageImpl<>(listaHistoricos);

            when(historicoTransacaoRepository.findAll()).thenReturn(listaHistoricos);

            when(mapper.retornarHistoricoTransacao(any(HistoricoTransacao.class)))
                    .thenAnswer(invocationOnMock -> {
                return new HistoricoTransacaoResponse(1L,valor,data,descricao,tipoCategoria,subtipo);
            });

            assertDoesNotThrow(()->{
                historicoService.encontrarTodasTransacoes(paginaHistoricos.getPageable());
            },"O método deveria encontrar todos as página de histórico");

            verify(historicoTransacaoRepository).findAll();
            verify(mapper,times(2)).retornarHistoricoTransacao(any(HistoricoTransacao.class));
        }

        @Test
        @DisplayName("Encontrar Transação por usuário pela id usuario - Cenário de Sucesso")
        public void deveEncontrarTransacaoPelaIdDoUsuario(){

            BigDecimal valor = BigDecimal.valueOf(1000);
            LocalDate data = LocalDate.now();
            String descricao = "teste";
            TiposCategorias tipoCategoria = TiposCategorias.RECEITA;
            SubTipoCategoria subtipo = SubTipoCategoria.SALARIO;

            HistoricoTransacao historicoAssociado = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoAssociado,"id",1L);

            Usuario usuarioLocalizado = new Usuario();
            ReflectionTestUtils.setField(usuarioLocalizado,"id",1L);
            usuarioLocalizado.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoAssociado)));

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioLocalizado));

            when(mapper.retornarHistoricoTransacao(historicoAssociado)).thenAnswer(invocationOnMock -> {
                return new HistoricoTransacaoResponse(1L,valor,data,descricao,tipoCategoria,subtipo);
            });

            assertDoesNotThrow(()->{
                historicoService.encontrarHistoricoTransacaoPorUsuario(1L);
            },"Deveria encontrar o histórico de transação associado a esse usuário");

            verify(usuarioRepository).findById(1L);
            verify(mapper).retornarHistoricoTransacao(historicoAssociado);
        }

        @Test
        @DisplayName("Encontrar Transação por Tipo de Categoria informado - Cenário de Sucesso")
        public void deveEncontrarTransacaoPeloTipoCategoriaInformado(){

            HistoricoTransacao transacaoum = new HistoricoTransacao();
            transacaoum.setTiposCategorias(TiposCategorias.RECEITA);

            HistoricoTransacao transacaodois = new HistoricoTransacao();
            transacaodois.setTiposCategorias(TiposCategorias.RECEITA);

            List<HistoricoTransacao> listaHistoricos = new ArrayList<>(List.of(transacaoum,transacaodois));

            when(historicoTransacaoRepository.findAll()).thenReturn(listaHistoricos);

            when(mapper.retornarHistoricoTransacao(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
               return new HistoricoTransacaoResponse(1L,BigDecimal.valueOf(1000),LocalDate.now(),"teste"
                       ,TiposCategorias.RECEITA,SubTipoCategoria.HERANCA);
            });
            assertDoesNotThrow(()->{
                historicoService.encontrarHistoricoTransacaoPorTipo(TiposCategorias.RECEITA);
            },"O método deveria retornar uma lista de todos os históricos de transações do tipo RECEITA");
            verify(historicoTransacaoRepository).findAll();
            verify(mapper,times(2)).retornarHistoricoTransacao(any(HistoricoTransacao.class));

        }
        @Test
        @DisplayName("Encontrar Transação pela Id Categoria Financeira - Cenário de Sucesso")
        public void deveEncontrarTransacaoPelaIdCategoriaFinanceira(){

            CategoriaFinanceira categoria = new CategoriaFinanceira();
            ReflectionTestUtils.setField(categoria,"id",1L);

            HistoricoTransacao transacaoassociadoAEssaCategoria = new HistoricoTransacao();
            HistoricoTransacao transacaodois = new HistoricoTransacao();

            transacaoassociadoAEssaCategoria.setCategoriaRelacionada(categoria);
            transacaodois.setCategoriaRelacionada(categoria);

            List<HistoricoTransacao> listaDeTransacoes = new ArrayList<>(List.of(transacaoassociadoAEssaCategoria,transacaodois));

            when(historicoTransacaoRepository.findAll()).thenReturn(listaDeTransacoes);
            when(mapper.retornarHistoricoTransacao(any(HistoricoTransacao.class))).thenAnswer(invocationOnMock -> {
               return new HistoricoTransacaoResponse(1L,BigDecimal.valueOf(1000),LocalDate.now(),"teste"
                       ,TiposCategorias.RECEITA,SubTipoCategoria.HERANCA);
            });

            assertDoesNotThrow(()->{
                historicoService.encontrarHistoricoTransacaoPorCategoria(1L);
            },"O método deveria encontrar os históricos de transações que estão associados a essa categoria");

            verify(historicoTransacaoRepository).findAll();
            verify(mapper,times(2)).retornarHistoricoTransacao(any(HistoricoTransacao.class));
        }
    }

    @Nested
    @DisplayName("Histórico de Transação - Cenários de Erros")
    public class CenariosDeErros{

        @Test
        @DisplayName("Encontrar Transação pela Data - Cenário que Data não está correta")
        public void deveRetornarErroSeDataEstiverIncorreta(){

            LocalDate dataErrada = LocalDate.of(2004,1,1);


            assertThrowsExactly(DadosInvalidosException.class,()->{
                historicoService.encontrarTransacaoPorData(dataErrada);
            },"O método deveria retornar erro DadosInvalidosException, pois a data está errada, muito antiga");

            //Não deve chamar o repositório, pois a verificação vem antes
            verifyNoInteractions(historicoTransacaoRepository);
        }

        @Test
        @DisplayName("Encontrar Transação pela Data - Cenário que nao tem nenhum histórico nessa data")
        public void deveRetornarHistoricoNaoEncontradoSeNaoTiverNenhumEncontradoNaData(){


            when(historicoTransacaoRepository.encontrarTransacoesPelaData(any())).thenReturn(List.of());

            assertThrowsExactly(HistoricoTransacaoNaoEncontrado.class,()->{
                historicoService.encontrarTransacaoPorData(LocalDate.now());
            },"O método deveria retornar erro DadosInvalidosException, pois a data está errada, muito antiga");

            //Não deve chamar o repositório, pois a verificação vem antes
            verify(historicoTransacaoRepository).encontrarTransacoesPelaData(any());
        }

        @Test
        @DisplayName("Encontrar Historico Atualização por data - Quando data está incorreta")
        public void encontrarHistoricoPorDataQuandoDataEstaIncorreta(){

            LocalDate dataIncorreta = LocalDate.of(2010,1,1);

            assertThrowsExactly(DadosInvalidosException.class,()->{
               historicoService.encontrarTransacaoPorData(dataIncorreta);
            },"O método deveria retornar o DadosInvalidosException, pois a data informada está incorreta");

        }

        @Test
        @DisplayName("Encontrar Historico Atualização por data - Quando Não tem nenhum histórico na data informada")
        public void encontrarHistoricoPorDataQuandoNaDataInformadaNaoPossuiNenhumHistorico(){

            LocalDate dataQueNaoExisteHistorico = LocalDate.now().plusMonths(1);

            when(historicoTransacaoRepository.encontrarTransacoesPelaData(dataQueNaoExisteHistorico))
                    .thenThrow(HistoricoTransacaoNaoEncontrado.class);

            assertThrowsExactly(HistoricoTransacaoNaoEncontrado.class,()->{
                historicoService.encontrarTransacaoPorData(dataQueNaoExisteHistorico);
            },"O método deveria retornar o HistoricoTransacaoNaoEncontrado, " +
                    "pois a data informada não possui nenhum histórico de transação");

            verify(historicoTransacaoRepository).encontrarTransacoesPelaData(dataQueNaoExisteHistorico);
        }

        @Test
        @DisplayName("Encontrar Todos Históricos de transações - Quando Não tem nenhum histórico de Transação no Banco")
        public void encontrarTodosHistoricoQuandoNaoEncontrouNenhumHistoricoTransacao(){



            when(historicoTransacaoRepository.findAll()).thenThrow(HistoricoTransacaoNaoEncontrado.class);

            List<HistoricoTransacao> listaVazia = new ArrayList<>(List.of());
            Page<HistoricoTransacao> paginaVazia = new PageImpl<>(listaVazia);

            assertThrowsExactly(HistoricoTransacaoNaoEncontrado.class,()->{
                historicoService.encontrarTodasTransacoes(paginaVazia.getPageable());
            },"O método deveria retornar o HistoricoTransacaoNaoEncontrado, " +
                    "pois Não foi encontrado nenhum histórico de transação");

            verify(historicoTransacaoRepository).findAll();
        }
    }
}


