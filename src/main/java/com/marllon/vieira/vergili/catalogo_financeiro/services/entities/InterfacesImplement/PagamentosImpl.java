package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    @Override
    public Pagamentos criarNovoPagamento(PagamentosRequest pagamento) {

        //Criar novo pagamento
        Pagamentos novoPagamento = new Pagamentos();
        novoPagamento.setValor(pagamento.valor());
        novoPagamento.setData(pagamento.data());
        novoPagamento.setDescricao(pagamento.descricao());
        novoPagamento.setCategoria(pagamento.categoria());

        //Se o novo pagamento for diferente o tipo de categoria que deve ser informado
        if(novoPagamento.getCategoria()!= DESPESA && novoPagamento.getCategoria() != RECEITA) {
                throw new IllegalArgumentException("Por favor, digite aqui: (DESPESA ou RECEITA)");
        }

        //Verificar, se a data passada do pagamento, não for de datas passadas a partir do dia de hoje
        if(novoPagamento.getData().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data do pagamento não pode ser de dias passados");
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
    public Pagamentos encontrarPagamentoPorId(Long id) {

        //Se encontrar, retornar os dados do pagamento encontrado
        return pagamentosRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhum pagamento encontrado com essa id informada"));
    }

    @Override
    public List<Pagamentos> encontrarTodosPagamentos() {
        //Encontrar todos os pagamentos
        List<Pagamentos> todosPagamentos = pagamentosRepository.findAll();

        //Se a lista no banco de dados for vazia
        if(todosPagamentos.isEmpty()){
            throw new NoSuchElementException("Não existe nenhum valor de pagamentos no banco de dados");
        }

        //senao retornar a lista de todos eles
        return todosPagamentos;
    }

    @Override
    public Pagamentos atualizarPagamento(Long id, PagamentosRequest pagamento) {

        //Verificar se a id do pagamento já existe no banco de dados
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhum pagamento encontrado com essa id!"));

        //Se o pagamento existir..
        pagamentoEncontrado.setValor(pagamento.valor());
        pagamentoEncontrado.setData(pagamento.data());
        pagamentoEncontrado.setDescricao(pagamento.descricao());
        pagamentoEncontrado.setCategoria(pagamento.categoria());

        //Verificar, se a data passada do historico de transacao, não for de datas passadas a partir do dia de hoje
        if(pagamentoEncontrado.getData().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data do histórico de transação deve ser do mesmo dia de hoje, igual da " +
                    "data de pagamento");
        }

        //verificar se esse pagamento já não existe, comparando  data e descricao
        if(pagamentosRepository.existsByDataAndDescricao(pagamentoEncontrado.getData(),
                pagamentoEncontrado.getDescricao())){
            throw new IllegalArgumentException("Já existe um pagamento com a mesma descricao e data");
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
                NoSuchElementException("Nenhum pagamento foi encontrado com esta id informada"));
        //Remover o pagamento encontrado
        pagamentosRepository.delete(pagamentoEncontrado);
        //Retornar o valor do pagamento que foi deletado
        return pagamentoEncontrado;
    }

    @Override
    public List<Pagamentos> encontrarPagamentoPorValor(BigDecimal valor) {

        //Encontrar todos os pagamentos
        List<Pagamentos> pagamentosEncontrados = pagamentosRepository.findAll();

        //Se não encontrar nenhum pagamento.. retornar exceção
        if(pagamentosEncontrados.isEmpty()){
            throw new NoSuchElementException("Não há nenhum valor no banco de dados");
        }

        //Se encontrar pagamentos, verificar quanto ao valor deles
            return pagamentosRepository.encontrarPagamentoPelaValor(valor);
        }
    }

