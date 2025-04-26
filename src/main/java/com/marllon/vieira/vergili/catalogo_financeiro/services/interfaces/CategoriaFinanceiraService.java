package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
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
     * Cria uma nova categoria financeira no sistema.
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
     * @return Retorna um Optional com a categoria encontrada ou vazio caso não encontre.
     * @throws CategoriaNaoEncontrada se os dados digitados forem inválidos.
     */
    Optional<CategoriaFinanceiraResponse> encontrarCategoriaPorId(Long id);

    /**
     * Encontra as categorias financeiras de um determinado subtipo.
     *
     * @param subTipo O subtipo da categoria (ex: Despesa, Receita).
     * @return Retorna uma lista de categorias financeiras com o subtipo informado.
     * @throws CategoriaNaoEncontrada se não for encontrado a categoria pela SubCategoria
     */
    List<CategoriaFinanceiraResponse> encontrarCategoriaCriadaPeloSubTipo(SubTipoCategoria subTipo);

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

    // ======================== MÉTODOS DE ASSOCIAÇÃO E DESASSOCIAÇÃO ========================

    /**
     * Associa uma categoria financeira a uma conta de usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param contaId O ID da conta de usuário.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComConta(Long categoriaId, Long contaId);

    /**
     * Associa uma categoria financeira a um pagamento.
     *
     * @param categoriaId O ID da categoria.
     * @param pagamentoId O ID do pagamento.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId);

    /**
     * Associa uma categoria financeira a uma transação.
     *
     * @param categoriaId O ID da categoria.
     * @param transacaoId O ID da transação.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComTransacao(Long categoriaId, Long transacaoId);

    /**
     * Associa uma categoria financeira a um usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param usuarioId O ID do usuário.
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarCategoriaComUsuario(Long categoriaId, Long usuarioId);

    /**
     * Associa um subtipo de categoria (ex: "Alimentação") a uma categoria do tipo **DESPESA**.
     *
     * @param categoriaId O ID da categoria.
     * @param subTipo O subtipo de categoria (DESPESA).
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarSubTipoCategoriaComDespesa(Long categoriaId, SubTipoCategoria subTipo);

    /**
     * Associa um subtipo de categoria a uma categoria do tipo **RECEITA**.
     *
     * @param categoriaId O ID da categoria.
     * @param subTipo O subtipo de categoria (RECEITA).
     * @throws AssociationErrorException se a associação não der certo.
     */
    void associarSubTipoCategoriaComReceita(Long categoriaId, SubTipoCategoria subTipo);

    // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================

    /**
     * Desassocia uma categoria de uma conta de usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param contaId O ID da conta de usuário.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaAConta(Long categoriaId, Long contaId);

    /**
     * Desassocia uma categoria de um pagamento.
     *
     * @param categoriaId O ID da categoria.
     * @param pagamentoId O ID do pagamento.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId);

    /**
     * Desassocia uma categoria de uma transação.
     *
     * @param categoriaId O ID da categoria.
     * @param transacaoId O ID da transação.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId);

    /**
     * Desassocia uma categoria de um usuário.
     *
     * @param categoriaId O ID da categoria.
     * @param usuarioId O ID do usuário.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId);

    /**
     * Remove a associação de uma categoria com o tipo **DESPESA**.
     *
     * @param categoriaId O ID da categoria.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaCriadaComDespesa(Long categoriaId);

    /**
     * Remove a associação de uma categoria com o tipo **RECEITA**.
     *
     * @param categoriaId O ID da categoria.
     * @throws DesassociationErrorException se a desassociação não der certo.
     */
    void desassociarCategoriaCriadaComReceita(Long categoriaId);

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
