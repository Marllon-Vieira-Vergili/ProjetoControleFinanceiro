package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.CategoriaFinanceiraAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.CategoriaFinanceiraAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.ICategoria;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/categoriaAssociada")
@Tag(name = "Cruds de associação da parte da categoria")
public class CategoriaFinanAssociationController {

    @Autowired
    private ICategoria categoria;

    @PostMapping(value = "/criarCategoriaEassociacoes")
    public CategoriaFinanceiraAssociationResponse criarCategoriaJaAssociada
            (@RequestBody CategoriaFinanceiraAssociationRequest categoriaDados){
        return categoria.criarEAssociarCategoria(categoriaDados);
    }

}
