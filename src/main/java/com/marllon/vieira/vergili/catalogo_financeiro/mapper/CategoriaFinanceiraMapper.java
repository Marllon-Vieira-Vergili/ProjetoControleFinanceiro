package com.marllon.vieira.vergili.catalogo_financeiro.mapper;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapear para saída de dados em DTO, evitando códigos BOILERPLATE,
 * usando o framework MAPSTRUCT
 *
 */
@Mapper(componentModel = "spring")
public interface CategoriaFinanceiraMapper {

    /**
     * @param categoriaFinanceira para obter os dados da entidade
     * @return CategoriaFinanceiraResponse, para retornar os valores DTO ao usuário
     *
     */
    @Mapping(source = "tiposCategorias",target = "tipoCategoria") //por que no DTO ta com nome diferente
    CategoriaFinanceiraResponse retornarDadosCategoria
            (CategoriaFinanceira categoriaFinanceira);
}
