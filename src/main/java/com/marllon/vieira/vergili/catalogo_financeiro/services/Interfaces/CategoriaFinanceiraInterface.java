package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;

import java.util.List;


/**Esta interface é para criação dos métodos  relacionado somente as categoriasFinanceiras. Os métodos são
 * para encontrar, e verificar existência de categorias financeiras, pois essa entidade tem um Enum como atributo.
 *
 * Alguns métodos implementados, foram sugeridos pela IA, como os de verificar existencia
 *
 * A maioria dos métodos, retorna o DTO(Data Transfer Object) no valor
 *
 */

public interface CategoriaFinanceiraInterface {


    //Ler
    CategoriaFinanceiraResponse encontrarCategoriaPorId(Long id);
    List<CategoriaFinanceiraResponse> encontrarTodasCategorias();
    CategoriaFinanceiraResponse encontrarCategoriaPorTipo(CategoriaFinanceiraRequest categoria);

}



