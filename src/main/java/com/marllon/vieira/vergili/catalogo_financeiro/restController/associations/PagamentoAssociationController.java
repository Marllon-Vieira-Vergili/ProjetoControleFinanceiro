package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.PagamentoAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.PagamentoAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IPagamentos;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}