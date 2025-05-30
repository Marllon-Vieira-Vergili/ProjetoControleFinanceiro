package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioCreateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposContasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.ContaUsuarioImpl;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource("/application-test.properties")
public class ContaUsuarioImplTest {

    @Mock
    private ContaUsuarioRepository contaUsuarioRepository;

    @Mock
    private ContaUsuarioAssociation contaUsuarioAssociation;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PagamentosService pagamentosService;

    @Mock
    private ContaUsuarioMapper contaUsuarioMapper;

    @Mock
    private PagamentosRepository pagamentosRepository;

    @InjectMocks
    private ContaUsuarioImpl contaUsuarioImpl;


    @BeforeEach
    public void instanciarMocksAntesDeCadaMetodo() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("ContasUsuarioImpl - Cenários de Sucesso")
    public class cenariosSucessoMetodos{

        @Test
        @DisplayName("Teste do método de criar Conta usuário")
        public void metodoCriarContaDeveCriarESerAssociadoAUmUsuario(){

            //Instanciando uma conta usuário e usuário
            Long idUsuario = 1L;
            Usuario usuarioTeste = new Usuario();
            ReflectionTestUtils.setField(usuarioTeste,"id",idUsuario);

            Long contaId = 1L;
            ContaUsuario contaUsuarioTeste  = new ContaUsuario();
            contaUsuarioTeste.setTipoConta(TiposContas.CONTA_CORRENTE);
            contaUsuarioTeste.setSaldo(BigDecimal.ZERO);
            contaUsuarioTeste.setNome("Conta teste");
            //contaUsuarioTeste.setUsuarioRelacionado(usuarioTeste);

            when(contaUsuarioRepository.save(any(ContaUsuario.class))).thenReturn(contaUsuarioTeste);

            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioTeste));

            doNothing().when(contaUsuarioAssociation).associarContaComUsuario(contaUsuarioTeste.getId(),idUsuario);

            when(contaUsuarioMapper.retornarDadosContaUsuario(contaUsuarioTeste))
                    .thenReturn(new ContaUsuarioResponse(contaUsuarioTeste.getId(),
                            contaUsuarioTeste.getNome(),
                            contaUsuarioTeste.getSaldo(),
                            contaUsuarioTeste.getTipoConta()));

            ContaUsuarioCreateRequest request = new ContaUsuarioCreateRequest("Conta teste"
                    ,TiposContas.CONTA_CORRENTE);

            ContaUsuarioResponse responseEsperada = new ContaUsuarioResponse
                    (1L,"Conta teste",BigDecimal.ZERO,TiposContas.CONTA_CORRENTE);

            assertDoesNotThrow(()->{
                ContaUsuarioResponse contaUsuarioResponse = contaUsuarioImpl.criarConta(request,idUsuario);


            });


