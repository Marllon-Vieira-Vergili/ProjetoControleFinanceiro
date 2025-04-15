package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {


    //Métodos customizados do repositório de Usuários

    //Encontrar o usuário pelo Nome dele
    @Query("SELECT u FROM Usuarios u WHERE u.nome =: nomeUsuario")
    Usuarios encontrarUsuarioPorNome(@Param("nomeUsuario") Usuarios nomeUsuario);

    //Encontrar o usuário pelo Email dele
    @Query("SELECT u FROM Usuarios u WHERE u.email =: emailUsuario")
    Usuarios encontrarUsuarioPeloEmail(@Param("emailUsuario") Usuarios emailUsuario);

    //Encontrar o usuário pelo telefone dele
    @Query("SELECT u FROM Usuarios u WHERE u.telefone =: telefoneUsuario")
    Usuarios encontrarUsuarioPeloTelefone(@Param("telefoneUsuario") Usuarios telefoneUsuario);

}
