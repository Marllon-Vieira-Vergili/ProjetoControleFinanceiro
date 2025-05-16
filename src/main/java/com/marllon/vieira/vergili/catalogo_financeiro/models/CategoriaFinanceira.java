package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classificar suas transações, por exemplo, categorias de alimentação, transporte, lazer, salário, investimentos,
 *  moradia,etc..
 * organizando os gastos e receitas, pela categoria de gastos, pra ver onde cada dinheiro está indo.
 *
 */

@Entity
@Table(name = "categoria_das_contas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "contaRelacionada"})
public class CategoriaFinanceira {


    //----------------------------------------ATRIBUTOS--------------------------------------------------------//


    @Id
    @Setter(AccessLevel.NONE) //Não gerar setter para ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;


    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipos_categorias", nullable = false)
    private TiposCategorias tiposCategorias;

    @NotNull(message = "O campo SubtTipo não pode ser nulo")
    @Column(name = "subtipo_categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubTipoCategoria subTipo;


    //----------------------------------------RELACIONAMENTOS--------------------------------------------------------//


    /**
     * Representa uma categoria de contas que pode estar associada a uma conta específica.
     * Uma categoria de despesa, como "Conta de Luz", pode ser paga por qualquer conta
     * (corrente, poupança, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "conta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_conta"))
    private ContaUsuario contaRelacionada;

    /**
     * Uma categoria pode estar relacionada a vários pagamentos.
     * Exemplo: A categoria "Conta de Água" pode ter múltiplos registros de pagamento associados.
     */
    @OneToMany(mappedBy = "categoriaRelacionada", fetch = FetchType.LAZY)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Uma categoria pode estar associada a múltiplas transações financeiras.
     * Exemplo: A categoria "Despesa - Internet" pode ter diferentes transações registradas ao longo do tempo.
     */
    @OneToMany(mappedBy = "categoriaRelacionada", fetch = FetchType.LAZY)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Muitas categorias podem estar associadas a um usuário.
     * Exemplo: Um usuário pode ter categorias como "Despesas", "Receitas" e "Investimentos".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_usuario"))
    private Usuario usuarioRelacionado;

}
