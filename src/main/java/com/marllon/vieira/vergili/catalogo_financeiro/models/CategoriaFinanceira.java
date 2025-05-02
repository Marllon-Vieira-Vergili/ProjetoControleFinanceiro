package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.ObjectNotFoundException;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;


    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipos_categorias", nullable = false)
    private TiposCategorias tiposCategorias;

    @NotNull(message = "O campo SubtTipo não pode ser nulo")
    @Column(name = "subtipo_categoria",nullable = false)
    @Enumerated(EnumType.STRING)
    private SubTipoCategoria subTipo;

    //Construtor dos enum
    public CategoriaFinanceira(TiposCategorias categoria, SubTipoCategoria subTipoCategoria) {

    }


    //----------------------------------------RELACIONAMENTOS--------------------------------------------------------//


    /**Várias categorias de contas, pode ter uma conta relacionadas(Uma categoria de despesa de conta, pode
     * ter uma conta relacionada ex: uma categoria de conta de despesa(conta de luz) pode ser paga por
     * qualquer conta(corrente, poupança, etc..)
     *
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,
            CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "conta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_conta"))
    //Categoria será associada a id da conta
    private ContaUsuario contaRelacionada;

    /**
     * várias categorias diferentes, ex:ContaUsuario água, luz, etc.. pode  ter vários pagamentos relacionados
     *   (ex: pagamento agua, pagamento luz, etc.)
     */
    @ManyToMany(mappedBy = "categoriasRelacionadas", fetch = FetchType.LAZY)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * várias categorias pode ter várias transações. (Ex: Categoria despesa agua, internet, luz),
     * pode estar associado a vários
     * tiipos de transações separadamente
     */
    @ManyToMany(mappedBy = "categoriasRelacionadas", fetch = FetchType.LAZY)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * muitas categorias de contas pode ter um usuário associado a essas categorias.ex: um usuário pode ter categoria
     *  de despesas, de receitas, e investimento, etc.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_usuario"))
    //categoria será relacionado a id do usuário, coluna de junção
    private Usuario usuarioRelacionado;



}
