package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.CategoriaFinanceiraInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categoria")
@Tag(name = "Métodos da Categoria Financeira, sem associação (apenas para teste")
public class CategoriaFinanceiraController {


    @Autowired
    private CategoriaFinanceiraInterface categoriaFinanceiraInterface;


    @GetMapping(value = "/obterCategoriaId/{id}")
    public CategoriaFinanceiraResponse encontrarCategoriaPorId(@PathVariable Long id){
        return categoriaFinanceiraInterface.encontrarCategoriaPorId(id);
    }

    @GetMapping(value = "/obterTodasCategorias")
    public List<CategoriaFinanceiraResponse> obterTodasCategorias(){
        return categoriaFinanceiraInterface.encontrarTodasCategorias();
    }

    @GetMapping(value = "/obterCategoria/{tipo}")
    public CategoriaFinanceiraResponse encontrarCategoriaPorTipo(@RequestBody CategoriaFinanceiraRequest tipo){
        return categoriaFinanceiraInterface.encontrarCategoriaPorTipo(tipo);
    }
}
