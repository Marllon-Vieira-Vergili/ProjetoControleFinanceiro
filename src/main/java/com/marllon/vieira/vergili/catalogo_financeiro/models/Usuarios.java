package com.marllon.vieira.vergili.catalogo_financeiro.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//gerenciar perfis e permissoes

@Entity
@Table(name = "usuarios")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "nome", length = 50)
    @NotBlank(message = "necessário que o campo nome seja preenchido!")
    public String nome;

    @Column(name = "email", length = 64)
    @Email(message = "o email deve conter o formato de um email. Exemplo:(nome@email.com)")
    @NotBlank(message = "necessário que o campo email seja preenchido!")
    public String email;

    @Column(name = "senha", length = 60)//Comprimento para salvamento por Bcrypt, se necessário
    @NotBlank(message = "Necessário que o campo senha seja preenchido!")
    @Pattern(regexp = "^[A-Z].{5,}$", message = "A primeira letra da senha deve ser maiúscula e conter no mínimo 6 caracteres")
    @Size(min = 6, message = "A senha necessita ter no mínimo 6 caracteres, incluindo letras, ou números!")
    public String senha;

    @Column(name = "telefone")
    @NotBlank(message = "Necessário que o campo de telefone seja preenchido!")
    @Size(min = 11, message = "Padrão de telefone aceito: (DDD)00000-0000")
    @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (DDD)00000-0000")
    public String telefone;



    //RELACIONAMENTOS

    //Um usuário pode ter vários pagamentos realizados
    List<Pagamentos> usuariorelacionadoPagamentos = new ArrayList<>();

    //Um Usuário pode possuir vários históricos de transação(usuário pode possuir vários historicos, etc)
    List<HistoricoTransacoes> usuarioRelacionadoTransacoes = new ArrayList<>();

    //Um usuário pode possuir várias contas relacionadas(contas de investimento, bancos, etc.)
    List<Contas> usuarioRelacionadoContas = new ArrayList<>();

    //Um usuário pode ter várias categorias de pagamentos relacionados(um usuário pode ter várias categorias de contas pagas
    List<CategoriasContas> usuarioRelacionadoCategorias = new ArrayList<>();


    //ASSOCIAÇÔES COM OUTRAS ENTIDADES

}
