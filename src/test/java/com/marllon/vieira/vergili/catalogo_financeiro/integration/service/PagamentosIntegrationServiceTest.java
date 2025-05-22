package com.marllon.vieira.vergili.catalogo_financeiro.integration.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
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
@Transactional
public class PagamentosIntegrationServiceTest {


    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private PagamentosRepository pagamentosRepository;

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

    @Autowired
    private EntityManager entityManager;


    @BeforeEach
    @Sql(value = "classpath:/sql/PagamentosDados.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void antesDeInicializarCadaUm(){

        //Executar os valores dentro do SQL
    }






    @Test
    @DisplayName("Deve falhar se usuario digitar DESPESA em metodo criar Recebimento")
    public void deveRetornarExcecaoSeUSuarioDigitarValorDESPESAEmCriarRecebimento(){
        PagamentosRequest requestRecebimento = new PagamentosRequest(BigDecimal.valueOf(100.00),
                LocalDate.now(),
                "teste",
                TiposCategorias.DESPESA,
                SubTipoCategoria.CONTA_AGUA,
                1L,
               1L,
                1L);

        assertTrue(pagamentosService.dataEstaCorreta(requestRecebimento.data()));
        assertTrue(pagamentosService.valorEstaCorreto(requestRecebimento.valor()));

//Assertando que ele joga exception de dados invalidos, se usuario digitar TiposCategorias.DESPESA em metodo de receber
        assertThrows(DadosInvalidosException.class,()
                ->{
           pagamentosService.criarRecebimento(requestRecebimento);
        });
    }

    @Test
    @DisplayName("Teste para verificar se está criando recebimento")
    @Sql(value = "classpath:/sql/PagamentosDados.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deveCriarRecebimento() {
        // Busca relacionamentos para garantir existência
        CategoriaFinanceira categoria = categoriaFinanceiraRepository.findById(1L).orElseThrow();
        ContaUsuario conta = contaUsuarioRepository.findById(1L).orElseThrow();
        Usuario usuario = usuarioRepository.findById(1L).orElseThrow();

        PagamentosRequest request = new PagamentosRequest(
                BigDecimal.valueOf(1000.00),
                LocalDate.now(),
                "teste",
                TiposCategorias.RECEITA,
                SubTipoCategoria.HERANCA,
                categoria.getId(),
                usuario.getId(),
                conta.getId()
        );

        PagamentosResponse response = pagamentosService.criarRecebimento(request);

        assertEquals(BigDecimal.valueOf(1000.00), response.valor());
        assertEquals(TiposCategorias.RECEITA, response.categoria());
        assertEquals(SubTipoCategoria.HERANCA, response.subTipoCategoria());
        assertEquals(conta.getId(), response.contaAssociada());
        assertEquals(usuario.getId(), response.usuarioAssociado());
    }


    @AfterEach
    public  void depoisDeInicializarCadaUmLimparValores(){
        jdbc.execute("DELETE FROM pagamentos");
        jdbc.execute("DELETE FROM historico_transacoes");
        jdbc.execute("DELETE FROM usuarios");
        jdbc.execute("DELETE FROM categoria_das_contas");

        }


    }
