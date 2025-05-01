package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias.RECEITA;

@Service
public class CategoriaFinanceiraImpl implements CategoriaFinanceiraService {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private CategoriaFinanceiraAssociation categoriaFinanceiraAssociation;

    @Autowired
    private CategoriaFinanceiraMapper categoriaFinanceiraMapper;


    @Override
    public CategoriaFinanceiraResponse criarCategoriaFinanceira(CategoriaFinanceiraRequest request) {

        //Criar uma nova categoria
        CategoriaFinanceira categoriaNova = new CategoriaFinanceira();

        categoriaNova.setTiposCategorias(request.tipoCategoria());
        categoriaNova.setSubTipo(request.subtipo());

        //Verificar se o tipo é receita, ou despesa
        if(tipoCategoriaExiste(request.tipoCategoria())){
            if(seCategoriaForReceita()){
                categoriaFinanceiraAssociation.associarSubTipoCategoriaComReceita(request.tipoCategoria(),request.subtipo());
            }else if (seCategoriaForDespesa()){
                categoriaFinanceiraAssociation.associarSubTipoCategoriaComDespesa(request.tipoCategoria(),request.subtipo());
            }else{
                throw new DadosInvalidosException("Os dados digitados são inválidos! Deve-Se adicionar" +
                        "o tipo da categoria relacionado ao seu subtipo da categoria");
            }
        }


        //Salvar a categoria financeira
        categoriaFinanceiraRepository.save(categoriaNova);


        //Retornar ao usuário
        return categoriaFinanceiraMapper.retornarDadosCategoria(categoriaNova);
    }

    @Override
    public Optional<CategoriaFinanceiraResponse> encontrarCategoriaPorId(Long id) {

        //Encontrar a categoria já criada pela ID
        CategoriaFinanceira categoriaEncontradaPelaId = getCategoriaById(id);

        return Optional.ofNullable(categoriaFinanceiraMapper.retornarDadosCategoria(categoriaEncontradaPelaId));
    }

    @Override
    public List<CategoriaFinanceiraResponse> encontrarCategoriaCriadaPeloSubTipo(Long id, SubTipoCategoria subTipo) {

        //Encontrar a categoria já criada pela ID
        CategoriaFinanceira categoriaEncontradaPelaId = getCategoriaById(id);

        //Verificar se essa categoria contem um subtipo associado
        if (categoriaEncontradaPelaId.getSubTipo().equals(subTipo)) {

            return Collections.singletonList(categoriaFinanceiraMapper
                    .retornarDadosCategoria(categoriaEncontradaPelaId));
        }else{
            throw new CategoriaNaoEncontrada("Não foi encontrado nenhuma categoria criada com essa id informado," +
                    " e esse subtipo informado");
        }
    }

    @Override
    public Page<CategoriaFinanceiraResponse> encontrarTodasCategorias(Pageable pageable) {

        Page<CategoriaFinanceira>
                todasCategoriasEncontradas = categoriaFinanceiraRepository.findAll(pageable);

        if(todasCategoriasEncontradas.isEmpty()){
            throw new CategoriaNaoEncontrada("Não há nenhuma categoria salva na base de dados");
        }
        List<CategoriaFinanceiraResponse> categoriaFinanceiraResponses=
                todasCategoriasEncontradas.stream()
                .map(categoriaFinanceira -> new
                        CategoriaFinanceiraResponse(categoriaFinanceira.getId(),
                        categoriaFinanceira.getTiposCategorias(),
                        categoriaFinanceira.getSubTipo())).toList();

        return new PageImpl<>(categoriaFinanceiraResponses);
    }

