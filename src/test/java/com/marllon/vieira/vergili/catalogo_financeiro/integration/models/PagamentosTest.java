package com.marllon.vieira.vergili.catalogo_financeiro.integration.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
public class PagamentosTest {

    Pagamentos pagamento;


    @BeforeEach
    public void instanciarUmObjetoDoTipoPagamentos(){
        pagamento = new Pagamentos();
        pagamento.setValor(BigDecimal.valueOf(100.00));
        pagamento.setData(LocalDate.of(2025,1,1));
        pagamento.setTiposCategorias(TiposCategorias.RECEITA);
        pagamento.setDescricao("teste");
        ReflectionTestUtils.setField(pagamento,"id",1L);
    }

    @Test
    @Order(0)
    @DisplayName("Teste do atributo ID da entidade pagamentos")
    public void verificarTesteAtributoId(){
        Long idDeComparacao = 1L;
        assertEquals(idDeComparacao,pagamento.getId());
    }

    @Test
    @Order(1)
    @DisplayName("Teste do atributo ID com ID que não existe")
    public void verificarTesteAtributoIdInexistente(){

        Long idDeComparacao = 0L;
        assertNotEquals(idDeComparacao,pagamento.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Teste do atributo valor")
    public void verificarAtributoValorDePagamento(){
        BigDecimal valorEsperado = BigDecimal.valueOf(100.00);
        assertEquals(valorEsperado,pagamento.getValor());
    }

    @Test
    @Order(3)
    @DisplayName("Teste do atributo Data")
    public void verificarAtributoLocalDateDaEntidadePagamentos(){

        LocalDate dataEsperada = LocalDate.of(2025,1,1);
        assertEquals(dataEsperada,pagamento.getData());
    }

    @Test
    @Order(4)
    @DisplayName("Teste do atributo Descrição")
    public void verificarAtributoDescricaoDaEntidadePagamentos(){

        String descricao = "teste";
        assertEquals(descricao,pagamento.getDescricao());
    }

    @Test
    @Order(5)
    @DisplayName("Teste do atributo de selecionar Tipo Categoria (ENUM)")
    public void verificarSeAtributoTipoCategoriaFunciona(){

        TiposCategorias tipoCategoriaEsperado = TiposCategorias.RECEITA;
        assertEquals(tipoCategoriaEsperado,pagamento.getTiposCategorias());
    }

}
