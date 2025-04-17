package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.TransacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;


@Service
public class HistoricoTransacaoImpl implements TransacoesService {


    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Override
    public HistoricoTransacaoResponse criarNovoHistoricoTransacao(HistoricoTransacaoRequest historicoTransacao){

        //Instanciar um novo histórico de transacao
        HistoricoTransacao novoHistorico = new HistoricoTransacao();
        novoHistorico.setValor(historicoTransacao.valor());
        novoHistorico.setData(historicoTransacao.data());
        novoHistorico.setDescricao(historicoTransacao.descricao());
        novoHistorico.setCategorias(historicoTransacao.categoria());

        //Se o novo histórico de transacao for diferente o tipo de categoria que deve ser informado
        if(novoHistorico.getCategorias()!= DESPESA && novoHistorico.getCategorias() != RECEITA) {
            throw new IllegalArgumentException("Por favor, digite aqui: (DESPESA ou RECEITA)");
        }

        //Verificar, se a data passada do historico de transacao, não for de datas passadas a partir do dia de hoje
        if(novoHistorico.getData().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data do histórico de transação deve ser do mesmo dia de hoje, igual da " +
                    "data de pagamento");
        }

        //verificar se esse histórico já não existe, comparando  data e descricao
        if(historicoTransacaoRepository.existsByDataAndDescricao(novoHistorico.getData(),
                novoHistorico.getDescricao())){
            throw new IllegalArgumentException("Já existe um histórico de transação com a mesma descrição e data");
        }

        //Salvar o histórico de transação
        historicoTransacaoRepository.save(novoHistorico);

        //Retornar o histórico de transação criado
        return new HistoricoTransacaoResponse(novoHistorico.getId(), novoHistorico.getValor(),novoHistorico.getData(),
                novoHistorico.getDescricao(), novoHistorico.getCategorias());
    }

    @Override
    public HistoricoTransacaoResponse encontrarTransacaoPorId(Long id) {
        //Encontrar a transacao pela id
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma transacao encontrada com a ID informada"));

        //Se não tiver nenhuma transacao no banco de dados
        if(historicoTransacaoRepository.findAll().isEmpty()){
            throw  new NullPointerException("Não há nenhuma transacao no banco de dados, ele está vazio");
        }
        //Retornar os dados da id encontrada
        return new HistoricoTransacaoResponse(transacaoEncontrada.getId(),transacaoEncontrada.getValor(), transacaoEncontrada.getData(),
                transacaoEncontrada.getDescricao(),transacaoEncontrada.getCategorias());

    }

    @Override
    public List<HistoricoTransacaoResponse> encontrarTodasTransacoes() {

        //Encontrar todas as transacoes
        List<HistoricoTransacao> todasTransacoes = historicoTransacaoRepository.findAll();

        //Se a lista no banco de dados for vazia
        if(todasTransacoes.isEmpty()){
            throw new NoSuchElementException("Não existe nenhum valor de transacoes no banco de dados");
        }

        //senao retornar a lista de todos eles
        return todasTransacoes.stream()
                .map(historicoTransacao -> new HistoricoTransacaoResponse(
                        historicoTransacao.getId(),
                        historicoTransacao.getValor(),
                        historicoTransacao.getData(),
                        historicoTransacao.getDescricao(),
                        historicoTransacao.getCategorias()))
                .toList();
    }

    @Override
    public HistoricoTransacaoResponse atualizarHistoricoTransacao(Long id, HistoricoTransacaoRequest historicoTransacao) {

        //Verificar se a id do historico de transacao já existe no banco de dados
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhum historico de transacao encontrado com essa id!"));

        //Se o historico existir..
        transacaoEncontrada.setValor(historicoTransacao.valor());
        transacaoEncontrada.setData(historicoTransacao.data());
        transacaoEncontrada.setDescricao(historicoTransacao.descricao());
        transacaoEncontrada.setCategorias(historicoTransacao.categoria());

        //verificar se esse historico de transacao já não existe, comparando  data e descricao
        if(historicoTransacaoRepository.existsByDataAndDescricao(transacaoEncontrada.getData(),
                transacaoEncontrada.getDescricao())){
            throw new IllegalArgumentException("Já existe um historico de transacao com a mesma descricao e data");
        }

        //Verificar, se a data passada do historico de transacao, não for de datas passadas a partir do dia de hoje
        if(transacaoEncontrada.getData().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data do histórico de transação deve ser do mesmo dia de hoje, igual da " +
                    "data de pagamento");
        }
        //Salvar o historico de transacao atualizado
        historicoTransacaoRepository.save(transacaoEncontrada);

        //Retornar os dados do novo historico transacao
        return new HistoricoTransacaoResponse(transacaoEncontrada.getId(),transacaoEncontrada.getValor(), transacaoEncontrada.getData(),
                transacaoEncontrada.getDescricao(),transacaoEncontrada.getCategorias());
    }

    @Override
    public HistoricoTransacaoResponse removerTransacaoPorId(Long id) {

        //encontrar o pagamento pela id
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(id).orElseThrow(() -> new
                NoSuchElementException("Nenhum histórico de transação foi encontrado com esta id informada"));

        //Remover o histórico de transação encontrado
        historicoTransacaoRepository.delete(transacaoEncontrada);

        //Retornar o valor do pagamento que foi deletado
        return new HistoricoTransacaoResponse(transacaoEncontrada.getId(),transacaoEncontrada.getValor(), transacaoEncontrada.getData(),
                transacaoEncontrada.getDescricao(),transacaoEncontrada.getCategorias());
    }
}
