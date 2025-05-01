package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class HistoricoTransacaoImpl implements HistoricoTransacaoAssociation {

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private CategoriaFinanceiraService categoriaFinanceiraService;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private HistoricoTransacaoService historicoTransacaoService;

    @Autowired
    private UsuariosService usuariosService;


    @Override
    public void associarTransacaoComPagamento(Long transacaoId, Long pagamentoId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);

        //Verificar se a lista de pagamentos relacionados a transação não está nula
        if(transacaoEncontrada.getPagamentosRelacionados() == null){
            transacaoEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        //Verificar do outro lado também
        if(pagamentoEncontrado.getTransacoesRelacionadas() == null){
            pagamentoEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }

        //Se ja estiver uma transação associado a esse pagamento
        if(transacaoEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
        pagamentoEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)){
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " ja está associado" +
                    " a este pagamento " + pagamentoId);
        }
                //Realizar a associação dos 2 lados
                transacaoEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
                pagamentoEncontrado.getTransacoesRelacionadas().add(transacaoEncontrada);

        //Salvar em ambos os lados
        historicoTransacaoRepository.save(transacaoEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarTransacaoComConta(Long transacaoId, Long contaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        ContaUsuario contaEncontrada = contaUsuarioService.getContaById(contaId);

        //Se do lado da conta relacionado as transações, estiver vazio a lista, instanciar uma nova lista
        if(contaEncontrada.getTransacoesRelacionadas() ==null){
            contaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        //Verificar se essa transação encontrada não possui uma conta relacionada
        if(transacaoEncontrada.getContaRelacionada() == null && contaEncontrada.getTransacoesRelacionadas()
                .contains(transacaoEncontrada)){
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " ja está associado" +
                    " a essa conta " + contaId);
        }
    }

    @Override
    public void associarTransacaoComUsuario(Long transacaoId, Long usuarioId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);
    }

    @Override
    public void associarTransacaoComCategoria(Long transacaoId, Long categoriaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);
    }

    @Override
    public void desassociarTransacaoDePagamento(Long transacaoId, Long pagamentoId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);

        //Verificar se essa transação está associado a essa conta
        if(!pagamentoEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada) &&
                !transacaoEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new DesassociationErrorException("a id desse histórico de transacao " + transacaoId + " " +
                    "não é associado a esse pagamento com essa id: " + pagamentoId);
        }

        //Senao.. realizar a desassociacao
        transacaoEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.getTransacoesRelacionadas().remove(transacaoEncontrada);

        //Realizar e salvar as alterações
        historicoTransacaoRepository.save(transacaoEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarTransacaoDeConta(Long transacaoId, Long contaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        ContaUsuario contaEncontrada = contaUsuarioService.getContaById(contaId);

        //Verificar se essa transação está associado a essa conta
        if(!transacaoEncontrada.getContaRelacionada().equals(contaEncontrada) ||
                !contaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada)){
                throw new DesassociationErrorException("a id desse histórico de transacao " + transacaoId + " " +
                        "não é associado a essa conta com essa id: " + contaId);
        }

        //Desassociar de ambos os lados
        contaEncontrada.getTransacoesRelacionadas().remove(transacaoEncontrada);
        transacaoEncontrada.setContaRelacionada(null);

        //Salvar os valores, persistindo os dados
        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void desassociarTransacaoDeUsuario(Long transacaoId, Long usuarioId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);

        //Verificar se essa transação está associado a essa categoria
        if(!transacaoEncontrada.getUsuarioRelacionado().equals(usuarioEncontrado) ||
                !usuarioEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)){
            throw new DesassociationErrorException("a id desse histórico de transacao " + transacaoId + " " +
                    "não é associado a esse usuário com esse id: " + usuarioId);
        }

        //Senao.. desassociar de ambos os lados
        transacaoEncontrada.setUsuarioRelacionado(null);
        usuarioEncontrado.getTransacoesRelacionadas().remove(transacaoEncontrada);

        //Salvar de ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void desassociarTransacaoDeCategoria(Long transacaoId, Long categoriaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        //Verificar se essa transação está associado a essa categoria
        if(!categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada) ||
                !transacaoEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            throw new DesassociationErrorException("a id desse histórico de transacao " + transacaoId + " " +
                    "não é associado a essa categoria com esse id: " + categoriaId);
        }

        //Senao.. desassociar de ambos os lados
        transacaoEncontrada.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.getTransacoesRelacionadas().remove(transacaoEncontrada);

        //Salvar de ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);

    }
}
