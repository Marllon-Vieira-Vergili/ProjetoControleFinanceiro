package com.marllon.vieira.vergili.catalogo_financeiro.integration.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.ALIMENTACAO;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testando classes de Entidades, utilizando Reflection Tests
 */


public class CategoriaFinanceiraTest {



    CategoriaFinanceira novaCategoria;


    @BeforeEach
    public void instanciarNovaCategoria(){
        novaCategoria = new CategoriaFinanceira();
        ReflectionTestUtils.setField(novaCategoria, "tiposCategorias", DESPESA);
        ReflectionTestUtils.setField(novaCategoria,"subTipo", ALIMENTACAO);
        ReflectionTestUtils.setField(novaCategoria,"id",5L);

    }

    @Test
    @DisplayName("Testar se a Id do CategoriaFinanceira está retornando a id relacionada")
    public void verificarIdGeradaCategoriaFinanceira(){
        Long id = (Long) ReflectionTestUtils.getField(novaCategoria, "id");
        assertEquals(5L,id,"A id informada deve ser a mesma que foi instanciada");
    }

    @Test
    @DisplayName("Testar se o atributo TipoCategoria da CategoriaFinanceira está retornando o Tipo Relacionado")
    public void verificarOTipoCategoria(){
        assertEquals(DESPESA,ReflectionTestUtils.getField(novaCategoria,
                "tiposCategorias"),"O atributo deve retornar o tipocategoria.DESPESA ");
    }

    @Test
    @DisplayName("Testar se o atributo SubTipoCategoria da CategoriaFinanceira está retornando o SubTipo Relacionado")
    public void verificarOSubTipo(){
        assertEquals(ALIMENTACAO,ReflectionTestUtils.getField(novaCategoria,"subTipo"),"O atributo deve retornar o SubTipo Relacionado");
    }
}
