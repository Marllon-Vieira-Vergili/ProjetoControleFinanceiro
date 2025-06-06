package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioUpdateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
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
    UsuarioResponse criarUsuario(UsuarioRequest usuario);

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id identificador único do usuário
     * @return {@code UsuarioResponse com os dados, se encontrado
     * @throws UsuarioNaoEncontrado se nenhum usuário for localizado
     */
    Optional<UsuarioResponse> encontrarUsuarioPorId(Long id);

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
    List<UsuarioResponse> buscarPorIdConta(Long contaId);

    /**
     * Retorna todos os usuários cadastrados de forma paginada.
     *
     * @param pageable parâmetros de paginação
     * @return página com usuários encontrados
     */
    Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable);

    /**
     * Atualiza os dados de um usuário existente com as informações fornecidas.
     * <p>
     * Este método deve ser utilizado para atualizar os dados cadastrais de um usuário.
     * Todos os campos presentes no {@link UsuarioRequest} substituirão os valores atuais do usuário.
     *
     * <p>
     * Caso queira permitir atualizações parciais (somente alguns campos), é recomendado
     * criar um DTO específico para atualização com campos opcionais.
     *
     * @param dadosUsuario objeto {@link UsuarioRequest} contendo os novos dados do usuário.
     *                     Todos os campos preenchidos neste objeto irão sobrescrever os dados existentes.
     * @return um {@link UsuarioResponse} contendo os dados atualizados do usuário.
     * @throws UsuarioNaoEncontrado se o usuário não for encontrado na base de dados.
     */
    UsuarioResponse atualizarDadosUsuario(Long id, UsuarioUpdateRequest dadosUsuario);


    /**
     * Remove um usuário do sistema.
     *
     * @param id identificador único do usuário a ser removido
     * @throws UsuarioNaoEncontrado se o usuário não existir
     */
    void deletarUsuario(Long id);

    // ======================== OPERAÇÕES ESPECÍFICAS ========================

    /**
     * Busca todas as contas associadas a um usuário específico.
     *
     * @param usuarioId ID do usuário a ser pesquisado.
     * @return Lista de contas associadas ao usuário.
     * @throws UsuarioNaoEncontrado se o usuário não for encontrado no sistema.
     */
    List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId);

    /**
     * Altera a senha de um usuário específico.
     *
     * @param id ID do usuário cuja senha será alterada.
     * @param novaSenha A nova senha a ser configurada.
     */
    void alterarSenhaUsuario(Long id, String novaSenha);

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
    boolean existeUsuarioIgualCriadoPeloEmail(String emailUsuario);
}
