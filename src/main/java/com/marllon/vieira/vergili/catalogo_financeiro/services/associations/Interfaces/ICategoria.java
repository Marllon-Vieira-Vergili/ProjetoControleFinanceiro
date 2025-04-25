package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.CategoriaFinanceiraAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.CategoriaFinanceiraAssociationResponse;

import java.util.List;

/**
 * Cria uma nova categoria financeira e realiza as associações necessárias com conta, usuário e outros dados relacionados.
 *
 * CategoriaDTO contendo os dados da nova categoria
 * @return Categoria com suas associações prontas para exibição
 */


public interface ICategoria {

    //Criar
    CategoriaFinanceiraAssociationResponse criarEAssociarCategoria(CategoriaFinanceiraAssociationRequest novaCategoria);

    //Ler
    CategoriaFinanceiraAssociationResponse encontrarCategoriaAssociadaPorId(Long id);
    List<CategoriaFinanceiraAssociationResponse> buscarCategoriaPorTipoESubCategoria(CategoriaFinanceiraRequest categoriaESubCategoria);

    //Atualizar
    CategoriaFinanceiraAssociationResponse alterarDadosCategoriaPelaID(Long id, CategoriaFinanceiraRequest categoriaAtualizada);


    //Deletar
    void removerCategoriaAssociadaPelaId(Long id);

}
