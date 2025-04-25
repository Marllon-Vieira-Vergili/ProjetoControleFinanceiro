package com.marllon.vieira.vergili.catalogo_financeiro.mapper;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import org.mapstruct.Mapper;


/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */


@Mapper(componentModel = "spring")
public interface ContaUsuarioMapper {
    ContaUsuarioResponse retornoValoresContaUsuario(ContaUsuario contaUsuario);
}
