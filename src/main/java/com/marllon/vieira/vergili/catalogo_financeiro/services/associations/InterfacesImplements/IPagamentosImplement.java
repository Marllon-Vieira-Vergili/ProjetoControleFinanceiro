package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.Despesas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.Receitas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;

@Service
public class IPagamentosImplement implements IPagamentos {

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private TransacoesService transacoesService;

    @Autowired
    private CategoriaFinanceiraService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    public PagamentoAssociationResponse criarEAssociarPagamento(PagamentoAssociationRequest novoPagamento) {
        //Arrays list fora do laço para retornar os valores após serem criados
        List<CategoriaFinanceiraResponse> categoriasRelacionadas = new ArrayList<>();
        List<HistoricoTransacaoResponse> historicoTransacoesRelacionados = new ArrayList<>();
        ContaUsuarioResponse contaRelacionada = null;
        UsuarioResponse usuarioRelacionado = null;
        List<PagamentosResponse> pagamentosRelacionados = new ArrayList<>();

        try{
            //Criar um valor de pagamento
            Pagamentos novoPagamentoCriado = pagamentosService.criarNovoPagamento(novoPagamento.pagamento());


            //Associar esse novo pagamento a uma categoria específica, se será de DESPESA ou RECEITA
            if(novoPagamentoCriado.getCategoria().name().isEmpty()) {
                List<CategoriaFinanceira> categoriasEncontradas = categoriaService.encontrarCategoriasPorTipo
                        (novoPagamento.tiposCategoria().tipoCategoria());
                if (novoPagamento.tiposCategoria().tipoCategoria().equals(RECEITA)) {
                    novoPagamentoCriado.associarPagamentoComCategoria(categoriasEncontradas.get(RECEITA.getValor()));
                    Receitas[] receitasFinanceiras = Receitas.values();
                    if(novoPagamento.tiposCategoria().subtipo() != null
                            && novoPagamento.tiposCategoria().subtipo().contains(Arrays.toString(receitasFinanceiras))){
                        //Associar o tipo receita ao subtipo da Categoria relacionada a esse pagamento
                        novoPagamentoCriado.setCategoria(TiposCategorias.valueOf(Arrays.toString(receitasFinanceiras)));
                    }else{
                        throw new IllegalArgumentException("Não há nenhum subtipo de categoria para salvar! os tipos " +
                                "válidos são: " + Arrays.toString(Receitas.values()));
                    }
                } else if(novoPagamento.tiposCategoria().tipoCategoria().equals(DESPESA)){
                    novoPagamentoCriado.associarPagamentoComCategoria(categoriasEncontradas.get(DESPESA.getValor()));
                    Despesas[] despesasFinanceiras = Despesas.values();
                    if(novoPagamento.tiposCategoria().subtipo() != null
                            && novoPagamento.tiposCategoria().subtipo().contains(Arrays.toString(despesasFinanceiras))){
                        //Associar o tipo de despesa ao subtipo da categoria relacionada a esse pagamento
                        novoPagamentoCriado.setCategoria(TiposCategorias.valueOf(Arrays.toString(despesasFinanceiras)));
                    }else{
                        throw new IllegalArgumentException("Não há nenhum subtipo de categoria para salvar! os tipos " +
                                "válidos são: " + Arrays.toString(Despesas.values()));
                    }
                }
                //Mapear os valores da Categoria para DTO
                categoriasRelacionadas = categoriasEncontradas.stream()
                        .map(categoriaFinanceira -> new CategoriaFinanceiraResponse
                                (categoriaFinanceira.getId(), categoriaFinanceira.getTiposCategorias(),
                                        categoriaFinanceira.getSubTipo())).toList();
            }
            //Associar esse novo pagamento a uma transacao específica, que terá o mesmo valor
            if(novoPagamentoCriado.getTransacoesRelacionadas().isEmpty()){
                List<HistoricoTransacao> transacaoEncontrada = transacoesService.
                        encontrarTransacaoPorValor(novoPagamentoCriado.getValor());
                //Se a id da transação, bater o valor e a data igual do pagamento, e categoria associar
                if(transacaoEncontrada.equals(novoPagamentoCriado)){
                    novoPagamentoCriado.associarPagamentoATransacao(transacaoEncontrada.getFirst());
                }
                else{
                    throw new NoSuchElementException("Não há nenhuma transação informada com os mesmos valores do pagamento");
                }

                //Mapear o valor da transação para DTO
                historicoTransacoesRelacionados = transacaoEncontrada.stream()
                        .map(historicoTransacao -> new HistoricoTransacaoResponse
                                (historicoTransacao.getId(), historicoTransacao.getValor(),historicoTransacao.getData(),
                                        historicoTransacao.getDescricao(), historicoTransacao.getCategorias())).toList();
            }

            //Associar esse pagamento a uma conta de usuário
            if(novoPagamentoCriado.getContaRelacionada() == null){
                //Localizar a conta do usuário que será associado a esse pagamento
                ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorNome
                        (novoPagamento.contaAssociada().nome());
                if(!contaEncontrada.getNome().isEmpty() && contaEncontrada.getPagamentosRelacionados().isEmpty()){
                    //Associar esse novo pagamento a esse usuário informado
                    novoPagamentoCriado.associarPagamentoComConta(contaEncontrada);
                }else{
                    throw new IllegalArgumentException("Não foi possível associar o novo pagamento a esta conta: " + contaEncontrada);
                }
                //Mapear o valor da conta para DTO
                contaRelacionada = new ContaUsuarioResponse(contaEncontrada.getId(),
                        contaEncontrada.getNome(), contaEncontrada.getSaldo());
            }

            //Associar o pagamento a um usuário
            if(novoPagamentoCriado.getUsuarioRelacionado() == null){
                //Localizar  usuário que será associado a esse pagamento
                Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPorNome
                        (novoPagamento.usuarioAssociado().nome());
                if(!usuarioEncontrado.getNome().isEmpty() && usuarioEncontrado.getPagamentosRelacionados().isEmpty()){
                    //Associar esse novo pagamento a esse usuário informado
                    novoPagamentoCriado.associarPagamentoComUsuario(usuarioEncontrado);
                }else{
                    throw new IllegalArgumentException("Não foi possível associar o novo pagamento a esse usuario: " + usuarioEncontrado);
                }
                //Mapear o valor do usuário para DTO
                usuarioRelacionado = new UsuarioResponse(usuarioEncontrado.getId(),
                        usuarioEncontrado.getNome(), usuarioEncontrado.getEmail(), usuarioEncontrado.getTelefone());
            }
            //Mapear o valor do pagamento para dto
            pagamentosRelacionados = Collections.singletonList(new PagamentosResponse(novoPagamentoCriado.getId(),
                    novoPagamentoCriado.getValor(), novoPagamentoCriado.getData(),
                    novoPagamentoCriado.getDescricao(), novoPagamentoCriado.getCategoria()));
        }catch (Exception e){
            throw new IllegalArgumentException("Não foi possível criar o pagamento, ou associar a alguma categoria" + e.getMessage());
        }
        return new PagamentoAssociationResponse(pagamentosRelacionados,historicoTransacoesRelacionados,
                categoriasRelacionadas,contaRelacionada,usuarioRelacionado);
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
    public PagamentoAssociationResponse atualizarPagamentoAssociado(Long id, PagamentoAssociationRequest pagamentoAtualizado) {
        return null;
    }

    @Override
    public PagamentoAssociationResponse removerPagamentoAssociadoPelaId(Long id) {
        return null;
    }
}
