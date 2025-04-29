package com.marllon.vieira.vergili.catalogo_financeiro.mapper;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import org.mapstruct.Mapper;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */
@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    /**
     * @param pagamento para obter os dados da entidade
     * @return PagamentosResponse, para retornar os valores DTO ao usuário
     *
     */
    PagamentosResponse retornarDadosPagamento(Pagamentos pagamento);
}
