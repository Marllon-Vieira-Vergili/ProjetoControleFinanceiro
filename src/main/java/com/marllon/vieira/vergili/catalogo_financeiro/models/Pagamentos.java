package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//registros de despesas e contas a pagar e receber de alguém.. criará registro nessa entidade

@Entity
@Table(name = "pagamentos")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "id")
@ToString
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "valor",nullable = false)
    @NotNull(message = "O campo do valor não pode ser vazio!")
    @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
    private BigDecimal valor;

    @Column(name = "data",nullable = false)
    @NotNull(message = "O campo da data não pode ficar vazio!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    @Column(name = "descricao", length = 255,nullable = false)
    @NotBlank(message = "O campo descrição não pode ficar vazio!")
    private String descricao;

    @Column(name = "categoria",nullable = false)
    @NotBlank(message = "O campo da categoria não pode ser vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$", message =
            "A categoria só aceita caracteres! Ex: conta de energia, conta de água, fatura cartão de crédito, etc.")
    private String categoria;



    //RELACIONAMENTOS

    //Um pagamento, pode ter um usuário(um pagamento pode vir de um usuário apenas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_usuarios_pagamentos"))
    private Usuarios usuarioRelacionado;

    //Vários pagamentos, pode ter vários históricos de transações(varios pagamentos diversos, pode ter varias transacoes)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "pagamentos_e_transacoes", joinColumns = @JoinColumn
            (name = "pagamentos_transacoes",foreignKey = @ForeignKey(name = "fk_pagamentos_transacoes")),
            inverseJoinColumns = @JoinColumn(name = "transacoes_pagamentos",foreignKey = @ForeignKey
                    (name = "fk_transacoes_pagamentos")))
    private List<HistoricoTransacoes> transacoesRelacionadas = new ArrayList<>();

    //vários pagamentos, podem sair de uma conta(um pagamento(ou vários) é feito por uma conta de um usuário)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_contas_pagamentos"))
    private Contas contaRelacionada;

    //Vários pagamentos, podem ter várias categorias de pagamentos(Vários pagamentos podem ter vários categorias e tipos
    //de pagamentos)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinTable(name = "pagamentos_e_categorias",joinColumns = @JoinColumn(name = "pagamento_categoria"),
                    foreignKey = @ForeignKey(name = "fk_pagamento_categoria"),inverseJoinColumns =
            @JoinColumn(name = "categoria_pagamento"), inverseForeignKey = @ForeignKey(name = "fk_categoria_pagamento"))
    private List<CategoriasContas> categoriasRelacionadas = new ArrayList<>();


    //ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
}
