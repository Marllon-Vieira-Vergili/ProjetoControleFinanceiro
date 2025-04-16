package com.marllon.vieira.vergili.catalogo_financeiro.repository;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ContaUsuarioRepository extends JpaRepository<ContaUsuario, Long> {


    //Metodos customizados do repositório de ContaUsuario


    //Método para verificar se nome e tipoConta são iguais(Comparação)
    boolean existsByNomeIgnoreCaseAndTipoContaIgnoreCase(String nome, String tipoConta);




    //Método para procurar a conta pelo nome da mesma
    @Query("SELECT c FROM ContaUsuario c WHERE c.nome =: nome")
    ContaUsuario encontrarContaPeloNome(@Param("nome") String nome);


    //Método para procurar a conta pelo nome saldo da mesma
    @Query("SELECT c FROM ContaUsuario c WHERE c.saldo =: saldo")
    ContaUsuario encontrarContaPeloSaldo(@Param("saldo") BigDecimal saldo);

    //Método para procurar a conta pelo tipo de conta
    @Query("SELECT c FROM ContaUsuario c WHERE c.tipoConta =: tipo")
    ContaUsuario encontrarpeloTipoDeConta(@Param("tipo") String tipoConta);
}
