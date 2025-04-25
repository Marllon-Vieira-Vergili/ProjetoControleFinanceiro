package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.TransacoesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(value = "/api/historicos_transacoes")
@Tag(name = "Métodos dos históricos de transações")
public class TransacaoController {



    @Autowired
    private TransacoesService transacoesService;



    @GetMapping(value = "/encontrarTransacaoPorId/{id}")
    public HistoricoTransacao encontrarTransacaoPorId(@PathVariable Long id){
        return transacoesService.encontrarTransacaoPorId(id);
    }

    @GetMapping(value = "/encontrarTransacaoPorValor/{valor}")
    public List<HistoricoTransacao> encontrarTransacaoPeloValor(@PathVariable BigDecimal valor){
        return transacoesService.encontrarTransacaoPorValor(valor);
    }

    @GetMapping(value = "/obterTodosHistoricosTransacoes")
    public List<HistoricoTransacao> obterTodosHistoricoTransacao(){
        return transacoesService.encontrarTodasTransacoes();
    }



    @PostMapping(value = "/criarTransacao")
    public HistoricoTransacao criarNovaTransacao(@RequestBody HistoricoTransacaoRequest historicoTransacao){
        return transacoesService.criarNovoHistoricoTransacao(historicoTransacao);
    }


    @PutMapping(value = "/atualizarTransacao/{id}")
    public HistoricoTransacao atualizarTransacao(@PathVariable Long id, @RequestBody HistoricoTransacaoRequest historicoTransacao){
        return transacoesService.atualizarHistoricoTransacao(id, historicoTransacao);
    }


    @DeleteMapping(value = "/removerTransacao/{id}")
    public HistoricoTransacao removerHistoricoTransacaoPorId(@PathVariable Long id){
        return transacoesService.removerTransacaoPorId(id);
    }


}
