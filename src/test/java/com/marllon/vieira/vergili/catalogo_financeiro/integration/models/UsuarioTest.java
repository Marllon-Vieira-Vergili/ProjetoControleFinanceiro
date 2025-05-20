package com.marllon.vieira.vergili.catalogo_financeiro.integration.models;

import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
public class UsuarioTest {


    Usuario usuarioTeste;


    @BeforeEach()
    public void instanciarUsuarioTeste(){
        usuarioTeste = new Usuario();
        ReflectionTestUtils.setField(usuarioTeste,"id",1L);
        usuarioTeste.setNome("teste");
        usuarioTeste.setTelefone("(11)11111-1111");
        usuarioTeste.setSenha("Teste123");
        usuarioTeste.setEmail("teste@email.com");
    }

    @Order(1)
    @Test
    @DisplayName("Teste do Atributo Usuário, se encontra equivalenta ao informado")
    public void verificarAtributoIdUsuario(){

        Long idEsperada = 1L;
        assertEquals(idEsperada,usuarioTeste.getId());
    }

    @Order(2)
    @Test
    @DisplayName("Teste do Atributo Usuário, se da null ao retornar valor nao existente")
    public void verificarSeAtributoIdRetornaNullQuandoRetornadoValorInexistente(){

        Long idNaoExistente = 0L;
        assertNotEquals(idNaoExistente,usuarioTeste.getId());
    }

    @Order(3)
    @Test
    @DisplayName("Teste do Atributo nome, verificar se passo o nome certo, ele vai funcionar")
    public void verificarSeValorNomePassadoEhEquivalente(){

       String nomeEsperado = "teste";

        assertEquals(nomeEsperado,usuarioTeste.getNome());
    }

    @Order(4)
    @Test
    @DisplayName("Teste do Atributo nome, verificar se passo um valor errado, ele não deve funcionar")
    public void verificarSeValorNomePassadoErradoIraDarErro(){

        String nomeEsperado = "123TesteJoao";

        assertNotEquals(nomeEsperado,usuarioTeste.getNome());
    }

    @Order(4)
    @Test
    @DisplayName("Teste do Atributo telefone, verificar se o atributo de telefone salva o mesmo valor que eu solcite")
    public void verificarSeOAtributoTelefoneSalvaIgualEuInformei(){

        String telefoneEsperado = "(11)11111-1111";

        assertEquals(telefoneEsperado,usuarioTeste.getTelefone());
    }

    @Order(5)
    @Test
    @DisplayName("Teste do Atributo telefone, verificar se o atributo de telefone não " +
            "salvará um valor diferente do formato que estipulei")
    public void atributoTelefoneNaoDeveSalvarTelefoneEmOutroFormatoDiferente(){

        String formatoErrado = "1111111-1111";
        assertNotEquals(formatoErrado,usuarioTeste.getTelefone());
    }

    @Order(6)
    @Test
    @DisplayName("Teste do Atributo senha, verificar se salvará uma senha no formato que pedi")
    public void atributoSenhaDeveSalvarNoFormatoPrimeiraMaiusculaComSeisCaracteres(){

        String senha = "Teste123";
        assertEquals(senha,usuarioTeste.getSenha());
    }

    @Order(7)
    @Test
    @DisplayName("Teste do Atributo senha, a senha não deve ser salva com requisitos diferentes dos passados")
    public void atributoSenhaNaoDeveraSerCriadaSeNaoTiverUmaMinusculaEMenosDeSeisCaracteres(){

        String senha = "teste123";
        assertNotEquals(senha,usuarioTeste.getSenha());

        String senhaComMenosDeSeisCaracteres = "Teste";
        assertNotSame(senhaComMenosDeSeisCaracteres.length(),usuarioTeste.getSenha().length());
    }

    @Order(8)
    @Test
    @DisplayName("Teste do Atributo email, o email deve ser o mesmo que foi informado")
    public void emailDeveSerOMesmoInformadoAqui() {

        String email = "teste@email.com";
        assertSame(email, usuarioTeste.getEmail());
    }

    @Order(9)
    @Test
    @DisplayName("Teste do Atributo email, o email não pode ser o mesmo que passei")
    public void emailnaoPodeSerOMesmo() {

        String email = "testeemail.com";
        assertNotSame(email, usuarioTeste.getEmail());
    }
}
