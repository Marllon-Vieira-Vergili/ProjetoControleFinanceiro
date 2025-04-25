package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;

import java.util.List;

public interface ContaUsuarioService {

    /**
     * Interface para representar os métodos separadamente de acordo com cada entidade representada,
     * neste caso, todos os métodos representados somente a entidade ("Conta de Usuário")
     *
     */
    //Criar (nova conta)

    ContaUsuario criarNovaConta(ContaUsuarioRequest conta);

    //Ler
    ContaUsuario encontrarContaPorId(Long id);
    ContaUsuario encontrarContaPorNome(String nome);
    List<ContaUsuario> encontrarTodasContas();

    //Atualizar
    ContaUsuario atualizarConta(Long id, ContaUsuarioRequest conta);

    //Remover
    ContaUsuario removerContaPorId(Long id);


    //Método para salvar em outras entidades
    void salvarNovaContaUsuario(ContaUsuario novaConta);
}
