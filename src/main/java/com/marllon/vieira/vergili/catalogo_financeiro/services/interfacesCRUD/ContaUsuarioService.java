package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioCreateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
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
    ContaUsuarioResponse criarConta(ContaUsuarioCreateRequest request, Long idUsuario);

    /**
     * Busca uma conta pelo ID
     * @param id ID da conta
     * @return  contendo a conta encontrada, ou retornar uma exception
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
     * @param nomeNovo com o nome atualizado
     * @return DTO com a conta atualizada
     * @throws ContaNaoEncontrada se a conta não for encontrada
     */
    ContaUsuarioResponse alterarNomeDeUmaConta(Long id, String nomeNovo) ;

    /**
     * Remove uma conta pelo ID
     * @param id ID da conta a ser removida
     * @throws ContaNaoEncontrada se a conta não for encontrada
     * @throws DataIntegrityViolationException se a conta possuir dependências
     */
    void deletarConta(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================
    /**
     * Verifica se uma conta é do tipo corrente, poupança ou investimento.
     *
     * @param contaId ID da conta
     * @return o tipo de conta correspondente
     */
    TiposContas verificarTipoConta(Long contaId);
    /**
     * Verificar se a conta tem saldo negativo.
     * @return true se a conta possuir saldo negativo
     */
    boolean seSaldoEstiverNegativo(BigDecimal saldo);
    /**
     * Verificar se a conta tem saldo positivo.
     * @return true se a conta possuir saldo positivo
     */
    boolean seSaldoEstiverPositivo(BigDecimal saldo);



    // ======================== MÈTODOS DE CÀLCULOS============================
    /**
     * Adiciona um valor ao saldo atual da conta do usuário.
     *
     * @param idConta a conta do usuário que terá o saldo atualizado.
     * @param valor o valor a ser adicionado ao saldo da conta.
     * @throws IllegalArgumentException se o valor for nulo ou negativo.
     */
    void adicionarSaldo(Long idConta, BigDecimal valor);

    /**
     * Subtrai um valor do saldo atual da conta do usuário.
     *
     * @param idConta a conta do usuário que terá o saldo reduzido.
     * @param valor o valor a ser subtraído do saldo da conta.
     * @throws IllegalArgumentException se o valor for nulo ou negativo.
     * @throws ArithmeticException se o valor for maior que o saldo atual da conta.Porém...
     * Só deve permitir saldo negativo em tipo de conta CORRENTE
     */
    void subtrairSaldo(Long idConta, BigDecimal valor);

    /**
     * Consulta o saldo atual da conta do usuário.
     *
     * @param contaId o identificador da conta a ser consultada.
     * @throws RuntimeException ou ContaNotFoundException (personalizada) se a conta não for encontrada.
     */
    BigDecimal consultarSaldo(Long contaId);



    // ======================== VALIDAÇÕES ========================



    /**
     * Verifica se já existe uma conta com os mesmos dados no banco de dados.
     * Pode considerar nome, tipo de conta, usuário associado e outros campos relevantes.
     *
     * @param  nome e tipoConta objeto com os dados da conta a ser verificada (exceto o ID).
     * @return true se já existir uma conta com os mesmos dados.
     */
    boolean jaExisteUmaContaIgual(String nome, TiposContas tipoConta);
}


