package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
Record para o usuário, ao enviar uma requisicao pela pela entidade de
Usuario.
Irá solicitar que ele coloque o nome de seu usuário, seu email, sua senha, seu telefone..
(Somente para envio de dados pela Entidade)
 */

public record UsuarioRequest(@NotBlank(message = "Nome do usuário é obrigatório!")
                             @Pattern(
                                     regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]+$",
                                     message = "O nome só pode conter letras, espaços, apóstrofos e hífens!")
                             String nome,

                             @Email(message = "o email deve conter o formato de um email. Exemplo:(nome@email.com)")
                             @NotBlank(message = "Campo email é obrigatório!")
                             String email,

                             @NotBlank(message = "Senha é obrigatória!")
                             @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$", message = "A senha deve possuir pelo menos uma letra maiúscula e um numero!")
                             @Size(min = 6, message = "A senha necessita ter no mínimo 6 caracteres, incluindo letras, ou números!")
                             String senha,

                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ("99)99999-9999"))
                             @NotBlank(message = "Campo telefone é obrigatório!")
                             @Size(min = 14,max = 14, message = "Padrão de telefone aceito: (DDD)00000-0000")
                             @Pattern(regexp = "\\(\\d{2}\\)\\d{5}-\\d{4}", message = "O formato deve ser formato Brasil (99)99999-9999")
                             String telefone) {
}

