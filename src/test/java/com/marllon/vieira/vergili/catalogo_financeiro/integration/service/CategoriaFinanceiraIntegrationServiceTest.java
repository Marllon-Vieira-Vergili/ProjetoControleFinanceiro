package com.marllon.vieira.vergili.catalogo_financeiro.integration.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe para teste de integração de Categoria Financeira, testar somente os método se pega
 * com o banco de dados
 */
@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest
public class CategoriaFinanceiraIntegrationServiceTest {

    @Autowired
    private CategoriaFinanceiraService categoriaservice;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private CategoriaFinanceiraMapper mapper;

    @Autowired
    private JdbcTemplate jdbc;


    @BeforeEach
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    public void executarValoresAntes(){
        //Vai instanciar todos os valores do arquivo SQL pra testar
    }



    @Test
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    @DisplayName("Teste do método de criar uma categoriaFinanceira e associando")
    public void verificarCategoriaFinanceiraCriaValor(){
        //Instanciando os valores
        Long pagamentoId = 1L;
        Long historicoTransacaoId = 1L;
        Long usuarioId = 1L;
        Long contaUsuarioId = 1L;

        Optional<Pagamentos> pagamentoEncontrado = pagamentosRepository.findById(pagamentoId);
        Optional<HistoricoTransacao> historicoEncontrado = historicoTransacaoRepository.findById(historicoTransacaoId);
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioId);
        Optional<ContaUsuario> contaUsuario = contaUsuarioRepository.findById(contaUsuarioId);

        assertTrue(pagamentoEncontrado.isPresent());
        assertTrue(historicoEncontrado.isPresent());
        assertTrue(usuarioEncontrado.isPresent());
        assertTrue(contaUsuario.isPresent());

        CategoriaFinanceiraRequest request = new
                CategoriaFinanceiraRequest(TiposCategorias.RECEITA,SubTipoCategoria.SALARIO,1L,1L,1L,1L);

       CategoriaFinanceiraResponse categoriaCriada =  categoriaservice.criarCategoriaFinanceira(request,1L,1L,1L,1L);

        CategoriaFinanceiraResponse response =
                new CategoriaFinanceiraResponse(1L,TiposCategorias.RECEITA, SubTipoCategoria.SALARIO);


        assertEquals(response,categoriaCriada);
    }
    @AfterEach
    public void executarDelecaoDepois(){
        //Deletar em ordem
        jdbc.execute("DELETE FROM historico_transacoes");
        jdbc.execute("DELETE FROM pagamentos");
        jdbc.execute("DELETE FROM categoria_das_contas");
        jdbc.execute("DELETE FROM contas");
        jdbc.execute("DELETE FROM usuarios");

    }
}
