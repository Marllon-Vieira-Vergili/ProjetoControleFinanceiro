package com.marllon.vieira.vergili.catalogo_financeiro.integration.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
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

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.COMBUSTIVEL;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.DESPESA_ALUGUEL;
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
        assertEquals(DESPESA,categoriasEncontradas.getFirst().getTiposCategorias()
                ,"Os tipos encontrados como DESPESA deve ser encontrado");
    }

    @Test
    @Sql(value = "/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método no Repositório para encontrar as categorias financeiras pelo subtipo")
    public void testeMetodoencontrarPorSubTipoCategoria(){
        CategoriaFinanceira categoriaEncontrada = categoriaFRepository.encontrarCategoriaPeloSubTipo(COMBUSTIVEL);
        assertTrue(categoriaEncontrada.getSubTipo().isTipoCategoriaDespesa()
                ,"A lista de deveria conter elementos do tipo despesa");
    }


    @Test
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método no Repositório para encontrar as categorias financeira pelo seu Tipo e Susbitpo")
    public void testeMetodoencontrarPorTipoAndSubtipo(){
        CategoriaFinanceira encontrado = categoriaFRepository.encontrarPorTipoAndSubtipo(DESPESA,DESPESA_ALUGUEL);
        assertNotNull(encontrado, "O Méodo deveria ter encontrado");
            assertEquals(DESPESA,encontrado.getTiposCategorias(),"O objeto não retornou nenhum valor do tipo DESPESA");
            assertEquals(DESPESA_ALUGUEL,encontrado.getSubTipo(),"O objeto não retornou nenhum subtipo DESPESA_ALUGUEL");

    }

    @AfterEach
    @DisplayName("Após execução dos métodos, apagar os dados do banco H2")
    public void afterTests(){
        jdbc.execute("DELETE from categoria_das_contas");

        //jdbc.execute("ALTER TABLE categoria_das_contas ALTER COLUMN ID RESTART WITH 1");
    }

}
