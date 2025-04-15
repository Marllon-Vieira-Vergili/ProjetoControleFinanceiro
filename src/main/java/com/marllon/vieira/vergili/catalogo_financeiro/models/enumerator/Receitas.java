package com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator;


import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public enum Receitas {

    SALARIO(1),
    PRESENTE(2),
    HERANCA(3),
    DIVIDENDOS(4),
    RENDA_FIXA(5),
    ALUGUEL(6),
    OUTROS(7);


    //atributo para receber o valor dos enumerators
    private final int opcao;


    //Construtor padrão
    Receitas(int opcao) {
        this.opcao = opcao;
    }


    //Método customizado para encontrar a opção pelo número(se necessário)
    public static Receitas encontrarReceitaPeloNumero(int opcao){
        for(Receitas receitaEncontrada: Receitas.values()){
            if(receitaEncontrada.getOpcao() == opcao){
                return receitaEncontrada;
            }
        }
        return null;
    }
}

