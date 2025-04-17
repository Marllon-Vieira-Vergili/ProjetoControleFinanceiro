package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.HistoricoTransacaoRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.TransacoesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/historicos_transacoes")
@Tag(name = "Métodos dos históricos de transações, sem associação (apenas para teste)")
public class TransacaoController {



    @Autowired
    private TransacoesService transacoesService;



    @GetMapping(value = "/encontrarTransacaoPorId/{id}")
    public HistoricoTransacaoResponse encontrarTransacaoPorId(@PathVariable Long id){
        return transacoesService.encontrarTransacaoPorId(id);
    }

    @GetMapping(value = "/obterTodosHistoricosTransacoes")
    public List<HistoricoTransacaoResponse> obterTodosHistoricoTransacao(){
        return transacoesService.encontrarTodasTransacoes();
    }



    @PostMapping(value = "/criarTransacao")
    public HistoricoTransacaoResponse criarNovaTransacao(@RequestBody HistoricoTransacaoRequest historicoTransacao){
        return transacoesService.criarNovoHistoricoTransacao(historicoTransacao);
    }


    @PutMapping(value = "/atualizarTransacao/{id}")
    public HistoricoTransacaoResponse atualizarTransacao(@PathVariable Long id, @RequestBody HistoricoTransacaoRequest historicoTransacao){
        return transacoesService.atualizarHistoricoTransacao(id, historicoTransacao);
    }


    @DeleteMapping(value = "/removerTransacao/{id}")
    public HistoricoTransacaoResponse removerHistoricoTransacaoPorId(@PathVariable Long id){
        return transacoesService.removerTransacaoPorId(id);
    }


}
