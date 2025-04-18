package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.CategoriaAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.CategoriaFinanceiraAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.ICategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CategoriaFinanceiraAssociationResponse criarEAssociarCategoria(CategoriaAssociationRequest novaCategoria) {

        try{
            //Criar nova categoria
            CategoriaFinanceira categoriaCriada =
                    categoriaFinanceiraService.criarCategoria(novaCategoria.categoriaESubtipo());

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
                    throw new IllegalArgumentException("Não foi possível associar essa categoria a este pagamento!");
                }

            //Associar a categoria a um novo histórico de transacao
            if(categoriaCriada.getTransacoesRelacionadas().isEmpty()){
                List<HistoricoTransacao> transacaoEncontrada = transacoesService.encontrarTransacaoPorValor
                        (novaCategoria.valorTransacao());

                //Associar a categoria ao Historico de transação
                categoriaCriada.associarCategoriaComTransacoes(transacaoEncontrada.stream()
                        .filter(historicoTransacao ->
                                transacaoEncontrada.equals(novaCategoria.valorTransacao()))
                        .findFirst().orElseThrow(()->
                                new NoSuchElementException("Nenhum elemento com esse valor, foi encontrado para associar com a categoria!")));
            }else{
                throw new IllegalArgumentException("Não foi possível associar essa categoria a esse pagamento!");
            }
            
            //Associar a categoria a um novo usuário

            //Associar a categoria a uma nova conta de usuário

            //Retornar essa nova categoria salva ao usuário
        }catch (Exception e){

        }

        return null;
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
