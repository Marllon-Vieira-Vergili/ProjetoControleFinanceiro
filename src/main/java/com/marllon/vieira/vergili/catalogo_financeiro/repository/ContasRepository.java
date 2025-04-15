package com.marllon.vieira.vergili.catalogo_financeiro.repository;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Contas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContasRepository extends JpaRepository<Contas, Long> {


    //Metodos customizados do repositório de Contas


    //Método para procurar a conta pelo nome da mesma
    @Query("SELECT c FROM Contas c WHERE c.nome =: nome")
    Contas encontrarContaPeloNome(@Param("nome")Contas conta);


    //Método para procurar a conta pelo nome saldo da mesma
    @Query("SELECT c FROM Contas c WHERE c.saldo =: saldo")
    Contas encontrarContaPeloSaldo(@Param("saldo")Contas saldo);

    //Método para procurar a conta pelo tipo de conta
    @Query("SELECT c FROM Contas c WHERE c.tipoConta =: tipo")
    Contas encontrarpeloTipoDeConta(@Param("tipo")Contas tipoConta);
}
