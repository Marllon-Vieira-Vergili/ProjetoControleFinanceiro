package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas.CONTA_CORRENTE;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas.CONTA_POUPANCA;


/**
 * representar diferentes contas bancárias ou carteiras, como saldo, titulo, corrente, poupança, etc.
 * Controlar direitinho o que sai de cada conta, cada uma de cada usuário
 */

@Entity
@Table(name = "contas",uniqueConstraints = {
        @UniqueConstraint(name = "fk_conta_usuario", columnNames = "usuario_id")})
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = {"nome", "saldo"})
@ToString(of = {"id","nome", "saldo"})
public class ContaUsuario {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotBlank(message = "O campo do nome não pode ficar vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
            message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
    private String nome;

    @Column(name = "saldo", nullable = false)
    @NotNull(message = "O campo do saldo na conta não pode ser nulo!")
    private BigDecimal saldo;


    @Column(name = "tipo_conta", nullable = false)
    @NotNull(message = "O campo TipoConta não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private TiposContas tipoConta;


    //RELACIONAMENTOS

    /**
     * uma conta pode ter várias categorias de recebimentos e gastos;(uma conta(poupança,corrente,etc..)
     * pode ter várias
     * categorias de pagamentos, seja despesa, receita, etc.
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


    /**
     * Uma conta pode ter vários pagamentos relacionados(uma conta(poupança,corrente,etc..)
     * pode ter vários pagamentos feitos
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Uma conta pode ter várias transações relacionadas(uma conta (poupança,corrente,etc..)
     * pode ter várias transações realizdas
     */
    @OneToMany(mappedBy = "contaRelacionada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Muitas Contas pode ter um usuário relacionado(Muitas contas de perfil)
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_conta_usuario"))
    private Usuario usuarioRelacionado;


    /**
     * MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */


    //Associar o tipo de conta a um enum conta
    public void associarTipoConta(TiposContas tipoConta) {

        //Associar o tipo da conta
        if (tipoConta != null) {
            this.tipoConta = tipoConta;
        } else {
            throw new IllegalArgumentException("Não foi possível " +
                    "associar o tipo de conta com esse valor passado. Por favor, selecione entre: "
                    + Arrays.toString(TiposContas.values()));
        }

    }


    //Associar Conta com Categoria de contas(One to Many)
    public void associarContaComCategoria(CategoriaFinanceira categoriaConta) {

        //Inicializar a lista primeiramente, para evitar erros de NullPointerException
        if (this.categoriasRelacionadas == null) {
            this.categoriasRelacionadas = new ArrayList<>();
        }
        //verificar se já não conta associado a essa categoria
        if (categoriasRelacionadas.contains(categoriaConta)) {
            throw new IllegalArgumentException("A categoria ja está associada a essa conta!");
        }
        //Se não existir, adicionar no lado das contas, o valor da categoria de contas
        this.categoriasRelacionadas.add(categoriaConta);
        //adicionar também do lado de categorias de contas
        categoriaConta.setContaRelacionada(this); //categoria, essa é sua conta
    }

    //Associar Conta com Pagamentos (One to Many) - Uma conta pode ter muitos pagamentos
    public void associarContaComPagamentos(Pagamentos pagamento) {

        //ter certeza que a lista não seja nula, já instanciando um novo array list
        if (this.pagamentosRelacionados == null) {
            //Instanciar uma nova lista de array
            this.pagamentosRelacionados = new ArrayList<>();
        }
        //Verificar se já não existe algum pagamento associado a essa conta
        if (this.pagamentosRelacionados.contains(pagamento)) {
            throw new NoSuchElementException("Já existe esse pagamento associado com a conta");
        }
        //Associar bidirecionalmente,  tanto ao lado das contas com os pagamentos
        this.pagamentosRelacionados.add(pagamento); //PagamentosRelacionados a essas contas, adicionar o pagamento, da outra entidade
        //quando do lado dos pagamentos, a conta associada
        pagamento.setContaRelacionada(this); //Pagamento, setar a sua conta relacionada a este pagamentos relacionados(this)
    }

    //Associar Conta com Historico de Transações (One to Many)
    public void associarContaComTransacoes(HistoricoTransacao transacao) {

        //Ja instanciar uma nova lista, para ter certeza que a mesma não será nula, evitando NullPointerException
        if (this.transacoesRelacionadas == null) {
            transacoesRelacionadas = new ArrayList<>();
        }
        //Se o dado do histórico de transação já estiver associado a conta, retornar uma exceção
        if (this.transacoesRelacionadas.contains(transacao)) {
            throw new NoSuchElementException("Já existe essa transação associada a esta conta!");
        }

        //Associar bidirecionalmente, tanto do lado de contas para transações
        this.transacoesRelacionadas.add(transacao);
        //Quanto do lado de transações para contas
        transacao.setContaRelacionada(this);

    }

    //Associar Conta com Usuário (Many to One)
    public void associarContaComUsuario(Usuario usuario) {

        // Verificar se a conta já está associada
        if (this.usuarioRelacionado != null) {
            throw new IllegalArgumentException("Essa conta já está associada a um usuário!");
        }

        // Associar do lado da conta
        this.usuarioRelacionado = usuario;

        // Garantir que a lista de contas do usuário não seja nula
        if (usuario.getContasRelacionadas() == null) {
            usuario.setContasRelacionadas(new ArrayList<>());
        }

        // Adicionar a conta à lista do usuário
        usuario.getContasRelacionadas().add(this);
    }

    /**
     * MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Desassociar Conta com categoria de contas(One to Many)
    public void desassociarContaDeCategorias(CategoriaFinanceira categoriaConta) {

        //Verificar se existe alguma categoria associada a essa conta
        if (!this.categoriasRelacionadas.contains(categoriaConta)) {
            return;
        }
        //Senão, vou pegar essa conta, e vou desassociar a categoria dela
        categoriasRelacionadas.remove(categoriaConta);
        //Vou desassociar também do lado das categorias para as contas
        categoriaConta.setContaRelacionada(null);
    }

    //Desassociar Conta com Pagamentos (One to Many)
    public void desassociarContaDePagamento(Pagamentos pagamento) {

        //Verificar se existe o pagamento passado como parametro, associado a essa conta
        if (!this.pagamentosRelacionados.contains(pagamento)) {
            return;
        }
        //Senão, se existir algum pagamento associado a essa conta, desassociar do lado da conta para o pagamento
        this.pagamentosRelacionados.remove(pagamento);
        //Vou desassociar também do lado do pagamento, a conta associada
        pagamento.setContaRelacionada(null);
    }

    //Desassociar Conta com Historico de Transações (One to Many)
    public void desassociarContaDeHistoricoDeTransacao(HistoricoTransacao transacao) {

        //Verificar primeiramente, se existe o histórico de transação passado como parametro, vinculado a essa conta
        if (!this.transacoesRelacionadas.contains(transacao)) {
            return;
        }
        //Senão, se existir.. vamos desassociá-lo do lado de conta para transação
        this.transacoesRelacionadas.remove(transacao);
        //desassociando também do lado da transação
        transacao.setContaRelacionada(null);
    }

    //Desassociar Conta com Usuário (Many to One)
    public void desassociarContaDeUsuario(Usuario usuario) {
        // Verificar se o usuário realmente tem essa conta
        if (usuario.getContasRelacionadas() == null || !usuario.getContasRelacionadas().contains(this)) {
            return;
        }

        // Desassociar do lado da conta
        this.usuarioRelacionado = null;

        // Remover a conta da lista de contas do usuário
        usuario.getContasRelacionadas().remove(this);
    }


    /**
     * Métodos para adição e subtração de valores do saldo
     */


    public void adicionarSaldo(ContaUsuario conta, BigDecimal valor){
        //Pegar o novo saldo
        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);
    }

    public void subtrairSaldo(ContaUsuario conta, BigDecimal valor){
        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        conta.setSaldo(novoSaldo);
    }
}
