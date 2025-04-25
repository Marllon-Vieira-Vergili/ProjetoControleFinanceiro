package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.PagamentoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;

@Service
public class PagamentosImpl implements PagamentosService {


    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public Pagamentos criarNovoRecebimento(PagamentosRequest recebimento) {

        //Criar novo recebimento
        Pagamentos novoRecebimento = new Pagamentos();
        novoRecebimento.setValor(recebimento.valor());
        novoRecebimento.setData(recebimento.data());
        novoRecebimento.setDescricao(recebimento.descricao());
        novoRecebimento.setCategoria(recebimento.categoria());

        //Verificar se o  usuário não digitou  valores nulos ou vazios
        if(novoRecebimento.getValor() == null || novoRecebimento.getValor().compareTo(BigDecimal.ZERO) <= 0 ||
                novoRecebimento.getDescricao() == null || novoRecebimento.getDescricao().isEmpty()){
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        }

        //Se o novo pagamento for diferente o tipo de categoria que deve ser informado
        if(novoRecebimento.getCategoria()!= RECEITA ) {
            throw new TiposCategoriasNaoEncontrado("Para criar um recebimento, só pode ser informado o tipo: RECEITA");
        }

        //Verificar, se a data passada do recebimento, não for de datas passadas a partir do dia de hoje
        if(novoRecebimento.getData().isBefore(LocalDate.now())){
            throw new DateTimeException("A data do pagamento não pode ser de dias passados");
        }

        //verificar se esse recebimento já não existe, comparando  data e descricao
        if(pagamentosRepository.existsByDataAndDescricao(novoRecebimento.getData(),
                novoRecebimento.getDescricao())){
            throw new IllegalArgumentException("Já existe um recebimento com a mesma descricao e data");
        }

        //Salvar o novo recebimento
        pagamentosRepository.save(novoRecebimento);

        return novoRecebimento;
    }


    @Override
    public Pagamentos criarNovoPagamento(PagamentosRequest pagamento) {

        //Criar novo pagamento
        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setValor(pagamento.valor());
        novoPagamento.setData(pagamento.data());
        novoPagamento.setDescricao(pagamento.descricao());
        novoPagamento.setCategoria(pagamento.categoria());

        //Verificar se o  usuário não digitou  valores nulos ou vazios
        if(novoPagamento.getValor() == null || novoPagamento.getValor().compareTo(BigDecimal.ZERO) <= 0 ||
                novoPagamento.getDescricao() == null || novoPagamento.getDescricao().isEmpty()){
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        }

        //Se o novo pagamento for diferente o tipo de categoria que deve ser informado
        if(novoPagamento.getCategoria()!= DESPESA ) {
                throw new TiposCategoriasNaoEncontrado("Para criar um pagamento, só pode ser de despesa");
        }

        //Verificar, se a data passada do pagamento, não for de datas passadas a partir do dia de hoje
        if(novoPagamento.getData().isBefore(LocalDate.now())){
            throw new DateTimeException("A data do pagamento não pode ser de dias passados");
        }

        //verificar se esse pagamento já não existe, comparando  data e descricao
        if(pagamentosRepository.existsByDataAndDescricao(novoPagamento.getData(),
                novoPagamento.getDescricao())){
            throw new IllegalArgumentException("Já existe um pagamento com a mesma descricao e data");
        }

        //salvar o novo pagamento
        pagamentosRepository.save(novoPagamento);

        //Retornar o novo pagamento criado
        return novoPagamento;
    }



    @Override
    public void processarPagamento(PagamentosRequest pagamento, ContaUsuarioRequest contaUsuario) {

        // Buscar conta e validar existência
        ContaUsuario contaEncontrada;
        try {
            contaEncontrada = contaUsuarioService.encontrarContaPorNome(contaUsuario.nome());
        } catch (RuntimeException e) {
            throw new ContaNaoEncontrada("Não foi possível encontrar nenhuma conta com esse nome informado"
                    + e.getMessage());
        }

        //Instanciar um valor
        BigDecimal valor = pagamento.valor();
        //pegar a categoria do parametro, e trocar as condições
        switch (pagamento.categoria()) {
            case DESPESA -> {
                if (contaEncontrada.getSaldo().compareTo(valor) < 0) {
                    contaEncontrada.subtrairSaldo(contaEncontrada, valor);
                }
            }
            case RECEITA -> contaEncontrada.adicionarSaldo(contaEncontrada, valor);
            default -> throw new TiposCategoriasNaoEncontrado("Categoria não foi encontrada");
        }
    }



