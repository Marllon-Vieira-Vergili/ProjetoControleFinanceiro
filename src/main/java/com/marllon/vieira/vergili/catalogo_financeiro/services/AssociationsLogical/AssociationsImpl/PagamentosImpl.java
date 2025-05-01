package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.PagamentosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


public class PagamentosImpl implements PagamentosAssociation {

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
    public void associarPagamentoComUsuario(Long pagamentoId, Long usuarioId) {

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);

        //Se a lista de pagamentos relacionados for nula... instanciar uma nova lista de array
        if(usuarioEncontrado.getPagamentosRelacionados() == null){
            usuarioEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }

        //Verificar ambos os lados se já não possuem essa associação
        if(pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()) || usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new AssociationErrorException("Esse pagamento com essa id: " + pagamentoId + " ja está associado a " +
                    " esse usuário: " + usuarioId);
        }

        //associar
        usuarioEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);
        pagamentoEncontrado.setUsuarioRelacionado(usuarioEncontrado);

        //Salvar para ambos os lados das entidades
        pagamentosRepository.save(pagamentoEncontrado);
        usuarioRepository.save(usuarioEncontrado);
    }

    @Override
    public void associarPagamentoATransacao(Long pagamentoId, Long transacaoId) {

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);

        //Se a lista de pagamentos relacionados for nula... instanciar uma nova lista de array
        if(pagamentoEncontrado.getTransacoesRelacionadas() == null){
            pagamentoEncontrado.setTransacoesRelacionadas(new ArrayList<>());
        }
        //Se do outro lado também for nulo, instanciar uma lista de arrays
        if(historicoEncontrado.getPagamentosRelacionados() == null){
            historicoEncontrado.setPagamentosRelacionados(new ArrayList<>());
        }

        //Verificar se esse pagamento ja não está associado a essa transação
        if(pagamentoEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado)
                || historicoEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new AssociationErrorException("Esse pagamento com essa id: " + pagamentoId + " ja está associado a " +
                    " essa transação: " + transacaoId);
        }


        //Realizar a associação para ambos
       pagamentoEncontrado.getTransacoesRelacionadas().add(historicoEncontrado);
        historicoEncontrado.getPagamentosRelacionados().add(pagamentoEncontrado);

        //Salvar em ambos os lados as associações
        pagamentosRepository.save(pagamentoEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);


    }

    @Override
    public void associarPagamentoComConta(Long pagamentoId, Long contaId) {

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        ContaUsuario contaEncontrada = contaUsuarioService.getContaById(contaId);

        //Verificar do lado da conta, se a lista de arrays não está vazia, se tiver, criar
        if(contaEncontrada.getPagamentosRelacionados().isEmpty()){
            contaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        //Verificar se não possui nenhum pagamento a conta associada
        if(!pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())
                ||contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new AssociationErrorException("Esse pagamento com essa id: " + pagamentoId + " ja está associado a " +
                    " essa conta: " + contaId);
        }

        //Verificar se esse pagamento já não possui outra conta de usuario associado
        if(!pagamentoEncontrado.getContaRelacionada().getId()
                .equals(contaEncontrada.getUsuarioRelacionado().getId())){
            throw new AssociationErrorException("Pagamento: " + pagamentoId + " já está associado a outra conta");
        }

        //Senão.. associar em ambos os lados
        pagamentoEncontrado.setContaRelacionada(contaEncontrada);
        contaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);

        //Salvar em ambos os lados
        pagamentosRepository.save(pagamentoEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void associarPagamentoComCategoria(Long pagamentoId, Long categoriaId) {

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        //Se do lado de pagamentos, a lista de arrays for vazia, instanciar um novo array list
        if(pagamentoEncontrado.getCategoriasRelacionadas() == null){
            pagamentoEncontrado.setCategoriasRelacionadas(new ArrayList<>());
        }
        //Se do outro lado também for nulo..
        if(categoriaEncontrada.getPagamentosRelacionados() == null){
           categoriaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        //Verificar se já não estão associados
        if(pagamentoEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)||
        !categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new AssociationErrorException("Esse pagamento com essa id: " + pagamentoId + " ja está associado a " +
                    " essa categoria criada: " + categoriaId);
        }

        //Senao... associar dos 2 lados
        pagamentoEncontrado.getCategoriasRelacionadas().add(categoriaEncontrada);
        categoriaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);

        //Salvar as alterações e persistir no banco de dados
        pagamentosRepository.save(pagamentoEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }

    @Override
    public void desassociarPagamentoUsuario(Long pagamentoId, Long usuarioId) {
        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);

        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);

        if (!pagamentoEncontrado.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId())
                ||!usuarioEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new DesassociationErrorException("a id desse pagamento " + pagamentoId + " " +
                    "não é associado a esse usuário com essa id: " + usuarioId);
        }

        //Senão.. desassociar em ambos os lados
        pagamentoEncontrado.setUsuarioRelacionado(null);
        usuarioEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);

        //Salvar em ambos os lados
        usuarioRepository.save(usuarioEncontrado);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void desassociarPagamentoDeTransacao(Long pagamentoId, Long transacaoId) {

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        HistoricoTransacao historicoEncontrado = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);

        if (!pagamentoEncontrado.getTransacoesRelacionadas().contains(historicoEncontrado) ||
        !historicoEncontrado.getPagamentosRelacionados().contains(pagamentoEncontrado)){
            throw new DesassociationErrorException("a id desse pagamento " + pagamentoId + " " +
                    "não é associado a esse histórico de transação com essa id: " + transacaoId);
        }

        //Desassociar de ambos os lados
        pagamentoEncontrado.getTransacoesRelacionadas().remove(historicoEncontrado);
        historicoEncontrado.getPagamentosRelacionados().remove(pagamentoEncontrado);

        //Salvar e persistir os dados em ambos lados
        pagamentosRepository.save(pagamentoEncontrado);
        historicoTransacaoRepository.save(historicoEncontrado);
    }

    @Override
    public void desassociarPagamentoConta(Long pagamentoId, Long contaId) {
        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        ContaUsuario contaEncontrada = contaUsuarioService.getContaById(contaId);

        if(!contaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)||
        !pagamentoEncontrado.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new DesassociationErrorException("a id desse pagamento " + pagamentoId + " " +
                    "não é associado a essa conta com esse id: " + contaId);
        }

        //Desassociar de ambos os lados
        pagamentoEncontrado.setContaRelacionada(null);
        contaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);

        //Salvar e persistir os dados em ambos os lados
        pagamentosRepository.save(pagamentoEncontrado);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void desassociarPagamentoCategoria(Long pagamentoId, Long categoriaId) {
        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)||
        !pagamentoEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            throw new DesassociationErrorException("a id desse pagamento " + pagamentoId + " " +
                    "não é associado a essa categoria com esse id: " + categoriaId);
        }

        //Desassociar de ambos os lados
        pagamentoEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);
        categoriaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);

        //Salvar e persistir os dados em ambos os lados
        pagamentosRepository.save(pagamentoEncontrado);
        categoriaFinanceiraRepository.save(categoriaEncontrada);
    }
}
