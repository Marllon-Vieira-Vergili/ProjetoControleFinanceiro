package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        if (request == null){
            
        }

        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setData(request.data());
        novoPagamento.setValor(request.valor());
        novoPagamento.setDescricao(request.descricao());

        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        novoHistorico.setData(request.data());
        novoHistorico.setValor(request.valor());
        novoHistorico.setDescricao(request.descricao());


        //Salvando o pagamento e o histórico transação para gerar uma id
        pagamentosRepository.save(novoPagamento);
        historicoTransacaoRepository.save(novoHistorico);

        //Obtendo a id dos valores após serem criados
        Long pagamentoId = novoPagamento.getId();
        Long historicoTransacaoId= novoHistorico.getId();

        //associando
        pagamentoAssociation.associarPagamentoATransacao(pagamentoId,historicoTransacaoId);
        pagamentoAssociation.associarPagamentoComCategoria(pagamentoId, request.idCategoriaFinanceira());
        pagamentoAssociation.associarPagamentoComConta(pagamentoId,request.idContaUsuario());
        pagamentoAssociation.associarPagamentoComUsuario(pagamentoId,request.idUsuarioCriado());

        return mapper.retornarDadosPagamento(novoPagamento);
    }

    @Override
    public PagamentosResponse criarPagamento(PagamentosRequest request) {
        return null;
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
}
