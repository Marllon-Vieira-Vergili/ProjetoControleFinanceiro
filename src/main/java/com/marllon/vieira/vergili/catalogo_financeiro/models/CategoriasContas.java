package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//Classificar suas transações, por exemplo, categorias de alimentação, transporte, lazer, salário, investimentos,
// moradia,etc..
//orzanizando os gastos e receitas, pela categoria de gastos, pra ver onde cada dinheiro está indo.
@Entity
@Table(name = "categoria_das_contas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "id")
@ToString
public class CategoriasContas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;


    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tiposCategorias",nullable = false)
    private TiposCategorias tiposCategorias;


    @Column(name = "descricao",nullable = false)
    @NotBlank(message = "O campo da descrição de uma categoria não pode ser vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
            message = "Descrição contém caracteres inválidos!")
    @Size(min = 5,message = "Descrição aceita de no mínimo 5 caracteres")
    private String descricao;



    //RELACIONAMENTOS:

    //Várias categorias de contas, pode ter uma conta relacionadas(Uma categoria de despesa de conta, pode ter uma conta relacionada
    //ex: uma categoria de conta de despesa(conta de luz) pode ser paga por qualquer conta(corrente, poupança, etc..)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH,
            CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "contas_id",referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_contas_id_categorias")) //Categoria será associada a id da conta
    private Contas categoriasRelacionadasAConta;

    //várias categorias diferentes, ex:Contas água, luz, etc.. pode  ter vários pagamentos relacionados
    // (ex: pagamento agua, pagamento luz, etc.)
    @ManyToMany(mappedBy = "pagamentosRelacionadosCategorias", fetch = FetchType.LAZY)
    private List<Pagamentos> categoriasRelacionadasPagamentos = new ArrayList<>();


    //várias categorias pode ter várias transações. (Ex: Categoria despesa agua, internet, luz), pode estar associado a vários
    //tipos de transações separadamente
    @ManyToMany(mappedBy = "transacoesRelacionadasCategorias", fetch = FetchType.LAZY)
    private List<HistoricoTransacoes> categoriasRelacionadosTransacoes = new ArrayList<>();

    //muitas categorias de contas pode ter um usuário associado a essas categorias.ex: um usuário pode ter categoria
    //de despesas, de receitas, e investimento, etc.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_usuarios_id")) //categoria será relacionado a id do usuário, coluna de junção
    private Usuarios categoriaRelacionadoUsuario;



    //ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE


    //Associar Categoria com Conta
    public void associarCategoriaComContas(Contas contas) {
        //Associar a categoria a conta
        this.categoriasRelacionadasAConta = contas;
        //Condição se a categoria de contas não tiver nenhuma conta associada a ela
        if(categoriasRelacionadasAConta.getContasRelacionadasCategorias() == null) {
            contas.setContasRelacionadasCategorias(new ArrayList<>());
        }
        //Se a categoria de contas não conter nenhuma categoria relacionada a esta conta, irá adicionar nela mesmo
        if(!(categoriasRelacionadasAConta.getContasRelacionadasCategorias().contains(this))){
            contas.getContasRelacionadasCategorias().add(this);
            }
        }


    //Associar Categoria com Pagamentos
    public void associarCategoriaComPagamentos(Pagamentos pagamentos){


        //Verificar se ao associar, sera o primeiro valor, se for, criar um arraylist
        if(categoriasRelacionadasPagamentos == null){
            categoriasRelacionadasPagamentos = new ArrayList<>();
        }
        //Se essa categoria não tiver contido o elemento, adicioná-lo
        if(!(this.categoriasRelacionadasPagamentos.contains(pagamentos))){
            categoriasRelacionadasPagamentos.add(pagamentos);
        }
    }

    //Associar Categoria com Transações

    //Associar Categoria com Usuário



}