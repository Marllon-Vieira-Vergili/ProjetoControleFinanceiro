package com.marllon.vieira.vergili.catalogo_financeiro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Setter(AccessLevel.NONE) //Não gerar setter para ID
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(name = "telefone",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ("99)99999-9999"))
    @NotBlank(message = "Campo telefone é obrigatório!")
    @Size(min = 14,max = 14, message = "Padrão de telefone aceito: (DDD)00000-0000")
    @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (99)99999-9999")
    private String telefone;


    //RELACIONAMENTOS

    /**
     * Um usuário pode ter vários pagamentos registrados.
     * Exemplo: O usuário pode realizar diferentes pagamentos ao longo do tempo.
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamentos> pagamentosRelacionados = new ArrayList<>();

    /**
     * Um usuário pode possuir múltiplos históricos de transações financeiras.
     * Exemplo: O usuário pode ter registros de compras, transferências, depósitos, etc.
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HistoricoTransacao> transacoesRelacionadas = new ArrayList<>();

    /**
     * Um usuário pode ter várias contas associadas.
     * Exemplo: O usuário pode possuir contas correntes, poupanças ou investimentos para gerenciar suas finanças.
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContaUsuario> contasRelacionadas = new ArrayList<>();

    /**
     * Um usuário pode ter categorias financeiras associadas.
     * Exemplo: As categorias podem representar diferentes tipos de despesas e receitas relacionadas ao usuário.
     */
    @OneToMany(mappedBy = "usuarioRelacionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoriaFinanceira> categoriasRelacionadas = new ArrayList<>();

}
