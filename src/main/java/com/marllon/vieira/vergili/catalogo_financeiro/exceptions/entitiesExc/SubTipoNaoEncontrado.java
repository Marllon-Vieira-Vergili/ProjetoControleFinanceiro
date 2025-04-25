package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc;

import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;

import java.util.Arrays;

public class SubTipoNaoEncontrado extends RuntimeException {
    public SubTipoNaoEncontrado(String message) {
        super("Este SubTipo de conta  não foi encontrado na base de dados. " +
                "os dados encontrados para o subtipo de conta é: : " + Arrays.toString(SubTipoCategoria.values()));
    }
}
