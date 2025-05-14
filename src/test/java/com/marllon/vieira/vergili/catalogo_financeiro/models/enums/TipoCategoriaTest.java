package com.marllon.vieira.vergili.catalogo_financeiro.models.enums;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TipoCategoriaTest {


    @BeforeAll
    public static void beforeAll(){
        System.out.println("Todos esses testes são os métodos dentro do enum TiposCategorias");
    }

    @ParameterizedTest
    @CsvSource(value = {"1, DESPESA", "2, RECEITA"})
    @DisplayName("Teste do método para buscar a categoria pelo numero")
    public void testandoSeMetodoBuscaPeloValor(int valor, TiposCategorias tipoEsperado){
        TiposCategorias resultadoAtual = TiposCategorias.buscarCategoriasPeloNumero(valor);
        TiposCategorias resultadoEsperado = tipoEsperado;
        assertEquals(resultadoEsperado,resultadoAtual,"O resultado esperado é que ele encontre" +
                " o tipo de categoria pelo seu número no ENUMERADOR");
    }

    @ParameterizedTest
    @EnumSource(TiposCategorias.class)
    @Order(2)
    @DisplayName("Teste do método para buscar o tipo de categoria pretendido pelo seu nome")
    public void testarSeMetodoEstaBuscandoPeloNome(TiposCategorias nome){
        Optional<TiposCategorias> tipoCategoriaEsperado = TiposCategorias.buscarCategoriasPeloNome(nome);
        assertEquals(tipoCategoriaEsperado.orElse(null),nome,"O Tipo de Categoria deve ser igual " +
                "aos Tipos de Categoria Existentes no Enum");
    }

    @ParameterizedTest
    @Order(3)
    @EnumSource(value = SubTipoCategoria.class,names = { "CONTA_LUZ",
            "CONTA_AGUA",
            "CONTA_INTERNET",
            "CARTAO_CREDITO",
            "CONTA_TELEFONE",
            "IMPOSTOS",
            "LAZER",
            "COMBUSTIVEL",
            "ALIMENTACAO",
            "DESPESA_ALUGUEL",
            "EMPRESTIMOS"})
    @DisplayName("Testando o método para mostrar todos os subtipos do tipo DESPESA")
    public void testandoOsMetodosParaMostrarTodosOsSubtiposDoEnumDespesa(){
        List<SubTipoCategoria> valoresEsperados = List.of(CONTA_LUZ,
                CONTA_AGUA,
                CONTA_INTERNET,
                CARTAO_CREDITO,
                CONTA_TELEFONE,
                IMPOSTOS,
                LAZER,
                COMBUSTIVEL,
                ALIMENTACAO,
                DESPESA_ALUGUEL,
                EMPRESTIMOS);
        List<SubTipoCategoria> valoresEncontrados = TiposCategorias.mostrarTodasDespesas();
        assertEquals(valoresEsperados,valoresEncontrados,"O método deve retornar os valores " +
                " encontrados iguais aos valores esperados que contem no enum subTipoCategoria associado ao" +
                " tipo de categoria escolhido");
    }

    @ParameterizedTest
    @EnumSource(value = SubTipoCategoria.class,names = {
            "SALARIO",
            "PRESENTE",
            "HERANCA",
            "DIVIDENDOS",
            "RENDA_FIXA",
            "RENDA_ALUGUEL",
            "OUTROS"})
    @Order(4)
    @DisplayName("Método para testar o metodo de mostrarTodasReceitas")
    public void testandoMetodosParaMostrarTodosOsSubtiposDoEnumReceita(){
        List<SubTipoCategoria> valoresEncontrados = List.of(SALARIO,
                PRESENTE,
                HERANCA,
                DIVIDENDOS,
                RENDA_FIXA,
                RENDA_ALUGUEL,
                OUTROS);
        List<SubTipoCategoria> valorEsperado = TiposCategorias.mostrarTodasReceitas();
        assertEquals(valorEsperado,valoresEncontrados,"Os valores esperados que ele mostra" +
                " deve ser igual aos encontrados na lista de subtipoCategoria, do tipo Receita");
    }

    @ParameterizedTest
    @CsvSource({"RECEITA, SALARIO","DESPESA,CONTA_INTERNET"})
    @DisplayName("Testando o método de associar um Tipo de categoria ao Seu SubTipo relacionado")
    public void testarMetodoVerificarAssociacaoDoTipoCategoriaASubcategoriaPretendida(TiposCategorias categoria,
                                                                             SubTipoCategoria subtipoPretendido){
    TiposCategorias resultadoEsperado = TiposCategorias.verificarAssociacaoEntreTipoECategoria(categoria,subtipoPretendido);
    assertEquals(categoria,resultadoEsperado, "O subtipoPretendido deve estar associado ao Tipo de categoria" +
            " dele");

    }
}
