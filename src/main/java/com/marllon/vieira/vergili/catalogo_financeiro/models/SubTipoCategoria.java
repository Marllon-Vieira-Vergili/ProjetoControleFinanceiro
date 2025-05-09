package com.marllon.vieira.vergili.catalogo_financeiro.models;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Optional;

@Getter(AccessLevel.PUBLIC)
public enum SubTipoCategoria {

    /**
     * Enums para associar ao Subtipo da Categoria
     */

    //Despesas
    CONTA_LUZ(1,TiposCategorias.DESPESA),
    CONTA_AGUA(2, TiposCategorias.DESPESA),
    CONTA_INTERNET(3,TiposCategorias.DESPESA),
    CARTAO_CREDITO(4,TiposCategorias.DESPESA),
    CONTA_TELEFONE(5,TiposCategorias.DESPESA),
    IMPOSTOS(6,TiposCategorias.DESPESA),
    LAZER(7,TiposCategorias.DESPESA),
    COMBUSTIVEL(8,TiposCategorias.DESPESA),
    ALIMENTACAO(9,TiposCategorias.DESPESA),
    DESPESA_ALUGUEL(10,TiposCategorias.DESPESA),
    EMPRESTIMOS(11, TiposCategorias.DESPESA),

//Receitas
    SALARIO(20,TiposCategorias.RECEITA),
    PRESENTE(21,TiposCategorias.RECEITA),
    HERANCA(22,TiposCategorias.RECEITA),
    DIVIDENDOS(23,TiposCategorias.RECEITA),
    RENDA_FIXA(24,TiposCategorias.RECEITA),
    RENDA_ALUGUEL(25,TiposCategorias.RECEITA),
    OUTROS(26,TiposCategorias.RECEITA);


    //Atributo para escolher a opção dos enums
    public final int valor;

    public final TiposCategorias tiposCategorias;

    //Construtor para os enums pegar os valores em números, se chamados
    SubTipoCategoria(int valor,TiposCategorias tiposCategorias) {
        this.valor = valor;
        this.tiposCategorias = tiposCategorias;
    }


    //Método para encontrar pelo nome
    public static Optional<SubTipoCategoria> encontrarSubTipoCategoriaPorNome(String nome){
        for(SubTipoCategoria subtipoEncontrado: SubTipoCategoria.values()){
            if(subtipoEncontrado.name().equalsIgnoreCase(nome)){
                return Optional.of(subtipoEncontrado);
            }
        }
        return Optional.empty();
    }

    //Método customizado para encontrar a opção pelo número(se necessário)
    public static SubTipoCategoria encontrarSubTipoCategoriaPeloNumero(int numero){
        for(SubTipoCategoria subtipoEncontrado: SubTipoCategoria.values()){
            if(subtipoEncontrado.getValor() == numero){
                return subtipoEncontrado;
            }
        }
        return null;
    }
    //Método para retornar valores RECEITA
    public boolean isTipoCategoriaReceita(){
        for(SubTipoCategoria subtipoEncontrado: SubTipoCategoria.values()){
            if(subtipoEncontrado.getTiposCategorias().equals(TiposCategorias.RECEITA)){
                return true;
            }
        }
        return false;
    }
    //Método para retornar valores DESPESA
    public boolean isTipoCategoriaDespesa(){
        for(SubTipoCategoria subtipoEncontrado: SubTipoCategoria.values()){
            if(subtipoEncontrado.getTiposCategorias().equals(TiposCategorias.DESPESA)){
                return true;
            }
        }
        return false;
    }
}
