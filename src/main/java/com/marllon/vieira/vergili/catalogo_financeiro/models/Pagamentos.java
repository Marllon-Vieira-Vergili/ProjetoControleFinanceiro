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
@ToString(of = {"id","valor", "data", "descricao", "categoria"})
public class Pagamentos{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "valor",nullable = false)
    @NotNull(message = "O campo do valor não pode ser vazio!")
    @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
    private BigDecimal valor;

    @Column(name = "data",nullable = false)
    @NotNull(message = "O campo da data não pode ficar vazio!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @Column(name = "descricao", length = 255,nullable = false)
    @NotBlank(message = "O campo descrição não pode ficar vazio!")
    private String descricao;



    //RELACIONAMENTOS

    /**muitos pagamentos, pode ter um usuário(muitos pagamentos, pode vir de um usuário apenas)
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_pagamento_usuario"))
    private Usuario usuarioRelacionado;

    /**Vários pagamentos, pode ter vários históricos de transações(varios pagamentos diversos, pode ter varias transacoes)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "pagamentos_e_transacoes", joinColumns = @JoinColumn
            (name = "pagamento_id",foreignKey = @ForeignKey(name = "pagamento_fk")),
            inverseJoinColumns = @JoinColumn(name = "transacao_id",foreignKey = @ForeignKey
                    (name = "transacao_fk")))
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**vários pagamentos, podem sair de uma conta(um pagamento(ou vários) é feito por uma conta de um usuário)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private ContaUsuario contaRelacionada;

    /**Vários pagamentos, podem ter várias categorias de pagamentos(Vários pagamentos podem ter vários categorias e tipos
    de pagamentos)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinTable(name = "pagamentos_e_categorias",joinColumns = @JoinColumn(name = "pagamento_id"),
                    foreignKey = @ForeignKey(name = "fk_pagamento"),inverseJoinColumns =
            @JoinColumn(name = "categoria_id"), inverseForeignKey = @ForeignKey(name = "fk_categoria"))
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


}
