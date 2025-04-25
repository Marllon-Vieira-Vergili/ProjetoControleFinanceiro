package com.marllon.vieira.vergili.catalogo_financeiro.mapper;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import org.mapstruct.Mapper;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */
@Mapper(componentModel = "spring")
public interface PagamentoMapper {
    PagamentosResponse retornoValoresPagamento(Pagamentos pagamento);
}
