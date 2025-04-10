package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
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

//registrar cada movimentação financeira Gerenciando contas, pagamentos e categorias, (histórico)

@Entity
@Table(name = "historicoTransacoes")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class HistoricoTransacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @NotNull(message = "O valor da transação deve possuir um valor!")
    @Column(name = "valor")
    @Min(value = 1, message = "O valor mínimo para o histórico da transação realizada, é de R$ 1,00")
    public BigDecimal valor;

    @NotNull(message = "O campo de data está nulo! Necessita de um dado")
    @Column(name = "data")
    public Date data;

    @NotBlank(message = "O campo de descrição está vazio!")
    @Column(name = "descricao")
    @Size(min = 5,message = "Descrição aceita de no mínimo 5 caracteres")
    public String descricao;

    @NotNull(message = "O campo de categoria não pode ser null!")
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    public TiposCategorias categorias;


    //Relacionamentos

    //Vários históricos de transações, pode ter vários pagamentos relacionados
    List<Pagamentos> transacoesRelacionadosPagamentos = new ArrayList<>();

    //vários históricos de transação, pode ter uma conta associada
    Contas transacoesRelacionadoConta;

    //vários históricos de transação, pode ter um usuário associado
    Usuarios transacoesRelacionadoUsuario;

    //vários históricos de transação, pode ter vários tipos de categorias de contas relacionadas
    List<CategoriasContas> transacoesRelacionadasCategorias = new ArrayList<>();

    //ASSOCIAÇÔES COM OUTRAS ENTIDADES
}
