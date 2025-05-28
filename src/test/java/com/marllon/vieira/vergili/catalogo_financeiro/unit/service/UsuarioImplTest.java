package com.marllon.vieira.vergili.catalogo_financeiro.unit.service;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.UsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas.CONTA_CORRENTE;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas.CONTA_POUPANCA;
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
    public void inicializarMocks() {
        //MockitoAnnotations.AbrirMocks desta classe(this)
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    @DisplayName("Usuários Services - Testes de Cenários de Sucesso")
    public class TesteMetodosUsuariosServicesEmCenariosDeSucesso {

        @Test
        @DisplayName("Criar Usuário - Cenário que funcionaria ao criar")
        public void criarUsuarioEmCenarioDeSucesso() {

            //Passando o request
            UsuarioRequest request = new UsuarioRequest
                    ("teste", "teste@email.com", "Teste123", "(11)11111-1111");

            //Quando o método salvar o usuário..
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(
                    invocationOnMock -> {
                        Usuario usuarioNovo = invocationOnMock.getArgument(0);
                        ReflectionTestUtils.setField(usuarioNovo, "id", 1L);
                        return usuarioNovo;
                    });

            when(usuarioMapper.retornarDadosUsuario(any(Usuario.class)))
                    .thenAnswer(invocationOnMock -> {
                        return new UsuarioResponse
                                (1L, "teste", "teste@email.com", "(11)11111-1111");
                    });

            //Chamando o método de resposta
            UsuarioResponse response = assertDoesNotThrow(() -> usuarioImplementation.criarUsuario(request)
                    , "Deverá criar o usuário sem retornar erro");

            //Instanciando uma resposta que eu espero que seja verdade
            UsuarioResponse respostaEsperadaDoUsuarioCriado = new UsuarioResponse
                    (1L, "teste", "teste@email.com", "(11)11111-1111");


            assertEquals(respostaEsperadaDoUsuarioCriado, response,
                    "O usuário criado deveria ser igual a resposta esperada");

            verify(usuarioRepository).save(any(Usuario.class));
            verify(usuarioMapper).retornarDadosUsuario(any(Usuario.class));
        }

        @Test
        @DisplayName("Encontrar usuário pela id - Cenário que encontraria")
        public void encontrarUsuarioPelaIdDeveEncontralo() {

            //Instanciando um usuário somente pra pegar uma ID pra encontrar
            Usuario usuarioTeste = new Usuario();
            ReflectionTestUtils.setField(usuarioTeste, "id", 1L);
            usuarioTeste.setEmail("teste@email.com");
            usuarioTeste.setNome("teste");
            usuarioTeste.setTelefone("(11)11111-1111");

            //Resposta esperada que o Mapper deve retornar
            UsuarioResponse response = new UsuarioResponse(1L, "teste", "teste@email.com"
                    , "(11)11111-1111");

            //Quando o mock encontrar esse usuário teste...
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));

            when(usuarioMapper.retornarDadosUsuario(usuarioTeste)).thenReturn(response);

            Optional<UsuarioResponse> usuarioEncontrado = Optional.of
                    (usuarioImplementation.encontrarUsuarioPorId(usuarioTeste.getId())).orElseThrow();

            assertTrue(usuarioEncontrado.isPresent(), "O usuário deveria ser encontrado por essa Id");

            verify(usuarioRepository).findById(usuarioTeste.getId());
        }

        @Test
        @DisplayName("Usuarios Services - Teste de Cenário que encontra Usuarios criados pelo nome")
        public void deveEncontrarPeloNomeERetornarEmUmaLista() {

            //Instanciando dados para teste

            Usuario usuario1 = new Usuario();
            usuario1.setTelefone("(11)11111-1111");
            usuario1.setNome("teste");
            usuario1.setSenha("Teste123");
            usuario1.setEmail("teste@email.com");

            Usuario usuario2 = new Usuario();
            usuario2.setTelefone("(22)22222-2222");
            usuario2.setNome("teste");
            usuario2.setSenha("Teste123");
            usuario2.setEmail("teste2@email.com");

            //Colocando os valores numa lista
            List<Usuario> usuariosEncontrados = List.of(usuario1, usuario2);

            //Mock dos métodos que esse método principal chama, pra simular dele
            when(usuarioRepository.encontrarUsuarioPorNome("teste")).thenReturn(usuariosEncontrados);
            when(usuarioMapper.retornarDadosUsuario(any(Usuario.class)))
                    .thenAnswer(invocationOnMock -> {
                        Usuario usuarioResponse = invocationOnMock.getArgument(0);
                        return new UsuarioResponse(usuarioResponse.getId()
                                , usuarioResponse.getNome()
                                , usuarioResponse.getEmail()
                                , usuarioResponse.getTelefone());
                    });
            //Assertando que ele nao vai jogar exceção de usuarionaoencontrado
            assertDoesNotThrow(() -> {
                List<UsuarioResponse> resultado = usuarioImplementation.buscarUsuarioPorNome("teste");
                assertFalse(resultado.isEmpty(), "O resultado não pode voltar vazio");
                assertEquals("teste", resultado.get(0).nome());
            });

            //Verificando se os valores foram chamados
            verify(usuarioRepository).encontrarUsuarioPorNome("teste");
            verify(usuarioMapper,times(2)).retornarDadosUsuario(any(Usuario.class));
        }
        @Test
        @DisplayName("Cenário de sucesso ao Buscar contas associadas pela id do usuário")
        public void deveBuscarContasAssociadasAoUsuarioPelaId(){

            //Id do usuário
            Long idUsuario = 1L;
            Usuario usuario = new Usuario();
            ReflectionTestUtils.setField(usuario,"id",idUsuario);
            usuario.setEmail("teste@email.com");
            usuario.setNome("teste");
            usuario.setTelefone("(11)11111-1111");


            //Conta usuario
            Long idContaUsuario = 1L;
            ContaUsuario contaUsuarioTeste = new ContaUsuario();
            ReflectionTestUtils.setField(contaUsuarioTeste,"id",idContaUsuario);
            contaUsuarioTeste.setUsuarioRelacionado(usuario);
            usuario.setContasRelacionadas(new ArrayList<>(List.of(contaUsuarioTeste)));

            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

            assertDoesNotThrow(()->{
                List<ContaUsuarioResponse> responseContaUsuario =
                        usuarioImplementation.buscarContasAssociadas(idUsuario);
                //assertTrue(responseContaUsuario.contains(usuario)),"O usuário deve estar contido associado a essa conta");
                assertEquals(1,usuario.getContasRelacionadas().size(),"A lista deve conter 1 objeto de usuário");

            });

            verify(usuarioRepository).findById(idUsuario);

        }

        @Test
        @DisplayName("Cenário de Sucesso onde é possível alterar a senha do usuário")
        public void deveConseguirAlterarSenhaUsuario(){

            //Instanciando um usuário para teste
            Long idUsuario = 1L;
            Usuario usuario = new Usuario();
            ReflectionTestUtils.setField(usuario,"id",idUsuario);
            usuario.setSenha("Teste123");
            usuario.setEmail("email@email.com");
            usuario.setTelefone("(11)11111-1111");
            usuario.setNome("teste");

            //Simulando quando o usuário repositorio encontrar essa id, ele deve retornar esse usuario acima
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

            assertDoesNotThrow(()->
                    usuarioImplementation.alterarSenhaUsuario(idUsuario,"Teste1234"));

            assertEquals("Teste1234",usuario.getSenha(),"A senha deve ser igual a da expectativa");
        }

        @Test
        @DisplayName("Deve encontrar o usuário pelo seu tipo de Conta Retornando True ou False")
        public void deveEncontrarOUsuarioDependendoDoTipoContaInformadoSeTrueOuFalse(){

            //Instanciando valores somente para testar agora
            Long contaUsuarioId = 1L;
            ContaUsuario contaUsuario = new ContaUsuario();
            ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);
            contaUsuario.setTipoConta(CONTA_POUPANCA);

            Long usuarioId = 1L;
            Usuario usuarioEncontrado = new Usuario();
            ReflectionTestUtils.setField(usuarioEncontrado,"id",usuarioId);

            usuarioEncontrado.setContasRelacionadas(new ArrayList<>(List.of(contaUsuario)));
            contaUsuario.setUsuarioRelacionado(usuarioEncontrado);

            //Quando.. o método de procurar o usuario pela Id for chamado, ele deve retornar...
            when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioEncontrado));

            //assertando que ele vai retornar True quando encontrar o usuário pelo tipo de conta
            boolean usuarioTemConta = usuarioImplementation.usuarioTemContaTipo(usuarioId,TiposContas.CONTA_POUPANCA);

            assertTrue(usuarioTemConta,"O usuário deveria retornar TRUE, pois ele tem conta do tipo CONTA_POUPANÇA");
        }

        @Test
        @DisplayName("Deve retornar TRUE se um usuário existe pela ID informada")
        public void deveRetornarTRUESeUmUsuarioExistirPelaIdInformada(){

            //Instanciando um usuário vazio somente para ver se o método aceita
            Long idUsuario = 1L;
            Usuario usuarioExiste = new Usuario();
            ReflectionTestUtils.setField(usuarioExiste,"id",idUsuario);

            //Quando o repositorio chamar o metodo exists by id, ele deve retornar true
            when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

            boolean usuarioFoiEncontrado = usuarioImplementation.existePelaId(idUsuario);

            assertTrue(usuarioFoiEncontrado,"O usuário deveria ser encontrado e retornado TRUE no método");
        }

        @Test
        @DisplayName("Deve retornar uma página com todos os usuários criados - Sucesso")
        public void deveRetornarUmaPaginaComTodosUsuariosCriados() {
            Long usuarioId1 = 1L;
            Long usuarioId2 = 2L;

            Usuario usuario1 = new Usuario();
            ReflectionTestUtils.setField(usuario1, "id", usuarioId1);
            usuario1.setNome("Usuario 1");

            Usuario usuario2 = new Usuario();
            ReflectionTestUtils.setField(usuario2, "id", usuarioId2);
            usuario2.setNome("Usuario 2");

            List<Usuario> listaUsuarios = List.of(usuario1, usuario2);

            when(usuarioRepository.findAll()).thenReturn(listaUsuarios);

            UsuarioResponse response1 = new UsuarioResponse(usuarioId1, "Usuario 1", "teste@email.com", "(11)11111-1111");
            UsuarioResponse response2 = new UsuarioResponse(usuarioId2, "Usuario 2", "teste2@email.com", "(22)22222-2222");

            when(usuarioMapper.retornarDadosUsuario(usuario1)).thenReturn(response1);
            when(usuarioMapper.retornarDadosUsuario(usuario2)).thenReturn(response2);

            Pageable pageable = PageRequest.of(0, 10);
            Page<UsuarioResponse> resultado = assertDoesNotThrow(() -> usuarioImplementation.buscarTodosUsuarios(pageable));

            assertNotNull(resultado);
            assertEquals(2, resultado.getTotalElements());
            assertTrue(resultado.getContent().contains(response1));
            assertTrue(resultado.getContent().contains(response2));

            verify(usuarioRepository).findAll();
            verify(usuarioMapper).retornarDadosUsuario(usuario1);
            verify(usuarioMapper).retornarDadosUsuario(usuario2);
        }

        @Test
        @DisplayName("Teste Mètodo remover Usuario - Sucesso")
        public void metodoRemoverUsuarioDeveFuncionar(){
            
        }


        @Nested
        @DisplayName("Usuários Services - Testes de Cenários de Falhas, erros e Exceptions")
        public class TesteMetodosUsuariosServicesEmCenariosDeErrosFalhas {

            @Test
            @DisplayName("Usuários Services - Cenário que ja existe usuário criado pelo email")
            public void criarUsuarioEmCenarioQueJaExiste() {

                //Passando o request
                UsuarioRequest request = new UsuarioRequest
                        ("teste", "teste@email.com", "Teste123", "(11)11111-1111");


                //Suponhando que desejamos criar um usuário novo, primeiro verificamos se o usuário já não existe
                when(usuarioRepository.existsByEmail(request.email()))
                        .thenReturn(true);


                assertThrows(JaExisteException.class, () -> usuarioImplementation.criarUsuario(request)
                        , "O método deve retornar a exceção " +
                                "Já existe exception, pois já foi criado um usuário por esse email");


                verify(usuarioRepository).existsByEmail(request.email());
            }

            @Test
            @DisplayName("Encontrar usuário pela id - Cenário que retornaria UsuarioNaoEncontradoException")
            public void metodoEncontrarUsuarioPelaIdDeveRetornarUsuarioNaoEncontrado() {

                //Instanciando um valor errado
                Long idInexistente = 5L;

                when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

                assertThrows(UsuarioNaoEncontrado.class, () -> {
                    usuarioImplementation.encontrarUsuarioPorId(idInexistente);
                });

                verify(usuarioRepository).findById(idInexistente);
            }

            @Test
            @DisplayName("Encontrar Usuário pelo nome - Cenário que não encontrou")
            public void encontrarUsuarioPeloNomeDeveRetornarUsuarioNaoEncontradoException(){

                //Mockando o metodo retorna lista vazia
                when(usuarioRepository.encontrarUsuarioPorNome("teste"))
                        .thenReturn(List.of());

                //Deve lancar exceção corretamente
                assertThrowsExactly(UsuarioNaoEncontrado.class,()->{
                    List<UsuarioResponse> resposta = usuarioImplementation.buscarUsuarioPorNome("teste");
                    assertTrue(resposta.isEmpty(),"Não deveria retornar nada");
                });

                //Analisar se o repositorio foi chamado
                verify(usuarioRepository).encontrarUsuarioPorNome("teste");
            }

            @Test
            @DisplayName("Buscar contas associadas - Deve retornar Exceção se não tiver associacao")
            public void buscarContasAssociadasDeveRetornarExcecao(){

                //Instanciando um usuário sem conta associada
                Long usuarioId = 1L;
                Usuario usuarioSemConta = new Usuario();
                ReflectionTestUtils.setField(usuarioSemConta,"id",usuarioId);
                usuarioSemConta.setTelefone("(11)11111-1111");
                usuarioSemConta.setSenha("Teste123");


                when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioSemConta));

                assertThrowsExactly(ContaNaoEncontrada.class,()->{
                    List<ContaUsuarioResponse> contasAssociadas = usuarioImplementation
                            .buscarContasAssociadas(usuarioId);
                    assertTrue(contasAssociadas.isEmpty(),"As contas associadas deveriam estar vazias");
                });
            }

            @Test
            @DisplayName("Se Não encontrar o usuário pelo seu tipo de Conta Retorne False")
            public void deveEncontrarOUsuarioDependendoDoTipoContaInformadoSeTrueOuFalse(){

                //Instanciando valores somente para testar agora
                Long contaUsuarioId = 1L;
                ContaUsuario contaUsuario = new ContaUsuario();
                ReflectionTestUtils.setField(contaUsuario,"id",contaUsuarioId);
                contaUsuario.setTipoConta(CONTA_POUPANCA);

                Long usuarioId = 1L;
                Usuario usuarioEncontrado = new Usuario();
                ReflectionTestUtils.setField(usuarioEncontrado,"id",usuarioId);

                usuarioEncontrado.setContasRelacionadas(new ArrayList<>(List.of(contaUsuario)));
                contaUsuario.setUsuarioRelacionado(usuarioEncontrado);

                //Quando.. o método de procurar o usuario pela Id for chamado, ele deve retornar...
                when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioEncontrado));

                //assertando que ele vai retornar True quando encontrar o usuário pelo tipo de conta
                boolean usuarioTemConta = usuarioImplementation.usuarioTemContaTipo(usuarioId, CONTA_CORRENTE);

                assertFalse(usuarioTemConta,"O método deveria retornar FALSE, " +
                        "pois O Usuario possui conta POUPANÇA, e foi solicitado no método conta CORRENTE");

            }

            @Test
            @DisplayName("Deve retornar FALSE se um usuário não existir pela id informada")
            public void deveRetornarFALSESeUmUsuarioExistirPelaIdInformada(){

                //Instanciando um usuário vazio somente para ver se o método aceita
                Long idUsuario = 1L;
                Usuario existePelaId1 = new Usuario();
                ReflectionTestUtils.setField(existePelaId1,"id",idUsuario);

                //Quando o repositorio chamar o metodo exists by id, ele deve retornar true
                when(usuarioRepository.existsById(2L)).thenReturn(false);

                boolean usuarioFoiEncontrado = usuarioImplementation.existePelaId(2L);

                assertFalse(usuarioFoiEncontrado,"O usuário não deveria ser encontrado e retornado FALSE no método");
            }
        }
    }
}


