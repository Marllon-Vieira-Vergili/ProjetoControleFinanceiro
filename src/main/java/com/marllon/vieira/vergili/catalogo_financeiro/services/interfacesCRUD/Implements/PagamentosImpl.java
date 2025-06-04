package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Pagamentos.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;

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
    private HistoricoTransacaoAssociation historicoTransacaoAssociation;

    @Autowired
    private PagamentoMapper mapper;

    @Autowired
    private ContaUsuarioService contaUsuarioService;


    @Override
    @Transactional
    public Pagamentos criarTransacao(BigDecimal valor, LocalDate data, String descricao,
                                     TiposCategorias tiposCategoria, SubTipoCategoria subTipo) {
        if (!dataEstaCorreta(data)) {
            throw new DadosInvalidosException("Por favor, digite uma data correta! " +
                    "entre a partir de hoje, e no máximo para 1 mês a partir de hoje");
        }
        if (!valorEstaCorreto(valor)) {
            throw new DadosInvalidosException("Por favor, digite um valor correto! ");
        }

        if (tiposCategoria != DESPESA && tiposCategoria != TiposCategorias.RECEITA) {
            throw new TiposCategoriasNaoEncontrado("Não foi encontrado o tipo de categoria corretamente");
        }

        Pagamentos novaTransacao = new Pagamentos();
        novaTransacao.setData(data);
        novaTransacao.setValor(valor);
        novaTransacao.setDescricao(descricao);
        novaTransacao.setTiposCategorias(tiposCategoria);

        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        novoHistorico.setData(data);
        novoHistorico.setValor(valor);
        novoHistorico.setDescricao(descricao);
        novoHistorico.setTiposCategorias(tiposCategoria);

        //Salvando o pagamento e o histórico transação para gerar uma id
        pagamentosRepository.save(novaTransacao);
        historicoTransacaoRepository.save(novoHistorico);

        //Encontrando a categoria financeira já criada pelo subtipocategoria informado
        CategoriaFinanceira categoriaEncontrada =
                Optional.ofNullable(categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(subTipo)).orElseThrow(()
                        -> new CategoriaNaoEncontrada("Não foi encontrada nenhuma categoria Financeira criada com o Subtipo informado"));

        //Associando
        pagamentoAssociation.associarPagamentoATransacao(novaTransacao.getId(), novoHistorico.getId());
        pagamentoAssociation.associarPagamentoComCategoria(novaTransacao.getId(), categoriaEncontrada.getId());
        historicoTransacaoAssociation.associarTransacaoComCategoria(novoHistorico.getId(), categoriaEncontrada.getId());

        return novaTransacao;
    }

    @Override
    @Transactional
    public PagamentosResponse criarRecebimento(PagamentosRequest request) {

        if (request.tipoCategoria() != TiposCategorias.RECEITA) {
            throw new DadosInvalidosException("Para criar Recebimento, somente o tipo RECEITA é válido");
        }

        if (jaExisteUmPagamentoIgual(request.valor(), request.data(), request.descricao(), request.tipoCategoria(),
                request.subTipoCategoria())) {
            throw new JaExisteException("Já existe um recebimento criado idêntico");
        }

        //Criando os valores
        Pagamentos recebimentoETransacaoCriado =
                criarTransacao(request.valor(),
                        request.data(),
                        request.descricao(),
                        request.tipoCategoria(),
                        request.subTipoCategoria());

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(request.idUsuarioCriado());
        Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(request.idContaUsuario());

        if (contaUsuarioEncontrada.isPresent() && !contaUsuarioEncontrada.get()
                .getPagamentosRelacionados().contains(recebimentoETransacaoCriado)) {
            try {
                pagamentoAssociation.associarPagamentoComConta(recebimentoETransacaoCriado.getId(),
                        contaUsuarioEncontrada.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar esse Recebimento a essa conta de usuário" + e.getCause());
            }
        }
        if (usuarioEncontrado.isPresent() && !usuarioEncontrado.get().getPagamentosRelacionados()
                .contains(recebimentoETransacaoCriado)) {
            try {
                pagamentoAssociation.associarPagamentoComUsuario(recebimentoETransacaoCriado.getId(), usuarioEncontrado.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar esse Recebimento a esse usuário" + e.getCause());
            }
        }

        //Buscando a conta desejada para atualizar o saldo dela
        //Adicionando o valor ao saldo na conta do usuário
        contaUsuarioEncontrada.ifPresent(contaUsuario ->
                contaUsuarioService.adicionarSaldo(contaUsuario.getId(), recebimentoETransacaoCriado.getValor()));

        return mapper.retornarDadosPagamento(recebimentoETransacaoCriado);

    }

    @Override
    @Transactional
    public PagamentosResponse criarPagamento(PagamentosRequest request) {

        if (request.tipoCategoria() != DESPESA) {
            throw new DadosInvalidosException("Para criar Pagamento, somente o tipo DESPESA é válido");
        }

        if (jaExisteUmPagamentoIgual(request.valor(), request.data(), request.descricao(), request.tipoCategoria(),
                request.subTipoCategoria())) {
            throw new JaExisteException("Já existe um pagamento criado idêntico");
        }

        //Criando os valores
        Pagamentos pagamentoETransacaoCriado =
                criarTransacao(request.valor(),
                        request.data(),
                        request.descricao(),
                        request.tipoCategoria(),
                        request.subTipoCategoria());



        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(request.idUsuarioCriado());
        Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(request.idContaUsuario());

        if (contaUsuarioEncontrada.isPresent() && !contaUsuarioEncontrada.get()
                .getPagamentosRelacionados().contains(pagamentoETransacaoCriado)) {
            try {
                pagamentoAssociation.associarPagamentoComConta(pagamentoETransacaoCriado.getId(),
                        contaUsuarioEncontrada.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar esse Pagamento a essa conta de usuário" + e.getCause());
            }
        }
        if (usuarioEncontrado.isPresent() && !usuarioEncontrado.get().getPagamentosRelacionados()
                .contains(pagamentoETransacaoCriado)) {
            try {
                pagamentoAssociation.associarPagamentoComUsuario(pagamentoETransacaoCriado.getId(), usuarioEncontrado.get().getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Não foi possível associar esse Pagamento a esse usuário" + e.getCause());
            }
        }

        //Buscando a conta desejada para atualizar o saldo dela
        if (contaUsuarioEncontrada.isPresent()){
            BigDecimal saldoConta = consultarSaldoDaConta(contaUsuarioEncontrada.get().getId());
            if (request.valor().compareTo(saldoConta) > 0){
                if (!contaUsuarioEncontrada.get().getTipoConta().equals(TiposContas.CONTA_CORRENTE)){
                    throw new DadosInvalidosException("O saldo não é suficiente para que seja realizado essa operação. " +
                            "O ùnico tipo de Conta que aceita saldo negativo é: " + TiposContas.CONTA_CORRENTE);
                }
            }
            //Subtraindo o valor ao saldo na conta do usuário
            contaUsuarioEncontrada.ifPresent(contaUsuario ->
                    contaUsuarioService.subtrairSaldo(contaUsuario.getId(), pagamentoETransacaoCriado.getValor()));
        }

        return mapper.retornarDadosPagamento(pagamentoETransacaoCriado);
    }

    @Override
    public Optional<PagamentosResponse> encontrarPagamentoOuRecebimentoPorid(Long id) {

        Pagamentos pagamentoOuRecebimentoEncontrado = (pagamentosRepository.findById(id)
                .orElseThrow(() -> new PagamentoNaoEncontrado("O Pagamento não foi encontrado!")));

        return Optional.ofNullable(mapper.retornarDadosPagamento(pagamentoOuRecebimentoEncontrado));
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentoOuRecebimentoPorData(LocalDate data) {

        List<Pagamentos> pagamentosEOuRecebimentosLocalizadosPelaData =
                pagamentosRepository.encontrarPagamentoPelaData(data);

        if (pagamentosEOuRecebimentosLocalizadosPelaData.isEmpty()) {
            throw new PagamentoNaoEncontrado("Não há nenhum pagamento ou recebimento na base de dados localizado nesta data");
        }

        return pagamentosEOuRecebimentosLocalizadosPelaData.stream().map(mapper::retornarDadosPagamento)
                .toList();
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPorUsuario(Long usuarioId) {

        Usuario usuarioLocalizado = usuarioRepository.findById(usuarioId).orElseThrow(()
                -> new UsuarioNaoEncontrado("Este Usuário não foi localizado "));

        List<Pagamentos> pagamentosDoUsuario = usuarioLocalizado.getPagamentosRelacionados();

        if (pagamentosDoUsuario.isEmpty()) {
            throw new PagamentoNaoEncontrado("Não há nenhum pagamento ou recebimento localizado deste Usuário");
        }

        return pagamentosDoUsuario.stream().map(mapper::retornarDadosPagamento).toList();
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentoOuRecebimentoPorTipo(TiposCategorias tipoCategoria) {

        return pagamentosRepository.findAll().stream().filter(pagamentos ->
                        pagamentos.getTiposCategorias() == tipoCategoria)
                .map(mapper::retornarDadosPagamento).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public PagamentosResponse atualizarPagamentoOuRecebimento(Long id, BigDecimal valor, LocalDate data, String descricao,
                                                 TiposCategorias tiposCategoria, SubTipoCategoria subTipo) {

        if (!dataEstaCorreta(data)) {
            throw new DadosInvalidosException("Por favor, digite uma data correta! " +
                    "entre a partir de hoje, e no máximo para 1 mês a partir de hoje");
        }
        if (!valorEstaCorreto(valor)) {
            throw new DadosInvalidosException("Por favor, digite um valor correto! ");
        }

        if (tiposCategoria != DESPESA && tiposCategoria != TiposCategorias.RECEITA) {
            throw new TiposCategoriasNaoEncontrado("Não foi encontrado o tipo de categoria corretamente");
        }

        if (jaExisteUmPagamentoIgual(valor, data, descricao, tiposCategoria, subTipo)) {
            throw new JaExisteException("Já existe uma transação criada com os mesmos valores");
        }

        Pagamentos pagamentoOuRecebimentoLocalizado = pagamentosRepository.findById(id).orElseThrow();

        pagamentoOuRecebimentoLocalizado.setTiposCategorias(tiposCategoria);
        pagamentoOuRecebimentoLocalizado.setData(data);
        pagamentoOuRecebimentoLocalizado.setValor(valor);
        pagamentoOuRecebimentoLocalizado.setDescricao(descricao);

        List<HistoricoTransacao> historicoDeTransacaoDesseObjeto = pagamentoOuRecebimentoLocalizado.getTransacoesRelacionadas();

       if (!historicoDeTransacaoDesseObjeto.isEmpty()){

           for (HistoricoTransacao historicoLocalizado: historicoDeTransacaoDesseObjeto){
               historicoLocalizado.setValor(valor);
               historicoLocalizado.setDescricao(descricao);
               historicoLocalizado.setData(data);
               historicoLocalizado.setTiposCategorias(tiposCategoria);
               historicoTransacaoRepository.save(historicoLocalizado);
           }
       }

       pagamentosRepository.save(pagamentoOuRecebimentoLocalizado);

       return mapper.retornarDadosPagamento(pagamentoOuRecebimentoLocalizado);
    }

    @Override
    public Page<PagamentosResponse> encontrarTodosPagamentos(Pageable pageable) {

        List<Pagamentos> todosPagamentos = pagamentosRepository.findAll();

        if (todosPagamentos.isEmpty()){
            throw new PagamentoNaoEncontrado("Nenhum pagamento foi encontrado no banco de dados");
        }

        Page<Pagamentos> pagamentosPage = new PageImpl<>(todosPagamentos);

        return pagamentosPage.map(mapper::retornarDadosPagamento);
    }

    @Override
    @Transactional
    public void deletarPagamento(Long id) {

        Pagamentos pagamentoOuRecebimentoLocalizado = pagamentosRepository.findById(id).orElseThrow();

        if (pagamentoOuRecebimentoLocalizado.getUsuarioRelacionado() != null){
            Usuario usuarioRelacionado = pagamentoOuRecebimentoLocalizado.getUsuarioRelacionado();
            try{
                pagamentoAssociation.desassociarPagamentoUsuario(pagamentoOuRecebimentoLocalizado.getId(), usuarioRelacionado.getId());
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Não foi possível desassociar pagamento de usuário. " + e.getCause());
            }
        }
        if (pagamentoOuRecebimentoLocalizado.getContaRelacionada() != null){
            ContaUsuario contaRelacionada = pagamentoOuRecebimentoLocalizado.getContaRelacionada();
            try{
                pagamentoAssociation.desassociarPagamentoConta(pagamentoOuRecebimentoLocalizado.getId(), contaRelacionada.getId());
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Não foi possível desassociar pagamento de conta de usuário. " + e.getCause());
            }
        }
        if (pagamentoOuRecebimentoLocalizado.getCategoriaRelacionada() != null){
            CategoriaFinanceira categoriaFinanceiraRelacionada = pagamentoOuRecebimentoLocalizado.getCategoriaRelacionada();
            try{
                pagamentoAssociation.desassociarPagamentoCategoria(pagamentoOuRecebimentoLocalizado.getId(), categoriaFinanceiraRelacionada.getId());
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Não foi possível desassociar pagamento de categoria financeira. " + e.getCause());
            }
        }
        List<HistoricoTransacao> historicoTransacaoRelacionado = pagamentoOuRecebimentoLocalizado.getTransacoesRelacionadas();
        if (pagamentoOuRecebimentoLocalizado.getTransacoesRelacionadas()!= null){
            for (HistoricoTransacao transacao: historicoTransacaoRelacionado){
                try{
                    pagamentoAssociation.desassociarPagamentoDeTransacao(pagamentoOuRecebimentoLocalizado.getId(), transacao.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException("Não foi possível desassociar pagamento de histórico de transação. " + e.getCause());
                }
                transacao.getPagamentosRelacionados().clear();
            }
        }
        pagamentosRepository.delete(pagamentoOuRecebimentoLocalizado);
    }

    @Override
    public BigDecimal consultarSaldoDaConta(Long contaId) {

        ContaUsuario contaLocalizada = contaUsuarioRepository.findById(contaId).orElseThrow(()->
                new ContaNaoEncontrada("Não foi encontrada nenhuma conta de usuário com essa id: " + contaId));

        return contaLocalizada.getSaldo();
    }


    @Override
    public boolean jaExisteUmPagamentoIgual(BigDecimal valor,LocalDate data,
                                            String descricao,TiposCategorias tipoCategoria,
                                            SubTipoCategoria subTipoCategoria) {

        boolean pagamentoLocalizado = pagamentosRepository.
                existTheSameData(valor,data,descricao,tipoCategoria);

        if (subTipoCategoria.tiposCategorias == tipoCategoria && pagamentoLocalizado){
            return true;
        }
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
