package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.CategoriaFinanceiraInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;


/**
 *
 * REVISAR TUDO ISSO AQUI
 */
@Service
public class CategoriaFinanceiraImpl implements CategoriaFinanceiraInterface {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Override
    public CategoriaFinanceiraResponse encontrarCategoriaPorId(Long id) {

        //Verificar se existe uma categoria de conta com o id informado, senao retornar exceção
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma categoria foi encontrada com essa id!"));

        //Retornar a categoria, se encontrada
        return new CategoriaFinanceiraResponse(categoriaEncontrada.getId(), categoriaEncontrada.getTiposCategorias());
    }

    @Override
    public List<CategoriaFinanceiraResponse> encontrarTodasCategorias() {

        //Encontrar todas as categorias ou retornar uma exceção se não forem encontradas
        List<CategoriaFinanceira> todasCategorias = categoriaFinanceiraRepository.findAll();

        if(todasCategorias.isEmpty()){
            throw new NoSuchElementException("não há nenhuma categoria financeira na lista");
        }
        //Retornar todas as categorias financeiras
        return todasCategorias.stream().map(categoriaFinanceira ->
                new CategoriaFinanceiraResponse(categoriaFinanceira.getId(), categoriaFinanceira.getTiposCategorias()))
                .toList();
    }

    @Override
    public CategoriaFinanceiraResponse encontrarCategoriaPorTipo(CategoriaFinanceiraRequest categoria) {

        //Encontrar todas as categorias
        List<CategoriaFinanceira> todasCategorias = categoriaFinanceiraRepository.findAll();

        if(!todasCategorias.isEmpty()){
            throw new NoSuchElementException("Não há nenhuma categoria financeira encontrada");
        }
        //Se todasCategorias contiver a categoria do tipo despesa
        if(categoria.tipoCategoria().equals(RECEITA)){
            //Vamos instanciar todos os tipos de valores de receita aqui, que é o Enum chamado "Receitas"
            TiposCategorias.mostrarTodasReceitas();
        }
        return null;
    }

}


