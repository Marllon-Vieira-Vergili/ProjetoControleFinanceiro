package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.CategoriaFinanceiraImpl;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria.*;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.configuration.GlobalConfiguration.validate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class CategoriaFinanceiraImplTest {


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


    @Test
    @Order(1)
    @DisplayName("Deve encontrar todas as outras entidades pela sua id, pra associar a categoria financeira")
    /**
     * Esse método de teste, é para testar a lógica do método de criar uma categoriaFinanceira nova.
     * Quando o usuário decidir chamar esse método, essa nova categoria deverá:
     *
     * Condição 1: Verificar antes de criar a categoria, se os valores do parâmetro estão condizentes, como o tipo escolhido
     * e o subtipo (Que são enums o Tipo escolhido (RECEITA ou DESPESA) e Subtipo
     *
     * Condição 2: O tipo de categoria deve ser salvo antes para gerar uma "id", e posteriormente associado
     *
     * Condição 3:Verificar se a id das outras instancias estão livres para ser associado a essa categoria,
     * ele deverá lançar um try/catch em cada método de associar a categoria Financeira a seus respectivos relacionamentos
     * se falhar, ele deverá lançar a exception "AssociationErrorException"
     *
     * se der tudo certo, o método deverá passar no teste.. Esse teste é em caso de sucesso
     */
    public void deveEncontrarTodasAsAssociacoesPelaIdECriarTipoCategoria() {
        //Arrange - Instanciando valores Mockados

        //Instanciando os objetos
        CategoriaFinanceira novaCategoriaMockadaParaTesteDoRepositorio = new CategoriaFinanceira();
        novaCategoriaMockadaParaTesteDoRepositorio.setTiposCategorias(DESPESA);
        novaCategoriaMockadaParaTesteDoRepositorio.setSubTipo(DESPESA_ALUGUEL);
        ReflectionTestUtils.setField(novaCategoriaMockadaParaTesteDoRepositorio, "id", 1L);

        //Instanciando as associações Mockadas, pela sua id

        Pagamentos pagamentoIdMock = new Pagamentos();
        ReflectionTestUtils.setField(pagamentoIdMock, "id", 1L);

        ContaUsuario contaUsuarioMockada = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuarioMockada, "id", 1L);

        Usuario usuarioMockado = new Usuario();
        ReflectionTestUtils.setField(usuarioMockado, "id", 1L);

        HistoricoTransacao historicoMockado = new HistoricoTransacao();
        ReflectionTestUtils.setField(historicoMockado, "id", 1L);

        when(pagamentosRepository.findById(pagamentoIdMock.getId())).thenReturn(Optional.of(pagamentoIdMock));
        when(contaUsuarioRepository.findById(contaUsuarioMockada.getId())).thenReturn(Optional.of(contaUsuarioMockada));
        when(historicoTransacaoRepository.findById(historicoMockado.getId())).thenReturn(Optional.of(historicoMockado));
        when(usuarioRepository.findById(usuarioMockado.getId())).thenReturn(Optional.of(usuarioMockado));

        when(categoriaFinanceiraRepository.save(any(CategoriaFinanceira.class)))
                .thenAnswer(invocationOnMock -> {
                    CategoriaFinanceira categoria = invocationOnMock.getArgument(0);
                    ReflectionTestUtils.setField(categoria, "id", 1L);
                    return categoria;
                });

        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComPagamento(novaCategoriaMockadaParaTesteDoRepositorio.getId(), pagamentoIdMock.getId());
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComUsuario(novaCategoriaMockadaParaTesteDoRepositorio.getId(), usuarioMockado.getId());
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComConta(novaCategoriaMockadaParaTesteDoRepositorio.getId(), contaUsuarioMockada.getId());
        doNothing().when(categoriaFinanceiraAssociation).associarCategoriaComTransacao(novaCategoriaMockadaParaTesteDoRepositorio.getId(), historicoMockado.getId());


        //O retorno que eu espero do método, que ele crie exatamente com esses mesmos valores
        CategoriaFinanceiraResponse respostaEsperadaDoMetodo = new CategoriaFinanceiraResponse(1L, DESPESA, DESPESA_ALUGUEL);

        when(mapper.retornarDadosCategoria(novaCategoriaMockadaParaTesteDoRepositorio)).thenReturn(respostaEsperadaDoMetodo);


        //Valor de entrada dos dados para passar ao criar a categoria
        CategoriaFinanceiraRequest valor = new CategoriaFinanceiraRequest(DESPESA, DESPESA_ALUGUEL, 1L, 1L, 1L, 1L);
        //Act. Chamada do método principal
        CategoriaFinanceiraResponse categoriacriada = categoriaFinanceiraService.criarCategoriaFinanceira(valor, 1L, 1L, 1L, 1L);

        //Assert
        assertEquals(categoriacriada, respostaEsperadaDoMetodo);

        //Verificar
        verify(categoriaFinanceiraRepository).save(novaCategoriaMockadaParaTesteDoRepositorio);

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
        ReflectionTestUtils.setField(categoria, "id", 1L);

        //Instanciando da categoriaFinanceiraResponse
        CategoriaFinanceiraResponse responseEsperado = new CategoriaFinanceiraResponse(id, DESPESA, COMBUSTIVEL);

        //Mock das dependencias usadas dentro do método
        when(categoriaFinanceiraRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(mapper.retornarDadosCategoria(categoria)).thenReturn(responseEsperado);

        //Act
        Optional<CategoriaFinanceiraResponse> response = categoriaFinanceiraService.encontrarCategoriaPorId(id);

        //Assert
        assertNotNull(response);
        assertEquals(DESPESA, response.get().tipoCategoria());
        assertEquals(COMBUSTIVEL, response.get().subTipo());
        assertEquals(id, response.get().id());

        //Verify (metodos somente que serao mockados, o meu que estou testando nao)
        verify(categoriaFinanceiraRepository).findById(id);
        verify(mapper).retornarDadosCategoria(categoria);
    }

    @Test
    @Order(3)
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
    @Order(4)
    @DisplayName("Deve retornar uma lista de todas as categorias criadas baseado em um subtipo indicado")
    public void deveEncontrarUmaListaDeValoresAssociadosAUmSubtipoDeCategoria() {

        //Instanciando os valores para mockar
        SubTipoCategoria subTipoParaTeste = COMBUSTIVEL;


        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(DESPESA);
        categoria1.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria1, "id", 1L);


        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria2, "id", 2L);


        List<CategoriaFinanceira> categoriasFinanceiras = List.of(categoria1, categoria2);

        CategoriaFinanceiraResponse response1 = new CategoriaFinanceiraResponse(1L, DESPESA, COMBUSTIVEL);
        CategoriaFinanceiraResponse response2 = new CategoriaFinanceiraResponse(2L, DESPESA, COMBUSTIVEL);

        when(categoriaFinanceiraRepository.encontrarPorSubtipoCategoria(subTipoParaTeste)).thenReturn(categoriasFinanceiras);
        when(mapper.retornarDadosCategoria(any(CategoriaFinanceira.class)))
                .thenReturn(response1, response2);


        //Agora testando o método que realmente deve ser testado
        List<CategoriaFinanceiraResponse> resultado = categoriaFinanceiraService.encontrarCategoriasCriadaPeloSubTipo(COMBUSTIVEL);

        //Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(response1, resultado.get(0));
        assertEquals(response2, resultado.get(1));

        //Verificar
        verify(categoriaFinanceiraRepository).encontrarPorSubtipoCategoria(subTipoParaTeste);
        verify(mapper).retornarDadosCategoria(categoria1);
        verify(mapper).retornarDadosCategoria(categoria2);
    }

    @Test
    @Order(5)
    @DisplayName("deve retornar todas as categoriasFinanceiras criadas")
    public void deveRetornarTodasAsCategoriasFinanceirasCriadas() {
        //Instanciando valores


        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(RECEITA);
        categoria1.setSubTipo(DIVIDENDOS);
        ReflectionTestUtils.setField(categoria1, "id", 1L);


        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(DESPESA_ALUGUEL);
        ReflectionTestUtils.setField(categoria2, "id", 2L);


        // Criar uma lista simulando a página de dados retornada do banco
        List<CategoriaFinanceira> categorias = List.of(categoria1, categoria2);
        Page<CategoriaFinanceira> paginaMockada = new PageImpl<>(categorias);

        //Criando um valor de paginas encontradas
        Pageable pageable = PageRequest.of(0, 2);

        //Mock do repositório
        when(categoriaFinanceiraRepository.findAll(pageable)).thenReturn(paginaMockada);

        //Mock do mapper
        when(mapper.retornarDadosCategoria(any(CategoriaFinanceira.class))).thenAnswer(
                invocationOnMock -> {
                    CategoriaFinanceira cat = invocationOnMock.getArgument(0);
                    return new CategoriaFinanceiraResponse(cat.getId(), cat.getTiposCategorias(), cat.getSubTipo());
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

    @Test
    @Order(6)
    @DisplayName("Teste para verificar se atualiza a Categoria Financeira Já criado")
    public void deveAtualizarOsDadosCategoriaFinanceiraJaExistente() {
        //Criando uma categoria Financeira Mockada apenas para gerar uma ID

        CategoriaFinanceira categoriaMockParaTeste = new CategoriaFinanceira();
        Long id = 1L;
        categoriaMockParaTeste.setTiposCategorias(DESPESA);
        categoriaMockParaTeste.setSubTipo(ALIMENTACAO);
        ReflectionTestUtils.setField(categoriaMockParaTeste, "id", id);

        //Quando a categoriaFinanceira repositorio salvar qualquer coisa na classe categoriaFinanceira
        when(categoriaFinanceiraRepository.findById(id)).thenReturn(Optional.of(Optional.of(categoriaMockParaTeste).orElseThrow()));

        //Mockar para salvar os dados da entidade falsa
        when(categoriaFinanceiraRepository.save(any(CategoriaFinanceira.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        when(mapper.retornarDadosCategoria(any())).thenAnswer(invocationOnMock -> {
            invocationOnMock.getArgument(0);
            return new CategoriaFinanceiraResponse(
                    categoriaMockParaTeste.getId(),
                    categoriaMockParaTeste.getTiposCategorias(),
                    categoriaMockParaTeste.getSubTipo());
        });

        CategoriaFinanceiraResponse responseEsperada = new CategoriaFinanceiraResponse(id, RECEITA, DIVIDENDOS);

        CategoriaFinanceiraResponse categoriaMockadaAtualizada =
                categoriaFinanceiraService.atualizarUmaCategoriaCriada
                        (id, RECEITA, DIVIDENDOS);


        assertEquals(responseEsperada, categoriaMockadaAtualizada);
    }

    @Test
    @Order(7)
    @DisplayName("Deve remover a categoriaFinanceira Pela ID")
    public void metodoDeveRemoverCategoriaFinanceira() {
        //CRIANDO UMA CATEGORIA FINANCEIRA JA ASSOCIADA COM OS RELACIONAMENTOS PARA TESTAR DELEÇÂO
        CategoriaFinanceira categoriaMockada = new CategoriaFinanceira();
        Long categoriaId = 1L;
        Long usuarioId = 1L;
        Long contaUsuarioId = 1L;
        Long pagamentoId = 1L;
        Long historicoTransacaoId = 1L;

        categoriaMockada.setTiposCategorias(DESPESA);
        categoriaMockada.setSubTipo(CONTA_INTERNET);
        ReflectionTestUtils.setField(categoriaMockada, "id", categoriaId);

        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);

        ContaUsuario contaUsuario = new ContaUsuario();
        ReflectionTestUtils.setField(contaUsuario, "id", contaUsuarioId);

        Pagamentos pagamento = new Pagamentos();
        ReflectionTestUtils.setField(pagamento, "id", pagamentoId);

        HistoricoTransacao historicoTransacao = new HistoricoTransacao();
        ReflectionTestUtils.setField(historicoTransacao, "id", historicoTransacaoId);

        categoriaMockada.setUsuarioRelacionado(usuario);
        categoriaMockada.setContaRelacionada(contaUsuario);
        categoriaMockada.setPagamentosRelacionados(List.of(pagamento));
        categoriaMockada.setTransacoesRelacionadas(List.of(historicoTransacao));

        when(categoriaFinanceiraRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaMockada))
                .thenReturn(Optional.empty()); //Aqui retornar ja o elemento vazio no mock
        doNothing().when(categoriaFinanceiraAssociation).desassociarCategoriaUsuario(categoriaId, usuarioId);
        doNothing().when(categoriaFinanceiraAssociation).desassociarCategoriaAConta(categoriaId, contaUsuarioId);
        doNothing().when(categoriaFinanceiraAssociation).desassociarCategoriaAPagamento(categoriaId, pagamentoId);
        doNothing().when(categoriaFinanceiraAssociation).desassociarCategoriaTransacao(categoriaId, historicoTransacaoId);

        assertDoesNotThrow(() -> categoriaFinanceiraService.deletarCategoria(categoriaId));


        Optional<CategoriaFinanceira> categoriaRemovida = categoriaFinanceiraRepository.findById(categoriaId);

        assertTrue(categoriaRemovida.isEmpty());

        //Verify
        verify(categoriaFinanceiraAssociation).desassociarCategoriaUsuario(categoriaId, usuarioId);
        verify(categoriaFinanceiraAssociation).desassociarCategoriaAConta(categoriaId, contaUsuarioId);
        verify(categoriaFinanceiraAssociation).desassociarCategoriaTransacao(categoriaId, historicoTransacaoId);
        verify(categoriaFinanceiraAssociation).desassociarCategoriaAPagamento(categoriaId, pagamentoId);
    }

    @Order(8)
    @DisplayName("Deve encontrar uma categoriaFinanceira pelo id, quando ela for despesa")
    @Test
    public void verificarQuandoCategoriaForDespesa() {
        Long categoria1Id = 1L;
        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(DESPESA);
        categoria1.setSubTipo(DESPESA_ALUGUEL);
        ReflectionTestUtils.setField(categoria1,"id",categoria1Id);
        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        Long categoria2Id = 2L;
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(COMBUSTIVEL);
        ReflectionTestUtils.setField(categoria2,"id",categoria2Id);

        List<CategoriaFinanceira> categoriasDespesa = List.of(categoria1, categoria2);

        when(categoriaFinanceiraRepository.encontrarPorTipoCategoria(DESPESA)).thenReturn(categoriasDespesa);

        boolean resultado = categoriaFinanceiraService.seCategoriaForDespesa();

        assertTrue(categoriasDespesa.contains(categoria1));
        assertTrue(categoriasDespesa.contains(categoria2));
        assertTrue(resultado);
        //verificar
        verify(categoriaFinanceiraRepository).encontrarPorTipoCategoria(DESPESA);
    }

    @Test
    @Order(9)
    @DisplayName("se metodo 'se for despesa' não retornar o valor, ele deve retornar false")
    public void deveRetornarFalseSeDespesa(){
        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(RECEITA);
        categoria1.setSubTipo(SALARIO);
        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(RECEITA);
        categoria2.setSubTipo(RENDA_FIXA);

        List<CategoriaFinanceira> categoriasEmLista = List.of(categoria1,categoria2);

        when(categoriaFinanceiraRepository.encontrarPorTipoCategoria(DESPESA)).thenReturn(categoriasEmLista);

        boolean resultado = categoriaFinanceiraService.seCategoriaForDespesa();

        assertFalse(resultado);
        verify(categoriaFinanceiraRepository).encontrarPorTipoCategoria(DESPESA);
    }

    @Order(11)
    @Test
    @DisplayName("Deve encontrar uma categoriaFinanceira pelo id, quando ela for receita")
    public void verificarQuandoCategoriaForReceita(){
        CategoriaFinanceira categoriaUm = new CategoriaFinanceira();
        categoriaUm.setTiposCategorias(RECEITA);
        categoriaUm.setSubTipo(DIVIDENDOS);
        CategoriaFinanceira categoriaDois = new CategoriaFinanceira();
        categoriaDois.setTiposCategorias(RECEITA);
        categoriaDois.setSubTipo(SALARIO);

        List<CategoriaFinanceira> listaReceitas = List.of(categoriaUm,categoriaDois);

        when(categoriaFinanceiraRepository.encontrarPorTipoCategoria(RECEITA)).thenReturn(listaReceitas);

        boolean resultadoEsperado = categoriaFinanceiraService.seCategoriaForReceita();

        assertTrue(listaReceitas.contains(categoriaUm));
        assertTrue(listaReceitas.contains(categoriaDois));
        assertTrue(resultadoEsperado);

        verify(categoriaFinanceiraRepository).encontrarPorTipoCategoria(RECEITA);
    }

    @Order(12)
    @Test
    @DisplayName("deve retornar falso, quando o metodo seReceita não encontrar objeto do tipo Receita")
    public void deveRetornarFalsoSeReceitaNaoForEncontrado(){
        CategoriaFinanceira categoria1 = new CategoriaFinanceira();
        categoria1.setTiposCategorias(DESPESA);
        categoria1.setSubTipo(CONTA_AGUA);
        CategoriaFinanceira categoria2 = new CategoriaFinanceira();
        categoria2.setTiposCategorias(DESPESA);
        categoria2.setSubTipo(CONTA_TELEFONE);

        List<CategoriaFinanceira> listaCategorias = List.of(categoria1,categoria2);

        when(categoriaFinanceiraRepository.encontrarPorTipoCategoria(RECEITA)).thenReturn(listaCategorias);

        boolean resultadoEsperado = categoriaFinanceiraService.seCategoriaForReceita();

        assertTrue(listaCategorias.contains(categoria1));
        assertTrue(listaCategorias.contains(categoria2));
        assertFalse(resultadoEsperado);

        verify(categoriaFinanceiraRepository).encontrarPorTipoCategoria(RECEITA);
    }

    @Order(13)
    @Test
    @DisplayName("Verificando metodo Tipo Categoria existe")
    public void testandoTipoCategoriaExiste(){

        List<TiposCategorias> categorias = List.of(RECEITA, DESPESA);

        boolean resultadoCategoriaReceitaExiste = categoriaFinanceiraService.tipoCategoriaExiste(RECEITA);
        boolean resultadoCategoriaDespesaExiste = categoriaFinanceiraService.tipoCategoriaExiste(DESPESA);

        assertEquals(2,categorias.size());
        assertTrue(resultadoCategoriaReceitaExiste);
        assertTrue(resultadoCategoriaDespesaExiste);
        assertNotNull(categorias);
    }

    @Test
    @Order(14)
    @DisplayName("Verificando se acha um tipo Categoria criado igual")
    public void deveEncontrarUmTipoCategoriaJaCriadoIgual(){

        //Criando valores falsos
        CategoriaFinanceira categoriaUm = new CategoriaFinanceira();
        categoriaUm.setTiposCategorias(RECEITA);
        categoriaUm.setSubTipo(RENDA_ALUGUEL);

        List<CategoriaFinanceira> categoriaLista = List.of(categoriaUm);

        when(categoriaFinanceiraRepository.encontrarPorTipoAndSubtipo(RECEITA,RENDA_ALUGUEL)).thenReturn(categoriaLista);

        boolean resultadoEsperado = categoriaFinanceiraService.jaExisteUmaCategoriaIgual(categoriaUm);

        assertTrue(resultadoEsperado);

        verify(categoriaFinanceiraRepository).encontrarPorTipoAndSubtipo(RECEITA,RENDA_ALUGUEL);
    }
}


