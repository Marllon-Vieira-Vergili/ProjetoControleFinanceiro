package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
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

    boolean seSaldoEstiverNegativo(Long contaId);
    boolean seSaldoEstiverPositivo(Long contaId);



    // ======================== MÈTODOS DE ASSOCIAÇÂO E DESASSOCIAÇÂO DESSA ENTIDADE ========================

    /**
     * Estes métodos são associações desta entidade ContaUsuario com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * O retorno dos dados vai variar conforme o nome da outra entidade
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */
    void associarTipoConta(Long contaId, TiposContas tiposConta);

    void associarContaComCategoria(Long contaId, Long categoriaId);

    void associarContaComPagamentos(Long contaId, Long pagamentoId);

    void associarContaComTransacoes(Long contaId, Long transacaoId);

    void associarContaComUsuario(Long contaId, Long usuarioId);


    /**
     * Estes métodos são desassociações desta entidade ContaUsuario com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * Não terá retorno de dados, só execução
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */
    void desassociarContaDeCategorias(Long contaId,  Long categoriaId);

    void desassociarContaDePagamento(Long contaId,  Long categoriaId);

    void desassociarContaDeHistoricoDeTransacao(Long contaId,  Long historicoId);

    void desassociarContaDeUsuario(Long contaId,  Long usuarioId);



    // ======================== MÈTODOS DE CÀLCULOS============================
    /**
     * Realizar calculos de acordo com o saldo da conta, para adicionar, ou subtrair saldo
     * @param conta conta do usuário que será calculada, e um valor do tipo BigDecimal
     * não retornar nada, só realizar o cálculo
     */

    void adicionarSaldo(ContaUsuario conta, BigDecimal valor);

    void subtrairSaldo(ContaUsuario conta, BigDecimal valor);

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


