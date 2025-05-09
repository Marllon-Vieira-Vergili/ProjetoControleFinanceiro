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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"id", "nome", "telefone"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    @EqualsAndHashCode.Include
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


}
