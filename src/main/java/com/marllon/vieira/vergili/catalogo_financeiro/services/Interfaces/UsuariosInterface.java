package com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.receive.entities.UsuariosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuariosResponse;
import java.util.List;

public interface UsuariosInterface {

    //Interface para criação dos Métodos CRUDS SEPARADAMENTE, um por entidade

    //Criar (novo usuário)
    UsuariosResponse criarNovoUsuario(UsuariosRequest usuario);

    //Ler
    UsuariosResponse encontrarUsuarioPorId(Long id);
    List<UsuariosResponse> encontrarTodosUsuarios();
    UsuariosResponse encontrarUsuarioPorNome(String nome);

    //Atualizar
    UsuariosResponse atualizarDadosUsuario(Long id, UsuariosRequest usuario);

    //Remover
    UsuariosResponse removerUsuarioPorId(Long id);
    UsuariosResponse removerUsuarioPorNome(String nome);
}
