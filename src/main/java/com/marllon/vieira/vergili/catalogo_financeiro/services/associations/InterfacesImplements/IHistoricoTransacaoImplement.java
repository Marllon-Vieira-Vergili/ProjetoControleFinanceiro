package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.HistoricoTAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.HistoricoTAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IHistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.CategoriaFinanceiraService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IHistoricoTransacaoImplement implements IHistoricoTransacao {

    @Autowired
    private TransacoesService transacoesService;

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private CategoriaFinanceiraService categoriaFinanceiraService;


    @Override
    public HistoricoTAssociationResponse encontrarHistoricoTransacaoAssociadaPorId(Long id) {

        //Encontrar o histórico pela sua id
        HistoricoTransacao historicoEncontrado = transacoesService.encontrarTransacaoPorId(id);

        //Retornar todas as suas associações em DTO

        HistoricoTransacaoResponse historicoTransacaoResponse = new HistoricoTransacaoResponse
                (historicoEncontrado.getId(),
                historicoEncontrado.getValor(),
                historicoEncontrado.getData(),
                historicoEncontrado.getDescricao(),
                historicoEncontrado.getCategorias());

        List<Pagamentos> pagamentoAssociado = historicoEncontrado.getPagamentosRelacionados();
        List<PagamentosResponse> pagamentosResponses = pagamentoAssociado.stream().map
                (pagamentos -> new PagamentosResponse
                        (pagamentos.getId(),
                                pagamentos.getValor(),
                                pagamentos.getData(),
                                pagamentos.getDescricao(),
                                pagamentos.getCategoria()))

                .toList();


        List<CategoriaFinanceira> categoriaAssociada = historicoEncontrado.getCategoriasRelacionadas();
        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses = categoriaAssociada.stream().map(
                categoriaFinanceira -> new CategoriaFinanceiraResponse(
                        categoriaFinanceira.getId(),
                        categoriaFinanceira.getTiposCategorias(),
                        categoriaFinanceira.getSubTipo()
                )).toList();


        ContaUsuario contaAssociada = historicoEncontrado.getContaRelacionada();
        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(contaAssociada.getId(),
                contaAssociada.getNome(),
                contaAssociada.getSaldo(),
                contaAssociada.getTipoConta());

        Usuario usuarioAssociado = historicoEncontrado.getUsuarioRelacionado();
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuarioAssociado.getId(),
                usuarioAssociado.getNome(),
                usuarioAssociado.getEmail(),
                usuarioAssociado.getTelefone());

        return new HistoricoTAssociationResponse(
                Collections.singletonList(historicoTransacaoResponse),
                pagamentosResponses,
                categoriaFinanceiraResponses,
                Collections.singletonList(contaUsuarioResponse),
                Collections.singletonList(usuarioResponse));
    }

    @Override
    public List<HistoricoTAssociationResponse> encontrarTodosHistoricosTransacoesAssociados() {

        //Encontrar todos os históricos de transações
        List<HistoricoTransacao> todosHistoricosEncontrados = transacoesService.encontrarTodasTransacoes();

        //Retornar os historicos de transacoes
        List<HistoricoTransacaoResponse> cadaTransacao = todosHistoricosEncontrados.stream()
                .map(historicoTransacao ->
                        new HistoricoTransacaoResponse(
                                historicoTransacao.getId(),
                                historicoTransacao.getValor(),
                                historicoTransacao.getData(),
                                historicoTransacao.getDescricao(),
                                historicoTransacao.getCategorias()
                        )).toList();

        //Retornar seus pagamentos associados
        List<PagamentosResponse> pagamentosAssociados = todosHistoricosEncontrados.stream()
                .flatMap(historicoTransacao ->
                        historicoTransacao.getPagamentosRelacionados().stream().map(
                                pagamentos -> new PagamentosResponse(pagamentos.getId(),
                                        pagamentos.getValor(),pagamentos.getData(),
                                        pagamentos.getDescricao(),
                                        pagamentos.getCategoria()
                                ))).toList();

        //Retornar suas categorias associadas
        List<CategoriaFinanceiraResponse> categoriasAssociadas = todosHistoricosEncontrados.stream()
                .flatMap(historicoTransacao ->
                        historicoTransacao.getCategoriasRelacionadas().stream().map(
                                categoriaFinanceira -> new CategoriaFinanceiraResponse(
                                        categoriaFinanceira.getId(),
                                        categoriaFinanceira.getTiposCategorias(),
                                        categoriaFinanceira.getSubTipo())
                        )).toList();
        //Retornar suas contas associadas
        List<ContaUsuarioResponse> contasAssociadas = todosHistoricosEncontrados
                .stream().map(HistoricoTransacao::getContaRelacionada)
                .map(contaUsuario -> new ContaUsuarioResponse(
                        contaUsuario.getId(),
                        contaUsuario.getNome(),
                        contaUsuario.getSaldo(),
                        contaUsuario.getTipoConta()
                )).toList();



        //Retornar seus usuários associados
        List<UsuarioResponse> usuariosAssociados = todosHistoricosEncontrados
                .stream().map(HistoricoTransacao::getUsuarioRelacionado)
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone()
                )).toList();


        return Collections.singletonList(new HistoricoTAssociationResponse(
                cadaTransacao,
                pagamentosAssociados,
                categoriasAssociadas,
                contasAssociadas,
                usuariosAssociados));
    }

    @Override
    public List<HistoricoTAssociationResponse> encontrarHistoricoTransacaoPorData(LocalDate data) {

        //Encontrar os históricos de transações pela data informada
        List<HistoricoTransacao> transacoesEncontradas = transacoesService.encontrarTransacaoPorData(data);

        //Retornar todas as suas associações em DTO
        List<HistoricoTransacaoResponse> transacoes = transacoesEncontradas.stream()
                .map(historicoTransacao ->
                new HistoricoTransacaoResponse(
                        historicoTransacao.getId(),
                        historicoTransacao.getValor(),
                        historicoTransacao.getData(),
                        historicoTransacao.getDescricao(),
                        historicoTransacao.getCategorias()
                )).toList();


        List<PagamentosResponse> pagamentosAssociados = transacoesEncontradas
                .stream().map(HistoricoTransacao::getPagamentosRelacionados)
                .flatMap(pagamentos -> pagamentos.stream()
                        .map(novoPagamento
                        -> new PagamentosResponse(novoPagamento.getId(),
                        novoPagamento.getValor(),
                        novoPagamento.getData(),
                        novoPagamento.getDescricao(),
                        novoPagamento.getCategoria()))).toList();

        List<CategoriaFinanceiraResponse> categoriaAssociada = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getCategoriasRelacionadas)
                .flatMap(categoriaFinanceiras -> categoriaFinanceiras
                        .stream().map(categoriaFinanceira ->
                                new CategoriaFinanceiraResponse(
                                        categoriaFinanceira.getId(),
                                        categoriaFinanceira.getTiposCategorias(),
                                        categoriaFinanceira.getSubTipo()
                                ))).toList();


        List<ContaUsuarioResponse> contaUsuarioResponse = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getContaRelacionada).map(
                        contaUsuario -> new ContaUsuarioResponse(
                                contaUsuario.getId(),
                                contaUsuario.getNome(),
                                contaUsuario.getSaldo(),
                                contaUsuario.getTipoConta()
                        )).toList();


        List<UsuarioResponse> usuarioResponse = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getUsuarioRelacionado).map(
                        usuario -> new UsuarioResponse(
                                usuario.getId(),
                                usuario.getNome(),
                                usuario.getEmail(),
                                usuario.getTelefone()
                        )).toList();

        return Collections.singletonList(new HistoricoTAssociationResponse(
                transacoes,
                pagamentosAssociados,
                categoriaAssociada,
                contaUsuarioResponse,
                usuarioResponse));
    }

    @Override
    public List<HistoricoTAssociationResponse> encontrarHistoricosPorValor(BigDecimal valor) {

        //Encontrar os históricos de transações pelo valor informado
        List<HistoricoTransacao> transacoesEncontradas = transacoesService.encontrarTransacaoPorValor(valor);

        //Retornar todas as suas associações em DTO
        List<HistoricoTransacaoResponse> transacoes = transacoesEncontradas.stream()
                .map(historicoTransacao ->
                        new HistoricoTransacaoResponse(
                                historicoTransacao.getId(),
                                historicoTransacao.getValor(),
                                historicoTransacao.getData(),
                                historicoTransacao.getDescricao(),
                                historicoTransacao.getCategorias()
                        )).toList();


        List<PagamentosResponse> pagamentosAssociados = transacoesEncontradas
                .stream().map(HistoricoTransacao::getPagamentosRelacionados)
                .flatMap(pagamentos -> pagamentos.stream()
                        .map(novoPagamento
                                -> new PagamentosResponse(novoPagamento.getId(),
                                novoPagamento.getValor(),
                                novoPagamento.getData(),
                                novoPagamento.getDescricao(),
                                novoPagamento.getCategoria()))).toList();

        List<CategoriaFinanceiraResponse> categoriaAssociada = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getCategoriasRelacionadas)
                .flatMap(categoriaFinanceiras -> categoriaFinanceiras
                        .stream().map(categoriaFinanceira ->
                                new CategoriaFinanceiraResponse(
                                        categoriaFinanceira.getId(),
                                        categoriaFinanceira.getTiposCategorias(),
                                        categoriaFinanceira.getSubTipo()
                                ))).toList();


        List<ContaUsuarioResponse> contaUsuarioResponse = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getContaRelacionada).map(
                        contaUsuario -> new ContaUsuarioResponse(
                                contaUsuario.getId(),
                                contaUsuario.getNome(),
                                contaUsuario.getSaldo(),
                                contaUsuario.getTipoConta()
                        )).toList();


        List<UsuarioResponse> usuarioResponse = transacoesEncontradas.stream()
                .map(HistoricoTransacao::getUsuarioRelacionado).map(
                        usuario -> new UsuarioResponse(
                                usuario.getId(),
                                usuario.getNome(),
                                usuario.getEmail(),
                                usuario.getTelefone()
                        )).toList();

        return Collections.singletonList(new HistoricoTAssociationResponse(
                transacoes,
                pagamentosAssociados,
                categoriaAssociada,
                contaUsuarioResponse,
                usuarioResponse));

    }

}
