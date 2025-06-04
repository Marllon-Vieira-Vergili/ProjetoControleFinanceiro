package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.HistoricoTransacaoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.HistoricoTransacaoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.HistoricoTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HistoricoTransacaoImpl implements HistoricoTransacaoService {

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private HistoricoTransacaoMapper historicoTransacaoMapper;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Optional<HistoricoTransacaoResponse> encontrarTransacaoPorid(Long id) {
        HistoricoTransacao historicoLocalizado = historicoTransacaoRepository.findById(id).orElseThrow(()
                -> new HistoricoTransacaoNaoEncontrado("Não foi encontrado nenhum histórico de transação com essa id: " + id));

        return Optional.ofNullable(historicoTransacaoMapper.retornarHistoricoTransacao(historicoLocalizado));
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarTransacaoPorData(LocalDate data) {

        if (!dataEstaCorreta(data)){
            throw new DadosInvalidosException("A data está incorreta, tente digitá-la novamente!");
        }

        List<HistoricoTransacao> historicosLocalizadosPelaData = historicoTransacaoRepository.encontrarTransacoesPelaData(data);

        if (historicosLocalizadosPelaData.isEmpty()){
            throw new HistoricoTransacaoNaoEncontrado("Não foi encontrado nenhum Histórico de transação pela data: " + data);
        }
        return historicosLocalizadosPelaData.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }

    @Override
    public Page<HistoricoTransacaoResponse> encontrarTodasTransacoes(Pageable pageable) {


        List<HistoricoTransacao> todasAsTransacoesRealizadas = historicoTransacaoRepository.findAll();

        if (todasAsTransacoesRealizadas.isEmpty()){
            throw new PagamentoNaoEncontrado("Nenhum Histórico de Transação foi encontrado no banco de dados");
        }
        Page<HistoricoTransacao> historicoTransacoes = new PageImpl<>(todasAsTransacoesRealizadas);

        return historicoTransacoes.map(historicoTransacaoMapper::retornarHistoricoTransacao);

    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorUsuario(Long usuarioId) {

        Usuario usuarioLocalizado = usuarioRepository.findById(usuarioId).orElseThrow(()
                ->new UsuarioNaoEncontrado("Nenhum usuário foi localizado com essa id!"));

        List<HistoricoTransacao> historicosTransacoes = usuarioLocalizado.getTransacoesRelacionadas();

        return historicosTransacoes.stream().map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorTipo(TiposCategorias tiposCategoria) {

        return historicoTransacaoRepository.findAll().stream().filter(historicoTransacao ->
                historicoTransacao.getTiposCategorias() == tiposCategoria)
                .map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarHistoricoTransacaoPorCategoria(Long categoriaId) {

        return historicoTransacaoRepository.findAll().stream().filter(historicoTransacao ->
                        Objects.equals(historicoTransacao.getCategoriaRelacionada().getId(), categoriaId))
                .map(historicoTransacaoMapper::retornarHistoricoTransacao).toList();
    }


    private boolean dataEstaCorreta(LocalDate data) {
        LocalDate hoje = LocalDate.now();
        LocalDate validoAteUmMesAFrenteDeHoje = hoje.plusMonths(1);

        return (!data.isBefore(hoje)) && ((!data.isAfter(validoAteUmMesAFrenteDeHoje)));
    }
}