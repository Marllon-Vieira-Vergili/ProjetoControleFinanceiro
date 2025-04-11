package com.marllon.vieira.vergili.catalogo_financeiro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

//gerenciar perfis e permissoes

@Entity
@Table(name = "usuarios")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(of = "id")
@ToString
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)

    private Long id;

    @Column(name = "nome", length = 50,nullable = false)
    @NotBlank(message = "Nome do usuário é obrigatório!")
    private String nome;

    @Column(name = "email", length = 64,unique = true,nullable = false)
    @Email(message = "o email deve conter o formato de um email. Exemplo:(nome@email.com)")
    @NotBlank(message = "Campo email é obrigatório!")
    private String email;

    @Column(name = "senha", length = 60,nullable = false)//Comprimento para salvamento por Bcrypt, se necessário
    @NotBlank(message = "Senha é obrigatória!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$", message = "A senha deve possuir pelo menos uma letra maiúscula e um numero!")
    @Size(min = 6, message = "A senha necessita ter no mínimo 6 caracteres, incluindo letras, ou números!")
    private String senha;

    @Column(name = "telefone",unique = true,nullable = false)
    @NotBlank(message = "Campo telefone é obrigatório!")
    @Size(min = 11, message = "Padrão de telefone aceito: (DDD)00000-0000")
    @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (DDD)00000-0000")
    private String telefone;



    //RELACIONAMENTOS

    //Um usuário pode ter vários pagamentos realizados
    @OneToMany(mappedBy = "pagamentosRelacionadosUsuario",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> usuariorelacionadoPagamentos = new ArrayList<>();

    //Um Usuário pode possuir vários históricos de transação(usuário pode possuir vários historicos, etc)
    @OneToMany(mappedBy = "transacoesRelacionadoUsuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacoes> usuarioRelacionadoTransacoes = new ArrayList<>();

    //Um usuário pode possuir uma conta(uma conta para mostrar seus gastos, receitas, etc.)
    @OneToOne(mappedBy = "contaRelacionadoUsuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contas usuarioRelacionadoConta;

    //Um usuário pode ter várias categorias de pagamentos relacionados(um usuário pode ter várias categorias de contas pagas
    @OneToMany(mappedBy = "categoriaRelacionadoUsuario",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CategoriasContas> usuarioRelacionadoCategorias = new ArrayList<>();


    //ASSOCIAÇÔES COM OUTRAS ENTIDADES BIDIRECIONALMENTE

}
