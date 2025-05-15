package com.marllon.vieira.vergili.catalogo_financeiro.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.CategoriaFinanceiraImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.*;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.class)
@ActiveProfiles("test")
class CategoriaFinanceiraImplTest {


    //Mock de todos os repositórios das outras entidades, pra testar associação com essa

    @Mock
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;
    // Mock para o Spring injetar no serviço real

    @Mock
    private ContaUsuarioRepository contaUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Mock
    private PagamentosRepository pagamentosRepository;

    @Mock
    private CategoriaFinanceiraAssociation categoriaFinanceiraAssociation;

    @InjectMocks
    private CategoriaFinanceiraImpl categoriaFinanceiraService;
    // Injetando a classe de serviço real com o repositório mockado

    @Mock
    private CategoriaFinanceiraMapper mapper;


    @BeforeEach
    @Order(0)
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        //Chamando a anotação mockitoannotations para ler todos os mocks antes
    }

//---------------------------MÈTODO CRIAR CATEGORIA FINANCEIRA----------------------------------//

    @Test
    @Order(1)
    @DisplayName("Deve encontrar todas as outras entidades pela sua id, pra associar a categoria financeira")
    public void deveEncontrarTodasAsAssociacoesPelaIdECriarTipoCategoria() {

        //Setup(mock) das entidades associadas
        Pagamentos pagamento = new Pagamentos();
        ReflectionTestUtils.setField(pagamento,"id",1L);


        HistoricoTransacao transacao = new HistoricoTransacao();
        ReflectionTestUtils.setField(transacao,"id",1L);


        ContaUsuario contaUsuario = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuario,"id",1L);


        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario,"id",1L);


        //Instanciando os valores
        CategoriaFinanceiraRequest request = new CategoriaFinanceiraRequest(RECEITA, SALARIO);
        CategoriaFinanceira categoriaSalva = new CategoriaFinanceira();
        categoriaSalva.setTiposCategorias(RECEITA);
        categoriaSalva.setSubTipo(SALARIO);
        ReflectionTestUtils.setField(categoriaSalva,"id",1L);


        //RespostaEsperada do categoriaFinanceiraResponse, quando ele criar a categoria financeira
        CategoriaFinanceiraResponse responseEsperado = new CategoriaFinanceiraResponse(1L, RECEITA, SALARIO);

        //--Mock para métodos void de associação
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComConta(1L, 1L);
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComUsuario(1L, 1L);
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComPagamento(1L, 1L);
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComTransacao(1L, 1L);


        // --- Mockando os repositórios das entidades associadas ---
        when(pagamentosRepository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(historicoTransacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));
        when(contaUsuarioRepository.findById(1L)).thenReturn(Optional.of(contaUsuario));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        when(categoriaFinanceiraRepository.save(any(CategoriaFinanceira.class))).thenReturn(categoriaSalva);
        when(mapper.retornarDadosCategoria(any(CategoriaFinanceira.class))).thenReturn(responseEsperado);

        //Agora criando os valores do parametro criar categoria financeira
        CategoriaFinanceiraResponse response = categoriaFinanceiraService.criarCategoriaFinanceira
                (request, 1L, 1L, 1L, 1L);

        // --- Assert--
        assertNotNull(response);
        assertEquals(RECEITA, response.tipoCategoria());
        assertEquals(SALARIO, response.subTipo());

        // --- Verifica se os repositórios foram chamados com as IDs corretas ---
        verify(pagamentosRepository).findById(1L);
        verify(contaUsuarioRepository).findById(1L);
        verify(historicoTransacaoRepository).findById(1L);
        verify(usuarioRepository).findById(1L);

        // --- Verifica se os valores foram associados com a Categoria Financeira correta ---
        verify(categoriaFinanceiraAssociation).associarCategoriaComPagamento(1L, 1L);
        verify(categoriaFinanceiraAssociation).associarCategoriaComConta(1L, 1L);
        verify(categoriaFinanceiraAssociation).associarCategoriaComTransacao(1L, 1L);
        verify(categoriaFinanceiraAssociation).associarCategoriaComUsuario(1L, 1L);

    }


    @Test
    @Order(2)
    @DisplayName("Deve Verificar se encontra o Tipo de categoria pela Id informada")
    public void testEstaEncontrandoOTipoCategoriaPelaId() {
        //Instanciando valores para teste
        Long id = 1L;
        //Instanciando da entidade
        CategoriaFinanceira categoria = new CategoriaFinanceira();
        categoria.setTiposCategorias(DESPESA);
        categoria.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria,"id",1L);

        //Instanciando da categoriaFinanceiraResponse
        CategoriaFinanceiraResponse responseEsperado = new CategoriaFinanceiraResponse(id, DESPESA, COMBUSTIVEL);

        //Mock das dependencias usadas dentro do método
        when(categoriaFinanceiraRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(mapper.retornarDadosCategoria(categoria)).thenReturn(responseEsperado);

        //Act
        CategoriaFinanceiraResponse response = categoriaFinanceiraService.encontrarCategoriaPorId(id);

        //Assert
        assertNotNull(response);
        assertEquals(DESPESA, response.tipoCategoria());
        assertEquals(COMBUSTIVEL, response.subTipo());
        assertEquals(id, response.id());

        //Verify (metodos somente que serao mockados, o meu que estou testando nao)
        verify(categoriaFinanceiraRepository).findById(id);
        verify(mapper).retornarDadosCategoria(categoria);
    }

    @Test
    @Order(2)
    @DisplayName("Deve lançar exceção se não encontrar a categoria pela id ")
    public void categoriaCriadaDeveSerAssociadaAUmPagamento() {
        //Inserção dos dados
        Long idNaoExistente = 150L;
        when(categoriaFinanceiraRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        //Assertando que após eu mockar uma id que nao existe, ele deve retornar a exceção
        assertThrows(CategoriaNaoEncontrada.class, ()
                -> {
            categoriaFinanceiraService.encontrarCategoriaPorId(idNaoExistente);
        });

        //Verificando se ele achou o valor mockado
        verify(categoriaFinanceiraRepository).findById(idNaoExistente);
    }

    @Test
    @Order(3)
    @DisplayName("Deve retornar uma lista de todas as categorias criadas baseado em um subtipo indicado")
    public void deveEncontrarUmaListaDeValoresAssociadosAUmSubtipoDeCategoria(){

        //Instanciando os valores para mockar
        SubTipoCategoria subTipoParaTeste = COMBUSTIVEL;


        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(DESPESA);
        categoria1.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria1,"id",1L);


        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria2,"id",2L);


        List<CategoriaFinanceira> categoriasFinanceiras = List.of(categoria1,categoria2);

        CategoriaFinanceiraResponse response1 = new CategoriaFinanceiraResponse(1L,DESPESA,COMBUSTIVEL);
        CategoriaFinanceiraResponse response2 = new CategoriaFinanceiraResponse(2L,DESPESA,COMBUSTIVEL);

        when(categoriaFinanceiraRepository.encontrarPorSubtipoCategoria(subTipoParaTeste)).thenReturn(categoriasFinanceiras);
        when(mapper.retornarDadosCategoria(any(CategoriaFinanceira.class)))
                .thenReturn(response1, response2);


        //Agora testando o método que realmente deve ser testado
        List<CategoriaFinanceiraResponse> resultado = categoriaFinanceiraService.encontrarCategoriasCriadaPeloSubTipo(COMBUSTIVEL);

        //Assert
        assertNotNull(resultado);
        assertEquals(2,resultado.size());
        assertEquals(response1,resultado.get(0));
        assertEquals(response2,resultado.get(1));

        //Verificar
        verify(categoriaFinanceiraRepository).encontrarPorSubtipoCategoria(subTipoParaTeste);
        verify(mapper).retornarDadosCategoria(categoria1);
        verify(mapper).retornarDadosCategoria(categoria2);
    }

    @Test
    @Order(4)
    @DisplayName("deve retornar todas as categoriasFinanceiras criadas")
    public void deveRetornarTodasAsCategoriasFinanceirasCriadas(){
        //Instanciando valores


        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(RECEITA);
        categoria1.setSubTipo(DIVIDENDOS);
        ReflectionTestUtils.setField(categoria1,"id",1L);


        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(DESPESA_ALUGUEL);
        ReflectionTestUtils.setField(categoria2,"id",2L);


        // Criar uma lista simulando a página de dados retornada do banco
        List<CategoriaFinanceira> categorias = List.of(categoria1,categoria2);
        Page<CategoriaFinanceira> paginaMockada = new PageImpl<>(categorias);

        //Criando um valor de paginas encontradas
        Pageable pageable = PageRequest.of(0,2);

        //Mock do repositório
        when(categoriaFinanceiraRepository.findAll(pageable)).thenReturn(paginaMockada);

        //Mock do mapper
        when(mapper.retornarDadosCategoria(any(CategoriaFinanceira.class))).thenAnswer(
                invocationOnMock -> {
                CategoriaFinanceira cat = invocationOnMock.getArgument(0);
                return new CategoriaFinanceiraResponse(cat.getId(),cat.getTiposCategorias(),cat.getSubTipo());
                });


        //Chamando o método que está sendo testado
        Page<CategoriaFinanceiraResponse>
                listResponse = categoriaFinanceiraService.encontrarTodasCategorias(pageable);

        // Asserts
        assertNotNull(listResponse);
        assertEquals(2, listResponse.getContent().size());

        assertEquals(1L, listResponse.getContent().get(0).id());
        assertEquals(RECEITA, listResponse.getContent().get(0).tipoCategoria());
        assertEquals(DIVIDENDOS, listResponse.getContent().get(0).subTipo());

        assertEquals(2L, listResponse.getContent().get(1).id());
        assertEquals(DESPESA, listResponse.getContent().get(1).tipoCategoria());
        assertEquals(DESPESA_ALUGUEL, listResponse.getContent().get(1).subTipo());

        // Verificações
        verify(categoriaFinanceiraRepository).findAll(pageable);
        verify(mapper).retornarDadosCategoria(categoria1);
        verify(mapper).retornarDadosCategoria(categoria2);
    }
}

