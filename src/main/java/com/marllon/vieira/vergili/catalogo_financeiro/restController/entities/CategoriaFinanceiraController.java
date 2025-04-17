package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.CategoriaFinanceiraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categoria")
@Tag(name = "Métodos da Categoria Financeira, sem associação (apenas para teste)")
public class CategoriaFinanceiraController {


    @Autowired
    private CategoriaFinanceiraService categoriaFinanceiraService;


    @GetMapping(value = "/obterCategoriaId/{id}")
    public CategoriaFinanceiraResponse encontrarCategoriaPorId(@PathVariable Long id){
        return categoriaFinanceiraService.encontrarCategoriaPorId(id);
    }

    @GetMapping(value = "/obterTodasCategorias")
    public List<CategoriaFinanceiraResponse> obterTodasCategorias(){
        return categoriaFinanceiraService.encontrarTodasCategorias();
    }

    @GetMapping(value = "/obterCategoria/{tipo}")
    public List<CategoriaFinanceiraResponse> encontrarCategoriaPorTipo(@PathVariable TiposCategorias tipo){
        return categoriaFinanceiraService.encontrarCategoriasPorTipo(tipo);
    }

    @PostMapping(value = "/adicionarCategoria/{id}")
    public CategoriaFinanceiraResponse adicionarCategoria(@RequestBody CategoriaFinanceiraRequest categoria){
        return categoriaFinanceiraService.criarCategoria(categoria);
    }

    @PutMapping(value = "/atualizarCategoria/{id}")
    public CategoriaFinanceiraResponse atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaFinanceiraRequest categoria){
        return categoriaFinanceiraService.atualizarCategoria(id, categoria);
    }


    @DeleteMapping(value = "/removerCategoriaPorId/{id}")
    public CategoriaFinanceiraResponse removerCategoriaPorId(@PathVariable Long id){
        return categoriaFinanceiraService.removerCategoria(id);
    }
}
