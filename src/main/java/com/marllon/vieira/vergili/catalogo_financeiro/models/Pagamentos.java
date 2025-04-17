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

/**registros de despesas e contas a pagar e receber de alguém.. criará registro nessa entidade
 */

@Entity
@Table(name = "pagamentos")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = {"valor", "data", "descricao", "categoria"})
@ToString(of = {"id","valor", "data", "descricao", "categoria"})
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "valor",nullable = false)
    @NotNull(message = "O campo do valor não pode ser vazio!")
    @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
    private BigDecimal valor;

    @Column(name = "data",nullable = false)
    @NotNull(message = "O campo da data não pode ficar vazio!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @Column(name = "descricao", length = 255,nullable = false)
    @NotBlank(message = "O campo descrição não pode ficar vazio!")
    private String descricao;

    @Column(name = "categoria",nullable = false)
    @NotNull(message = "O campo de categoria não pode ser null!")
    @Enumerated(EnumType.STRING)
    private TiposCategorias categoria;



    //RELACIONAMENTOS

    /**muitos pagamentos, pode ter um usuário(muitos pagamentos, pode vir de um usuário apenas)
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_pagamento_usuario"))
    private Usuario usuarioRelacionado;

    /**Vários pagamentos, pode ter vários históricos de transações(varios pagamentos diversos, pode ter varias transacoes)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "pagamentos_e_transacoes", joinColumns = @JoinColumn
            (name = "pagamento_id",foreignKey = @ForeignKey(name = "pagamento_fk")),
            inverseJoinColumns = @JoinColumn(name = "transacao_id",foreignKey = @ForeignKey
                    (name = "transacao_fk")))
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**vários pagamentos, podem sair de uma conta(um pagamento(ou vários) é feito por uma conta de um usuário)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private ContaUsuario contaRelacionada;

    /**Vários pagamentos, podem ter várias categorias de pagamentos(Vários pagamentos podem ter vários categorias e tipos
    de pagamentos)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinTable(name = "pagamentos_e_categorias",joinColumns = @JoinColumn(name = "pagamento_id"),
                    foreignKey = @ForeignKey(name = "fk_pagamento"),inverseJoinColumns =
            @JoinColumn(name = "categoria_id"), inverseForeignKey = @ForeignKey(name = "fk_categoria"))
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


    /**MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */


    //Associar Pagamentos com Usuário relacionado(Many to one)
    public void associarPagamentoComUsuario(Usuario usuario) {

        //Se do lado do usuário, não existir um arrayList de pagamentos, instanciar
        if (usuario.getPagamentosRelacionados() == null) {
            usuario.setPagamentosRelacionados(new ArrayList<>());
        }
        //Instanciar um valor, se do lado do usuário não possuir esse pagamento associado
        if (!usuario.getPagamentosRelacionados().contains(this)) {
            //Associar o usuário ao pagamento, do lado do usuário, um usuário para muitos pagamentos
            usuario.getPagamentosRelacionados().add(this);
        }
            //Associar do lado dos pagamentos bidirecionalmente
            if (this.usuarioRelacionado == null) {
                this.usuarioRelacionado = usuario;
        }
    }

    //Associar Pagamentos com Transações Relacionadas(Many to Many)
    public void associarPagamentoATransacao(HistoricoTransacao transacao){

        //Instanciar um novo arrayList de pagamentos, se do lado das transações não possuir
        if(transacao.getPagamentosRelacionados() == null){
            transacao.setPagamentosRelacionados(new ArrayList<>());
        }
        //Associar do lado de transações ao pagamentos
        if(!transacao.getPagamentosRelacionados().contains(this)){
            transacao.getPagamentosRelacionados().add(this);
        }

        //agora, instanciar um arrayList do lado de pagamentos a transações, se não tiver um array list deste lado
        if(this.transacoesRelacionadas == null){
            this.transacoesRelacionadas = new ArrayList<>();
        }
        //e Associar do lado de pagamentos a transações
        if(!this.transacoesRelacionadas.contains(transacao)){
            this.transacoesRelacionadas.add(transacao);
        }
    }

    //Associar Pagamentos com Conta relacionada(Many to one)
    public void associarPagamentoComConta(ContaUsuario conta){

        //Se do meu lado da conta, que é uma conta para muitos pagamentos, não possuir um arrayList de pagamentos, criar
        if(conta.getPagamentosRelacionados() == null){
            conta.setPagamentosRelacionados(new ArrayList<>());
        }
        //Associar do lado da conta a este pagamento
        if(!conta.getPagamentosRelacionados().contains(this)){
            conta.getPagamentosRelacionados().add(this);
        }
        //Do lado do pagamento, associar também a conta se ainda não foi feito
        if(this.contaRelacionada == null || !this.contaRelacionada.equals(conta)){
            //Associar também do lado de pagamentos a essa conta passada como parâmetro
            this.contaRelacionada = conta;
        }
    }

    //Associar Pagamentos com Categoria de ContaUsuario relacionada(Many to Many)
    public void associarPagamentoComCategoria(CategoriaFinanceira categoria){

        //Instanciar um novo arrayList, do pado pagamentos, se estiver nula
        if(this.categoriasRelacionadas == null){
            this.categoriasRelacionadas = new ArrayList<>();
        }
        //Associar o pagamento a categoria
        if(!this.categoriasRelacionadas.contains(categoria)){
            this.categoriasRelacionadas.add(categoria);
            //Associar também do lado da categoria
        }

        //Verificar se do lado categoria, não possui um array vazio, se tiver, instanciar também
        if(categoria.getPagamentosRelacionados() == null){
            categoria.setPagamentosRelacionados(new ArrayList<>());
        }
        //Associar do lado categoria também ao array list
        if(!categoria.getPagamentosRelacionados().contains(this)){
            categoria.getPagamentosRelacionados().add(this);
        }
    }

    /**MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE
     */

    //Desassociar pagamento de Usuario(Many to one)
    public void desassociarPagamentoUsuario(Usuario usuario){

        //Verificar, se existe algum usuário associado a esse pagamento
        if(this.usuarioRelacionado == null || this.usuarioRelacionado.getPagamentosRelacionados() == null ||
                !this.usuarioRelacionado.getPagamentosRelacionados().contains(this)){
            throw new NoSuchElementException("Não existe nenhum pagamento associado a esse usuário");
        }

        //Desassociar do lado do usuario, deste pagamento(lado one to many do usuário)
        if(usuario.getPagamentosRelacionados() != null){
            usuario.getPagamentosRelacionados().remove(this);
        }
        //Desassociar deste lado também, do lado pagamentos, muitos pagamentos para um usuario
        if(this.usuarioRelacionado != null && this.usuarioRelacionado.getPagamentosRelacionados().contains(this)){
            this.usuarioRelacionado = null;
        }
    }

    //Desassociar pagamento de Transação(Many to Many)
    public void desassociarPagamentoDeTransacao(HistoricoTransacao transacao){

        //Verificar, primeiramente, se existe algum pagamento associado a essa transação
        if(this.transacoesRelacionadas == null || !this.transacoesRelacionadas.contains(transacao)){
            throw new NoSuchElementException("Não há nenhum pagamento associado a essa transação");
        }
        //Desassociar do lado do pagamento ao histórico de transação
        this.transacoesRelacionadas.remove(transacao);

        //Desassociar agora do lado da transação ao pagamento
        if(transacao.getPagamentosRelacionados() != null){
            transacao.getPagamentosRelacionados().remove(this);
        }

    }
    //Desassociar pagamento de Conta(Many to One)
    public void desassociarPagamentoConta(ContaUsuario conta) {

        //Verificar, primeiramente, se existe algum pagamento associado a essa transação
        if (this.contaRelacionada.getPagamentosRelacionados() == null) {
            throw new NoSuchElementException("Não há nenhum pagamento associado a essa conta!");
        }
        //Desassociar do lado da conta, para os pagamentos encontrados
        if (conta.getPagamentosRelacionados() != null) {
            conta.getPagamentosRelacionados().remove(this);
        }
        //desassociar também do lado da conta, que vai receber essa conta
        if(this.contaRelacionada.equals(conta)) {
            this.contaRelacionada = null;
        }
    }


    //Desassociar pagamento de Categoria(Many to Many)
    public void desassociarPagamentoCategoria(CategoriaFinanceira categoria){

        //Verificar, primeiramente, se existe algum pagamento associado a essa categoria
        if (this.categoriasRelacionadas == null || !this.categoriasRelacionadas.contains(categoria)) {
            throw new NoSuchElementException("Não há nenhum pagamento associado a essa categoria!");
        }
        //Desassociar tanto do lado dos pagamentos
        this.categoriasRelacionadas.remove(categoria);
        //Quanto do lado da categoria aos pagamentos
        if(categoria.getPagamentosRelacionados() != null) {
            categoria.getPagamentosRelacionados().remove(this);
        }
    }


}
