package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;

import java.math.BigDecimal;
import java.time.LocalDate;

//Record que irá retornar os dados do Histórico de transações, quando ele inserir todos os dados
public record HistoricoTransacoesResponse(Long id, BigDecimal valor, LocalDate data, String descricao,
                                          TiposCategorias categoria) {
}
