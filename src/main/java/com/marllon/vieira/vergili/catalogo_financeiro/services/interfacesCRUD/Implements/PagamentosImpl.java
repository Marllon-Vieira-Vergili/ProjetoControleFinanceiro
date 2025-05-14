package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentosImpl implements PagamentosService {
    @Override
    public PagamentosResponse criarRecebimento(PagamentosRequest request) {
        return null;
    }

    @Override
    public PagamentosResponse criarPagamento(PagamentosRequest request) {
        return null;
    }

    @Override
    public Pagamentos encontrarPagamentoPorid(Long id) {
        return null;
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
