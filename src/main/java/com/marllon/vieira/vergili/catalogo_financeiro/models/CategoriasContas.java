package com.marllon.vieira.vergili.catalogo_financeiro.models;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//Classificar suas transações, por exemplo, categorias de alimentação, transporte, lazer, salário, investimentos,
// moradia,etc..
//orzanizando os gastos e receitas, pela categoria de gastos, pra ver onde cada dinheiro está indo.
@Entity
@Table(name = "categoriaDasContas")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoriasContas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;


    @NotNull(message = "O campo tipo de categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tiposCategorias")
    public TiposCategorias tiposCategorias;


    @Column(name = "descricao")
    @NotBlank(message = "O campo da descrição de uma categoria não pode ser vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
            message = "Descrição contém caracteres inválidos!")
    public String descricao;



    //RELACIONAMENTOS:

    //Uma categoria pode ter várias contas relacionadas(Uma categoria de despesa de conta, pode ter várias contas relacionadas
    //ex: uma categoria de conta de despesa(conta de luz) pode ser paga por qualquer conta(corrente, poupança, etc..)
    private List<Contas> categoriasRelacionadasAContas = new ArrayList<>();

    //várias categorias diferentes, ex:Contas água, luz, etc.. pode  ter vários pagamentos relacionados
    // (ex: pagamento agua, pagamento luz, etc.)
    private List<Pagamentos> categoriasRelacionadasPagamentos = new ArrayList<>();


    //várias categorias pode ter várias transações. (Ex: Categoria despesa agua, internet, luz), pode estar associado a vários
    //tipos de transações separadamente
    private List<HistoricoTransacoes> categoriasRelacionadosTransacoes = new ArrayList<>();

    //muitas categorias de contas pode ter um usuário associado a essas categorias.ex: um usuário pode ter categoria
    //de despesas, de receitas, e investimento, etc.
    private Usuarios categoriaRelacionadoUsuario;

    //ASSOCIAÇÔES COM OUTRAS ENTIDADES
}
