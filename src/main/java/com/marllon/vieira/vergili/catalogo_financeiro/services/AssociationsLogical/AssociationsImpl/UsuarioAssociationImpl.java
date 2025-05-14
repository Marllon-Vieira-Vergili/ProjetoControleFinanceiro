package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioAssociationImpl implements UsuariosAssociation {

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
    public void associarUsuarioComPagamento(Long usuarioId, Long pagamentoid) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoid);

        //Se do lado do usuário, obter pagamentos relacionados, a lista estiver vazia, ou nula
        if (usuarioEncontrado.getPagamentosRelacionados() == null) {
            usuarioEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }
        //Se já existir a associação em ambos os lados
        if (usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
                pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())) {
                throw new AssociationErrorException("Esse usuario com essa id: " + usuarioId + " ja está associado a " +
                        " esse pagamento: " + pagamentoid);
        }
        //Associar em ambos os lados
        usuarioEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarUsuarioComTransacoes(Long usuarioId, Long transacaoId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);

        if(usuarioEncontrado.getTransacoesRelacionadas() == null){
            usuarioEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }
        if(usuarioEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado) ||
                historicoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new AssociationErrorException("Esse usuario com essa id: " + usuarioId + " ja está associado a " +
                    " essa transação: " + transacaoId);
        }
        //Senão... associar
        usuarioEncontrado.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        //Salvar as modificações bidirecionalmente
        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void associarUsuarioComConta(Long usuarioId, Long contaId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);

        if(usuarioEncontrado.getContasRelacionadas() == null){
            usuarioEncontrado.setContasRelacionadas(new ArrayList<>());
        }

        if(usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada) ||
        contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new AssociationErrorException("Esse usuario com essa id: " + usuarioId + " ja está associado a " +
                    " essa conta: " + contaId);
        }
        //Senao... associar em ambos os lados
        usuarioEncontrado.getContasRelacionadas().add(contaEncontrada);
        contaEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        //Salvar as associações em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void associarUsuarioComCategoria(Long usuarioId, Long categoriaId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

        if(usuarioEncontrado.getCategoriasRelacionadas() == null){
            usuarioEncontrado.setCategoriasRelacionadas(new ArrayList<>());
        }
        if(usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada) ||
                categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new AssociationErrorException("Esse usuario com essa id: " + usuarioId + " ja está associado a " +
                    " essa categoria: " + categoriaId);
        }
        //Senão.. associar em ambos os lados

        usuarioEncontrado.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarUsuarioComPagamento(Long usuarioId, Long pagamentoId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoId);

        if(!pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()) ||
        !usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new DesassociationErrorException("a id desse usuario " + usuarioId + " " +
                    "não é associado a esse pagamento com esse id: " + pagamentoId);
        }
        //Senão... desassociar de ambos os lados
        usuarioEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(null);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarUsuarioComTransacao(Long usuarioId, Long transacaoId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);

        if(!usuarioEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado) ||
        !historicoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new DesassociationErrorException("a id desse usuario " + usuarioId + " " +
                    "não é associado a esse histórico de transação com esse id: " + transacaoId);
        }
        //Senão.. desassociar
        usuarioEncontrado.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.setUsuarioRelacionado(null);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void desassociarUsuarioComConta(Long usuarioId, Long contaId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);

        if(!usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada) ||
                !contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new DesassociationErrorException("a id desse usuario " + usuarioId + " " +
                    "não é associado a essa conta com esse id: " + contaId);
        }
        //Senão.. desassociar
        usuarioEncontrado.getContasRelacionadas().remove(contaEncontrada);
        contaEncontrada.setUsuarioRelacionado(null);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void desassociarUsuarioComCategoria(Long usuarioId, Long categoriaId) {
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

        if(!usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada) ||
                !categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())){
            throw new DesassociationErrorException("a id desse usuario " + usuarioId + " " +
                    "não é associado a essa categoria financeira com esse id: " + categoriaId);
        }
        //Senão.. desassociar
        usuarioEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.setUsuarioRelacionado(null);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }
}
