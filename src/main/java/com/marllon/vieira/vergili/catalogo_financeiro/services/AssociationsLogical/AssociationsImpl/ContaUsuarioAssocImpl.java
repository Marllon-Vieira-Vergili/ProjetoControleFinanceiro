package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContaUsuarioAssocImpl implements ContaUsuarioAssociation {

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
    public void associarContaComCategoria(Long contaId, Long categoriaId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

        if(contaEncontrada.getCategoriasRelacionadas() == null){
            contaEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        //Se a conta já possuir essa categoria associada
        if(contaEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)
                || categoriaEncontrada.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " ja está associado" +
                    " a essa categoria " + categoriaId);
        }
        //Senão.. associar
        contaEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.setContaRelacionada(contaEncontrada);

        //Salvar em ambos os lados das associações
        contaUsuarioRepository.save(contaEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void associarContaComPagamento(Long contaId, Long pagamentoId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoId);

        if(contaEncontrada.getPagamentosRelacionados() == null){
            contaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }
        if(contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                ||pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " ja está associado" +
                    " a esse pagamento " + pagamentoId);
        }
        //Senão.. associar
        contaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setContaRelacionada(contaEncontrada);

        //Salvar em ambos os lados das associações
        contaUsuarioRepository.save(contaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarContaComTransacao(Long contaId, Long transacaoId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.encontrarTransacaoPorid(transacaoId);

        if(contaEncontrada.getTransacoesRelacionadas() == null){
            contaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        if(contaEncontrada.getTransacoesRelacionadas().contains(historicoEncontrado)
                ||historicoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " ja está associado" +
                    " a esse histórico de transação " + transacaoId);
        }
        //Realizar a associação bidirecionalmente
        contaEncontrada.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.setContaRelacionada(contaEncontrada);

        //Salvar em ambos os lados
        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void associarContaComUsuario(Long contaId, Long usuarioId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);

        if(contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                ||usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada)){
            throw new AssociationErrorException("Essa conta com o id: " + contaId + " ja está associado" +
                    " a esse usuário " + usuarioId);
        }
        //Senão.. associar em ambos os lados
        contaEncontrada.setUsuarioRelacionado(usuarioEncontrado);
        usuarioEncontrado.getContasRelacionadas().add(contaEncontrada);

        //Salvar
        contaUsuarioRepository.save(contaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }

    @Override
    public void desassociarContaDeCategoria(Long contaId, Long categoriaId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.encontrarCategoriaPorId(categoriaId);

        if(contaEncontrada.getCategoriasRelacionadas() == null){
            contaEncontrada.setCategoriasRelacionadas(new ArrayList<>());
        }
        if(!contaEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)
                ||!categoriaEncontrada.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new DesassociationErrorException("a id dessa conta " + contaId + " " +
                    "não é associado a essa categoria financeira com essa id: " + categoriaId);
        }
        //Senão.. desassociar em ambos os lados
        contaEncontrada.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.setContaRelacionada(null);

        //Salvar em ambos os lados
        contaUsuarioRepository.save(contaEncontrada);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarContaDePagamento(Long contaId, Long pagamentoId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        Pagamentos pagamentoEncontrado = pagamentosService.encontrarPagamentoPorid(pagamentoId);

        if(!contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                ||!pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new DesassociationErrorException("a id dessa conta " + contaId + " " +
                    "não é associado a esse pagamento com essa id: " + pagamentoId);
        }
        //Senão.. desassociar em ambos os lados
        contaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.setContaRelacionada(null);

        //Salvar em ambos os lados
        contaUsuarioRepository.save(contaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarContaDeHistoricoDeTransacao(Long contaId, Long historicoId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.encontrarTransacaoPorid(historicoId);

        if(!contaEncontrada.getTransacoesRelacionadas().contains(historicoEncontrado)
                ||!historicoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new DesassociationErrorException("a id dessa conta " + contaId + " " +
                    "não é associado a esse histórico de transação com essa id: " + historicoId);
        }
        //Senão.. desassociar em ambos os lados
        contaEncontrada.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.setContaRelacionada(null);

        //Salvar em ambos os lados
        contaUsuarioRepository.save(contaEncontrada);
        historicoTransacaoRepository.save(historicoEncontrado);

    }



    @Override
    public void desassociarContaDeUsuario(Long contaId, Long usuarioId) {
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(contaId);
        Usuario usuarioEncontrado = usuariosService.encontrarUsuarioPorId(usuarioId);

        if(!contaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                ||!usuarioEncontrado.getContasRelacionadas().contains(contaEncontrada)){
            throw new DesassociationErrorException("a id dessa conta " + contaId + " " +
                    "não é associado a esse usuário com essa id: " + usuarioId);
        }
        //Senão.. desassociar em ambos os lados
        contaEncontrada.setUsuarioRelacionado(null);
        usuarioEncontrado.getContasRelacionadas().remove(contaEncontrada);

        //Salvar em ambos os lados
        contaUsuarioRepository.save(contaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }
}
