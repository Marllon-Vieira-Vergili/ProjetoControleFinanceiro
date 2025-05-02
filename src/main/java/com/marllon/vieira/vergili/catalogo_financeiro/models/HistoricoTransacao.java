package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**registrar cada movimentação financeira Gerenciando contas, pagamentos e categorias, (histórico)
 *
 */

@Entity
@Table(name = "historico_transacoes")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = {"valor", "data", "descricao", "categorias", "subTipo"})
@ToString(of = {"id", "valor", "data", "descricao", "categorias"})
public class HistoricoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "O valor da transação deve possuir um valor!")
    @Column(name = "valor", nullable = false)
    @Min(value = 1, message = "O valor mínimo para o histórico da transação realizada, é de R$ 1,00")
    private BigDecimal valor;

    @NotNull(message = "O campo de data está nulo! Necessita de um dado")
    @Column(name = "data", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @NotBlank(message = "O campo de descrição está vazio!")
    @Column(name = "descricao", nullable = false)
    @Size(min = 5, message = "Descrição aceita de no mínimo 5 caracteres")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
            message = "Descrição contém caracteres inválidos!")
    private String descricao;

    @NotNull(message = "O campo de categoria não pode ser null!")
    @Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private TiposCategorias categorias;

    @NotNull(message = "O campo SubtTipo não pode ser nulo")
    @Column(name = "subtipo_transacao",nullable = false)
    @Enumerated(EnumType.STRING)
    private SubTipoCategoria subTipo;


    //Relacionamentos

    /**Vários históricos de transações, pode ter vários pagamentos relacionados
     */
    @ManyToMany(mappedBy = "transacoesRelacionadas", fetch = FetchType.LAZY)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**vários históricos de transação, pode ter uma conta associada
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_transacao_conta"))
    private ContaUsuario contaRelacionada;

    /**Vários históricos de transação, pode ter um usuário associado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_transacao_usuario"))
    private Usuario usuarioRelacionado;

    /**vários históricos de transação, pode ter vários tipos de categorias de contas relacionadas
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "transacoes_e_categorias", joinColumns = @JoinColumn(name = "transacao_id")
            , foreignKey = @ForeignKey(name = "fk_transacao_id"), inverseJoinColumns =
    @JoinColumn(name = "categoria_id"), inverseForeignKey = @ForeignKey(name = "fk_categoria_id"))
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


}

