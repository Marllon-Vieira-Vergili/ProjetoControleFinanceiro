package com.marllon.vieira.vergili.catalogo_financeiro.models;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//representar diferentes contas bancárias ou carteiras, como saldo, titulo, corrente, poupança, etc.
//Controlar direitinho o que sai de cada conta, cada uma de cada usuário
@Entity
@Table(name = "contas",uniqueConstraints = {
        @UniqueConstraint(name = "fk_contas_id_usuarios", columnNames = "usuarios_id")})
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "id")
@ToString
public class Contas {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false)
    @NotBlank(message = "O campo do nome não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
    private String nome;

    @Column(name = "saldo",nullable = false)
    @NotNull(message = "O campo do saldo na conta não pode ser nulo!")
    private BigDecimal saldo;

    @Column(name = "tipoConta",nullable = false)
    @NotBlank(message = "O campo do tipo de conta não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "O tipo só pode conter caracteres alfabéticos e espaços! Ex: Conta Poupança, Conta Corrente, etc.")
    private String tipoConta;


    //RELACIONAMENTOS

    //uma conta pode ter várias categorias de recebimentos e gastos;(uma conta(poupança,corrente,etc..) pode ter várias
    // categorias de pagamentos, seja despesa, receita, etc.
    @OneToMany(mappedBy = "categoriasRelacionadasAConta", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    private List<CategoriasContas> contasRelacionadasCategorias = new ArrayList<>();

    //Uma conta pode ter vários pagamentos relacionados(uma conta(poupança,corrente,etc..)pode ter vários pagamentos feitos
    @OneToMany(mappedBy = "pagamentoRelacionadoContas",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> contasRelacionadosPagamentos = new ArrayList<>();

    //Uma conta pode ter várias transações relacionadas(uma conta (poupança,corrente,etc..) pode ter várias transações realizdas
    @OneToMany(mappedBy = "transacoesRelacionadoConta",fetch = FetchType.LAZY,cascade = CascadeType.ALL )
    private List<HistoricoTransacoes> contasRelacionadosTransacoes = new ArrayList<>();

    //Uma conta pode ter um usuário relacionado(Uma conta de perfil financeiro somente)
    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "usuarios_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_contas_id_usuarios"))
    private Usuarios contaRelacionadoUsuario;

    //ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE





}
