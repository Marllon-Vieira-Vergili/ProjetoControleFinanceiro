package com.marllon.vieira.vergili.catalogo_financeiro.integration.service;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase
@Transactional
public class UsuarioIntegrationServiceTest {

    @Autowired
    private UsuariosService usuarioService;

    @Autowired
    private UsuariosAssociation usuariosAssociation;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbc;


    Usuario usuario;
    @AfterEach
    public void instanciarUmUsuarioParaTeste(){
        //Criando um objeto para teste
        usuario.setTelefone("(11)11111-1111");
        usuario.setNome("teste");
        usuario.setSenha("Teste123");
        usuario.setEmail("teste@email.com");

        //Salvando o valor no banco de dados de teste
        usuarioRepository.save(usuario);
    }
/*
    @BeforeEach
    public void limparDadosUsuarioAposCadaTeste(){
        jdbc.execute("DELETE FROM usuarios");
    }

 */

    @Nested
    @DisplayName("Cenários de Sucesso - Métodos UsuarioService")
    public class TesteCenariosSucessoUsuarioService{

        @Test
        @DisplayName("Método Criar Usuário - Sucesso")
        public void criarUsuarioCenarioSucesso(){

            UsuarioRequest request = new UsuarioRequest(usuario.getNome(),
                    usuario.getEmail(), usuario.getSenha(), usuario.getTelefone());

            assertDoesNotThrow(()->usuarioService.criarUsuario(request));
        }
    }

    @Nested
    @DisplayName("Cenários de Erros - Métodos UsuarioService")
    public class TesteCenariosErrosUsuarioService{

    }
}
