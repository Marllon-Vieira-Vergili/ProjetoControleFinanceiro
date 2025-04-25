package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;

import java.util.Arrays;

public class TiposCategoriasNaoEncontrado extends RuntimeException {
    public TiposCategoriasNaoEncontrado(String message) {
        super("Este tipo de categoria não foi encontrado na base de dados. " +
                "os dados encontrados para o tipo de categoria são: " + Arrays.toString(TiposCategorias.values()));
    }
}
