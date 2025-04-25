package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.CategoriaFinanceiraAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.CategoriaFinanceiraAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.ICategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private IPagamentos pagamentos;

    @Autowired
    private TransacoesService transacoesService;

    @Override
    public CategoriaFinanceiraAssociationResponse criarEAssociarCategoria(CategoriaFinanceiraAssociationRequest novaCategoria) {

        //Antes de criar a categoria, deve-se criar um pagamento pra ele
        PagamentoAssociationResponse pagamentoCriado = pagamentos.criarEAssociarPagamento(novaCategoria.pagamento());

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
                List<Usuario> usuarioEncontrado = usuarioService.encontrarUsuarioPorNome(novaCategoria.nomeUsuario());

                //Associar a categoria ao usuário encontrado
                categoriaCriada.associarCategoriaComUsuario((Usuario) usuarioEncontrado);
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
                    getSaldo(),categoriaCriada.getContaRelacionada().getTipoConta());

            return new CategoriaFinanceiraAssociationResponse(categoriaResponse,pagamentosResponses,
                    transacaoResponses, Collections.singletonList(contaUsuarioResponse), Collections.singletonList(usuarioResponse));
        }catch (Exception e){
            throw new IllegalArgumentException("Não foi possível criar e associar essa categoria");
        }
    }

    @Override
    public CategoriaFinanceiraAssociationResponse encontrarCategoriaAssociadaPorId(Long id) {

        //Encontrar a categoria pela ID
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(id);

        //Retornar os dados dessa categoria em DTO
        CategoriaFinanceiraResponse categoriaDTO = new CategoriaFinanceiraResponse(categoriaEncontrada.getId(),
                categoriaEncontrada.getTiposCategorias(),
                categoriaEncontrada.getSubTipo());

        //Retornar os pagamentos relacionados a essa categoria
        List<PagamentosResponse> pagamentosRelacionados = categoriaEncontrada.getPagamentosRelacionados()
                .stream()
                        .map(pagamentos -> new PagamentosResponse(pagamentos.getId(),
                                pagamentos.getValor(),
                                pagamentos.getData(),
                                pagamentos.getDescricao(),
                                pagamentos.getCategoria()
                        )).toList();

        //Retornar a o histórico transação relacionado a essa categoria
        List<HistoricoTransacaoResponse> transacoesRelacionadas = categoriaEncontrada.getTransacoesRelacionadas()
                .stream().map(historicoTransacao ->
                        new HistoricoTransacaoResponse(
                                historicoTransacao.getId(),
                                historicoTransacao.getValor(),
                                historicoTransacao.getData(),
                                historicoTransacao.getDescricao(),
                                historicoTransacao.getCategorias()
                        )).toList();

        //Retornar as contas dos usuários relacionados a essa categoria
        List<ContaUsuarioResponse> contasRelacionadas = categoriaEncontrada
                .getContaRelacionada().getCategoriasRelacionadas().stream()
                .map(CategoriaFinanceira::getContaRelacionada)
                .map(contaUsuario ->
                        new ContaUsuarioResponse(contaUsuario.getId(),
                                contaUsuario.getNome(),
                                contaUsuario.getSaldo(),
                                contaUsuario.getTipoConta()))
                .toList();

        //Retornar os usuários que ta associado a essa categoria
        List<UsuarioResponse> usuariosRelacionados = categoriaEncontrada.getUsuarioRelacionado()
                .getCategoriasRelacionadas().stream()
                .map(CategoriaFinanceira::getUsuarioRelacionado)
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone()
                )).toList();




        return new CategoriaFinanceiraAssociationResponse(
                Collections.singletonList(categoriaDTO),
                pagamentosRelacionados,
                transacoesRelacionadas,
                contasRelacionadas,
                usuariosRelacionados);
    }



    @Override
    public List<CategoriaFinanceiraAssociationResponse> buscarCategoriaPorTipoESubCategoria
            (CategoriaFinanceiraRequest categoriaESubCategoria) {

        //Encontrar a categoria associada ao tipo escolhido
        CategoriaFinanceira tipocategoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaETipo
                (categoriaESubCategoria.tipoCategoria(),categoriaESubCategoria.subtipo());

        //Retornar os dados dessa categoria em DTO
        CategoriaFinanceiraResponse categoriaDTO = new CategoriaFinanceiraResponse(
                tipocategoriaEncontrada.getId(),
        tipocategoriaEncontrada.getTiposCategorias(),
        tipocategoriaEncontrada.getSubTipo());

        //Retornar os pagamentos relacionados a essa categoria
        List<PagamentosResponse> pagamentosRelacionados = tipocategoriaEncontrada.getPagamentosRelacionados()
                .stream()
                .map(pagamentos -> new PagamentosResponse(pagamentos.getId(),
                        pagamentos.getValor(),
                        pagamentos.getData(),
                        pagamentos.getDescricao(),
                        pagamentos.getCategoria()
                )).toList();

        //Retornar a o histórico transação relacionado a essa categoria
        List<HistoricoTransacaoResponse> transacoesRelacionadas = tipocategoriaEncontrada.getTransacoesRelacionadas()
                .stream().map(historicoTransacao ->
                        new HistoricoTransacaoResponse(
                                historicoTransacao.getId(),
                                historicoTransacao.getValor(),
                                historicoTransacao.getData(),
                                historicoTransacao.getDescricao(),
                                historicoTransacao.getCategorias()
                        )).toList();

        //Retornar as contas dos usuários relacionados a essa categoria
        List<ContaUsuarioResponse> contasRelacionadas = tipocategoriaEncontrada
                .getContaRelacionada().getCategoriasRelacionadas().stream()
                .map(CategoriaFinanceira::getContaRelacionada)
                .map(contaUsuario ->
                        new ContaUsuarioResponse(contaUsuario.getId(),
                                contaUsuario.getNome(),
                                contaUsuario.getSaldo(),
                                contaUsuario.getTipoConta()))
                .toList();

        //Retornar os usuários que ta associado a essa categoria
        List<UsuarioResponse> usuariosRelacionados = tipocategoriaEncontrada.getUsuarioRelacionado()
                .getCategoriasRelacionadas().stream()
                .map(CategoriaFinanceira::getUsuarioRelacionado)
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone()
                )).toList();




        return Collections.singletonList(new CategoriaFinanceiraAssociationResponse(
                Collections.singletonList(categoriaDTO),
                pagamentosRelacionados,
                transacoesRelacionadas,
                contasRelacionadas,
                usuariosRelacionados));
    }

    @Override
    public CategoriaFinanceiraAssociationResponse alterarDadosCategoriaPelaID
            (Long id, CategoriaFinanceiraRequest categoriaAtualizada) {
        return null;
    }

    @Override
    public void removerCategoriaAssociadaPelaId(Long id) {

        // Encontrar a categoria pela id
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(id);

        // Remover associações com transações
        for (HistoricoTransacao transacoesEncontradas : new ArrayList<>(categoriaEncontrada.getTransacoesRelacionadas())) {
            try {
                categoriaEncontrada.desassociarCategoriaTransacao(transacoesEncontradas);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar transação: " + e.getMessage());
            }
        }


        // Remover associações com conta
        try{
            ContaUsuario contaEncontrada = categoriaEncontrada.getContaRelacionada();
            categoriaEncontrada.desassociarCategoriaAConta(contaEncontrada);
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível desassociar esse pagamento desta conta");
        }


        // Remover associações com usuário
        try{
            Usuario usuarioEncontrado = categoriaEncontrada.getUsuarioRelacionado();
            categoriaEncontrada.desassociarCategoriaUsuario(usuarioEncontrado);
        } catch (RuntimeException e) {
            throw new RuntimeException("Não foi possível desassociar esse pagamento deste usuário");
        }

        // Remover o pagamento
        categoriaFinanceiraService.removerCategoria(id);
        }
    }


