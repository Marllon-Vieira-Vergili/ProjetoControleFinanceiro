package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioAssocImpl implements UsuariosAssociation {

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
    public void associarUsuarioComPagamento(Long usuarioId, Long pagamentoId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AssociationErrorException("Usuário não encontrado: " + usuarioId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new AssociationErrorException("Pagamento não encontrado: " + pagamentoId));

        if (usuarioEncontrado.getPagamentosRelacionados() == null) {
            usuarioEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }
        if (usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
                pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())) {
            throw new AssociationErrorException("Esse usuário já está associado ao pagamento: " + pagamentoId);
        }

        usuarioEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarUsuarioComTransacoes(Long usuarioId, Long transacaoId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AssociationErrorException("Usuário não encontrado: " + usuarioId));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new AssociationErrorException("Transação não encontrada: " + transacaoId));

        if (usuarioEncontrado.getTransacoesRelacionadas() == null) {
            usuarioEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }
        if (usuarioEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado) ||
                historicoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())) {
            throw new AssociationErrorException("Esse usuário já está associado à transação: " + transacaoId);
        }

        usuarioEncontrado.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void associarUsuarioComConta(Long usuarioId, Long contaId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AssociationErrorException("Usuário não encontrado: " + usuarioId));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new AssociationErrorException("Conta não encontrada: " + contaId));

        if (usuarioEncontrado.getContasRelacionadas() == null) {
            usuarioEncontrado.setContasRelacionadas(new ArrayList<>());
        }
        if (usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada) ||
                contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())) {
            throw new AssociationErrorException("Esse usuário já está associado à conta: " + contaId);
        }

        usuarioEncontrado.getContasRelacionadas().add(contaEncontrada);
        contaEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void associarUsuarioComCategoria(Long usuarioId, Long categoriaId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AssociationErrorException("Usuário não encontrado: " + usuarioId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new AssociationErrorException("Categoria não encontrada: " + categoriaId));

        if (usuarioEncontrado.getCategoriasRelacionadas() == null) {
            usuarioEncontrado.setCategoriasRelacionadas(new ArrayList<>());
        }
        if (usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada) ||
                categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())) {
            throw new AssociationErrorException("Esse usuário já está associado à categoria: " + categoriaId);
        }

        usuarioEncontrado.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }


    @Override
    public void desassociarUsuarioComPagamento(Long usuarioId, Long pagamentoId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new DesassociationErrorException("Usuário não encontrado: " + usuarioId));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new DesassociationErrorException("Pagamento não encontrado: " + pagamentoId));

        if (!usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
                (pagamentoEncontrado.getUsuarioRelacionado() != null &&
                        !pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()))) {
            throw new DesassociationErrorException("O usuário " + usuarioId + " não está associado ao pagamento " + pagamentoId);
        }

        usuarioEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(null);

        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarUsuarioComTransacao(Long usuarioId, Long transacaoId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new DesassociationErrorException("Usuário não encontrado: " + usuarioId));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new DesassociationErrorException("Transação não encontrada: " + transacaoId));

        if (!usuarioEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado) ||
                (historicoEncontrado.getUsuarioRelacionado() != null &&
                        !historicoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()))) {
            throw new DesassociationErrorException("O usuário " + usuarioId + " não está associado à transação " + transacaoId);
        }

        usuarioEncontrado.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.setUsuarioRelacionado(null);

        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void desassociarUsuarioComConta(Long usuarioId, Long contaId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new DesassociationErrorException("Usuário não encontrado: " + usuarioId));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new DesassociationErrorException("Conta não encontrada: " + contaId));

        if (!usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada) ||
                (contaEncontrada.getUsuarioRelacionado() != null &&
                        !contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()))) {
            throw new DesassociationErrorException("O usuário " + usuarioId + " não está associado à conta " + contaId);
        }

        usuarioEncontrado.getContasRelacionadas().remove(contaEncontrada);
        contaEncontrada.setUsuarioRelacionado(null);

        usuarioRepository.save(usuarioEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void desassociarUsuarioComCategoria(Long usuarioId, Long categoriaId) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new DesassociationErrorException("Usuário não encontrado: " + usuarioId));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new DesassociationErrorException("Categoria financeira não encontrada: " + categoriaId));

        if (!usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada) ||
                (categoriaEncontrada.getUsuarioRelacionado() != null &&
                        !categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()))) {
            throw new DesassociationErrorException("O usuário " + usuarioId + " não está associado à categoria " + categoriaId);
        }

        usuarioEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.setUsuarioRelacionado(null);

        usuarioRepository.save(usuarioEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }
}

