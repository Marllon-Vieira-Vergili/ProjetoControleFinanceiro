package com.marllon.vieira.vergili.catalogo_financeiro.integration.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.*;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de teste para testar os métodos personalizados do meu repositório Categoria Financeira
 *Métodos: encontrarPorTipoCategoria,encontrarPorSubtipoCategoria, findByTipoAndSubtipo
 *
 */
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoriaFinanceiraRepositoryTest{


    @Autowired
    private CategoriaFinanceiraRepository categoriaFRepository;

    @Autowired
    private JdbcTemplate jdbc;



    @Test
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método no Repositório para encontrar as categorias financeiras pelo tipo categoria")
    public void testeMetodoencontrarPorTipoCategoria(){
        List<CategoriaFinanceira> categoriasEncontradas = categoriaFRepository.encontrarPorTipoCategoria(DESPESA);
        assertFalse(categoriasEncontradas.isEmpty(),"A lista da categoria é vazia");
        for(CategoriaFinanceira categoriaEncontrada: categoriasEncontradas){
            assertEquals(DESPESA,categoriaEncontrada.getTiposCategorias(),"Os tipos encontrados como DESPESA deve ser encontrado");
        }
    }

    @Test
    @Sql(value = "/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método no Repositório para encontrar as categorias financeiras pelo subtipo")
    public void testeMetodoencontrarPorSubTipoCategoria(){
        List<CategoriaFinanceira> categoriasEncontradas = categoriaFRepository.encontrarPorSubtipoCategoria(COMBUSTIVEL);
        assertFalse(categoriasEncontradas.isEmpty(),"A lista de categoria é vazia");
        for(CategoriaFinanceira categoriaEncontrada: categoriasEncontradas){
            assertEquals(COMBUSTIVEL,categoriaEncontrada.getSubTipo(),"Os tipos encontrados como COMBUSTIVEL devem ser encontrados");
        }
    }


    @Test
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método no Repositório para encontrar as categorias financeira pelo seu Tipo e Susbitpo")
    public void testeMetodoencontrarPorTipoAndSubtipo(){
        List<CategoriaFinanceira> encontrados = categoriaFRepository.encontrarPorTipoAndSubtipo(DESPESA,DESPESA_ALUGUEL);
        assertFalse(encontrados.isEmpty(),"A lista da categoria financeira está vazia");
        for(CategoriaFinanceira objetosEncontrados: encontrados){
            assertEquals(DESPESA,objetosEncontrados.getTiposCategorias(),"O objeto não retornou nenhum valor do tipo DESPESA");
            assertEquals(DESPESA_ALUGUEL,objetosEncontrados.getSubTipo(),"O objeto não retornou nenhum subtipo DESPESA_ALUGUEL");
        }
    }

    @AfterEach
    @DisplayName("Após execução dos métodos, apagar os dados do banco H2")
    public void afterTests(){
        jdbc.execute("DELETE from categoria_das_contas");

        jdbc.execute("ALTER TABLE categoria_das_contas ALTER COLUMN ID RESTART WITH 1");
    }

}
