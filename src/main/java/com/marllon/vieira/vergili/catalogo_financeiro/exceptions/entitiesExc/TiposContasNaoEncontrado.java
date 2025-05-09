package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc;
import com.marllon.vieira.vergili.catalogo_financeiro.models.TiposContas;
import java.util.Arrays;

public class TiposContasNaoEncontrado extends RuntimeException {
    public TiposContasNaoEncontrado(String message) {
        super("Este tipo de conta não foi encontrado na base de dados. " +
                "os dados encontrados para o tipo de conta são: " + Arrays.toString(TiposContas.values()));
    }
}
