package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PagamentosAssocImpl implements PagamentosAssociation {

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
    public void associarPagamentoComUsuario(Long pagamentoId, Long usuarioId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado"));

        if (usuarioEncontrado.getPagamentosRelacionados() == null) {
            usuarioEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }

        if (pagamentoEncontrado.getUsuarioRelacionado() != null &&
                pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioId)) {
            throw new AssociationErrorException("Esse pagamento já está associado a esse usuário");
        }

        usuarioEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        usuarioRepository.save(usuarioEncontrado);
    }

    @Override
    public void associarPagamentoATransacao(Long pagamentoId, Long transacaoId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada"));

        if (pagamentoEncontrado.getTransacoesRelacionadas() == null) {
            pagamentoEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }
        if (historicoEncontrado.getPagamentosRelacionados() == null) {
            historicoEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }

        if (pagamentoEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado)) {
            throw new AssociationErrorException("Pagamento já associado à transação");
        }

        pagamentoEncontrado.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void associarPagamentoComConta(Long pagamentoId, Long contaId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada"));

        if (contaEncontrada.getPagamentosRelacionados() == null) {
            contaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        if (pagamentoEncontrado.getContaRelacionada() != null &&
                pagamentoEncontrado.getContaRelacionada().getId().equals(contaId)) {
            throw new AssociationErrorException("Esse pagamento já está associado a essa conta");
        }

        pagamentoEncontrado.setContaRelacionada(contaEncontrada);
        contaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void associarPagamentoComCategoria(Long pagamentoId, Long categoriaId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada"));


        if (categoriaEncontrada.getPagamentosRelacionados() == null) {
            categoriaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        if(categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                && pagamentoEncontrado.getCategoriaRelacionada() != null
                &&pagamentoEncontrado.getCategoriaRelacionada().equals(categoriaEncontrada)){
            throw new AssociationErrorException("Ambos ja estão associados");
        }

        pagamentoEncontrado.setCategoriaRelacionada(categoriaEncontrada);
        categoriaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarPagamentoUsuario(Long pagamentoId, Long usuarioId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado"));

        if (pagamentoEncontrado.getUsuarioRelacionado() == null ||
                !pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioId)) {
            throw new DesassociationErrorException("Pagamento não está associado a esse usuário");
        }

        pagamentoEncontrado.setUsuarioRelacionado(null);
        usuarioEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarPagamentoDeTransacao(Long pagamentoId, Long transacaoId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        HistoricoTransacao historicoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Transação não encontrada"));

        if (!pagamentoEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado)) {
            throw new DesassociationErrorException("Pagamento não está associado a essa transação");
        }

        pagamentoEncontrado.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void desassociarPagamentoConta(Long pagamentoId, Long contaId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não encontrada"));

        if (pagamentoEncontrado.getContaRelacionada() == null ||
                !pagamentoEncontrado.getContaRelacionada().getId().equals(contaId)) {
            throw new DesassociationErrorException("Pagamento não está associado a essa conta");
        }

        pagamentoEncontrado.setContaRelacionada(null);
        contaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void desassociarPagamentoCategoria(Long pagamentoId, Long categoriaId) {

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não encontrado"));

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não encontrada"));

        if (!(pagamentoEncontrado.getCategoriaRelacionada() == categoriaEncontrada)
                && !categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)) {
            throw new DesassociationErrorException("Pagamento não está associado a essa categoria");
        }

        pagamentoEncontrado.setCategoriaRelacionada(null);
        categoriaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);

        pagamentosRepository.save(pagamentoEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }
}
