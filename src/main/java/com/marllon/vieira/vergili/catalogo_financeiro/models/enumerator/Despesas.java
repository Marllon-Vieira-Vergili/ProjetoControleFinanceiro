package com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public enum Despesas {

    CONTA_LUZ(1),
    CONTA_AGUA(2),
    CONTA_INTERNET(3),
    CARTAO_CREDITO(4),
    CONTA_TELEFONE(5),
    IMPOSTOS(6),
    LAZER(7),
    EMPRESTIMOS(8);


    //atributo para receber o valor dos enumerators
    private final int opcao;

    //Construtor padrão
    Despesas(int opcao) {
        this.opcao = opcao;
    }




    //Método customizado para encontrar a opção pelo número(se necessário)
    public static Despesas encontrarDespesaPeloNumero(int opcao){
        for(Despesas despesasEncontradas: Despesas.values()){
            if(despesasEncontradas.getOpcao() == opcao){
                return despesasEncontradas;
            }
        }
        return null;
    }
}
