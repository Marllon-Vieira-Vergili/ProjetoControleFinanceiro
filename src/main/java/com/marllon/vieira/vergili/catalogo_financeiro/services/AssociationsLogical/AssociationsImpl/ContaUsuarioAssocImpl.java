package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContaUsuarioAssocImpl implements ContaUsuarioAssociation {

    @Autowired
    @Lazy
    private PagamentosRepository pagamentosRepository;

    @Autowired
    @Lazy
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    @Lazy
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    @Lazy
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;


    @Override
    public void associarContaComCategoria(Long contaId, Long categoriaId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada com id: " + categoriaId));

        if (contaEncontrada.getCategoriasRelacionadas() == null) {
            contaEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        if (contaEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)
                || categoriaEncontrada.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " já está associada a essa categoria " + categoriaId);
        }

        contaEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.setContaRelacionada(contaEncontrada);

        contaUsuarioRepository.save(contaEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void associarContaComPagamento(Long contaId, Long pagamentoId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado com id: " + pagamentoId));

        if (contaEncontrada.getPagamentosRelacionados() == null) {
            contaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }
        if (contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                || pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " já está associada a esse pagamento " + pagamentoId);
        }

        contaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setContaRelacionada(contaEncontrada);

        contaUsuarioRepository.save(contaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarContaComTransacao(Long contaId, Long transacaoId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Histórico não encontrado com id: " + transacaoId));

        if (contaEncontrada.getTransacoesRelacionadas() == null) {
            contaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        if (contaEncontrada.getTransacoesRelacionadas().contains(historicoEncontrado)
                || historicoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " já está associada a esse histórico de transação " + transacaoId);
        }

        contaEncontrada.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.setContaRelacionada(contaEncontrada);

        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void associarContaComUsuario(Long contaId, Long usuarioId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado com id: " + usuarioId));

        if (contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                || usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada)) {
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " já está associada a esse usuário " + usuarioId);
        }

        contaEncontrada.setUsuarioRelacionado(usuarioEncontrado);
        usuarioEncontrado.getContasRelacionadas().add(contaEncontrada);

        contaUsuarioRepository.save(contaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }

    // Métodos de desassociação com orElseThrow
    @Override
    public void desassociarContaDeCategoria(Long contaId, Long categoriaId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada com id: " + categoriaId));

        if (contaEncontrada.getCategoriasRelacionadas() == null) {
            contaEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        if (!contaEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)
                || !categoriaEncontrada.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new DesassociationErrorException("A conta com id " + contaId + " não está associada à categoria " + categoriaId);
        }

        contaEncontrada.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.setContaRelacionada(null);

        contaUsuarioRepository.save(contaEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarContaDePagamento(Long contaId, Long pagamentoId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado com id: " + pagamentoId));

        if (!contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                || !pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new DesassociationErrorException("A conta com id " + contaId + " não está associada ao pagamento " + pagamentoId);
        }

        contaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.setContaRelacionada(null);

        contaUsuarioRepository.save(contaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(historicoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Histórico de transação não encontrado com id: " + historicoId));

        if (!contaEncontrada.getTransacoesRelacionadas().contains(historicoEncontrado)
                || !historicoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())) {
            throw new DesassociationErrorException("A conta com id " + contaId + " não está associada ao histórico de transação " + historicoId);
        }

        contaEncontrada.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.setContaRelacionada(null);

        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void desassociarContaDeUsuario(Long contaId, Long usuarioId) {
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada com id: " + contaId));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado com id: " + usuarioId));

        if (!contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                || !usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada)) {
            throw new DesassociationErrorException("A conta com id " + contaId + " não está associada ao usuário " + usuarioId);
        }

        contaEncontrada.setUsuarioRelacionado(null);
        usuarioEncontrado.getContasRelacionadas().remove(contaEncontrada);

        contaUsuarioRepository.save(contaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }
}