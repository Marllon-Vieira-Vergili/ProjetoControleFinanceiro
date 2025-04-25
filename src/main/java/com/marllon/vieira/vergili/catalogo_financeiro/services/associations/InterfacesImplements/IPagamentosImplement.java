package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.CategoriaFinanceiraService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IPagamentosImplement implements IPagamentos {

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private CategoriaFinanceiraService categoriaService;

    @Autowired
    private TransacoesService transacoesService;


    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento) {

        // Buscar conta e validar existência
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorNome(novoPagamento.nomeContaAssociada());
        if (contaEncontrada == null) {
            throw new NoSuchElementException("Nenhuma conta foi encontrada com esse nome!");
        }

        //Verificar se o tipo de pagamento é receita
        if (novoPagamento.pagamento().categoria().equals(TiposCategorias.DESPESA)) {
            // Verificar saldo disponível antes de criar o pagamento
            BigDecimal valorPagamento = novoPagamento.pagamento().valor();
            if (contaEncontrada.getSaldo().compareTo(valorPagamento) < 0) {
                // Subtrair saldo da conta, mas conta ficará com valor negativo
                contaEncontrada.subtrairSaldo(contaEncontrada, valorPagamento);
            }
        } else {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser do tipo RECEITA," +
                    " e nem Subtipo de pagamentos associados a receitas");
        }


        // Criar novo pagamento
        Pagamentos novoPagamentoCriado;
        try {
            novoPagamentoCriado = pagamentosService.criarNovoPagamento(novoPagamento.pagamento());
            if (novoPagamentoCriado == null) {
                throw new RuntimeException("Não foi possível criar este pagamento!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar o pagamento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao criar o pagamento: " + e);
        }

        // Associar histórico de transação se necessário
        HistoricoTransacao novoHistoricoTransacao = null;
        if (novoPagamentoCriado.getTransacoesRelacionadas().isEmpty()) {
            try {
                novoHistoricoTransacao = new HistoricoTransacao();
                novoHistoricoTransacao.setValor(novoPagamentoCriado.getValor());
                novoHistoricoTransacao.setData(novoPagamentoCriado.getData());
                novoHistoricoTransacao.setDescricao(novoPagamentoCriado.getDescricao());
                novoHistoricoTransacao.setCategorias(novoPagamentoCriado.getCategoria());
                novoHistoricoTransacao.setContaRelacionada(novoPagamentoCriado.getContaRelacionada());
                novoHistoricoTransacao.setUsuarioRelacionado(novoPagamentoCriado.getUsuarioRelacionado());

                novoPagamentoCriado.associarPagamentoATransacao(novoHistoricoTransacao);
                transacoesService.salvarNovaTransacao(novoHistoricoTransacao);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar o Histórico de transação: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Erro inesperado ao criar Histórico de transação: " + e);
            }
        }

        // Associar pagamento à conta e usuário
        try {
            if (Arrays.asList(TiposContas.values()).contains(contaEncontrada.getTipoConta())) {
                novoPagamentoCriado.associarPagamentoComConta(contaEncontrada);
                novoPagamentoCriado.setUsuarioRelacionado(contaEncontrada.getUsuarioRelacionado());
                contaUsuarioService.salvarNovaContaUsuario(contaEncontrada);
            } else {
                throw new IllegalArgumentException("Tipo de conta inválido ou conta não existe.");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao associar o pagamento à conta: " + e.getMessage());
        }

        // Associar pagamento a categoria
        CategoriaFinanceira categoriaASerAssociada;
        try {
            if (novoPagamentoCriado.getCategoria() == null) {
                categoriaASerAssociada = categoriaService.encontrarCategoriaETipo(
                        novoPagamento.pagamento().categoria(),
                        novoPagamento.subTipoCategoria()
                );

                if (categoriaASerAssociada == null) {
                    CategoriaFinanceira novaCategoria = new CategoriaFinanceira();
                    novaCategoria.setTiposCategorias(novoPagamento.pagamento().categoria());
                    novaCategoria.setSubTipo(novoPagamento.subTipoCategoria());
                    novaCategoria.associarCategoriaComConta(contaEncontrada);
                    novaCategoria.setUsuarioRelacionado(novoPagamentoCriado.getUsuarioRelacionado());

                    categoriaService.salvarNovaCategoria(novaCategoria);
                }

                novoPagamentoCriado.associarPagamentoComCategoria(categoriaASerAssociada);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao associar categoria ao pagamento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao associar categoria ao pagamento: " + e);
        }

        // Montar DTOs para resposta
        PagamentosResponse pagamentosResponse = new PagamentosResponse(
                novoPagamentoCriado.getId(),
                novoPagamentoCriado.getValor(),
                novoPagamentoCriado.getData(),
                novoPagamentoCriado.getDescricao(),
                novoPagamentoCriado.getCategoria()
        );

        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(
                contaEncontrada.getId(),
                contaEncontrada.getNome(),
                contaEncontrada.getSaldo(),
                contaEncontrada.getTipoConta()
        );

        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses =
                novoPagamentoCriado.getCategoriasRelacionadas().stream()
                        .map(c -> new CategoriaFinanceiraResponse(c.getId(), c.getTiposCategorias(), c.getSubTipo()))
                        .toList();

        assert novoHistoricoTransacao != null;
        HistoricoTransacaoResponse historicoTransacaoResponse = new HistoricoTransacaoResponse(
                novoHistoricoTransacao.getId(),
                novoHistoricoTransacao.getValor(),
                novoHistoricoTransacao.getData(),
                novoHistoricoTransacao.getDescricao(),
                novoHistoricoTransacao.getCategorias()
        );


        return new PagamentoAssociationResponse(
                Collections.singletonList(pagamentosResponse),
                Collections.singletonList(historicoTransacaoResponse),
                categoriaFinanceiraResponses,
                Collections.singletonList(contaUsuarioResponse)
        );
    }

    @Override
    public PagamentoAssociationResponse criarEAssociarRecebimento(PagamentoAssociationRequest novoRecebimento) {


        // Buscar conta e validar existência
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorNome(novoRecebimento.nomeContaAssociada());
        if (contaEncontrada == null) {
            throw new NoSuchElementException("Nenhuma conta foi encontrada com esse nome!");
        }

        //Verificar se o tipo de pagamento é receita
        if (novoRecebimento.pagamento().categoria().equals(TiposCategorias.RECEITA)) {
            // Verificar saldo disponível antes de criar o pagamento
            BigDecimal valorRecebimento = novoRecebimento.pagamento().valor();
                // somar saldo da conta
                contaEncontrada.adicionarSaldo(contaEncontrada, valorRecebimento);

        } else {
            throw new IllegalArgumentException("Tipo de recebimento não pode ser do tipo DESPESA," +
                    " e nem Subtipo de pagamentos associados a despesas");
        }


        // Criar novo recebimento
        Pagamentos novoRecebimentoCriado;
        try {
            novoRecebimentoCriado = pagamentosService.criarNovoPagamento(novoRecebimento.pagamento());
            if (novoRecebimentoCriado == null) {
                throw new RuntimeException("Não foi possível criar este pagamento!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar o recebimento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao criar o recebimento: " + e);
        }

        // Associar histórico de transação se necessário
        HistoricoTransacao novoHistoricoTransacao = null;
        if (novoRecebimentoCriado.getTransacoesRelacionadas().isEmpty()) {
            try {
                novoHistoricoTransacao = new HistoricoTransacao();
                novoHistoricoTransacao.setValor(novoRecebimentoCriado.getValor());
                novoHistoricoTransacao.setData(novoRecebimentoCriado.getData());
                novoHistoricoTransacao.setDescricao(novoRecebimentoCriado.getDescricao());
                novoHistoricoTransacao.setCategorias(novoRecebimentoCriado.getCategoria());
                novoHistoricoTransacao.setContaRelacionada(novoRecebimentoCriado.getContaRelacionada());
                novoHistoricoTransacao.setUsuarioRelacionado(novoRecebimentoCriado.getUsuarioRelacionado());

                novoRecebimentoCriado.associarPagamentoATransacao(novoHistoricoTransacao);
                transacoesService.salvarNovaTransacao(novoHistoricoTransacao);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao criar o Histórico de transação: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Erro inesperado ao criar Histórico de transação: " + e);
            }
        }

        // Associar pagamento à conta e usuário
        try {
            if (Arrays.asList(TiposContas.values()).contains(contaEncontrada.getTipoConta())) {
                novoRecebimentoCriado.associarPagamentoComConta(contaEncontrada);
                novoRecebimentoCriado.setUsuarioRelacionado(contaEncontrada.getUsuarioRelacionado());
                contaUsuarioService.salvarNovaContaUsuario(contaEncontrada);
            } else {
                throw new IllegalArgumentException("Tipo de conta inválido ou conta não existe.");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao associar o pagamento à conta: " + e.getMessage());
        }

        // Associar pagamento a categoria
        CategoriaFinanceira categoriaASerAssociada;
        try {
            if (novoRecebimentoCriado.getCategoria() == null) {
                categoriaASerAssociada = categoriaService.encontrarCategoriaETipo(
                        novoRecebimento.pagamento().categoria(),
                        novoRecebimento.subTipoCategoria()
                );

                if (categoriaASerAssociada == null) {
                    CategoriaFinanceira novaCategoria = new CategoriaFinanceira();
                    novaCategoria.setTiposCategorias(novoRecebimento.pagamento().categoria());
                    novaCategoria.setSubTipo(novoRecebimento.subTipoCategoria());
                    novaCategoria.associarCategoriaComConta(contaEncontrada);
                    novaCategoria.setUsuarioRelacionado(novoRecebimentoCriado.getUsuarioRelacionado());

                    categoriaService.salvarNovaCategoria(novaCategoria);
                }

                novoRecebimentoCriado.associarPagamentoComCategoria(categoriaASerAssociada);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao associar categoria ao pagamento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao associar categoria ao pagamento: " + e);
        }

        // Montar DTOs para resposta
        PagamentosResponse pagamentosResponse = new PagamentosResponse(
                novoRecebimentoCriado.getId(),
                novoRecebimentoCriado.getValor(),
                novoRecebimentoCriado.getData(),
                novoRecebimentoCriado.getDescricao(),
                novoRecebimentoCriado.getCategoria()
        );

        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(
                contaEncontrada.getId(),
                contaEncontrada.getNome(),
                contaEncontrada.getSaldo(),
                contaEncontrada.getTipoConta()
        );

        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses =
                novoRecebimentoCriado.getCategoriasRelacionadas().stream()
                        .map(c -> new CategoriaFinanceiraResponse(c.getId(), c.getTiposCategorias(), c.getSubTipo()))
                        .toList();

        assert novoHistoricoTransacao != null;
        HistoricoTransacaoResponse historicoTransacaoResponse = new HistoricoTransacaoResponse(
                novoHistoricoTransacao.getId(),
                novoHistoricoTransacao.getValor(),
                novoHistoricoTransacao.getData(),
                novoHistoricoTransacao.getDescricao(),
                novoHistoricoTransacao.getCategorias()
        );


        return new PagamentoAssociationResponse(
                Collections.singletonList(pagamentosResponse),
                Collections.singletonList(historicoTransacaoResponse),
                categoriaFinanceiraResponses,
                Collections.singletonList(contaUsuarioResponse)
        );
    }


    @Override
    public PagamentoAssociationResponse encontrarPagamentoAssociadoPorId(Long id) {

        //Encontrar o pagamento pela id
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("Nenhum pagamento encontrado com essa id!"));


        // Encontrar suas associações mapeados pela DTO
        PagamentosResponse pagamentosResponse = new PagamentosResponse(
                pagamentoEncontrado.getId(),
                pagamentoEncontrado.getValor(),
                pagamentoEncontrado.getData(),
                pagamentoEncontrado.getDescricao(),
                pagamentoEncontrado.getCategoria()
        );

        List<HistoricoTransacao> historicoTransacaoEncontrado = pagamentoEncontrado.getTransacoesRelacionadas();
        List<HistoricoTransacaoResponse> historicoTransacaoResponses = historicoTransacaoEncontrado.stream()
                .map(historicoTransacao -> new HistoricoTransacaoResponse(historicoTransacao.getId(),
                        historicoTransacao.getValor(), historicoTransacao.getData(), historicoTransacao.getDescricao(),
                        historicoTransacao.getCategorias())).toList();


        ContaUsuario contaEncontrada = pagamentoEncontrado.getContaRelacionada();
        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(contaEncontrada.getId(),
                contaEncontrada.getNome(), contaEncontrada.getSaldo(),contaEncontrada.getTipoConta());


        Usuario usuarioEncontrado = pagamentoEncontrado.getUsuarioRelacionado();
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(),
                usuarioEncontrado.getEmail(), usuarioEncontrado.getTelefone());

        List<CategoriaFinanceira> categoriaFinanceiraEncontrada = pagamentoEncontrado.getCategoriasRelacionadas();
        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses = categoriaFinanceiraEncontrada.stream()
                .map(categoriaFinanceira -> new CategoriaFinanceiraResponse(categoriaFinanceira
                        .getId(), categoriaFinanceira.getTiposCategorias(), categoriaFinanceira.getSubTipo())).toList();


        return new PagamentoAssociationResponse(
                Collections.singletonList(pagamentosResponse),
                historicoTransacaoResponses,
                categoriaFinanceiraResponses,
                Collections.singletonList(contaUsuarioResponse)
        );
    }

    @Override
    public List<PagamentoAssociationResponse> encontrarTodosPagamentosAssociados() {

        //Encontrar todos os pagamentos
        List<Pagamentos> todosPagamentos = pagamentosService.encontrarTodosPagamentos();

        // Encontrar suas associações mapeados pela DTO
        List<PagamentosResponse> pagamentosResponse = todosPagamentos.stream().map(pagamentos ->
                new PagamentosResponse(pagamentos.getId(),
                        pagamentos.getValor(), pagamentos.getData(), pagamentos.getDescricao()
                        , pagamentos.getCategoria())).toList();


        List<HistoricoTransacaoResponse> historicoTransacaoEncontrado = todosPagamentos.stream().flatMap(pagamentos
                -> pagamentos.getTransacoesRelacionadas().stream().map
                (historicoTransacao -> new HistoricoTransacaoResponse(historicoTransacao.getId(),
                        historicoTransacao.getValor(), historicoTransacao.getData(), historicoTransacao.getDescricao(),
                        historicoTransacao.getCategorias()))).toList();


        List<ContaUsuario> contasEncontradas = todosPagamentos.stream().
                map(Pagamentos::getContaRelacionada).toList();
        List<ContaUsuarioResponse> contaUsuarios = contasEncontradas.stream().map(contaUsuario ->
                new ContaUsuarioResponse(
                        contaUsuario.getId(),
                        contaUsuario.getNome(),
                        contaUsuario.getSaldo(),
                        contaUsuario.getTipoConta())).toList();


        List<CategoriaFinanceiraResponse> categoriasFinanceirasEncontradas =
                todosPagamentos.stream().flatMap
                        (pagamentos -> pagamentos.getCategoriasRelacionadas()
                                .stream().map(categoriaRelacionada ->
                                        new CategoriaFinanceiraResponse(categoriaRelacionada.getId(),
                                                categoriaRelacionada.getTiposCategorias(),
                                                categoriaRelacionada.getSubTipo()))).toList();


        return Collections.singletonList(new PagamentoAssociationResponse(pagamentosResponse,
                historicoTransacaoEncontrado, categoriasFinanceirasEncontradas, contaUsuarios));
    }

    @Override
    public PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest
            pagamentoAtualizado) {

        //Encontrar antigo pagamento pela id
        Pagamentos pagamentoAntigo;
        try {
            pagamentoAntigo = pagamentosService.encontrarPagamentoPorId(id);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Não foi encontrado nenhum pagamento com essa id informada!" + e.getMessage());
        }


        // Buscar conta e validar existência
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorNome(pagamentoAtualizado.nomeContaAssociada());
        if (contaEncontrada == null) {
            throw new NoSuchElementException("Nenhuma conta foi encontrada com esse nome!");
        }

        //Verificar se o tipo de pagamento é receita
        if (pagamentoAtualizado.pagamento().categoria().equals(TiposCategorias.DESPESA)) {
            // Verificar saldo disponível antes de criar o pagamento
            BigDecimal diferenca = pagamentoAtualizado.pagamento().valor().subtract(pagamentoAntigo.getValor());
            if (contaEncontrada.getSaldo().compareTo(diferenca) < 0) {
                // Subtrair saldo da conta, mas conta ficará com valor negativo
                contaEncontrada.subtrairSaldo(contaEncontrada, diferenca);
            }
        } else {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser do tipo RECEITA," +
                    " e nem Subtipo de pagamentos associados a receitas");
        }

        //Atualizando os dados do pagamento
        Pagamentos pagamentoDevidamenteAtualizado;
        try {
            pagamentoDevidamenteAtualizado = pagamentosService.atualizarPagamento
                    (id, pagamentoAtualizado.pagamento());
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível realizar a atualização dos dados deste pagamento" + e.getMessage());
        }


        // Montar DTOs para resposta
        PagamentosResponse pagamentosResponse = new PagamentosResponse(
                pagamentoDevidamenteAtualizado.getId(),
                pagamentoDevidamenteAtualizado.getValor(),
                pagamentoDevidamenteAtualizado.getData(),
                pagamentoDevidamenteAtualizado.getDescricao(),
                pagamentoDevidamenteAtualizado.getCategoria()
        );

        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(
                contaEncontrada.getId(),
                contaEncontrada.getNome(),
                contaEncontrada.getSaldo(),
                contaEncontrada.getTipoConta()
        );

        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses =
                contaEncontrada.getCategoriasRelacionadas().stream()
                        .map(c -> new CategoriaFinanceiraResponse(c.getId(), c.getTiposCategorias(), c.getSubTipo()))
                        .toList();


        List<HistoricoTransacao> historicoTransacaoEncontrado = pagamentoDevidamenteAtualizado.getTransacoesRelacionadas();
        List<HistoricoTransacaoResponse> historicoTransacaoResponses = historicoTransacaoEncontrado.stream()
                .map(historicoTransacao -> new HistoricoTransacaoResponse(historicoTransacao.getId(),
                        historicoTransacao.getValor(), historicoTransacao.getData(), historicoTransacao.getDescricao(),
                        historicoTransacao.getCategorias())).toList();


        //Retornar o DTO ao usuario
        return new PagamentoAssociationResponse(
                Collections.singletonList(pagamentosResponse),
                historicoTransacaoResponses,
                categoriaFinanceiraResponses,
                Collections.singletonList(contaUsuarioResponse));
    }

    @Override
    public void removerPagamentoAssociadoPelaId(Long id) {

        // Encontrar o pagamento pela id
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorId(id);

        // Remover associações com transações
        for (HistoricoTransacao transacoesEncontradas : new ArrayList<>(pagamentoEncontrado.getTransacoesRelacionadas())) {
            try {
                pagamentoEncontrado.desassociarPagamentoDeTransacao(transacoesEncontradas);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar transação: " + e.getMessage());
            }
        }

        // Remover associações com categorias
        for (CategoriaFinanceira categoriaFinanceira : new ArrayList<>(pagamentoEncontrado.getCategoriasRelacionadas())) {
            try {
                pagamentoEncontrado.desassociarPagamentoCategoria(categoriaFinanceira);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar categoria: " + e.getMessage());
            }
        }

        // Remover associações com conta
        try{
            ContaUsuario contaEncontrada = pagamentoEncontrado.getContaRelacionada();
            pagamentoEncontrado.desassociarPagamentoConta(contaEncontrada);
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível desassociar esse pagamento desta conta");
        }


        // Remover associações com usuário
        try{
            Usuario usuarioEncontrado = pagamentoEncontrado.getUsuarioRelacionado();
            pagamentoEncontrado.desassociarPagamentoUsuario(usuarioEncontrado);
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível desassociar esse pagamento deste usuário");
        }

        // Remover o pagamento
        pagamentosService.removerPagamentoPorId(id);
    }
}

