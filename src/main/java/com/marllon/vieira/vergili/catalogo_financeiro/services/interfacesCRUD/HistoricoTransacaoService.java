package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
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
     * @param tiposCategoria Tipo da transação (ex: "RECEITA" ou "DESPESA").
     * @return Lista de objetos {@link HistoricoTransacaoResponse} que correspondem ao tipo especificado.
     * @throws TiposCategoriasNaoEncontrado se o tipo fornecido não for válido.
     * @throws HistoricoTransacaoNaoEncontrado se não houver históricos para o tipo especificado.
     */
    List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(TiposCategorias tiposCategoria);

    /**
     * Retorna uma lista de históricos de transação associados a uma categoria específica.
     *
     * @param categoriaId ID da categoria financeira.
     * @return Lista de objetos {@link HistoricoTransacaoResponse} vinculados à categoria.
     * @throws CategoriaNaoEncontrada se a categoria com o ID fornecido não existir.
     * @throws HistoricoTransacaoNaoEncontrado se não houver nenhum histórico associado à categoria.
     */
    List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId);



    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Consulta o valor total das transações vinculadas a uma determinada conta.
     *
     * @param contaId identificador da conta.
     * @return somatório da transação.
     */
    BigDecimal consultarValorTotalTransacoes(Long contaId);



    // ======================== VALIDAÇÕES ========================


    /**
     * EvitarCódigo BOILERPLATE, INSTANCIANDO EM TODOS OS MÈTODOS de verificação
     *
     * @param id ID da do histórico de transação
     * @throws HistoricoTransacaoNaoEncontrado se não for encontrado o histórico pela id.
     */
   HistoricoTransacao getHistoricoTransacaoById(Long id);

    /**
     * Verifica se uma transação existe a partir do seu ID.
     *
     * @param id identificador da transação.
     * @return true se a transação existir, false caso contrário.
     */
    boolean transacaoExistePelaID(Long id);

    /**
     * Verifica se já existe uma transação idêntica vinculada a uma conta.
     *
     * @param historicoTransacao historico a ser verificado.
     * @return true se houver duplicidade, false caso contrário.
     */
    boolean jaExisteUmaTransacaoIgual(HistoricoTransacaoRequest historicoTransacao);
}
