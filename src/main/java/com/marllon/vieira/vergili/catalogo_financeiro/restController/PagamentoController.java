package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.PagamentosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pagamento")
@Tag(name = "Métodos dos pagamentos")
public class PagamentoController {



    @Autowired
    private PagamentosService pagamentosService;



    @GetMapping(value = "/encontrarPagamentoPorId/{id}")
    public Pagamentos encontrarPagamentoPorId(@PathVariable Long id){
        return pagamentosService.encontrarPagamentoPorId(id);
    }

    @GetMapping(value = "/encontrarPagamentoPorValor/{valor}")
    public List<Pagamentos> encontrarPagamentoPorId(@PathVariable BigDecimal valor){
        return pagamentosService.encontrarPagamentoPorValor(valor);
    }

    @GetMapping(value = "/obterTodosPagamentos")
    public List<Pagamentos> obterTodosPagamentos(){
        return pagamentosService.encontrarTodosPagamentos();
    }



    @PostMapping(value = "/adicionarPagamento")
    public Pagamentos adicionarPagamento(@RequestBody PagamentosRequest pagamento){
        return pagamentosService.criarNovoPagamento(pagamento);
    }


    @PutMapping(value = "/atualizarPagamento/{id}")
    public Pagamentos atualizarPagamento(@PathVariable Long id, @RequestBody PagamentosRequest pagamento){
        return pagamentosService.atualizarPagamento(id,pagamento);
    }


    @DeleteMapping(value = "/removerPagamento/{id}")
    public Pagamentos removerPagamentoPorId(@PathVariable Long id){
        return pagamentosService.removerPagamentoPorId(id);
    }


}
