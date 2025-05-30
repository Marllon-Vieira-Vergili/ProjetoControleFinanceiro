package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentosImpl implements PagamentosService {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private CategoriaFinanceiraAssociation categoriaAssociation;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private PagamentosAssociation pagamentoAssociation;

    @Autowired
    private PagamentoMapper mapper;

    @Autowired
    private ContaUsuarioService contaUsuarioService;


    @Override
    @Transactional
    public PagamentosResponse criarRecebimento(PagamentosRequest request) {

        if (request.tipoCategoria() != TiposCategorias.RECEITA){
            throw new DadosInvalidosException("Para criar Recebimento, somente o tipo RECEITA é válido");
        }

        if(!dataEstaCorreta(request.data())){
            throw new DadosInvalidosException("Por favor, digite uma data correta! " +
                    "entre a partir de hoje, e no máximo para 1 mês a partir de hoje");
        }
        if(!valorEstaCorreto(request.valor())){
            throw new DadosInvalidosException("Por favor, digite um valor correto! ");
        }

        Pagamentos novoRecebimento = new Pagamentos();
        novoRecebimento.setData(request.data());
        novoRecebimento.setValor(request.valor());
        novoRecebimento.setDescricao(request.descricao());
        novoRecebimento.setTiposCategorias(request.tipoCategoria());

        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        novoHistorico.setData(request.data());
        novoHistorico.setValor(request.valor());
        novoHistorico.setDescricao(request.descricao());
        novoHistorico.setTiposCategorias(request.tipoCategoria());

        //Salvando o pagamento e o histórico transação para gerar uma id
        pagamentosRepository.save(novoRecebimento);
        historicoTransacaoRepository.save(novoHistorico);

        //Obtendo a id dos valores após serem criados
        Long pagamentoId = novoRecebimento.getId();
        Long historicoTransacaoId= novoHistorico.getId();


        Optional<CategoriaFinanceira> categoriaFinanceiraEncontrada = categoriaFinanceiraRepository.findById(request.idCategoriaFinanceira());
        Optional<HistoricoTransacao> historicoTransacaoEncontrado = historicoTransacaoRepository.findById(novoHistorico.getId());
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(request.idUsuarioCriado());
        Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(request.idContaUsuario());


        //associando
        if (novoRecebimento.getId() != null){
            try{
                pagamentoAssociation.associarPagamentoATransacao(pagamentoId,historicoTransacaoEncontrado.get().getId());
                pagamentoAssociation.associarPagamentoComCategoria(pagamentoId, categoriaFinanceiraEncontrada.get().getId());
                pagamentoAssociation.associarPagamentoComConta(pagamentoId,contaUsuarioEncontrada.get().getId());
                pagamentoAssociation.associarPagamentoComUsuario(pagamentoId,usuarioEncontrado.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar Recebimento com: " + e.getCause());
            }
        }

        //Buscando a conta desejada para atualizar o saldo dela

        //Adicionando o valor ao saldo na conta do usuário
        contaUsuarioEncontrada.ifPresent(contaUsuario ->
                contaUsuarioService.adicionarSaldo(contaUsuario.getId(), novoRecebimento.getValor()));

        return mapper.retornarDadosPagamento(novoRecebimento);
    }

    @Override
    @Transactional
    public PagamentosResponse criarPagamento(PagamentosRequest request) {

        if (request.tipoCategoria() != TiposCategorias.DESPESA){
            throw new DadosInvalidosException("Para criar Pagamento, somente o tipo DESPESA é válido");
        }

        if(!dataEstaCorreta(request.data())){
            throw new DadosInvalidosException("Por favor, digite uma data correta! " +
                    "entre a partir de hoje, e no máximo para 1 mês a partir de hoje");
        }
        if(!valorEstaCorreto(request.valor())){
            throw new DadosInvalidosException("Por favor, digite um valor correto! ");
        }

        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setData(request.data());
        novoPagamento.setValor(request.valor());
        novoPagamento.setDescricao(request.descricao());
        novoPagamento.setTiposCategorias(request.tipoCategoria());

        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        novoHistorico.setData(request.data());
        novoHistorico.setValor(request.valor());
        novoHistorico.setDescricao(request.descricao());
        novoHistorico.setTiposCategorias(request.tipoCategoria());

        //Salvando o pagamento e o histórico transação para gerar uma id
        pagamentosRepository.save(novoPagamento);
        historicoTransacaoRepository.save(novoHistorico);

        //Obtendo a id dos valores após serem criados
        Long pagamentoId = novoPagamento.getId();
        Long historicoTransacaoId= novoHistorico.getId();


        Optional<CategoriaFinanceira> categoriaFinanceiraEncontrada = categoriaFinanceiraRepository.findById(request.idCategoriaFinanceira());
        Optional<HistoricoTransacao> historicoTransacaoEncontrado = historicoTransacaoRepository.findById(historicoTransacaoId);
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(request.idUsuarioCriado());
        Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(request.idContaUsuario());


        //associando
        if (novoPagamento.getId() != null){
            try{
                pagamentoAssociation.associarPagamentoATransacao(pagamentoId,historicoTransacaoEncontrado.get().getId());
                pagamentoAssociation.associarPagamentoComCategoria(pagamentoId, categoriaFinanceiraEncontrada.get().getId());
                pagamentoAssociation.associarPagamentoComConta(pagamentoId,contaUsuarioEncontrada.get().getId());
                pagamentoAssociation.associarPagamentoComUsuario(pagamentoId,usuarioEncontrado.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar Pagamento com: " + e.getCause());
            }
        }

        //Buscando a conta desejada para atualizar o saldo dela

        //Adicionando o valor ao saldo na conta do usuário
        contaUsuarioEncontrada.ifPresent(contaUsuario ->
                contaUsuarioService.subtrairSaldo(contaUsuario.getId(), novoPagamento.getValor()));

        return mapper.retornarDadosPagamento(novoPagamento);
    }

    @Override
    public Optional<PagamentosResponse> encontrarPagamentoOuRecebimentoPorid(Long id) {

        Pagamentos pagamentoOuRecebimentoEncontrado = (pagamentosRepository.findById(id)
                .orElseThrow(() -> new PagamentoNaoEncontrado("O Pagamento não foi encontrado!")));

        return Optional.ofNullable(mapper.retornarDadosPagamento(pagamentoOuRecebimentoEncontrado));
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentoPorData(LocalDate data) {

        List<Pagamentos> pagamentosEOuRecebimentosLocalizadosPelaData =
                pagamentosRepository.encontrarPagamentoPelaData(data);

        return pagamentosEOuRecebimentosLocalizadosPelaData.stream().map(mapper::retornarDadosPagamento)
                .toList();
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPorUsuario(Long usuarioId) {


        return List.of();
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPorTipo(TiposCategorias tipoCategoria) {
        return List.of();
    }

    @Override
    @Transactional
    public PagamentosResponse atualizarPagamento(Long id, PagamentosRequest request) {
        return null;
    }

    @Override
    public Page<PagamentosResponse> encontrarTodosPagamentos(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public void deletarPagamento(Long id) {

    }

    @Override
    public BigDecimal consultarValorPagamento(Long contaId) {
        return null;
    }

    @Override
    public boolean sePagamentoForDespesa() {
        return false;
    }

    @Override
    public boolean sePagamentoForReceita() {
        return false;
    }

    @Override
    public boolean jaExisteUmPagamentoIgual(PagamentosRequest pagamento) {
        return false;
    }

    @Override
    public boolean dataEstaCorreta(LocalDate data) {
        LocalDate hoje = LocalDate.now();
        LocalDate validoAteUmMesAFrenteDeHoje = hoje.plusMonths(1);

        return (!data.isBefore(hoje)) && ((!data.isAfter(validoAteUmMesAFrenteDeHoje)));
    }

    @Override
    public boolean valorEstaCorreto(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
}
