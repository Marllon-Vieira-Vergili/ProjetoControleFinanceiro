package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.HistoricoTransacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoTransacaoImpl implements HistoricoTransacaoService {
    @Override
    public Optional<HistoricoTransacao> encontrarTransacaoPorid(Long id) {
        return Optional.empty();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data) {
        return List.of();
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
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(TiposCategorias tiposCategoria) {
        return List.of();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId) {
        return List.of();
    }

    @Override
    public BigDecimal consultarValorTotalTransacoes(Long contaId) {
        return null;
    }

    @Override
    public boolean jaExisteUmaTransacaoIgual(HistoricoTransacaoRequest historicoTransacao) {
        return false;
    }
}