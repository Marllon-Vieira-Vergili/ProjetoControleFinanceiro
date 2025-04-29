package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;


public interface UsuariosService {

    // ======================== OPERAÇÕES CRUD BÁSICAS ========================

    /**
     * Cria um novo usuário com os dados fornecidos.
     *
     * @param usuario dados do novo usuário
     * @return {@code UsuarioResponse} com os dados persistidos
     * @throws DadosInvalidosException se os dados forem inválidos ou incompletos
     */
    UsuarioResponse criarUsuario(UsuarioRequest usuario, ContaUsuarioRequest conta);

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id identificador único do usuário
     * @return {@code Optional<UsuarioResponse>} com os dados, se encontrado
     * @throws UsuarioNaoEncontrado se nenhum usuário for localizado
     */
    Optional<UsuarioResponse> buscarUsuarioPorId(Long id);

    /**
     * Busca usuários que contenham o nome especificado.
     *
     * @param nome nome do usuário (parcial ou completo)
     * @return lista de usuários encontrados
     */
    List<UsuarioResponse> buscarUsuarioPorNome(String nome);

    /**
     * Busca usuários associados a uma conta específica.
     *
     * @param contaId ID da conta
     * @return lista de usuários associados à conta
     */
    List<UsuarioResponse> buscarPorContaId(Long contaId);

    /**
     * Retorna todos os usuários cadastrados de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com usuários encontrados
     */
    Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable);

    /**
     * Remove um usuário do sistema.
     *
     * @param id identificador único do usuário a ser removido
     * @throws UsuarioNaoEncontrado se o usuário não existir
     */
    void deletarUsuario(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Retorna todas as contas associadas a um usuário específico.
     *
     * @param usuarioId ID do usuário
     * @return lista de contas associadas
     * @throws UsuarioNaoEncontrado se o usuário não for encontrado
     */
    List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId);

    // ======================== MÉTODOS DE VALIDAÇÕES ========================

    /**
     * Verifica se o usuário possui uma conta do tipo especificado.
     *
     * @param usuarioId ID do usuário
     * @param tipoConta tipo da conta (ex: CONTA_CORRENTE, POUPANCA)
     * @return {@code true} se o usuário possuir, {@code false} caso contrário
     */
    boolean usuarioTemContaTipo(Long usuarioId, TiposContas tipoConta);

    /**
     * Verifica se existe um usuário com o ID fornecido.
     *
     * @param id ID do usuário
     * @return {@code true} se existir, {@code false} se não
     */
    boolean existePelaId(Long id);

    /**
     * Verifica se já existe um usuário com as mesmas informações fornecidas.
     *
     * @param usuario dados do usuário a ser comparado
     * @return {@code true} se um usuário idêntico já existir, {@code false} se não
     */
    boolean existeUsuarioIgual(UsuarioRequest usuario);
}
