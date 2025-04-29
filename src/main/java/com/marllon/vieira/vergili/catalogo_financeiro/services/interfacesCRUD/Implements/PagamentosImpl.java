package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.PagamentoMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.management.RuntimeErrorException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;

public class PagamentosImpl implements PagamentosService {

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private PagamentoMapper pagamentoMapper;

    @Autowired
    private PagamentosAssociation pagamentosAssociation;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Override
    public PagamentosResponse criarRecebimento(PagamentosRequest request) {


        if(request.data().isBefore(LocalDate.now()) ||
                request.data().isAfter(LocalDate.now().plusMonths(1).withDayOfMonth(1))){
            throw new DadosInvalidosException("Por favor, digite uma data válida, de até 1 mês a frente!");
        }

        if(request.tipoCategoria() == null || sePagamentoForDespesa()){
            throw new DadosInvalidosException("Para criar recebimento, por favor, é necessário associar ao tipo RECEITA");
        }

        if(jaExisteUmPagamentoIgual(request)){
            throw new IllegalArgumentException("Não é possível criar novo recebimento, pois ja existe um recebimento igual criado!");
        }


        Pagamentos novoRecebimento;
        try {
            //Criar um novo valor
            novoRecebimento = new Pagamentos();
            novoRecebimento.setValor(request.valor());
            novoRecebimento.setData(request.data());
            novoRecebimento.setDescricao(request.descricao());
            novoRecebimento.setCategoria(request.tipoCategoria());
            novoRecebimento.setSubTipo(request.subTipoCategoria());
        }catch (RuntimeErrorException e){
            throw new RuntimeErrorException(new Error("Não foi possível criar o novo pagamento " + e.getTargetError()));
        }

        HistoricoTransacao novoHistorico;
        try{
            //Criar também um novo histórico de transação automaticamente
            novoHistorico = new HistoricoTransacao();
            novoHistorico.setValor(novoRecebimento.getValor());
            novoHistorico.setData(novoRecebimento.getData());
            novoHistorico.setDescricao(novoRecebimento.getDescricao());
            novoHistorico.setCategorias(novoRecebimento.getCategoria());
            novoHistorico.setSubTipo(novoRecebimento.getSubTipo());
        } catch (RuntimeException e) {
            throw new RuntimeErrorException(new Error("Não foi possível criar um histórico de transação deste novo pagamento " + e.getCause()));
        }


        //Salvar o pagamento e o histórico de pagamento
        pagamentosRepository.save(novoRecebimento);
        historicoTransacaoRepository.save(novoHistorico);



        return pagamentoMapper.retornarDadosPagamento(novoRecebimento);
    }

    @Override
    public PagamentosResponse criarPagamento(PagamentosRequest request) {

        if(request.data().isBefore(LocalDate.now()) ||
                request.data().isAfter(LocalDate.now().plusMonths(1).withDayOfMonth(1))){
            throw new DadosInvalidosException("Por favor, digite uma data válida!");
        }

        if(request.tipoCategoria() == null || sePagamentoForReceita()){
            throw new DadosInvalidosException("Para criar recebimento, por favor, é necessário associar ao tipo DESPESA");
        }

        if(jaExisteUmPagamentoIgual(request)){
            throw new IllegalArgumentException("Não é possível criar novo recebimento, pois ja existe um recebimento igual criado!");
        }


        Pagamentos novoPagamento;
        try {
            //Criar um novo valor
            novoPagamento = new Pagamentos();
            novoPagamento.setValor(request.valor());
            novoPagamento.setData(request.data());
            novoPagamento.setDescricao(request.descricao());
            novoPagamento.setCategoria(request.tipoCategoria());
            novoPagamento.setSubTipo(request.subTipoCategoria());
        }catch (RuntimeErrorException e){
            throw new RuntimeErrorException(new Error("Não foi possível criar o novo pagamento " + e.getTargetError()));
        }

        HistoricoTransacao novoHistorico;
        try{
            //Criar também um novo histórico de transação automaticamente
            novoHistorico = new HistoricoTransacao();
            novoHistorico.setValor(novoPagamento.getValor());
            novoHistorico.setData(novoPagamento.getData());
            novoHistorico.setDescricao(novoPagamento.getDescricao());
            novoHistorico.setCategorias(novoPagamento.getCategoria());
            novoHistorico.setSubTipo(novoPagamento.getSubTipo());
        } catch (RuntimeException e) {
            throw new RuntimeErrorException(new Error("Não foi possível criar um histórico de transação deste novo pagamento " + e.getCause()));
        }


        //Salvar o pagamento e o histórico de pagamento
        pagamentosRepository.save(novoPagamento);
        historicoTransacaoRepository.save(novoHistorico);



        return pagamentoMapper.retornarDadosPagamento(novoPagamento);
    }

