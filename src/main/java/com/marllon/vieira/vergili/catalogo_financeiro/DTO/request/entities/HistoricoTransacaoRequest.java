package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
Record para o usuário, ao enviar uma requisicao pela pela entidade de
Historico de transações,

Irá solicitar que ele coloque o valor que foi feito no pagamento para o histórico, a data que foi realizado a transacao,
e a o tipo de categoria que essa conta foi realizada(comida, conta de agua, luz, internet, etc...)
(Somente para envio de dados pela Entidade)
 */

public record HistoricoTransacaoRequest(BigDecimal valor, LocalDate data, String descricao, TiposCategorias categoria) {

}
