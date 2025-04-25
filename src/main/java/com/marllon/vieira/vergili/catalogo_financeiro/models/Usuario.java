package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**Classe do usuário gerenciar perfis e permissoes, o usuário criará um nome, colocará seu email, senha, e telefone
 * para criar uma "Conta" e logar nesta
 *
 */

@Entity
@Table(name = "usuarios",uniqueConstraints = {
        @UniqueConstraint(name = "unique_usuario_email",columnNames = "email"),
@UniqueConstraint(name = "unique_usuario_telefone", columnNames = "telefone")})
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "nome", "telefone"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "nome", length = 50,nullable = false)
    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]+$",
            message = "O nome só pode conter letras, espaços, apóstrofos e hífens!")
    @NotBlank(message = "Nome do usuário é obrigatório!")
    private String nome;

    @Column(name = "email", length = 64,nullable = false)
    @Email(message = "o email deve conter o formato de um email. Exemplo:(nome@email.com)")
    @NotBlank(message = "Campo email é obrigatório!")
    private String email;

    @Column(name = "senha", length = 60,nullable = false)//Comprimento para salvamento por Bcrypt, se necessário
    @NotBlank(message = "Senha é obrigatória!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$", message = "A senha deve possuir pelo menos uma letra maiúscula e um numero!")
    @Size(min = 6, message = "A senha necessita ter no mínimo 6 caracteres, incluindo letras, ou números!")
    private String senha;

    @Column(name = "telefone",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ("99)99999-9999"))
    @NotBlank(message = "Campo telefone é obrigatório!")
    @Size(min = 14,max = 14, message = "Padrão de telefone aceito: (DDD)00000-0000")
    @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (99)99999-9999")
    private String telefone;


    //RELACIONAMENTOS

    /**Um usuário pode ter vários pagamentos realizados
     *
     */
    @OneToMany(mappedBy = "usuarioRelacionado",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**Um Usuário pode possuir vários históricos de transação(usuário pode possuir vários historicos, etc)
     *
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**Um usuário pode possuir uma conta(uma conta para mostrar seus gastos, receitas, etc.)
     *
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContaUsuario> contasRelacionadas;

    /**Um usuário pode ter várias categorias de pagamentos relacionados(um usuário pode ter várias categorias de contas pagas
     *
     */
    @OneToMany(mappedBy = "usuarioRelacionado",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();


    //--------------------MÈTODOS DE ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE----------------------------//
    /**
     * Estes métodos são associações desta entidade CategoriaFinanceira com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * O retorno dos dados vai variar conforme o nome da outra entidade
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */


    public void associarUsuarioComPagamento(Pagamentos pagamento){

        //Verificar se a lista de pagamentos do usuario é nula, se for nula, criar uma nova lista de array
        if(this.pagamentosRelacionados == null || this.pagamentosRelacionados.isEmpty()){
            this.pagamentosRelacionados = new ArrayList<>();
        }
        //Se a lista ja nao for mais nula, ou ja estiver criada,Associar o usuário ao pagamento
        this.pagamentosRelacionados.add(pagamento);

        //Associar do lado do pagamento ao usuario
        if(pagamento.getUsuarioRelacionado() == null || !pagamento.getUsuarioRelacionado().equals(this)) {
            pagamento.setUsuarioRelacionado(this);
        }
    }


    public void associarUsuarioComTransacoes(HistoricoTransacao transacao){

        //Verificar se a lista de transações com o usuário é nula
        if(this.transacoesRelacionadas == null || this.transacoesRelacionadas.isEmpty()){
            this.transacoesRelacionadas = new ArrayList<>();
        }

        //Associar o usuário com as transações
        this.transacoesRelacionadas.add(transacao);

        //E também do lado de transação para um usuário
        if(transacao.getUsuarioRelacionado() == null) {
            transacao.setUsuarioRelacionado(this);
        }

    }

    public void associarUsuarioComConta(ContaUsuario conta){

        //Associar usuário com uma conta
        if(this.contasRelacionadas == null) {
            this.contasRelacionadas = new ArrayList<>();
        }
        //Associar o usuário com as contas
        this.contasRelacionadas.add(conta);

        //e também associar a conta ao usuário, do outro lado
        if(conta.getUsuarioRelacionado() == null){
            conta.setUsuarioRelacionado(this);
        }
    }

    public void associarUsuarioComCategoria(CategoriaFinanceira categoria){

        //Instanciar uma nova arrayList de usuario para categorias relacionadas, se a mesma ainda nao estiver sido criada
        if(this.categoriasRelacionadas == null ){
            this.categoriasRelacionadas = new ArrayList<>();
        }
    //Senão..
        //Associar usuario com a categoria
        if(!this.categoriasRelacionadas.contains(categoria)) {
            this.categoriasRelacionadas.add(categoria);
        }
        //Associar a categoria ao usuário(bidirecionalidade)
        if(categoria.getUsuarioRelacionado() == null || !categoria.getUsuarioRelacionado().equals(this)) {
            categoria.setUsuarioRelacionado(this);
        }
    }

    //--------------------MÈTODOS DE DESASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE----------------------------//
    /**
     * Estes métodos são desassociações desta entidade Usuario com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * Não irá ter retorno dos dados, pois só será feito para execução
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */
    //Desassociar Usuario com Pagamentos Relacionados(One to Many)
    public void desassociarUsuarioComPagamento(Pagamentos pagamento){

        //Verificar se o usuário ja está associado ao pagamento que será desassociado
        if(this.pagamentosRelacionados == null || !this.pagamentosRelacionados.contains(pagamento)){
            return;
        }

        //Desassocio o usuário do pagamento
        this.pagamentosRelacionados.remove(pagamento);

        //E o pagamento do usuário
        if(pagamento.getUsuarioRelacionado().equals(this)) {
            pagamento.setUsuarioRelacionado(null);
        }
    }
    //Desassociar Usuario com Transações Relacionadas(One to Many)
    public void desassociarUsuarioComTransacao(HistoricoTransacao transacao){

        //Verificar se o usuário ja está associado a transação que será desassociado
        if(this.transacoesRelacionadas == null || !this.transacoesRelacionadas.contains(transacao)){
            return;
        }
        //Desassocio do lado do usuário
        this.transacoesRelacionadas.remove(transacao);
        //E tambem desassocio do lado da transação
        if(transacao.getUsuarioRelacionado().equals(this)){
            transacao.setUsuarioRelacionado(null);
        }
    }
    //Desassociar usuario com Conta Relacionada(One to One)
    public void desassociarUsuarioComConta(ContaUsuario conta){

        //Verificar se o usuário ja está associado a uma conta
        if(this.contasRelacionadas == null || !this.contasRelacionadas.equals(conta)){
            return;
        }
        //Desassociar do lado da conta, para o usuário
        conta.setUsuarioRelacionado(null);
        //e  do lado do usuário com a conta
        this.contasRelacionadas = null;

    }
    //Desassociar usuário com Categorias de contas Relacionadas(One to Many)
    public void desassociarUsuarioComCategoria(CategoriaFinanceira categoria){

        //Verificar se o usuário possui alguma associação com essa categoria
        if(this.categoriasRelacionadas == null || !this.categoriasRelacionadas.contains(categoria)){
            return;
        }
        //Desassociar do lado do usuário com a cetegoria
        this.categoriasRelacionadas.remove(categoria);
        //E do lado da categoria com o usuário(bidirecionalmente)
        if(categoria.getUsuarioRelacionado() != null && categoria.getUsuarioRelacionado().equals(this)){
        categoria.setUsuarioRelacionado(null);
        }
    }
}