    @Override
    public CategoriaFinanceiraResponse atualizarUmaCategoriaCriada
            (Long idCategoria, CategoriaFinanceiraRequest novosDados) {

        //Encontrar a categoria pela id


        CategoriaFinanceira categoriaAtualizada = getCategoriaById(idCategoria);
        //Obter os novos dados
        categoriaAtualizada.setTiposCategorias(novosDados.tipoCategoria());
        categoriaAtualizada.setSubTipo(novosDados.subtipo());


        //Verificar se o tipo é receita, ou despesa
        if(tipoCategoriaExiste(novosDados.tipoCategoria())){
            if(seCategoriaForReceita()){
                categoriaFinanceiraAssociation.associarSubTipoCategoriaComReceita(novosDados.tipoCategoria(),novosDados.subtipo());
            }else if (seCategoriaForDespesa()){
                categoriaFinanceiraAssociation.associarSubTipoCategoriaComDespesa(novosDados.tipoCategoria(), novosDados.subtipo());
            }else{
                throw new DadosInvalidosException("Os dados digitados são inválidos! Deve-Se adicionar" +
                        "o tipo da categoria relacionado ao seu subtipo da categoria");
            }

        }

        //Salvar a categoria financeira
        categoriaFinanceiraRepository.save(categoriaAtualizada);


        //Retornar ao usuário
        return categoriaFinanceiraMapper.retornarDadosCategoria(categoriaAtualizada);


    }

    @Override
    public void deletarCategoria(Long id) {

        //Desassociar a categoria antes de deletá-la
        CategoriaFinanceira idEncontrado = getCategoriaById(id);

            if(seCategoriaForDespesa()){
                categoriaFinanceiraAssociation.desassociarCategoriaCriadaComDespesa(idEncontrado.getId());

            } else if (seCategoriaForReceita()) {
                categoriaFinanceiraAssociation.desassociarCategoriaCriadaComReceita(idEncontrado.getId());
            } else{
                throw new DesassociationErrorException("Erro ao desassociar do tipo Categoria ");
            }



        try{
            categoriaFinanceiraAssociation.desassociarCategoriaUsuario(id, idEncontrado.getUsuarioRelacionado().getId());

        } catch (DesassociationErrorException e) {
            throw new DesassociationErrorException("Erro ao desassociar de Usuario:  " + e.getMessage());
        }

        try{
           categoriaFinanceiraAssociation.desassociarCategoriaAConta(id,idEncontrado.getContaRelacionada().getId());
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Conta Relacionada:  " + e.getMessage());
        }

        try{
            for(Pagamentos pagamentosEncontrados: idEncontrado.getPagamentosRelacionados()){
                categoriaFinanceiraAssociation.desassociarCategoriaAPagamento(id, pagamentosEncontrados.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Pagamentos relacionados:  " + e.getMessage());
        }

        try{
            for(HistoricoTransacao historicosEncontrados: idEncontrado.getTransacoesRelacionadas()){
                categoriaFinanceiraAssociation.desassociarCategoriaTransacao(id, historicosEncontrados.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Histórico transações relacionados:  " + e.getMessage());
        }

        //Deletar a categoria Financeira
        categoriaFinanceiraRepository.deleteById(id);
    }

    @Override
    public boolean seCategoriaForDespesa() {

        //Verificar se o tipo é despesa
        Optional<TiposCategorias> tiposCategorias =
                TiposCategorias.buscarCategoriasPeloNome(DESPESA.name());
        //Se tiver presente, retornar
        return tiposCategorias.isPresent();
    }


    @Override
    public boolean seCategoriaForReceita() {

        //Verificar se o tipo é receita
        Optional<TiposCategorias> tiposCategorias =
                TiposCategorias.buscarCategoriasPeloNome(RECEITA.name());

        return tiposCategorias.isPresent();
    }

    @Override
    public boolean tipoCategoriaExiste(TiposCategorias tipoCategoria) {
        return Arrays.asList(TiposCategorias.values()).contains(tipoCategoria);
    }




    @Override
    public boolean jaExisteUmaCategoriaIgual(CategoriaFinanceira dadosCategoria) {
        List<CategoriaFinanceira> todasCategoriasEncontradas = categoriaFinanceiraRepository.findAll();
            return todasCategoriasEncontradas.stream().anyMatch(categoriaFinanceira ->
                    categoriaFinanceira.equals(dadosCategoria));
    }

    @Override
    public CategoriaFinanceira getCategoriaById(Long id) {
        return categoriaFinanceiraRepository.findById(id).orElseThrow(()
                -> new CategoriaNaoEncontrada("Não foi encontrado nenhuma categoria com essa id " + id +  " informada"));
    }
}
