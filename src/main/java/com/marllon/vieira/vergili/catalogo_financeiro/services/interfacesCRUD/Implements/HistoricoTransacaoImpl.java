package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.HistoricoTransacaoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.HistoricoTransacaoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.HistoricoTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias.RECEITA;

@Service
public class HistoricoTransacaoImpl implements HistoricoTransacaoService {


    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistoricoTransacaoMapper historicoTransacaoMapper;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;




    @Override
    public Optional<HistoricoTransacaoResponse> encontrarTransacaoPorid(Long id) {

        HistoricoTransacao historicoEncontrado = getHistoricoTransacaoById(id);
        return Optional.ofNullable(historicoTransacaoMapper.retornarHistoricoTransacao(historicoEncontrado));
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data) {

        List<HistoricoTransacao> historicosEncontrados = historicoTransacaoRepository.encontrarTransacoesPelaData(data);

        if(historicosEncontrados.isEmpty()){
            throw new HistoricoTransacaoNaoEncontrado("Não foi encontrado nenhum histórico de transação com a data informada");
        }

        return historicosEncontrados.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }



    @Override
    public Page<HistoricoTransacaoResponse> encontrarTodasTransacoes(Pageable pageable) {

        Page<HistoricoTransacao> todosHistoricosEncontrados = historicoTransacaoRepository.findAll(pageable);

        if(todosHistoricosEncontrados.isEmpty()){
            throw new NoSuchElementException("Não há nenhum elemento de histórico de transação salvo");
        }
/*
        return new PageImpl<>(todosHistoricosEncontrados.stream().map(historico
                -> new HistoricoTransacaoResponse(historico.getId(),
                historico.getValor(),
                historico.getData(),
                historico.getDescricao(), historico.getCategorias())).collect(Collectors.toList()));

 */
        return null;
    }


    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorUsuario(Long usuarioId) {

        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId).orElseThrow(()
                -> new UsuarioNaoEncontrado("O usuário com a id informada: " +usuarioId + " não foi encontrado"));

        List<HistoricoTransacao> historicosDoUsuario = usuarioEncontrado.getTransacoesRelacionadas();
        if(historicosDoUsuario.isEmpty()){
            throw new NoSuchElementException("Não há nenhum histórico de transação encontrado associado a esse usuário");
        }
        return historicosDoUsuario.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(TiposCategorias tipoCategoria) {

        if(tipoCategoria != RECEITA && tipoCategoria != DESPESA){
            throw new DadosInvalidosException("Por favor, digite um tipo de categoria válido: "
                    + Arrays.toString(TiposCategorias.values()));
        }

        List<HistoricoTransacao> historicoTransacoes = historicoTransacaoRepository.
                encontrarTransacoesPeloTipoCategoria(tipoCategoria);

        return historicoTransacoes.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId).
                orElseThrow(() -> new CategoriaNaoEncontrada("Não foi encontrada nenhuma categoria com a id informada"));

        List<HistoricoTransacao> historicosEncontrados = categoriaEncontrada.getTransacoesRelacionadas();
        if(historicosEncontrados.isEmpty()){
            throw new HistoricoTransacaoNaoEncontrado("Não foi encontrado nenhum histórico de transação " +
                    "associado a essa categoria");
        }
        return historicosEncontrados.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }



    @Override
    public BigDecimal consultarValorTotalTransacoes(Long contaId) {

        ContaUsuario contaUsuarioEncontrada = contaUsuarioRepository.findById(contaId).orElseThrow(()
                -> new ContaNaoEncontrada("Não foi possível encontrar uma conta associada a essa id informada"));

        //Encontrar os relacionados
        List<Pagamentos> pagamentosEncontrados = contaUsuarioEncontrada.getPagamentosRelacionados();
        List<HistoricoTransacao> historicoTransacoes = contaUsuarioEncontrada.getTransacoesRelacionadas();

        //Realizar a soma dos valores
        BigDecimal somaPagamentos = pagamentosEncontrados.stream().map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal totalTransacoes = historicoTransacoes.stream().map(HistoricoTransacao::getValor)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        if(!somaPagamentos.equals(totalTransacoes)){
            throw new DadosInvalidosException("A soma de pagamento não bate com o histórico de transação");
        }
        return totalTransacoes;
    }

    @Override
    public HistoricoTransacao getHistoricoTransacaoById(Long id) {
        return historicoTransacaoRepository.findById(id).orElseThrow(()
                ->new UsuarioNaoEncontrado("Não foi encontrado nenhum histórico de transação com essa id: " + id +  " informada"));
    }

    @Override
    public boolean transacaoExistePelaID(Long id) {
        return historicoTransacaoRepository.existsById(id);
    }

    @Override
    public boolean jaExisteUmaTransacaoIgual(HistoricoTransacaoRequest dadosHistorico) {

        return historicoTransacaoRepository.existsTheSameData(dadosHistorico.valor(),
                dadosHistorico.data(), dadosHistorico.descricao(),
                dadosHistorico.tipoCategoria(), dadosHistorico.subTipoCategoria());
    }
}
