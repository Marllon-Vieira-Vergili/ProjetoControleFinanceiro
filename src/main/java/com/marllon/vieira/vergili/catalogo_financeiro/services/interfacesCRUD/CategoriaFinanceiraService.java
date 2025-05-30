package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CategoriaFinanceiraService {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria uma nova categoria financeira no sistema e ja associar.
     *
     * @param request DTO contendo os dados necessários para a criação.
     * @return Retorna um DTO com os dados da categoria criada.
     * @throws DadosInvalidosException se os dados digitados forem inválidos.
     */
    CategoriaFinanceiraResponse criarCategoriaFinanceira(CategoriaFinanceiraRequest request);

    /**
     * Busca uma categoria financeira pelo seu ID.
     *
     * @param id O ID da categoria a ser buscada.
     * @return Retorna a categoria encontrada ou vazio caso não encontre.
     * @throws CategoriaNaoEncontrada se os dados digitados forem inválidos.
     */
    Optional<CategoriaFinanceiraResponse> encontrarCategoriaPorId(Long id);

    /**
     * Encontra as categorias financeiras de um determinado subtipo.
     *
     * @param subTipo O subtipo da categoria (ex: Despesa, Receita).
     * @return Retorna uma lista de categorias financeiras com o subtipo informado.
     * @throws CategoriaNaoEncontrada se não for encontrado categorias pela SubCategoria
     */
    CategoriaFinanceiraResponse encontrarCategoriaCriadaPeloSubtipo(SubTipoCategoria subTipo);

    /**
     * Lista todas as categorias financeiras, com suporte a paginação.
     *
     * @param pageable Objeto de paginação (tamanho da página, número da página, ordenação).
     * @return Retorna uma página com as categorias financeiras.
     */
    Page<CategoriaFinanceiraResponse> encontrarTodasCategorias(Pageable pageable);

    /**
     * Atualiza os dados de uma categoria financeira existente.
     *
     * @param idCategoria O ID da categoria a ser atualizada.
     * @return Retorna o DTO com os dados da categoria atualizada.
     * @throws DadosInvalidosException se os dados digitados forem inválidos.
     */
    CategoriaFinanceiraResponse atualizarUmaCategoriaCriada(Long idCategoria,
                                                            TiposCategorias tiposCategoria,
                                                            SubTipoCategoria subTipo);

    /**
     * Remove uma categoria financeira pelo seu ID.
     *
     * @param id O ID da categoria a ser removida.
     * @throws CategoriaNaoEncontrada se não encontrar a categoria pela id.
     */
    void deletarCategoria(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================




    // ======================== VALIDAÇÕES ========================



    /**
     * Verifica se um determinado tipo de categoria e subtipo são compatíveis entre si.
     *
     * Este método é útil para validar a relação entre categorias e subtipos em sistemas
     * que exigem consistência na organização dos dados.
     *
     * @return true se o tipo de categoria e subtipo forem compatíveis, false caso contrário.
     */
    boolean validarCompatibilidadeTipoESubtipo(TiposCategorias tipoCategoria,SubTipoCategoria subTipoCategoria);

    /**
     * Verifica se já existe uma categoria com os mesmos dados fornecidos (exceto o ID) no banco de dados.
     *
     * @param tiposCategoria Objeto com os dados da categoria a ser verificada.
     * @return Retorna true se já existir uma categoria igual, caso contrário false.
     */
    boolean jaExisteUmaCategoriaIgual(TiposCategorias tiposCategoria, SubTipoCategoria subTipoCategoria);
}
