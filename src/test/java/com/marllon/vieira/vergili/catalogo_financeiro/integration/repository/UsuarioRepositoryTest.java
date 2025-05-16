package com.marllon.vieira.vergili.catalogo_financeiro.integration.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para testar os métodos personalizados do meu repositório Usuario
 * Métodos:existsByNomeAndEmailAndTelefone(booleano), encontrarUsuarioPorNome(Retorna Lista Usuario),
 * * encontrarUsuarioPeloEmail(Retorna Usuario), encontrarUsuarioPeloTelefone(retorna Usuario)
 */
@DataJpaTest
@TestPropertySource("/application-test.properties")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Test
    @Sql("/sql/UsuarioDados.sql")
    @DisplayName("Teste do método para verificar se ja tem um valor igual, com mesmo nome, email e telefone")
    public void testarMetodoVerificarSeJaExisteUmUsuarioIgual(){
        boolean usuarioEncontrado = usuarioRepository.existsByNomeAndEmailAndTelefone
                ("Maria Fernanda", "maria.fernanda@email.com", "(21)99876-5432");
        assertTrue(usuarioEncontrado,"O método deveria retornar que esses dados informados existe na base de dados");
    }
    @Test
    @Sql("/sql/UsuarioDados.sql")
    @DisplayName("Teste do método para verificar se ja tem um valor igual, mas com dados falsos")
    public void testarMetodoVerificarSeJaExisteUmUsuarioIgualNaoEncontraValorFalso(){
        boolean usuarioEncontrado = usuarioRepository.existsByNomeAndEmailAndTelefone
                ("Maria Fernandah", "maria.fernadnda@email.com", "(21)99876-5432");
        assertFalse(usuarioEncontrado,"O método não deveria informar que encontrou esses dados na base de dados, eles não existem");
    }

    @Test
    @Sql("/sql/UsuarioDados.sql")
    @DisplayName("Teste do método para encontrar o usuário pelo nome dele")
    public void testarMetodoEncontrarUsuarioPeloNome(){
        List<Usuario> usuarioEncontradoComEsseNome = usuarioRepository.encontrarUsuarioPorNome("João da Silva");
        for(Usuario usuarioLocalizado: usuarioEncontradoComEsseNome){
            assertEquals("João da Silva",usuarioLocalizado.getNome());
        }

    }

    @Test
    @Sql("/sql/UsuarioDados.sql")
    @DisplayName("Teste do método para encontrar o usuário pelo telefone dele")
    public void testarMetodoEncontrarUsuarioPeloSeuTelefone(){
        Usuario usuarioEncontradoComEsseTelefone = usuarioRepository.encontrarUsuarioPeloTelefone("(51)91234-0000");
        assertEquals("(51)91234-0000",usuarioEncontradoComEsseTelefone.getTelefone());
    }
    @Test
    @Sql("/sql/UsuarioDados.sql")
    @DisplayName("Teste do método para encontrar o usuário pelo email dele")
    public void testarMetodoEncontrarUsuarioPeloSeuEmail(){
        Usuario usuarioEncontradoComEsseEmail = usuarioRepository.encontrarUsuarioPeloEmail("erica.davila@email.com");
        assertEquals("erica.davila@email.com",usuarioEncontradoComEsseEmail.getEmail());
    }

}
