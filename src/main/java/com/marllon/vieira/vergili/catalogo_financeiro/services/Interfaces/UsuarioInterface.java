package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.UsuarioResponse;
import java.util.List;

public interface UsuarioInterface {

    /**Esta interface é para criação dos métodos CRUDS(CREATE, READ, UPDATE, DELETE) relacionado somente a
     * Usuario.
     *
     * A maioria dos métodos, retorna o DTO(Data Transfer Object) no valor
     *
     */

    //Criar (novo usuário)
    UsuarioResponse criarNovoUsuario(UsuarioRequest usuario);

    //Ler
    UsuarioResponse encontrarUsuarioPorId(Long id);
    List<UsuarioResponse> encontrarTodosUsuarios();
    UsuarioResponse encontrarUsuarioPorNome(String nome);

    //Atualizar
    UsuarioResponse atualizarDadosUsuario(Long id, UsuarioRequest usuario);

    //Remover
    UsuarioResponse removerUsuarioPorId(Long id);
    UsuarioResponse removerUsuarioPorNome(String nome);
}
