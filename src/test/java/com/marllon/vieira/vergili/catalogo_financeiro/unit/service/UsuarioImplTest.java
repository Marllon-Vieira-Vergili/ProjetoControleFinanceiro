package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.UsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.HistoricoTransacaoRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements.UsuarioImpl;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles(value = "test")
public class UsuarioImplTest {

    @Mock
    private UsuariosAssociation usuariosAssociation;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioImpl usuarioImplementation;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContaUsuarioRepository contaUsuarioRepository;

    @Mock
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Mock
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @BeforeEach
    public void inicializarMocks(){
        //MockitoAnnotations.AbrirMocks desta classe(this)
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("Usuários Services - Testes de Cenários de Sucesso")
    public class TesteMetodosUsuariosServicesEmCenariosDeSucesso{

        @Test
        @DisplayName("Criar Usuário - Cenário que funcionaria ao criar")
        public void criarUsuarioEmCenarioDeSucesso(){

            //Passando o request
            UsuarioRequest request = new UsuarioRequest
                    ("teste","teste@email.com","Teste123","(11)11111-1111");

            //Quando o método salvar o usuário..
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(
                    invocationOnMock -> {
                        Usuario usuarioNovo = invocationOnMock.getArgument(0);
                        ReflectionTestUtils.setField(usuarioNovo,"id",1L);
                        return usuarioNovo;
                    });

            when(usuarioMapper.retornarDadosUsuario(any(Usuario.class)))
                    .thenAnswer(invocationOnMock -> {
                        return new UsuarioResponse
                                (1L,"teste","teste@email.com","(11)11111-1111");
                    });

            //Chamando o método de resposta
            UsuarioResponse response = assertDoesNotThrow(()->usuarioImplementation.criarUsuario(request)
                    ,"Deverá criar o usuário sem retornar erro");

            //Instanciando uma resposta que eu espero que seja verdade
            UsuarioResponse respostaEsperadaDoUsuarioCriado = new UsuarioResponse
                    (1L,"teste","teste@email.com","(11)11111-1111");


           assertEquals(respostaEsperadaDoUsuarioCriado,response,
                   "O usuário criado deveria ser igual a resposta esperada");

           verify(usuarioRepository).save(any(Usuario.class));
           verify(usuarioMapper).retornarDadosUsuario(any(Usuario.class));
        }

        @Test
        @DisplayName("Encontrar usuário pela id - Cenário que encontraria")
        public void encontrarUsuarioPelaIdDeveEncontralo(){

            //Instanciando um usuário somente pra pegar uma ID pra encontrar
            Usuario usuarioTeste = new Usuario();
            ReflectionTestUtils.setField(usuarioTeste,"id",1L);
            usuarioTeste.setEmail("teste@email.com");
            usuarioTeste.setNome("teste");
            usuarioTeste.setTelefone("(11)11111-1111");

            //Resposta esperada que o Mapper deve retornar
            UsuarioResponse response = new UsuarioResponse(1L,"teste","teste@email.com"
                    ,"(11)11111-1111");

            //Quando o mock encontrar esse usuário teste...
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));

            when(usuarioMapper.retornarDadosUsuario(usuarioTeste)).thenReturn(response);

            Optional<UsuarioResponse> usuarioEncontrado = Optional.of
                    (usuarioImplementation.encontrarUsuarioPorId(usuarioTeste.getId())).orElseThrow();

            assertTrue(usuarioEncontrado.isPresent(),"O usuário deveria ser encontrado por essa Id");

            verify(usuarioRepository).findById(usuarioTeste.getId());
        }
    }

    @Nested
    @DisplayName("Usuários Services - Testes de Cenários de Falhas, erros e Exceptions")
    public class TesteMetodosUsuariosServicesEmCenariosDeErrosFalhas{

        @Test
        @DisplayName("Usuários Services - Cenário que ja existe usuário criado pelo email")
        public void criarUsuarioEmCenarioQueJaExiste(){

            //Passando o request
            UsuarioRequest request = new UsuarioRequest
                    ("teste","teste@email.com","Teste123","(11)11111-1111");


            //Suponhando que desejamos criar um usuário novo, primeiro verificamos se o usuário já não existe
            when(usuarioRepository.existsByEmail(request.email()))
                    .thenReturn(true);


            assertThrows(JaExisteException.class,()->usuarioImplementation.criarUsuario(request)
                    ,"O método deve retornar a exceção " +
                            "Já existe exception, pois já foi criado um usuário por esse email");


            verify(usuarioRepository).existsByEmail(request.email());
        }

        @Test
        @DisplayName("Encontrar usuário pela id - Cenário que retornaria UsuarioNaoEncontradoException")
        public void metodoEncontrarUsuarioPelaIdDeveRetornarUsuarioNaoEncontrado(){

            //Instanciando um valor errado
            UsuarioResponse response = new
                    UsuarioResponse(3L,"teste","teste@email.com","(11)11111-1111");

        }
    }
}


