package com.marllon.vieira.vergili.catalogo_financeiro.mapper;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.mapstruct.Mapper;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE, usando o framework MAPSTRUCT
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponse retornarValoresUsuario(Usuario usuario);
}
