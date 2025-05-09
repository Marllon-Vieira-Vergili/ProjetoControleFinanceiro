package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
Record para o usuário, ao enviar uma requisicao pela pela entidade de
Pagamentos.

Irá solicitar que ele coloque o valor que foi feito no pagamento , a data que foi realizado o pagamento,
e a o tipo de categoria que esse pagamento foi feito.(comida, conta de agua, luz, internet, etc...)
(Somente para envio de dados pela Entidade)
 */
public record PagamentosRequest(
                                @NotNull(message = "O campo do valor não pode ser vazio!")
                                @Min(value = 1, message = "O valor mínimo para inserção de um valor, é de R$ 1,00")
                                BigDecimal valor,

                                @NotNull(message = "O campo da data não pode ficar vazio!")
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                @DateTimeFormat(pattern = "dd/MM/yyyy")
                                LocalDate data,

                                @Pattern(regexp = "^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ0-9 ,.]+$",
                                        message = "Descrição contém caracteres inválidos!")
                                @NotBlank(message = "O campo descrição não pode ficar vazio!")
                                String descricao,

                                @NotNull(message = "O campo de categoria não pode ser null!")
                                @Enumerated(EnumType.STRING)
                                TiposCategorias tipoCategoria,

                                @NotNull(message = "O campo SubtTipo não pode ser nulos")
                                @Enumerated(EnumType.STRING)
                                SubTipoCategoria subTipoCategoria,

                                @NotNull(message = "O campo id da categoria não pode ser nulo")
                                Long idCategoriaFinanceira,

                                @NotNull(message = "O campo id do usuário criado não pode ser nulo")
                                Long idUsuarioCriado,

                                @NotNull(message = "O campo id da conta do usuário criada não pode ser nulo!")
                                Long idContaUsuario){
}

