package com.marllon.vieira.vergili.catalogo_financeiro.repository;


import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para testar os métodos personalizados do meu repositório Conta Usuario
 * Métodos:existsByNomeAndSaldo(Booleano), encontrarContaPeloNome(ContaUsuario),
 * * encontrarContaPeloSaldo(ContaUsuario), encontrarpeloTipoDeConta(ContaUsuario)
 */

@DataJpaTest
@TestPropertySource(value = "/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContaUsuarioRepositoryTest {

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;



    /**
     * Esse método está testando a condiçao do método do repositório na parte de existir nome e saldo, se ele achar
     * deve retornar true no booleano condicional dele
     *
     */
    @Test
    @Sql("/sql/ContaUsuarioDados.sql")
    @DisplayName("Teste do método existsByNomeAndSaldo(booleano), se achar retornar TRUE")
    public void testeDoMetodoExistsByNomeAndSaldoSeAchar(){
        boolean contasEncontradas = contaUsuarioRepository.existsByNomeAndSaldo("Conta Pedro", BigDecimal.valueOf((1000.00)));
        assertTrue(contasEncontradas,"O método não encontrou nenhuma conta com esse nome e esse saldo informado");
    }

    /**
     * Esse método está testando a condiçao do método do repositório na parte de existir nome e saldo, se ele nao achar
     * deve retornar falso no booleano condicional dele
     *
     */
    @Test
    @Sql("/sql/ContaUsuarioDados.sql")
    @DisplayName("Teste do método existsByNomeAndSaldo(booleano), testar se ele nao achar, deve retornar FALSE")
    public void testeDoMetodoExistsByNomeAndSaldoSeNAOAchar(){
        boolean contaEncontrada = contaUsuarioRepository.existsByNomeAndSaldo("Conta Josimar", BigDecimal.valueOf(100.000));
        assertFalse(contaEncontrada, "O método não encontrou nenhum objeto no banco com esse nome e esse saldo informado");
    }

    /**
     * Esse método está testando a condiçao do método do repositório na parte encontrar a(s) conta(s) pelo seu nome(s)
     * , se ele nao achar
     * deve retornar falso no booleano condicional dele
     *
     */
    
    @Test
    @DisplayName("Testando metodo encontrarContaPeloNome(String nome), que espera uma Lista")
    @Sql("/sql/ContaUsuarioDados.sql")
    public void testeMetodoEncontrarContaPeloNomePassandoNomeComoParametro() {
        List<ContaUsuario> nomesContasEncontrados = contaUsuarioRepository.encontrarContaPeloNome("Salário Bruno");
        assertFalse(nomesContasEncontrados.isEmpty(), "O método não encontrou nenhuma conta com esse nome informado");
        for (ContaUsuario contaEncontrada : nomesContasEncontrados) {
            assertEquals(List.of(contaEncontrada), nomesContasEncontrados, "A conta encontrada não está condizente com o nome informado");
        }
    }

        @Test
        @DisplayName("Testando metodo encontrarContaPeloSaldo(BigDecimal saldo), que espera um saldo")
        @Sql("/sql/ContaUsuarioDados.sql")
        public void testeMetodoEncontrarContaPeloSaldoPassandoSaldoComoParametro(){
        BigDecimal valor = BigDecimal.valueOf(3600.00).setScale(2, RoundingMode.HALF_EVEN);
            List<ContaUsuario> contasEncontradasComEsseSaldo = contaUsuarioRepository.encontrarContaPeloSaldo(BigDecimal.valueOf(3600.00));
            assertFalse(contasEncontradasComEsseSaldo.isEmpty(),"O método não encontrou nenhuma conta com o valor desse saldo informado");
            for(ContaUsuario contasEncontradas: contasEncontradasComEsseSaldo){
                assertEquals(valor,contasEncontradas.getSaldo()
,"As contas encontradas não estão condizentes com esse saldo encontrado");
            }
        }
    }