    @Override
    public PagamentosResponse encontrarPagamentoPorid(Long id) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(()
                ->new PagamentoNaoEncontrado("Nenhum pagamento foi encontrado com essa id!"));

        return pagamentoMapper.retornarDadosPagamento(pagamentoEncontrado);
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentoPorData(LocalDate data) {

        List<Pagamentos> pagamentoEncontrado = pagamentosRepository.encontrarPagamentoPelaData(data);

        if(pagamentoEncontrado == null){
            throw new PagamentoNaoEncontrado("Não foi encontrado nenhum pagamento com essa data informada");
        }

        return pagamentoEncontrado.stream().map(pagamentoMapper::retornarDadosPagamento).collect(Collectors.toList());
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPelaCategoriaCriada(Long categoriaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId).orElseThrow(()
                -> new CategoriaNaoEncontrada("A id da categoria informada não foi encontrada"));

        List<Pagamentos> pagamentosEncontrados = categoriaEncontrada.getPagamentosRelacionados();

        return pagamentosEncontrados.stream().map(pagamentoMapper::retornarDadosPagamento).
                collect(Collectors.toList());
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPorUsuario(Long usuarioId) {

        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId).orElseThrow(()
                -> new UsuarioNaoEncontrado("Nenhum usuário foi encontrado com essa id"));

        List<Pagamentos> pagamentosEncontrados = usuarioEncontrado.getPagamentosRelacionados();

        return pagamentosEncontrados.stream().map(pagamentoMapper::retornarDadosPagamento).collect(Collectors.toList());
    }

    @Override
    public List<PagamentosResponse> encontrarPagamentosPorTipo(TiposCategorias tipoCategoria) {

        if(tipoCategoria.name().equalsIgnoreCase(Arrays.toString(TiposCategorias.values()))){
            throw new TiposCategoriasNaoEncontrado("Esse tipode categoria não foi encontrado, tente novamente");
        }

        List<Pagamentos> pagamentosEncontrados;
        try{
            pagamentosEncontrados = pagamentosRepository.encontrarPagamentoPelaCategoria(tipoCategoria);


        }catch (RuntimeException e){
            throw new PagamentoNaoEncontrado("Nenhum pagamento foi encontrado nessa categoria");
        }

        return pagamentosEncontrados.stream().map(pagamentoMapper::retornarDadosPagamento).collect(Collectors.toList());
    }

    @Override
    public PagamentosResponse atualizarPagamento(Long id, PagamentosRequest request) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(()
                -> new PagamentoNaoEncontrado("Não foi encontrado nenhum pagamento com essa id"));

        if(request.data().isBefore(LocalDate.now()) ||
                request.data().isAfter(LocalDate.now().plusMonths(1).withDayOfMonth(1))){
            throw new DadosInvalidosException("Por favor, digite uma data válida até 1 mês a frente!");
        }

        if(request.tipoCategoria() == null || sePagamentoForReceita()){
            throw new DadosInvalidosException("Para criar recebimento, por favor, é necessário associar ao tipo DESPESA");
        }

        if(jaExisteUmPagamentoIgual(request)){
            throw new IllegalArgumentException("Não é possível criar novo recebimento, pois ja existe um recebimento igual criado!");
        }



        try {
            if(pagamentoEncontrado != null){
                //alterar os valores do pagamento
                pagamentoEncontrado.setValor(request.valor());
                pagamentoEncontrado.setData(request.data());
                pagamentoEncontrado.setDescricao(request.descricao());
                pagamentoEncontrado.setCategoria(request.tipoCategoria());
                pagamentoEncontrado.setSubTipo(request.subTipoCategoria());
            }

        }catch (RuntimeErrorException e){
            throw new RuntimeErrorException(new Error("Não foi possível atualizar o novo pagamento " + e.getTargetError()));
        }

            //Atualizar também do lado do histórico de transação
        assert pagamentoEncontrado != null;
        List<HistoricoTransacao> historicoAtualizado = pagamentoEncontrado.getTransacoesRelacionadas();

        if(historicoAtualizado.isEmpty()){
            throw new HistoricoTransacaoNaoEncontrado("Não foi encontrado nenhum histórico de transação " +
                    "para ser atualizado junto com esse pagamento");
        }

