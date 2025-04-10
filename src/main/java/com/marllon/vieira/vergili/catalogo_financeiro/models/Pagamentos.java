package com.marllon.vieira.vergili.catalogo_financeiro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//registros de despesas e contas a pagar e receber de alguém.. criará registro nessa entidade

@Entity
@Table(name = "pagamentos")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "valor")
    @NotNull(message = "O campo do valor não pode ser vazio!")
    @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
    public BigDecimal valor;

    @Column(name = "data")
    @NotNull(message = "O campo da data não pode ficar vazio!")
    public Date data;

    @Column(name = "descricao", length = 255)
    @NotBlank(message = "O campo descrição não pode ficar vazio!")
    public String descricao;

    @Column(name = "categoria")
    @NotBlank(message = "O campo da categoria não pode ser vazio!")
    @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$", message =
            "A categoria só aceita caracteres! Ex: conta de energia, conta de água, fatura cartão de crédito, etc.")
    public String categoria;



    //RELACIONAMENTOS

    //Um pagamento, pode ter um usuário(um pagamento pode vir de um usuário apenas)
    Usuarios pagamentosRelacionadosUsuario;

    //Vários pagamentos, pode ter vários históricos de transações(varios pagamentos diversos, pode ter varias transacoes)
    List<HistoricoTransacoes> pagamentosRelacionadosTransacoes = new ArrayList<>();

    //vários pagamentos, podem sair de uma conta(um pagamento(ou vários) é feito por uma conta de um usuário)
    Contas pagamentoRelacionadoContas;

    //Vários pagamentos, podem ter várias categorias de pagamentos(Vários pagamentos podem ter vários categorias e tipos
    //de pagamentos)
    List<CategoriasContas> pagamentosRelacionadosCategorias = new ArrayList<>();


    //ASSOCIAÇÔES COM OUTRAS ENTIDADES
}
