package com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * ENUMERATOR para mostrar opções tipos de categorias, seja despesa, receita, etc.
 * */


@Getter(AccessLevel.PUBLIC)
public enum TiposCategorias {

    DESPESA(1),
    RECEITA(2);


    //Atributo para escolher a opção dos Enums
    public final int valor;


    //Construtor para os enums pegar os valores em números, se chamados
    TiposCategorias(int valor) {
        this.valor = valor;
    }


    //Criando um método para buscar a categoria pelo numero dela(se necessário)
    public static Optional<TiposCategorias> buscarCategoriasPeloNumero(int valor){
        for(TiposCategorias tipoCategoria: TiposCategorias.values()){
            if(tipoCategoria.getValor() == valor){
                return Optional.of(tipoCategoria);
            }
        }
        return Optional.empty();
    }

    //Criando um método para buscar a categoria pelo nome dela(se necessario)
    public static Optional<TiposCategorias> buscarCategoriasPeloNome(String nome){
        for(TiposCategorias tipoCategoria: TiposCategorias.values()){
            if(tipoCategoria.name().equalsIgnoreCase(nome)){
                return Optional.of(tipoCategoria);
            }
        }
        return Optional.empty();
    }





    /**Criando um método para caso o usuário decida escolher o valor "DESPESA", será mostrado todos os outros
    valores de despesa a ele
     */
    public static List<SubTipoCategoria> mostrarTodasDespesas() {

        return (Arrays.stream(SubTipoCategoria.values()).filter(
                subTipoCategoria ->
                        subTipoCategoria.tiposCategorias.name().contains(TiposCategorias.DESPESA.name())).
                toList());
    }

    public static List<SubTipoCategoria> mostrarTodasReceitas() {
        return (Arrays.stream(SubTipoCategoria.values()).filter(
                subTipoCategoria -> subTipoCategoria.tiposCategorias.name().contains
                        (RECEITA.name())).toList());
        }
    }

