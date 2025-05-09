package com.marllon.vieira.vergili.catalogo_financeiro.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias.RECEITA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas.CONTA_CORRENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContaUsuarioTest {

    ContaUsuario contausuario;

    /*
    @Column(name = "nome", nullable = false)
    @NotBlank(message = "O campo do nome não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
    private String nome;

    @Column(name = "saldo", nullable = false)
    @NotNull(message = "O campo do saldo na conta não pode ser nulo!")
    private BigDecimal saldo;


    @Column(name = "tipo_conta", nullable = false)
    @NotNull(message = "O campo TipoConta não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private TiposContas tipoConta;
     */

    @BeforeEach()
    public void instanciarUmaConta(){
        contausuario = new ContaUsuario();
        ReflectionTestUtils.setField(contausuario,"nome","Conta do Marllon");
        ReflectionTestUtils.setField(contausuario,"saldo",new BigDecimal(1500));
        ReflectionTestUtils.setField(contausuario,"tipoConta",CONTA_CORRENTE);

        ReflectionTestUtils.setField(contausuario,"id","100L");
    }

    @Test
    @DisplayName("Testando o atributo da id da Conta(fins de prática)")
    public void testIdAtributo(){
        Long idEquivalente =(Long) ReflectionTestUtils.getField(contausuario,"id");
        assertEquals("100L", idEquivalente);
    }


}
