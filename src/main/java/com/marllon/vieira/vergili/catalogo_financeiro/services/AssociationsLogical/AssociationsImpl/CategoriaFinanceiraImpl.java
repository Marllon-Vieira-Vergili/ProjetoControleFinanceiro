package com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.AssociationsImpl;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.SubTipoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;

public class CategoriaFinanceiraImpl implements CategoriaFinanceiraAssociation {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;



    @Override
    public void associarCategoriaComConta(Long categoriaId, Long contaId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId).orElseThrow(()
                -> new CategoriaNaoEncontrada("Categoria não foi encontrada"));

        ContaUsuario contaUsuarioEncontrada = contaUsuarioRepository.findById(contaId).orElseThrow(()
                -> new ContaNaoEncontrada("Conta não foi encontrada"));

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
    public void associarCategoriaComPagamento(Long categoriaId, Long pagamentoId) {

        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(categoriaId).orElseThrow(()
                -> new CategoriaNaoEncontrada("Categoria não foi encontrada"));

        Pagamentos pagamentoEncontrado = pagamentosRepository.findById(pagamentoId).orElseThrow(()
                -> new ContaNaoEncontrada("Pagamento não foi encontrado"));

        if(categoriaEncontrada.getPagamentosRelacionados() == null){
            categoriaEncontrada.setPagamentosRelacionados(new ArrayList<>());
        }

        if(!categoriaEncontrada.getPagamentosRelacionados().contains(pagamentoEncontrado)){
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
    public void associarCategoriaComTransacao(Long categoriaId, Long transacaoId) {

    }

    @Override
    public void associarCategoriaComUsuario(Long categoriaId, Long usuarioId) {

    }

    @Override
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
    public void desassociarCategoriaAConta(Long categoriaId, Long contaId) {

    }

    @Override
    public void desassociarCategoriaAPagamento(Long categoriaId, Long pagamentoId) {

    }

    @Override
    public void desassociarCategoriaTransacao(Long categoriaId, Long transacaoId) {

    }

    @Override
    public void desassociarCategoriaUsuario(Long categoriaId, Long usuarioId) {

    }

    @Override
    public void desassociarCategoriaCriadaComDespesa(Long categoriaId) {

//Obter o id da categoria
        CategoriaFinanceira categoriaCriadaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(()->new CategoriaNaoEncontrada("Categoria com a id: " + categoriaId + " não foi encontrada"));

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
    public void desassociarCategoriaCriadaComReceita(Long categoriaId) {

        //Obter o id da categoria
        CategoriaFinanceira categoriaCriadaEncontrada = categoriaFinanceiraRepository.findById(categoriaId)
                .orElseThrow(()->new CategoriaNaoEncontrada("Categoria com a id: " + categoriaId + " não foi encontrada"));

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
