package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Pagamentos.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PagamentosService  {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    //Método para criar uma transação financeira e um histórico junto
    Pagamentos criarTransacao(BigDecimal valor, LocalDate data, String descricao,
                              TiposCategorias tiposCategoria, SubTipoCategoria subTipo);

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
    Optional<PagamentosResponse> encontrarPagamentoOuRecebimentoPorid(Long id);

    /**
     * Busca pagamentos por data.
     *
     * @param data Data de criação.
     * @return Lista de pagamentos.
     */
    List<PagamentosResponse> encontrarPagamentoOuRecebimentoPorData(LocalDate data);

    /**
     * Busca pagamentos por categoria.
     *
     * @param categoriaId do id da categoria pretendida
     * @return Lista de pagamentos.
     */
    //List<PagamentosResponse> encontrarPagamentosPelaCategoriaCriada(Long categoriaId);

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
    List<PagamentosResponse> encontrarPagamentoOuRecebimentoPorTipo(TiposCategorias tipoCategoria);


    /**
     * Atualiza um pagamento.
     *
     *
     * @return Pagamento atualizado.
     * @throws PagamentoNaoEncontrado caso o pagamento não exista.
     */
    PagamentosResponse atualizarPagamentoOuRecebimento(Long id, BigDecimal valor, LocalDate data, String descricao,
                                                       TiposCategorias tiposCategoria, SubTipoCategoria subTipo);

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



    // ======================== MÉTODOS DE VALIDAÇÕES ========================

    /**
     * Verifica se já existe um pagamento com os mesmos dados.
     *
     * @return true se já existir.
     * @throws JaExisteException caso já exista.
     */
    boolean jaExisteUmPagamentoIgual(BigDecimal valor,LocalDate data,
                                     String descricao,TiposCategorias tipoCategoria,
                                     SubTipoCategoria subTipoCategoria);

    /**
     *
     * @return true se a data informada pelo usuário está em um prazo do dia atual
     */
    boolean dataEstaCorreta(LocalDate data);

    /**
     *
     * @return true se valor estiver correto sendo Receita ou Pagamento(Despesa)
     */
    boolean valorEstaCorreto(BigDecimal valor);
}
