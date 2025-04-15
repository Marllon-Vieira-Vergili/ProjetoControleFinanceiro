package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriasContas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<CategoriasContas,Long> {


    //Métodos customizados do repositório de categorias


    //Método para procurar a categoria de contas pelo tipo de Conta
    @Query(value = "SELECT c FROM CategoriasContas c WHERE c.tiposCategorias =: tipos_categorias")
    CategoriasContas encontrarPorTipoCategoria(@Param(value = "tipos_categorias") TiposCategorias tipoCategoria);

    /*
    //Método para procurar a categoria de contas pela descrição da categoria
    @Query(value = "SELECT c FROM CategoriasContas c WHERE c.descricao =: descricaoCategoria")
    CategoriasContas encontrarCategoriaPorTipo(@Param(value = "descricaoCategoria") TiposCategorias descricaoCategoria);

     */
}
