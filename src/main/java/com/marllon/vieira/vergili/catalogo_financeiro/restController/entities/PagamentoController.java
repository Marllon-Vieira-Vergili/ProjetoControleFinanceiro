package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.PagamentosRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.PagamentosResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.PagamentosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pagamento")
@Tag(name = "Métodos dos pagamentos, sem associação (apenas para teste)")
public class PagamentoController {



    @Autowired
    private PagamentosService pagamentosService;



    @GetMapping(value = "/encontrarPagamentoPorId/{id}")
    public PagamentosResponse encontrarPagamentoPorId(@PathVariable Long id){
        return pagamentosService.encontrarPagamentoPorId(id);
    }

    @GetMapping(value = "/obterTodosPagamentos")
    public List<PagamentosResponse> obterTodosPagamentos(){
        return pagamentosService.encontrarTodosPagamentos();
    }



    @PostMapping(value = "/adicionarPagamento")
    public PagamentosResponse adicionarPagamento(@RequestBody PagamentosRequest pagamento){
        return pagamentosService.criarNovoPagamento(pagamento);
    }


    @PutMapping(value = "/atualizarPagamento/{id}")
    public PagamentosResponse atualizarPagamento(@PathVariable Long id, @RequestBody PagamentosRequest pagamento){
        return pagamentosService.atualizarPagamento(id,pagamento);
    }


    @DeleteMapping(value = "/removerPagamento/{id}")
    public PagamentosResponse removerPagamentoPorId(@PathVariable Long id){
        return pagamentosService.removerPagamentoPorId(id);
    }


}
