package com.marllon.vieira.vergili.catalogo_financeiro.repository;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaFinanceiraRepository extends JpaRepository<CategoriaFinanceira,Long> {


    //Métodos customizados do repositório de categorias


    //Método para procurar a categoria de contas pelo tipo de Conta
    @Query(value = "SELECT c FROM CategoriaFinanceira c WHERE c.tiposCategorias =: tipos_categorias")
    CategoriaFinanceira encontrarPorTipoCategoria(@Param(value = "tipos_categorias") TiposCategorias tipoCategoria);


}