        try{
            for(HistoricoTransacao historicoPercorrido: historicoAtualizado){
                historicoPercorrido.setValor(pagamentoEncontrado.getValor());
                historicoPercorrido.setData(pagamentoEncontrado.getData());
                historicoPercorrido.setDescricao(pagamentoEncontrado.getDescricao());
                historicoPercorrido.setCategorias(pagamentoEncontrado.getCategoria());
                historicoPercorrido.setSubTipo(pagamentoEncontrado.getSubTipo());

                historicoTransacaoRepository.save(historicoPercorrido);
            }
        }catch (RuntimeException e) {
            throw new RuntimeErrorException(new Error("Não foi possível alterar do lado do histórico de transacao" +
                    "" + e.getCause()));
        }
        //Salvar o pagamento também
        pagamentosRepository.save(pagamentoEncontrado);

        return pagamentoMapper.retornarDadosPagamento(pagamentoEncontrado);

    }

    @Override
    public Page<PagamentosResponse> encontrarTodosPagamentos(Pageable pageable) {

        Page<Pagamentos>
                todosPagamentosEncontrados = pagamentosRepository.findAll(pageable);

        if(todosPagamentosEncontrados.isEmpty()){
            throw new CategoriaNaoEncontrada("Não há nenhuma categoria salva na base de dados");
        }
        List<PagamentosResponse> pagamentosResponses=
                todosPagamentosEncontrados.stream()
                        .map(pagamentos -> new
                                PagamentosResponse(pagamentos.getId(),
                                pagamentos.getValor(),
                                pagamentos.getData(),
                                pagamentos.getDescricao(),
                                pagamentos.getCategoria())).toList();

        return new PageImpl<>(pagamentosResponses);

    }

    @Override
    public void deletarPagamento(Long id) {

        Pagamentos pagamentoSerRemovido = pagamentosRepository.findById(id).orElseThrow(() ->
                new ContaNaoEncontrada("Não foi encontrado nenhum pagamento com essa id informada"));

        try{
            pagamentosAssociation.desassociarPagamentoConta(id, pagamentoSerRemovido.getContaRelacionada().getId());
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar do tipo de conta ");
        }

        try{
            pagamentosAssociation.desassociarPagamentoUsuario(id, pagamentoSerRemovido.getUsuarioRelacionado().getId());

        } catch (DesassociationErrorException e) {
            throw new DesassociationErrorException("Erro ao desassociar de Usuario:  " + e.getMessage());
        }

        try{
            for(CategoriaFinanceira categoriasEncontradas: pagamentoSerRemovido.getCategoriasRelacionadas()){
                pagamentosAssociation.desassociarPagamentoCategoria(id, categoriasEncontradas.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Categorias Financeiras:  " + e.getMessage());
        }

        try{
            for(HistoricoTransacao historicoTransacaoEncontrado: pagamentoSerRemovido.getTransacoesRelacionadas()){
                pagamentosAssociation.desassociarPagamentoDeTransacao(id, historicoTransacaoEncontrado.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Historico de transações:  " + e.getMessage());
        }


        //Remover a conta encontrada
        pagamentosRepository.delete(pagamentoSerRemovido);
    }

    @Override
    public BigDecimal consultarValorPagamento(Long contaId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(contaId).orElseThrow(() ->
                new PagamentoNaoEncontrado("Não foi encontrado nenhum pagamento com essa id informada"));

        return pagamentoEncontrado.getValor();
    }

    @Override
    public boolean sePagamentoForDespesa() {

        //Verificar se o tipo é despesa
        Optional<TiposCategorias> tiposCategorias =
                TiposCategorias.buscarCategoriasPeloNome(DESPESA.name());
        //Se tiver presente, retornar
        return tiposCategorias.isPresent();
    }

    @Override
    public boolean sePagamentoForReceita() {

        //Verificar se o tipo é despesa
        Optional<TiposCategorias> tiposCategorias =
                TiposCategorias.buscarCategoriasPeloNome(RECEITA.name());
        //Se tiver presente, retornar
        return tiposCategorias.isPresent();
    }


    @Override
    public boolean jaExisteUmPagamentoIgual(PagamentosRequest pagamentoRequest) {
        return pagamentosRepository.findAll().stream().anyMatch(pagamento ->
                pagamento.getValor().equals(pagamentoRequest.valor()) &&
                        pagamento.getData().equals(pagamentoRequest.data()) &&
                        pagamento.getDescricao().equals(pagamentoRequest.descricao())&&
                        pagamento.getCategoria().equals(pagamentoRequest.tipoCategoria())&&
                pagamento.getSubTipo().equals(pagamentoRequest.subTipoCategoria()));
    }
}
