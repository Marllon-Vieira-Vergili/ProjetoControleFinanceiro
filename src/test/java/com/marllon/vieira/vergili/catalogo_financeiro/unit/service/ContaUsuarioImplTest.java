package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioCreateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
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
    }
