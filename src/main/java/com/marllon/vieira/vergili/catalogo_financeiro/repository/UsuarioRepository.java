package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //Métodos customizados do repositório de Usuários

    //Método customizado para comparação de valores(para não salvar o mesmo valor no banco de dados)
    boolean existsByNomeAndEmailAndTelefone(String nome, String email, String telefone);

    //Encontrar o usuário pelo Nome dele
    @Query("SELECT u FROM Usuario u WHERE u.nome =:nomeUsuario")
    Usuario encontrarUsuarioPorNome(@Param("nomeUsuario") String nomeUsuario);

    //Encontrar o usuário pelo Email dele
    @Query("SELECT u FROM Usuario u WHERE u.email =:emailUsuario")
    Usuario encontrarUsuarioPeloEmail(@Param("emailUsuario") String emailUsuario);

    //Encontrar o usuário pelo telefone dele
    @Query("SELECT u FROM Usuario u WHERE u.telefone =:telefoneUsuario")
    Usuario encontrarUsuarioPeloTelefone(@Param("telefoneUsuario") String telefoneUsuario);

}
