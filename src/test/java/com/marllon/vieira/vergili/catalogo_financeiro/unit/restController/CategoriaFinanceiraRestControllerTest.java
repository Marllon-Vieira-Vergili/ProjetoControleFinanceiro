package com.marllon.vieira.vergili.catalogo_financeiro.unit.restController;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.restController.CategoriaFinanceiraController;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc
@WebMvcTest(CategoriaFinanceiraController.class)
@TestPropertySource("classpath:application-test.properties")
public class CategoriaFinanceiraRestControllerTest {

    static CategoriaFinanceira categoriaMock;

    @Mock
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;


    @MockitoBean
    private CategoriaFinanceiraService categoriaService;

    @Mock
    private CategoriaFinanceiraMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAllInstanciatingOneObject(){
        categoriaMock = new CategoriaFinanceira();
        categoriaMock.setTiposCategorias(TiposCategorias.DESPESA);
        categoriaMock.setSubTipo(SubTipoCategoria.ALIMENTACAO);
        ReflectionTestUtils.setField(categoriaMock,"id",1L);
    }

    @BeforeEach
    public void beforeEach(){
        //jdbc.execute("insert into categoria_das_contas(tipos_categorias,subtipo_categoria) values('DESPESA','ALIMENTACAO')");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("Deve encontrar a categoria pela sua id")
    public void deveBuscarCategoriaPorId() throws Exception {
        //Arrange - simular o comportamento do service
        Long id = 1L;
        CategoriaFinanceiraResponse responseEsperada =
                new CategoriaFinanceiraResponse(id, TiposCategorias.DESPESA,SubTipoCategoria.ALIMENTACAO);

        //Mockando
        when(categoriaService.encontrarCategoriaPorId(id)).thenReturn(Optional.of(responseEsperada));

        //Act + Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categoria/mostrarTipoCategoriaPeloId/{id}",id))
                .andExpect(status().isFound()) //Para retornar status 302(Found)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipoCategoria").value("DESPESA"))
                .andExpect(jsonPath("$.subTipo").value("ALIMENTACAO"));
    }

    @Test
    @Order(2)
    @DisplayName("Deve mostrar a pagina de todos os valores encontrados de tiposCategorias")
    public void testeDeveRetornarAPaginaComTodasCategoriasFinanceiras() throws Exception {

        CategoriaFinanceiraResponse categoriaUm = new CategoriaFinanceiraResponse(1L,TiposCategorias.DESPESA,SubTipoCategoria.ALIMENTACAO);
       CategoriaFinanceiraResponse categoriaDois = new CategoriaFinanceiraResponse(2L,TiposCategorias.RECEITA,SubTipoCategoria.DIVIDENDOS);

        List<CategoriaFinanceiraResponse> listaCategoria = List.of(categoriaUm,categoriaDois);
        Page<CategoriaFinanceiraResponse> paginaCategoriaFinanceira = new PageImpl<>(listaCategoria);

                when(categoriaService.encontrarTodasCategorias(any(Pageable.class))).thenReturn(paginaCategoriaFinanceira);


                assertDoesNotThrow(()->categoriaFinanceiraRepository.findAll());
                mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/categoria/mostrarTodasAsPaginas"))
                        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content[0].id").value(1L))
                        .andExpect(jsonPath("$.content[0].tipoCategoria").value(TiposCategorias.DESPESA.name()))
                        .andExpect(jsonPath("$.content[0].subTipo").value(SubTipoCategoria.ALIMENTACAO.name()))
                        .andExpect(jsonPath("$.content[1].id").value(2L))
                        .andExpect(jsonPath("$.content[1].tipoCategoria").value(TiposCategorias.RECEITA.name()))
                        .andExpect(jsonPath("$.content[1].subTipo").value(SubTipoCategoria.DIVIDENDOS.name()));
    }
    


}
