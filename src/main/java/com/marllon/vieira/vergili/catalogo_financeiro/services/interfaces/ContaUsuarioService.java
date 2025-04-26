package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContaUsuarioService {


    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria uma nova conta de usuário
     * @param request DTO com dados para criação da conta
     * @return DTO com a conta criada
     * @throws DadosInvalidosException se os dados da request forem inválidos
     */
    ContaUsuarioResponse criarConta(ContaUsuarioRequest request);

    /**
     * Busca uma conta pelo ID
     * @param id ID da conta
     * @return Optional contendo a conta encontrada, ou retornar uma exception
     * @throws ContaNaoEncontrada se não for encontrada a conta
     */
    Optional<ContaUsuarioResponse> encontrarContaPorId(Long id);

    /**
     * Listar todas as contas pelo nome encontrado, pois pode ter várias contas com o mesmo nome
     * @param nome para o usuário digitar o nome
     * @return List de contas, ou..
     * @throws ContaNaoEncontrada se não encontrar nenhuma conta com esse nome
     */
    List<ContaUsuarioResponse> encontrarContaPorNome(String nome);

    /**
     * Lista todas as contas com suporte a paginação.
     *
     * @param pageable objeto que contém as informações de paginação, como número da página, tamanho da página e ordenação.
     * @return uma página contendo as contas encontradas.
     * @throws ContaNaoEncontrada se não houver nenhuma conta cadastrada no sistema.
     */
    Page<ContaUsuarioResponse> encontrarTodas(Pageable pageable);

    /**
     * Atualiza uma conta existente
     * @param id ID da conta a ser atualizada
     * @param request DTO com dados atualizados
     * @return DTO com a conta atualizada
     * @throws EntityNotFoundException se a conta não for encontrada
     */
    ContaUsuarioResponse atualizarUmaConta(Long id, ContaUsuarioRequest request);

    /**
     * Remove uma conta pelo ID
     * @param id ID da conta a ser removida
     * @throws EntityNotFoundException se a conta não for encontrada
     * @throws DataIntegrityViolationException se a conta possuir dependências
     */
    void deletarConta(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================
    /**
     * Verifica se uma conta é do tipo corrente, poupança ou investimento.
     * @param contaId ID da conta
     * @return o tipo de conta (enum) correspondente
     */
    TiposContas verificarTipoConta(Long contaId);
    /**
     * Verificar se uma conta passada pelo ID tem saldo negativo.
     * @param contaId ID da conta
     * @return true se a conta possuir saldo negativo
     */
    boolean seSaldoEstiverNegativo(Long contaId);
    /**
     * Verificar se uma conta passada pelo ID tem saldo positivo.
     * @param contaId ID da conta
     * @return true se a conta possuir saldo negativo
     */
    boolean seSaldoEstiverPositivo(Long contaId);



    // ======================== MÈTODOS DE ASSOCIAÇÂO E DESASSOCIAÇÂO DESSA ENTIDADE ========================

    /**
     * Associa um tipo de conta (enum) à conta do usuário.
     *
     * @param contaId     identificador da conta.
     * @param tiposConta  tipo de conta a ser associado.
     * @throws AssociationErrorException se a conta não for encontrada ou se o tipo já estiver associado.
     */
    void associarTipoConta(Long contaId, TiposContas tiposConta);

    /**
     * Associa uma categoria financeira a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador da categoria financeira.
     * @throws AssociationErrorException se a conta ou categoria não forem encontradas,
     *                                   ou se a associação já existir.
     */
    void associarContaComCategoria(Long contaId, Long categoriaId);

    /**
     * Associa um pagamento a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param pagamentoId identificador do pagamento.
     * @throws AssociationErrorException se a conta ou pagamento não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComPagamentos(Long contaId, Long pagamentoId);

    /**
     * Associa uma transação a uma conta de usuário.
     *
     * @param contaId     identificador da conta.
     * @param transacaoId identificador da transação.
     * @throws AssociationErrorException se a conta ou transação não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComTransacoes(Long contaId, Long transacaoId);

    /**
     * Associa um usuário à conta.
     *
     * @param contaId   identificador da conta.
     * @param usuarioId identificador do usuário.
     * @throws AssociationErrorException se a conta ou usuário não forem encontrados,
     *                                   ou se a associação já existir.
     */
    void associarContaComUsuario(Long contaId, Long usuarioId);


    // ================= DESASSOCIAÇÕES =================

    /**
     * Remove a associação entre uma conta e uma categoria financeira.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador da categoria.
     * @throws DesassociationErrorException se a conta ou categoria não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeCategorias(Long contaId, Long categoriaId);

    /**
     * Remove a associação entre uma conta e um pagamento.
     *
     * @param contaId     identificador da conta.
     * @param categoriaId identificador do pagamento (verificar se o nome está correto).
     * @throws DesassociationErrorException se a conta ou pagamento não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDePagamento(Long contaId, Long categoriaId); // categoriaId deve ser pagamentoId?

    /**
     * Remove a associação entre uma conta e um histórico de transação.
     *
     * @param contaId    identificador da conta.
     * @param historicoId identificador do histórico.
     * @throws DesassociationErrorException se a conta ou transação não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId);

    /**
     * Remove a associação entre uma conta e um usuário.
     *
     * @param contaId   identificador da conta.
     * @param usuarioId identificador do usuário.
     * @throws DesassociationErrorException se a conta ou usuário não forem encontrados,
     *                                      ou se a associação não existir.
     */
    void desassociarContaDeUsuario(Long contaId, Long usuarioId);



    // ======================== MÈTODOS DE CÀLCULOS============================
    /**
     * Adiciona um valor ao saldo atual da conta do usuário.
     *
     * @param conta a conta do usuário que terá o saldo atualizado.
     * @param valor o valor a ser adicionado ao saldo da conta.
     * @throws IllegalArgumentException se o valor for nulo ou negativo.
     */
    void adicionarSaldo(ContaUsuario conta, BigDecimal valor);

    /**
     * Subtrai um valor do saldo atual da conta do usuário.
     *
     * @param conta a conta do usuário que terá o saldo reduzido.
     * @param valor o valor a ser subtraído do saldo da conta.
     * @throws IllegalArgumentException se o valor for nulo ou negativo.
     * @throws ArithmeticException se o valor for maior que o saldo atual da conta.Porém...
     * Só deve permitir saldo negativo em tipo de conta CORRENTE
     */
    void subtrairSaldo(ContaUsuario conta, BigDecimal valor);

    /**
     * Consulta o saldo atual da conta do usuário.
     *
     * @param contaId o identificador da conta a ser consultada.
     * @throws RuntimeException ou ContaNotFoundException (personalizada) se a conta não for encontrada.
     */
    void consultarSaldo(Long contaId);



    // ======================== VALIDAÇÕES ========================

    /**
     * Verifica se uma conta existe pelo ID
     * @param id ID da conta
     * @return true se a conta existir
     */
    boolean contaExistePelaID(Long id);



    /**
     * Verifica se já existe uma conta com os mesmos dados no banco de dados.
     * Pode considerar nome, tipo de conta, usuário associado e outros campos relevantes.
     *
     * @param contaUsuario objeto com os dados da conta a ser verificada (exceto o ID).
     * @return true se já existir uma conta com os mesmos dados.
     */
    boolean jaExisteUmaContaIgual(ContaUsuario contaUsuario);
}


