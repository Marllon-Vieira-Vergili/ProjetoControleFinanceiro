package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//registrar cada movimentação financeira Gerenciando contas, pagamentos e categorias, (histórico)

@Entity
@Table(name = "historico_transacoes")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "id")
@ToString
public class HistoricoTransacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @NotNull(message = "O valor da transação deve possuir um valor!")
    @Column(name = "valor",nullable = false)
    @Min(value = 1, message = "O valor mínimo para o histórico da transação realizada, é de R$ 1,00")
    private BigDecimal valor;

    @NotNull(message = "O campo de data está nulo! Necessita de um dado")
    @Column(name = "data",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    @NotBlank(message = "O campo de descrição está vazio!")
    @Column(name = "descricao",nullable = false)
    @Size(min = 5,message = "Descrição aceita de no mínimo 5 caracteres")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
            message = "Descrição contém caracteres inválidos!")
    private String descricao;

    @NotNull(message = "O campo de categoria não pode ser null!")
    @Column(name = "categoria",nullable = false)
    @Enumerated(EnumType.STRING)
    private TiposCategorias categorias;


    //Relacionamentos

    //Vários históricos de transações, pode ter vários pagamentos relacionados
    @ManyToMany(mappedBy = "transacoesRelacionadas",fetch = FetchType.LAZY)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    //vários históricos de transação, pode ter uma conta associada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contas_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_contas_id_transacoes"))
    private Contas contaRelacionada;

    //vários históricos de transação, pode ter um usuário associado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarios_id_transacoes") )
    private Usuarios usuarioRelacionado;

    //vários históricos de transação, pode ter vários tipos de categorias de contas relacionadas
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinTable(name = "transacoes_e_categorias", joinColumns = @JoinColumn(name = "transacoes_categorias")
                    ,foreignKey = @ForeignKey(name = "fk_transacoes_categorias"),inverseJoinColumns =
            @JoinColumn(name = "categorias_transacoes"),inverseForeignKey = @ForeignKey(name = "fk_categorias_transacoes"))
    private List<CategoriasContas> categoriasRelacionadas = new ArrayList<>();

    //ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE

    //Associar Historico de transações com Pagamentos relacionados(Many to Many)
    public void associarTransacaoComPagamento(Pagamentos pagamento){

        //Instanciar primeiramente, um Array List vazio, caso pagamentoRelacionado seja nulo
        if(this.pagamentosRelacionados == null){
            this.pagamentosRelacionados = new ArrayList<>();
        }
        //Verificar se já possui algum pagamento associado a esse histórico de transação
        if(this.pagamentosRelacionados.contains(pagamento)){
            throw new IllegalArgumentException("Este pagamento ja está associado a esse histórico de transação!");
        }
        //Associar o histórico de transações, para o lado do pagamento
            this.pagamentosRelacionados.add(pagamento);

        //agora associando para o lado do pagamento também, bidirecionalmente, o histórico de transação
        if(!pagamento.getTransacoesRelacionadas().contains(this)){
            pagamento.getTransacoesRelacionadas().add(this);
        }

    }
    //Associar Historico de transações com Conta relacionada(Many to One)
    public void associarTransacaoComConta(Contas conta){

        //Associando o histórico de transações, para o lado da conta
        this.contaRelacionada = conta;
        //Se a conta(do lado um) não possuir um histórico de transações(do lado muitos históricos de transações)
        if(conta.getTransacoesRelacionadas() == null){
            conta.setTransacoesRelacionadas(new ArrayList<>());
        }
        //Associar do lado conta a transação
        if(!conta.getTransacoesRelacionadas().contains(this)){
            conta.getTransacoesRelacionadas().add(this);
            }
        }


    //Associar Historico de transações com Usuário relacionado(Many to One)
    public void associarTransacaoComUsuario(Usuarios usuario){

        //Associar a transação ao usuário
        this.usuarioRelacionado = usuario;
    }

    //Associar Historico de transações com Categorias Relacioandas(Many to Many)



    //DESASSOCIAÇÂO COM OUTRAS ENTIDADES BIDIRECIONALMENTE

    //Desassociar Historico de transações com Pagamentos relacionados(Many to Many)

    //Desassociar Historico de transações com Conta relacionada(Many to One)

    //Desassociar Historico de transações com Usuário relacionado(Many to One)

    //Desassociar Historico de transações com Categorias Relacioandas(Many to Many)

}
