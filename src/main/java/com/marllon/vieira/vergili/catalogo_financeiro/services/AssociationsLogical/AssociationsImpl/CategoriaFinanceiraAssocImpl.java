package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.*;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.TiposCategorias.RECEITA;

@Service
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaFinanceiraAssocImpl implements CategoriaFinanceiraAssociation {


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
    @Transactional
    public void associarCategoriaComConta(Long categoriaId, Long contaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);
        ContaUsuario contaUsuarioEncontrada = contaUsuarioService.getContaById(contaId);

        if(categoriaEncontrada.getContaRelacionada() == null ||
                !contaUsuarioEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            //Associação bidirecional
            try{
                categoriaEncontrada.setContaRelacionada(contaUsuarioEncontrada);
                contaUsuarioEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
            } catch (RuntimeException e) {
                throw new AssociationErrorException("Erro ao associar a categoria com a conta" +
                        e.getMessage());
            }
        }
        //salvar ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        contaUsuarioRepository.save(contaUsuarioEncontrada);
    }

    @Override
    @Transactional
    public void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);

        if(categoriaEncontrada.getPagamentosRelacionados() == null){
            categoriaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        if(pagamentoEncontrado.getCategoriasRelacionadas() == null){
            pagamentoEncontrado.setCategoriasRelacionadas(new ArrayList<>());
        }

        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)
                || !pagamentoEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            try{
                categoriaEncontrada.getPagamentosRelacionados().add(pagamentoEncontrado);
                pagamentoEncontrado.getCategoriasRelacionadas().add(categoriaEncontrada);

            } catch (RuntimeException e) {
                throw new AssociationErrorException(e.getMessage());
            }
        }

        //Salvar em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);
    }

    @Override
    @Transactional
    public void associarCategoriaComTransacao(Long categoriaId, Long transacaoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        HistoricoTransacao transacaoEncontrada = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);

        if(categoriaEncontrada.getTransacoesRelacionadas() == null){
            categoriaEncontrada.setTransacoesRelacionadas(new ArrayList<>());
        }
        try{
            if(!categoriaEncontrada.getTransacoesRelacionadas().contains(transacaoEncontrada) ||
                    !transacaoEncontrada.getCategoriasRelacionadas().contains(categoriaEncontrada)){
                categoriaEncontrada.getTransacoesRelacionadas().add(transacaoEncontrada);
                transacaoEncontrada.getCategoriasRelacionadas().add(categoriaEncontrada);
            }
        } catch (Exception e) {
            throw new AssociationErrorException("Não foi possível realizar a associação da categoria com a transação");
        }

        //Salvar em ambos os lados
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(transacaoEncontrada);


    }

    @Override
    @Transactional
    public void associarCategoriaComUsuario(Long categoriaId, Long usuarioId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);

        // Verifica se a categoria já está associada
        if (categoriaEncontrada.getUsuarioRelacionado().getId().equals(usuarioEncontrado.getId()) ||
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
    @Transactional
    public void associarSubTipoCategoriaComDespesa(TiposCategorias tipoCategoriaDespesa,
                                                   SubTipoCategoria subTipoDespesa) {

        //Obter o tipo de categoria DESPESA
        String tipocategoriaFinanceira = tipoCategoriaDespesa.name();
        if(tipocategoriaFinanceira.equalsIgnoreCase(DESPESA.name())){
            if(subTipoDespesa.getTiposCategorias().name().contains(DESPESA.name())){
                //Associar o tipo de categoria ao subtipo de despesa selecionado,
                // para a categoria financeira
                CategoriaFinanceira categoriaFinanceira = new CategoriaFinanceira();
                categoriaFinanceira.setTiposCategorias(tipoCategoriaDespesa);
                categoriaFinanceira.setSubTipo(subTipoDespesa);
            } else if (tipocategoriaFinanceira.equalsIgnoreCase(RECEITA.name())) {
                if(subTipoDespesa.getTiposCategorias().name().contains(RECEITA.name())){
                    throw new AssociationErrorException("Erro ao Associar o subtipo RECEITA, só pode ser associado tipo Despesa");
                }
            }
        }else{
            throw new SubTipoNaoEncontrado("Não foi encontrado um subtipo que é associado ao tipo de despesa, por favor, tente novamente!");
        }
    }

    @Override
    @Transactional
    public void associarSubTipoCategoriaComReceita(TiposCategorias tipoCategoriaReceita,
                                                   SubTipoCategoria subTipoReceita) {

        //Obter o tipo de categoria RECEITA
        String tipocategoriaFinanceira = tipoCategoriaReceita.name();
        if(tipocategoriaFinanceira.equalsIgnoreCase(RECEITA.name())){
            if(subTipoReceita.getTiposCategorias().name().contains(RECEITA.name())){
                //Associar o tipo de categoria ao subtipo de receita selecionado,
                // para a categoria financeira
                CategoriaFinanceira categoriaFinanceira = new CategoriaFinanceira();
                categoriaFinanceira.setTiposCategorias(tipoCategoriaReceita);
                categoriaFinanceira.setSubTipo(subTipoReceita);
            } else if (tipocategoriaFinanceira.equalsIgnoreCase(DESPESA.name())) {
                if(subTipoReceita.getTiposCategorias().name().contains(DESPESA.name())){
                    throw new AssociationErrorException("Erro ao Associar o subtipo DESPESA, só pode ser associado tipo Receita");
                }
            }
        }else{
            throw new SubTipoNaoEncontrado("Não foi encontrado um subtipo que é associado ao tipo de Receita, por favor, tente novamente!");
        }



    }


    @Override
    @Transactional
    public void desassociarCategoriaAConta(Long categoriaId, Long contaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        ContaUsuario contaEncontrada = contaUsuarioService.getContaById(contaId);

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
    @Transactional
    public void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);


        Pagamentos pagamentoEncontrado = pagamentosService.getPagamentoById(pagamentoId);

        //Verificar se esse pagamento está associado
        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado) ||
                !pagamentoEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)){
            throw new DesassociationErrorException("a id dessa categoria " + categoriaId + " " +
                    "não é associado a esse pagamento com essa id: " + pagamentoId);
        }

        //Senão.. desassociar em ambos os lados
        categoriaEncontrada.getPagamentosRelacionados().remove(pagamentoEncontrado);
        pagamentoEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);

        //Salvar as alterações realizadas
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        pagamentosRepository.save(pagamentoEncontrado);

    }

    @Override
    @Transactional
    public void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        HistoricoTransacao historicoTransacaoEncontrado = historicoTransacaoService.getHistoricoTransacaoById(transacaoId);

        //Verificar se ja possui uma associação dessas
        if(!categoriaEncontrada.getTransacoesRelacionadas().contains(historicoTransacaoEncontrado)||
        !historicoTransacaoEncontrado.getCategoriasRelacionadas().contains(categoriaEncontrada)){

            throw new DesassociationErrorException("a id dessa categoria " + categoriaId + " " +
                    "não é associado a esse histórico de transação com essa id: " + transacaoId);
        }

        //Desassociar em ambos os lados bidirecionalmente
        categoriaEncontrada.getTransacoesRelacionadas().remove(historicoTransacaoEncontrado);
        historicoTransacaoEncontrado.getCategoriasRelacionadas().remove(categoriaEncontrada);

        //Salvar as alterações
        categoriaFinanceiraRepository.save(categoriaEncontrada);
        historicoTransacaoRepository.save(historicoTransacaoEncontrado);
    }

    @Override
    @Transactional
    public void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);

        Usuario usuarioEncontrado = usuariosService.getUsuarioById(usuarioId);

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

    @Override
    @Transactional
    public void desassociarCategoriaCriadaComDespesa(Long categoriaId) {

//Obter o id da categoria
        CategoriaFinanceira categoriaCriadaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);
        if(categoriaCriadaEncontrada.getTiposCategorias().equals(DESPESA)){
            if(categoriaCriadaEncontrada.getSubTipo().name().equals(DESPESA.name())){
                //desassociar o tipo de categoria ao subtipo de despesa selecionado,
                // para a categoria financeira
                categoriaCriadaEncontrada.setSubTipo(null);
                categoriaCriadaEncontrada.setTiposCategorias(null);
                //Salvando no banco de dados as desassociações
                categoriaFinanceiraRepository.save(categoriaCriadaEncontrada);
            }
        }
        if(categoriaCriadaEncontrada.getTiposCategorias().equals(RECEITA)){
            throw new DadosInvalidosException("Dados inválidos! Por favor, verifique se essa if informada " +
                    "possui uma categoria com o tipo DESPESA CRIADA");
        }
        else{
            throw new DesassociationErrorException("Não foi possível desassociar uma categoria de uma despesa");
        }
    }


    @Override
    @Transactional
    public void desassociarCategoriaCriadaComReceita(Long categoriaId) {

        //Obter o id da categoria
        CategoriaFinanceira categoriaCriadaEncontrada = categoriaFinanceiraService.getCategoriaById(categoriaId);
        if(categoriaCriadaEncontrada.getTiposCategorias().equals(RECEITA)){
            if(categoriaCriadaEncontrada.getSubTipo().name().equals(RECEITA.name())){
                //desassociar o tipo de categoria ao subtipo de receita selecionado,
                // para a categoria financeira
                categoriaCriadaEncontrada.setSubTipo(null);
                categoriaCriadaEncontrada.setTiposCategorias(null);
                //Salvando no banco de dados as desassociações
                categoriaFinanceiraRepository.save(categoriaCriadaEncontrada);
            }
        }
        if(categoriaCriadaEncontrada.getTiposCategorias().equals(DESPESA)){
            throw new DadosInvalidosException("Dados inválidos! Por favor, verifique se essa if informada " +
                    "possui uma categoria com o tipo RECEITA CRIADA");
        }
        else{
            throw new DesassociationErrorException("Não foi possível desassociar uma categoria de uma receita");
        }
    }


}
