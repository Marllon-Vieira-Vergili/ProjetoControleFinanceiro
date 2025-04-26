package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.HistoricoTransacaoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface para gerenciamento das operações relacionadas ao Histórico de Transações.
 * Contém métodos CRUD, operações específicas e de associação/desassociação com outras entidades.
 */
public interface HistoricoTransacaoService {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria uma nova transação no histórico.
     *
     * @param request dados da transação a ser criada.
     * @return representação da transação criada.
     * @throws DadosInvalidosException se não for possível criar a transação com os dados fornecidos.
     * @throws JaExisteException se já existir uma transação idêntica no banco de dados.
     */
    HistoricoTransacaoResponse criarTransacao(HistoricoTransacaoRequest request);

    /**
     * Busca uma transação pelo ID.
     *
     * @param id identificador da transação.
     * @return transação encontrada, se existir.
     * @throws HistoricoTransacaoNaoEncontrado se não for encontrada a transação.
     */
    Optional<HistoricoTransacaoResponse> encontrarTransacaoPorid(Long id);

    /**
     * Retorna uma lista de transações realizadas em uma data específica.
     *
     * @param data data desejada.
     * @return lista de transações encontradas.
     * @throws HistoricoTransacaoNaoEncontrado se não for encontrada nenhuma transação na data especificada.
     * @throws DateTimeException se o usuário retornar uma data incompatível
     */
    List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data);

    /**
     * Atualiza os dados de uma transação existente, (SE necessário..), casos muitos específicos
     * @param request dados atualizados da transação.
     * @return representação da transação atualizada.
     * @throws DadosInvalidosException se não for possível atualizar a transação com os dados fornecidos.
     * @throws JaExisteException se já existir uma transação idêntica no banco de dados.
     */
    HistoricoTransacaoResponse atualizarHistoricoTransacao(HistoricoTransacaoRequest request);

    /**
     * Lista todas as transações paginadas.
     * @param pageable objeto de paginação.
     * @return página de transações.
     */
    Page<HistoricoTransacaoResponse> encontrarTodasTransacoes(Pageable pageable);

    /**
     * Retorna uma lista de históricos de transação associados a um usuário específico.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de objetos {@link HistoricoTransacaoResponse} pertencentes ao usuário.
     * @throws UsuarioNaoEncontrado se o usuário com o ID fornecido não existir.
     * @throws HistoricoTransacaoNaoEncontrado se o usuário não possuir históricos de transação.
     */
    List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorUsuario(Long usuarioId);

    /**
     * Retorna uma lista de históricos de transação filtrados por tipo (ex: RECEITA ou DESPESA).
     *
     * @param tipo Tipo da transação (ex: "RECEITA" ou "DESPESA").
     * @return Lista de objetos {@link HistoricoTransacaoResponse} que correspondem ao tipo especificado.
     * @throws TiposCategoriasNaoEncontrado se o tipo fornecido não for válido.
     * @throws HistoricoTransacaoNaoEncontrado se não houver históricos para o tipo especificado.
     */
    List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(String tipo);

    /**
     * Retorna uma lista de históricos de transação associados a uma categoria específica.
     *
     * @param categoriaId ID da categoria financeira.
     * @return Lista de objetos {@link HistoricoTransacaoResponse} vinculados à categoria.
     * @throws CategoriaNaoEncontrada se a categoria com o ID fornecido não existir.
     * @throws HistoricoTransacaoNaoEncontrado se não houver nenhum histórico associado à categoria.
     */
    List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId);


    /**
     * Remove uma transação do histórico.
     * @param id identificador da transação a ser removida.
     * @throws HistoricoTransacaoNaoEncontrado se não for encontrado o histórico de atualização
     */
    void deletarHistoricoTransacao(Long id);


    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Consulta o valor total das transações vinculadas a uma determinada conta.
     *
     * @param contaId identificador da conta.
     * @return somatório da transação.
     */
    BigDecimal consultarValorTransacao(Long contaId);


    // ======================== MÉTODOS DE ASSOCIAÇÃO ========================

    /**
     * Associa um pagamento a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param pagamentoId identificador do pagamento.
     * @throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComPagamento(Long transacaoId, Long pagamentoId);

    /**
     * Associa uma conta a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param contaId identificador da conta.
     * @throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComConta(Long transacaoId, Long contaId);

    /**
     * Associa um usuário a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário.
     *@throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComUsuario(Long transacaoId, Long usuarioId);

    /**
     * Associa uma categoria financeira a uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param categoriaId identificador da categoria financeira.
     *@throws AssociationErrorException se a associação não for bem-sucedida.
     */
    void associarTransacaoComCategoria(Long transacaoId, Long categoriaId);


    // ======================== MÉTODOS DE DESASSOCIAÇÃO ========================

    /**
     * Desassocia um pagamento de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário (caso necessário para validação).
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDePagamento(Long transacaoId, Long usuarioId);

    /**
     * Desassocia uma conta de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param contaId identificador da conta.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeConta(Long transacaoId, Long contaId);

    /**
     * Desassocia um usuário de uma transação.
     * @param transacaoId identificador da transação.
     * @param usuarioId identificador do usuário.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeUsuario(Long transacaoId, Long usuarioId);

    /**
     * Desassocia uma categoria financeira de uma transação.
     *
     * @param transacaoId identificador da transação.
     * @param categoriaId identificador da categoria.
     *@throws DesassociationErrorException se a desassociação não for bem-sucedida.
     */
    void desassociarTransacaoDeCategoria(Long transacaoId, Long categoriaId);


    // ======================== VALIDAÇÕES ========================

    /**
     * Verifica se uma transação existe a partir do seu ID.
     *
     * @param id identificador da transação.
     * @return true se a transação existir, false caso contrário.
     */
    boolean validacaoExistePelaID(Long id);

    /**
     * Verifica se já existe uma transação idêntica vinculada a uma conta.
     *
     * @param contaUsuario conta a ser verificada.
     * @return true se houver duplicidade, false caso contrário.
     */
    boolean JaExisteUmaTransacaoIgual(ContaUsuario contaUsuario);
}
