package com.marllon.vieira.vergili.catalogo_financeiro.services.interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoricoTransacaoService {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================


    HistoricoTransacaoResponse criarTransacao(HistoricoTransacaoRequest request);


    Optional<HistoricoTransacaoResponse> encontrarTransacaoPorid(Long id);


    List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data);


    Page<HistoricoTransacaoResponse> encontrarTodasTransacoes(Pageable pageable);

    void deletarHistoricoTransacao(Long id);


    // ======================== OPERAÇÕES ESPECÍFICAS ========================


    void consultarValorTransacao(Long contaId);

    // ======================== MÈTODOS DE ASSOCIAÇÂO E DESASSOCIAÇÂO DESSA ENTIDADE ========================


    /**
     * Estes métodos são associações desta entidade HistóricoTransacao com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * O retorno dos dados vai variar conforme o nome da outra entidade
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */
    void associarTransacaoComPagamento(Pagamentos pagamento);

    void associarTransacaoComConta(ContaUsuario conta);

    void associarTransacaoComUsuario(Usuario usuario);

    void associarTransacaoComCategoria(CategoriaFinanceira categoria);


    /**
     * Estes métodos são desassociações desta entidade HistoricoTransacao com todas as outras..
     * O parâmetro de entrada de dados vai variar conforme o nome da outra entidade
     * Não irá ter retorno dos dados, pois só será feito para execução
     * Jogar Exceções personalizadas, se houver erros de não encontrados, ou já existe.. etc;
     */

    //Desassociar Historico de transações com Pagamentos relacionados(Many to Many)

    //Verificar se a transação possui um pagamento relacionado
    void desassociarTransacaoDePagamento(Pagamentos pagamento);


    //Desassociar Historico de transações com Conta relacionada(Many to One)
    void desassociarTransacaoDeConta(ContaUsuario conta);

    //Desassociar Historico de transações com Usuário relacionado(Many to One)
    void desassociarTransacaoDeUsuario(Usuario usuario);

    //Desassociar Historico de transações com Categorias Relacioandas(Many to Many)
    void desassociarTransacaoDeCategoria(CategoriaFinanceira categoria);



    // ======================== VALIDAÇÕES ========================


    boolean validacaoExistePelaID(Long id);

    boolean JaExisteUmaTransacaoIgual(ContaUsuario contaUsuario);
}
