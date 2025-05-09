package com.marllon.vieira.vergili.catalogo_financeiro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * representar diferentes contas bancárias ou carteiras, como saldo, titulo, corrente, poupança, etc.
 * Controlar direitinho o que sai de cada conta, cada uma de cada usuário
 */

@Entity
@Table(name = "contas",uniqueConstraints = {
        @UniqueConstraint(name = "fk_conta_usuario", columnNames = "usuario_id")})
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id","nome", "saldo"})
public class ContaUsuario {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

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


    //RELACIONAMENTOS

    /**
     * uma conta pode ter várias categorias de recebimentos e gastos;(uma conta(poupança,corrente,etc..)
     * pode ter várias
     * categorias de pagamentos, seja despesa, receita, etc.
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


    /**
     * Uma conta pode ter vários pagamentos relacionados(uma conta(poupança,corrente,etc..)
     * pode ter vários pagamentos feitos
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Uma conta pode ter várias transações relacionadas(uma conta (poupança,corrente,etc..)
     * pode ter várias transações realizdas
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Muitas Contas pode ter um usuário relacionado(Muitas contas de perfil)
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_conta_usuario"))
    private Usuario usuarioRelacionado;


}
