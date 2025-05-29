package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;


/**Record para o usuário, ao enviar uma requisicao pela pela entidade de Conta,
Irá solicitar que ele coloque o nome da conta, o saldo da conta e o tipo da conta.
(Somente para envio de dados pela Entidade)
 */
public record ContaUsuarioRequest(@NotBlank(message = "O campo do nome não pode ficar vazio!")
                                  @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]+$",
                                          message = "Números e caracteres especiais como (@ ! # $ % & *) não são aceitos!")
                                  String nome,

                                  @NotNull(message = "O campo do saldo na conta não pode ser nulo!")
                                   BigDecimal saldo,

                                  @NotNull(message = "O campo TipoConta não pode ser nulo")
                                  @Enumerated(EnumType.STRING)
                                  TiposContas tipoConta) {
}


