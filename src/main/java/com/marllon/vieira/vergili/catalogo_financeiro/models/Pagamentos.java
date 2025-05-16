package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**registros de despesas e contas a pagar e receber de alguém.. criará registro nessa entidade
 */

@Entity
@Table(name = "pagamentos")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id","valor", "data", "descricao"})
public class Pagamentos {

    @Id
    @Setter(AccessLevel.NONE) //Não gerar setter para ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "valor", nullable = false)
    @NotNull(message = "O campo do valor não pode ser vazio!")
    @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    @NotNull(message = "O campo da data não pode ficar vazio!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @Column(name = "descricao", length = 255, nullable = false)
    @NotBlank(message = "O campo descrição não pode ficar vazio!")
    private String descricao;

    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipos_categorias", nullable = false)
    private TiposCategorias tiposCategorias;


    // RELACIONAMENTOS

    /**
     * Muitos pagamentos podem estar associados a um único usuário.
     * Exemplo: Um usuário pode realizar diversos pagamentos ao longo do tempo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_pagamento_usuario"))
    private Usuario usuarioRelacionado;

    /**
     * Um pagamento pode estar relacionado a várias transações financeiras.
     * Exemplo: Um pagamento parcelado pode ser vinculado a diferentes registros de transação.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "pagamentos_e_transacoes",
            joinColumns = @JoinColumn(name = "pagamento_id", foreignKey = @ForeignKey(name = "pagamento_fk")),
            inverseJoinColumns = @JoinColumn(name = "transacao_id", foreignKey = @ForeignKey(name = "transacao_fk")))
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Um pagamento pode estar vinculado a uma conta específica.
     * Exemplo: Os pagamentos podem ser realizados a partir de contas correntes, poupanças ou outras fontes financeiras.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private ContaUsuario contaRelacionada;

    /**
     * Um pagamento pode estar associado a uma categoria financeira.
     * Exemplo: Um pagamento pode ser classificado como "Despesa - Aluguel" ou "Receita - Salário", organizando melhor os registros financeiros.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_id"))
    private CategoriaFinanceira categoriaRelacionada;
}
