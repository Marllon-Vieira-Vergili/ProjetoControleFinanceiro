package com.marllon.vieira.vergili.catalogo_financeiro.models;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//representar diferentes contas bancárias ou carteiras, como saldo, titulo, corrente, poupança, etc.
//Controlar direitinho o que sai de cada conta, cada uma de cada usuário
@Entity
@Table(name = "contas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Contas {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "nome")
    @NotBlank(message = "O campo do nome não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
    public String nome;

    @Column(name = "saldo")
    @NotNull(message = "O campo do saldo na conta não pode ser nulo!")
    public BigDecimal saldo;

    @Column(name = "tipo")
    @NotBlank(message = "O campo do tipo de conta não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "O tipo só pode conter caracteres alfabéticos e espaços! Ex: Conta Poupança, Conta Corrente, etc.")
    public String tipo;


    //RELACIONAMENTOS

    //Uma conta pode ter várias categorias de recebimentos e gastos;(uma conta(poupança,corrente,etc..) pode ter várias
    // categorias de pagamentos, seja despesa, receita, etc.
    private List<CategoriasContas> contasRelacionadasCategorias = new ArrayList<>();

    //Uma conta pode ter vários pagamentos relacionados(uma conta(poupança,corrente,etc..)pode ter vários pagamentos feitos
    private List<Pagamentos> contasRelacionadosPagamentos = new ArrayList<>();

    //Uma conta pode ter várias transações relacionadas(uma conta (poupança,corrente,etc..) pode ter várias transações realizdas
    private List<HistoricoTransacoes> contasRelacionadosTransacoes = new ArrayList<>();

    //Várias contas podem ter um usuário relacionado(uma conta corrente, poupança, pode ter um usuário relacionado apenas)
    private Usuarios contasRelacionadosUsuario;

    //ASSOCIAÇÔES COM OUTRAS ENTIDADES
}
