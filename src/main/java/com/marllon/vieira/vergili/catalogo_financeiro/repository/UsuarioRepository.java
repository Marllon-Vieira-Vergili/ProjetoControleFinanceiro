package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //Métodos customizados do repositório de Usuários

    //Encontrar o usuário pelo Nome dele
    @Query("SELECT u FROM Usuario u WHERE u.nome =: nomeUsuario")
    Usuario encontrarUsuarioPorNome(@Param("nomeUsuario") Usuario nomeUsuario);

    //Encontrar o usuário pelo Email dele
    @Query("SELECT u FROM Usuario u WHERE u.email =: emailUsuario")
    Usuario encontrarUsuarioPeloEmail(@Param("emailUsuario") Usuario emailUsuario);

    //Encontrar o usuário pelo telefone dele
    @Query("SELECT u FROM Usuario u WHERE u.telefone =: telefoneUsuario")
    Usuario encontrarUsuarioPeloTelefone(@Param("telefoneUsuario") Usuario telefoneUsuario);

}
