package com.marllon.vieira.vergili.catalogo_financeiro.repository;

import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositório responsável pelas operações de persistência da entidade {@link ContaUsuario}.
 * Estende JpaRepository para fornecer operações CRUD, além de métodos customizados
 * para busca por nome, saldo e tipo de conta.
 */
public interface ContaUsuarioRepository extends JpaRepository<ContaUsuario, Long> {

    /**
     * Verifica se já existe uma conta com o mesmo nome e saldo.
     * Útil para validar duplicidade antes de salvar uma nova conta.
     *
     * @param nome o nome da conta
     * @param saldo o saldo da conta
     * @return true se já existir uma conta com o mesmo nome e saldo
     */
    boolean existsByNomeAndSaldo(String nome, BigDecimal saldo);

    /**
     * Busca uma conta com base no nome.
     * Útil para buscas diretas por nome.
     *
     * @param nome o nome da conta
     * @return a {@link ContaUsuario} correspondente, ou null se não for encontrada
     */
    @Query("SELECT c FROM ContaUsuario c WHERE c.nome = :nome")
    List<ContaUsuario> encontrarContaPeloNome(@Param("nome") String nome);

    /**
     * Busca uma conta com base no saldo exato.
     * Pode ser usada para relatórios ou verificações específicas.
     *
     * @param saldo o saldo da conta
     * @return a {@link ContaUsuario} correspondente, ou null se não for encontrada
     */
    @Query("SELECT c FROM ContaUsuario c WHERE c.saldo = :saldo")
    List<ContaUsuario> encontrarContaPeloSaldo(@Param("saldo") BigDecimal saldo);

    /**
     * Busca uma conta com base no tipo de conta (ex: CORRENTE, POUPANÇA).
     *
     * @param tipoConta o tipo da conta em formato {@link String}
     * @return a {@link ContaUsuario} correspondente, ou null se não for encontrada
     */
    @Query("SELECT c FROM ContaUsuario c WHERE c.tipoConta = :tipo")
    List<ContaUsuario> encontrarPeloTipoDeConta(@Param("tipo") String tipoConta);
}
