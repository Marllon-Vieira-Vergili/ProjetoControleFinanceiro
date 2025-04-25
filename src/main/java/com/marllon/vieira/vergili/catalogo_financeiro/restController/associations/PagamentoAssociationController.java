package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/pagamentosAssociados")
@Tag(name = "Cruds de associação da parte de pagamentos")
public class PagamentoAssociationController {

    @Autowired
    private IPagamentos pagamentos;

    @PostMapping(value = "/criarPagamentosEassociacoes")
    public PagamentoAssociationResponse criarPagamentoJaAssociado
            (@RequestBody PagamentoAssociationRequest dadosPagamento){
        return pagamentos.criarEAssociarPagamento(dadosPagamento);
    }

    @GetMapping(value = "/encontrarTodosPagamentos")
    public List<PagamentoAssociationResponse> encontrarTodosPagamentos(){
        return pagamentos.encontrarTodosPagamentosAssociados();
    }

    @GetMapping(value = "/encontrarPagamentoPorId/{id}")
    public PagamentoAssociationResponse encontrarPagamentoPorId(@PathVariable Long id){
        return pagamentos.encontrarPagamentoAssociadoPorId(id);
    }

    @PutMapping(value = "/atualizarPagamentoPorId/{id}")
    public PagamentoAssociationResponse atualizarPagamentoPorId(@PathVariable Long id, @RequestBody
    PagamentoAssociationRequest dadosAtualizados){
        return pagamentos.atualizarPagamentoAssociado(id,dadosAtualizados);
    }
    @DeleteMapping(value = "/removerPagamentoPorId/{id}")
    public void removerPagamentoPorId(@PathVariable Long id){
        pagamentos.removerPagamentoAssociadoPelaId(id);
    }
}