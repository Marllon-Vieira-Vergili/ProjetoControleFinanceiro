package com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator;
import lombok.AccessLevel;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;


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
    public static TiposCategorias buscarCategoriasPeloNumero(int valor){
        for(TiposCategorias tipoCategoria: TiposCategorias.values()){
            if(tipoCategoria.getValor() == valor){
                return tipoCategoria;
            }
        }
        return null;
    }

    //Criando um método para buscar a categoria pelo nome dela(se necessario)
    public static boolean buscarCategoriasPeloNome(String nome){
        for(TiposCategorias tipoCategoria: TiposCategorias.values()){
            if(tipoCategoria.name().equals(nome)){
                return true;
            }
        }
        return false;
    }





    /**Criando um método para caso o usuário decida escolher o valor "DESPESA", será mostrado todos os outros
    valores de despesa a ele
     */
    public static List<Despesas> mostrarTodasDespesas() {
        return Arrays.asList(Despesas.values());
    }

    public static List<Receitas> mostrarTodasReceitas() {
        return Arrays.asList(Receitas.values());
        }
    }

