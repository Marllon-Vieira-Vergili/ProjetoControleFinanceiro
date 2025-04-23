package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.ContaUsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.ContaUsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IContaUsuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/contasAssociadas")
@Tag(name = "Cruds de manipulação das contas associadas")
public class ContaUsuarioAssociationController {


    @Autowired
    private IContaUsuario contaUsuario;


    @PostMapping(value = "/adicionarContaUsuario")
    public ContaUsuarioAssociationResponse adicionarContaAUsuario
            (@RequestBody ContaUsuarioAssociationRequest novaConta) {
        return contaUsuario.criarEAssociarConta(novaConta);
    }


    @GetMapping(value = "/encontrarContaUsuario/{id}")
    public ContaUsuarioAssociationResponse encontrarUsuarioPorId
            (@PathVariable Long id){
        return contaUsuario.encontrarContaAssociadaPorId(id);
    }

    @GetMapping(value = "/encontrarTodasAsContas")
    public List<ContaUsuarioAssociationResponse> encontrarTodasAsContas(){
        return contaUsuario.encontrarTodasContasAssociadas();
    }

    @PutMapping(value = "/atualizarConta/{id}")
    public ContaUsuarioAssociationResponse atualizarConta(@PathVariable Long id,
                                                          @RequestBody ContaUsuarioRequest contaAtualizada){
        return contaUsuario.atualizarContaAssociada(id,contaAtualizada);
    }

    @DeleteMapping(value = "/removerConta/{id}")
    public void removerConta(@PathVariable Long id){
        contaUsuario.removerContaAssociadaPelaId(id);
    }
}
