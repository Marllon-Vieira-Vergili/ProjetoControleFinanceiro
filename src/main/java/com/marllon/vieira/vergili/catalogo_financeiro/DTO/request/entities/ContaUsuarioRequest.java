package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;

import java.math.BigDecimal;


/**Record para o usuário, ao enviar uma requisicao pela pela entidade de Conta,
Irá solicitar que ele coloque o nome da conta, o saldo da conta e o tipo da conta.
(Somente para envio de dados pela Entidade)
 */
public record ContaUsuarioRequest(String nome, BigDecimal saldo, TiposContas tipoConta) {
}
