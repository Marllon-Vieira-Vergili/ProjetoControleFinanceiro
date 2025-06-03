package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Pagamentos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagamentosCreateRequest(@NotNull(message = "O campo do valor não pode ser vazio!")
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
                                      SubTipoCategoria subTipoCategoria) {
}
