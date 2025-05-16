package com.marllon.vieira.vergili.catalogo_financeiro.integration.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas.CONTA_CORRENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContaUsuarioTest {

    ContaUsuario contausuario;


    @BeforeEach()
    public void instanciarUmaConta(){
        contausuario = new ContaUsuario();
        ReflectionTestUtils.setField(contausuario,"nome","Conta do Marllon");
        ReflectionTestUtils.setField(contausuario,"saldo",new BigDecimal(1500));
        ReflectionTestUtils.setField(contausuario,"tipoConta",CONTA_CORRENTE);

        ReflectionTestUtils.setField(contausuario,"id",100L);
    }

    @Test
    @DisplayName("Testando o atributo da id da Conta(fins de prática)")
    public void testIdAtributo(){
        Long idEquivalente =(Long) ReflectionTestUtils.getField(contausuario,"id");
        assertEquals(100L, idEquivalente);
    }

    @Test
    @DisplayName("Testando o nome é igual")
    public void testNomeEquals(){
        String nome = "Conta do Marllon";
        assertEquals(nome,ReflectionTestUtils.getField(contausuario, "nome"),"O nome deve ser equivalente");
    }
    @Test
    @DisplayName("Testando se o atributo saldo é equivalente")
    public void testeSaldoEquals(){
        BigDecimal saldo = new BigDecimal(1500);
        assertEquals(saldo,ReflectionTestUtils.getField(contausuario, "saldo"),"O saldo deve ser equivalente");
    }
}
