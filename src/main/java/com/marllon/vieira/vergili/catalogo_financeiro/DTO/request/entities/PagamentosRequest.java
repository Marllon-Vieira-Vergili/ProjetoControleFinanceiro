package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public record PagamentosRequest(BigDecimal valor,
                                LocalDate data,
                                String descricao,
                                TiposCategorias categoria) {
}
