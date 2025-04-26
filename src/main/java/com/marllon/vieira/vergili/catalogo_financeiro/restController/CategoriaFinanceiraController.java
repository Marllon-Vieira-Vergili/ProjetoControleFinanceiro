package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por expor os endpoints da API relacionados às categorias financeiras.
 *
 * Os métodos que serão adicionados aqui irão permitir operações como criação, atualização,
 * listagem, busca e exclusão de categorias financeiras, além de associações com outras entidades.
 */
@RestController
@RequestMapping(value = "/api/categoria")
@Tag(name = "Métodos da Categoria Financeira", description = "Operações relacionadas ao gerenciamento de categorias financeiras, como receitas e despesas.")
public class CategoriaFinanceiraController {

    // Métodos de manipulação de CategoriaFinanceira serão implementados aqui futuramente.

}
