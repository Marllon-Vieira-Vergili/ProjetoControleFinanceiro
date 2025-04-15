package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

//Record que irá retornar os retorno dos pagamentos, quando ele inserir todos os dados
public record PagamentosResponse(Long id, BigDecimal valor, LocalDate data, String descricao, String categoria) {
}
