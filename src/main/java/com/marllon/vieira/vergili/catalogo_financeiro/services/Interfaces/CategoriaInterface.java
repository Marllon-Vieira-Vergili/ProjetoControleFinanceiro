package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.CategoriaRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;


import java.util.List;


public interface CategoriaInterface{


    //Interface para criação dos Métodos CRUDS SEPARADAMENTE, um por entidade

    //Criar (nova categoria de conta)

    CategoriaResponse criarCategoriaDeConta(CategoriaRequest categoriaConta);

    //Ler
    CategoriaResponse encontrarCategoriaPorId(Integer id);
    List<CategoriaResponse> encontrarTodasCategorias();
    CategoriaResponse encontrarCategoriaPorTipo(TiposCategorias categoria);

    //Atualizar
    CategoriaResponse atualizarCategoriaDeConta(Integer id, CategoriaRequest categoriaConta);

    //Remover
    CategoriaResponse removerCategoriaDeContaPorId(Integer id);
    CategoriaResponse removerCategoriaDeContaPorNome(String nome);
}

