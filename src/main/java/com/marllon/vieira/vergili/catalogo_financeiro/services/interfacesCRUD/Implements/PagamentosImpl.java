package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    @Override
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

        //associando
        pagamentoAssociation.associarPagamentoATransacao(pagamentoId,historicoTransacaoId);
        pagamentoAssociation.associarPagamentoComCategoria(pagamentoId, request.idCategoriaFinanceira());
        pagamentoAssociation.associarPagamentoComConta(pagamentoId,request.idContaUsuario());
        pagamentoAssociation.associarPagamentoComUsuario(pagamentoId,request.idUsuarioCriado());

        //Adicionando o valor ao saldo na conta do usuário

        return mapper.retornarDadosPagamento(novoRecebimento);
    }

    @Override
    public PagamentosResponse criarPagamento(PagamentosRequest request) {

        //Verificando se o usuário passará os dados corretos para criação
        if(!dataEstaCorreta(request.data())){
            throw new DadosInvalidosException("Por favor, digite uma data válida para o pagamento " +
                    "entre a partir do dia de hoje, para no máximo 1 mês a frente");
        }
        if(!valorEstaCorreto((request.valor()))){
            throw new DadosInvalidosException("Por favor, digite um valor válido para o pagamento");
        }

        //Criando o pagamento
        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setData(request.data());
        novoPagamento.setValor(request.valor());
        novoPagamento.setDescricao(request.descricao());
        novoPagamento.setTiposCategorias(request.tipoCategoria());

        //Salvando o novo pagamento
        pagamentosRepository.save(novoPagamento);

        return mapper.retornarDadosPagamento(novoPagamento);
    }

    @Override
    public Optional<Pagamentos> encontrarPagamentoPorid(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentoPorData(LocalDate data) {
        return List.of();
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
    public PagamentosResponse atualizarPagamento(Long id, PagamentosRequest request) {
        return null;
    }

    @Override
    public Page<PagamentosResponse> encontrarTodosPagamentos(Pageable pageable) {
        return null;
    }

    @Override
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
