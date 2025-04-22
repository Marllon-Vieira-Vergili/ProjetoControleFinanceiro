package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;

import java.util.List;

public interface UsuarioService {

    /**
     * Interface para representar os métodos separadamente de acordo com cada entidade representada,
     * neste caso, todos os métodos representados somente a entidade ("Usuário")
     *
     */

    //Criar (novo usuário)
    Usuario criarNovoUsuario(UsuarioRequest usuario);

    //Ler
    Usuario encontrarUsuarioPorId(Long id);
    List<Usuario> encontrarTodosUsuarios();
    List<Usuario> encontrarUsuarioPorNome(String nome); //Pode ter mais de um usuário com mesmo nome

    //Atualizar
    Usuario atualizarDadosUsuario(Long id, UsuarioRequest usuario);

    //Remover
    Usuario removerUsuarioPorId(Long id);

}
