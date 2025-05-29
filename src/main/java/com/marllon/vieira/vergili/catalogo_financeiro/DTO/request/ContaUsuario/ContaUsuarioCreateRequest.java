package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Record para o usuário ao enviar uma requisição pela entidade de Conta.
 * O campo saldo foi removido para permitir a criação da conta sem um valor inicial.
 * O saldo será gerenciado posteriormente em operações específicas.
 */
public record ContaUsuarioCreateRequest(@NotBlank(message = "O campo do nome não pode ficar vazio!")
                                  @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
                                          message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
                                  String nome,

                                  @NotNull(message = "O campo TipoConta não pode ser nulo")
                                  @Enumerated(EnumType.STRING)
                                  TiposContas tipoConta) {
}


