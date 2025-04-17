package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import java.util.List;

public interface ContaUsuarioService {

    /**Esta interface é para criação dos métodos CRUDS(CREATE, READ, UPDATE, DELETE) relacionado somente as
     * ContaUsuario.
     *
     * A maioria dos métodos, retorna o DTO(Data Transfer Object) no valor
     *
     */

    //Criar (nova conta)

    ContaUsuarioResponse criarNovaConta(ContaUsuarioRequest conta);

    //Ler
    ContaUsuarioResponse encontrarContaPorId(Long id);
    List<ContaUsuarioResponse> encontrarTodasContas();

    //Atualizar
    ContaUsuarioResponse atualizarConta(Long id, ContaUsuarioRequest conta);

    //Remover
    boolean removerContaPorId(Long id);
}
