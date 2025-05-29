package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) utilizado exclusivamente para a atualização de dados do Usuário.
 * <p>
 * Este record representa os dados que podem ser alterados pelo usuário:
 * nome, e-mail e telefone.
 * </p>
 *
 * <p>
 * Importante: <b>não inclui o campo senha</b> justamente para evitar alterações
 * indevidas ou inseguras da senha durante o processo de atualização de dados cadastrais.
 * Caso o usuário deseje alterar sua senha, deverá ser criado um DTO e método específicos.
 * </p>
 *
 * <p>
 * Validações incluídas:
 * <ul>
 *   <li>Nome: obrigatório, somente letras, espaços, apóstrofos e hífens.</li>
 *   <li>Email: obrigatório, formato válido.</li>
 *   <li>Telefone: obrigatório, padrão brasileiro (99)99999-9999.</li>
 * </ul>
 * </p>
 *
 * @author Marllon
 */

public record UsuarioUpdateRequest(@NotBlank(message = "Nome do usuário é obrigatório!")
                             @Pattern(
                                     regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]+$",
                                     message = "O nome só pode conter letras, espaços, apóstrofos e hífens!")
                             String nome,

                             @Email(message = "o email deve conter o formato de um email. Exemplo:(nome@email.com)")
                             @NotBlank(message = "Campo email é obrigatório!")
                             String email,


                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ("99)99999-9999"))
                             @NotBlank(message = "Campo telefone é obrigatório!")
                             @Size(min = 14,max = 14, message = "Padrão de telefone aceito: (DDD)00000-0000")
                             @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (99)99999-9999")
                             String telefone) {
}