package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/contaUsuario")
@Tag(name = "Métodos da conta de usuário, sem associação (apenas para teste)")
public class ContaUsuarioController {



        @Autowired
        private ContaUsuarioService contaUsuarioService;


        @GetMapping(value = "/encontrarContaPorId/{id}")
        public ContaUsuario encontrarContaPorId(@PathVariable Long id){
            return contaUsuarioService.encontrarContaPorId(id);
        }

    @GetMapping(value = "/encontrarContaPorNome/{nome}")
    public ContaUsuario encontrarContaPorNome(@PathVariable String nome){
        return contaUsuarioService.encontrarContaPorNome(nome);
    }

        @GetMapping(value = "/obterTodasContas")
        public List<ContaUsuario> obterTodasContas(){
            return contaUsuarioService.encontrarTodasContas();
        }

        @PostMapping(value = "/adicionarConta")
        public ContaUsuario adicionarConta(@RequestBody ContaUsuarioRequest conta){
            return contaUsuarioService.criarNovaConta(conta);
        }

        @PutMapping(value = "/atualizarConta/{id}")
        public ContaUsuario atualizarConta(@PathVariable Long id, @RequestBody ContaUsuarioRequest conta){
            return contaUsuarioService.atualizarConta(id,conta);
        }


        @DeleteMapping(value = "/removerConta/{id}")
    public boolean removerContaPorId(@PathVariable Long id){
            return contaUsuarioService.removerContaPorId(id);
        }
}