    @Override
    public Pagamentos encontrarPagamentoPorId(Long id) {

        //Se encontrar, retornar os dados do pagamento encontrado
        return pagamentosRepository.findById(id).orElseThrow(() ->
                new PagamentoNaoEncontrado("Nenhum pagamento encontrado com essa id informada"));
    }

    @Override
    public List<Pagamentos> encontrarTodosPagamentos() {
        //Encontrar todos os pagamentos
        List<Pagamentos> todosPagamentos = pagamentosRepository.findAll();

        //Se a lista no banco de dados for vazia
        if(todosPagamentos.isEmpty()){
            throw new PagamentoNaoEncontrado("Não existe nenhum valor de pagamentos no banco de dados");
        }

        //senao retornar a lista de todos eles
        return todosPagamentos;
    }

    @Override
    public Pagamentos atualizarPagamento(Long id, PagamentosRequest pagamento) {

        //Verificar se a id do pagamento já existe no banco de dados
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(() ->
                new PagamentoNaoEncontrado("Nenhum pagamento encontrado com essa id!"));

        //Se o pagamento existir..
        pagamentoEncontrado.setValor(pagamento.valor());
        pagamentoEncontrado.setData(pagamento.data());
        pagamentoEncontrado.setDescricao(pagamento.descricao());
        pagamentoEncontrado.setCategoria(pagamento.categoria());

        //Verificar se o  usuário não digitou  valores nulos ou vazios
        if(pagamentoEncontrado.getValor() == null || pagamentoEncontrado.getValor().compareTo(BigDecimal.ZERO) <= 0 ||
                pagamentoEncontrado.getDescricao() == null || pagamentoEncontrado.getDescricao().isEmpty()){
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        }

        //Verificar, se a data passada do historico de transacao, não for de datas passadas a partir do dia de hoje
        if(pagamentoEncontrado.getData().isBefore(LocalDate.now())){
            throw new DateTimeException("A data do histórico de transação deve ser do mesmo dia de hoje, igual da " +
                    "data de pagamento");
        }

        //verificar se esse pagamento já não existe, comparando  data e descricao
        if(pagamentosRepository.existsByDataAndDescricao(pagamentoEncontrado.getData(),
                pagamentoEncontrado.getDescricao())){
            throw new JaExisteException("Já existe um pagamento com a mesma descricao e data");
        }
        //Salvar o pagamento atualizado
        pagamentosRepository.save(pagamentoEncontrado);

        //Retornar os dados do novo pagamento
        return pagamentoEncontrado;
    }


    @Override
    public Pagamentos removerPagamentoPorId(Long id) {

        //encontrar o pagamento pela id
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(() -> new
                PagamentoNaoEncontrado("Nenhum pagamento foi encontrado com esta id informada"));
        //Remover o pagamento encontrado
        pagamentosRepository.delete(pagamentoEncontrado);
        //Retornar o valor do pagamento que foi deletado
        return pagamentoEncontrado;
    }

    @Override
    public void salvarNovoPagamento(Pagamentos novoPagamento) {
        pagamentosRepository.save(novoPagamento);
    }

    @Override
    public List<Pagamentos> encontrarPagamentoPorValor(BigDecimal valor) {

        //Encontrar todos os pagamentos
        List<Pagamentos> pagamentosEncontrados = pagamentosRepository.findAll();

        //Se não encontrar nenhum pagamento.. retornar exceção
        if(pagamentosEncontrados.isEmpty()){
            throw new PagamentoNaoEncontrado("Não há nenhum valor no banco de dados");
        }

        //Se encontrar pagamentos, verificar quanto ao valor deles
            return pagamentosRepository.encontrarPagamentoPelaValor(valor);
        }
    }

