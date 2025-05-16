package com.marllon.vieira.vergili.catalogo_financeiro.integration.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest
public class PagamentosIntegrationServiceTest {


    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private PagamentosAssociation pagamentosAssociation;

    @Autowired
    private JdbcTemplate jdbc;


    @BeforeEach
    @Sql("/sql/PagamentosDados.sql")
    public void antesDeInicializarCadaUm(){
        //Executar os valores dentro do SQL
    }

    @AfterEach
    public void depoisDeInicializarCadaUmLimparValores(){
        jdbc.execute("DELETE FROM pagamentos");
        jdbc.execute("DELETE FROM historico_transacoes");
        jdbc.execute("DELETE FROM usuarios");
        jdbc.execute("DELETE FROM categoria_das_contas");

    }

    @Test
    @DisplayName("Criar um pagamento e associá-lo")
    public void criarPagamentoTeste(){

        Optional<CategoriaFinanceira> categoria = categoriaFinanceiraRepository.findById(1L);
        assertNotNull(Optional.of(categoria));

        Optional<ContaUsuario> contaUsuario = contaUsuarioRepository.findById(1L);
        assertNotNull(Optional.of(contaUsuario));

        Optional<Usuario> usuario = usuarioRepository.findById(1L);
        assertNotNull(Optional.of(usuario));

        Optional<HistoricoTransacao> transacao = historicoTransacaoRepository.findById(1L);
        assertNotNull(Optional.of(contaUsuario));

        PagamentosRequest request =new PagamentosRequest(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_EVEN),LocalDate.of(2025,1,1),
                "pagamento teste",TiposCategorias.RECEITA, SubTipoCategoria.DIVIDENDOS,1L,1L,1L);

        PagamentosResponse pagamentoCriadoDoMetodo = pagamentosService.criarPagamento(request);

        PagamentosResponse response = new PagamentosResponse
                (1L, BigDecimal.valueOf(1000.00).setScale(2,RoundingMode.HALF_EVEN)
                        ,LocalDate.of(2025,1,1),"pagamento teste",
                        TiposCategorias.RECEITA,SubTipoCategoria.DIVIDENDOS,1L,1L);

        pagamentosAssociation.associarPagamentoComUsuario(response.id(),usuario.orElseThrow().getId());
        pagamentosAssociation.associarPagamentoComConta(response.id(),contaUsuario.orElseThrow().getId());
        pagamentosAssociation.associarPagamentoComCategoria(response.id(),categoria.orElseThrow().getId());
        pagamentosAssociation.associarPagamentoATransacao(response.id(),transacao.orElseThrow().getId());

        assertEquals(response, pagamentoCriadoDoMetodo);

        }
    }
