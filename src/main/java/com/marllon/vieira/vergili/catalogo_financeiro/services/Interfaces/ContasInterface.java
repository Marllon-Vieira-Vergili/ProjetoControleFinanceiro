package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.ContasRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContasResponse;
import java.util.List;

public interface ContasInterface {

    //Interface para criação dos Métodos CRUDS SEPARADAMENTE, um por entidade


    //Criar (nova conta)

    ContasResponse criarNovaConta(ContasRequest conta);

    //Ler
    ContasResponse encontrarContaPorId(Integer id);
    List<ContasResponse> encontrarTodasContas();
    ContasResponse encontrarContaPorNome(String nome);

    //Atualizar
    ContasResponse atualizarConta(Integer id, ContasRequest conta);

    //Remover
    ContasResponse removerContaPorId(Integer id);
    ContasResponse removerContaPorNome(String nome);
}
