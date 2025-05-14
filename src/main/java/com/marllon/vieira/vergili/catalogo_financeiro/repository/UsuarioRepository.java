package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelas operações de persistência da entidade {@link Usuario}.
 * Fornece operações CRUD e consultas personalizadas baseadas em nome, email e telefone.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Verifica se existe um usuário com o mesmo nome, email e telefone.
     * Utilizado para evitar duplicidade de registros.
     *
     * @param nome o nome do usuário
     * @param email o email do usuário
     * @param telefone o telefone do usuário
     * @return true se existir um usuário com os dados fornecidos
     */
    boolean existsByNomeAndEmailAndTelefone(String nome, String email, String telefone);

    /**
     * Busca todos os usuários que possuem um nome exato.
     *
     * @param nomeUsuario o nome do usuário a ser buscado
     * @return uma lista de {@link Usuario} com o nome correspondente
     */
    @Query("SELECT u FROM Usuario u WHERE u.nome = :nomeUsuario")
    List<Usuario> encontrarUsuarioPorNome(@Param("nomeUsuario") String nomeUsuario);

    /**
     * Busca um usuário pelo email.
     *
     * @param emailUsuario o email do usuário
     * @return o {@link Usuario} correspondente, ou null se não encontrado
     */
    @Query("SELECT u FROM Usuario u WHERE u.email = :emailUsuario")
    Usuario encontrarUsuarioPeloEmail(@Param("emailUsuario") String emailUsuario);

    /**
     * Busca um usuário pelo telefone.
     *
     * @param telefoneUsuario o telefone do usuário
     * @return o {@link Usuario} correspondente, ou null se não encontrado
     */
    @Query("SELECT u FROM Usuario u WHERE u.telefone = :telefoneUsuario")
    Usuario encontrarUsuarioPeloTelefone(@Param("telefoneUsuario") String telefoneUsuario);
}
