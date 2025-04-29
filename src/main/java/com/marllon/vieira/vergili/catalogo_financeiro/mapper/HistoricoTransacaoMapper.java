package com.marllon.vieira.vergili.catalogo_financeiro.mapper;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import org.mapstruct.Mapper;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */
@Mapper(componentModel = "spring")
public interface HistoricoTransacaoMapper {

    /**
     * @param historicoTransacao para obter os dados da entidade
     * @return HistoricoTransacaoResponse, para retornar os valores DTO ao usuário
     *
     */
    HistoricoTransacaoResponse retornarHistoricoTransacao(HistoricoTransacao historicoTransacao);
}
