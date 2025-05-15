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
    CategoriaFinanceiraResponse criarCategoriaFinanceira(CategoriaFinanceiraRequest request
            ,Long pagamentoId,Long historicoTransacaoId, Long contaUsuarioId, Long usuarioId);

    /**
     * Busca uma categoria financeira pelo seu ID.
     *
     * @param id O ID da categoria a ser buscada.
     * @return Retorna a categoria encontrada ou vazio caso não encontre.
     * @throws CategoriaNaoEncontrada se os dados digitados forem inválidos.
     */
    CategoriaFinanceiraResponse encontrarCategoriaPorId(Long id);

    /**
     * Encontra as categorias financeiras de um determinado subtipo.
     *
     * @param subTipo O subtipo da categoria (ex: Despesa, Receita).
     * @return Retorna uma lista de categorias financeiras com o subtipo informado.
     * @throws CategoriaNaoEncontrada se não for encontrado categorias pela SubCategoria
     */
    List<CategoriaFinanceiraResponse> encontrarCategoriasCriadaPeloSubTipo
    (SubTipoCategoria subTipo);

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
     * @param novosDados DTO com os novos dados da categoria.
     * @return Retorna o DTO com os dados da categoria atualizada.
     * @throws DadosInvalidosException se os dados digitados forem inválidos.
     */
    CategoriaFinanceiraResponse atualizarUmaCategoriaCriada(Long idCategoria, CategoriaFinanceiraRequest novosDados);

    /**
     * Remove uma categoria financeira pelo seu ID.
     *
     * @param id O ID da categoria a ser removida.
     * @throws CategoriaNaoEncontrada se não encontrar a categoria pela id.
     */
    void deletarCategoria(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Verifica se a categoria é do tipo **DESPESA**.
     *
     * @return Retorna true se a categoria for do tipo despesa, caso contrário false.
     */
    boolean seCategoriaForDespesa();

    /**
     * Verifica se a categoria é do tipo **RECEITA**.
     *
     * @return Retorna true se a categoria for do tipo receita, caso contrário false.
     */
    boolean seCategoriaForReceita();


    // ======================== VALIDAÇÕES ========================

    /**
     * Verifica se o tipo de categoria fornecido existe no sistema.
     *
     * @param tipoCategoria O tipo de categoria a ser validado.
     * @return Retorna true se o tipo de categoria existir, caso contrário false.
     */
    boolean tipoCategoriaExiste(TiposCategorias tipoCategoria);



    /**
     * Verifica se já existe uma categoria com os mesmos dados fornecidos (exceto o ID) no banco de dados.
     *
     * @param dadosCategoria Objeto com os dados da categoria a ser verificada.
     * @return Retorna true se já existir uma categoria igual, caso contrário false.
     */
    boolean jaExisteUmaCategoriaIgual(CategoriaFinanceira dadosCategoria);
}
