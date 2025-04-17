package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
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
@EqualsAndHashCode(of = {"valor", "data", "descricao", "categorias"})
@ToString(of = {"id", "valor", "data", "descricao", "categorias"})
public class HistoricoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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


    /**MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Associar Historico de transações com Pagamentos relacionados(Many to Many)
    public void associarTransacaoComPagamento(Pagamentos pagamento) {

        //Instanciar primeiramente, um Array List vazio, caso pagamentoRelacionado seja nulo
        if (this.pagamentosRelacionados == null) {
            this.pagamentosRelacionados = new ArrayList<>();
        }
        //Verificar se já possui algum pagamento associado a esse histórico de transação
        if (this.pagamentosRelacionados.contains(pagamento)) {
            throw new IllegalArgumentException("Este pagamento ja está associado a esse histórico de transação!");
        }
        //Associar o histórico de transações, para o lado do pagamento
        this.pagamentosRelacionados.add(pagamento);

        //agora associando para o lado do pagamento também, bidirecionalmente, o histórico de transação
        if (!pagamento.getTransacoesRelacionadas().contains(this)) {
            pagamento.getTransacoesRelacionadas().add(this);
        }

    }

    //Associar Historico de transações com Conta relacionada(Many to One)
    public void associarTransacaoComConta(ContaUsuario conta) {

        //Associando o histórico de transações, para o lado da conta
        this.contaRelacionada = conta;
        //Se a conta(do lado um) não possuir um histórico de transações(do lado muitos históricos de transações)
        if (conta.getTransacoesRelacionadas() == null) {
            conta.setTransacoesRelacionadas(new ArrayList<>());
        }
        //Associar do lado conta a transação
        if (!conta.getTransacoesRelacionadas().contains(this)) {
            conta.getTransacoesRelacionadas().add(this);
        }

    }


    //Associar Historico de transações com Usuário relacionado(Many to One)
    public void associarTransacaoComUsuario(Usuario usuario) {

        //Associar a transação ao usuário
        this.usuarioRelacionado = usuario;

        //Verificar se a lista de transações não está vazia
        if (usuario.getTransacoesRelacionadas() == null) {
            usuario.setTransacoesRelacionadas(new ArrayList<>());
        }

        //Verificar se a transação possui algum usuário relacionado
        if (usuario.getTransacoesRelacionadas().contains(this)) {
            throw new IllegalArgumentException("Este usuário ja está associado a essa transação!");
        }
        //Associar também a transação a esse usuário
        usuario.getTransacoesRelacionadas().add(this);

    }

    //Associar Historico de transações com Categorias Relacionadas(Many to Many)
    public void associarTransacaoComCategoria(CategoriaFinanceira categoria) {


    //Já instanciar um novo array list
        if(this.categoriasRelacionadas == null) {
            this.categoriasRelacionadas = new ArrayList<>();
        }

        //Se essa categoria já estiver contida na lista de histórico de transações, retornar uma exception
        if(this.categoriasRelacionadas.contains(categoria)){
            throw new IllegalArgumentException("Esta categoria já está associada a esse histórico de transação!");
        }

        //associar a transacao a categoria do lado da transação para a categoria
        this.categoriasRelacionadas.add(categoria);

        //Se do lado da categoria for nula, instanciar um array list do outro lado
        if(categoria.getTransacoesRelacionadas() == null){
            categoria.setTransacoesRelacionadas(new ArrayList<>());
        }
        //Associar também ao lado da categoria
        if(!categoria.getTransacoesRelacionadas().contains(this)){
            categoria.getTransacoesRelacionadas().add(this);
        }

}




    /**MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Desassociar Historico de transações com Pagamentos relacionados(Many to Many)

    //Verificar se a transação possui um pagamento relacionado
    public void desassociarTransacaoDePagamento(Pagamentos pagamento) {

        //Verificar se essa transação está associada a um pagamento
        if (this.pagamentosRelacionados == null || !this.pagamentosRelacionados.contains(pagamento)) {
            throw new IllegalArgumentException("Esta transação não está associada a este pagamento!");
        }

        //Desassociar do lado do pagamento para a transação
        this.pagamentosRelacionados.remove(pagamento);

        //Desassociar também do lado pagamento, para o histórico de transações
        if (pagamento.getTransacoesRelacionadas() != null) {
            pagamento.getTransacoesRelacionadas().remove(this);
        }
    }



    //Desassociar Historico de transações com Conta relacionada(Many to One)
    public void desassociarTransacaoDeConta(ContaUsuario conta){

        //Verificar se essa transação está associada a uma conta
        if(this.contaRelacionada == null || !this.contaRelacionada.getTransacoesRelacionadas().contains(this)){
            throw new IllegalArgumentException("Esta transação não está associada a esta conta!!");
        }
        //Desassociar do lado da conta para a transação(uma conta para muitas transações)
            this.contaRelacionada.getTransacoesRelacionadas().remove(this);
            //Desassociar do lado transação para uma conta(lado one)
            this.contaRelacionada = null;
    }


    //Desassociar Historico de transações com Usuário relacionado(Many to One)
    public void desassociarTransacaoDeUsuario(Usuario usuario){

        //Verificar se o histórico de transações contem o usuário passado como parâmetro
        if(this.usuarioRelacionado == null || !this.usuarioRelacionado.getTransacoesRelacionadas().contains(this)){
            throw new NoSuchElementException("Este usuário não existe para ser desassociado!");
        }
        //Remover do lado de histórico de transações, do lado "Um" do usuario
            this.usuarioRelacionado.getTransacoesRelacionadas().remove(this);
        //desassociar o usuário dessa transação(do lado muitos)
        this.usuarioRelacionado = null;
    }

    //Desassociar Historico de transações com Categorias Relacioandas(Many to Many)
    public void desassociarTransacaoDeCategoria(CategoriaFinanceira categoria){

        //Verificar se o histórico de transações não está vazio, e se a categoria relacionada está contida aqui
        if(this.categoriasRelacionadas == null || !this.categoriasRelacionadas.contains(categoria)){
            throw new NoSuchElementException("Não há nenhum histórico de transação relacionado a essa categoria!!");
        }
        //Se tiver, uma transação associada a essa categoria, desassociar do lado da transação
        this.categoriasRelacionadas.remove(categoria);

        //Desassociar também do lado da categoria
        if(categoria.getTransacoesRelacionadas() != null){
            categoria.getTransacoesRelacionadas().remove(this);
        }
    }
}

