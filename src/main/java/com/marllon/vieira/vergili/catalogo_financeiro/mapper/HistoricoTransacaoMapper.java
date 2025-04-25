package com.marllon.vieira.vergili.catalogo_financeiro.mapper;


import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import org.mapstruct.Mapper;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */
@Mapper(componentModel = "spring")
public interface HistoricoTransacaoMapper {
    HistoricoTransacaoResponse retornoValoresHistoricoTransacao(HistoricoTransacao historicoTransacao);
}
