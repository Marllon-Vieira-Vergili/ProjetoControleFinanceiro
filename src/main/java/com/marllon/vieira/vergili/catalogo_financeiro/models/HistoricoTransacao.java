package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**registrar cada movimentação financeira Gerenciando contas, pagamentos e categorias, (histórico)
 *
 */

@Entity
@Table(name = "historico_transacoes")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "valor", "data", "descricao"})
public class HistoricoTransacao {

    @Id
    @Setter(AccessLevel.NONE) //Não gerar setter para ID
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

    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipos_categorias", nullable = false)
    private TiposCategorias tiposCategorias;

    // RELACIONAMENTOS

    /**
     * Um histórico de transação pode estar relacionado a vários pagamentos.
     * Exemplo: Um histórico pode representar uma compra parcelada ou múltiplos pagamentos
     * associados à mesma transação.
     */
    @ManyToMany(mappedBy = "transacoesRelacionadas", fetch = FetchType.LAZY)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Um histórico de transação pode estar associado a uma conta específica.
     * Exemplo: A transação pode ter ocorrido em uma conta corrente ou poupança.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_transacao_conta"))
    private ContaUsuario contaRelacionada;

    /**
     * Um histórico de transação pode estar vinculado a um usuário.
     * Exemplo: O usuário responsável pela transação pode ser identificado,
     * permitindo consultas e relatórios financeiros.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_transacao_usuario"))
    private Usuario usuarioRelacionado;



    /**
     * Um histórico de transação pode estar relacionado a uma categoria financeira.
     * Exemplo: A transação pode ser classificada como "Despesa - Alimentação" ou
     * "Receita - Salário", organizando melhor os registros financeiros.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_categoria_id"))
    private CategoriaFinanceira categoriaRelacionada;

}