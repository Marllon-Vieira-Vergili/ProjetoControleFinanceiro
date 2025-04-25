package com.marllon.vieira.vergili.catalogo_financeiro.repository;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriaFinanceiraRepository extends JpaRepository<CategoriaFinanceira,Long> {


    //Métodos customizados do repositório de categorias


    //Método para procurar a categoria de contas pelo tipo de Conta
    @Query(value = "SELECT c FROM CategoriaFinanceira c WHERE c.tiposCategorias = :tipos_categorias")
    List<CategoriaFinanceira> encontrarPorTipoCategoria(@Param(value = "tipos_categorias") TiposCategorias tipoCategoria);


    //Método para procurar a categoria de contas pelo Subtipo da Conta
    @Query(value = "SELECT c FROM CategoriaFinanceira c WHERE c.subTipo = :subtipo_categoria")
    List<CategoriaFinanceira> encontrarPorSubtipoCategoria(@Param(value = "subtipo_categoria") TiposCategorias subtipoCategoria);


    //Método para procurar a categoria pelo tipo, e seu subtipo respectivamente
    @Query(value = "SELECT c FROM CategoriaFinanceira c WHERE c.tiposCategorias = :tipoCategoria AND c.subTipo = :subTipoCategoria")
    CategoriaFinanceira findByTipoAndSubtipo(@Param(value = "tipoCategoria") TiposCategorias tipoCategoria,
                                                       @Param(value = "subTipoCategoria") SubTipoCategoria subTipoCategoria);
}
