package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;

import java.util.Arrays;

public class TiposCategoriasNaoEncontrado extends RuntimeException {
    public TiposCategoriasNaoEncontrado(String message) {
        super("Este tipo de categoria não foi encontrado na base de dados. " +
                "os dados encontrados para o tipo de categoria são: " + Arrays.toString(TiposCategorias.values()));
    }
}
