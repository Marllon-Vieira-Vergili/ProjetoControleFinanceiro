package com.marllon.vieira.vergili.catalogo_financeiro.repository;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repositório responsável pelas operações de persistência da entidade {@link CategoriaFinanceira}.
 * Estende JpaRepository para fornecer métodos CRUD padrão, além de métodos customizados
 * para buscas por tipo e subtipo de categoria.
 */
public interface CategoriaFinanceiraRepository extends JpaRepository<CategoriaFinanceira, Long> {

    /**
     * Busca todas as categorias financeiras de acordo com o tipo (RECEITA, DESPESA, etc).
     *
     * @param tipoCategoria o tipo da categoria {@link TiposCategorias}
     * @return uma lista de {@link CategoriaFinanceira} que correspondem ao tipo informado
     */
    @Query("SELECT c FROM CategoriaFinanceira c WHERE c.tiposCategorias = :tipos_categorias")
    CategoriaFinanceira encontrarPorTipoCategoria(@Param("tipos_categorias") TiposCategorias tipoCategoria);


    /**
     * Busca todas as categorias financeiras de acordo com o subtipo.
     * Ideal para categorias mais específicas (ex: ALIMENTAÇÃO, INVESTIMENTO, etc.).
     *
     * @param subtipoCategoria o subtipo da categoria {@link SubTipoCategoria}
     * @return uma lista de {@link CategoriaFinanceira} que possuem o subtipo informado
     */
    @Query("SELECT c FROM CategoriaFinanceira c WHERE c.subTipo = :subtipo_categoria")
    CategoriaFinanceira encontrarCategoriaPeloSubTipo(@Param("subtipo_categoria") SubTipoCategoria subtipoCategoria);

    /**
     * Busca uma categoria financeira que corresponda exatamente ao tipo e subtipo informados.
     * Útil para evitar duplicidade e garantir unicidade da combinação.
     *
     * @param tipoCategoria o tipo principal da categoria {@link TiposCategorias}
     * @param subTipoCategoria o subtipo da categoria {@link SubTipoCategoria}
     * @return uma instância de {@link CategoriaFinanceira} correspondente, ou null se não for encontrada
     */
    @Query("SELECT c FROM CategoriaFinanceira c WHERE c.tiposCategorias = :tipoCategoria AND c.subTipo = :subTipoCategoria")
    CategoriaFinanceira encontrarPorTipoAndSubtipo(@Param("tipoCategoria") TiposCategorias tipoCategoria,
                                             @Param("subTipoCategoria") SubTipoCategoria subTipoCategoria);
}
