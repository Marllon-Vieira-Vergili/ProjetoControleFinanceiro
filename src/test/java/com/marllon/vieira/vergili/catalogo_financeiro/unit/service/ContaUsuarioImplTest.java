package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.ContaUsuarioImpl;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestPropertySource("/application-test.properties")
public class ContaUsuarioImplTest {

    @Mock
    private ContaUsuarioRepository contaUsuarioRepository;

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
}