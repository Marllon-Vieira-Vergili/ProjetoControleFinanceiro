package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;
@Component
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaFinanceiraAssocImpl implements CategoriaFinanceiraAssociation {


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
    public void associarCategoriaComConta(Long categoriaId, Long contaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));

        ContaUsuario contaUsuarioEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não foi encontrada com essa id informada"));

        if(contaUsuarioEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada) &&
                categoriaEncontrada.getContaRelacionada() == (contaUsuarioEncontrada)){
            throw new AssociationErrorException("Erro ao associar a categoria com a conta");
            }

        //Associação bidirecional
        categoriaEncontrada.setContaRelacionada(contaUsuarioEncontrada);
        contaUsuarioEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);

        //salvar ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        contaUsuarioRepository.save(contaUsuarioEncontrada);
    }

    @Override
    public void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));
        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não foi encontrada com essa id informada"));

        if(categoriaEncontrada.getPagamentosRelacionados() == null){
            categoriaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }



        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                || pagamentoEncontrado.getCategoriaRelacionada() == null){
            try{
                categoriaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
                pagamentoEncontrado.setCategoriaRelacionada(categoriaEncontrada);

            } catch (RuntimeException e) {
                throw new AssociationErrorException(e.getMessage());
            }
        }

        //Salvar em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    public void associarCategoriaComTransacao(Long categoriaId, Long transacaoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));
        HistoricoTransacao transacaoEncontrada = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Historico transação não foi encontrada com essa id informada"));

        if(categoriaEncontrada.getTransacoesRelacionadas() == null){
            categoriaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        try{
            if(!categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada) ||
                    transacaoEncontrada.getCategoriaRelacionada() == null){
                categoriaEncontrada.getTransacoesRelacionadas().add(transacaoEncontrada);
                transacaoEncontrada.setCategoriaRelacionada(categoriaEncontrada);
            }
        } catch (Exception e) {
            throw new AssociationErrorException("Não foi possível realizar a associação da categoria com a transação");
        }

        //Salvar em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);


    }

    @Override
    public void associarCategoriaComUsuario(Long categoriaId, Long usuarioId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));
        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("usuario não foi encontrada com essa id informada"));

        // Verifica se a categoria já está associada
        if (categoriaEncontrada.getUsuarioRelacionado() != null && categoriaEncontrada
                .getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()) ||
                usuarioEncontrado.getCategoriasRelacionadas() != null &&
                usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)) {
            throw new AssociationErrorException("Esta categoria já está associada a um usuário.");
        }

        // Associa a categoria ao usuário
        categoriaEncontrada.setUsuarioRelacionado(usuarioEncontrado);

        // Inicializa a lista de categorias se for necessário e adiciona a categoria
        if (usuarioEncontrado.getCategoriasRelacionadas() == null) {
            usuarioEncontrado.setCategoriasRelacionadas(new ArrayList<>());
        }
        usuarioEncontrado.getCategoriasRelacionadas().add(categoriaEncontrada);

        // Salvar em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }


    @Override
    public void desassociarCategoriaAConta(Long categoriaId, Long contaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(() -> new ContaNaoEncontrada("Conta não foi encontrada com essa id informada"));


        if(!contaEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)
                || !categoriaEncontrada.getContaRelacionada().getId().equals(contaEncontrada.getId())){
            throw new DesassociationErrorException("A categoria não está associada a essa conta");
        }
            //Remoção dos 2 lados bidirecionalmente
            categoriaEncontrada.setContaRelacionada(null);
            contaEncontrada.getCategoriasRelacionadas().remove(categoriaEncontrada);

        //Salvar as alterações em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        contaUsuarioRepository.save(contaEncontrada);
    }

    @Override
    public void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId)
                .orElseThrow(() -> new PagamentoNaoEncontrado("Pagamento não foi encontrada com essa id informada"));

        //Verificar se esse pagamento está associado
        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
                pagamentoEncontrado.getCategoriaRelacionada() == null){
            throw new DesassociationErrorException("a id dessa categoria " + categoriaId + " " +
                    "não é associado a esse pagamento com essa id: " + pagamentoId);
        }

        //Senão.. desassociar em ambos os lados
        categoriaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.setCategoriaRelacionada(null);

        //Salvar as alterações realizadas
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);

    }

    @Override
    public void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));

        HistoricoTransacao historicoTransacaoEncontrado = historicoTransacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new HistoricoTransacaoNaoEncontrado("Historico transação não foi encontrada com essa id informada"));
        //Verificar se ja possui uma associação dessas
        if(!categoriaEncontrada.getTransacoesRelacionadas().contains(historicoTransacaoEncontrado)||
        historicoTransacaoEncontrado.getCategoriaRelacionada() == null){

            throw new DesassociationErrorException("a id dessa categoria " + categoriaId + " " +
                    "não é associado a esse histórico de transação com essa id: " + transacaoId);
        }

        //Desassociar em ambos os lados bidirecionalmente
        categoriaEncontrada.getTransacoesRelacionadas().remove(historicoTransacaoEncontrado);
        historicoTransacaoEncontrado.setCategoriaRelacionada(null);

        //Salvar as alterações
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(historicoTransacaoEncontrado);
    }

    @Override
    public void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNaoEncontrada("Categoria não foi encontrada com essa id informada"));

        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontrado("Usuario não foi encontrado com essa id informada"));

        //Verificar se a id do usuário informada está associado a essa categoria de contas
        if(!categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()) ||
                !usuarioEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            throw new DesassociationErrorException("a id dessa categoria " + categoriaId + " " +
                    "não é associado a esse usuário com essa id: " + usuarioId);
        }

        //Senão.. desassociar em ambos os lados
        categoriaEncontrada.setUsuarioRelacionado(null);
        usuarioEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);

        //Salvar as alterações
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        usuarioRepository.save(usuarioEncontrado);
    }

}
