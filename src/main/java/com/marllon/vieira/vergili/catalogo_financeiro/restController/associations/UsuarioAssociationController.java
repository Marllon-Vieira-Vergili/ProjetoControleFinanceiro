package com.marllon.vieira.vergili.catalogo_financeiro.restController.associations;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.UsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.UsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IUsuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuarioAssociados")
@Tag(name = "Métodos de associação do usuário")
public class UsuarioAssociationController {



    @Autowired
    private IUsuario usuario;



    @PostMapping(value = "/criarUsuarioAssociado")
    public UsuarioAssociationResponse criarNovoUsuarioEConta(@RequestBody UsuarioAssociationRequest novoUsuario){
        return usuario.criarEAssociarUsuario(novoUsuario);
    }

    @GetMapping(value = "/encontrarUsuarioPelaId/{id}")
    public UsuarioAssociationResponse encontrarUsuarioEcontasPorId(@PathVariable Long id){
        return usuario.encontrarUsuarioAssociadoPorId(id);

    }


    @GetMapping(value = "/encontrarUsuarioPorNome/{nome}")
    public List<UsuarioAssociationResponse> encontrarUsuarioPorNome(@PathVariable String nome){
        return usuario.encontrarUsuarioAssociadoPorNome(nome);
    }



    @GetMapping(value = "/obterTodosUsuariosEContas")
    public List<UsuarioAssociationResponse> obterTodosUsuarios(){
        return usuario.encontrarTodosUsuariosAssociados();
    }






    @PutMapping(value = "/atualizarUsuario/{id}")
    public UsuarioAssociationResponse atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest usuarioAtualizado){
        return usuario.atualizarUsuarioAssociado(id, usuarioAtualizado);
    }


    @DeleteMapping(value = "/removerUsuarioPorId/{id}")
    public void removerUsuarioPelaId(@PathVariable Long id){
        usuario.removerUsuarioAssociadoPelaId(id);
    }





}