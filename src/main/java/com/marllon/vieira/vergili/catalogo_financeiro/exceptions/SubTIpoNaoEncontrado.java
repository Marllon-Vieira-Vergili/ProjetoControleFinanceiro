package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;

import java.util.Arrays;

public class SubTIpoNaoEncontrado extends RuntimeException {
    public SubTIpoNaoEncontrado(String message) {
        super("Este SubTipo de conta  não foi encontrado na base de dados. " +
                "os dados encontrados para o subtipo de conta é: : " + Arrays.toString(SubTipoCategoria.values()));
    }
}
