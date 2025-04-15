package com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities;


import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;

//Record que irá retornar os dados da categoria ao usuário, quando ele inserir todos os dados
public record CategoriaResponse(Long id, TiposCategorias tipoCategoria) {
}
