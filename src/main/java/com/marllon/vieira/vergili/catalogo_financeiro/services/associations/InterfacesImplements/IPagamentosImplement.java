package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.PagamentosRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.CategoriaFinanceiraService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;

@Service
public class IPagamentosImplement implements IPagamentos {

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento) {

        //Criar o pagamento
        Pagamentos novoPagamentoCriado = pagamentosService.criarNovoPagamento(novoPagamento.pagamento());

        //Se não for possivel criar.
        if(novoPagamentoCriado == null){
            throw new IllegalArgumentException("Não foi possível criar um novo pagamento");
        }

        //Associar o pagamento a uma conta
            ContaUsuario contaAssociada =
                    contaUsuarioService.encontrarContaPorNome(novoPagamento.nomeContaAssociada());
        if(contaAssociada.getNome().equalsIgnoreCase(novoPagamento.nomeContaAssociada())){
            if(contaAssociada.getTipoConta().name().equals(TiposContas.todosTiposValidos())){
                //Associar o novo pagamento a conta indicada
                novoPagamentoCriado.associarPagamentoComConta(contaAssociada);
            }else{
                throw new IllegalArgumentException("Não foi possível associar esse pagamento a essa conta, " +
                        "provavelmente conta não existe");
            }
        }

        //Associar o pagamento ao usuário desta conta
        if(novoPagamentoCriado.getUsuarioRelacionado() == null){
            List<Usuario> usuarioEncontrado = usuarioService.encontrarTodosUsuarios();
            usuarioEncontrado.stream().distinct().map(usuario -> usuario.getContasRelacionadas()
                    .equals(contaAssociada)).toList();
        }


        //Se a categoria não tiver sido criada, criar uma nova categoria
        if(novoPagamentoCriado.getCategoria() == null){

        }
        //Associar o pagamento a uma categoria
        if(novoPagamento.pagamento().categoria().equals(RECEITA)){
            novoPagamentoCriado.setCategoria(novoPagamento.pagamento().categoria());
        }else{
            novoPagamentoCriado.setCategoria(novoPagamento.pagamento().categoria());
        }


            List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses =
                    novoPagamentoCriado.getCategoriasRelacionadas().stream().map(categoriaFinanceira ->
                            new CategoriaFinanceiraResponse(categoriaFinanceira.getId(), categoriaFinanceira.getTiposCategorias(),
                                    categoriaFinanceira.getSubTipo())).toList();

        //Criar um arraylist para pegar os valores dentro do IF para retornar em DTO
        List<HistoricoTransacaoResponse> historicoTransacaoResponse = new ArrayList<>();


        //gerar automaticamente um histórico de transação a partir desse pagamento
        if(novoPagamentoCriado.getTransacoesRelacionadas().isEmpty()){

            HistoricoTransacao novoHistoricoTransacao = new HistoricoTransacao();
            novoHistoricoTransacao.setValor(novoPagamentoCriado.getValor());
            novoHistoricoTransacao.setData(novoPagamentoCriado.getData());
            novoHistoricoTransacao.setDescricao(novoPagamentoCriado.getDescricao());
            novoHistoricoTransacao.setCategorias(novoPagamentoCriado.getCategoria());

            //Associar o pagamento novo a este historico de transação
            novoPagamentoCriado.associarPagamentoATransacao(novoHistoricoTransacao);

            historicoTransacaoResponse = Collections.singletonList(new HistoricoTransacaoResponse
                    (novoHistoricoTransacao.getId(), novoHistoricoTransacao.getValor(),
                            novoHistoricoTransacao.getData(), novoHistoricoTransacao.getDescricao(),
                            novoHistoricoTransacao.getCategorias()));
        }

        //Salvar os valores e associacoes
        pagamentosRepository.save(novoPagamentoCriado);

        //Mapear os valores em valores DTO
        PagamentosResponse pagamentosResponse = new PagamentosResponse(novoPagamentoCriado.getId(),
                novoPagamentoCriado.getValor(),novoPagamentoCriado.getData(), novoPagamentoCriado.getDescricao(),
                novoPagamentoCriado.getCategoria());

        ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(contaAssociada.getId(),
                contaAssociada.getNome(), contaAssociada.getSaldo());


        //Retornar o DTO ao usuário das associações realizadas
        return new PagamentoAssociationResponse(Collections.singletonList(pagamentosResponse),
                historicoTransacaoResponse, categoriaFinanceiraResponses,contaUsuarioResponse);


    }


    @Override
    public PagamentoAssociationResponse encontrarPagamentoAssociadoPorId(Long id) {
        return null;
    }

    @Override
    public List<PagamentoAssociationResponse> encontrarTodosPagamentosAssociados() {
        return List.of();
    }

    @Override
    public PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest
            pagamentoAtualizado) {
        return null;
    }

    @Override
    public PagamentoAssociationResponse removerPagamentoAssociadoPelaId(Long id) {
        return null;
    }
}

