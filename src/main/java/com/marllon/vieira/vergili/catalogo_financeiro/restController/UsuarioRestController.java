package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "CRUD de USUÀRIOS")
public class UsuarioRestController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping(value = "/encontrarUsuarioPorId/{id}")
    public ResponseEntity<UsuarioResponse> localizarUsuarioPelaId(@PathVariable Long id){

        if (usuariosService.encontrarUsuarioPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/listarContasDoUsuario/{id}")
    public ResponseEntity<UsuarioResponse> localizarContasAssociadas(@PathVariable Long id){

        return null;
    }

    @PostMapping(value = "/criarUsuario")
    public ResponseEntity<UsuarioResponse> criarNovoUsuario(@RequestBody UsuarioRequest request){

        return null;
    }

    @PutMapping(value = "/atualizarDadosUsuario/{id}")
    public ResponseEntity<UsuarioResponse> atualizarInformacaoUsuario(@PathVariable Long usuarioId,
                                                                      @RequestBody UsuarioRequest request){

        return null;
    }

    @DeleteMapping(value = "/removerUsuario/{id}")
    public ResponseEntity<UsuarioResponse> removerUsuarioPelaId(@PathVariable Long usuarioId){

        return null;
    }

    /*
    ✅ Fluxo dos endpoints:
Ação	Endpoint
Criar usuário	POST /usuarios
Listar contas do usuário	GET /usuarios/{id}/contas
Criar nova conta para o usuário	POST /usuarios/{id}/contas
Alterar ou excluir conta	PUT /contas/{id} ou DELETE /contas/{id}
     */
}
