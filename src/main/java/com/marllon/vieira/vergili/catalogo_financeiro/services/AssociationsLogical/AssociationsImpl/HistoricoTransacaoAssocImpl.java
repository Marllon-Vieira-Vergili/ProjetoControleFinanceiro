package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.HistoricoTransacaoAssociation;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
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

    @Override
    public void associarTransacaoComPagamento(Long transacaoId, Long pagamentoId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado com id: " + pagamentoId));

        if (transacaoEncontrada.getPagamentosRelacionados() == null) {
            transacaoEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }
        if (pagamentoEncontrado.getTransacoesRelacionadas() == null) {
            pagamentoEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }

        if (transacaoEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                || pagamentoEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)) {
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " já está associada a este pagamento " + pagamentoId);
        }

        transacaoEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.getTransacoesRelacionadas().add(transacaoEncontrada);

        historicoTransacaoRepository.save(transacaoEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarTransacaoComConta(Long transacaoId, Long contaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));

        if (contaEncontrada.getTransacoesRelacionadas() == null) {
            contaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        if (transacaoEncontrada.getContaRelacionada() == null && contaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada)) {
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " já está associada a essa conta " + contaId);
        }
    }

    @Override
    public void associarTransacaoComUsuario(Long transacaoId, Long usuarioId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado com id: " + usuarioId));

        if (usuarioEncontrado.getTransacoesRelacionadas() == null) {
            usuarioEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }

        if ((transacaoEncontrada.getUsuarioRelacionado() != null &&
                transacaoEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()))
                || usuarioEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)) {
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " já está associada a esse usuário " + usuarioId);
        }

        usuarioEncontrado.getTransacoesRelacionadas().add(transacaoEncontrada);
        transacaoEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void associarTransacaoComCategoria(Long transacaoId, Long categoriaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada com id: " + categoriaId));

        if (transacaoEncontrada.getCategoriasRelacionadas() == null) {
            transacaoEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        if (categoriaEncontrada.getTransacoesRelacionadas() == null) {
            categoriaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }

        if (categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada)
                || transacaoEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)) {
            throw new AssociationErrorException("Essa transação com o id: " + transacaoId + " já está associada a essa categoria " + categoriaId);
        }

        transacaoEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.getTransacoesRelacionadas().add(transacaoEncontrada);

        historicoTransacaoRepository.save(transacaoEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarTransacaoDePagamento(Long transacaoId, Long pagamentoId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado com id: " + pagamentoId));

        if (!pagamentoEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)
                || !transacaoEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)) {
            throw new DesassociationErrorException("A transação " + transacaoId + " não está associada ao pagamento " + pagamentoId);
        }

        transacaoEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.getTransacoesRelacionadas().remove(transacaoEncontrada);

        historicoTransacaoRepository.save(transacaoEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarTransacaoDeConta(Long transacaoId, Long contaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));

        if (!transacaoEncontrada.getContaRelacionada().equals(contaEncontrada)
                || !contaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada)) {
            throw new DesassociationErrorException("A transação " + transacaoId + " não está associada à conta " + contaId);
        }

        contaEncontrada.getTransacoesRelacionadas().remove(transacaoEncontrada);
        transacaoEncontrada.setContaRelacionada(null);

        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void desassociarTransacaoDeUsuario(Long transacaoId, Long usuarioId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado com id: " + usuarioId));

        if (!transacaoEncontrada.getUsuarioRelacionado().equals(usuarioEncontrado)
                || !usuarioEncontrado.getTransacoesRelacionadas().contains(transacaoEncontrada)) {
            throw new DesassociationErrorException("A transação " + transacaoId + " não está associada ao usuário " + usuarioId);
        }

        transacaoEncontrada.setUsuarioRelacionado(null);
        usuarioEncontrado.getTransacoesRelacionadas().remove(transacaoEncontrada);

        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }

    @Override
    public void desassociarTransacaoDeCategoria(Long transacaoId, Long categoriaId) {
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada com id: " + transacaoId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada com id: " + categoriaId));

        if (!categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada)
                || !transacaoEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)) {
            throw new DesassociationErrorException("A transação " + transacaoId + " não está associada à categoria " + categoriaId);
        }

        transacaoEncontrada.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.getTransacoesRelacionadas().remove(transacaoEncontrada);

        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);
    }
}
