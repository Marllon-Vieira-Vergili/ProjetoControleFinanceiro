package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PagamentosService  {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria uma nova receita.
     *
     * @param request Dados para criação.
     * @return Receita criada.
     */
    PagamentosResponse criarReceita(PagamentosRequest request);

    /**
     * Cria um novo pagamento (receita ou despesa).
     *
     * @param request Dados para criação.
     * @return Pagamento criado.
     * @throws SubTipoNaoEncontrado caso o subtipo não esteja vinculado corretamente à categoria.
     */
    PagamentosResponse criarPagamento(PagamentosRequest request);

    /**
     * Busca um pagamento pelo ID.
     *
     * @param id ID do pagamento.
     * @return Optional contendo o pagamento encontrado.
     */
    Optional<PagamentosResponse> encontrarPagamentoPorid(Long id);

    /**
     * Busca pagamentos por data.
     *
     * @param data Data de criação.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentoPorData(LocalDate data);

    /**
     * Busca pagamentos por categoria.
     *
     * @param categoriaId ID da categoria.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPorCategoria(Long categoriaId);

    /**
     * Busca pagamentos por usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPorUsuario(Long usuarioId);

    /**
     * Busca pagamentos por tipo (RECEITA ou DESPESA).
     *
     * @param tipo Tipo do pagamento.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPorTipo(String tipo); // ou usar enum

    /**
     * Atualiza um pagamento.
     *
     * @param request Dados atualizados.
     * @return Pagamento atualizado.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    PagamentosResponse atualizarPagamento(PagamentosRequest request);

    /**
     * Busca todos os pagamentos com paginação.
     *
     * @param pageable Objeto de paginação.
     * @return Página de pagamentos.
     */
    Page<PagamentosResponse> encontrarTodosPagamentos(Pageable pageable);

    /**
     * Deleta um pagamento pelo ID.
     *
     * @param id ID do pagamento.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    void deletarPagamento(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Consulta o valor total dos pagamentos de uma conta.
     *
     * @param contaId ID da conta.
     * @return Valor total dos pagamentos.
     */
    BigDecimal consultarValorPagamento(Long contaId);

    /**
     * Verifica se o pagamento é uma despesa.
     *
     * @param pagamentoId ID do pagamento.
     * @return true se for despesa.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForDespesa(Long pagamentoId);

    /**
     * Verifica se o pagamento é uma receita.
     *
     * @param pagamentoId ID do pagamento.
     * @return true se for receita.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForReceita(Long pagamentoId);

    // ======================== MÉTODOS DE ASSOCIAÇÃO E DESASSOCIAÇÃO ========================

    /**
     * Associa um pagamento a um usuário.
     *
     * @param pagamentoId ID do pagamento.
     * @param usuarioId ID do usuário.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoComUsuario(Long pagamentoId, Long usuarioId);

    /**
     * Associa um pagamento a uma transação.
     *
     * @param pagamentoId ID do pagamento.
     * @param transacaoId ID da transação.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoATransacao(Long pagamentoId, Long transacaoId);

    /**
     * Associa um pagamento a uma conta.
     *
     * @param pagamentoId ID do pagamento.
     * @param contaId ID da conta.
     * @throws AssociationErrorException em caso de falha na associação.
     */
    void associarPagamentoComConta(Long pagamentoId, Long contaId);

    /**
     * Associa um pagamento a uma categoria.
     *
     * @param pagamentoId ID do pagamento.
     * @param categoriaId ID da categoria.
     * @throws AssociationErrorException em caso de falha na associação.
     * @throws SubTipoNaoEncontrado caso o subtipo não combine com a categoria.
     */
    void associarPagamentoComCategoria(Long pagamentoId, Long categoriaId);

    /**
     * Desassocia um pagamento de um usuário.
     *
     * @param pagamentoId ID do pagamento.
     * @param usuarioId ID do usuário.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoUsuario(Long pagamentoId, Long usuarioId);

    /**
     * Desassocia um pagamento de uma transação.
     *
     * @param pagamentoId ID do pagamento.
     * @param transacaoId ID da transação.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoDeTransacao(Long pagamentoId, Long transacaoId);

    /**
     * Desassocia um pagamento de uma conta.
     *
     * @param pagamentoId ID do pagamento.
     * @param contaId ID da conta.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoConta(Long pagamentoId, Long contaId);

    /**
     * Desassocia um pagamento de uma categoria.
     *
     * @param pagamentoId ID do pagamento.
     * @param categoriaId ID da categoria.
     * @throws DesassociationErrorException em caso de falha.
     */
    void desassociarPagamentoCategoria(Long pagamentoId, Long categoriaId);

    // ======================== MÉTODOS DE VALIDAÇÕES ========================

    /**
     * Verifica se o pagamento existe.
     *
     * @param id ID do pagamento.
     * @return true se existir.
     */
    boolean pagamentoExistePelaId(Long id);

    /**
     * Verifica se já existe um pagamento com os mesmos dados.
     *
     * @param pagamento Pagamento a verificar.
     * @return true se já existir.
     * @throws JaExisteException caso já exista.
     */
    boolean JaExisteUmPagamentoIgual(PagamentosRequest pagamento);
}
