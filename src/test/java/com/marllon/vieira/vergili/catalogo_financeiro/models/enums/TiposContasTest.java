package com.marllon.vieira.vergili.catalogo_financeiro.models.enums;

import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas.buscarTipoContaPeloNome;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas.todosTiposValidos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TiposContasTest {

    @BeforeAll
    public static void beforeAllExecuteThis(){
        System.out.println("Esta classe de testes, é para testar o funcionamento dos métodos dentro do enum TiposContas");
    }


    @ParameterizedTest
    @EnumSource(value = TiposContas.class,names = {"CONTA_POUPANCA",
            "CONTA_SALARIO",
            "CONTA_INVESTIMENTO",
            "CONTA_CORRENTE"})
    @DisplayName("Testando o método de mostrar Todos Os Tipos de Contas do Enum")
    public void testandoMetodoDeMostrarTodosTiposContasDoEnum(TiposContas tiposConta){
        String todosTiposContas = todosTiposValidos().toString();
        assertTrue(todosTiposContas.contains(tiposConta.name()),
                "O valor esperado deve ser igual ao valor atual que foi passado");
    }

    @ParameterizedTest
    @EnumSource(value = TiposContas.class,names = {"CONTA_POUPANCA",
    "CONTA_SALARIO","CONTA_INVESTIMENTO","CONTA_CORRENTE"})
    @DisplayName("Testar se o método de buscar pelo TipoConta Funciona")
    public void testarSeBuscaPeloTipoConta(TiposContas tiposEsperado){
        boolean tipoInformado = buscarTipoContaPeloNome(tiposEsperado);
        assertTrue(tipoInformado,"O tipo de conta informado é válido, mas ele não deveria retornar false");
    }

    @ParameterizedTest
    @CsvSource(value = {"1,CONTA_POUPANCA",
            " 2,CONTA_SALARIO",
            " 3,CONTA_INVESTIMENTO",
            " 4,CONTA_CORRENTE"})
    @DisplayName("Teste do método para verificar se acha o Tipo pelo seu número associado")
    public void testarSeBuscaTipoContaPeloSeuNumero(int numero, TiposContas tipoContaInformado){
        TiposContas tiposContas = TiposContas.buscarContasPeloNumero(numero);
        TiposContas resultadoAtual = TiposContas.buscarContasPeloNumero(tipoContaInformado.getValor());
        assertEquals(resultadoAtual,tiposContas);
    }

}
