package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.CategoriaRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriasContas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.CategoriaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaInterfaceImpl  implements CategoriaInterface {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponse criarCategoriaDeConta(CategoriaRequest categoriaConta) {

        //Criar a nova categoria
        CategoriasContas novaCategoria = new CategoriasContas();
        novaCategoria.setTiposCategorias(categoriaConta.tipoCategoria());

        //Verificar se a descrição ou o Enum tipo categoria está incorreto
        if(novaCategoria.getTiposCategorias() == null){
           throw new IllegalArgumentException("Por favor, é necessário que coloque um tipo de categoria");
        }

        //Verificar se essa categoria de conta ja não está criada no banco de dados

        //Criar a nova categoria
        categoriaRepository.save(novaCategoria);

        //Retornar ao usuário a resposta da nova categoria de conta criada
        return new CategoriaResponse(novaCategoria.getId(),
                novaCategoria.getTiposCategorias());
    }

    @Override
    public CategoriaResponse encontrarCategoriaPorId(Integer id) {
        return null;
    }

    @Override
    public List<CategoriaResponse> encontrarTodasCategorias() {
        return List.of();
    }

    @Override
    public CategoriaResponse encontrarCategoriaPorTipo(TiposCategorias categoria) {
        return null;
    }

    @Override
    public CategoriaResponse atualizarCategoriaDeConta(Integer id, CategoriaRequest categoriaConta) {
        return null;
    }

    @Override
    public CategoriaResponse removerCategoriaDeContaPorId(Integer id) {
        return null;
    }

    @Override
    public CategoriaResponse removerCategoriaDeContaPorNome(String nome) {
        return null;
    }
}
