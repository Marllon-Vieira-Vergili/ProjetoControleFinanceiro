package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.CategoriaFinanceiraAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.CategoriaFinanceiraAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.ICategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ICategoriaImplement implements ICategoria {


    @Autowired
    private CategoriaFinanceiraService categoriaFinanceiraService;


    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private TransacoesService transacoesService;

    @Override
    public CategoriaFinanceiraAssociationResponse criarEAssociarCategoria(CategoriaFinanceiraAssociationRequest novaCategoria) {

        try{
            //Criar nova categoria
            CategoriaFinanceira categoriaCriada =
                    categoriaFinanceiraService.criarCategoria(novaCategoria.categoria());

            //Verificar se a subcategoria foi digitada
            if(categoriaCriada.getTiposCategorias() == null){
                throw new IllegalArgumentException("Por favor, digite um tipo de categoria, se é receita ou despesa!");
            }

            //Associar a categoria a um novo pagamento
            if(categoriaCriada.getPagamentosRelacionados().isEmpty()){
                List<Pagamentos> pagamentoEncontrado = pagamentosService.encontrarPagamentoPorValor
                        (novaCategoria.valorPagamento());


                    //Associar a categoria ao pagamento
                categoriaCriada.associarCategoriaComPagamentos(pagamentoEncontrado.stream().filter(pagamentos ->
                        pagamentos.getValor().equals(novaCategoria.valorPagamento())).findFirst().orElseThrow(()
                        -> new NoSuchElementException
                        ("Nenhum elemento foi encontrado com esse valor, para associar com a categoria")));
                } else{
                    throw new IllegalArgumentException("Erro ao associar a categoria: " + categoriaCriada + " com pagamento informado");
                }

            //Associar a categoria a um novo histórico de transacao
            if(categoriaCriada.getTransacoesRelacionadas().isEmpty()){
                List<HistoricoTransacao> transacaoEncontrada = transacoesService.encontrarTransacaoPorValor
                        (novaCategoria.valorTransacao());

                //Verificar se o valor do pagamento e da transação são iguais
                if(novaCategoria.valorTransacao().equals(novaCategoria.valorPagamento())){
            //Associar a categoria ao Historico de transação
                    categoriaCriada.associarCategoriaComTransacoes(transacaoEncontrada.stream()
                            .filter(historicoTransacao ->
                                    transacaoEncontrada.equals(novaCategoria.valorTransacao()))
                            .findFirst().orElseThrow(()->
                                    new NoSuchElementException("Nenhum elemento com esse valor, foi encontrado para associar com a categoria!")));
                    }
                }
                else{
                throw new IllegalArgumentException("Erro ao associar a categoria: " + categoriaCriada + " com historico de transação informado");
            }
            
            //Associar a categoria a um novo usuário
            if(categoriaCriada.getUsuarioRelacionado().getNome().equals(novaCategoria.nomeConta())){
                Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPorNome(novaCategoria.nomeUsuario());

                //Associar a categoria ao usuário encontrado
                categoriaCriada.associarCategoriaComUsuario(usuarioEncontrado);
            }else{
                throw new IllegalArgumentException("Erro ao associar a categoria: " + categoriaCriada + " com esse usuário informado");
            }

            //Associar a categoria a uma nova conta
            if(categoriaCriada.getContaRelacionada().getNome().equals(novaCategoria.nomeConta())){
                ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorNome(novaCategoria.nomeConta());

                //Associar a categoria a essa conta informada
                categoriaCriada.associarCategoriaComConta(contaEncontrada);
            }else{
                throw new IllegalArgumentException("Erro ao associar a categoria: " + categoriaCriada + " com essa conta informada");
            }

            //Mapear os valores encontrados em formato DTO e enviar para retornar o valor
            List<CategoriaFinanceiraResponse> categoriaResponse = Collections.singletonList
                    (new CategoriaFinanceiraResponse(categoriaCriada.getId(), categoriaCriada.getTiposCategorias(),
                            categoriaCriada.getSubTipo()));
            List<PagamentosResponse> pagamentosResponses = categoriaCriada.
                    getPagamentosRelacionados().stream().map(pagamentos -> new
                            PagamentosResponse(pagamentos.getId(), pagamentos.getValor(),pagamentos.getData(),
                            pagamentos.getDescricao(), pagamentos.getCategoria())).toList();

            List<HistoricoTransacaoResponse> transacaoResponses = categoriaCriada.getTransacoesRelacionadas()
                    .stream().map(historicoTransacao -> new HistoricoTransacaoResponse
                            (historicoTransacao.getId(), historicoTransacao.getValor(),historicoTransacao.getData(),
                                    historicoTransacao.getDescricao(), historicoTransacao.getCategorias())).toList();
            UsuarioResponse usuarioResponse = new UsuarioResponse(categoriaCriada.getUsuarioRelacionado().getId(),
                    categoriaCriada.getUsuarioRelacionado().getNome(),categoriaCriada.getUsuarioRelacionado().getEmail()
                    ,categoriaCriada.getUsuarioRelacionado().getTelefone());
            ContaUsuarioResponse contaUsuarioResponse = new ContaUsuarioResponse(categoriaCriada.getContaRelacionada().
                    getId(),categoriaCriada.getContaRelacionada().getNome(),categoriaCriada.getContaRelacionada().
                    getSaldo());

            return new CategoriaFinanceiraAssociationResponse(categoriaResponse,pagamentosResponses,
                    transacaoResponses,contaUsuarioResponse,usuarioResponse);
        }catch (Exception e){
            throw new IllegalArgumentException("Não foi possível criar e associar essa categoria");
        }
    }

    @Override
    public CategoriaFinanceiraAssociationResponse encontrarCategoriaAssociadaPorId(Long id) {
        return null;
    }

    @Override
    public List<CategoriaFinanceiraAssociationResponse> encontrarTodasCategoriasAssociadas() {
        return List.of();
    }

    @Override
    public CategoriaFinanceiraAssociationResponse atualizarCategoriaAssociada(Long id, CategoriaFinanceiraRequest categoriaAtualizada) {
        return null;
    }

    @Override
    public CategoriaFinanceiraAssociationResponse removerCategoriaAssociadaPelaId(Long id) {
        return null;
    }
}
