package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
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
@Table(name = "contas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id","nome", "saldo"})
public class ContaUsuario {

    @Id
    @Setter(AccessLevel.NONE) //Não gerar setter para ID
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
     * Uma conta pode estar associada a várias categorias financeiras.
     * Exemplo: Uma conta (poupança, corrente, etc.) pode ter categorias de pagamento como
     * despesas, receitas.
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();

    /**
     * Uma conta pode ter vários pagamentos associados.
     * Exemplo: Uma conta (poupança, corrente, etc.) pode registrar múltiplos pagamentos feitos ao longo do tempo.
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Uma conta pode registrar múltiplas transações financeiras.
     * Exemplo: Uma conta (poupança, corrente, etc.) pode ter transações como depósitos, saques e transferências.
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Muitas contas podem estar associadas a um único usuário.
     * Exemplo: Um usuário pode ter diversas contas cadastradas, como conta corrente, poupança e investimentos.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_conta_usuario"))
    private Usuario usuarioRelacionado;


}
