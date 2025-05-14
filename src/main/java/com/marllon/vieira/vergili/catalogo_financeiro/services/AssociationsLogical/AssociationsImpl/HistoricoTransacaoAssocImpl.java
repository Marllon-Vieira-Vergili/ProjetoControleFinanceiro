package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoricoTransacaoAssocImpl implements HistoricoTransacaoAssociation {

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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoId);

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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);

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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);


        //Verificar do lado do usuário quanto a transações se a lista não está vazia
        if(usuarioEncontrado.getTransacoesRelacionadas() == null){
            usuarioEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }

        //Verificar de ambos os lados se já não possui essa associação feita
        if(transacaoEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                ||usuarioEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)){
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " ja está associado" +
                    " a esse usuário " + usuarioId);
        }

        //Senão.. associar
        usuarioEncontrado.getTransacoesRelacionadas().add(transacaoEncontrada);
        transacaoEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        //Realizar o salvamento
        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void associarTransacaoComCategoria(Long transacaoId, Long categoriaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

        //Verificar se ambas as listas de transação e categorias não serão inicializadas vazia
        if(transacaoEncontrada.getCategoriasRelacionadas() == null){
            transacaoEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        if(categoriaEncontrada.getTransacoesRelacionadas() == null){
            categoriaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }

        //Verificar se em ambos os lados já não possui essa associação realizada
        if(categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada) ||
                transacaoEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " ja está associado" +
                    " a essa categoria " + categoriaId);
        }
        //Senão... associar em ambos os lados
        transacaoEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.getTransacoesRelacionadas().add(transacaoEncontrada);

        //Realizar o salvamento em ambos os lados
        historicoTransacaoRepository.save(transacaoEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarTransacaoDePagamento(Long transacaoId, Long pagamentoId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoId);

        //Verificar se essa transação está associado a essa conta
        if(!pagamentoEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada) ||
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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);

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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);

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
        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

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
