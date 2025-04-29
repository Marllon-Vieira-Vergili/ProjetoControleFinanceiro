package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.HistoricoTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HistoricoTransacaoImpl implements HistoricoTransacaoService {


    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    PagamentosRepository pagamentosRepository;


    @Override
    public HistoricoTransacaoResponse criarTransacao(HistoricoTransacaoRequest request) {

        //Verificar se o pagamento ja foi criado
        Pagamentos pagamentoCriado = pagamentos
        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        return null;
    }

    @Override
    public Optional<HistoricoTransacaoResponse> encontrarTransacaoPorid(Long id) {
        return Optional.empty();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data) {
        return List.of();
    }

    @Override
    public HistoricoTransacaoResponse atualizarHistoricoTransacao(HistoricoTransacaoRequest request) {
        return null;
    }

    @Override
    public Page<HistoricoTransacaoResponse> encontrarTodasTransacoes(Pageable pageable) {
        return null;
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorUsuario(Long usuarioId) {
        return List.of();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(String tipo) {
        return List.of();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId) {
        return List.of();
    }

    @Override
    public void deletarHistoricoTransacao(Long id) {

    }

    @Override
    public BigDecimal consultarValorTransacao(Long contaId) {
        return null;
    }

    @Override
    public boolean validacaoExistePelaID(Long id) {
        return false;
    }

    @Override
    public boolean JaExisteUmaTransacaoIgual(ContaUsuario contaUsuario) {
        return false;
    }
}
