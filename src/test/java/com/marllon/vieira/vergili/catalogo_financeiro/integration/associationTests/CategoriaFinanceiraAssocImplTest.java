package com.marllon.vieira.vergili.catalogo_financeiro.integration.associationTests;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste dos métodos de associação e desassociação de categoriaFinanceira com as outras entidades
 * void associarCategoriaComConta(Long categoriaId, Long contaId);
 * void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId);
 * void associarCategoriaComTransacao(Long categoriaId, Long transacaoId);
 * void associarCategoriaComUsuario(Long categoriaId, Long usuarioId);
 * void associarSubTipoCategoriaComDespesa(TiposCategorias tipoCategoriaDespesa, SubTipoCategoria subTipoDespesa);
 * void associarSubTipoCategoriaComReceita(TiposCategorias tipoCategoriaReceita, SubTipoCategoria subTipoReceita);
 * //////--------------------------------------------------------////////////////////////////////
 * // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================
 * void desassociarCategoriaAConta(Long categoriaId, Long contaId);
 * void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId);
 * void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId);
 * void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId);
 * void desassociarCategoriaCriadaComDespesa(Long categoriaId);
 * void desassociarCategoriaCriadaComReceita(Long categoriaId);
 */
@SpringBootTest
@TestPropertySource("classpath:/application-test.properties")
public class CategoriaFinanceiraAssocImplTest {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private CategoriaFinanceiraAssociation categoriaFinanceiraAssociation;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    public void antesDeExecutarCadaMetodo(){
        //Executando os valores do arquivo sql antes
    }

    @Test
    @Sql("/sql/CategoriaFinanceiraDados.sql")
    @DisplayName(" teste do método void associarCategoriaComConta(Long categoriaId, Long contaId);")
    public void testeMetodoAssociarCategoriaComConta(){

        Optional<CategoriaFinanceira> categoriaEncontrada = categoriaFinanceiraRepository.findById(2L);
        assertTrue(categoriaEncontrada.isPresent());

        Optional<ContaUsuario> contaEncontrada = contaUsuarioRepository.findById(1L);
        assertTrue(contaEncontrada.isPresent());

         categoriaFinanceiraAssociation.associarCategoriaComConta(categoriaEncontrada.get().getId(),
                contaEncontrada.get().getId());

         //assertDoesNotThrow(this::testeMetodoAssociarCategoriaComConta);
         assertEquals(categoriaEncontrada.get().getContaRelacionada(),contaEncontrada.get().getCategoriasRelacionadas());
    }
}




