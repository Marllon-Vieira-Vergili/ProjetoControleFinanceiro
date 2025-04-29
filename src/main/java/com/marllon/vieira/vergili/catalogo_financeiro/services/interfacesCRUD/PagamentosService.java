package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PagamentosService  {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria uma nova receita(recebimento).
     *
     * @param request Dados para criação.
     * @return Receita criada.
     */
    PagamentosResponse criarRecebimento(PagamentosRequest request);

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
     * @return PagamentosResponse contendo o pagamento encontrado
     */
    PagamentosResponse encontrarPagamentoPorid(Long id);

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
     * @param categoriaId do id da categoria pretendida
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPelaCategoriaCriada(Long categoriaId);

    /**
     * Busca pagamentos por usuário.
     *
     * @param usuarioId a categoria pretendida.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPorUsuario(Long usuarioId);

    /**
     * Busca pagamentos por tipo (RECEITA ou DESPESA).
     *
     * @param tipoCategoria Tipo da categoria.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentosPorTipo(TiposCategorias tipoCategoria); // ou usar enum

    /**
     * Atualiza um pagamento.
     *
     * @param request Dados atualizados, id para passar o id do pagamento .
     * @return Pagamento atualizado.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    PagamentosResponse atualizarPagamento(Long id, PagamentosRequest request);

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
     *
     * @return true se for despesa.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForDespesa();

    /**
     * Verifica se o pagamento é uma receita.
     *
     *
     * @return true se for receita.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    boolean sePagamentoForReceita();


    // ======================== MÉTODOS DE VALIDAÇÕES ========================

    /**
     * Verifica se já existe um pagamento com os mesmos dados.
     *
     * @param pagamento Pagamento a verificar.
     * @return true se já existir.
     * @throws JaExisteException caso já exista.
     */
    boolean jaExisteUmPagamentoIgual(PagamentosRequest pagamento);
}