            verify(contaUsuarioRepository,times(2)).save(any(ContaUsuario.class));
            verify(contaUsuarioAssociation).associarContaComUsuario(contaUsuarioTeste.getId(),idUsuario);
            verify(contaUsuarioMapper).retornarDadosContaUsuario(contaUsuarioTeste);


        }

        @Test
        @DisplayName("Teste do metodo adicionarSaldo")
        public void metodoAdicionarSaldoDeveAdicionarValorAConta() {
            //Instanciando um valor pra teste
            Long contaUsuarioId = 1L;
            ContaUsuario contaUsuarioTeste = new ContaUsuario();
            ReflectionTestUtils.setField(contaUsuarioTeste,"id",contaUsuarioId);
            contaUsuarioTeste.setSaldo(BigDecimal.valueOf(1000));
            contaUsuarioTeste.setTipoConta(TiposContas.CONTA_POUPANCA);
            contaUsuarioTeste.setNome("conta poupança teste");

            when(contaUsuarioRepository.save(any(ContaUsuario.class))).thenAnswer(
                    invocationOnMock -> {
                        ContaUsuario contaUsuario = invocationOnMock.getArgument(0);
                        ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);
                        return contaUsuario;
                    });

            //Agora testando o método com um valor já criado pra teste
            when(contaUsuarioRepository.findById(contaUsuarioId)).thenReturn(Optional.of(contaUsuarioTeste));


            contaUsuarioImpl.adicionarSaldo(contaUsuarioId,BigDecimal.valueOf(1000));
            assertEquals(BigDecimal.valueOf(2000),contaUsuarioTeste.getSaldo());

            verify(contaUsuarioRepository).save(any(ContaUsuario.class));
            verify(contaUsuarioRepository).findById(contaUsuarioId);
        }

        @Test
        @DisplayName("Teste do metodo subtrairSaldo")
        public void metodoSubtrairSaldoDeveSubtrairValorAConta(){
            //Instanciando um valor pra teste
            Long contaUsuarioId = 1L;
            ContaUsuario contaUsuarioTeste = new ContaUsuario();
            ReflectionTestUtils.setField(contaUsuarioTeste,"id",contaUsuarioId);
            contaUsuarioTeste.setSaldo(BigDecimal.valueOf(1000));
            contaUsuarioTeste.setTipoConta(TiposContas.CONTA_CORRENTE);
            contaUsuarioTeste.setNome("conta poupança teste");

            when(contaUsuarioRepository.save(any(ContaUsuario.class))).thenAnswer(
                    invocationOnMock -> {
                        ContaUsuario contaUsuario = invocationOnMock.getArgument(0);
                        ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);
                        return contaUsuario;
                    });

            //Agora testando o metodo de subtrair
            when(contaUsuarioRepository.findById(contaUsuarioId)).thenReturn(Optional.of(contaUsuarioTeste));

            contaUsuarioImpl.subtrairSaldo(contaUsuarioId,BigDecimal.valueOf(1500));

            assertEquals(BigDecimal.valueOf(-500),contaUsuarioTeste.getSaldo());

            verify(contaUsuarioRepository).save(any(ContaUsuario.class));
            verify(contaUsuarioRepository).findById(contaUsuarioId);

        }

        @Test
        @DisplayName("Metodo procurar ContaUsuarioPelaId")
        public void deveEncontrarContaUsuarioPelaId(){

            Long contaId = 1L;
            ContaUsuario conta = new ContaUsuario();
            ReflectionTestUtils.setField(conta,"id",contaId);

            when(contaUsuarioRepository.findById(contaId)).thenReturn(Optional.of(conta));

            assertDoesNotThrow(()-> contaUsuarioImpl.encontrarContaPorId(contaId)
                    ,"O método encontrar pela id deve encontrar a conta");

            verify(contaUsuarioRepository).findById(contaId);
        }

        @Test
        @DisplayName("Teste do método procurar conta pelo Nome - Deve encontrar")
        public void deveEncontrarContasPeloNome(){

            Long idConta1 = 1L;
            ContaUsuario conta1 = new ContaUsuario();
            conta1.setNome("teste");
            ReflectionTestUtils.setField(conta1,"id",idConta1);

            Long idConta2 = 2L;
            ContaUsuario conta2 = new ContaUsuario();
            conta2.setNome("teste");
            ReflectionTestUtils.setField(conta2,"id",idConta2);

            List<ContaUsuario> listaContas = new ArrayList<>(List.of(conta1,conta2));

            when(contaUsuarioRepository.encontrarContaPeloNome("teste")).thenReturn(listaContas);

            when(contaUsuarioMapper.retornarDadosContaUsuario(conta1)).thenAnswer(invocationOnMock -> {
                return new ContaUsuarioResponse(idConta1,
                        conta1.getNome(),
                        conta1.getSaldo(),
                        conta1.getTipoConta());
            });

            when(contaUsuarioMapper.retornarDadosContaUsuario(conta2)).thenAnswer(invocationOnMock -> {
                return new ContaUsuarioResponse(idConta2,
                        conta1.getNome(),
                        conta1.getSaldo(),
                        conta1.getTipoConta());
            });


            List<ContaUsuarioResponse> listaResponse = assertDoesNotThrow(()->
                             contaUsuarioImpl.encontrarContaPorNome("teste")
                    ,"O método deve encontrar e retornar as contas encontradas com este nome");


            assertEquals(2,listaResponse.size(),"O método deve retornar 2 Objetos");
            assertEquals(idConta1,listaResponse.get(0).id(),"A Id da primeira conta encontrada deve ser 1");
            assertEquals("teste",listaResponse.get(0).nome(),"O nome da conta 1 deve ser igual ao esperado");
            assertEquals(idConta2,listaResponse.get(1).id(),"A Id da segunda conta encontrada deve ser 1");
            assertEquals("teste",listaResponse.get(1).nome(),"O nome da conta 2 deve ser igual ao esperado");

            verify(contaUsuarioRepository).encontrarContaPeloNome("teste");
            verify(contaUsuarioMapper).retornarDadosContaUsuario(conta1);
            verify(contaUsuarioMapper).retornarDadosContaUsuario(conta2);
        }

        @Test
        @DisplayName("Metodo encontrar todas Contas deve retornar a lista de todas")
        public void metodoEncontrarTodasContasDeveRetornarPageable(){

            Long idConta1 = 1L;
            ContaUsuario conta1 = new ContaUsuario();
            conta1.setNome("teste");
            ReflectionTestUtils.setField(conta1,"id",idConta1);

            Long idConta2 = 2L;
            ContaUsuario conta2 = new ContaUsuario();
            conta2.setNome("teste");
            ReflectionTestUtils.setField(conta2,"id",idConta2);

            List<ContaUsuario> contasEncontradas = new ArrayList<>(List.of(conta1,conta2));

            when(contaUsuarioRepository.findAll()).thenReturn(contasEncontradas);

            Page<ContaUsuario> paginaContas = new PageImpl<>(contasEncontradas);

            assertDoesNotThrow(() ->{
                contaUsuarioImpl.encontrarTodas(paginaContas.getPageable());
            },"O método não deveria retornar nenhuma exceção de erro");

            assertFalse(paginaContas.isEmpty(),"Paginas contas deveria possuir 2 elementos");
            verify(contaUsuarioRepository).findAll();
        }

        @Test
        @DisplayName("Metodo alterar Nome Conta Usuario - Cenário de Sucesso")
        public void alterarNomeContaUsuarioDeveSerSucesso(){

            Long contaId = 1L;
            ContaUsuario contaAserAtualizada = new ContaUsuario();
            contaAserAtualizada.setNome("conta teste");
            contaAserAtualizada.setTipoConta(TiposContas.CONTA_CORRENTE);
            contaAserAtualizada.setSaldo(BigDecimal.ZERO);
            ReflectionTestUtils.setField(contaAserAtualizada,"id",contaId);

            when(contaUsuarioRepository.findById(contaId)).thenReturn(Optional.of(contaAserAtualizada));

            when(contaUsuarioRepository.save(contaAserAtualizada)).thenReturn(contaAserAtualizada);

            when(contaUsuarioMapper.retornarDadosContaUsuario(any(ContaUsuario.class)))
                    .thenAnswer(invocationOnMock -> {
                        return new ContaUsuarioResponse(contaId,contaAserAtualizada.getNome()
                                ,contaAserAtualizada.getSaldo(),
                                contaAserAtualizada.getTipoConta());
                    });


            String nomeAlterado = "conta Joao";
            assertDoesNotThrow(()->{
                contaUsuarioImpl.alterarNomeDeUmaConta(contaId,nomeAlterado);
            });

            assertEquals(nomeAlterado,contaAserAtualizada.getNome()
                    ,"O nome da conta a ser alterado deveria ser igual ao esperado");
            verify(contaUsuarioRepository).findById(contaId);
            verify(contaUsuarioRepository).save(contaAserAtualizada);
            verify(contaUsuarioMapper).retornarDadosContaUsuario(any(ContaUsuario.class));

        }

        @Test
        @DisplayName("Método remover Conta Usuário com sucesso")
        public void removerContaUsuarioComSucesso(){

            //Instanciando os valores e associando
            Long usuarioId = 1L;
            Long categoriaFinanId = 1L;
            Long contaUserId = 1L;
            Long historicoTransacaoId = 1L;
            Long pagamentoId = 1L;

            Usuario usuarioTeste = new Usuario();
            ReflectionTestUtils.setField(usuarioTeste,"id",usuarioId);

            ContaUsuario contaTeste = new ContaUsuario();
            ReflectionTestUtils.setField(contaTeste,"id",contaUserId);

            CategoriaFinanceira categoriaTeste = new CategoriaFinanceira();
            ReflectionTestUtils.setField(categoriaTeste,"id",categoriaFinanId);

            Pagamentos pagamentoTeste = new Pagamentos();
            ReflectionTestUtils.setField(pagamentoTeste,"id",pagamentoId);

            HistoricoTransacao historicoTeste = new HistoricoTransacao();
            ReflectionTestUtils.setField(historicoTeste,"id",historicoTransacaoId);

            //Associando os valores
            contaTeste.setUsuarioRelacionado(usuarioTeste);
            contaTeste.setTransacoesRelacionadas(new ArrayList<>(List.of(historicoTeste)));
            contaTeste.setPagamentosRelacionados(new ArrayList<>(List.of(pagamentoTeste)));
            contaTeste.setCategoriasRelacionadas(new ArrayList<>(List.of(categoriaTeste)));

            //Assertando que estão associados...
            assertEquals(usuarioTeste,contaTeste.getUsuarioRelacionado(),"A conta deveria estar associado ao usuário");
            assertTrue(contaTeste.getTransacoesRelacionadas().contains(historicoTeste),"A conta deveria estar com a transação associada");
            assertTrue(contaTeste.getPagamentosRelacionados().contains(pagamentoTeste),"A conta deveria estar com o pagamento associado");
            assertTrue(contaTeste.getCategoriasRelacionadas().contains(categoriaTeste),"A conta deveria estar com a categoria financeira associada");

            //Agora simulando os resultados
            when(contaUsuarioRepository.findById(contaUserId)).thenReturn(Optional.of(contaTeste));

            doNothing().when(contaUsuarioAssociation).desassociarContaDeUsuario(contaUserId,usuarioId);
            doNothing().when(contaUsuarioAssociation).desassociarContaDeCategoria(contaUserId,categoriaFinanId);
            doNothing().when(contaUsuarioAssociation).desassociarContaDePagamento(contaUserId,pagamentoId);
            doNothing().when(contaUsuarioAssociation).desassociarContaDeHistoricoDeTransacao(contaUserId,historicoTransacaoId);

             assertDoesNotThrow(()-> contaUsuarioImpl.deletarConta(contaUserId)
                    ,"O método deveria remover a conta normalmente");



            //Verificar se chamou os mocks
            verify(contaUsuarioRepository).findById(contaUserId);
            verify(contaUsuarioAssociation).desassociarContaDeUsuario(contaUserId,usuarioId);
            verify(contaUsuarioAssociation).desassociarContaDeCategoria(contaUserId,categoriaFinanId);
            verify(contaUsuarioAssociation).desassociarContaDePagamento(contaUserId,pagamentoId);
            verify(contaUsuarioAssociation).desassociarContaDeHistoricoDeTransacao(contaUserId,historicoTransacaoId);
            verify(contaUsuarioRepository).delete(contaTeste);
        }

        @Test
        @DisplayName("Verificar tipo de Conta - Cenário de Sucesso")
        public void verificarTipoContaPelaId(){

            Long contaUsuarioId = 1L;
            ContaUsuario contaUsuario = new ContaUsuario();
            contaUsuario.setTipoConta(TiposContas.CONTA_INVESTIMENTO);
            ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);

            when(contaUsuarioRepository.findById(contaUsuarioId)).thenReturn(Optional.of(contaUsuario));

            TiposContas tipoDeConta = assertDoesNotThrow(()
                    ->contaUsuarioImpl.verificarTipoConta(contaUsuarioId));

            assertEquals(TiposContas.CONTA_INVESTIMENTO,tipoDeConta,"Deveria ser igual ao resultado esperado");

            verify(contaUsuarioRepository).findById(contaUsuarioId);
        }

        @Test
        @DisplayName("Teste Metodo SeSaldoForNegativo deve retornar true")
        public void testeSeSaldoForNegativo(){

            ContaUsuario contaUsuario = new ContaUsuario();
            contaUsuario.setSaldo(BigDecimal.valueOf(-100));

            assertTrue(contaUsuarioImpl.seSaldoEstiverNegativo(contaUsuario.getSaldo())
                    ,"Deveria Retornar True que o saldo da conta está negativo");

        }

        @Test
        @DisplayName("Teste Metodo SeSaldoForPositivo deve retornar true")
        public void testeSeSaldoForPositivo(){

            ContaUsuario contaUsuario = new ContaUsuario();
            contaUsuario.setSaldo(BigDecimal.valueOf(100));

            assertTrue(contaUsuarioImpl.seSaldoEstiverPositivo(contaUsuario.getSaldo())
                    ,"Deveria Retornar True que o saldo da conta está positivo");

        }

        @Test
        @DisplayName("Teste do método se já existe uma conta igual pelo nome e tipo de conta")
        public void testandoMetodoJaExisteUmaContaIgual(){

            ContaUsuario contaUsuario = new ContaUsuario();
            contaUsuario.setNome("teste");
            contaUsuario.setTipoConta(TiposContas.CONTA_POUPANCA);

            when(contaUsuarioRepository.encontrarContaPeloNome("teste")).thenReturn(List.of(contaUsuario));
            when(contaUsuarioRepository.encontrarPeloTipoDeConta(TiposContas.CONTA_POUPANCA.name()))
                    .thenReturn(List.of(contaUsuario));

            assertTrue(contaUsuarioImpl.jaExisteUmaContaIgual("teste",TiposContas.CONTA_POUPANCA)
                    ,"Deveria Retornar TRUE, pois já existe");

        }

        @Test
        @DisplayName("Método deve consultar e retornar saldo de uma conta")
        public void deveConsultarSaldoERetornarValor(){

            Long contaUserId = 1L;
            ContaUsuario contauser = new ContaUsuario();
            contauser.setSaldo(BigDecimal.valueOf(100));

            when(contaUsuarioRepository.findById(contaUserId)).thenReturn(Optional.of(contauser));

            assertEquals(BigDecimal.valueOf(100),contaUsuarioImpl.consultarSaldo(contaUserId),
                    "Ambos deveriam retornar o mesmo valor");

        }
    }

    @Nested
    @DisplayName("Cenários de Erros e Exceptions")
    class CenariosDeErrosAndExceptions{

        @Test
        @DisplayName("Criar Usuario Retorna UsuarioNaoEncontrado")
        public void metodoCriarContaDeveRetornarUsuarioNaoEncontrado(){

            //Instanciando uma conta usuário e usuário

            Long contaId = 1L;
            ContaUsuario contaUsuarioTeste  = new ContaUsuario();
            contaUsuarioTeste.setTipoConta(TiposContas.CONTA_CORRENTE);
            contaUsuarioTeste.setSaldo(BigDecimal.ZERO);
            contaUsuarioTeste.setNome("Conta teste");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

            ContaUsuarioCreateRequest request = new ContaUsuarioCreateRequest("Conta teste"
                    ,TiposContas.CONTA_CORRENTE);

            assertThrowsExactly(UsuarioNaoEncontrado.class,()->{
                contaUsuarioImpl.criarConta(request,1L);
            });

            verify(usuarioRepository).findById(1L);

            }
        }
    @Test
    @DisplayName("Criar Conta deve Retornar AssociationErrorException")
    public void deveRetornarAssociationErrorException(){

        //Instanciando uma conta usuário e usuário

        Long contaId = 1L;
        ContaUsuario contaUsuarioTeste  = new ContaUsuario();
        contaUsuarioTeste.setTipoConta(TiposContas.CONTA_CORRENTE);
        contaUsuarioTeste.setSaldo(BigDecimal.ZERO);
        contaUsuarioTeste.setNome("Conta teste");


        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario,"id",usuarioId);


        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        doThrow(new AssociationErrorException(super.toString()))
                .when(contaUsuarioAssociation).associarContaComUsuario(any(), eq(usuarioId));

        ContaUsuarioCreateRequest request = new ContaUsuarioCreateRequest("Conta teste"
                ,TiposContas.CONTA_CORRENTE);

        assertThrowsExactly(AssociationErrorException.class,()->{
            contaUsuarioImpl.criarConta(request,usuarioId);
        });

        verify(usuarioRepository).findById(1L);
        verify(contaUsuarioAssociation).associarContaComUsuario(any(), eq(usuarioId));
    }

    @Test
    @DisplayName("Teste do método procurar conta pelo Nome - Não deve encontrar por um nome")
    public void deveEncontrarContasPeloNome(){


        List<ContaUsuario> listaContas = new ArrayList<>(List.of());

        when(contaUsuarioRepository.encontrarContaPeloNome("carlos")).thenReturn(listaContas);


        assertTrue(listaContas.isEmpty(),"Lista de contas não tem nenhum objeto");

        assertThrowsExactly(ContaNaoEncontrada.class,()->
                        contaUsuarioImpl.encontrarContaPorNome("carlos")
                ,"O método deveria retornar um erro que nao encontrou nenhuma conta com esse nome");

        verify(contaUsuarioRepository).encontrarContaPeloNome("carlos");
    }

    @Test
    @DisplayName("Metodo encontrar todas Contas não deve encontrar e retornar uma exception")
    public void metodoEncontrarTodasContasDeveRetornarExcecaoDeErro(){


        List<ContaUsuario> contasEncontradas = new ArrayList<>(List.of());

        when(contaUsuarioRepository.findAll()).thenReturn(contasEncontradas);

        Page<ContaUsuario> paginaContas = new PageImpl<>(contasEncontradas);

        assertThrowsExactly(ContaNaoEncontrada.class,() ->{
            contaUsuarioImpl.encontrarTodas(paginaContas.getPageable());
        },"O método deveria jogar exatamente essa exception, pois não foi encontrado nenhum valor na lista");

        assertTrue(paginaContas.isEmpty(),"Paginas não deveria possuir nenhum elemento");
        verify(contaUsuarioRepository).findAll();
        }

    @Test
    @DisplayName("Método remover Conta Usuário deve retornar Exception de Desassociacao")
    public void removerContaUsuarioComSucesso(){

        //Instanciando os valores e associando
        Long usuarioId = 1L;
        Long contaUserId = 1L;


        Usuario usuarioTeste = new Usuario();
        ReflectionTestUtils.setField(usuarioTeste,"id",usuarioId);

        ContaUsuario contaTeste = new ContaUsuario();
        ReflectionTestUtils.setField(contaTeste,"id",contaUserId);

        //Associando os valores
        contaTeste.setUsuarioRelacionado(usuarioTeste);

        //Assertando que estão associados...
        assertEquals(usuarioTeste,contaTeste.getUsuarioRelacionado()
                ,"A conta deveria estar associado ao usuário");

        //Agora simulando os resultados
        when(contaUsuarioRepository.findById(contaUserId)).thenReturn(Optional.of(contaTeste));

        doThrow(new DesassociationErrorException("Erro ao desassociar conta de usuário do usuário."))
                .when(contaUsuarioAssociation).desassociarContaDeUsuario(contaUserId,usuarioId);


        assertThrows(DesassociationErrorException.class,()-> contaUsuarioImpl.deletarConta(contaUserId)
                ,"O método deveria retornar a exceção de desassociação");



        //Verificar se chamou os mocks
        verify(contaUsuarioRepository).findById(contaUserId);
        verify(contaUsuarioAssociation).desassociarContaDeUsuario(contaUserId,usuarioId);

    }
    @Test
    @DisplayName("Verificar tipo de Conta - Cenário de Exception")
    public void verificarTipoContaPelaId(){

        Long contaUsuarioId = 1L;
        ContaUsuario contaUsuario = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);

        when(contaUsuarioRepository.findById(contaUsuarioId)).thenReturn(Optional.of(contaUsuario));

        assertThrowsExactly(TiposContasNaoEncontrado.class,()
                ->contaUsuarioImpl.verificarTipoConta(contaUsuarioId)
                ,"O método deveria retornar exceção, pois não tem conta de usuário associado");


        verify(contaUsuarioRepository).findById(contaUsuarioId);
    }
    }
