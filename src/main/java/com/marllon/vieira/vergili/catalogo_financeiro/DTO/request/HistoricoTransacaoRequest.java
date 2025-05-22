package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
Record para o usuário, ao enviar uma requisicao pela pela entidade de
Historico de transações,

Irá solicitar que ele coloque o valor que foi feito no pagamento para o histórico, a data que foi realizado a transacao,
e a o tipo de categoria que essa conta foi realizada(comida, conta de agua, luz, internet, etc...)
(Somente para envio de dados pela Entidade)
 */

public record HistoricoTransacaoRequest(@NotNull(message = "O valor da transação deve possuir um valor!")
                                        @Min(value = 1, message = "O valor mínimo para o histórico da transação realizada, é de R$ 1,00")
                                        BigDecimal valor,
                                        @NotNull(message = "O campo de data Não pode ficar nulo! Necessita de um dado")
                                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                        @DateTimeFormat(pattern = "dd/MM/yyyy")
                                        LocalDate data,

                                        @NotBlank(message = "O campo de descrição está vazio!")
                                        @Size(min = 5, message = "Descrição aceita de no mínimo 5 caracteres")
                                        @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
                                                message = "Descrição contém caracteres inválidos!")
                                        String descricao,

                                        @NotNull(message = "O campo de categoria não pode ser null!")
                                        @Column(name = "categoria", nullable = false)
                                        @Enumerated(EnumType.STRING)
                                        TiposCategorias tipoCategoria,

                                        @NotNull(message = "O campo SubtTipo não pode ser nulo")
                                        @Enumerated(EnumType.STRING)
                                        SubTipoCategoria subTipoCategoria,

                                        @NotNull(message = "O campo id do pagamento criado não pode ser nulo")
                                        Long idPagamentoRequest,

                                        @NotNull(message = "O campo id da categoria não pode ser nulo")
                                        Long idCategoriaFinanceira,

                                        @NotNull(message = "O campo id do usuário criado não pode ser nulo")
                                        Long idUsuarioCriado,

                                        @NotNull(message = "O campo id da conta do usuário criada não pode ser nulo!")
                                        Long idContaUsuario
                                        ) {

}

