package com.marllon.vieira.vergili.catalogo_financeiro.restController.entities;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.ContaUsuarioInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/contaUsuario")
@Tag(name = "Métodos da conta de usuário, sem associação (apenas para teste")
public class ContaUsuarioController {



        @Autowired
        private ContaUsuarioInterface contaUsuarioInterface;


        @GetMapping(value = "/encontrarContaPorId/{id}")
        public ContaUsuarioResponse encontrarContaPorId(@PathVariable Long id){
            return contaUsuarioInterface.encontrarContaPorId(id);
        }

        @GetMapping(value = "/obterTodasContas")
        public List<ContaUsuarioResponse> obterTodasContas(){
            return contaUsuarioInterface.encontrarTodasContas();
        }

        @PostMapping(value = "/adicionarConta")
        public ContaUsuarioResponse adicionarConta(@RequestBody ContaUsuarioRequest conta){
            return contaUsuarioInterface.criarNovaConta(conta);
        }

        @PutMapping(value = "/atualizarConta/{id}")
        public ContaUsuarioResponse atualizarConta(@PathVariable Long id, @RequestBody ContaUsuarioRequest conta){
            return contaUsuarioInterface.atualizarConta(id,conta);
        }


        @DeleteMapping(value = "/removerConta/{id}")
    public boolean removerContaPorId(@PathVariable Long id){
            return contaUsuarioInterface.removerContaPorId(id);
        }
}
