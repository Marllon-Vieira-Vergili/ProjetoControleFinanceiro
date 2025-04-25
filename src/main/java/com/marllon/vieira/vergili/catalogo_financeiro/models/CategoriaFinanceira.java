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
@EqualsAndHashCode(of = "tiposCategorias")
@ToString(of = {"id", "contaRelacionada"})
public class CategoriaFinanceira {


    //----------------------------------------ATRIBUTOS--------------------------------------------------------//


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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



//--------------------MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE----------------------------//

    //jogar na lógica de negócios
    /**
     * Estes métodos são associações desta entidade CategoriaFinanceira com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * O retorno dos dados vai variar conforme o nome da outra entidade
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */

    public void associarCategoriaComConta(ContaUsuario conta) {

        //Verificar primeiramente, se essa conta passada como parametro, ja não existe na associação das duas entidades
        if (conta.getCategoriasRelacionadas() != null && conta.getCategoriasRelacionadas().contains(this)) {
            throw new JaExisteException("Já existe uma conta associada a essa categoria de contas!");
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



    public void associarCategoriaComPagamentos(Pagamentos pagamento) {

        //Verificar se já não possui nenhum pagamento passado como parâmetro, já associado a essa categoria
        if (pagamento.getCategoriasRelacionadas().contains(this)) {
            throw new JaExisteException("Esse pagamento já foi passado a essa categoria");
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

    public void associarCategoriaComTransacoes(HistoricoTransacao transacao) {

        //Verificar se já existe algum histórico de transação já associado a essa categoria
        if (transacao.getCategoriasRelacionadas().contains(this)) {
            throw new JaExisteException("Já existe uma transação associada a essa categoria");
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


    public void associarCategoriaComUsuario(Usuario usuario) {

        //Verificar se já existe algum usuario já associado a essa categoria
        if (usuario.getCategoriasRelacionadas().contains(this)) {
            throw new JaExisteException("Já existe um usuario associado a essa categoria");
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


    public void associarSubTipoCategoriaComDespesa(SubTipoCategoria subTipo) {
        //Verificar se o tipo de categoria é despesa
        if (this.tiposCategorias.equals(TiposCategorias.DESPESA)) {
            List<SubTipoCategoria> subtiposDespesas = TiposCategorias.mostrarTodasDespesas();
            if(subtiposDespesas == null || !subtiposDespesas.contains(subTipo)) {
                throw new IllegalArgumentException("Não existe esse subtipo de categoria para associar a essa categoria," +
                        " os tipos disponíveis são: " + subtiposDespesas);
            }
            //Associar o subtipo a categoria
            this.subTipo = subTipo;
        } else {
            throw new ObjectNotFoundException(subTipo,
                    "Não é possível associar esse tipo de subTipo, pois ele é de RECEITA");
        }
    }


    public void associarSubTipoCategoriaComReceita(SubTipoCategoria subTipo) {
        //Verificar se o tipo de categoria é receita
        if (this.tiposCategorias.equals(TiposCategorias.RECEITA)) {
            List<SubTipoCategoria> subtiposReceitas = TiposCategorias.mostrarTodasReceitas();
            if(subtiposReceitas == null || !subtiposReceitas.contains(subTipo)) {
                throw new SubTipoNaoEncontrado("Não existe esse subtipo de categoria para associar a essa categoria," +
                        " os tipos disponíveis são: " + subtiposReceitas);
            }
            //Associar o subtipo a categoria
            this.subTipo = subTipo;
        } else {
            throw new ObjectNotFoundException(subTipo,
                    "Não é possível associar esse tipo de subTipo, pois ele é de DESPESA");
        }
    }


    //--------------------MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE----------------------------//

    /**
     * Estes métodos são desassociações desta entidade CategoriaFinanceira com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * Não irá ter retorno dos dados, pois só será feito para execução
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */
    public void desassociarCategoriaAConta(ContaUsuario conta){

        //Se a conta passada não existir associação na categoria, retornar uma exception
        if(conta.getCategoriasRelacionadas() == null || !conta.getCategoriasRelacionadas().contains(this)){
            throw new ObjectNotFoundException(conta,"Não há nenhuma conta associado a essa categoria");
        }
        //Remover a associação da conta
        conta.getCategoriasRelacionadas().remove(this);
        //remover a associação da categoria com a conta, do lado da categoria aqui
        this.contaRelacionada = null;
    }


    public void desassociarCategoriaAPagamento(Pagamentos pagamento){

        //Se o pagamento não existir associação na categoria, retornar uma exception
        if(pagamento.getCategoriasRelacionadas() == null || !pagamento.getCategoriasRelacionadas().contains(this)){
            throw new ObjectNotFoundException(pagamento,"Não há nenhum pagamento associado a essa categoria");
        }

        //Remover a associação do pagamento
        pagamento.getCategoriasRelacionadas().remove(this);

        //remover a associação da categoria na lista de pagamentos relacionados dentro da categoria
       if( this.pagamentosRelacionados != null){
           this.pagamentosRelacionados.remove(pagamento);
       }
    }


    public void desassociarCategoriaTransacao(HistoricoTransacao transacao){

        //Se o histórico de transação não existir associação na categoria, retornar uma exception
        if(transacao.getCategoriasRelacionadas() == null || !transacao.getCategoriasRelacionadas().contains(this)) {
            throw new ObjectNotFoundException(transacao,"Não há nenhuma transação associada a essa categoria");
        }
            //Senão, remover a associação do histórico de transação
            transacao.getCategoriasRelacionadas().remove(this);

        //Remover também do lado da categoria relacionado a essa transacao
        if(this.transacoesRelacionadas != null){
            this.transacoesRelacionadas.remove(transacao);
        }

    }


    public void desassociarCategoriaUsuario(Usuario usuario) {

        //Se o usuario não existir associação na categoria, retornar uma exception
        if (usuario.getCategoriasRelacionadas() == null || !usuario.getCategoriasRelacionadas().contains(this)) {
            throw new ObjectNotFoundException(usuario, "Não há nenhum usuário associado a essa categoria");
        }
        //Remover a associação do lado do usuário
        usuario.getCategoriasRelacionadas().remove(this);
        //Remover a associação também do lado da categoria de contas relacionado a esse usuário
        this.usuarioRelacionado = null;
    }



    public void desassociarTipoCategoriaComDespesa(SubTipoCategoria subTipo) {
        //Verificar se o tipo de categoria é despesa
        if (this.tiposCategorias.equals(TiposCategorias.DESPESA)) {
            if(this.subTipo == null || !this.subTipo.equals(subTipo)) {
                throw new SubTipoNaoEncontrado("Não há um subtipo associado para remoção");
            }

            //Desassociar do subtipo
            this.subTipo = null;
        }else{
            throw new ObjectNotFoundException(subTipo,
                    "Não é possível remover um subtipo de uma categoria que não seja da despesa");
        }
    }


    public void desassociarTipoCategoriaComReceita(SubTipoCategoria subTipo) {
        //Verificar se o tipo de categoria é receita
        if (this.tiposCategorias.equals(TiposCategorias.RECEITA)) {
            if(this.subTipo == null || !this.subTipo.equals(subTipo)) {
                throw new SubTipoNaoEncontrado("Não há um subtipo associado para remoção");
            }
            //Desassociar do subtipo
            this.subTipo = null;
        } else {
            throw new ObjectNotFoundException(subTipo,
                    "Não é possível remover um subtipo de uma categoria que não seja receita");
        }
    }
}
