package com.marllon.vieira.vergili.catalogo_financeiro.models.enums;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;


/**
 * ENUMERATOR para mostrar opções de Tipos de contas
 * */


@Getter(AccessLevel.PUBLIC)
public enum TiposContas {

    CONTA_POUPANCA(1),
    CONTA_SALARIO(2),
    CONTA_INVESTIMENTO(3),
    CONTA_CORRENTE(4);


    //Atributo para escolher a opção dos Enums
    public final int valor;


    //Construtor para os enums pegar os valores em números, se chamados
    TiposContas(int valor) {
        this.valor = valor;
    }


    //Criando um método para buscar o tipo de conta pelo numero dela(se necessário)
    public static TiposContas buscarContasPeloNumero(int valor){
        for(TiposContas tipoConta: TiposContas.values()){
            if(tipoConta.getValor() == valor){
                return tipoConta;
            }
        }
        return null;
    }

    //Criando um método para buscar o tipo de conta pelo nome dela(se necessario)
    public static boolean buscarTipoContaPeloNome(TiposContas conta){
        for(TiposContas tiposContas: TiposContas.values()){
            if(tiposContas.name().equals(conta.name())){
                return true;
            }
        }
        return false;
    }

    //Metodo para mostrar todos os tipos de contas
    public static List<TiposContas> todosTiposValidos(){
            return Arrays.asList(TiposContas.values());
        }
    }
