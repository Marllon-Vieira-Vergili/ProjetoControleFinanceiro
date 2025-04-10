package com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public enum TiposCategorias {

    DESPESA(1),
    RECEITA(2),
    INVESTIMENTO(3);


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
}

