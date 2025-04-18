package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Classificar suas transações, por exemplo, categorias de alimentação, transporte, lazer, salário, investimentos,
 *  moradia,etc..
 * orzanizando os gastos e receitas, pela categoria de gastos, pra ver onde cada dinheiro está indo.
 *
 */

@Entity
@Table(name = "categoria_das_contas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "tiposCategorias")
@ToString(of = {"id", "contaRelacionada"})
public class CategoriaFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipos_categorias", nullable = false)
    private TiposCategorias tiposCategorias;


    private String subTipo;


    //RELACIONAMENTOS:

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


    /**MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Associar Categoria com contas(Many to One)
    public void associarCategoriaComConta(ContaUsuario conta) {

        //Verificar primeiramente, se essa conta passada como parametro, ja não existe na associação das duas entidades
        if (conta.getCategoriasRelacionadas() != null && conta.getCategoriasRelacionadas().contains(this)) {
            throw new IllegalArgumentException("Já existe uma conta associada a essa categoria de contas!");
        }

        //Associar a categoria a conta(Lado Many to one) - Muitas categorias para uma Conta
        this.contaRelacionada = conta;

        //Verificar se ao associar, sera o primeiro valor, se for, criar um arraylist
        if (conta.getCategoriasRelacionadas() == null) {
            this.contaRelacionada.setCategoriasRelacionadas(new ArrayList<>());
        }
        //Associar também do outro lado
        if (!(conta.getCategoriasRelacionadas().contains(this))) {
            this.contaRelacionada.getCategoriasRelacionadas().add(this);
        }

    }

    //Associar Categoria com Pagamento (Many to many)
    public void associarCategoriaComPagamentos(Pagamentos pagamento) {

        //Verificar se já não possui nenhum pagamento passado como parâmetro, já associado a essa categoria
        if (pagamento.getCategoriasRelacionadas().contains(this)) {
            throw new IllegalArgumentException("Esse pagamento já foi passado a essa categoria");
        }

        //Se a lista de pagamentos estiver vazia, criar uma nova array list
        if (this.pagamentosRelacionados == null) {
            this.pagamentosRelacionados = new ArrayList<>();
        }
        //Verificar a lista do outro lado também, se ela estiver vazia
        if (pagamento.getCategoriasRelacionadas() == null) {
            pagamento.setCategoriasRelacionadas(new ArrayList<>());
        }
        //verificar se os pagamentos relacionados contem esses pagamentos
        if (!(this.pagamentosRelacionados.contains(pagamento))) {
            this.pagamentosRelacionados.add(pagamento);
        }
        //Manter a bidirecionalidade no lado pagamentos também, pois são muitos por muitos
        if (!(pagamento.getCategoriasRelacionadas().contains(this))) {
            pagamento.getCategoriasRelacionadas().add(this);
        }
    }

    //Associar Categoria com Transações(Many to Many)
    public void associarCategoriaComTransacoes(HistoricoTransacao transacao) {

        //Verificar se já existe algum histórico de transação já associado a essa categoria
        if (transacao.getCategoriasRelacionadas().contains(this)) {
            throw new IllegalArgumentException("Já existe uma transação associada a essa categoria");
        }

        //Se a lista de transações relacionadas a pagamentos estiver vazia, instanciar uma nova llista
        if (this.transacoesRelacionadas == null) {
            this.transacoesRelacionadas = new ArrayList<>();
        }
        //Verificar do outro lado também a lista
        if (transacao.getCategoriasRelacionadas() == null) {
            transacao.setCategoriasRelacionadas(new ArrayList<>());
        }
        //se a lista das transacoes relacionadas não conter o elemento da transacao na outra lista, adicionar este elemento dentro dela
        if (!(transacoesRelacionadas.contains(transacao))) {
            this.transacoesRelacionadas.add(transacao);
        }

        //associar também com o outro lado para manter a bidirecionalidade
        if (!(transacao.getCategoriasRelacionadas().contains(this))) {
            transacao.getCategoriasRelacionadas().add(this);
        }
    }

    //Associar Categoria com Usuário(Many to one)
    public void associarCategoriaComUsuario(Usuario usuario) {

        //Verificar se já existe algum usuario já associado a essa categoria
        if (usuario.getCategoriasRelacionadas().contains(this)) {
            throw new IllegalArgumentException("Já existe um usuario associado a essa categoria");
        }

        //Associar o usuário a categoria (Many to one)
        this.usuarioRelacionado = usuario;

        //Verificar se a lista de categorias de contas relacionadas ao usuario está vazio
        if (usuario.getCategoriasRelacionadas() == null) {
            usuario.setCategoriasRelacionadas(new ArrayList<>());
        }
        //Se a lista de categorias não estiver contido no usuário, associar
        if (!(usuario.getCategoriasRelacionadas().contains(this))) {
            usuario.getCategoriasRelacionadas().add(this);
        }
    }




    /**MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Desassociar categoria de contas a uma conta específica (Many to one)
    public void desassociarCategoriaAConta(ContaUsuario conta){

        //Se a conta passada não existir associação na categoria, retornar uma exception
        if(conta.getCategoriasRelacionadas() == null || !conta.getCategoriasRelacionadas().contains(this)){
            throw new NoSuchElementException("Não há nenhuma conta associado a essa categoria");
        }
        //Remover a associação da conta
        conta.getCategoriasRelacionadas().remove(this);
        //remover a associação da categoria com a conta, do lado da categoria aqui
        this.contaRelacionada = null;
    }

    //Desassociar categoria de contas a um pagamento específico (Many to Many)
    public void desassociarCategoriaAPagamento(Pagamentos pagamento){

        //Se o pagamento não existir associação na categoria, retornar uma exception
        if(pagamento.getCategoriasRelacionadas() == null || !pagamento.getCategoriasRelacionadas().contains(this)){
            throw new NoSuchElementException("Não há nenhum pagamento associado a essa categoria");
        }

        //Remover a associação do pagamento
        pagamento.getCategoriasRelacionadas().remove(this);

        //remover a associação da categoria na lista de pagamentos relacionados dentro da categoria
       if( this.pagamentosRelacionados != null){
           this.pagamentosRelacionados.remove(pagamento);
       }
    }

    //Desassociar categoria de contas a uma transação específica (Many to Many)
    public void desassociarCategoriaTransacao(HistoricoTransacao transacao){

        //Se o histórico de transação não existir associação na categoria, retornar uma exception
        if(transacao.getCategoriasRelacionadas() == null || !transacao.getCategoriasRelacionadas().contains(this)) {
            throw new NoSuchElementException("Não há nenhuma transação associada a essa categoria");
        }
            //Senão, remover a associação do histórico de transação
            transacao.getCategoriasRelacionadas().remove(this);

        //Remover também do lado da categoria relacionado a essa transacao
        if(this.transacoesRelacionadas != null){
            this.transacoesRelacionadas.remove(transacao);
        }

    }

    //Desassociar categoria de contas a um usuário específico (Many to one)
    public void desassociarCategoriaUsuario(Usuario usuario) {

        //Se o usuario não existir associação na categoria, retornar uma exception
        if (usuario.getCategoriasRelacionadas() == null || !usuario.getCategoriasRelacionadas().contains(this)) {
            throw new NoSuchElementException("Não há nenhum usuário associado a essa categoria");
        }
        //Remover a associação do lado do usuário
        usuario.getCategoriasRelacionadas().remove(this);
        //Remover a associação também do lado da categoria de contas relacionado a esse usuário
        this.usuarioRelacionado = null;
    }


}
