package com.marllon.vieira.vergili.catalogo_financeiro.models.enums;

import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubtipoCategoriaTest {


    @BeforeAll
    public static void antesDeExecutar(){
        System.out.println("Essa classe é execução apenas dos métodos do ENUM Subcategoria");

    }

    @ParameterizedTest
    @EnumSource(value = SubTipoCategoria.class,names = {
            "CONTA_LUZ",
            "CONTA_AGUA",
            "CONTA_INTERNET",
            "CARTAO_CREDITO",
            "CONTA_TELEFONE",
            "IMPOSTOS",
            "LAZER",
            "DESPESA_ALUGUEL",
            "COMBUSTIVEL",
            "ALIMENTACAO",
            "EMPRESTIMOS"
    })
    @Order(1)
    @DisplayName("SubTipos do tipo Despesa, devem retornar TRUE no método isTipoCategoriaDespesa")
    public void deveRetornarOsTiposDespesa(SubTipoCategoria subtipo) {
        assertTrue(subtipo.isTipoCategoriaDespesa(), "O método deve retornar valores " +
                "de DESPESAS ");
    }


    @ParameterizedTest
    @EnumSource(value = SubTipoCategoria.class, names = {
            "SALARIO",
            "PRESENTE",
            "HERANCA",
            "DIVIDENDOS",
            "RENDA_FIXA",
            "RENDA_ALUGUEL",
            "OUTROS"
    })
    @Order(2)
    @DisplayName("SubTipos do tipo receita, devem retornar TRUE no método isTipoCategoriaReceita")
    public void deveRetornarOsTiposReceita(SubTipoCategoria subtipo){
        assertTrue(subtipo.isTipoCategoriaReceita(),"O método deve retornar os " +
                "valores do tipo RECEITA");
    }

    @ParameterizedTest
    @Order(3)
    @EnumSource(SubTipoCategoria.class)
    @DisplayName("Teste do método de encontrar pelo nome do Subtipo")
    public void deveEncontrarPeloNome(SubTipoCategoria subtipo){
        Optional<SubTipoCategoria> subTipoEncontrado = SubTipoCategoria.encontrarSubTipoCategoriaPorNome(subtipo.name());
        assertTrue(subTipoEncontrado.isPresent(),"O método deve encontrar um Subtipo com o nome informado");
        assertEquals(subtipo,subTipoEncontrado.get() ,"O subtipo passado no parâmetro" +
                " deve ser o mesmo que existe no ENUM");
    }

    @ParameterizedTest
    @Order(4)
    @CsvSource(value = {"1,CONTA_LUZ","2,CONTA_AGUA","3,CONTA_INTERNET","4,CARTAO_CREDITO","5,CONTA_TELEFONE","6,IMPOSTOS"
            ,"7,LAZER","8,COMBUSTIVEL", "9,ALIMENTACAO","10,DESPESA_ALUGUEL","11,EMPRESTIMOS",
            "20,SALARIO","21,PRESENTE","22,HERANCA","23,DIVIDENDOS","24,RENDA_FIXA","25,RENDA_ALUGUEL","26,OUTROS"})
    @DisplayName("Teste do método de encontrar o subtipoCategoria pelo seu número no enum")
    public void deveEncontrarOValorPeloNumeroDoEnumDaSubCategoria(int num, SubTipoCategoria subtipoInformado){
        SubTipoCategoria subTipoAtual = SubTipoCategoria.encontrarSubTipoCategoriaPeloNumero(subtipoInformado.getValor());
        SubTipoCategoria subTipoEsperado = SubTipoCategoria.encontrarSubTipoCategoriaPeloNumero(num);
        assertEquals(subTipoAtual,subTipoEsperado,"O Número informado no parâmetro do método deve retornar o nome" +
                " do enum, igual ao subtipo que foi encontrado");
    }
}
