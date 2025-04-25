package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.HistoricoTAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IHistoricoTransacao;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/historicosTransacoes")
@Tag(name = "Cruds para consultas de históricos de associações")
public class HistoricoTransacaoAssociationController {

    @Autowired
    private IHistoricoTransacao transacao;



    @GetMapping(value = "/encontrarTodosHistoricosTransacoes")
    public List<HistoricoTAssociationResponse> encontrarTodosHistoricosTransacoes(){
        return transacao.encontrarTodosHistoricosTransacoesAssociados();
    }

    @GetMapping(value = "/encontrarTransacaoPorId/{id}")
    public HistoricoTAssociationResponse encontrarTransacaoPorId(@PathVariable Long id){
        return transacao.encontrarHistoricoTransacaoAssociadaPorId(id);
    }
    @GetMapping(value = "/encontrarTransacaoPorData/{data}")
    public List<HistoricoTAssociationResponse> encontrarTransacaoPorData(@PathVariable LocalDate data){
        return transacao.encontrarHistoricoTransacaoPorData(data);
    }

    @GetMapping(value = "/encontrarTransacaoPorValor/{valorTransacao}")
    public List<HistoricoTAssociationResponse> encontrarTransacaoPorValor(@PathVariable BigDecimal valorTransacao){
        return transacao.encontrarHistoricosPorValor(valorTransacao);
    }

}